package it.tika;


import flipdroid.grepper.EncodingDetector;
import it.tika.cases.Case;
import it.tika.cases.CaseRepositoryDBMongoDB;
import it.tika.exception.DBNotAvailableException;
import it.tika.exception.ExtractorException;
import it.tika.exception.URLRepoException;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;

public class URLAbstractResource extends ServerResource {

    private URLDBInterface getDB() {
        return URLDBMongoDB.getInstance();
    }

    public void setDB(URLDBInterface db) {
        //TODO: database implementation injection
    }

    private URLAbstract find() {

        URLAbstract result = null;
        Form form = this.getQuery();
        long start = 0;
        long end = 0;
        StopWatch sw = new StopWatch();
        try {
            String url = form.getFirst("url").getValue();
            boolean nocache = false;
            if (form.getFirst("nocache") != null) {
                nocache = Boolean.valueOf(form.getFirst("nocache").getValue());
            }

            String urlDecoded = java.net.URLDecoder.decode(url, "UTF-8");

            if (!nocache) {
                try {
                    sw.start("DB Cache Query");
                    result = getDB().find(urlDecoded);
                    sw.stopPrintReset();
                } catch (DBNotAvailableException e) {
                    getLogger().log(Level.INFO, e.getMessage(), e);
                }
            }


            if (result == null) {
                sw.start("Bytes Fetching");
                byte[] rawBytes = URLRawRepo.getInstance().fetch(urlDecoded);
                sw.stopPrintReset();
                sw.start("Charset Detection");
                String charset = EncodingDetector.detect(new BufferedInputStream(new ByteArrayInputStream(rawBytes)));
//                String charset = EncodingDetector.detect(urlDecoded);
                sw.stopPrintReset();
                Charset cs = null;
                if (charset != null)
                    cs = Charset.forName(charset);

                else {
                    try {
                        cs = Charset.forName("utf-8");
                    } catch (UnsupportedCharsetException e) {
                        // keep default
                    }
                }


                if (rawBytes == null) {
                    getLogger().log(Level.INFO, "Can't fetch document from url:" + urlDecoded);
                } else {
                    result = new URLAbstract(rawBytes, cs);
                    result.setUrl(url);
                    sw.start("Content Extraction");
                    result = WebpageExtractor.getInstance().extract(result);
                    sw.stopPrintReset();
                    if (result != null) {
                        try {
                            sw.start("Persist Result");
                            getDB().insert(result);
                            sw.stopPrintReset();
                        } catch (DBNotAvailableException e) {
                            getLogger().log(Level.INFO, e.getMessage(), e);
                        }
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            getLogger().log(Level.INFO, e.getMessage(), e);
        } catch (NullPointerException ne) {
            getLogger().log(Level.SEVERE, ne.getMessage(), ne);
        } catch (URLRepoException urle) {
            getLogger().log(Level.SEVERE, urle.getMessage(), urle);
        } catch (ExtractorException ee) {
            getLogger().log(Level.SEVERE, ee.getMessage(), ee);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }

    @Get("JSON")
    public String toJson() {
        Form form = this.getQuery();
        String isRating = form.getFirstValue("rate");
        if (isRating == null || isRating.length() == 0) {
            String result = "";
            URLAbstract urlAbstract = null;


            urlAbstract = find();


            if (urlAbstract == null) {
                getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                return null;
            }


            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.accumulate("title", urlAbstract.getTitle());
                jsonObject.accumulate("content", urlAbstract.getContent());
            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return writeJSON(jsonObject);
        } else {
            String correct = form.getFirstValue("tick");
            String json = form.getFirstValue("sample");
            String url = form.getFirstValue("url");
            int caseId = new Random().nextInt(99999999);

            Case sample = new Case();
            sample.setCreatedDate(new Date());
            sample.setUrl(url);
            sample.setGood(Boolean.parseBoolean(correct));
            sample.setSampleBody(json);
            sample.setId(caseId);
            try {
                CaseRepositoryDBMongoDB.getInstance().addCase(sample);
            } catch (DBNotAvailableException e) {
                getLogger().log(Level.INFO, e.getMessage(), e);
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("result", "succeed");
                jsonObject.accumulate("caseId", caseId);
            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return writeJSON(jsonObject);

        }
    }

    private String writeJSON(JSONObject jsonObject) {
        String result = "";

        JsonRepresentation representation = new JsonRepresentation(jsonObject);
        StringWriter writer = new StringWriter();
        try {
            representation.write(writer);
            result += writer.toString();
        } catch (IOException e) {
            getLogger().log(Level.INFO, e.getMessage(), e);
        }
        return result;
    }
}

class StopWatch {
    long start = 0;
    long end = 0;
    String event;

    public void start() {
        start = System.currentTimeMillis();
    }

    public void start(String event) {
        start();
        this.event = event;
    }

    public void stop() {
        end = System.currentTimeMillis();
    }

    public void stopPrintReset() {
        stop();
        report();
        reset();
    }

    public void reset() {
        start = 0;
        end = 0;
        event = null;
    }

    public void report() {
        System.out.println(event == null ? "No name event" : event + " cost " + (end - start) + "ms");
    }
}

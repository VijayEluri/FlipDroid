package com.goal98.flipdroid.model.featured;

import com.goal98.flipdroid.client.LastModifiedStampedResult;
import com.goal98.flipdroid.model.Article;
import com.goal98.flipdroid.model.RemoteArticleSource;
import com.goal98.flipdroid.model.cachesystem.BaseCacheableArticleSource;
import com.goal98.flipdroid.model.cachesystem.CacheToken;
import com.goal98.tika.common.TikaConstants;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 1/14/12
 * Time: 12:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class FeaturedArticleSource extends RemoteArticleSource {

    public FeaturedArticleSource(String remoteSourceToken, String sourceName, String sourceImage) {
        super(remoteSourceToken, sourceName, sourceImage);
    }

    public CacheToken getCacheToken() {
        CacheToken token = new CacheToken();
        token.setType(TikaConstants.TYPE_FEATURED);
        token.setToken(this.remoteSourceToken);
        return token;
    }

    protected void setSourceType(Article article) {
        article.setSourceType(TikaConstants.TYPE_FEATURED);
    }
}

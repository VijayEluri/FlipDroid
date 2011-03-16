/**
 * boilerpipe
 *
 * Copyright (c) 2009 Christian Kohlschütter
 *
 * The author licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.l3s.boilerpipe.filters.english;

import java.util.regex.Pattern;

import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.labels.DefaultLabels;

/**
 * Finds blocks which are potentially indicating the end of an article text and marks
 * them with {@link DefaultLabels#INDICATES_END_OF_TEXT}. This can be used in conjunction
 * with a downstream {@link IgnoreBlocksAfterContentFilter}. 
 * 
 * @author Christian Kohlschütter
 * @see IgnoreBlocksAfterContentFilter
 */
public class TerminatingBlocksFinder implements BoilerpipeFilter {
    public static final TerminatingBlocksFinder INSTANCE = new TerminatingBlocksFinder();

    /**
     * Returns the singleton instance for TerminatingBlocksFinder.
     */
    public static TerminatingBlocksFinder getInstance() {
        return INSTANCE;
    }
    
    private static final Pattern N_COMMENTS = Pattern.compile("(?msi)^[0-9]+ (Comments|users responded in)");

    public boolean process(TextDocument doc)
            throws BoilerpipeProcessingException {
        boolean changes = false;

        for (TextBlock tb : doc.getTextBlocks()) {
            if (tb.getNumWords() < 15) {
                final String text = tb.getText().trim();
                if (text.startsWith("Comments")
                        || N_COMMENTS.matcher(text).find()
                        || text.contains("What you think...")
                        || text.contains("add your comment")
                        || text.contains("Add your comment")
                        || text.contains("Add Your Comment")
                        || text.contains("Add Comment")
                        || text.contains("Reader views")
                        || text.contains("相关阅读")
                        || text.contains("相关新闻")
                        || text.contains("Have your say")
                        || text.contains("Have Your Say")
                        || text.contains("Reader Comments")
                        || text.equals("Thanks for your comments - this feedback is now closed")
                        || text.startsWith("© Reuters")
                        || text.startsWith("Please rate this")
                        || text.startsWith("声明")
                        || text.contains("评论")
                        || text.matches("到第[0-9 ]*页")
                        || (text.contains("回复") && tb.getLinkDensity() != 0)
                        || text.contains("相关")
                        || text.contains("热点")
                        || text.contains("版权所有")
                        || text.contains("推荐")
                        || text.contains("标签")
                        || text.contains("分类")
                        || (text.contains("点击观看") && tb.getLinkDensity() != 0)
                        || (text.contains("注册") && tb.getLinkDensity() != 0)
                        || text.contains("首页")
                        || text.contains("今日关注")
                        || text.contains("来源")
                        || text.contains("作者")
                        || text.contains("时间")
                        || text.contains("分享")
                        || text.contains("扩展阅读")
                        || text.contains("赞")
                        || text.contains("更多")
                        || text.contains("分享到")
                        || text.toUpperCase().contains("RELATED TOPICS")
                        || text.toUpperCase().contains("SHARE IT ON")
                        || text.toUpperCase().contains("SHARE THIS ON")
                ) {
                    tb.addLabel(DefaultLabels.INDICATES_END_OF_TEXT);
                    changes = true;
                }
            }
        }

        return changes;
    }

}
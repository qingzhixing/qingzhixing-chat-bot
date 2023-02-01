package org.qingzhixing.MessageContentHandlers;

import org.qingzhixing.Keywords.Keyword;
import org.qingzhixing.Keywords.KeywordsLoader;

import java.util.PriorityQueue;

public class KeywordReplyGroupPlainText extends AbstractGroupPlainTextHandler {
    PriorityQueue<Keyword> keywords;

    public KeywordReplyGroupPlainText() {
        super();
        keywords = new KeywordsLoader("/keywords.xml").keywords();
    }

    //成功匹配则中断，返回true
    @Override
    public boolean Handle() {
        var text = getPlainTextContent();
        for (var keyword : keywords) {
            if (text.contains(keyword.keyword().value())) {
                group().sendMessage(keyword.DoReply());
                return true;
            }
        }
        return false;
    }
}

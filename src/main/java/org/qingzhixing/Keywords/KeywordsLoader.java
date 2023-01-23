package org.qingzhixing.Keywords;

import javafx.util.Pair;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;
import org.qingzhixing.Utilities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class KeywordsLoader {
    private static final Logger logger = Logger.getLogger(KeywordsLoader.class);
    private final PriorityQueue<Keyword> keywords;

    KeywordsLoader(@NotNull String keywordsFilePath) {
        keywords = new PriorityQueue<>(Comparator.reverseOrder());
        ParseKeywordsFile(keywordsFilePath);
    }

    private Pair<String, Integer> ParseTagAndGetWeightFromElement(@NotNull Element element, @NotNull String target) {
        var targetElement = element.getChild(target);
        if (targetElement == null) {
            logger.error("错误数据:不存在 \"" + target + "\" 标签");
            return null;
        }
        var targetText = targetElement.getText();
        var weightAttribute = targetElement.getAttribute("weight");
        var weight = 0;
        if (weightAttribute == null) {
            weight = 1;
        } else {
            weight = Integer.parseInt(weightAttribute.getValue());
        }
        return new Pair<String, Integer>(targetText, weight);
    }

    private List<Keyword.InnerData> ParseRepliesFromKeywordDataElement(@NotNull Element keywordDataElement) {
        List<Element> repliesElements = keywordDataElement.getChildren("replies");
        if (repliesElements == null) {
            logger.warn("不存在replies标签，数据为非法，忽略该标签");
            return null;
        }
        var replies = new ArrayList<Keyword.InnerData>();
        repliesElements.forEach(replyElement -> {
            var replyPair = ParseTagAndGetWeightFromElement(replyElement, "reply");
            if (replyPair == null || replyPair.getKey().equals("")) {
                logger.warn("reply标签不存在或为空,忽略该reply");
                return;
            }
            var replyText = replyPair.getKey();
            var replyWeight = replyPair.getValue();
            replies.add(new Keyword.InnerData(replyText, replyWeight));
        });
        if (replies.size() == 0) {
            logger.warn("该keywordData中不存在合法reply");
            return null;
        }
        return replies;
    }


    private void ParseKeywordsFile(@NotNull String keywordsFilePath) {
        URL keywordsURL;
        try {
            keywordsURL = Utilities.GetCurrentJarResourceURL(keywordsFilePath);
        } catch (RuntimeException e) {
            logger.error("Unable to load keywords file.Reason:" + e.getMessage() + e);
            return;
        }

        var builder = new SAXBuilder();
        try {
            var document = builder.build(keywordsURL);
            var rootElement = document.getRootElement();

            List<Element> keywordDatas = rootElement.getChildren("keywordData");
            if (keywordDatas == null) {
                logger.warn("警告！不存在keywordData标签，将不会有关键词回答！");
                return;
            }
            keywordDatas.forEach(keywordData -> {
                var keywordPair = ParseTagAndGetWeightFromElement(keywordData, "keyword");
                if (keywordPair == null || keywordPair.getKey().equals("")) {
                    logger.warn("keyword标签不存在或为空,忽略该keywordData");
                    return;
                }
                var keywordText = keywordPair.getKey();
                var keywordWeight = keywordPair.getValue();

                var replies = ParseRepliesFromKeywordDataElement(keywordData);
                if (replies == null) {
                    logger.warn("replies标签不存在或所有子reply非法，忽略该keywordData");
                    return;
                }

                keywords.add(new Keyword(new Keyword.InnerData(keywordText, keywordWeight), replies));
            });
        } catch (IOException | JDOMException e) {
            throw new RuntimeException(e);
        }
    }
}

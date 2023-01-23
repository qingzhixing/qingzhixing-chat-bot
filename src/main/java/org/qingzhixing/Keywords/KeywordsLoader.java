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

    public KeywordsLoader(@NotNull String keywordsFilePath) {
        keywords = new PriorityQueue<>(Comparator.reverseOrder());
        ParseKeywordsFile(keywordsFilePath);
    }

    public PriorityQueue<Keyword> keywords() {
        return keywords;
    }

    //非法返回null
    private List<Pair<String, Integer>> ParseTagAndGetWeightFromElement(@NotNull Element element, @NotNull String tag) {
        logger.debug("解析tag中:" + tag);
        List<Element> tagElements = element.getChildren(tag);
        if (tagElements.isEmpty()) {
            logger.error("错误数据:不存在 \"" + tag + "\" 标签");
            return null;
        }
        ArrayList<Pair<String, Integer>> answer = new ArrayList<>();

        tagElements.forEach(tagElement -> {
            var tagText = tagElement.getText().trim();
            var weightAttribute = tagElement.getAttribute("weight");
            var weight = 0;
            if (weightAttribute == null) {
                weight = 1;
            } else {
                weight = Integer.parseInt(weightAttribute.getValue());
            }
            logger.info("tagText: \"" + tagText + "\" weight: " + weight);
            answer.add(new Pair<>(tagText, weight));
        });
        return answer;
    }

    //非法返回null
    private List<Keyword.InnerData> ParseRepliesFromKeywordDataElement(@NotNull Element keywordDataElement) {
        logger.info("解析replies中");
        var repliesElement = keywordDataElement.getChild("replies");
        if (repliesElement == null) {
            logger.warn("不存在replies标签，数据为非法，忽略该标签");
            return null;
        }
        var replies = new ArrayList<Keyword.InnerData>();
        var replyPairs = ParseTagAndGetWeightFromElement(repliesElement, "reply");
        if (replyPairs == null) {
            logger.error("没有找到任何reply标签，该replies标签非法，解析该关键词失败");
            return null;
        }
        replyPairs.forEach(replyPair -> {
            if (replyPair.getKey().equals("")) {
                logger.warn("该reply为空,忽略该reply");
                return;
            }
            var replyText = replyPair.getKey();
            var replyWeight = replyPair.getValue();
            logger.info("解析到reply:\"" + replyText + "\" replyWeight:" + replyWeight);
            replies.add(new Keyword.InnerData(replyText, replyWeight));

        });
        if (replies.size() == 0) {
            logger.warn("该keywordData中不存在合法reply");
            return null;
        }
        return replies;
    }


    private void ParseKeywordsFile(@NotNull String keywordsFilePath) {
        logger.info("解析关键词文件中...");
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
            if (keywordDatas.isEmpty()) {
                logger.warn("警告！不存在keywordData标签，将不会有关键词回答！");
                return;
            }
            keywordDatas.forEach(keywordData -> {
                logger.info("正在解析一个keywordData标签");
                var keywordPairs = ParseTagAndGetWeightFromElement(keywordData, "keyword");
                Pair<String, Integer> keywordPair;
                if (keywordPairs == null) {
                    logger.warn("keyword标签不存在,忽略该keywordData");
                    return;
                }
                keywordPair = keywordPairs.get(0);
                if (keywordPair.getKey().equals("")) {
                    logger.warn("keyword标签为空,忽略该keywordData");
                    return;
                }
                var keywordText = keywordPair.getKey();
                var keywordWeight = keywordPair.getValue();
                logger.info("解析到keyword: \"" + keywordText + "\" weight: " + keywordWeight);

                var replies = ParseRepliesFromKeywordDataElement(keywordData);
                if (replies == null) {
                    logger.warn("replies标签不存在或所有子reply非法，忽略该keywordData");
                    return;
                }

                var keywordObject = new Keyword(new Keyword.InnerData(keywordText, keywordWeight), replies);
                keywords.add(keywordObject);
                logger.info("解析到Keyword对象:\n" + keywordObject);
            });
        } catch (IOException | JDOMException e) {
            throw new RuntimeException(e);
        }
    }
}

package org.qingzhixing.CommandMatchers;

public class Matcher_QuestionAnswer extends AbstractMatcher {
    public Matcher_QuestionAnswer() {
        super();
        this.setMode(MatchMode.CUSTOM)
                .setCommandName("question-answer")
                .setDescription("[@bot] <你问的问题 - 句子中存在'?','？','吗','嘛'> - 返回机器人的有趣回答,不艾特有10%几率自动回答");
    }

    @Override
    protected void Handle() {
        var workedText = originalText();
        workedText = workedText.replace("？", "");
        workedText = workedText.replace("?", "");
        workedText = workedText.replace("嘛", "");
        workedText = workedText.replace("吗", "");
        //检测到问题则输出对应sb答案
        if (!workedText.equals(originalText()) && !workedText.equals("")) {
            //格式化
            workedText = workedText.trim();
//            logger.debug("获取到问题: " + originalText + "\n回答: " + workedText);
            contact().sendMessage(workedText);
        }
    }

    @Override
    protected boolean Match() {
        //没有AtBot则90%概率不回答
        if (!isAtBot() && Math.random() < 0.9) {
            return false;
        }
        //否则50几率回答
        if (Math.random() < 0.5) {
            return false;
        }
        return originalText().contains("?") || originalText().contains("？") ||
                originalText().contains("吗") || originalText().contains("嘛");
    }
}

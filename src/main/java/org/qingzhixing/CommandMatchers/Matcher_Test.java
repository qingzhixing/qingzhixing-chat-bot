package org.qingzhixing.CommandMatchers;

import net.mamoe.mirai.message.data.PlainText;

public class Matcher_Test extends AbstractMatcher {
    public Matcher_Test() {
        super();
        this.setCommandName("test")
                .setDescription("此功能仅用作开发者测试，会得到什么回答不确定哟~")
                .setMode(MatchMode.START)
                .setNeedOnlyAtBot(true);
    }

    @Override
    protected void Handle() {
        QuoteThenReply(new PlainText("引用回复测试"));
    }
}

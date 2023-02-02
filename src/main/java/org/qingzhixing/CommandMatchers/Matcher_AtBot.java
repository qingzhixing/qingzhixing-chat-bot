package org.qingzhixing.CommandMatchers;

import net.mamoe.mirai.message.data.PlainText;

public class Matcher_AtBot extends AbstractMatcher {
    public Matcher_AtBot() {
        super();
        this.setCommandName("at-bot")
                .setDescription("[@bot] - 别艾特我啦！不止At bot有50%几率回复，否则为10%")
                .setNeedAtBot(false)
                .setMode(MatchMode.CUSTOM);
    }

    @Override
    protected void Handle() {
        //多at单at不同回复
        if (!isOnlyAtBot()) {
            AtThenReply(new PlainText("  你干嘛！不许艾特我啦！！🤯"));
        } else {
            AtThenReply(new PlainText("  艾特我干嘛呀！！！！"));
        }
    }

    @Override
    public boolean Match() {
        if (!isAtBot()) return false;
        if (!isOnlyAtBot()) return Math.random() <= 0.1;
        return Math.random() <= 0.5;
    }
}

package org.qingzhixing.CommandMatchers;

import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Matcher_Help extends AbstractMatcher {
    private final List<AbstractMatcher> matchers;

    public Matcher_Help(@NotNull List<AbstractMatcher> matchers) {
        super();
        this.setCommandName("help")
                .setDescription("帮助。后面加指令名称如 '/help my-info' 会显示该指令的描述，否则返回所有指令的描述")
                .setNeedOnlyAtBot(true);
        this.matchers = matchers;
    }

    @Override
    protected void Handle() {
        var remainingString = originalText().replaceFirst(GetCommandString(), "").trim();
        if (remainingString.equals("")) {
            AtomicReference<String> replyStringAtom = new AtomicReference<>("");
            matchers.forEach(matcher -> {
                replyStringAtom.set(replyStringAtom.get() + matcher.GetUsage() + '\n');
            });
            AtThenReply(new PlainText('\n' + replyStringAtom.get()));
            return;
        }
        var replyString = "";
        for (var matcher : matchers) {
            if (matcher.commandName().equals(remainingString)) {
                replyString = matcher.GetUsage();
                break;
            }
        }
        if (replyString.equals("")) replyString = "没有找到指令: '" + replyString + "'";
        AtThenReply(new PlainText('\n' + replyString));
    }
}

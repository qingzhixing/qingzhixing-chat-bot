package org.qingzhixing.MessageContentHandlers;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageContent;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGroupMessageContentHandler extends AbstractMessageContentHandler {
    private Group group;

    protected AbstractGroupMessageContentHandler() {
        super();
    }

    public Group group() {
        return group;
    }

    protected void BindContext(@NotNull Member sender, @NotNull MessageContent content, @NotNull MessageChain originalMessageChain, @NotNull Group group) {
        super.BindContext(sender, content, originalMessageChain);
        this.group = group;
    }
}

package org.qingzhixing.EventListeners.MessageContentHandlers.GroupMessageContentHandlers;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import org.qingzhixing.EventListeners.MessageContentHandlers.AbstractMessageContentHandler;

public abstract class AbstractGroupMessageContentHandler extends AbstractMessageContentHandler {
    private Group group;

    protected AbstractGroupMessageContentHandler(Friend masterFriend) {
        super(masterFriend);
    }

    public Group group() {
        return group;
    }

    protected void BindContext(@NotNull Member sender, @NotNull PlainText plainText, @NotNull Group group) {
        super.BindContext(sender, plainText);
        this.group = group;
    }
}

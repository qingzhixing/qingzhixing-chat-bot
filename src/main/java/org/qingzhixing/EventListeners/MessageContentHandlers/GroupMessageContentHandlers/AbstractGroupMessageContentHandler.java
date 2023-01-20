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

    public void BindContext(@NotNull Member sender, @NotNull PlainText plainText, @NotNull Group group) {
        super.BindContext(sender, plainText);
        this.group = group;
    }

    /*
     * @return 返回 true 为 中断事件处理，事件不再往后传播给其他Handler
     */
    public boolean BindContextAndHandle(@NotNull Member sender, @NotNull PlainText plainText, @NotNull Group group) {
        BindContext(sender, plainText, group);
        return Handle();
    }
}

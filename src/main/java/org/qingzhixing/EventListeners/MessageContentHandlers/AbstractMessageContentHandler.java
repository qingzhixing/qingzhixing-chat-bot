package org.qingzhixing.EventListeners.MessageContentHandlers;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.Message;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMessageContentHandler {
    private final Friend masterFriend;
    private Member sender;
    private Message message;

    protected AbstractMessageContentHandler(Friend masterFriend) {
        this.masterFriend = masterFriend;
    }

    public Member sender() {
        return sender;
    }

    public Message message() {
        return message;
    }

    public Friend masterFriend() {
        return masterFriend;
    }

    public boolean isMasterFriendExits() {
        return masterFriend != null;
    }

    public void BindContext(@NotNull Member sender, @NotNull Message message) {
        this.sender = sender;
        this.message = message;
    }

    /*
     * @return 返回 true 为 中断事件处理，事件不再往后传播给其他Handler
     */
    abstract public boolean Handle();

    /*
     * @return 返回 true 为 中断事件处理，事件不再往后传播给其他Handler
     */
    public boolean BindContextAndHandle(@NotNull Member sender, @NotNull Message message) {
        BindContext(sender, message);
        return Handle();
    }
}

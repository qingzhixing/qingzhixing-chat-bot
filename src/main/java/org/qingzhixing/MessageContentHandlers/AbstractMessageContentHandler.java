package org.qingzhixing.MessageContentHandlers;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMessageContentHandler {
    private final Friend masterFriend;
    private Member sender;
    private MessageContent content;

    private MessageChain originalMessageChain;

    protected AbstractMessageContentHandler(Friend masterFriend) {
        this.masterFriend = masterFriend;
    }

    protected static void AtThenReply(@NotNull Message originalMessage, @NotNull Member atTarget, @NotNull Contact sendTarget) {
        var newChain = MessageUtils.newChain(
                new At(atTarget.getId()),
                originalMessage
        );
        sendTarget.sendMessage(newChain);
    }

    protected static void QuoteThenReply(@NotNull Message originalMessage, @NotNull MessageChain originalMessageChain, @NotNull Contact sendTarget) {
        var newChain = MessageUtils.newChain(
                new QuoteReply(originalMessageChain),
                originalMessage
        );
        sendTarget.sendMessage(newChain);
    }

    public MessageChain originalMessageChain() {
        return originalMessageChain;
    }

    public Member sender() {
        return sender;
    }

    public Message content() {
        return content;
    }

    public Friend masterFriend() {
        return masterFriend;
    }

    public boolean isMasterFriendExits() {
        return masterFriend != null;
    }

    protected void BindContext(@NotNull Member sender, @NotNull MessageContent content, @NotNull MessageChain originalMessageChain) {
        this.sender = sender;
        this.content = content;
        this.originalMessageChain = originalMessageChain;
    }

    /*
     * @return 返回 true 为 中断事件处理，事件不再往后传播给其他Handler
     */
    abstract public boolean Handle();
}

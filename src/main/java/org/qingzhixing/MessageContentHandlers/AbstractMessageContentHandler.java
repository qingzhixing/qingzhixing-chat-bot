package org.qingzhixing.MessageContentHandlers;

import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageContent;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMessageContentHandler {
    private Member sender;
    private MessageContent content;

    private MessageChain originalMessageChain;

    protected AbstractMessageContentHandler() {

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

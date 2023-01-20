package org.qingzhixing.EventListeners.MessageContentHandlers;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGroupPlainTextHandler extends AbstractGroupMessageContentHandler {
    private boolean isAtBot;
    private boolean isOnlyAtBot;

    protected AbstractGroupPlainTextHandler(Friend masterFriend) {
        super(masterFriend);
    }

    public boolean isAtBot() {
        return isAtBot;
    }

    public boolean isOnlyAtBot() {
        return isOnlyAtBot;
    }

    public void BindContext(@NotNull Member sender, @NotNull PlainText plainText, @NotNull Group group, boolean atBot, boolean isOnlyAtBot) {
        super.BindContext(sender, plainText, group);
        this.isAtBot = atBot;
        this.isOnlyAtBot = isOnlyAtBot;
    }

    protected PlainText plainText() {
        return (PlainText) message();
    }

    protected String getPlainTextContent() {
        return plainText().getContent();
    }

    /*
     * @return 返回 true 为 中断事件处理，事件不再往后传播给其他Handler
     */
    public boolean BindContextAndHandle(@NotNull Member sender, @NotNull PlainText plainText, @NotNull Group group, boolean atBot, boolean isOnlyAtBot) {
        BindContext(sender, plainText, group, atBot, isOnlyAtBot);
        return Handle();
    }

    protected void AtThenReply(@NotNull Message message, @NotNull Member atTarget, @NotNull Contact sendTarget) {
        var newChain = MessageUtils.newChain(
                new At(atTarget.getId()),
                message
        );
        sendTarget.sendMessage(newChain);
    }
}

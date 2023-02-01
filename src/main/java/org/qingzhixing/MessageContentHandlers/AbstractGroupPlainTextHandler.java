package org.qingzhixing.MessageContentHandlers;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGroupPlainTextHandler extends AbstractGroupMessageContentHandler {
    private boolean isAtBot;
    private boolean isOnlyAtBot;

    protected AbstractGroupPlainTextHandler() {
        super();
    }

    public boolean isAtBot() {
        return isAtBot;
    }

    public boolean isOnlyAtBot() {
        return isOnlyAtBot;
    }

    public void BindContext(@NotNull Member sender, @NotNull PlainText plainText, @NotNull MessageChain originalMessageChain, @NotNull Group group, boolean atBot, boolean isOnlyAtBot) {
        super.BindContext(sender, plainText, originalMessageChain, group);
        this.isAtBot = atBot;
        this.isOnlyAtBot = isOnlyAtBot;
    }

    protected PlainText plainText() {
        return (PlainText) content();
    }

    protected String getPlainTextContent() {
        return plainText().getContent();
    }

    /*
     * @return 返回 true 为 中断事件处理，事件不再往后传播给其他Handler
     */
    public boolean BindContextAndHandle(@NotNull Member sender, @NotNull PlainText plainText, @NotNull MessageChain originalMessageChain, @NotNull Group group, boolean atBot, boolean isOnlyAtBot) {
        BindContext(sender, plainText, originalMessageChain, group, atBot, isOnlyAtBot);
        return Handle();
    }


}

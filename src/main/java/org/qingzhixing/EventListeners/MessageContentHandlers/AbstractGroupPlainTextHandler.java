package org.qingzhixing.EventListeners.MessageContentHandlers;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGroupPlainTextHandler extends AbstractGroupMessageContentHandler {
    private boolean atBot;

    protected AbstractGroupPlainTextHandler(Friend masterFriend) {
        super(masterFriend);
    }

    public boolean isAtBot() {
        return atBot;
    }

    public void BindContext(@NotNull Member sender, @NotNull PlainText plainText, @NotNull Group group, boolean atBot) {
        super.BindContext(sender, plainText, group);
        this.atBot = atBot;
    }

    protected PlainText getPlainText() {
        return (PlainText) message();
    }

    /*
     * @return 返回 true 为 中断事件处理，事件不再往后传播给其他Handler
     */
    public boolean BindContextAndHandle(@NotNull Member sender, @NotNull PlainText plainText, @NotNull Group group, boolean atBot) {
        BindContext(sender, plainText, group, atBot);
        return Handle();
    }
}

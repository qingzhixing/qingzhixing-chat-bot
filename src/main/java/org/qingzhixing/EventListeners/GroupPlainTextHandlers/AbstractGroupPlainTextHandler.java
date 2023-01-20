package org.qingzhixing.EventListeners.GroupPlainTextHandlers;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGroupPlainTextHandler {
    protected PlainText plainText;
    protected boolean atBot;
    protected Member sender;
    protected Group group;

    public final void BindContext(@NotNull PlainText plainText, boolean atBot, @NotNull Member sender, @NotNull Group group) {
        this.plainText = plainText;
        this.atBot = atBot;
        this.sender = sender;
        this.group = group;
    }

    /*
     * @return 返回 true 为 中断事件处理，事件不再往后传播给其他Handler
     */
    abstract public boolean Handle();

    /*
     * @return 返回 true 为 中断事件处理，事件不再往后传播给其他Handler
     */
    public final boolean BindContextAndHandle(@NotNull PlainText plainText, boolean atBot, @NotNull Member sender, @NotNull Group group) {
        BindContext(plainText, atBot, sender, group);
        return Handle();
    }
}

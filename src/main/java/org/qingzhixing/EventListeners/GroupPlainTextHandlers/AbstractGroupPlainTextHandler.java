package org.qingzhixing.EventListeners.GroupPlainTextHandlers;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGroupPlainTextHandler {
    //true 为 中断事件处理，事件不再往后传播给其他Handler
    public abstract boolean HandleGroupPlainText(@NotNull PlainText plainText, boolean atBot, @NotNull Member sender, @NotNull Group group);
}

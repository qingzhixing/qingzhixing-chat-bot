package org.qingzhixing.EventListeners;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.FlashImage;
import net.mamoe.mirai.message.data.MessageContent;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.qingzhixing.Utilities;

public class SimpleEventListener extends SimpleListenerHost {
    private static final Logger logger = Logger.getLogger(SimpleEventListener.class);
    private final Friend masterFriend;

    public SimpleEventListener(Friend master) {
        this.masterFriend = master;
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        super.handleException(context, exception);
        logger.error("在处理会话时出现异常:");
        System.out.println("Context: " + context);
        System.out.println("Exception: " + exception);
    }

    @EventHandler
    public void onMessage(@NotNull MessageEvent event) {
        var sender = event.getSender();
        var messageChain = event.getMessage();
        boolean atFlag = Utilities.CheckMessageChainAt(messageChain);
        messageChain.forEach(message -> {
            if (!(message instanceof MessageContent)) return;
            MessageContent messageContent = (MessageContent) message;
            if (messageContent instanceof FlashImage) {
                if (masterFriend == null) return;
                var commonImage = ((FlashImage) messageContent).getImage();
                masterFriend.sendMessage("截获闪照:");
                masterFriend.sendMessage(commonImage);
                masterFriend.sendMessage("发送者 昵称:\"" + sender.getNick() + "\" QQID:" + sender.getId());
            } else if (messageContent instanceof PlainText && atFlag) {

            }
        });
    }
}

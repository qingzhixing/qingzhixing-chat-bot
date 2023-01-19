package org.qingzhixing.EventListeners;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
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
    public void onGroupMessage(@NotNull GroupMessageEvent event) {
        var sender = event.getSender();
        var group = event.getGroup();
        var messageChain = event.getMessage();
        var bot = event.getBot();

        boolean atFlag = Utilities.CheckMessageChainAtUser(messageChain, bot.getAsFriend());
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
                var text = ((PlainText) messageContent).getContent();
                Image avatar = Utilities.URLToImage(sender.getAvatarUrl(), bot);
                if (text.contains("我是谁")) {
                    MessageChain replyChain = MessageUtils.newChain(
                            new PlainText("头像:"),
                            avatar,
                            new PlainText("昵称: \"" + sender.getNick() + "\"\n"),
                            new PlainText("ID: " + sender.getId())
                    );
                    group.sendMessage(replyChain);
                }
            }
        });
    }
}

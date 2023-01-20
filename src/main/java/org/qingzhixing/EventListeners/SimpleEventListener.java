package org.qingzhixing.EventListeners;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
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

    /*
     * 处理所有闪照Message
     */
    private void HandleFlashImageMessage(@NotNull FlashImage flashImage, @NotNull Member sender) {
        if (masterFriend == null) return;
        var commonImage = flashImage.getImage();
        masterFriend.sendMessage("截获闪照:");
        masterFriend.sendMessage(commonImage);
        masterFriend.sendMessage("发送者 昵称:\"" + sender.getNick() + "\" QQID:" + sender.getId());
    }

    private void HandleGroupPlainText(@NotNull PlainText plainText, boolean atBot, @NotNull Member sender, @NotNull Group group) {
        var text = plainText.getContent();

        if (atBot && text.contains("我是谁")) {
            Image avatar = null;
            try {
                avatar = Utilities.URLToImage(sender.getAvatarUrl(), sender.getBot());
            } catch (RuntimeException e) {
                var logErrorMessage = " 获取图片出错啦TT！";
                logger.error(logErrorMessage);
                var replyChain = MessageUtils.newChain(
                        new At(sender.getId()),
                        new PlainText(logErrorMessage)
                );
                group.sendMessage(replyChain);
                //通知master
                if (masterFriend != null) {
                    masterFriend.sendMessage("" +
                            "用户使用指令'我是谁'时出现错误!" +
                            "\n用户昵称:" + sender.getNick() +
                            "\n用户ID:" + sender.getId() +
                            "\n群名:" + group.getName() +
                            "\n群号:" + group.getId() +
                            "\n错误原因:" + logErrorMessage
                    );
                }
                return;
            }
            MessageChain replyChain = MessageUtils.newChain(
                    new At(sender.getId()),
                    new PlainText(" 头像:"),
                    avatar,
                    new PlainText("昵称: \"" + sender.getNick() + "\"\n"),
                    new PlainText("ID: " + sender.getId())
            );
            group.sendMessage(replyChain);
        }
    }

    @EventHandler
    public void onGroupMessage(@NotNull GroupMessageEvent event) {
        var sender = event.getSender();
        var group = event.getGroup();
        var messageChain = event.getMessage();
        var bot = event.getBot();

        boolean atBot = Utilities.CheckMessageChainAtUser(messageChain, bot.getAsFriend(), group);
        messageChain.forEach(message -> {
            if (!(message instanceof MessageContent)) return;
            MessageContent messageContent = (MessageContent) message;
            if (messageContent instanceof FlashImage) {
                HandleFlashImageMessage((FlashImage) messageContent, sender);
            } else if (messageContent instanceof PlainText) {
                HandleGroupPlainText((PlainText) message, atBot, sender, group);
            }
        });
    }
}

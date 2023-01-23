package org.qingzhixing.EventListeners;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.FlashImage;
import net.mamoe.mirai.message.data.MessageContent;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.qingzhixing.MessageContentHandlers.AbstractGroupPlainTextHandler;
import org.qingzhixing.MessageContentHandlers.CommandGroupPlainTextHandler;
import org.qingzhixing.Utilities;

import java.util.ArrayList;

public class SimpleEventListener extends SimpleListenerHost {
    private static final Logger logger = Logger.getLogger(SimpleEventListener.class);
    private final Friend masterFriend;
    private final ArrayList<AbstractGroupPlainTextHandler> groupPlainTextHandlers;

    public SimpleEventListener(Friend master) {
        this.masterFriend = master;
        groupPlainTextHandlers = new ArrayList<>();
        //添加的顺序表示优先级
        groupPlainTextHandlers.add(new CommandGroupPlainTextHandler(masterFriend));
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

    private void HandleGroupPlainText(@NotNull Member sender, @NotNull PlainText plainText, @NotNull Group group, boolean atBot, boolean isOnlyAtBot) {
        for (var handler : groupPlainTextHandlers) {
            if (handler.BindContextAndHandle(sender, plainText, group, atBot, isOnlyAtBot)) return;
        }
    }

    @EventHandler
    public void onGroupMessage(@NotNull GroupMessageEvent event) {
        var sender = event.getSender();
        var group = event.getGroup();
        var messageChain = event.getMessage();
        var bot = event.getBot();

        boolean isAtBot = Utilities.CheckMessageChainAtUser(messageChain, bot.getAsFriend(), group);
        boolean isOnlyAtBot = Utilities.CheckMessageChainOnlyAtUser(messageChain, bot.getAsFriend(), group);
        messageChain.forEach(message -> {
            if (!(message instanceof MessageContent)) return;
            MessageContent messageContent = (MessageContent) message;
            if (messageContent instanceof FlashImage) {
                HandleFlashImageMessage((FlashImage) messageContent, sender);
            } else if (messageContent instanceof PlainText) {
                HandleGroupPlainText(sender, (PlainText) message, group, isAtBot, isOnlyAtBot);
            }
        });
    }
}

package org.qingzhixing.EventListeners.MessageContentHandlers;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.data.*;
import org.apache.log4j.Logger;
import org.qingzhixing.Utilities;

public final class CommandGroupPlainTextHandler extends AbstractGroupPlainTextHandler {
    private static final Logger logger = Logger.getLogger(CommandGroupPlainTextHandler.class);

    public CommandGroupPlainTextHandler(Friend masterFriend) {
        super(masterFriend);
    }

    @Override
    public boolean Handle() {
        WoShiShuiHandler();
        Test_Handler();
        return false;
    }

    private void WoShiShuiHandler() {
        var text = getPlainText().getContent();

        if (isAtBot() && text.contains("我是谁")) {
            Image avatar = null;
            try {
                avatar = Utilities.URLToImage(sender().getAvatarUrl(), sender().getBot());
            } catch (RuntimeException e) {
                var logErrorMessage = " 获取图片出错啦TT！";
                logger.error(logErrorMessage);
                var replyChain = MessageUtils.newChain(
                        new At(sender().getId()),
                        new PlainText(logErrorMessage)
                );
                group().sendMessage(replyChain);
                //通知master
                if (isMasterFriendExits()) {
                    masterFriend().sendMessage("" +
                            "用户使用指令'我是谁'时出现错误!" +
                            "\n用户昵称:" + sender().getNick() +
                            "\n用户ID:" + sender().getId() +
                            "\n群名:" + group().getName() +
                            "\n群号:" + group().getId() +
                            "\n错误原因:" + logErrorMessage
                    );
                }
                return;
            }
            MessageChain replyChain = MessageUtils.newChain(
                    new At(sender().getId()),
                    new PlainText(" 头像:"),
                    avatar,
                    new PlainText("昵称: \"" + sender().getNick() + "\"\n"),
                    new PlainText("ID: " + sender().getId())
            );
            group().sendMessage(replyChain);
        }
    }

    private void Test_Handler() {
        logger.debug("Test_Handler() called");
    }
}

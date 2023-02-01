package org.qingzhixing.CommandMatchers;

import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import org.qingzhixing.Utilities;

public class Matcher_MyInfo extends AbstractMatcher {


    public Matcher_MyInfo(@NotNull String originalText, boolean isAtBot, boolean isOnlyAtBot) {
        super(originalText, isAtBot, isOnlyAtBot);
        this.setCommandName("my-info");
        this.setDescription("返回头像、昵称与 QQ ID");
    }

    @Override
    public void Handle() {
        Image avatar;
        try {
            avatar = Utilities.URLToImage(sender().getAvatarUrl(), sender().getBot());
        } catch (RuntimeException e) {
            var logErrorMessage = " 获取图片出错啦TT！";
            logger.error(logErrorMessage);
            AtThenReply(new PlainText(logErrorMessage), sender(), group());
            //通知master
            if (isMasterFriendExits()) {
                masterFriend().sendMessage("" +
                        "用户使用指令 '/my-info' 时出现错误!" +
                        "\n用户昵称:" + sender().getNick() +
                        "\n用户ID:" + sender().getId() +
                        "\n群名:" + group().getName() +
                        "\n群号:" + group().getId() +
                        "\n错误原因:" + logErrorMessage
                );
            }
            return true;
        }
        MessageChain replyChain = MessageUtils.newChain(
                new PlainText("\n头像:"),
                avatar,
                new PlainText("昵称: \"" + sender().getNick() + "\"\n"),
                new PlainText("ID: " + sender().getId())
        );
        AtThenReply(replyChain, sender(), group());
        return true;

    }
}

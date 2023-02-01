package org.qingzhixing.CommandMatchers;

import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qingzhixing.Global;
import org.qingzhixing.Utilities;

public class Matcher_MyInfo extends AbstractMatcher {
    private static final Logger logger = LogManager.getLogger(Matcher_MyInfo.class);


    public Matcher_MyInfo() {
        super();
        this.setCommandName("my-info")
                .setDescription("返回头像、昵称与 QQ ID")
                .setMode(MatchMode.START)
                .setNeedOnlyAtBot(true);
    }

    @Override
    public void Handle() {
        Image avatar;
        try {
            avatar = Utilities.URLToImage(sender().getAvatarUrl(), sender().getBot());
        } catch (RuntimeException e) {
            var logErrorMessage = " 获取图片出错啦TT！";
            logger.error(logErrorMessage);
            AtThenReply(new PlainText(logErrorMessage));
            //通知master
            if (Global.isMasterFriendExists()) {
                Global.masterFriend().sendMessage("" +
                        "用户使用指令 '/my-info' 时出现错误!" +
                        "\n用户昵称:" + sender().getNick() +
                        "\n用户ID:" + sender().getId() +
                        "\nContactID:" + contact().getId() +
                        "\nContact是否为群:" + ContactIsGroup() +
                        "\n错误原因:" + logErrorMessage
                );
            }
            return;
        }
        MessageChain replyChain = MessageUtils.newChain(
                new PlainText("\n头像:"),
                avatar,
                new PlainText("昵称: \"" + sender().getNick() + "\"\n"),
                new PlainText("ID: " + sender().getId())
        );
        AtThenReply(replyChain);
    }
}

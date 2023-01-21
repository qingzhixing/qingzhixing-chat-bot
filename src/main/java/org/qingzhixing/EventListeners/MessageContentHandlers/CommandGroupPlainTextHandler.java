package org.qingzhixing.EventListeners.MessageContentHandlers;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.log4j.Logger;
import org.qingzhixing.Utilities;

import java.util.ArrayList;

public final class CommandGroupPlainTextHandler extends AbstractGroupPlainTextHandler {
    private static final Logger logger = Logger.getLogger(CommandGroupPlainTextHandler.class);
    private final ArrayList<CommandMatcher> matchers;

    public CommandGroupPlainTextHandler(Friend masterFriend) {
        super(masterFriend);
        matchers = new ArrayList<>();
        //顺序代表优先级
        matchers.add(this::CommandHandler_Help);
        matchers.add(this::CommandHandler_WoShiShui);
        matchers.add(this::CommandHandler_QuestionAnswer);
    }


    @Override
    public boolean Handle() {
        for (var matcher : matchers) {
            if (matcher.MatchFunction()) break;
        }
        return false;
    }

    //返回是否成功匹配
    private boolean CommandHandler_WoShiShui() {
        var text = getPlainTextContent();

        if (!isOnlyAtBot() || !text.contains("我是谁")) return false;
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
                        "用户使用指令'我是谁'时出现错误!" +
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

    private boolean CommandHandler_Help() {
        var text = getPlainTextContent();
        if (!isOnlyAtBot() || !text.contains("/help")) return false;
        var replyText = new PlainText(
                "\n目前可以使用的指令:\n" +
                        "[@bot] 我是谁 - 返回头像、昵称与 QQ ID\n" +
                        "[@bot] /help - 帮助\n" +
                        "[@bot] <你问的问题 - 句子中存在'?','？','吗','嘛','么'> - 返回机器人的有趣回答\n" +
                        "    tips:不艾特有10%几率自动回答"
        );
        AtThenReply(replyText, sender(), group());
        return true;
    }

    private boolean CommandHandler_QuestionAnswer() {
        //没有AtBot则90%概率不回答
        if (!isAtBot() && Math.random() < 0.9) {
            return false;
        }
        var originalText = getPlainTextContent();
        var workedText = originalText;
        workedText = workedText.replace("？", "");
        workedText = workedText.replace("?", "");
        workedText = workedText.replace("嘛", "");
        workedText = workedText.replace("吗", "");
        workedText = workedText.replace("么", "");
        //检测到问题则输出对应sb答案
        if (!workedText.equals(originalText) && !workedText.equals("")) {
            //格式化
            workedText = workedText.trim();
//            logger.debug("获取到问题: " + originalText + "\n回答: " + workedText);
            group().sendMessage(workedText);
            return true;
        }
        return false;
    }

    @FunctionalInterface
    private interface CommandMatcher {
        //返回是否成功匹配
        boolean MatchFunction();
    }
}

package org.qingzhixing.MessageContentHandlers;

import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qingzhixing.CommandMatchers.AbstractMatcher;
import org.qingzhixing.CommandMatchers.Matcher_Help;
import org.qingzhixing.CommandMatchers.Matcher_MyInfo;
import org.qingzhixing.CommandMatchers.Matcher_QuestionAnswer;
import org.qingzhixing.Global;
import org.qingzhixing.Utilities;

import java.util.ArrayList;

public final class CommandGroupPlainTextHandler extends AbstractGroupPlainTextHandler {
    private static final Logger logger = LogManager.getLogger(CommandGroupPlainTextHandler.class);
    private final ArrayList<CommandMatcher> matchers_banned;

    private final ArrayList<AbstractMatcher> matchers;

    public CommandGroupPlainTextHandler() {
        super();
        matchers = new ArrayList<>();
        matchers.add(new Matcher_MyInfo());
        matchers.add(new Matcher_QuestionAnswer());
        matchers.add(new Matcher_Help(matchers));

        matchers_banned = new ArrayList<>();
        //顺序代表优先级
        matchers_banned.add(this::CommandHandler_Help);
        matchers_banned.add(this::CommandHandler_MyInfo);
        matchers_banned.add(this::CommandHandler_QuestionAnswer);
        matchers_banned.add(this::CommandHandler_AtBot);
        matchers_banned.add(this::CommandHandler_Test);
    }


    @Override
    public boolean Handle() {
        for (var matcher : matchers) {
            logger.debug(matcher);
            matcher.BindContext(getPlainTextContent(), sender(), group(), isAtBot(), isOnlyAtBot());
            if (matcher.MatchAndHandle()) {
                logger.debug("匹配成功:" + matcher.GetCommandString());
                return true;
            }
            logger.debug("匹配失败:" + matcher.GetCommandString());
        }
        return false;
    }

    //返回是否成功匹配
    private boolean CommandHandler_MyInfo() {
        var text = getPlainTextContent();

        if (!isOnlyAtBot() || !text.contains("/my-info")) return false;
        Image avatar;
        try {
            avatar = Utilities.URLToImage(sender().getAvatarUrl(), sender().getBot());
        } catch (RuntimeException e) {
            var logErrorMessage = " 获取图片出错啦TT！";
            logger.error(logErrorMessage);
            Utilities.AtThenReply(new PlainText(logErrorMessage), sender(), group());
            //通知master
            if (Global.isMasterFriendExists()) {
                Global.masterFriend().sendMessage("" +
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
        Utilities.AtThenReply(replyChain, sender(), group());
        return true;

    }

    private boolean CommandHandler_Help() {
        var text = getPlainTextContent();
        if (!isOnlyAtBot() || !text.contains("/help")) return false;
        var replyText = new PlainText(
                "\n目前可以使用的指令:\n" +
                        "[only@bot] /my-info - 返回头像、昵称与 QQ ID\n" +
                        "[only@bot] /help - 帮助\n" +
                        "[@bot] <你问的问题 - 句子中存在'?','？','吗','嘛'> - 返回机器人的有趣回答\n" +
                        "    tips:不艾特有10%几率自动回答\n" +
                        "[@bot] - 别艾特我啦！不止At bot有50%几率回复，否则为10%\n" +
                        "[only@bot] /test - 此功能仅用作开发者测试，会得到什么回答不确定哟~"
        );
        Utilities.AtThenReply(replyText, sender(), group());
        return true;
    }

    private boolean CommandHandler_QuestionAnswer() {
        //没有AtBot则90%概率不回答
        if (!isAtBot() && Math.random() < 0.9) {
            return false;
        }
        //否则50几率回答
        if (Math.random() < 0.5) {
            return false;
        }
        var originalText = getPlainTextContent();
        var workedText = originalText;
        workedText = workedText.replace("？", "");
        workedText = workedText.replace("?", "");
        workedText = workedText.replace("嘛", "");
        workedText = workedText.replace("吗", "");
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

    private boolean CommandHandler_AtBot() {
        if (!isAtBot()) return false;
        //不止艾特bot则50%几率回复
        if (!isOnlyAtBot()) {
            if (Math.random() < 0.5) {
                Utilities.AtThenReply(new PlainText("  艾特我干嘛呀！！！！"), sender(), group());
                return true;
            }
        } else {
            //单独atBot有10%几率回复
            if (Math.random() < 0.1) {
                Utilities.AtThenReply(new PlainText("  你干嘛！不许艾特我啦！！🤯"), sender(), group());
                return true;
            }
        }
        return false;
    }

    private boolean CommandHandler_Test() {
        var text = getPlainTextContent();
        if (!isOnlyAtBot() || !text.contains("/test")) return false;
        Utilities.QuoteThenReply(new PlainText("引用回复测试"), originalMessageChain(), group());
        return true;
    }

    @FunctionalInterface
    private interface CommandMatcher {
        //返回是否成功匹配
        boolean MatchFunction();
    }
}

package org.qingzhixing;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class Utilities {
    private static final Logger logger = LogManager.getLogger(Utilities.class);

    @NotNull
    public static URL GenerateJarResourceFileURL(@NotNull String path) throws RuntimeException {
        URL url = Utilities.class.getResource(path);
        if (url == null) {
            String errorMessage = "jar资源文件为null.";
            logger.error(errorMessage);
            logger.error("path:" + path);
            throw new RuntimeException(errorMessage);
        }
        return url;
    }

    /**
     * 用地址构造File,自动检测是否存在或是否为文件
     *
     * @return 返回一个用目录地址构造的File类
     */
    public static File BuildResourceFile(@NotNull String path) throws FileNotFoundException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException("目标地址：" + path + " 不存在或为目录");
        }
        return file;
    }

    @NotNull
    public static URL GenerateResourceFileURL(@NotNull String path) throws FileNotFoundException {
        File resourceFile = BuildResourceFile(path);
        try {
            return resourceFile.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    public static <T extends MessageContent> List<MessageContent>
    GetMessageChainContentOf(MessageChain messageChain, Class<T> messageType) {
        ArrayList<MessageContent> messageContentArrayList = new ArrayList<>();
        messageChain.forEach(message -> {
            if (!(message instanceof MessageContent)) return;
            if (message.getClass().isAssignableFrom(messageType)) {
                messageContentArrayList.add((MessageContent) message);
            }
        });
        return messageContentArrayList;
    }

    public static <T extends MessageContent> boolean
    CheckMessageChainExistsContentOf(MessageChain messageChain, Class<T> messageType) {
        return GetMessageChainContentOf(messageChain, messageType).size() != 0;
    }

    public static boolean CheckMessageChainAtUser(@NotNull MessageChain messageChain, @NotNull User user, @NotNull Group group) {
//        logger.debug("Checking message chain at user:" + user.getNick() + " user id:" + user.getId());
        var contents = GetMessageChainContentOf(messageChain, At.class);
        for (var content : contents) {
            var atContent = (At) content;
//            logger.debug("存在At: " + atContent.getDisplay(group) + "At content: " + atContent.contentToString());
            if (atContent.getTarget() == user.getId()) {
//                logger.debug("找到艾特User:" + user.getNick() + " user id:" + user.getId());
                return true;
            }
        }
//        logger.debug("没找到At User:" + user.getNick() + " user id:" + user.getId());
        return false;
    }

    public static boolean CheckMessageChainOnlyAtUser(@NotNull MessageChain messageChain, @NotNull User user, @NotNull Group group) {
        var atMessageContent = GetMessageChainContentOf(messageChain, At.class);
        return atMessageContent.size() == 1 &&
                ((At) atMessageContent.get(0)).getTarget() == user.getId();
    }

    public static boolean CheckMessageChainAtAll(MessageChain messageChain) {
        return CheckMessageChainExistsContentOf(messageChain, AtAll.class);
    }

    public static net.mamoe.mirai.message.data.Image URLToImage(String url, Bot bot) throws RuntimeException {
//        if (true)
//            throw new RuntimeException("Test-中断Image获取");

        InputStream imageInputStream = null;
        net.mamoe.mirai.message.data.Image image = null;
        try {
            imageInputStream = new URL(url).openStream();
        } catch (IOException e) {
            logger.error("初始化imageInputStream时出错:" + e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
        if (imageInputStream == null) {
            logger.error("imageInputStream为null");
        } else {
            image = Contact.uploadImage(bot.getAsFriend(), imageInputStream);
        }
        return image;
    }

    public static void AtThenReply(@NotNull Message originalMessage, @NotNull Member atTarget, @NotNull Contact sendTarget) {
        var newChain = MessageUtils.newChain(
                new At(atTarget.getId()),
                originalMessage
        );
        sendTarget.sendMessage(newChain);
    }

    public static void QuoteThenReply(@NotNull Message originalMessage, @NotNull MessageChain originalMessageChain, @NotNull Contact sendTarget) {
        var newChain = MessageUtils.newChain(
                new QuoteReply(originalMessageChain),
                originalMessage
        );
        sendTarget.sendMessage(newChain);
    }

    /**
     * @return 从MessageChain中提取到的所有PlainText转化为字符串之后相连接的最终字符串
     */
    public static @NotNull String ExtractPlainTextString(@NotNull MessageChain originalMessageChain) {
        var str = new StringBuilder();
        for (var singleMessage : originalMessageChain) {
            if (!(singleMessage instanceof PlainText)) continue;
            var plainText = (PlainText) singleMessage;
            str.append(plainText);
        }
        return str.toString();
    }

    /*
    TODO:失败了，有其他方法获得jar包内文件的File对象吗?
     public static File GetCurrentJarResourceFile(@NotNull String filePath) {
         try {
             URL url = Utilities.GetCurrentJarResourceURL(filePath);
             logger.debug("URL:" + url);
             URI uri = url.toURI();
             logger.debug("URI:" + uri);
             Path path = Paths.get(uri);
             logger.debug("Path:" + path);
             logger.debug("File:" + path.toFile());
             return path.toFile();
         } catch (RuntimeException | URISyntaxException e) {
             throw new RuntimeException(e);
         }
     }
    */
}

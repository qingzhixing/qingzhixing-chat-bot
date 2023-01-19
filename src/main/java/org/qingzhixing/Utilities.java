package org.qingzhixing;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.AtAll;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageContent;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class Utilities {
    private static final Logger logger = Logger.getLogger(Utilities.class);

    @NotNull
    public static URL GetCurrentJarResourceURL(@NotNull String path) throws RuntimeException {
        URL url = Utilities.class.getResource(path);
        if (url == null) {
            String errorMessage = "jar资源文件为null.";
            logger.error(errorMessage);
            logger.error("path:" + path);
            throw new RuntimeException(errorMessage);
        }
        return url;
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
        return GetMessageChainContentOf(messageChain, messageType).size() == 0;
    }

    public static boolean CheckMessageChainAtUser(MessageChain messageChain, User user) {
        var contents = GetMessageChainContentOf(messageChain, At.class);
        for (var content : contents) {
            if (((At) content).getTarget() == user.getId()) {
                return true;
            }
        }
        return false;
    }

    public static boolean CheckMessageChainAtAll(MessageChain messageChain) {
        return CheckMessageChainExistsContentOf(messageChain, AtAll.class);
    }

    public static net.mamoe.mirai.message.data.Image URLToImage(String url, Bot bot) {
        InputStream imageInputStream = null;
        net.mamoe.mirai.message.data.Image image = null;
        try {
            imageInputStream = new URL(url).openStream();
        } catch (IOException e) {
            logger.error("初始化imageInputStream时出错:" + e.getMessage(), e);
        }
        if (imageInputStream == null) {
            logger.error("imageInputStream为null");
        } else {
            image = Contact.uploadImage(bot.getAsFriend(), imageInputStream);
        }
        return image;
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

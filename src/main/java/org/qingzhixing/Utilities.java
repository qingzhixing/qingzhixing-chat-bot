package org.qingzhixing;

import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.AtAll;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageContent;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

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

    public static <T extends MessageContent> boolean CheckMessageChainExistsContentOf(MessageChain messageChain, Class<T> messageType) {
        for (var message : messageChain) {
            if (message.getClass().isAssignableFrom(messageType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean CheckMessageChainAt(MessageChain messageChain) {
        return CheckMessageChainExistsContentOf(messageChain, At.class);
    }

    public static boolean CheckMessageChainAtAll(MessageChain messageChain) {
        return CheckMessageChainExistsContentOf(messageChain, AtAll.class);
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

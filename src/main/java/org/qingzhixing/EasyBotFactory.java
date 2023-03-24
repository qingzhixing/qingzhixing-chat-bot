package org.qingzhixing;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class EasyBotFactory {

    @NotNull
    public static Bot newBot(@NotNull Account botAccount) {
        return BotFactory.INSTANCE.newBot(
                botAccount.ID,
                botAccount.password,
                new BotConfiguration() {{
                    setHeartbeatStrategy(HeartbeatStrategy.REGISTER);
                    // pad支持多台设备同时登陆
                    setProtocol(MiraiProtocol.ANDROID_WATCH);
                    // 缓存文件夹，最终为 workingDir 目录中的 cache 目录
                    setCacheDir(new File("cache"));
                    enableContactCache();
                }}
        );
    }
}

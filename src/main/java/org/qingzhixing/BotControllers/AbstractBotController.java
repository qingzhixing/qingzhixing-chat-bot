package org.qingzhixing.BotControllers;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.SimpleListenerHost;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;


public abstract class AbstractBotController {
    private static final Logger logger = Logger.getLogger(AbstractBotController.class);
    protected Bot bot;

    public void BindBot(@NotNull Bot bot) {
        this.bot = bot;
    }

    public void Start() {
        logger.info(this.getClass().getName() + ".Start() called");
    }

    public final void BindEventListener(@NotNull SimpleListenerHost eventListener) {
        bot.getEventChannel().registerListenerHost(eventListener);
    }
}

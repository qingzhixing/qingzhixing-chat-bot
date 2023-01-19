package org.qingzhixing.BotControllers;


import net.mamoe.mirai.Bot;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.qingzhixing.Account;

public class Test_BotController extends AbstractBotController {
    private static final Logger logger = Logger.getLogger(Test_BotController.class);

    private final Account master;

    public Test_BotController(@NotNull Bot bot, @NotNull Account masterAccount) {
        BindBot(bot);
        master = masterAccount;
    }

    @Override
    public void Start() {
        logger.info("bot QQID: " + bot.getId());
        logger.info("master QQID: " + master.ID);
        bot.login();
    }
}

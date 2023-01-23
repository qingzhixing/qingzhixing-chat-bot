package org.qingzhixing;

import org.jetbrains.annotations.NotNull;
import org.qingzhixing.BotControllers.AbstractBotController;
import org.qingzhixing.BotControllers.SimpleBotController;

import java.util.ArrayList;

public class BotGroupController {
    private final ArrayList<AbstractBotController> botControllers;

    public BotGroupController(@NotNull Settings settings) {
        botControllers = new ArrayList<>();
        settings.botAccountList().forEach(botAccount -> {
            var bot = EasyBotFactory.newBot(botAccount);
            botControllers.add(new SimpleBotController(bot, settings.masterAccount()));
        });
    }

    public void StartControllers() {
        botControllers.forEach(AbstractBotController::Start);
    }
}

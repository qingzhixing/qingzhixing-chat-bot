package org.qingzhixing;

import org.jetbrains.annotations.NotNull;
import org.qingzhixing.BotControllers.AbstractBotController;
import org.qingzhixing.BotControllers.Test_BotController;

import java.util.ArrayList;

public class BotGroupController {
    private final ArrayList<AbstractBotController> botControllers;

    public BotGroupController(@NotNull Settings settings) {
        botControllers = new ArrayList<>();
        for (var botAccount : settings.getBotAccountList()) {
            var bot = EasyBotFactory.newBot(botAccount);
            botControllers.add(new Test_BotController(bot, settings.getMasterAccount()));
        }
    }

    public void StartControllers() {
        for (var controller : botControllers) {
            controller.Start();
        }
    }
}
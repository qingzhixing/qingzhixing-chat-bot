package org.qingzhixing;

import org.apache.log4j.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        var privateSettings = new Settings("/settings-private.xml");
        var publicSettings = new Settings("/settings.xml");
        System.out.println("settings-private.xml:");
        privateSettings.Debug_ConsoleOutputSettings();
        System.out.println("\nsettings.xml:");
        publicSettings.Debug_ConsoleOutputSettings();

        Settings finalSettings;
        if (privateSettings.CheckIsValid()) {
            logger.info("settings-private.xml合法，使用");
            finalSettings = privateSettings;
        } else {
            logger.info("settings-private.xml不合法或不存在");
            if (publicSettings.CheckIsValid()) {
                logger.info("settings.xml合法，使用");
                finalSettings = publicSettings;
            } else {
                logger.info("settings.xml不合法或不存在，无配置文件，程序无法运行");
                return;
            }
        }

        var botGroupControllers = new BotGroupController(finalSettings);
        botGroupControllers.StartControllers();
    }
}

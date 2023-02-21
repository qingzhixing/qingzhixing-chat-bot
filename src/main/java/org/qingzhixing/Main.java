package org.qingzhixing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void main(String[] args) {

        var privateSettingsJarResource = new Settings();
        privateSettingsJarResource.ParseJarResourceFile("/settings-private.xml");
        var publicSettingsJarResource = new Settings();
        publicSettingsJarResource.ParseJarResourceFile("/settings.xml");
        
        System.out.println("settings-private.xml:");
        privateSettingsJarResource.Debug_ConsoleOutputSettings();
        System.out.println("\nsettings.xml:");
        publicSettingsJarResource.Debug_ConsoleOutputSettings();

        Settings finalSettings;
        if (privateSettingsJarResource.CheckIsValid()) {
            logger.info("settings-private.xml合法，使用");
            finalSettings = privateSettingsJarResource;
        } else {
            logger.info("settings-private.xml不合法或不存在");
            if (publicSettingsJarResource.CheckIsValid()) {
                logger.info("settings.xml合法，使用");
                finalSettings = publicSettingsJarResource;
            } else {
                logger.info("settings.xml不合法或不存在，无配置文件，程序无法运行");
                return;
            }
        }

        var botGroupControllers = new BotGroupController(finalSettings);
        botGroupControllers.StartControllers();
    }
}

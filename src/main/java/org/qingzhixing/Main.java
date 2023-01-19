package org.qingzhixing;

public class Main {
    public static void main(String[] args) {

        var settings = new Settings("/settings.xml");
        settings.Debug_ConsoleOutputSettings();
        var botGroupControllers = new BotGroupController(settings);
        botGroupControllers.StartControllers();
    }
}

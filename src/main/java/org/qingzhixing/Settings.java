package org.qingzhixing;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Settings {
    private static final Logger logger = Logger.getLogger(Settings.class);
    private final ArrayList<Account> botAccountList;
    private final Account masterAccount;

    public Settings(@NotNull String settingsFilePath) {
        botAccountList = new ArrayList<>();
        masterAccount = new Account();
        ParseSettingsFile(settingsFilePath);
    }

    public ArrayList<Account> getBotAccountList() {
        return botAccountList;
    }

    public Account getMasterAccount() {
        return masterAccount;
    }

    public void ParseSettingsFile(@NotNull String settingsFilePath) {
        URL settingsURL;
        try {
            settingsURL = Utilities.GetCurrentJarResourceURL(settingsFilePath);
        } catch (Exception e) {
            logger.error("Unable to load settings.Reason: " + e.getMessage(), e);
            return;
        }
        /*
        File settingsFile;
        try {
            settingsFile = Utilities.GetCurrentJarResourceFile(settingsFilePath);
        } catch (RuntimeException e) {
            logger.error("Unable to load settings.Reason: " + e.getMessage(), e);
            System.exit(1);
            return;
        }

        logger.debug(settingsFile.getAbsolutePath());
        //判断settingsFile是否存在并且判断是否为文件
        if (!settingsFile.exists() || !settingsFile.isFile()) {
            String errorString = "settings file不存在或者为文件夹";
            logger.error(errorString);
            throw new IllegalArgumentException(errorString);
        }
         */

        //使用SAXBuilder解析xml
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(settingsURL);
            Element rootElement = document.getRootElement();

            //parse bot settings
            Element botSettingsElement = rootElement.getChild("botSettings");
            List<Element> botAccountElements = botSettingsElement.getChildren("account");
            for (Element element : botAccountElements) {
                Element botQQIDElement = element.getChild("botQQID");
                Element botQQPasswordElement = element.getChild("botQQPassword");
                if (botQQPasswordElement == null ||
                        botQQPasswordElement.getText().equals("") ||
                        botQQIDElement == null ||
                        botQQIDElement.getText().equals("")
                ) {
                    logger.warn("Bot account 配置存在一个账号配置QQID或者Password项不存在或未填写");
                    continue;
                }
                long id = Long.parseLong(botQQIDElement.getText());
                String password = botQQPasswordElement.getText();
                botAccountList.add(new Account(id, password));
            }

            //parse master settings
            Element masterSettingsElement = rootElement.getChild("masterSettings");
            Element masterQQIDElement = masterSettingsElement.getChild("masterQQID");
            if (masterQQIDElement == null || masterQQIDElement.getText().equals("")) {
                logger.warn("Master QQID 未设置（配置不存在或者为空）");
            } else {
                masterAccount.ID = Long.parseLong(masterQQIDElement.getText());
            }

        } catch (JDOMException | IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void Debug_ConsoleOutputSettings() {
        System.out.println("#Bot Accounts:");
        for (var account : botAccountList) {
            System.out.println(account);
        }
        System.out.println("#Master Account:");
        System.out.println(masterAccount.ID);
    }

    public boolean CheckIsValid() {
        //bot账号列表数量>0认为合法
        return botAccountList.size() > 0;
    }
}

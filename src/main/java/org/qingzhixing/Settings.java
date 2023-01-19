package org.qingzhixing;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Settings {
    private static final Logger logger = Logger.getLogger(Settings.class);
    private final ArrayList<Account> botAccountList;
    private final Account masterQQID;

    public Settings(File settingsFile) {
        botAccountList = new ArrayList<>();
        masterQQID = new Account();
        ParseSettingsFile(settingsFile);
    }

    public void ParseSettingsFile(File settingsFile) throws IllegalArgumentException {
        //判断settingsFile是否为空
        if (settingsFile == null) {
            String errorString = "settings file 不能为null！";
            logger.error(errorString);
            throw new IllegalArgumentException(errorString);
        }
        //判断settingsFile是否存在并且判断是否为文件
        if (!settingsFile.exists() || settingsFile.isFile()) {
            String errorString = "settings file不存在或者为文件夹";
            logger.error(errorString);
            throw new IllegalArgumentException(errorString);
        }
        //使用SAXBuilder解析xml
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(settingsFile);
            Element rootElement = document.getRootElement();

            //parse bot settings
            Element botSettingsElement = rootElement.getChild("BotSettings");
            var botAccountList = botSettingsElement.getChildren("Account");


        } catch (JDOMException | IOException e) {
            logger.error(e.getMessage());
        }
    }

}

package org.qingzhixing.MessageContentHandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qingzhixing.CommandMatchers.*;

import java.util.ArrayList;

public final class CommandGroupPlainTextHandler extends AbstractGroupPlainTextHandler {
    private static final Logger logger = LogManager.getLogger(CommandGroupPlainTextHandler.class);

    private final ArrayList<AbstractMatcher> matchers;

    public CommandGroupPlainTextHandler() {
        super();
        matchers = new ArrayList<>();
        matchers.add(new Matcher_MyInfo());
        matchers.add(new Matcher_QuestionAnswer());
        matchers.add(new Matcher_Help(matchers));
        matchers.add(new Matcher_AtBot());
        matchers.add(new Matcher_Test());
    }

    @Override
    public boolean Handle() {
        for (var matcher : matchers) {
//            logger.debug(matcher);
            matcher.BindContext(getPlainTextContent(), originalMessageChain(), sender(), group(), isAtBot(), isOnlyAtBot());
            if (matcher.MatchAndHandle()) {
//                logger.debug("匹配成功:" + matcher.GetCommandString());
                return true;
            }
//            logger.debug("匹配失败:" + matcher.GetCommandString());
        }
        return false;
    }

}

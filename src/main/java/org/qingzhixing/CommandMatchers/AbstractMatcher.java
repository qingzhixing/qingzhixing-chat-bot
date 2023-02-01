package org.qingzhixing.CommandMatchers;


import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMatcher {
    private static final Logger logger = Logger.getLogger(AbstractMatcher.class);
    private final boolean isAtBot;
    private final boolean isOnlyAtBot;
    private boolean needOnlyAtBot;
    private boolean needAtBot;
    private String description;
    private String commandName;
    private MatchMode mode;
    private String originalText;

    public AbstractMatcher(@NotNull String originalText, boolean isAtBot, boolean isOnlyAtBot) {
        description = "";
        commandName = "";
        mode = MatchMode.START_WITH;
        needOnlyAtBot = false;
        needAtBot = false;
        this.originalText = originalText;
        this.isAtBot = isAtBot;
        this.isOnlyAtBot = isOnlyAtBot;
    }

    protected AbstractMatcher setNeedAtBot(boolean needAtBot) {
        this.needAtBot = needAtBot;
        return this;
    }

    protected AbstractMatcher setNeedOnlyAtBot(boolean needOnlyAtBot) {
        this.needOnlyAtBot = needOnlyAtBot;
        return this;
    }

    protected boolean isNotAtBot() {
        return !isAtBot;
    }

    protected boolean isNotOnlyAtBot() {
        return !isOnlyAtBot;
    }

    protected String commandName() {
        return commandName;
    }

    protected MatchMode mode() {
        return mode;
    }

    protected String originalText() {
        return originalText;
    }

    protected String description() {
        return description;
    }

    public String GetCommandString() {
        return ((mode == MatchMode.START_WITH) ? "/" : "") + commandName;
    }

    public final String GetUsage() {
        var commandString = GetCommandString();
        return commandString + " - " + description + "\n     匹配模式:" + mode;
    }

    protected abstract void Handle();

    //返回是否成功匹配
    public final boolean Match() {
        if (needAtBot && isNotAtBot()) return false;
        if (needOnlyAtBot && isNotOnlyAtBot()) return false;
        //空指令则不匹配
        if (commandName.equals("")) return false;
        switch (mode) {
            case START_WITH: {
                var subString = '/' + commandName;
                return originalText.startsWith(subString);
            }
            case CONTAINS: {
                return originalText.contains(commandName);
            }
            default: {
                logger.error("存在一个非法mode,进入了switch default中");
                return false;
            }
        }
    }

    //返回是否成功匹配
    public final boolean MatchAndHandle() {
        if (!Match()) return false;
        Handle();
        return true;
    }

    public AbstractMatcher setDescription(@NotNull String description) {
        this.description = description.trim();
        return this;
    }

    public AbstractMatcher setCommandName(@NotNull String commandName) {
        this.commandName = commandName.trim();
        return this;
    }

    public AbstractMatcher setMode(@NotNull MatchMode mode) {
        this.mode = mode;
        return this;
    }

    public AbstractMatcher setOriginalText(@NotNull String originalText) {
        this.originalText = originalText.trim();
        return this;
    }


    public enum MatchMode {
        START_WITH("START_WITH"),       // 按 /指令名 的方式在文本最开始匹配
        CONTAINS("CONTAINS")          // 只要包含指令名都算匹配成功
        ;

        private final String stringName;

        MatchMode(@NotNull String stringName) {
            this.stringName = stringName;
        }

        @Override
        public String toString() {
            return stringName;
        }
    }
}

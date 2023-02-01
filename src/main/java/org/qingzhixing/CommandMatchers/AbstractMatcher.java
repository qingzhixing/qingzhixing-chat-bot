package org.qingzhixing.CommandMatchers;


import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMatcher {
    private static final Logger logger = Logger.getLogger(AbstractMatcher.class);
    //TODO:Complete
    private String description;

    private String commandName;

    private MatchMode mode;

    private String originalText;

    public AbstractMatcher(@NotNull String originalText) {
        description = "";
        commandName = "";
        mode = MatchMode.DEFAULT;
        this.originalText = originalText;
    }

    public AbstractMatcher(@NotNull String description, @NotNull String commandName, @NotNull MatchMode mode, @NotNull String originalText) {
        this.description = description.trim();
        this.commandName = commandName.trim();
        this.mode = mode;
        this.originalText = originalText.trim();
    }

    public String commandName() {
        return commandName;
    }

    public MatchMode mode() {
        return mode;
    }

    public String originalText() {
        return originalText;
    }

    public String description() {
        return description;
    }

    public abstract void Handle();

    //返回是否成功匹配
    public final boolean Match() {
        //空指令则不匹配
        if (commandName.equals("")) return false;
        switch (mode) {
            case DEFAULT: {
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
        DEFAULT,        // 按 /指令名 的方式在文本最开始匹配
        CONTAINS        // 只要包含指令名都算匹配成功
    }
}

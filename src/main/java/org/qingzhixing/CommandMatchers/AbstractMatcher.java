package org.qingzhixing.CommandMatchers;


import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.qingzhixing.Utilities;

public abstract class AbstractMatcher {
    private static final Logger logger = LogManager.getLogger(AbstractMatcher.class);
    private boolean isAtBot;
    private boolean isOnlyAtBot;
    private Member sender;
    private Contact contact;
    private String originalText;
    private boolean needOnlyAtBot;
    private boolean needAtBot;
    private String description;
    private String commandName;
    private MatchMode mode;

    public AbstractMatcher() {
        description = "";
        commandName = "";
        mode = MatchMode.START;
        needOnlyAtBot = false;
        needAtBot = false;
        originalText = "";
        isAtBot = false;
        isOnlyAtBot = false;
        sender = null;
        contact = null;
    }

    public final void BindContext(@NotNull String originalText, @NotNull Member sender, @NotNull Contact contact, boolean isAtBot, boolean isOnlyAtBot) {
        this.originalText = originalText.trim();
        this.isAtBot = isAtBot;
        this.isOnlyAtBot = isOnlyAtBot;
        this.sender = sender;
        this.contact = contact;
    }

    //返回是否At成功
    public final boolean AtThenReply(@NotNull final Message originalMessage) {
        //仅群聊中可At
        if (!(contact instanceof Group)) {
            sender.sendMessage(originalMessage);
            return false;
        }
        Utilities.AtThenReply(originalMessage, sender, contact);
        return true;
    }

    public Member sender() {
        return sender;
    }

    public Contact contact() {
        return contact;
    }

    public boolean ContactIsGroup() {
        return contact instanceof Group;
    }

    protected AbstractMatcher setNeedAtBot(boolean needAtBot) {
        this.needAtBot = needAtBot;
        if (!needAtBot) setNeedOnlyAtBot(false);
        return this;
    }

    protected AbstractMatcher setNeedOnlyAtBot(boolean needOnlyAtBot) {
        this.needOnlyAtBot = needOnlyAtBot;
        if (needOnlyAtBot) setNeedAtBot(true);
        return this;
    }

    protected boolean isAtBot() {
        return isAtBot;
    }

    protected boolean isOnlyAtBot() {
        return isOnlyAtBot;
    }

    protected String commandName() {
        return commandName;
    }

    @Override
    public String toString() {
        return "AbstractMatcher{" +
                "isAtBot=" + isAtBot +
                ", isOnlyAtBot=" + isOnlyAtBot +
                ", sender=" + sender +
                ", contact=" + contact +
                ", originalText='" + originalText + '\'' +
                ", needOnlyAtBot=" + needOnlyAtBot +
                ", needAtBot=" + needAtBot +
                ", description='" + description + '\'' +
                ", commandName='" + commandName + '\'' +
                ", mode=" + mode +
                '}';
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
        return ((mode == MatchMode.START) ? "/" : "") + commandName;
    }

    public final String GetUsage() {
        var atState = "";
        if (needAtBot) atState = "[@bot]";
        if (needOnlyAtBot) atState = "[only@bot]";

        return atState + ' ' + GetCommandString() + " : " + description + "\n     匹配模式:" + mode.description;
    }

    protected abstract void Handle();

    //返回是否成功匹配
    protected boolean Match() {
        if (needAtBot && !isAtBot()) return false;
        if (needOnlyAtBot && !isOnlyAtBot()) return false;
        switch (mode) {
            case START:
            case START_WITH_ARGS: {
                //空指令则不匹配
                if (commandName.equals("")) return false;
                return originalText.startsWith(GetCommandString());
            }
            case CONTAINS: {
                //空指令则不匹配
                if (commandName.equals("")) return false;
                return originalText.contains(GetCommandString());
            }
            case CUSTOM: {
                logger.warn("使用了自定义的匹配模式，但是并未重写 Match() 方法");
                return false;
            }
            default: {
                logger.warn("存在一个非法匹配模式");
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


    public enum MatchMode {
        START("按 /指令名 的方式在文本最开始匹配"),
        START_WITH_ARGS("按 /指令名 的方式在文本最开始匹配,并且处理参数"),
        CONTAINS("只要包含指令名都算匹配成功"),
        CUSTOM("自定义匹配方式"),
        ;

        private final String description;

        MatchMode(@NotNull String description) {
            this.description = description;
        }

        public String description() {
            return description;
        }

        @Override
        public String toString() {
            return "MatchMode{" +
                    "description='" + description + '\'' +
                    '}';
        }
    }
}

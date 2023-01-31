package org.qingzhixing.CommandMatchers;

public abstract class AbstractMatcher {
    //TODO:Complete
    protected String description;

    protected String commandName;

    protected MatchMode mode;

    protected String originalText;

    public AbstractMatcher() {
        description = "";
        commandName = "";
        mode = MatchMode.DEFAULT;
    }

    public abstract void Handle();

    //返回是否成功匹配
    public boolean Match() {
        //空指令则不匹配
        return !commandName.equals("");
        //TODO:Complete
    }

    //返回是否成功匹配
    public boolean MatchAndHandle() {
        if (!Match()) return false;
        Handle();
        return true;
    }

    //返回指令描述
    public final String description() {
        return description;
    }

    enum MatchMode {
        DEFAULT,        // 按 /指令名 的方式在文本最开始匹配
        CONTAINS        // 只要包含指令名都算匹配成功
    }
}

package org.qingzhixing.BotControllers;


import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.PokeMessage;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.qingzhixing.Account;
import org.qingzhixing.EventListeners.SimpleEventListener;
import org.qingzhixing.Global;

public class SimpleBotController extends AbstractBotController {
    private static final Logger logger = Logger.getLogger(SimpleBotController.class);

    private final Account masterAccount;

    public SimpleBotController(@NotNull Bot bot, @NotNull Account masterAccount) {
        super(bot);
        this.masterAccount = masterAccount;
    }

    @Override
    public void Start() {
        logger.info("bot QQID: " + bot.getId());
        logger.info("master QQID: " + masterAccount.ID);
        bot.login();
        Global.setMasterFriend(bot.getFriend(masterAccount.ID));
        BindEventListener(new SimpleEventListener());
        NotifyingMaster();
    }

    public void NotifyingMaster() {
        if (Global.isMasterFriendExists()) {
            Global.masterFriend().sendMessage("重大消息！青纸星的bot上线辣！😍");
            Global.masterFriend().sendMessage(PokeMessage.ChuoYiChuo);
        } else {
            logger.warn("没找到bot好友列表中master的QQ,无法通知其上线");
        }
    }
}

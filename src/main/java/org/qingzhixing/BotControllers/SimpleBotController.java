package org.qingzhixing.BotControllers;


import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.data.PokeMessage;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.qingzhixing.Account;
import org.qingzhixing.EventListeners.SimpleEventListener;

public class SimpleBotController extends AbstractBotController {
    private static final Logger logger = Logger.getLogger(SimpleBotController.class);

    private final Account masterAccount;
    private Friend masterFriend;

    public SimpleBotController(@NotNull Bot bot, @NotNull Account masterAccount) {
        super(bot);
        this.masterAccount = masterAccount;
    }

    @Override
    public void Start() {
        logger.info("bot QQID: " + bot.getId());
        logger.info("master QQID: " + masterAccount.ID);
        bot.login();
        masterFriend = bot.getFriend(masterAccount.ID);
        BindEventListener(new SimpleEventListener(masterFriend));
        NotifyingMaster();
    }

    public void NotifyingMaster() {
        if (masterFriend != null) {
            masterFriend.sendMessage("重大消息！青纸星的bot上线辣！😍");
            masterFriend.sendMessage(PokeMessage.ChuoYiChuo);
        } else {
            logger.warn("没找到bot好友列表中master的QQ,无法通知其上线");
        }
    }
}

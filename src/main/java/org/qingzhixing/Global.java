package org.qingzhixing;

import net.mamoe.mirai.contact.Friend;

public final class Global {
    private static Friend masterFriend;

    public static boolean isMasterFriendExists() {
        return masterFriend != null;
    }

    public static void setMasterFriend(Friend masterFriend) {
        Global.masterFriend = masterFriend;
    }

    public static Friend masterFriend() {
        return masterFriend;
    }
}

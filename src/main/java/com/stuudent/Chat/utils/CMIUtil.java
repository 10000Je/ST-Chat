package com.stuudent.Chat.utils;

import com.Zrips.CMI.CMI;
import org.bukkit.entity.Player;

public class CMIUtil {

    public static CMI getCMI() {
        return CMI.getInstance();
    }

    public static boolean isIgnored(Player chatPlayer, Player targetPlayer) {
        return getCMI().getPlayerManager().getUser(chatPlayer).isIgnoring(targetPlayer.getUniqueId());
    }

}

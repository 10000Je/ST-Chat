package com.stuudent.Chat.utils;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.entity.Player;

public class EssentialsUtil {

    public static Essentials getEssentials() {
        return Essentials.getPlugin(Essentials.class);
    }

    public static User getUser(Player player) {
        return getEssentials().getUser(player);
    }

    public static boolean isIgnored(Player chatPlayer, Player targetPlayer) {
        return getUser(chatPlayer).isIgnoredPlayer(getUser(targetPlayer));
    }

}

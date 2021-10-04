package com.stuudent.Chat.utils;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.entity.Player;

/**
* @apiNote 에센셜을 soft_depend 으로 활용할때는 Util 클래스를 따로 만들어서 메소드를 사용하는 것을 권장합니다.
 */
public class EssentialsUtil {

    public static Essentials getEssentials() {
        return Essentials.getPlugin(Essentials.class);
    }

    public static User getUser(Player player) {
        return getEssentials().getUser(player);
    }

    public static boolean isIgnoredPlayer(Player chatPlayer, Player targetPlayer) {
        return getUser(chatPlayer).isIgnoredPlayer(getUser(targetPlayer));
    }

}

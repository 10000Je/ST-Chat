package com.stuudent.Chat;

import com.stuudent.Chat.handlers.data.CTPlayer;
import com.stuudent.Chat.handlers.data.CTData;
import com.stuudent.Chat.handlers.channel.CTGlobal;
import com.stuudent.Chat.handlers.channel.CTRegion;
import com.stuudent.Chat.handlers.data.CTItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChatAPI {

    public static CTGlobal getGlobalChat(Player chatPlayer) {
        return new CTGlobal(chatPlayer);
    }

    public static CTRegion getRegionChat(Player chatPlayer) {
        return new CTRegion(chatPlayer);
    }

    public static CTPlayer getPlayer(Player chatPlayer) {
        return new CTPlayer(chatPlayer);
    }

    public static CTItem getItem(ItemStack targetItem) {
        return new CTItem(targetItem);
    }

    public static CTData getData() {
        return new CTData();
    }

}

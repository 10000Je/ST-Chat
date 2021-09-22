package com.stuudent.Chat;

import com.stuudent.Chat.data.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChatAPI {

    public static GlobalChatData getGlobalChat(Player chatPlayer) {
        return new GlobalChatData(chatPlayer);
    }

    public static RegionChatData getRegionChat(Player chatPlayer) {
        return new RegionChatData(chatPlayer);
    }

    public static ChatPlayerData getPlayer(Player chatPlayer) {
        return new ChatPlayerData(chatPlayer);
    }

    public static ChatItemData getItem(ItemStack targetItem) {
        return new ChatItemData(targetItem);
    }

    public static AllData getData() {
        return new AllData();
    }

}

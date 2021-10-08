package com.stuudent.chat;

import com.stuudent.chat.data.ItemData;
import com.stuudent.chat.data.PlayerData;
import com.stuudent.chat.interfaces.ChatItem;
import com.stuudent.chat.interfaces.ChatPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChatAPI {

    public static ChatPlayer getPlayer(Player chatPlayer) {
        return new PlayerData(chatPlayer);
    }

    public static ChatItem getItem(ItemStack item) {
        return new ItemData(item);
    }

}

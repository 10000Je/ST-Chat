package com.stuudent.Chat;

import com.stuudent.Chat.data.ItemData;
import com.stuudent.Chat.data.PlayerData;
import com.stuudent.Chat.interfaces.ChatItem;
import com.stuudent.Chat.interfaces.ChatPlayer;
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

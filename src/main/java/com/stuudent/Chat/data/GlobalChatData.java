package com.stuudent.Chat.data;

import com.stuudent.Chat.ChatAPI;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GlobalChatData {

    public ChatPlayerData chatPlayerData;
    public AllData allData;

    public GlobalChatData(Player chatPlayer) {
        this.chatPlayerData = ChatAPI.getPlayer(chatPlayer);
        this.allData = ChatAPI.getData();
    }

    public ChatPlayerData getChatPlayerData() {
        return this.chatPlayerData;
    }

    public List<Player> getPlayers() {
        return this.allData.getGlobalPlayers(this.chatPlayerData);
    }

    public TextComponent getFormat(String message, boolean toKorean) {
        return this.allData.getGlobalFormat(this.chatPlayerData, message, toKorean);
    }

    public TextComponent getFormat(String message, boolean toKorean, ItemStack showItem) {
        return this.allData.getGlobalFormat(this.chatPlayerData, message, toKorean, showItem);
    }



}

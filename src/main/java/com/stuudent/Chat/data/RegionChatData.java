package com.stuudent.Chat.data;

import com.stuudent.Chat.ChatAPI;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RegionChatData {

    public ChatPlayerData chatPlayerData;
    public AllData allData;

    public RegionChatData(Player chatPlayer) {
        this.chatPlayerData = ChatAPI.getPlayer(chatPlayer);
        this.allData = ChatAPI.getData();
    }

    public ChatPlayerData getChatPlayerData() {
        return this.chatPlayerData;
    }

    public List<Player> getPlayers() {
        return this.allData.getRegionPlayers(this.chatPlayerData);
    }

    public int getPlayerNumbers() {
        return this.allData.getRegionNumbers(this.chatPlayerData);
    }

    public boolean isInRegion(Player targetPlayer) {
        return this.allData.isInRegion(this.chatPlayerData, targetPlayer);
    }

    public double getDistance(Player targetPlayer) {
        return this.allData.getDistance(this.chatPlayerData, targetPlayer);
    }

    public TextComponent getFormat(String message, boolean toKorean) {
        return this.allData.getRegionFormat(this.chatPlayerData, message, toKorean);
    }

    public TextComponent getFormat(String message, boolean toKorean, ItemStack showItem) {
        return this.allData.getRegionFormat(this.chatPlayerData, message, toKorean, showItem);
    }

}

package com.stuudent.Chat.handlers.channel;

import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.handlers.data.CTData;
import com.stuudent.Chat.handlers.data.CTPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CTRegion {

    public CTPlayer ctPlayer;
    public CTData ctData;

    public CTRegion(Player chatPlayer) {
        this.ctPlayer = ChatAPI.getPlayer(chatPlayer);
        this.ctData = ChatAPI.getData();
    }

    public CTPlayer getCTPlayer() {
        return this.ctPlayer;
    }

    public List<Player> getPlayers() {
        return this.ctData.getRegionPlayers(this.ctPlayer);
    }

    public int getNumbers() {
        return this.ctData.getRegionNumbers(this.ctPlayer);
    }

    public boolean isInRegion(Player targetPlayer) {
        return this.ctData.isInRegion(this.ctPlayer, targetPlayer);
    }

    public double getDistance(Player targetPlayer) {
        return this.ctData.getDistance(this.ctPlayer, targetPlayer);
    }

    public TextComponent getFormat(String message, boolean toKorean) {
        return this.ctData.getRegionFormat(this.ctPlayer, message, toKorean);
    }

    public TextComponent getFormat(String message, boolean toKorean, ItemStack showItem) {
        return this.ctData.getRegionFormat(this.ctPlayer, message, toKorean, showItem);
    }

}

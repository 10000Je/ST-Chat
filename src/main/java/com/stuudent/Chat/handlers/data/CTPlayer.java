package com.stuudent.Chat.handlers.data;

import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.enums.Channel;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;

public class CTPlayer {

    public Player chatPlayer;
    public CTData ctData;

    public CTPlayer(Player chatPlayer) {
        this.chatPlayer = chatPlayer;
        this.ctData = ChatAPI.getData();
    }

    public Player getPlayer() {
        return this.chatPlayer;
    }

    public boolean isIgnored(Player targetPlayer) {
        return this.ctData.isIgnored(this.chatPlayer, targetPlayer);
    }

    public void setChannel(Channel targetChannel) {
        this.ctData.setChannel(this.chatPlayer, targetChannel);
    }

    public Channel getChannel() {
        return this.ctData.getChannel(this.chatPlayer);
    }

    public void setTime() {
        this.ctData.setTime(this.chatPlayer);
    }

    public long getTime() {
        return this.ctData.getTime(this.chatPlayer);
    }

    public long getLeftTime() {
        return this.ctData.getLeftTime(this.chatPlayer);
    }

    public int getCoolTime() {
        return this.ctData.getCoolTime();
    }

    public boolean isCoolTime() {
        return (getLeftTime() > 0);
    }

    public void setChatColor(ChatColor targetColor) {
        this.ctData.setChatColor(this.chatPlayer, targetColor);
    }

    public ChatColor getChatColor() {
        return this.ctData.getChatColor(this.chatPlayer);
    }

    public void setNickColor(ChatColor targetColor) {
        this.ctData.setNickColor(this.chatPlayer, targetColor);
    }

    public ChatColor getNickColor() {
        return this.ctData.getNickColor(this.chatPlayer);
    }

    public boolean isKorean() {
        return this.ctData.isKorean(this.chatPlayer);
    }

    public void setKorean(boolean state) {
        this.ctData.setKorean(this.chatPlayer, state);
    }

    public TextComponent getHand() {
        return this.ctData.getHandText(this.chatPlayer);
    }


}

package com.stuudent.Chat.data;

import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.enums.ChannelType;
import com.stuudent.Chat.enums.MegaPhoneType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ChatPlayerData {

    public Player chatPlayer;
    public AllData allData;

    public ChatPlayerData(Player chatPlayer) {
        this.chatPlayer = chatPlayer;
        this.allData = ChatAPI.getData();
    }

    public Player getPlayer() {
        return this.chatPlayer;
    }

    public boolean isIgnored(Player targetPlayer) {
        return this.allData.isIgnored(this.chatPlayer, targetPlayer);
    }

    public void setChannel(ChannelType targetChannelType) {
        this.allData.setChannel(this.chatPlayer, targetChannelType);
    }

    public ChannelType getChannel() {
        return this.allData.getChannel(this.chatPlayer);
    }

    public void setTime() {
        this.allData.setTime(this.chatPlayer);
    }

    public long getTime() {
        return this.allData.getTime(this.chatPlayer);
    }

    public long getLeftTime() {
        return this.allData.getLeftTime(this.chatPlayer);
    }

    public int getCoolTime() {
        return this.allData.getCoolTime();
    }

    public boolean isCoolTime() {
        return (getLeftTime() > 0);
    }

    public void setChatColor(ChatColor targetColor) {
        this.allData.setChatColor(this.chatPlayer, targetColor);
    }

    public ChatColor getChatColor() {
        return this.allData.getChatColor(this.chatPlayer);
    }

    public void setNickColor(ChatColor targetColor) {
        this.allData.setNickColor(this.chatPlayer, targetColor);
    }

    public ChatColor getNickColor() {
        return this.allData.getNickColor(this.chatPlayer);
    }

    public boolean isKorean() {
        return this.allData.isKorean(this.chatPlayer);
    }

    public void setKorean(boolean state) {
        this.allData.setKorean(this.chatPlayer, state);
    }

    public TextComponent getHand() {
        return this.allData.getHandText(this.chatPlayer);
    }

    public void setMegaPhoneCool(MegaPhoneType megaPhoneType) {
        this.allData.setMegaPhoneCool(megaPhoneType, this.chatPlayer);
    }

    public long getMegaPhoneCoolTime(MegaPhoneType megaPhoneType) {
        return this.allData.getMegaPhoneCoolTime(megaPhoneType, this.chatPlayer);
    }

    public boolean isMegaPhoneCool(MegaPhoneType megaPhoneType) {
        return this.allData.isMegaPhoneCool(megaPhoneType, this.chatPlayer);
    }

    public TextComponent getMegaPhoneMessage(MegaPhoneType megaPhoneType, String message) {
        return this.allData.getMegaPhoneMessage(megaPhoneType, this.chatPlayer, message);
    }

}

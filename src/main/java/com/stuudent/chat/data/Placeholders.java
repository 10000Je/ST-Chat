package com.stuudent.chat.data;

import com.stuudent.chat.ChatAPI;
import com.stuudent.chat.enums.ChannelType;
import com.stuudent.chat.enums.MegaPhoneType;
import com.stuudent.chat.interfaces.ChatPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "stchat";
    }

    @Override
    public @NotNull String getAuthor() {
        return "STuuDENT";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.2";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if(player == null)
            return null;
        ChatPlayer chatPlayer = ChatAPI.getPlayer(player.getPlayer());
        switch(params) {
            case "channel":
                if(chatPlayer.getChannel().equals(ChannelType.GLOBAL))
                    return "전체";
                else if(chatPlayer.getChannel().equals(ChannelType.REGION))
                    return "지역";
                else
                    return null;
            case "korean":
                if(chatPlayer.isKoreanChatEnabled())
                    return "켜짐";
                else
                    return "꺼짐";
            case "item_cool":
                return String.valueOf(chatPlayer.getShowItemCool());
            case "cool_normal":
                return String.valueOf(chatPlayer.getMegaPhoneCool(MegaPhoneType.NORMAL));
            case "cool_advanced":
                return String.valueOf(chatPlayer.getMegaPhoneCool(MegaPhoneType.ADVANCED));
            default:
                return null;
        }
    }


}

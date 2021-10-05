package com.stuudent.Chat.interfaces;

import com.stuudent.Chat.enums.ChannelType;
import com.stuudent.Chat.enums.MegaPhoneType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.List;

public interface ChatPlayer {

    /**
     * @return returns Bukkit Player.
     */
    Player getBukkitPlayer();

    /**
     * @param target is the player who will be checked whether ignored or not.
     * @return if target is ignored, returns true. otherwise, return false.
     */
    boolean isIgnored(Player target);

    /**
     * @param channel will be chatPlayer's current channel.
     */
    void setChannel(ChannelType channel);

    /**
     * @return returns chatPlayer's current channel.
     */
    ChannelType getChannel();

    /**
     * @apiNote This Method will reset chatPlayer's ShowItem CoolTime.
     */
    void resetShowItemCool();

    /**
     * @return returns chatPlayer's ShowItem CoolTime.
     */
    long getShowItemCool();

    /**
     * @return if chatPlayer's CoolTime is over 0, returns true. otherwise, return false.
     */
    boolean isShowItemCool();

    /**
     * @param color will be chatPlayer's chatColor.
     */
    void setChatColor(ChatColor color);

    /**
     * @return returns chatPlayer's chatColor.
     */
    ChatColor getChatColor();

    /**
     * @return returns chatPlayer's hand TextComponent.
     */
    TextComponent getHandText();

    /**
     * @return if chatPlayer's KoreanChat is enabled, returns true. otherwise, returns false.
     */
    boolean isKoreanChatEnabled();

    /**
     * @param state will be chatPlayer's KoreanChat Status.
     */
    void setKoreanChatState(boolean state);

    /**
     * @param megaPhone is MegaPhoneType that coolTime will be reset.
     */
    void resetMegaPhoneCool(MegaPhoneType megaPhone);

    /**
     * @param megaPhone is MegaPhoneType that coolTime will be return.
     * @return returns chatPlayer's MegaPhone coolTime.
     */
    long getMegaPhoneCool(MegaPhoneType megaPhone);

    /**
     * @param megaPhone is MegaPhoneType that coolTime will be checked whether overs 0 or not.
     * @return if chatPlayer's MegaPhone CoolTime overs 0, it will return true. otherwise, it will return false.
     */
    boolean isMegaPhoneCool(MegaPhoneType megaPhone);

    /**
     * @param megaPhone is MegaPhoneType that chatPlayer going to use.
     * @param message is MegaPhone Message that chatPlayer wrote.
     * @return returns Formatted MegaPhone Message TextComponent.
     */
    TextComponent getMegaPhoneFormat(MegaPhoneType megaPhone, String message);

    /**
     * @return returns chatPlayer's receivers.
     */
    List<Player> getChatReceivers();

    /**
     * @param message is chat Message that chatPlayer wrote.
     * @return returns Formatted ChatMessage TextComponent
     */
    TextComponent getChatFormat(String message);

    /**
     * @param message is broadcast Message that chatPlayer wrote.
     * @return returns Formatted Broadcast Message TextComponent List.
     */
    List<TextComponent> getBroadcastFormats(String message);

    /**
     * @param message is chat Message that chatPlayer wrote.
     * @return returns Formatted Chat Message for Discord Webhook.
     */
    String getDiscordWebhookFormat(String message);

}

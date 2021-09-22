package com.stuudent.Chat.listeners;

import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.ChatCore;
import com.stuudent.Chat.data.ChatPlayerData;
import com.stuudent.Chat.data.GlobalChatData;
import com.stuudent.Chat.data.RegionChatData;
import com.stuudent.Chat.enums.ChannelType;
import com.stuudent.Chat.events.ChatEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.isCancelled())
            return;
        e.setCancelled(true);
        Player player = e.getPlayer();
        ChatPlayerData chatPlayerData = ChatAPI.getPlayer(player);
        if(chatPlayerData.getChannel().equals(ChannelType.GLOBAL)) {
            GlobalChatData globalChatData = ChatAPI.getGlobalChat(player);
            TextComponent textMessage;
            if(e.getMessage().contains(ChatCore.cf.getString("ItemShowText"))) {
                if(chatPlayerData.isCoolTime()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("ItemShowCoolTimeMsg").
                            replace("[LEFTTIME]",String.valueOf(chatPlayerData.getLeftTime()))));
                    return;
                }
                ItemStack showItem = player.getInventory().getItemInMainHand();
                textMessage = globalChatData.getFormat(e.getMessage(), chatPlayerData.isKorean(), showItem);
                ChatEvent event = new ChatEvent(player, e.getMessage(), showItem, ChannelType.GLOBAL);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) {
                    return;
                }
                for(Player gp : globalChatData.getPlayers())
                    gp.sendMessage(textMessage);
                chatPlayerData.setTime();
            } else {
                textMessage = globalChatData.getFormat(e.getMessage(), chatPlayerData.isKorean());
                ChatEvent event = new ChatEvent(player, e.getMessage(), ChannelType.GLOBAL);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) {
                    return;
                }
                for(Player gp : globalChatData.getPlayers())
                    gp.sendMessage(textMessage);
            }
            Bukkit.getConsoleSender().sendMessage(textMessage);
            return;
        }
        if(chatPlayerData.getChannel().equals(ChannelType.REGION)) {
            RegionChatData regionChatData = ChatAPI.getRegionChat(player);
            TextComponent textMessage;
            if(e.getMessage().contains(ChatCore.cf.getString("ItemShowText"))) {
                if(chatPlayerData.isCoolTime()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("ItemShowCoolTimeMsg").
                            replace("[LEFTTIME]",String.valueOf(chatPlayerData.getLeftTime()))));
                    return;
                }
                ItemStack showItem = player.getInventory().getItemInMainHand();
                textMessage = regionChatData.getFormat(e.getMessage(), chatPlayerData.isKorean(), showItem);
                ChatEvent event = new ChatEvent(player, e.getMessage(), showItem, ChannelType.REGION);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) {
                    return;
                }
                for(Player rp : regionChatData.getPlayers())
                    rp.sendMessage(textMessage);
                chatPlayerData.setTime();
            } else {
                textMessage = regionChatData.getFormat(e.getMessage(), chatPlayerData.isKorean());
                ChatEvent event = new ChatEvent(player, e.getMessage(), ChannelType.REGION);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) {
                    return;
                }
                for(Player rp : regionChatData.getPlayers())
                    rp.sendMessage(textMessage);
            }
            Bukkit.getConsoleSender().sendMessage(textMessage);
        }
    }

}

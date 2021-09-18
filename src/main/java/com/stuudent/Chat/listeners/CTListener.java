package com.stuudent.Chat.listeners;

import com.stuudent.Chat.CTCore;
import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.enums.Channel;
import com.stuudent.Chat.handlers.data.CTPlayer;
import com.stuudent.Chat.handlers.channel.CTGlobal;
import com.stuudent.Chat.handlers.channel.CTRegion;
import com.stuudent.Chat.handlers.event.ChatEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public class CTListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.isCancelled())
            return;
        e.setCancelled(true);
        Player player = e.getPlayer();
        CTPlayer ctPlayer = ChatAPI.getPlayer(player);
        if(ctPlayer.getChannel().equals(Channel.GLOBAL)) {
            CTGlobal ctGlobal = ChatAPI.getGlobalChat(player);
            TextComponent textMessage;
            if(e.getMessage().contains(CTCore.cf.getString("ItemShowText"))) {
                if(ctPlayer.isCoolTime()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', CTCore.cf.getString("ItemShowCoolTimeMsg").
                            replace("[LEFTTIME]",String.valueOf(ctPlayer.getLeftTime()))));
                    return;
                }
                ItemStack showItem = player.getInventory().getItemInMainHand();
                textMessage = ctGlobal.getFormat(e.getMessage(), ctPlayer.isKorean(), showItem);
                ChatEvent event = new ChatEvent(player, e.getMessage(), showItem, Channel.GLOBAL);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) {
                    return;
                }
                for(Player gp : ctGlobal.getPlayers())
                    gp.sendMessage(textMessage);
                ctPlayer.setTime();
            } else {
                textMessage = ctGlobal.getFormat(e.getMessage(), ctPlayer.isKorean());
                ChatEvent event = new ChatEvent(player, e.getMessage(), Channel.GLOBAL);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) {
                    return;
                }
                for(Player gp : ctGlobal.getPlayers())
                    gp.sendMessage(textMessage);
            }
            Bukkit.getConsoleSender().sendMessage(textMessage);
            return;
        }
        if(ctPlayer.getChannel().equals(Channel.REGION)) {
            CTRegion ctRegion = ChatAPI.getRegionChat(player);
            TextComponent textMessage;
            if(e.getMessage().contains(CTCore.cf.getString("ItemShowText"))) {
                if(ctPlayer.isCoolTime()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', CTCore.cf.getString("ItemShowCoolTimeMsg").
                            replace("[LEFTTIME]",String.valueOf(ctPlayer.getLeftTime()))));
                    return;
                }
                ItemStack showItem = player.getInventory().getItemInMainHand();
                textMessage = ctRegion.getFormat(e.getMessage(), ctPlayer.isKorean(), showItem);
                ChatEvent event = new ChatEvent(player, e.getMessage(), showItem, Channel.REGION);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) {
                    return;
                }
                for(Player rp : ctRegion.getPlayers())
                    rp.sendMessage(textMessage);
                ctPlayer.setTime();
            } else {
                textMessage = ctRegion.getFormat(e.getMessage(), ctPlayer.isKorean());
                ChatEvent event = new ChatEvent(player, e.getMessage(), Channel.REGION);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) {
                    return;
                }
                for(Player rp : ctRegion.getPlayers())
                    rp.sendMessage(textMessage);
            }
            Bukkit.getConsoleSender().sendMessage(textMessage);
        }
    }

}

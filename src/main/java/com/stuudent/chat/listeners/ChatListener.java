package com.stuudent.chat.listeners;

import com.stuudent.chat.ChatAPI;
import com.stuudent.chat.ChatCore;
import com.stuudent.chat.events.ChatEvent;
import com.stuudent.chat.interfaces.ChatPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.isCancelled())
            return;
        e.setCancelled(true);
        Player player = e.getPlayer();
        ChatPlayer chatPlayer = ChatAPI.getPlayer(player);
        if(e.getMessage().contains(ChatCore.cf.getString("ItemShowText"))) {
            if(chatPlayer.isShowItemCool()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("ItemShowCoolTimeMsg").
                        replace("%item_cool%",String.valueOf(chatPlayer.getShowItemCool()))));
                return;
            }
        }
        ChatEvent event = new ChatEvent(player, e.getMessage());
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) {
            e.setCancelled(false);
            return;
        }
        TextComponent textMessage = chatPlayer.getChatFormat(event.getMessage());
        for(Player p : chatPlayer.getChatReceivers())
            p.sendMessage(textMessage);
        chatPlayer.resetShowItemCool();
        Bukkit.getConsoleSender().sendMessage(textMessage);
    }

}

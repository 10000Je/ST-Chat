package com.stuudent.Chat.schedulers;

import com.stuudent.Chat.ChatCore;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class BroadcastScheduler implements Runnable{

    int index = 0;
    List<String> broadcastMessages = ChatCore.cf.getStringList("BroadcastRepeatingMessage");
    @Override
    public void run() {
        Bukkit.getScheduler().runTaskAsynchronously(ChatCore.instance, () -> {
            if(index == broadcastMessages.size()) index = 0;
            for(Player player : Bukkit.getOnlinePlayers()) {
                for (String broadcastMessage : broadcastMessages.get(index).split("\\\\n"))
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, broadcastMessage)));
                index++;
            }
        });
    }

}

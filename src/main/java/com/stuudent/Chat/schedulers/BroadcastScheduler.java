package com.stuudent.Chat.schedulers;

import com.stuudent.Chat.ChatCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;

public class BroadcastScheduler implements Runnable{

    int index = 0;
    List<String> broadcastMessages = ChatCore.cf.getStringList("BroadcastRepeatingMessage");
    @Override
    public void run() {
        if(index == broadcastMessages.size()) index = 0;
        for(String broadcastMessage : broadcastMessages.get(index).split("\\\\n"))
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcastMessage));
        index++;
    }

}

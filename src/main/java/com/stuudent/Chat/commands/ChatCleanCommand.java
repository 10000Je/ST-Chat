package com.stuudent.Chat.commands;

import com.stuudent.Chat.ChatCore;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatCleanCommand implements TabExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(cmd.getName().equals("청소")) {
            Bukkit.getScheduler().runTaskAsynchronously(ChatCore.instance, () -> {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat(ChatCore.cf.getString("CCTimeFormat"));
                String date_format = format.format(date);
                for(int i=0; i<100; i++) {
                    for(Player player : Bukkit.getOnlinePlayers())
                        player.sendMessage("");
                }
                for(String message : ChatCore.cf.getStringList("CCMessage"))
                    if(sender instanceof Player) {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                PlaceholderAPI.setPlaceholders((Player) sender, message.replace("%current_time%", date_format))));
                    } else {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                PlaceholderAPI.setPlaceholders(null, message.replace("%current_time%", date_format))));
                    }
            });
        }
        if(cmd.getName().equals("개인채팅청소")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("§c이 명령어는 플레이어만 사용할 수 있습니다.");
                return false;
            }
            Player player = (Player) sender;
            Bukkit.getScheduler().runTaskAsynchronously(ChatCore.instance, () -> {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat(ChatCore.cf.getString("CCTimeFormat"));
                String date_format = format.format(date);
                for(int i=0; i<100; i++) {
                    player.sendMessage("");
                }
                for(String message : ChatCore.cf.getStringList("PCCMessage"))
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            PlaceholderAPI.setPlaceholders(player, message.replace("%current_time%", date_format))));
            });
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        return null;
    }
}

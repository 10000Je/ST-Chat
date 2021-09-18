package com.stuudent.Chat.commands;

import com.stuudent.Chat.CTCore;
import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.handlers.data.CTPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class KoreanCmd implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!cmd.getName().equals("한글채팅"))
            return false;
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        CTPlayer ctPlayer = ChatAPI.getPlayer(player);
        if(ctPlayer.isKorean()) {
            ctPlayer.setKorean(false);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', CTCore.cf.getString("KoreanDisable")));
        } else {
            ctPlayer.setKorean(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', CTCore.cf.getString("KoreanEnable")));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        return null;
    }

}

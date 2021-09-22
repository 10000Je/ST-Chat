package com.stuudent.Chat.commands;

import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.ChatCore;
import com.stuudent.Chat.data.ChatPlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class KoreanChatCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!cmd.getName().equals("한글채팅"))
            return false;
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        ChatPlayerData chatPlayerData = ChatAPI.getPlayer(player);
        if(chatPlayerData.isKorean()) {
            chatPlayerData.setKorean(false);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("KoreanDisable")));
        } else {
            chatPlayerData.setKorean(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("KoreanEnable")));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        return null;
    }

}

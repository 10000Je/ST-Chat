package com.stuudent.Chat.commands;

import com.stuudent.Chat.CTCore;
import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.enums.Channel;
import com.stuudent.Chat.handlers.data.CTPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class GlobalCmd implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!cmd.getName().equals("전체채팅"))
            return false;
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        CTPlayer ctPlayer = ChatAPI.getPlayer(player);
        ctPlayer.setChannel(Channel.GLOBAL);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CTCore.cf.getString("GlobalEnable")));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        return null;
    }

}

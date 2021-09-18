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

public class RegionCmd implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!cmd.getName().equals("지역채팅"))
            return false;
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        CTPlayer ctPlayer = ChatAPI.getPlayer(player);
        ctPlayer.setChannel(Channel.REGION);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CTCore.cf.getString("RegionEnable")));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}

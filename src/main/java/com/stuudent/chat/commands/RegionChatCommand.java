package com.stuudent.chat.commands;

import com.stuudent.chat.ChatAPI;
import com.stuudent.chat.ChatCore;
import com.stuudent.chat.enums.ChannelType;
import com.stuudent.chat.interfaces.ChatPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class RegionChatCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!cmd.getName().equals("지역채팅"))
            return false;
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        ChatPlayer chatPlayer = ChatAPI.getPlayer(player);
        chatPlayer.setChannel(ChannelType.REGION);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("RegionEnable")));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}

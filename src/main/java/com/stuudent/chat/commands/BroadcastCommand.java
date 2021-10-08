package com.stuudent.chat.commands;

import com.stuudent.chat.ChatAPI;
import com.stuudent.chat.ChatCore;
import com.stuudent.chat.interfaces.ChatPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class BroadcastCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!cmd.getName().equals("공지")) return false;
        if(!(sender instanceof Player)) {
            sender.sendMessage("§c이 명령어는 플레이어만 사용할 수 있습니다.");
            return false;
        }
        Player player = (Player) sender;
        if(args.length == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("BroadcastCommandHelp")));
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(String arg : args)
            stringBuilder.append(arg).append(" ");
        ChatPlayer chatPlayer = ChatAPI.getPlayer(player);
        List<TextComponent> broadcastMessages = chatPlayer.getBroadcastFormats(stringBuilder.toString());
        for(TextComponent broadcastMessage : broadcastMessages) {
            for(Player onlinePlayer : Bukkit.getOnlinePlayers())
                onlinePlayer.sendMessage(broadcastMessage);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        return null;
    }

}

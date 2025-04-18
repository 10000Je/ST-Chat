package com.stuudent.chat.commands;

import com.stuudent.chat.ChatCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReloadCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!cmd.getName().equals("채팅관리")) return false;
        if(args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("ReloadHelpMessage").replace("\\n", "\n")));
            return false;
        }
        else if(args[0].equals("리로드") || args[0].equalsIgnoreCase("reload")) {
            ChatCore.instance.reloadConfig();
            ChatCore.cf = ChatCore.instance.getConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("ReloadComplete").replace("\\n", "\n")));
            return false;
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("ReloadUnknownCommand").replace("\\n", "\n")));
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!cmd.getName().equals("채팅관리")) return null;
        List<String> available = new ArrayList<>();
        List<String> arg0s = Arrays.asList("리로드", "reload");
        if(args.length == 1) {
            for(String arg0 : arg0s) {
                if(arg0.toLowerCase().startsWith(args[0].toLowerCase())) {
                    available.add(arg0);
                }
            }
            return available;
        }
        return null;
    }

}

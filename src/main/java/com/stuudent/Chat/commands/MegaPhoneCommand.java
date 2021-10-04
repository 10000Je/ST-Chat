package com.stuudent.Chat.commands;

import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.ChatCore;
import com.stuudent.Chat.enums.MegaPhoneType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MegaPhoneCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(cmd.getName().equals("확성기설정")) {
            if(args.length == 0) {
                for(String message : ChatCore.cf.getStringList("MegaPhoneSetHelpMessage"))
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
            else if(args[0].equals("일반가격")) {
                if(args.length == 1) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneSetWritePrice")));
                    return false;
                }
                try {
                    float price = Float.parseFloat(args[1]);
                    if(price < 0) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneSetNotPrice")));
                        return false;
                    }
                    ChatAPI.getData().setMegaPhonePrice(MegaPhoneType.NORMAL, price);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneSetNormalPrice").
                            replace("%megaphone_price%", String.valueOf(price))));
                    return false;
                } catch(NumberFormatException e) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneSetNotPrice")));
                    return false;
                }
            }
            else if(args[0].equals("고급가격")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneSetWritePrice")));
                    return false;
                }
                try {
                    float price = Float.parseFloat(args[1]);
                    if (price < 0) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneSetNotPrice")));
                        return false;
                    }
                    ChatAPI.getData().setMegaPhonePrice(MegaPhoneType.ADVANCED, price);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneSetAdvancedPrice").
                            replace("%megaphone_price%", String.valueOf(price))));
                    return false;
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneSetNotPrice")));
                    return false;
                }
            }
            else if(args[0].equals("금지")) {
                ChatAPI.getData().setMegaPhoneDisable();
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneSetDisabled")));
                return false;
            }
            else if(args[0].equals("허용")) {
                ChatAPI.getData().setMegaPhoneEnable();
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneSetEnabled")));
                return false;
            }
            else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneUnknownCommand")));
                return false;
            }
        }
        if(cmd.getName().equals("일반확성기")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("§이 명령어는 플레이어만 사용할 수 있습니다.");
                return false;
            }
            Player player = (Player) sender;
            if(args.length == 0) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneNormalHelpMessage").
                        replace("%megaphone_price%", String.valueOf(ChatAPI.getData().getMegaPhonePrice(MegaPhoneType.NORMAL))).
                        replace("%megaphone_cool%", String.valueOf(ChatCore.cf.getInt("MegaPhoneNormalCoolTime")))));
            }
            else {
                if(!ChatAPI.getData().isMegaPhoneEnabled()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneDisabledMessage")));
                    return false;
                }
                if(!ChatCore.getEconomy().has(player, ChatAPI.getData().getMegaPhonePrice(MegaPhoneType.NORMAL))) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneLackMoney")));
                    return false;
                }
                if(ChatAPI.getPlayer(player).isMegaPhoneCool(MegaPhoneType.NORMAL)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneNormalCoolMessage").
                            replace("%megaphone_cool%", String.valueOf(ChatAPI.getPlayer(player).getMegaPhoneCoolTime(MegaPhoneType.NORMAL)))));
                    return false;
                }
                ChatCore.getEconomy().withdrawPlayer(player, ChatAPI.getData().getMegaPhonePrice(MegaPhoneType.NORMAL));
                ChatAPI.getPlayer(player).setMegaPhoneCool(MegaPhoneType.NORMAL);
                StringBuilder stringBuilder = new StringBuilder();
                for(String message : args) {
                    stringBuilder.append(message).append(" ");
                }
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    TextComponent message = ChatAPI.getPlayer(player).getMegaPhoneMessage(MegaPhoneType.NORMAL, stringBuilder.toString());
                    if(ChatAPI.getPlayer(onlinePlayer).isIgnored(player))
                        continue;
                    onlinePlayer.sendMessage(message);
                }
            }
            return false;
        }
        if(cmd.getName().equals("고급확성기")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("§이 명령어는 플레이어만 사용할 수 있습니다.");
                return false;
            }
            Player player = (Player) sender;
            if(args.length == 0) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneAdvancedHelpMessage").
                        replace("%megaphone_price%", String.valueOf(ChatAPI.getData().getMegaPhonePrice(MegaPhoneType.ADVANCED))).
                        replace("%megaphone_cool%", String.valueOf(ChatCore.cf.getInt("MegaPhoneAdvancedCoolTime")))));
            }
            else {
                if(!ChatAPI.getData().isMegaPhoneEnabled()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneDisabledMessage")));
                    return false;
                }
                if(!ChatCore.getEconomy().has(player, ChatAPI.getData().getMegaPhonePrice(MegaPhoneType.ADVANCED))) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneLackMoney")));
                    return false;
                }
                if(ChatAPI.getPlayer(player).isMegaPhoneCool(MegaPhoneType.ADVANCED)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("MegaPhoneAdvancedCoolMessage").
                            replace("%megaphone_cool%", String.valueOf(ChatAPI.getPlayer(player).getMegaPhoneCoolTime(MegaPhoneType.ADVANCED)))));
                    return false;
                }
                ChatCore.getEconomy().withdrawPlayer(player, ChatAPI.getData().getMegaPhonePrice(MegaPhoneType.ADVANCED));
                ChatAPI.getPlayer(player).setMegaPhoneCool(MegaPhoneType.ADVANCED);
                StringBuilder stringBuilder = new StringBuilder();
                for(String message : args) {
                    stringBuilder.append(message).append(" ");
                }
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    TextComponent message = ChatAPI.getPlayer(player).getMegaPhoneMessage(MegaPhoneType.ADVANCED, stringBuilder.toString());
                    if(ChatAPI.getPlayer(onlinePlayer).isIgnored(player))
                        continue;
                    onlinePlayer.sendMessage(message);
                    //noinspection deprecation
                    onlinePlayer.sendMessage(ChatMessageType.ACTION_BAR, message);
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.1f, 1f);
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if(cmd.getName().equals("확성기설정")) {
            ArrayList<String> available = new ArrayList<>();
            if(args.length == 1) {
                List<String> args0 = Arrays.asList("일반가격", "고급가격");
                for(String arg : args0) {
                    if(arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                        available.add(arg);
                    }
                }
                return available;
            }
        }
        return null;
    }

}

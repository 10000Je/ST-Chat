package com.stuudent.Chat.handlers.data;

import com.Zrips.CMI.Containers.CMIUser;
import com.earth2me.essentials.User;
import com.github.kimcore.inko.Inko;
import com.stuudent.Chat.CTCore;
import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.enums.Channel;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CTData {

    public static YamlConfiguration cf;
    public static YamlConfiguration tempCf;
    public static File configFile;
    public static File tempFile;

    static {
        configFile = new File("plugins/" + CTCore.instance.getName() + "/CTData.yml");
        tempFile = new File("plugins/" + CTCore.instance.getName() + "/tempData.yml");
        cf = YamlConfiguration.loadConfiguration(configFile);
        tempCf = YamlConfiguration.loadConfiguration(tempFile);
    }

    /* CTPlayer 전용 메소드 */

    public boolean isIgnored(Player chatPlayer, Player targetPlayer) {
        if(CTCore.ess != null) {
            User targetUser = CTCore.ess.getUser(targetPlayer);
            User chatUser = CTCore.ess.getUser(chatPlayer);
            return chatUser.isIgnoredPlayer(targetUser);
        }
        else if(CTCore.cmi != null) {
            CMIUser cmiUser = CTCore.cmi.getPlayerManager().getUser(chatPlayer);
            return cmiUser.isIgnoring(targetPlayer.getUniqueId());
        }
        else {
            return false;
        }
    }

    public void save() {
        try {
            cf.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setChannel(Player chatPlayer, Channel targetChannel) {
        cf.set(chatPlayer.getUniqueId().toString() + ".CHANNEL", targetChannel.name());
    }

    public Channel getChannel(Player chatPlayer) {
        String textChannel = cf.getString(chatPlayer.getUniqueId().toString() + ".CHANNEL", "GLOBAL");
        return Channel.valueOf(textChannel);
    }

    public void setTime(Player chatPlayer) {
        tempCf.set(chatPlayer.getUniqueId().toString() + ".ITEM", Instant.now().getEpochSecond());
    }

    public long getTime(Player chatPlayer) {
        return tempCf.getLong(chatPlayer.getUniqueId().toString() + ".ITEM", 0);
    }

    public long getLeftTime(Player chatPlayer) {
        long passedTime = Instant.now().getEpochSecond()-getTime(chatPlayer);
        return (getCoolTime() - passedTime);
    }

    public int getCoolTime() {
        return CTCore.cf.getInt("ItemShowCoolTime", 60);
    }

    public void setChatColor(Player chatPlayer, ChatColor targetColor) {
        cf.set(chatPlayer.getUniqueId().toString() + ".CHATCOLOR", targetColor.name());
    }

    public ChatColor getChatColor(Player chatPlayer) {
        String textColor = cf.getString(chatPlayer.getUniqueId().toString() + ".CHATCOLOR", CTCore.cf.getString("DefaultChatColor", "WHITE"));
        return ChatColor.valueOf(textColor);
    }

    public void setNickColor(Player chatPlayer, ChatColor targetColor) {
        cf.set(chatPlayer.getUniqueId().toString() + ".NICKCOLOR", targetColor.name());
    }

    public ChatColor getNickColor(Player chatPlayer) {
        String textColor = cf.getString(chatPlayer.getUniqueId().toString() + ".NICKCOLOR", CTCore.cf.getString("DefaultNickColor", "WHITE"));
        return ChatColor.valueOf(textColor);
    }

    public TextComponent getHandText(Player handPlayer) {
        String yourHand = ChatColor.translateAlternateColorCodes('&',
                CTCore.cf.getString("HandShowChat").replace("[PLAYER]", handPlayer.getDisplayName()));
        TextComponent textHand = new TextComponent(yourHand);
        List<BaseComponent> HoverText = new ArrayList<>();
        for (String HT : CTCore.cf.getStringList("HandShowLore"))
            HoverText.add(new TextComponent(ChatColor.translateAlternateColorCodes('&', HT.replace("[PLAYER]", handPlayer.getDisplayName()))));
        BaseComponent[] HoverTexts = HoverText.toArray(new BaseComponent[HoverText.size()]);
        textHand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, HoverTexts));
        return textHand;
    }

    public boolean isKorean(Player chatPlayer) {
        return cf.getBoolean(chatPlayer.getUniqueId().toString() + ".KOREANCHAT", false);
    }

    public void setKorean(Player chatPlayer, boolean state) {
        cf.set(chatPlayer.getUniqueId().toString() + ".KOREANCHAT", state);
    }

    /* CTGlobal 전용메소드 */

    public List<Player> getGlobalPlayers(CTPlayer chatPlayer) {
        List<Player> globalPlayers = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            CTPlayer cp = ChatAPI.getPlayer(p);
            if(cp.isIgnored(chatPlayer.getPlayer()))
                continue;
            globalPlayers.add(p);
        }
        return globalPlayers;
    }

    public TextComponent getGlobalFormat(CTPlayer chatPlayer, String message, boolean toKorean) {
        String[] spLitFormat = ChatColor.translateAlternateColorCodes('&', CTCore.cf.getString("GlobalChatFormat")).
                replace("[MESSAGE]", "[SPLIT][MESSAGE][SPLIT]").
                replace("[PLAYERGROUP]", "[SPLIT][PLAYERGROUP][SPLIT]").
                replace("[PLAYERNAME]", "[SPLIT][PLAYERNAME][SPLIT]").
                split("\\[SPLIT\\]");
        TextComponent textFormat = new TextComponent();
        for(String spLitF : spLitFormat) {
            switch(spLitF) {
                case "[MESSAGE]":
                    if(toKorean) {
                        Inko inko = new Inko();
                        message = inko.en2ko(message, true);
                    }
                    if(chatPlayer.getPlayer().hasPermission(CTCore.cf.getString("AdminPermission"))) {
                        textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', message));
                    } else {
                        TextComponent textMessage = new TextComponent(message);
                        textMessage.setColor(chatPlayer.getChatColor());
                        textFormat.addExtra(textMessage);
                    }
                    break;
                case "[PLAYERGROUP]":
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', CTCore.getChat().getPlayerPrefix(chatPlayer.getPlayer())));
                    break;
                case "[PLAYERNAME]":
                    TextComponent nickName = new TextComponent(chatPlayer.getPlayer().getDisplayName());
                    nickName.setColor(chatPlayer.getNickColor());
                    textFormat.addExtra(nickName);
                    break;
                default:
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', spLitF));
                    break;
            }
        }
        return textFormat;
    }

    public TextComponent getGlobalFormat(CTPlayer chatPlayer, String message, boolean toKorean, ItemStack showItem) {
        String[] spLitFormat = ChatColor.translateAlternateColorCodes('&', CTCore.cf.getString("GlobalChatFormat")).
                replace("[MESSAGE]", "[SPLIT][MESSAGE][SPLIT]").
                replace("[PLAYERGROUP]", "[SPLIT][PLAYERGROUP][SPLIT]").
                replace("[PLAYERNAME]", "[SPLIT][PLAYERNAME][SPLIT]").
                split("\\[SPLIT\\]");
        TextComponent textFormat = new TextComponent();
        for(String spLitF : spLitFormat) {
            switch(spLitF) {
                case "[MESSAGE]":
                    String itemShowText = CTCore.cf.getString("ItemShowText", "[i]");
                    String[] spLitMessage = message.replace(itemShowText, "[SPLIT]" + itemShowText + "[SPLIT]").split("\\[SPLIT\\]");
                    TextComponent textMessage = new TextComponent();
                    for(String spLitM : spLitMessage) {
                        if(spLitM.equals(itemShowText)) {
                            if(showItem.getType().equals(Material.AIR)) {
                                textMessage.addExtra(chatPlayer.getHand());
                            } else {
                                textMessage.addExtra(ChatAPI.getItem(showItem).getItem());
                            }
                        } else {
                            if(toKorean) {
                                Inko inko = new Inko();
                                spLitM = inko.en2ko(spLitM, true);
                            }
                            if(chatPlayer.getPlayer().hasPermission(CTCore.cf.getString("AdminPermission"))) {
                                textMessage.addExtra(ChatColor.translateAlternateColorCodes('&', spLitM));
                            } else {
                                TextComponent chat = new TextComponent(spLitM);
                                chat.setColor(chatPlayer.getChatColor());
                                textFormat.addExtra(chat);
                            }
                        }
                    }
                    textFormat.addExtra(textMessage);
                    break;
                case "[PLAYERGROUP]":
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', CTCore.getChat().getPlayerPrefix(chatPlayer.getPlayer())));
                    break;
                case "[PLAYERNAME]":
                    TextComponent nickName = new TextComponent(chatPlayer.getPlayer().getDisplayName());
                    nickName.setColor(chatPlayer.getNickColor());
                    textFormat.addExtra(nickName);
                    break;
                default:
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', spLitF));
                    break;
            }
        }
        return textFormat;
    }

    /* CTRegion 전용메소드 */

    public List<Player> getRegionPlayers(CTPlayer chatPlayer) {
        List<Player> regionPlayers = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(isInRegion(chatPlayer, p)) {
                CTPlayer cp = ChatAPI.getPlayer(p);
                if(cp.isIgnored(chatPlayer.getPlayer()))
                    continue;
                regionPlayers.add(p);
            }
        }
        return regionPlayers;
    }

    public int getRegionNumbers(CTPlayer chatPlayer) {
        return getRegionPlayers(chatPlayer).size();
    }

    public boolean isInRegion(CTPlayer chatPlayer, Player targetPlayer) {
        return (getDistance(chatPlayer, targetPlayer) <= CTCore.cf.getDouble("RegionChatDistance", 300));
    }

    public double getDistance(CTPlayer chatPlayer, Player targetPlayer) {
        return chatPlayer.getPlayer().getLocation().distance(targetPlayer.getLocation());
    }

    public TextComponent getRegionFormat(CTPlayer chatPlayer, String message, boolean toKorean) {
        String[] spLitFormat = ChatColor.translateAlternateColorCodes('&', CTCore.cf.getString("RegionChatFormat")).
                replace("[MESSAGE]", "[SPLIT][MESSAGE][SPLIT]").
                replace("[PLAYERGROUP]", "[SPLIT][PLAYERGROUP][SPLIT]").
                replace("[PLAYERNAME]", "[SPLIT][PLAYERNAME][SPLIT]").
                replace("[PLAYERNUM]", "[SPLIT][PLAYERNUM][SPLIT]").
                split("\\[SPLIT\\]");
        TextComponent textFormat = new TextComponent();
        for(String splitF : spLitFormat) {
            switch(splitF) {
                case "[MESSAGE]":
                    if(toKorean) {
                        Inko inko = new Inko();
                        message = inko.en2ko(message, true);
                    }
                    if(chatPlayer.getPlayer().hasPermission(CTCore.cf.getString("AdminPermission"))) {
                        textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', message));
                    } else {
                        TextComponent textMessage = new TextComponent(message);
                        textMessage.setColor(chatPlayer.getChatColor());
                        textFormat.addExtra(textMessage);
                    }
                    break;
                case "[PLAYERGROUP]":
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', CTCore.getChat().getPlayerPrefix(chatPlayer.getPlayer())));
                    break;
                case "[PLAYERNAME]":
                    TextComponent nickName = new TextComponent(chatPlayer.getPlayer().getDisplayName());
                    nickName.setColor(chatPlayer.getNickColor());
                    textFormat.addExtra(nickName);
                    break;
                case "[PLAYERNUM]":
                    TextComponent pNum = new TextComponent(String.valueOf(getRegionNumbers(chatPlayer)));
                    pNum.setColor(ChatColor.valueOf(CTCore.cf.getString("PlayerNumberColor", "WHITE")));
                    textFormat.addExtra(pNum);
                    break;
                default:
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', splitF));
                    break;
            }
        }
        return textFormat;
    }

    public TextComponent getRegionFormat(CTPlayer chatPlayer, String message, boolean toKorean, ItemStack showItem) {
        String[] spLitFormat = ChatColor.translateAlternateColorCodes('&', CTCore.cf.getString("RegionChatFormat")).
                replace("[MESSAGE]", "[SPLIT][MESSAGE][SPLIT]").
                replace("[PLAYERGROUP]", "[SPLIT][PLAYERGROUP][SPLIT]").
                replace("[PLAYERNAME]", "[SPLIT][PLAYERNAME][SPLIT]").
                replace("[PLAYERNUM]", "[SPLIT][PLAYERNUM][SPLIT]").
                split("\\[SPLIT\\]");
        TextComponent textFormat = new TextComponent();
        for(String splitF : spLitFormat) {
            switch(splitF) {
                case "[MESSAGE]":
                    String itemShowText = CTCore.cf.getString("ItemShowText", "[i]");
                    String[] spLitMessage = message.replace(itemShowText, "[SPLIT]" + itemShowText + "[SPLIT]").split("\\[SPLIT\\]");
                    TextComponent textMessage = new TextComponent();
                    for(String spLitM : spLitMessage) {
                        if(spLitM.equals(itemShowText)) {
                            if(showItem.getType().equals(Material.AIR)) {
                                textMessage.addExtra(chatPlayer.getHand());
                            } else {
                                textMessage.addExtra(ChatAPI.getItem(showItem).getItem());
                            }
                        } else {
                            if(toKorean) {
                                Inko inko = new Inko();
                                spLitM = inko.en2ko(spLitM, true);
                            }
                            if(chatPlayer.getPlayer().hasPermission(CTCore.cf.getString("AdminPermission"))) {
                                textMessage.addExtra(ChatColor.translateAlternateColorCodes('&', spLitM));
                            } else {
                                TextComponent chat = new TextComponent(spLitM);
                                chat.setColor(chatPlayer.getChatColor());
                                textFormat.addExtra(chat);
                            }
                        }
                    }
                    textFormat.addExtra(textMessage);
                    break;
                case "[PLAYERGROUP]":
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', CTCore.getChat().getPlayerPrefix(chatPlayer.getPlayer())));
                    break;
                case "[PLAYERNAME]":
                    TextComponent nickName = new TextComponent(chatPlayer.getPlayer().getDisplayName());
                    nickName.setColor(chatPlayer.getNickColor());
                    textFormat.addExtra(nickName);
                    break;
                case "[PLAYERNUM]":
                    TextComponent pNum = new TextComponent(String.valueOf(getRegionNumbers(chatPlayer)));
                    pNum.setColor(ChatColor.valueOf(CTCore.cf.getString("PlayerNumberColor", "WHITE")));
                    textFormat.addExtra(pNum);
                    break;
                default:
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', splitF));
                    break;
            }
        }
        return textFormat;
    }

    /* CTItem 전용메소드 */

    public TextComponent getItemText(ItemStack targetItem) {
        TextComponent textItem = new TextComponent(getItemFormat(targetItem));
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(targetItem);
        NBTTagCompound nbt = new NBTTagCompound();
        nmsItem.save(nbt);
        BaseComponent[] nbtText = {new TextComponent(nbt.toString())};
        textItem.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, nbtText));
        return textItem;
    }

    public String getItemName(ItemStack targetItem) {
        String[] itemType = targetItem.getType().name().toLowerCase().replace("_", "_|").split("\\|");
        String itemName = "";
        for(String type : itemType)
            itemName = itemName + type.substring(0,1).toUpperCase() + type.substring(1);
        itemName = itemName.replace("_", " ");
        return targetItem.hasItemMeta() ? targetItem.getItemMeta().hasDisplayName() ?
                targetItem.getItemMeta().getDisplayName() : itemName : itemName;
    };

    public String getItemFormat(ItemStack targetItem) {
        return ChatColor.translateAlternateColorCodes('&', CTCore.cf.getString("ItemShowChat")
                .replace("[ITEMNAME]", getItemName(targetItem)).replace("[AMOUNT]", String.valueOf(getItemAmount(targetItem))));
    }

    public int getItemAmount(ItemStack targetItem) {
        return targetItem.getAmount();
    }

}

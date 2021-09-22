package com.stuudent.Chat.data;

import com.Zrips.CMI.Containers.CMIUser;
import com.earth2me.essentials.User;
import com.github.kimcore.inko.Inko;
import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.ChatCore;
import com.stuudent.Chat.enums.ChannelType;
import com.stuudent.Chat.enums.MegaPhoneType;
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

public class AllData {

    public static YamlConfiguration playerData;
    public static YamlConfiguration tempPlayerData;
    public static YamlConfiguration megaPhoneData;
    public static File playerDataFile;
    public static File tempPlayerDataFile;
    public static File megaPhoneDataFile;

    static {
        playerDataFile = new File("plugins/" + ChatCore.instance.getName() + "/playerData.yml");
        tempPlayerDataFile = new File("plugins/" + ChatCore.instance.getName() + "/tempPlayerData.yml");
        megaPhoneDataFile = new File("plugins/" + ChatCore.instance.getName() + "/megaPhoneData.yml");
        playerData = YamlConfiguration.loadConfiguration(playerDataFile);
        tempPlayerData = YamlConfiguration.loadConfiguration(tempPlayerDataFile);
        megaPhoneData = YamlConfiguration.loadConfiguration(megaPhoneDataFile);
    }

    /* CTPlayer 전용 메소드 */

    public boolean isIgnored(Player chatPlayer, Player targetPlayer) {
        if(ChatCore.ess != null) {
            User targetUser = ChatCore.ess.getUser(targetPlayer);
            User chatUser = ChatCore.ess.getUser(chatPlayer);
            return chatUser.isIgnoredPlayer(targetUser);
        }
        else if(ChatCore.cmi != null) {
            CMIUser cmiUser = ChatCore.cmi.getPlayerManager().getUser(chatPlayer);
            return cmiUser.isIgnoring(targetPlayer.getUniqueId());
        }
        else {
            return false;
        }
    }

    public void save() {
        try {
            playerData.save(playerDataFile);
            megaPhoneData.save(megaPhoneDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setChannel(Player chatPlayer, ChannelType targetChannelType) {
        playerData.set(chatPlayer.getUniqueId().toString() + ".CHANNEL", targetChannelType.name());
    }

    public ChannelType getChannel(Player chatPlayer) {
        String textChannel = playerData.getString(chatPlayer.getUniqueId().toString() + ".CHANNEL", "GLOBAL");
        return ChannelType.valueOf(textChannel);
    }

    public void setTime(Player chatPlayer) {
        tempPlayerData.set(chatPlayer.getUniqueId().toString() + ".ITEM", Instant.now().getEpochSecond());
    }

    public long getTime(Player chatPlayer) {
        return tempPlayerData.getLong(chatPlayer.getUniqueId().toString() + ".ITEM", 0);
    }

    public long getLeftTime(Player chatPlayer) {
        long passedTime = Instant.now().getEpochSecond()-getTime(chatPlayer);
        return (getCoolTime() - passedTime);
    }

    public int getCoolTime() {
        return ChatCore.cf.getInt("ItemShowCoolTime", 60);
    }

    public void setChatColor(Player chatPlayer, ChatColor targetColor) {
        playerData.set(chatPlayer.getUniqueId().toString() + ".CHATCOLOR", targetColor.name());
    }

    public ChatColor getChatColor(Player chatPlayer) {
        String textColor = playerData.getString(chatPlayer.getUniqueId().toString() + ".CHATCOLOR", ChatCore.cf.getString("DefaultChatColor", "WHITE"));
        return ChatColor.valueOf(textColor);
    }

    public void setNickColor(Player chatPlayer, ChatColor targetColor) {
        playerData.set(chatPlayer.getUniqueId().toString() + ".NICKCOLOR", targetColor.name());
    }

    public ChatColor getNickColor(Player chatPlayer) {
        String textColor = playerData.getString(chatPlayer.getUniqueId().toString() + ".NICKCOLOR", ChatCore.cf.getString("DefaultNickColor", "WHITE"));
        return ChatColor.valueOf(textColor);
    }

    public TextComponent getHandText(Player handPlayer) {
        String yourHand = ChatColor.translateAlternateColorCodes('&',
                ChatCore.cf.getString("HandShowChat").replace("[PLAYER]", handPlayer.getDisplayName()));
        TextComponent textHand = new TextComponent(yourHand);
        List<BaseComponent> HoverText = new ArrayList<>();
        for (String HT : ChatCore.cf.getStringList("HandShowLore"))
            HoverText.add(new TextComponent(ChatColor.translateAlternateColorCodes('&', HT.replace("[PLAYER]", handPlayer.getDisplayName()))));
        BaseComponent[] HoverTexts = HoverText.toArray(new BaseComponent[HoverText.size()]);
        textHand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, HoverTexts));
        return textHand;
    }

    public boolean isKorean(Player chatPlayer) {
        return playerData.getBoolean(chatPlayer.getUniqueId().toString() + ".KOREANCHAT", false);
    }

    public void setKorean(Player chatPlayer, boolean state) {
        playerData.set(chatPlayer.getUniqueId().toString() + ".KOREANCHAT", state);
    }

    public void setMegaPhoneCool(MegaPhoneType megaPhoneType, Player chatPlayer) {
        if(megaPhoneType.equals(MegaPhoneType.NORMAL)) {
            tempPlayerData.set(chatPlayer.getUniqueId().toString() + ".MEGAPHONE_NORMAL", Instant.now().getEpochSecond());
        } else if(megaPhoneType.equals(MegaPhoneType.ADVANCED)) {
            tempPlayerData.set(chatPlayer.getUniqueId().toString() + ".MEGAPHONE_ADVANCED", Instant.now().getEpochSecond());
        }
    }

    public long getMegaPhoneCoolTime(MegaPhoneType megaPhoneType, Player chatPlayer) {
        if(megaPhoneType.equals(MegaPhoneType.NORMAL)) {
            return ChatCore.cf.getInt("MegaPhoneNormalCoolTime", 0) -
                    (Instant.now().getEpochSecond() - tempPlayerData.getLong(chatPlayer.getUniqueId().toString() + ".MEGAPHONE_NORMAL", 0));
        } else if(megaPhoneType.equals(MegaPhoneType.ADVANCED)) {
            return ChatCore.cf.getInt("MegaPhoneAdvancedCoolTime", 0) -
                    (Instant.now().getEpochSecond() - tempPlayerData.getLong(chatPlayer.getUniqueId().toString() + ".MEGAPHONE_ADVANCED", 0));
        } else {
            return 0;
        }
    }

    public boolean isMegaPhoneCool(MegaPhoneType megaPhoneType, Player chatPlayer) {
        return getMegaPhoneCoolTime(megaPhoneType, chatPlayer) > 0;
    }

    public TextComponent getMegaPhoneMessage(MegaPhoneType megaPhoneType, Player player, String message) {
        if(megaPhoneType.equals(MegaPhoneType.NORMAL)) {
            TextComponent sendTcMessage = new TextComponent();
            String[] sendTexts = ChatCore.cf.getString("MegaPhoneNormalSend").replace("[PLAYER]", "[SPLIT][PLAYER][SPLIT]").
                    replace("[MESSAGE]", "[SPLIT][MESSAGE][SPLIT]").split("\\[SPLIT\\]");
            for(String sendText : sendTexts) {
                if(sendText.equals("[PLAYER]")) {
                    sendTcMessage.addExtra(new TextComponent(player.getDisplayName()));
                }
                else if(sendText.equals("[MESSAGE]")) {
                    TextComponent tcMessage = new TextComponent();
                    String[] messages = message.replace(ChatCore.cf.getString("ItemShowText"),
                            "[SPLIT]" + ChatCore.cf.getString("ItemShowText") + "[SPLIT]").split("\\[SPLIT\\]");
                    for(String text : messages) {
                        if(text.equals(ChatCore.cf.getString("ItemShowText"))) {
                            TextComponent addExtra = player.getInventory().getItemInMainHand().getType().equals(Material.AIR) ?
                                    ChatAPI.getPlayer(player).getHand() : ChatAPI.getItem(player.getInventory().getItemInMainHand()).getItem();
                            tcMessage.addExtra(addExtra);
                        }
                        else {
                            TextComponent addExtra = new TextComponent(text);
                            addExtra.setColor(ChatColor.valueOf(ChatCore.cf.getString("MegaPhoneNormalColor")));
                            tcMessage.addExtra(addExtra);
                        }
                    }
                    sendTcMessage.addExtra(tcMessage);
                }
                else {
                    sendTcMessage.addExtra(ChatColor.translateAlternateColorCodes('&', sendText));
                }
            }
            return sendTcMessage;
        }
        else if(megaPhoneType.equals(MegaPhoneType.ADVANCED)) {
            TextComponent sendTcMessage = new TextComponent();
            String[] sendTexts = ChatCore.cf.getString("MegaPhoneAdvancedSend").replace("[PLAYER]", "[SPLIT][PLAYER][SPLIT]").
                    replace("[MESSAGE]", "[SPLIT][MESSAGE][SPLIT]").split("\\[SPLIT\\]");
            for(String sendText : sendTexts) {
                if(sendText.equals("[PLAYER]")) {
                    sendTcMessage.addExtra(new TextComponent(player.getDisplayName()));
                }
                else if(sendText.equals("[MESSAGE]")) {
                    TextComponent tcMessage = new TextComponent();
                    String[] messages = message.toString().replace(ChatCore.cf.getString("ItemShowText"),
                            "[SPLIT]" + ChatCore.cf.getString("ItemShowText") + "[SPLIT]").split("\\[SPLIT\\]");
                    for(String text : messages) {
                        if(text.equals(ChatCore.cf.getString("ItemShowText"))) {
                            TextComponent addExtra = player.getInventory().getItemInMainHand().getType().equals(Material.AIR) ?
                                    ChatAPI.getPlayer(player).getHand() : ChatAPI.getItem(player.getInventory().getItemInMainHand()).getItem();
                            tcMessage.addExtra(addExtra);
                        }
                        else {
                            TextComponent addExtra = new TextComponent(text);
                            addExtra.setColor(ChatColor.valueOf(ChatCore.cf.getString("MegaPhoneAdvancedColor")));
                            tcMessage.addExtra(addExtra);
                        }
                    }
                    sendTcMessage.addExtra(tcMessage);
                }
                else {
                    sendTcMessage.addExtra(ChatColor.translateAlternateColorCodes('&', sendText));
                }
            }
            return sendTcMessage;
        }
        else {
            return null;
        }
    }

    /* CTGlobal 전용메소드 */

    public List<Player> getGlobalPlayers(ChatPlayerData chatPlayer) {
        List<Player> globalPlayers = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            ChatPlayerData cp = ChatAPI.getPlayer(p);
            if(cp.isIgnored(chatPlayer.getPlayer()))
                continue;
            globalPlayers.add(p);
        }
        return globalPlayers;
    }

    public TextComponent getGlobalFormat(ChatPlayerData chatPlayer, String message, boolean toKorean) {
        String[] spLitFormat = ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("GlobalChatFormat")).
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
                    if(chatPlayer.getPlayer().hasPermission(ChatCore.cf.getString("AdminPermission"))) {
                        textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', message));
                    } else {
                        TextComponent textMessage = new TextComponent(message);
                        textMessage.setColor(chatPlayer.getChatColor());
                        textFormat.addExtra(textMessage);
                    }
                    break;
                case "[PLAYERGROUP]":
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', ChatCore.getChat().getPlayerPrefix(chatPlayer.getPlayer())));
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

    public TextComponent getGlobalFormat(ChatPlayerData chatPlayer, String message, boolean toKorean, ItemStack showItem) {
        String[] spLitFormat = ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("GlobalChatFormat")).
                replace("[MESSAGE]", "[SPLIT][MESSAGE][SPLIT]").
                replace("[PLAYERGROUP]", "[SPLIT][PLAYERGROUP][SPLIT]").
                replace("[PLAYERNAME]", "[SPLIT][PLAYERNAME][SPLIT]").
                split("\\[SPLIT\\]");
        TextComponent textFormat = new TextComponent();
        for(String spLitF : spLitFormat) {
            switch(spLitF) {
                case "[MESSAGE]":
                    String itemShowText = ChatCore.cf.getString("ItemShowText", "[i]");
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
                            if(chatPlayer.getPlayer().hasPermission(ChatCore.cf.getString("AdminPermission"))) {
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
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', ChatCore.getChat().getPlayerPrefix(chatPlayer.getPlayer())));
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

    public List<Player> getRegionPlayers(ChatPlayerData chatPlayer) {
        List<Player> regionPlayers = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(isInRegion(chatPlayer, p)) {
                ChatPlayerData cp = ChatAPI.getPlayer(p);
                if(cp.isIgnored(chatPlayer.getPlayer()))
                    continue;
                regionPlayers.add(p);
            }
        }
        return regionPlayers;
    }

    public int getRegionNumbers(ChatPlayerData chatPlayer) {
        return getRegionPlayers(chatPlayer).size();
    }

    public boolean isInRegion(ChatPlayerData chatPlayer, Player targetPlayer) {
        return (getDistance(chatPlayer, targetPlayer) <= ChatCore.cf.getDouble("RegionChatDistance", 300));
    }

    public double getDistance(ChatPlayerData chatPlayer, Player targetPlayer) {
        return chatPlayer.getPlayer().getLocation().distance(targetPlayer.getLocation());
    }

    public TextComponent getRegionFormat(ChatPlayerData chatPlayer, String message, boolean toKorean) {
        String[] spLitFormat = ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("RegionChatFormat")).
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
                    if(chatPlayer.getPlayer().hasPermission(ChatCore.cf.getString("AdminPermission"))) {
                        textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', message));
                    } else {
                        TextComponent textMessage = new TextComponent(message);
                        textMessage.setColor(chatPlayer.getChatColor());
                        textFormat.addExtra(textMessage);
                    }
                    break;
                case "[PLAYERGROUP]":
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', ChatCore.getChat().getPlayerPrefix(chatPlayer.getPlayer())));
                    break;
                case "[PLAYERNAME]":
                    TextComponent nickName = new TextComponent(chatPlayer.getPlayer().getDisplayName());
                    nickName.setColor(chatPlayer.getNickColor());
                    textFormat.addExtra(nickName);
                    break;
                case "[PLAYERNUM]":
                    TextComponent pNum = new TextComponent(String.valueOf(getRegionNumbers(chatPlayer)));
                    pNum.setColor(ChatColor.valueOf(ChatCore.cf.getString("PlayerNumberColor", "WHITE")));
                    textFormat.addExtra(pNum);
                    break;
                default:
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', splitF));
                    break;
            }
        }
        return textFormat;
    }

    public TextComponent getRegionFormat(ChatPlayerData chatPlayer, String message, boolean toKorean, ItemStack showItem) {
        String[] spLitFormat = ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("RegionChatFormat")).
                replace("[MESSAGE]", "[SPLIT][MESSAGE][SPLIT]").
                replace("[PLAYERGROUP]", "[SPLIT][PLAYERGROUP][SPLIT]").
                replace("[PLAYERNAME]", "[SPLIT][PLAYERNAME][SPLIT]").
                replace("[PLAYERNUM]", "[SPLIT][PLAYERNUM][SPLIT]").
                split("\\[SPLIT\\]");
        TextComponent textFormat = new TextComponent();
        for(String splitF : spLitFormat) {
            switch(splitF) {
                case "[MESSAGE]":
                    String itemShowText = ChatCore.cf.getString("ItemShowText", "[i]");
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
                            if(chatPlayer.getPlayer().hasPermission(ChatCore.cf.getString("AdminPermission"))) {
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
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', ChatCore.getChat().getPlayerPrefix(chatPlayer.getPlayer())));
                    break;
                case "[PLAYERNAME]":
                    TextComponent nickName = new TextComponent(chatPlayer.getPlayer().getDisplayName());
                    nickName.setColor(chatPlayer.getNickColor());
                    textFormat.addExtra(nickName);
                    break;
                case "[PLAYERNUM]":
                    TextComponent pNum = new TextComponent(String.valueOf(getRegionNumbers(chatPlayer)));
                    pNum.setColor(ChatColor.valueOf(ChatCore.cf.getString("PlayerNumberColor", "WHITE")));
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
        return ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("ItemShowChat")
                .replace("[ITEMNAME]", getItemName(targetItem)).replace("[AMOUNT]", String.valueOf(getItemAmount(targetItem))));
    }

    public int getItemAmount(ItemStack targetItem) {
        return targetItem.getAmount();
    }

    /* 확성기 데이터 */
    public void setMegaPhonePrice(MegaPhoneType megaPhoneType, double megaPhonePrice) {
        megaPhoneData.set(megaPhoneType.name() + ".PRICE", megaPhonePrice);
    }

    public double getMegaPhonePrice(MegaPhoneType megaPhoneType) {
        return megaPhoneData.getDouble(megaPhoneType.name() + ".PRICE", 0);
    }

    public void setMegaPhoneEnable() {
        megaPhoneData.set("AVAILABLE", true);
    }

    public void setMegaPhoneDisable() {
        megaPhoneData.set("AVAILABLE", false);
    }

    public boolean isMegaPhoneEnabled() {
        return megaPhoneData.getBoolean("AVAILABLE", false);
    }

}

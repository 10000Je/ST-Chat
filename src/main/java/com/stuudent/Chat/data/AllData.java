package com.stuudent.Chat.data;

import com.Zrips.CMI.CMI;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.github.kimcore.inko.Inko;
import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.ChatCore;
import com.stuudent.Chat.enums.ChannelType;
import com.stuudent.Chat.enums.MegaPhoneType;
import com.stuudent.Chat.utils.EssentialsUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AllData {

    public static YamlConfiguration playerData;
    public static YamlConfiguration tempPlayerData;
    public static YamlConfiguration megaPhoneData;
    public static File playerDataFile;
    public static File megaPhoneDataFile;

    static {
        playerDataFile = new File("plugins/" + ChatCore.instance.getName() + "/playerData.yml");
        megaPhoneDataFile = new File("plugins/" + ChatCore.instance.getName() + "/megaPhoneData.yml");
        playerData = YamlConfiguration.loadConfiguration(playerDataFile);
        tempPlayerData = new YamlConfiguration();
        megaPhoneData = YamlConfiguration.loadConfiguration(megaPhoneDataFile);
    }

    /* CTPlayer 전용 메소드 */

    public boolean isIgnored(Player chatPlayer, Player targetPlayer) {
        if(ChatCore.cmi) {
            return CMI.getInstance().getPlayerManager().getUser(chatPlayer).isIgnoring(targetPlayer.getUniqueId());
        }
        else if(ChatCore.ess) {
            return EssentialsUtil.isIgnoredPlayer(chatPlayer, targetPlayer);
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
        String yourHand = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(handPlayer, ChatCore.cf.getString("HandShowChat")));
        TextComponent textHand = new TextComponent(yourHand);
        List<BaseComponent> HoverText = new ArrayList<>();
        for(int i=0; i < ChatCore.cf.getStringList("HandShowLore").size(); i++) {
            if(i == ChatCore.cf.getStringList("HandShowLore").size()-1) {
                HoverText.add(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                        PlaceholderAPI.setPlaceholders(handPlayer, ChatCore.cf.getStringList("HandShowLore").get(i)))));
            } else {
                HoverText.add(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                        PlaceholderAPI.setPlaceholders(handPlayer, ChatCore.cf.getStringList("HandShowLore").get(i)) + "\n")));
            }
        }
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
            String[] sendTexts = ChatCore.cf.getString("MegaPhoneNormalSend").
                    replace("%megaphone_message%", "%split%%megaphone_message%%split%").split("%split%");
            for(String sendText : sendTexts) {
                if(sendText.equals("%megaphone_message%")) {
                    TextComponent tcMessage = new TextComponent();
                    String[] messages = message.replace(ChatCore.cf.getString("ItemShowText"),
                            "%split%" + ChatCore.cf.getString("ItemShowText") + "%split%").split("%split%");
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
                    sendTcMessage.addExtra(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, sendText)));
                }
            }
            return sendTcMessage;
        }
        else if(megaPhoneType.equals(MegaPhoneType.ADVANCED)) {
            TextComponent sendTcMessage = new TextComponent();
            String[] sendTexts = ChatCore.cf.getString("MegaPhoneAdvancedSend").
                    replace("%megaphone_message%", "%split%%megaphone_message%%split%").split("%split%");
            for(String sendText : sendTexts) {
                if(sendText.equals("%megaphone_message%")) {
                    TextComponent tcMessage = new TextComponent();
                    String[] messages = message.toString().replace(ChatCore.cf.getString("ItemShowText"),
                            "%split%" + ChatCore.cf.getString("ItemShowText") + "%split%").split("%split%");
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
                    sendTcMessage.addExtra(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, sendText)));
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
                replace("%chat_message%", "%split%%chat_message%%split%").
                split("%split%");
        TextComponent textFormat = new TextComponent();
        for(String spLitF : spLitFormat) {
            if (spLitF.equals("%chat_message%")) {
                if (toKorean) {
                    Inko inko = new Inko();
                    message = inko.en2ko(message, true);
                }
                if (chatPlayer.getPlayer().hasPermission(ChatCore.cf.getString("AdminPermission"))) {
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', message));
                } else {
                    TextComponent textMessage = new TextComponent(message);
                    textMessage.setColor(chatPlayer.getChatColor());
                    textFormat.addExtra(textMessage);
                }
            } else {
                textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(chatPlayer.getPlayer(), spLitF)));
            }
        }
        return textFormat;
    }

    public TextComponent getGlobalFormat(ChatPlayerData chatPlayer, String message, boolean toKorean, ItemStack showItem) {
        String[] spLitFormat = ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("GlobalChatFormat")).
                replace("%chat_message%", "%split%%chat_message%%split%").
                split("%split%");
        TextComponent textFormat = new TextComponent();
        for(String spLitF : spLitFormat) {
            if (spLitF.equals("%chat_message%")) {
                String itemShowText = ChatCore.cf.getString("ItemShowText", "[i]");
                String[] spLitMessage = message.replace(itemShowText, "%split%" + itemShowText + "%split%").split("%split%");
                TextComponent textMessage = new TextComponent();
                for (String spLitM : spLitMessage) {
                    if (spLitM.equals(itemShowText)) {
                        if (showItem.getType().equals(Material.AIR)) {
                            textMessage.addExtra(chatPlayer.getHand());
                        } else {
                            textMessage.addExtra(ChatAPI.getItem(showItem).getItem());
                        }
                    } else {
                        if (toKorean) {
                            Inko inko = new Inko();
                            spLitM = inko.en2ko(spLitM, true);
                        }
                        if (chatPlayer.getPlayer().hasPermission(ChatCore.cf.getString("AdminPermission"))) {
                            textMessage.addExtra(ChatColor.translateAlternateColorCodes('&', spLitM));
                        } else {
                            TextComponent chat = new TextComponent(spLitM);
                            chat.setColor(chatPlayer.getChatColor());
                            textFormat.addExtra(chat);
                        }
                    }
                }
                textFormat.addExtra(textMessage);
            } else {
                textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(chatPlayer.getPlayer(), spLitF)));
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
        if(chatPlayer.getPlayer().getWorld().equals(targetPlayer.getWorld())) {
            return (getDistance(chatPlayer, targetPlayer) <= ChatCore.cf.getDouble("RegionChatDistance", 300));
        } else {
            return false;
        }
    }

    public double getDistance(ChatPlayerData chatPlayer, Player targetPlayer) {
        return chatPlayer.getPlayer().getLocation().distance(targetPlayer.getLocation());
    }

    public TextComponent getRegionFormat(ChatPlayerData chatPlayer, String message, boolean toKorean) {
        String[] spLitFormat = ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("RegionChatFormat")).
                replace("%chat_message%", "%split%%chat_message%%split%").
                replace("%region_number%", "%split%%region_number%%split%").
                split("%split%");
        TextComponent textFormat = new TextComponent();
        for(String splitF : spLitFormat) {
            switch(splitF) {
                case "%chat_message%":
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
                case "%region_number%":
                    TextComponent pNum = new TextComponent(String.valueOf(getRegionNumbers(chatPlayer)));
                    pNum.setColor(ChatColor.valueOf(ChatCore.cf.getString("PlayerNumberColor", "WHITE")));
                    List<TextComponent> regionPlayers = new ArrayList<>();
                    for(int i=0; i < getRegionNumbers(chatPlayer); i++) {
                        if(i == getRegionNumbers(chatPlayer) - 1) {
                            regionPlayers.add(new TextComponent(getRegionPlayers(chatPlayer).get(i).getDisplayName()));
                        } else {
                            regionPlayers.add(new TextComponent(getRegionPlayers(chatPlayer).get(i).getDisplayName() + "\n"));
                        }
                    }
                    BaseComponent[] regionBase = regionPlayers.toArray(new BaseComponent[regionPlayers.size()]);
                    pNum.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, regionBase));
                    textFormat.addExtra(pNum);
                    break;
                default:
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(chatPlayer.getPlayer(), splitF)));
                    break;
            }
        }
        return textFormat;
    }

    public TextComponent getRegionFormat(ChatPlayerData chatPlayer, String message, boolean toKorean, ItemStack showItem) {
        String[] spLitFormat = ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("RegionChatFormat")).
                replace("%chat_message%", "%split%%chat_message%%split%").
                replace("%region_number%", "%split%%region_number%%split%").
                split("%split%");
        TextComponent textFormat = new TextComponent();
        for(String splitF : spLitFormat) {
            switch(splitF) {
                case "%chat_message%":
                    String itemShowText = ChatCore.cf.getString("ItemShowText", "[i]");
                    String[] spLitMessage = message.replace(itemShowText, "%split%" + itemShowText + "%split%").split("%split%");
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
                case "%region_number%":
                    TextComponent pNum = new TextComponent(String.valueOf(getRegionNumbers(chatPlayer)));
                    pNum.setColor(ChatColor.valueOf(ChatCore.cf.getString("PlayerNumberColor", "WHITE")));
                    List<TextComponent> regionPlayers = new ArrayList<>();
                    for(int i=0; i < getRegionNumbers(chatPlayer); i++) {
                        if(i == getRegionNumbers(chatPlayer) - 1) {
                            regionPlayers.add(new TextComponent(getRegionPlayers(chatPlayer).get(i).getDisplayName()));
                        } else {
                            regionPlayers.add(new TextComponent(getRegionPlayers(chatPlayer).get(i).getDisplayName() + "\n"));
                        }
                    }
                    BaseComponent[] regionBase = regionPlayers.toArray(new BaseComponent[regionPlayers.size()]);
                    pNum.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, regionBase));
                    textFormat.addExtra(pNum);
                    break;
                default:
                    textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(chatPlayer.getPlayer(), splitF)));
                    break;
            }
        }
        return textFormat;
    }

    /* CTItem 전용메소드 */

    public TextComponent getItemText(ItemStack targetItem) {
        try {
            if(ChatCore.pcl) {
                TextComponent textItem = new TextComponent(getItemFormat(targetItem));
                Object nmsItem = MinecraftReflection.getMinecraftItemStack(targetItem);
                Object nbt = MinecraftReflection.getNBTCompoundClass().newInstance();
                Method save = MinecraftReflection.getItemStackClass().getDeclaredMethod("save", MinecraftReflection.getNBTCompoundClass());
                save.invoke(nmsItem, nbt);
                BaseComponent[] nbtText = {new TextComponent(nbt.toString())};
                textItem.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, nbtText));
                return textItem;
            } else {
                return new TextComponent(getItemFormat(targetItem));
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return new TextComponent(getItemFormat(targetItem));
        }
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
                .replace("%item_name%", getItemName(targetItem)).replace("%item_amount%", String.valueOf(getItemAmount(targetItem))));
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

    /* 공지사항 관련 메소드 */

    public List<TextComponent> getBroadcastMessage(Player chatPlayer, String message) {
        List<TextComponent> textComponents = new ArrayList<>();
        for(String broadcastMessage : ChatCore.cf.getStringList("BroadcastMessage")) {
            TextComponent eachTextComponent = new TextComponent();
            String[] sendTexts = broadcastMessage.replace("%broadcast_message%", "%split%%broadcast_message%%split%").split("%split%");
            for(String sendText : sendTexts) {
                if(sendText.equals("%broadcast_message%")) {
                    TextComponent itemIncluded = new TextComponent();
                    String[] itemMessages = message.replace(ChatCore.cf.getString("ItemShowText"),
                            "%split%" + ChatCore.cf.getString("ItemShowText") + "%split%").split("%split%");
                    for(String itemMessage : itemMessages) {
                        if(itemMessage.equals(ChatCore.cf.getString("ItemShowText"))) {
                            TextComponent addExtra = chatPlayer.getInventory().getItemInMainHand().getType().equals(Material.AIR) ?
                                    ChatAPI.getPlayer(chatPlayer).getHand() : ChatAPI.getItem(chatPlayer.getInventory().getItemInMainHand()).getItem();
                            itemIncluded.addExtra(addExtra);
                        }
                        else {
                            TextComponent addExtra = new TextComponent(ChatColor.translateAlternateColorCodes('&', itemMessage));
                            itemIncluded.addExtra(addExtra);
                        }
                    }
                    eachTextComponent.addExtra(itemIncluded);
                }
                else {
                    eachTextComponent.addExtra(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(chatPlayer, sendText)));
                }
            }
            textComponents.add(eachTextComponent);
        }
        return textComponents;
    }

}

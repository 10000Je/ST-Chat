package com.stuudent.Chat.data;

import com.Zrips.CMI.CMI;
import com.github.kimcore.inko.Inko;
import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.ChatCore;
import com.stuudent.Chat.enums.ChannelType;
import com.stuudent.Chat.enums.MegaPhoneType;
import com.stuudent.Chat.interfaces.ChatItem;
import com.stuudent.Chat.interfaces.ChatPlayer;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PlayerData implements ChatPlayer {

    public static File playerDataFile;
    public static YamlConfiguration playerData;
    public static YamlConfiguration tempData;

    public Player chatPlayer;

    static {
        playerDataFile = new File("plugins/" + ChatCore.instance.getName() + "/playerData.yml");
        playerData = YamlConfiguration.loadConfiguration(playerDataFile);
        tempData = new YamlConfiguration();
    }

    public static void save() {
        try {
            playerData.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerData(Player chatPlayer) {
        this.chatPlayer = chatPlayer;
    }

    @Override
    public Player getBukkitPlayer() {
        return this.chatPlayer;
    }

    @Override
    public boolean isIgnored(Player target) {
        if(ChatCore.cmi) {
            return CMI.getInstance().getPlayerManager().getUser(chatPlayer).isIgnoring(target.getUniqueId());
        }
        else if(ChatCore.ess) {
            return EssentialsUtil.isIgnoredPlayer(chatPlayer, target);
        }
        else {
            return false;
        }
    }

    @Override
    public void setChannel(ChannelType channel) {
        playerData.set(chatPlayer.getUniqueId().toString() + ".CHANNEL", channel.name());
    }

    @Override
    public ChannelType getChannel() {
        String textChannel = playerData.getString(chatPlayer.getUniqueId().toString() + ".CHANNEL", "GLOBAL");
        return ChannelType.valueOf(textChannel);
    }

    @Override
    public void resetShowItemCool() {
        tempData.set(chatPlayer.getUniqueId().toString() + ".LAST_TIME_SHOWED_ITEM", Instant.now().getEpochSecond());
    }

    @Override
    public long getShowItemCool() {
        return ChatCore.cf.getLong("ItemShowCoolTime", 60) - (Instant.now().getEpochSecond() - tempData.getLong(chatPlayer.getUniqueId().toString() + ".LAST_TIME_SHOWED_ITEM", 0));
    }

    @Override
    public boolean isShowItemCool() {
        return getShowItemCool() > 0;
    }

    @Override
    public void setChatColor(ChatColor color) {
        playerData.set(chatPlayer.getUniqueId().toString() + ".CHAT_COLOR", color.name());
    }

    @Override
    public ChatColor getChatColor() {
        String textColor = playerData.getString(chatPlayer.getUniqueId().toString() + ".CHAT_COLOR", ChatCore.cf.getString("DefaultChatColor", "WHITE"));
        return ChatColor.valueOf(textColor);
    }

    @Override
    public TextComponent getHandText() {
        String yourHand = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(this.chatPlayer, ChatCore.cf.getString("HandShowChat")));
        TextComponent textHand = new TextComponent(yourHand);
        List<BaseComponent> HoverText = new ArrayList<>();
        for(int i=0; i < ChatCore.cf.getStringList("HandShowLore").size(); i++) {
            if(i == ChatCore.cf.getStringList("HandShowLore").size()-1) {
                HoverText.add(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                        PlaceholderAPI.setPlaceholders(this.chatPlayer, ChatCore.cf.getStringList("HandShowLore").get(i)))));
            } else {
                HoverText.add(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                        PlaceholderAPI.setPlaceholders(this.chatPlayer, ChatCore.cf.getStringList("HandShowLore").get(i)) + "\n")));
            }
        }
        BaseComponent[] HoverTexts = HoverText.toArray(new BaseComponent[0]);
        textHand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, HoverTexts));
        return textHand;
    }

    @Override
    public boolean isKoreanChatEnabled() {
        return playerData.getBoolean(chatPlayer.getUniqueId().toString() + ".KOREAN_CHAT", false);
    }

    @Override
    public void setKoreanChatState(boolean state) {
        playerData.set(chatPlayer.getUniqueId().toString() + ".KOREAN_CHAT", state);
    }

    @Override
    public void resetMegaPhoneCool(MegaPhoneType megaPhone) {
        if(megaPhone.equals(MegaPhoneType.NORMAL)) {
            tempData.set(chatPlayer.getUniqueId().toString() + ".LAST_TIME_MEGAPHONE_NORMAL", Instant.now().getEpochSecond());
        } else if(megaPhone.equals(MegaPhoneType.ADVANCED)) {
            tempData.set(chatPlayer.getUniqueId().toString() + ".LAST_TIME_MEGAPHONE_ADVANCED", Instant.now().getEpochSecond());
        }
    }

    @Override
    public long getMegaPhoneCool(MegaPhoneType megaPhone) {
        if(megaPhone.equals(MegaPhoneType.NORMAL)) {
            return ChatCore.cf.getInt("MegaPhoneNormalCoolTime", 0) -
                    (Instant.now().getEpochSecond() - tempData.getLong(chatPlayer.getUniqueId().toString() + ".LAST_TIME_MEGAPHONE_NORMAL", 0));
        } else if(megaPhone.equals(MegaPhoneType.ADVANCED)) {
            return ChatCore.cf.getInt("MegaPhoneAdvancedCoolTime", 0) -
                    (Instant.now().getEpochSecond() - tempData.getLong(chatPlayer.getUniqueId().toString() + ".LAST_TIME_MEGAPHONE_ADVANCED", 0));
        } else {
            return 0;
        }
    }

    @Override
    public boolean isMegaPhoneCool(MegaPhoneType megaPhone) {
        return getMegaPhoneCool(megaPhone) > 0;
    }

    @Override
    public TextComponent getMegaPhoneFormat(MegaPhoneType megaPhone, String message) {
        if(megaPhone.equals(MegaPhoneType.NORMAL)) {
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
                            TextComponent addExtra = this.chatPlayer.getInventory().getItemInMainHand().getType().equals(Material.AIR) ?
                                    getHandText() : ChatAPI.getItem(this.chatPlayer.getInventory().getItemInMainHand()).getText();
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
                    sendTcMessage.addExtra(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(this.chatPlayer, sendText)));
                }
            }
            return sendTcMessage;
        }
        else if(megaPhone.equals(MegaPhoneType.ADVANCED)) {
            TextComponent sendTcMessage = new TextComponent();
            String[] sendTexts = ChatCore.cf.getString("MegaPhoneAdvancedSend").
                    replace("%megaphone_message%", "%split%%megaphone_message%%split%").split("%split%");
            for(String sendText : sendTexts) {
                if(sendText.equals("%megaphone_message%")) {
                    TextComponent tcMessage = new TextComponent();
                    String[] messages = message.replace(ChatCore.cf.getString("ItemShowText"),
                            "%split%" + ChatCore.cf.getString("ItemShowText") + "%split%").split("%split%");
                    for(String text : messages) {
                        if(text.equals(ChatCore.cf.getString("ItemShowText"))) {
                            TextComponent addExtra = this.chatPlayer.getInventory().getItemInMainHand().getType().equals(Material.AIR) ?
                                    getHandText() : ChatAPI.getItem(this.chatPlayer.getInventory().getItemInMainHand()).getText();
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
                    sendTcMessage.addExtra(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(this.chatPlayer, sendText)));
                }
            }
            return sendTcMessage;
        }
        else {
            return null;
        }
    }

    @Override
    public List<Player> getChatReceivers() {
        if(getChannel().equals(ChannelType.GLOBAL)) {
            List<Player> globalPlayers = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                ChatPlayer cp = ChatAPI.getPlayer(p);
                if (cp.isIgnored(chatPlayer.getPlayer()))
                    continue;
                globalPlayers.add(p);
            }
            return globalPlayers;
        }
        else if(getChannel().equals(ChannelType.REGION)) {
            List<Player> regionPlayers = new ArrayList<>();
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(!chatPlayer.getWorld().equals(p.getWorld())) continue;
                if(chatPlayer.getLocation().distance(p.getLocation()) > ChatCore.cf.getDouble("RegionChatDistance", 300)) continue;
                ChatPlayer cp = ChatAPI.getPlayer(p);
                if(cp.isIgnored(chatPlayer.getPlayer()))
                    continue;
                regionPlayers.add(p);
            }
            return regionPlayers;
        } else {
            return null;
        }
    }

    @Override
    public TextComponent getChatFormat(String message) {
        List<String> spLitFormat = new ArrayList<>();
        if (getChannel().equals(ChannelType.GLOBAL)) {
            spLitFormat = Arrays.asList(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("GlobalChatFormat")).
                    replace("%chat_message%", "%split%%chat_message%%split%").
                    replace("%receivers_number%", "%split%%receivers_number%%split%").
                    split("%split%"));
        } else if(getChannel().equals(ChannelType.REGION)) {
            spLitFormat = Arrays.asList(ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("RegionChatFormat")).
                    replace("%chat_message%", "%split%%chat_message%%split%").
                    replace("%receivers_number%", "%split%%receivers_number%%split%").
                    split("%split%"));
        }
        TextComponent textFormat = new TextComponent();
        for(String splitF : spLitFormat) {
            if(splitF.equals("%chat_message%")) {
                String itemShowText = ChatCore.cf.getString("ItemShowText", "[i]");
                String[] spLitMessage = message.replace(itemShowText, "%split%" + itemShowText + "%split%").split("%split%");
                TextComponent textMessage = new TextComponent();
                for(String spLitM : spLitMessage) {
                    if(spLitM.equals(itemShowText)) {
                        if(this.chatPlayer.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                            textMessage.addExtra(getHandText());
                        } else {
                            textMessage.addExtra(ChatAPI.getItem(this.chatPlayer.getInventory().getItemInMainHand()).getText());
                        }
                    } else {
                        if(isKoreanChatEnabled()) {
                            Inko inko = new Inko();
                            spLitM = inko.en2ko(spLitM, true);
                        }
                        if(this.chatPlayer.hasPermission(ChatCore.cf.getString("AdminPermission"))) {
                            textMessage.addExtra(ChatColor.translateAlternateColorCodes('&', spLitM));
                        } else {
                            TextComponent chat = new TextComponent(spLitM);
                            chat.setColor(getChatColor());
                            textMessage.addExtra(chat);
                        }
                    }
                }
                textFormat.addExtra(textMessage);
            }
            else if(splitF.equals("%receivers_number%")) {
                TextComponent playerNum = new TextComponent(String.valueOf(getChatReceivers().size()));
                playerNum.setColor(ChatColor.valueOf(ChatCore.cf.getString("PlayerNumberColor", "WHITE")));
                List<TextComponent> receivers = new ArrayList<>();
                for(int i=0; i < getChatReceivers().size(); i++) {
                    if(i==getChatReceivers().size() - 1) {
                        receivers.add(new TextComponent(getChatReceivers().get(i).getDisplayName()));
                    } else {
                        receivers.add(new TextComponent(getChatReceivers().get(i).getDisplayName() + "\n"));
                    }
                }
                BaseComponent[] receiverBase = receivers.toArray(new BaseComponent[0]);
                playerNum.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, receiverBase));
                textFormat.addExtra(playerNum);
            }
            else {
                textFormat.addExtra(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(this.chatPlayer, splitF)));
            }
        }
        return textFormat;
    }

    @Override
    public List<TextComponent> getBroadcastFormats(String message) {
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
                                    getHandText() : ChatAPI.getItem(chatPlayer.getInventory().getItemInMainHand()).getText();
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

    @Override
    public String getDiscordWebhookFormat(String message) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
        String currentTime = dateFormat.format(date);
        ChatItem itemData = ChatAPI.getItem(this.chatPlayer.getInventory().getItemInMainHand());
        Inko inko = new Inko();
        String ItemShowText = ChatCore.cf.getString("ItemShowText", "[i]");
        String[] spLitMessage = message.replace(ItemShowText, "%split%" + ItemShowText + "%split%").split("%split%");
        String reMessage = "";
        for(String spLitM : spLitMessage) {
            if(spLitM.equals(ItemShowText)) {
                reMessage = this.chatPlayer.getInventory().getItemInMainHand().getType().equals(Material.AIR) ?
                        reMessage + org.bukkit.ChatColor.stripColor(getHandText().getText()) : reMessage + org.bukkit.ChatColor.stripColor(itemData.getFormat());
            } else {
                reMessage = isKoreanChatEnabled() ? reMessage + inko.en2ko(org.bukkit.ChatColor.stripColor(spLitM), true) : reMessage + org.bukkit.ChatColor.stripColor(spLitM);
            }
        }
        return PlaceholderAPI.setPlaceholders(this.chatPlayer, ChatCore.cf.getString("DiscordFormat").replace("%current_time%", currentTime)
                .replace("%current_channel%", getChannel().name()).replace("%chat_message%", reMessage).replace("\\n", "\n"));
    }

}

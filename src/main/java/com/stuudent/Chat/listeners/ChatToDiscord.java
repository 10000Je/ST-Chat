package com.stuudent.Chat.listeners;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.github.kimcore.inko.Inko;
import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.ChatCore;
import com.stuudent.Chat.data.ChatItemData;
import com.stuudent.Chat.data.ChatPlayerData;
import com.stuudent.Chat.enums.ChannelType;
import com.stuudent.Chat.events.ChatEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatToDiscord implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(ChatEvent e) {
        String chatMessage = getFormat(e.getPlayer(), e.getMessage(), e.getShowItem(), e.getChannel());
        WebhookClient client = WebhookClient.withUrl(ChatCore.cf.getString("DiscordWebhookUrl"));
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(e.getPlayer().getName());
        builder.setAvatarUrl("https://mc-heads.net/avatar/" + e.getPlayer().getName());
        builder.setContent(chatMessage);
        client.send(builder.build());
        client.close();
    }

    public String getFormat(Player chatPlayer, String message, ItemStack showedItem, ChannelType channelType) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
        String currentTime = dateFormat.format(date);
        ChatItemData chatItemData = ChatAPI.getItem(showedItem);
        ChatPlayerData chatPlayerData = ChatAPI.getPlayer(chatPlayer);
        Inko inko = new Inko();
        String ItemShowText = ChatCore.cf.getString("ItemShowText", "[i]");
        String[] spLitMessage = message.replace(ItemShowText, "[SPLIT]" + ItemShowText + "[SPLIT]").split("\\[SPLIT\\]");
        String reMessage = "";
        for(String spLitM : spLitMessage) {
            if(spLitM.equals(ItemShowText)) {
                reMessage = chatItemData.getItemStack().getType().equals(Material.AIR) ? reMessage + ChatColor.stripColor(chatPlayerData.getHand().getText()) : reMessage + ChatColor.stripColor(chatItemData.getFormat());
            } else {
                reMessage = chatPlayerData.isKorean() ? reMessage + inko.en2ko(ChatColor.stripColor(spLitM), true) : reMessage + ChatColor.stripColor(spLitM);
            }
        }
        return ChatCore.cf.getString("DiscordFormat")
                .replace("[CURRENTTIME]", currentTime).replace("[CHANNEL]", channelType.name()).replace("[MESSAGE]", reMessage).replace("\\n", "\n");
    }

}

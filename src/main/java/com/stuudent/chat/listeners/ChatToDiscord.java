package com.stuudent.chat.listeners;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.stuudent.chat.ChatAPI;
import com.stuudent.chat.ChatCore;
import com.stuudent.chat.events.ChatEvent;
import com.stuudent.chat.interfaces.ChatPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatToDiscord implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(ChatEvent e) {
        if(ChatCore.dcw) {
            try {
                ChatPlayer chatPlayer = ChatAPI.getPlayer(e.getPlayer());
                String chatMessage = chatPlayer.getDiscordWebhookFormat(e.getMessage());
                WebhookClient client = WebhookClient.withUrl(ChatCore.cf.getString("DiscordWebhookUrl"));
                WebhookMessageBuilder builder = new WebhookMessageBuilder();
                builder.setUsername(e.getPlayer().getName());
                builder.setAvatarUrl("https://mc-heads.net/avatar/" + e.getPlayer().getName());
                builder.setContent(chatMessage);
                client.send(builder.build());
                client.close();
            }
            catch(IllegalArgumentException ignored) {}
        }
    }

}

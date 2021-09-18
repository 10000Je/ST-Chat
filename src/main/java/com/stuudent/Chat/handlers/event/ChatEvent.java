package com.stuudent.Chat.handlers.event;

import com.stuudent.Chat.ChatAPI;
import com.stuudent.Chat.enums.Channel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.lang.annotation.Documented;

public class ChatEvent extends Event implements Cancellable {

    public static HandlerList handlers;
    public Player chatPlayer;
    public Channel channel;
    public ItemStack showItem;
    public String message;
    public boolean cancelled;

    static {
        handlers = new HandlerList();
    }

    public ChatEvent(Player chatPlayer, String message, Channel channel) {
        this.chatPlayer = chatPlayer;
        this.message = message;
        this.channel = channel;
        this.showItem = null;
    }

    public ChatEvent(Player chatPlayer, String message, ItemStack showItem, Channel channel) {
        this.chatPlayer = chatPlayer;
        this.message = message;
        this.channel = channel;
        this.showItem = showItem;
    }

    public String getMessage() {
        return this.message;
    }

    public Channel getChannel() {
        return this.channel;
    }

    /**
    * @return if player didn't show any item to chat, it will return null, otherwise it will return ItemStack.
    */
    public ItemStack getShowItem() {
        return this.showItem;
    }

    public Player getPlayer() {
        return this.chatPlayer;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

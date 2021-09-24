package com.stuudent.Chat.events;

import com.stuudent.Chat.enums.ChannelType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ChatEvent extends Event implements Cancellable {

    public static HandlerList handlers;
    public Player chatPlayer;
    public ChannelType channelType;
    public ItemStack showItem;
    public String message;
    public boolean cancelled;

    static {
        handlers = new HandlerList();
    }

    public ChatEvent(Player chatPlayer, String message, ChannelType channelType) {
        this.chatPlayer = chatPlayer;
        this.message = message;
        this.channelType = channelType;
        this.showItem = null;
    }

    public ChatEvent(Player chatPlayer, String message, ItemStack showItem, ChannelType channelType) {
        this.chatPlayer = chatPlayer;
        this.message = message;
        this.channelType = channelType;
        this.showItem = showItem;
    }

    public String getMessage() {
        return this.message;
    }

    public ChannelType getChannel() {
        return this.channelType;
    }

    /**
    * @return if player didn't show any item to chat, it will return null. otherwise, it will return ItemStack.
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

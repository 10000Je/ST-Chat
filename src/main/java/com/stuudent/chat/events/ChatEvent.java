package com.stuudent.chat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChatEvent extends Event implements Cancellable {

    public static HandlerList handlers;
    public Player chatPlayer;
    public String message;
    public boolean cancelled;

    static {
        handlers = new HandlerList();
    }

    public ChatEvent(Player chatPlayer, String message) {
        this.chatPlayer = chatPlayer;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
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

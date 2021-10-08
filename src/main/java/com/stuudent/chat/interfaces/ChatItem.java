package com.stuudent.chat.interfaces;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.ItemStack;


public interface ChatItem {

    /**
     * @return returns Bukkit ItemStack.
     */
    ItemStack getBukkitItemStack();

    /**
     * @return returns Formatted Item NBT TextComponent.
     */
    TextComponent getText();

    /**
     * @return returns Formatted Item Name.
     */
    String getName();

    /**
     * @return returns Formatted Item Text.
     */
    String getFormat();

}

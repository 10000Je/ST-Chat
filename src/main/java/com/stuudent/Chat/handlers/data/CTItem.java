package com.stuudent.Chat.handlers.data;

import com.stuudent.Chat.ChatAPI;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.ItemStack;

public class CTItem {

    public ItemStack targetItem;
    public CTData ctData;

    public CTItem(ItemStack targetItem) {
        this.targetItem = targetItem;
        this.ctData = ChatAPI.getData();
    }

    public ItemStack getItemStack() {
        return this.targetItem;
    }

    public TextComponent getItem() {
        return this.ctData.getItemText(this.targetItem);
    }

    public String getName() {
        return this.ctData.getItemName(this.targetItem);
    }

    public String getFormat() {
        return this.ctData.getItemFormat(this.targetItem);
    }

    public int getAmount() {
        return this.ctData.getItemAmount(this.targetItem);
    }

}

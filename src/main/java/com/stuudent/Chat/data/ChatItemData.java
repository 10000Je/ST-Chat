package com.stuudent.Chat.data;

import com.stuudent.Chat.ChatAPI;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.ItemStack;

public class ChatItemData {

    public ItemStack targetItem;
    public AllData allData;

    public ChatItemData(ItemStack targetItem) {
        this.targetItem = targetItem;
        this.allData = ChatAPI.getData();
    }

    public ItemStack getItemStack() {
        return this.targetItem;
    }

    public TextComponent getItem() {
        return this.allData.getItemText(this.targetItem);
    }

    public String getName() {
        return this.allData.getItemName(this.targetItem);
    }

    public String getFormat() {
        return this.allData.getItemFormat(this.targetItem);
    }

    public int getAmount() {
        return this.allData.getItemAmount(this.targetItem);
    }

}

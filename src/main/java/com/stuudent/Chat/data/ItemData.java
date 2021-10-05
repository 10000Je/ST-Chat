package com.stuudent.Chat.data;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.stuudent.Chat.ChatCore;
import com.stuudent.Chat.interfaces.ChatItem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ItemData implements ChatItem {

    public ItemStack chatItem;

    public ItemData(ItemStack chatItem) {
        this.chatItem = chatItem;
    }

    @Override
    public ItemStack getBukkitItemStack() {
        return this.chatItem;
    }

    @Override
    public TextComponent getText() {
        try {
            if(ChatCore.pcl) {
                TextComponent textItem = new TextComponent(getFormat());
                Object nmsItem = MinecraftReflection.getMinecraftItemStack(this.chatItem);
                Object nbt = MinecraftReflection.getNBTCompoundClass().newInstance();
                Method save = MinecraftReflection.getItemStackClass().getDeclaredMethod("save", MinecraftReflection.getNBTCompoundClass());
                save.invoke(nmsItem, nbt);
                BaseComponent[] nbtText = {new TextComponent(nbt.toString())};
                textItem.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, nbtText));
                return textItem;
            } else {
                return new TextComponent(getFormat());
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return new TextComponent(getFormat());
        }
    }

    @Override
    public String getName() {
        String[] itemType = this.chatItem.getType().name().toLowerCase().replace("_", "_|").split("\\|");
        StringBuilder itemName = new StringBuilder();
        for(String type : itemType)
            itemName.append(type.substring(0,1).toUpperCase()).append(type.substring(1));
        itemName = new StringBuilder(itemName.toString().replace("_", " "));
        return this.chatItem.hasItemMeta() ? this.chatItem.getItemMeta().hasDisplayName() ?
                this.chatItem.getItemMeta().getDisplayName() : itemName.toString() : itemName.toString();
    }

    @Override
    public String getFormat() {
        return ChatColor.translateAlternateColorCodes('&', ChatCore.cf.getString("ItemShowChat")
                .replace("%item_name%", getName()).replace("%item_amount%", String.valueOf(this.chatItem.getAmount())));
    }

}

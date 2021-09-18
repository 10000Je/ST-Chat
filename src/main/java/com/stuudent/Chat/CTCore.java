package com.stuudent.Chat;

import com.Zrips.CMI.CMI;
import com.earth2me.essentials.Essentials;
import com.stuudent.Chat.commands.GlobalCmd;
import com.stuudent.Chat.commands.KoreanCmd;
import com.stuudent.Chat.commands.RegionCmd;
import com.stuudent.Chat.handlers.data.CTData;
import com.stuudent.Chat.listeners.CTListener;
import com.stuudent.Chat.listeners.ChatToDiscord;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class CTCore extends JavaPlugin {

    public static CTCore instance;
    public static FileConfiguration cf;
    public static Essentials ess;
    public static CMI cmi;
    private static Chat chat;

    static {
        chat = null;
    }

    @Override
    public void onEnable() {
        instance = this;
        ess = getServer().getPluginManager().isPluginEnabled("Essentials") ? Essentials.getPlugin(Essentials.class) : null;
        cmi = getServer().getPluginManager().isPluginEnabled("CMI") ? CMI.getInstance() : null;
        if(!setupChat()) {
            Bukkit.getConsoleSender().sendMessage("§6ST§f-§aChat §ev" + getDescription().getVersion() + " §cVAULT 플러그인이 존재하지 않습니다.");
            getServer().getPluginManager().disablePlugin(instance);
            return;
        }
        saveDefaultConfig();
        cf = getConfig();
        registerListeners();
        setCommandExecutors();
        Bukkit.getConsoleSender().sendMessage("§6ST§f-§aChat §ev" + getDescription().getVersion() + " §a플러그인이 활성화 되었습니다. §f(created by STuuDENT, Discord 민제#5894)");
    }

    @Override
    public void onDisable() {
        CTData ctData = ChatAPI.getData();
        ctData.save();
        Bukkit.getConsoleSender().sendMessage("§6ST§f-§aChat §ev" + getDescription().getVersion() + " §c플러그인이 비활성화 되었습니다. §f(created by STuuDENT, Discord 민제#5894)");
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public static Chat getChat() {
        return chat;
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new CTListener(), instance);
        Bukkit.getPluginManager().registerEvents(new ChatToDiscord(), instance);
    }

    public void setCommandExecutors() {
        getCommand("전체채팅").setExecutor(new GlobalCmd());
        getCommand("전체채팅").setTabCompleter(new GlobalCmd());
        getCommand("지역채팅").setExecutor(new RegionCmd());
        getCommand("지역채팅").setTabCompleter(new RegionCmd());
        getCommand("한글채팅").setExecutor(new KoreanCmd());
        getCommand("한글채팅").setTabCompleter(new KoreanCmd());
    }
}

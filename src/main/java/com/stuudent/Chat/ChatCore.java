package com.stuudent.Chat;

import com.Zrips.CMI.CMI;
import com.earth2me.essentials.Essentials;
import com.stuudent.Chat.commands.*;
import com.stuudent.Chat.data.AllData;
import com.stuudent.Chat.listeners.ChatListener;
import com.stuudent.Chat.listeners.ChatToDiscord;
import com.stuudent.Chat.schedulers.BroadcastScheduler;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatCore extends JavaPlugin {

    public static ChatCore instance;
    public static FileConfiguration cf;
    public static boolean ess;
    public static boolean cmi;
    private static Chat chat;
    private static Economy econ;

    static {
        chat = null;
        econ = null;
    }

    @Override
    public void onEnable() {
        instance = this;
        ess = getServer().getPluginManager().getPlugin("Essentials") != null;
        cmi = getServer().getPluginManager().getPlugin("CMI") != null;
        if(!setupChat() || !setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage("§6ST§f-§aChat §ev" + getDescription().getVersion() + " §cVAULT 플러그인이 존재하지 않습니다.");
            getServer().getPluginManager().disablePlugin(instance);
            return;
        }
        saveDefaultConfig();
        cf = getConfig();
        registerListeners();
        registerSchedulers();
        setCommandExecutors();
        setCommandTabCompleter();
        Bukkit.getConsoleSender().sendMessage("§6ST§f-§aChat §ev" + getDescription().getVersion() + " §a플러그인이 활성화 되었습니다. §f(created by STuuDENT, Discord 민제#5894)");
    }

    @Override
    public void onDisable() {
        AllData allData = ChatAPI.getData();
        allData.save();
        unregisterSchedulers();
        Bukkit.getConsoleSender().sendMessage("§6ST§f-§aChat §ev" + getDescription().getVersion() + " §c플러그인이 비활성화 되었습니다. §f(created by STuuDENT, Discord 민제#5894)");
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Chat getChat() {
        return chat;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ChatListener(), instance);
        Bukkit.getPluginManager().registerEvents(new ChatToDiscord(), instance);
    }

    public void setCommandExecutors() {
        getCommand("전체채팅").setExecutor(new GlobalChatCommand());
        getCommand("지역채팅").setExecutor(new RegionChatCommand());
        getCommand("한글채팅").setExecutor(new KoreanChatCommand());
        getCommand("청소").setExecutor(new ChatCleanCommand());
        getCommand("개인채팅청소").setExecutor(new ChatCleanCommand());
        getCommand("확성기설정").setExecutor(new MegaPhoneCommand());
        getCommand("일반확성기").setExecutor(new MegaPhoneCommand());
        getCommand("고급확성기").setExecutor(new MegaPhoneCommand());
        getCommand("공지").setExecutor(new BroadcastCommand());
    }

    public void setCommandTabCompleter() {
        getCommand("전체채팅").setTabCompleter(new GlobalChatCommand());
        getCommand("지역채팅").setTabCompleter(new RegionChatCommand());
        getCommand("한글채팅").setTabCompleter(new KoreanChatCommand());
        getCommand("청소").setTabCompleter(new ChatCleanCommand());
        getCommand("개인채팅청소").setTabCompleter(new ChatCleanCommand());
        getCommand("확성기설정").setTabCompleter(new MegaPhoneCommand());
        getCommand("일반확성기").setTabCompleter(new MegaPhoneCommand());
        getCommand("고급확성기").setTabCompleter(new MegaPhoneCommand());
        getCommand("공지").setTabCompleter(new BroadcastCommand());
    }

    public void registerSchedulers() {
        //noinspection deprecation
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, new BroadcastScheduler(), 0, cf.getLong("BroadcastRepeatingPeriod")* 20L);
    }

    public void unregisterSchedulers() {
        Bukkit.getScheduler().cancelTasks(instance);
    }
}

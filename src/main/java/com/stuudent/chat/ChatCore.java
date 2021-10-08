package com.stuudent.chat;

import com.stuudent.chat.commands.*;
import com.stuudent.chat.data.MegaPhoneData;
import com.stuudent.chat.data.Placeholders;
import com.stuudent.chat.data.PlayerData;
import com.stuudent.chat.listeners.ChatListener;
import com.stuudent.chat.listeners.ChatToDiscord;
import com.stuudent.chat.schedulers.BroadcastScheduler;
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
    public static boolean pcl;
    public static boolean dcw;
    private static Chat chat;
    private static Economy econ;

    static {
        chat = null;
        econ = null;
    }

    @Override
    public void onEnable() {
        instance = this;
        if(!dependCheck())
            return;
        setupChat();
        setupEconomy();
        softDependCheck();
        saveDefaultConfig();
        cf = getConfig();
        registerListeners();
        registerSchedulers();
        setCommandExecutors();
        setCommandTabCompleter();
        new Placeholders().register();
        Bukkit.getConsoleSender().sendMessage("§6ST§f-§aChat §ev" + getDescription().getVersion() + " §a플러그인이 활성화 되었습니다. §f(created by STuuDENT, Discord 민제#5894)");
    }

    @Override
    public void onDisable() {
        PlayerData.save();
        MegaPhoneData.save();
        unregisterSchedulers();
        Bukkit.getConsoleSender().sendMessage("§6ST§f-§aChat §ev" + getDescription().getVersion() + " §c플러그인이 비활성화 되었습니다. §f(created by STuuDENT, Discord 민제#5894)");
    }

    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
    }

    public static Chat getChat() {
        return chat;
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
    }

    public static Economy getEconomy() {
        return econ;
    }

    public boolean dependCheck() {
        if(getServer().getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getPluginManager().disablePlugin(instance);
            Bukkit.getConsoleSender().sendMessage("§6ST§f-§aChat §ev" + getDescription().getVersion() + " §fVault §c플러그인이 존재하지 않습니다.");
            return false;
        }
        if(getServer().getPluginManager().getPlugin("Kotlin") == null) {
            Bukkit.getPluginManager().disablePlugin(instance);
            Bukkit.getConsoleSender().sendMessage("§6ST§f-§aChat §ev" + getDescription().getVersion() + " §fKotlin §c플러그인이 존재하지 않습니다.");
            return false;
        }
        if(getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Bukkit.getPluginManager().disablePlugin(instance);
            Bukkit.getConsoleSender().sendMessage("§6ST§f-§aChat §ev" + getDescription().getVersion() + " §fPlaceholderAPI §c플러그인이 존재하지 않습니다.");
            return false;
        }
        return true;
    }

    public void softDependCheck() {
        ess = getServer().getPluginManager().getPlugin("Essentials") != null;
        cmi = getServer().getPluginManager().getPlugin("CMI") != null;
        pcl = getServer().getPluginManager().getPlugin("ProtocolLib") != null;
        dcw = getServer().getPluginManager().getPlugin("DiscordWebhook") != null;
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
        getCommand("채팅관리").setExecutor(new ReloadCommand());
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
        getCommand("채팅관리").setTabCompleter(new ReloadCommand());
    }

    public void registerSchedulers() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new BroadcastScheduler(), 0, cf.getLong("BroadcastRepeatingPeriod")* 20L);
    }

    public void unregisterSchedulers() {
        Bukkit.getScheduler().cancelTasks(instance);
    }
}

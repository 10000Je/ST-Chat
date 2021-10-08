package com.stuudent.chat.data;

import com.stuudent.chat.ChatCore;
import com.stuudent.chat.enums.MegaPhoneType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MegaPhoneData {

    public static YamlConfiguration megaPhoneData;
    public static File megaPhoneDataFile;

    static {
        megaPhoneDataFile = new File("plugins/" + ChatCore.instance.getName() + "/megaPhoneData.yml");
        megaPhoneData = YamlConfiguration.loadConfiguration(megaPhoneDataFile);
    }

    public static void save() {
        try {
            megaPhoneData.save(megaPhoneDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setMegaPhonePrice(MegaPhoneType megaPhoneType, double megaPhonePrice) {
        megaPhoneData.set(megaPhoneType.name() + ".PRICE", megaPhonePrice);
    }

    public static double getMegaPhonePrice(MegaPhoneType megaPhoneType) {
        return megaPhoneData.getDouble(megaPhoneType.name() + ".PRICE", 0);
    }

    public static void setMegaPhoneEnable() {
        megaPhoneData.set("AVAILABLE", true);
    }

    public static void setMegaPhoneDisable() {
        megaPhoneData.set("AVAILABLE", false);
    }

    public static boolean isMegaPhoneEnabled() {
        return megaPhoneData.getBoolean("AVAILABLE", false);
    }

}

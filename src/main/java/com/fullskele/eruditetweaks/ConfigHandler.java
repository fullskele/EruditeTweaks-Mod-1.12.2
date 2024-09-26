package com.fullskele.eruditetweaks;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ConfigHandler {

    public static Configuration config;

    public static String[] EXTRA_BABY_SPAWNS;
    public static boolean DOES_DEFILEMENT_BASE;
    public static String[] DEFILEMENT_CONVERSIONS;
    public static boolean DOES_ARCHAEOLOGISTS_CHANGES;
    public static String[][] ARCHAEOLOGIST_TRADES = new String[6][];

    public static void init(File file) {
        config = new Configuration(file);
        String category;

        category = "Extra Baby Spawns";
        config.addCustomCategoryComment(category, "Format - modid:entityid,minExtraSpawns,MaxExtraSpawns");
        EXTRA_BABY_SPAWNS = config.getStringList("Extra Baby Spawns", category, new String[]{"minecraft:pig,0-2"}, "");

        category = "Defiled Lands Custom Conversions";
        config.addCustomCategoryComment(category, "Format - modid1:itemid1:metadata1, modid2:itemid2:metadata2");

        DOES_DEFILEMENT_BASE = config.getBoolean("register base defilements", category, true, "Should the base Defilement conversions be registered?");
        DEFILEMENT_CONVERSIONS = config.getStringList("Custom Defilements", category, new String[]{"minecraft:enchanting_table:0, defiledlands:conjuring_altar:0"}, "");

        category = "Quark Archaeologist Custom Trades";
        config.addCustomCategoryComment(category, "Format - modid1:itemid1,itemdamage1,minamount1,maxamount1, modid2:itemid2,itemdamage2,minamount2,maxamount2, tradeweight\nNote: Trades that include minecraft:air are treated as a 'skip' for that slot when rolled");

        DOES_ARCHAEOLOGISTS_CHANGES = config.getBoolean("replace archaeologist trades", category, true, "Should the Archaeologist's trades be tweaked at all?");

        ARCHAEOLOGIST_TRADES[0] = config.getStringList("Trade Slot 1", category, new String[]{"minecraft:emerald,0,2,4, minecraft:bone,0,3,5, 100"}, "");
        ARCHAEOLOGIST_TRADES[1] = config.getStringList("Trade Slot 2", category, new String[]{"minecraft:bone,0,10,14, minecraft:emerald,0,1,1, 50","minecraft:gunpowder,0,7,16, minecraft:emerald,0,1,1, 50"}, "");
        ARCHAEOLOGIST_TRADES[2] = config.getStringList("Trade Slot 3", category, new String[]{"minecraft:coal,0,16,25, minecraft:emerald,0,1,1, 50","minecraft:air,0,0,0, minecraft:air,0,0,0, 50"}, "");
        ARCHAEOLOGIST_TRADES[3] = config.getStringList("Trade Slot 4", category, new String[]{"minecraft:emerald,0,12,23, minecraft:diamond,0,1,1, 50","minecraft:air,0,0,0, minecraft:air,0,0,0, 50"}, "");
        ARCHAEOLOGIST_TRADES[4] = config.getStringList("Trade Slot 5", category, new String[]{"minecraft:emerald,0,8,12, minecraft:iron_pickaxe,0,1,1, 25","minecraft:emerald,0,6,9, minecraft:iron_shovel,0,1,1, 25","minecraft:air,0,0,0, minecraft:air,0,0,0, 50"}, "");
        ARCHAEOLOGIST_TRADES[5] = config.getStringList("Trade Slot 6", category, new String[]{"minecraft:emerald,0,6,9, quark:archaeologist_hat,5,1,1, 100"}, "");


        config.save();
    }

    public static void RegisterConfig(FMLPreInitializationEvent event) {
        EruditeTweaks.config = new File(event.getModConfigurationDirectory() + "/");
        init(new File(EruditeTweaks.config.getPath(), EruditeTweaks.MODID + ".cfg"));
    }

}

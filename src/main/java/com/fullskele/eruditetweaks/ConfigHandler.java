package com.fullskele.eruditetweaks;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ConfigHandler {

    public static Configuration config;

    public static String[] EXTRA_BABY_SPAWNS;
    public static boolean TWEAK_VANILLA_ANIMALS;
    public static String PIG_FOODS;
    public static String HORSE_FOODS;
    public static String COW_FOODS;
    public static String SHEEP_FOODS;
    public static String LLAMA_FOODS;
    public static String CHICKEN_FOODS;
    public static String RABBIT_FOODS;

    public static boolean ENABLE_RABBIT_AI;
    public static boolean ENABLE_SHEEP_AI;
    public static String RABBIT_CROPS_TO_RAID;
    public static String SHEEP_TALL_GRASSES;
    public static String[] SHEEP_GRASS_TO_DIRT;

    public static boolean DOES_DEFILEMENT_BASE;
    public static String[] DEFILEMENT_CONVERSIONS;

    public static boolean DOES_ARCHAEOLOGISTS_CHANGES;
    public static String[][] ARCHAEOLOGIST_TRADES = new String[6][];
    public static boolean SHOW_DEATH_SCROLL_CLEAR_MESSAGE;

    public static void init(File file) {
        config = new Configuration(file);
        String category;

        category = "Minecraft: Animal Breeding Tweaks";
        config.addCustomCategoryComment(category, "Format - modid:entityid,minExtraSpawns,MaxExtraSpawns");
        EXTRA_BABY_SPAWNS = config.getStringList("Extra Baby Spawns", category, new String[]{"minecraft:pig,0-2"}, "");

        TWEAK_VANILLA_ANIMALS = config.getBoolean("enable breeding item changes", category, false, "Should vanilla animals have their breeding items tweaked?");

        PIG_FOODS = config.getString("pig breeding item list", category, "minecraft:carrot,minecraft:apple", "");
        HORSE_FOODS = config.getString("horse breeding item list", category, "minecraft:wheat,minecraft:pumpkin", "");
        COW_FOODS = config.getString("cow breeding item list", category, "minecraft:wheat,minecraft:beetroot", "");
        SHEEP_FOODS = config.getString("sheep breeding item list", category, "minecraft:wheat,minecraft:melon", "");
        LLAMA_FOODS = config.getString("llama breeding item list", category, "minecraft:wheat,minecraft:pumpkin_seeds", "");
        CHICKEN_FOODS = config.getString("chicken breeding item list", category, "minecraft:wheat_seeds,minecraft:rotten_flesh", "");
        RABBIT_FOODS = config.getString("rabbit breeding item list", category, "minecraft:golden_carrot,minecraft:tallgrass", "");

        category = "Minecraft: Animal AI Tweaks";
        config.addCustomCategoryComment(category, "Animal AI Tweaks");
        ENABLE_RABBIT_AI = config.getBoolean("enable rabbit AI additions", category, false, "");
        ENABLE_SHEEP_AI = config.getBoolean("enable sheep AI additions", category, false, "");
        RABBIT_CROPS_TO_RAID = config.getString("rabbit crops to raid", category, "minecraft:potatoes:7,minecraft:wheat:7", "");
        SHEEP_TALL_GRASSES = config.getString("sheep tall grasses to eat", category, "minecraft:web,minecraft:deadbush", "");
        SHEEP_GRASS_TO_DIRT = config.getStringList("sheep grass to dirt conversions", category, new String[]{"minecraft:dirt:2,minecraft:dirt:0"}, "");


        category = "Defiled Lands: Defilement Conversion Tweaks";
        config.addCustomCategoryComment(category, "Format - modid1:itemid1:metadata1, modid2:itemid2:metadata2");

        DOES_DEFILEMENT_BASE = config.getBoolean("register base defilements", category, true, "Should the base Defilement conversions be registered?");
        DEFILEMENT_CONVERSIONS = config.getStringList("Custom Defilements", category, new String[]{"minecraft:enchanting_table:0, defiledlands:conjuring_altar:0"}, "");

        category = "Quark: Archaeologist Trade Tweaks";
        config.addCustomCategoryComment(category, "Format - modid1:itemid1,itemdamage1,minamount1,maxamount1, modid2:itemid2,itemdamage2,minamount2,maxamount2, tradeweight\nNote: Trades that include minecraft:air are treated as a 'skip' for that slot when rolled");

        DOES_ARCHAEOLOGISTS_CHANGES = config.getBoolean("replace archaeologist trades", category, true, "Should the Archaeologist's trades be tweaked at all?");

        ARCHAEOLOGIST_TRADES[0] = config.getStringList("Trade Slot 1", category, new String[]{"minecraft:emerald,0,2,4, minecraft:bone,0,3,5, 100"}, "");
        ARCHAEOLOGIST_TRADES[1] = config.getStringList("Trade Slot 2", category, new String[]{"minecraft:bone,0,10,14, minecraft:emerald,0,1,1, 50","minecraft:gunpowder,0,7,16, minecraft:emerald,0,1,1, 50"}, "");
        ARCHAEOLOGIST_TRADES[2] = config.getStringList("Trade Slot 3", category, new String[]{"minecraft:coal,0,16,25, minecraft:emerald,0,1,1, 50","minecraft:air,0,0,0, minecraft:air,0,0,0, 50"}, "");
        ARCHAEOLOGIST_TRADES[3] = config.getStringList("Trade Slot 4", category, new String[]{"minecraft:emerald,0,12,23, minecraft:diamond,0,1,1, 50","minecraft:air,0,0,0, minecraft:air,0,0,0, 50"}, "");
        ARCHAEOLOGIST_TRADES[4] = config.getStringList("Trade Slot 5", category, new String[]{"minecraft:emerald,0,8,12, minecraft:iron_pickaxe,0,1,1, 25","minecraft:emerald,0,6,9, minecraft:iron_shovel,0,1,1, 25","minecraft:air,0,0,0, minecraft:air,0,0,0, 50"}, "");
        ARCHAEOLOGIST_TRADES[5] = config.getStringList("Trade Slot 6", category, new String[]{"minecraft:emerald,0,6,9, quark:archaeologist_hat,5,1,1, 100"}, "");

        category = "Corpse Complex: Command Tweaks";

        config.addCustomCategoryComment(category, "corpse complex tweaks");

        SHOW_DEATH_SCROLL_CLEAR_MESSAGE = config.getBoolean("Messages for /deathscrollclear", category, true, "Whether to show any messages after running the command /deathscrollclear");


        config.save();
    }

    public static void RegisterConfig(FMLPreInitializationEvent event) {

        EruditeTweaks.config = new File(event.getModConfigurationDirectory() + "/");
        init(new File(EruditeTweaks.config.getPath(), EruditeTweaks.MODID + ".cfg"));
    }

}

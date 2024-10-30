package com.fullskele.eruditetweaks;

import com.fullskele.eruditetweaks.tweaks.corpsecomplex.CommandDeathScrollClear;
import com.fullskele.eruditetweaks.tweaks.minecraft.BreedingModifiers;
import com.fullskele.eruditetweaks.tweaks.minecraft.ExtraLitterSpawns;
import com.fullskele.eruditetweaks.tweaks.quark.ArchaeologistAdditions;
import com.google.common.collect.Range;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;
import java.util.*;

@Mod(modid = EruditeTweaks.MODID,
        name = EruditeTweaks.NAME,
        version = EruditeTweaks.VERSION,
        dependencies ="after:defiledlands"
)
public class EruditeTweaks {
    public static final String MODID = "eruditetweaks";
    public static final String NAME = "Erudite Tweaks";
    public static final String VERSION = "1.0.3";


    public static File config;
    public static final HashMap<String, Range<Integer>> EXTRA_LITTER_SPAWNS = new HashMap<>();
    public static final Map<Class<? extends EntityAnimal>, Set<Item>> ANIMAL_BREEDING_ITEM_MAP = new HashMap<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.RegisterConfig(event);

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        //May need postinit?
        ArchaeologistAdditions.genArchaeologistTradesFromConfig();
        ExtraLitterSpawns.registerExtraLitterRanges();
        BreedingModifiers.registerVanillaBreedingItems();

        System.err.println("ERUDITE TWEAKS DEBUG: " + ANIMAL_BREEDING_ITEM_MAP);

        MinecraftForge.EVENT_BUS.register(BreedingModifiers.class);
        MinecraftForge.EVENT_BUS.register(ExtraLitterSpawns.class);

    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (Loader.isModLoaded("corpsecomplex")) event.registerServerCommand(new CommandDeathScrollClear());
    }

}

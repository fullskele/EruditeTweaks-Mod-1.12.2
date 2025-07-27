package com.fullskele.eruditetweaks;

import com.fullskele.eruditetweaks.tweaks.corpsecomplex.CommandDeathScrollClear;
import com.fullskele.eruditetweaks.tweaks.minecraft.BreedingModifiers;
import com.fullskele.eruditetweaks.tweaks.minecraft.ExtraLitterSpawns;
import com.fullskele.eruditetweaks.tweaks.minecraft.AnimalSpawnAdditions;
import com.fullskele.eruditetweaks.tweaks.minecraft.PlayerRegenTweaks;
import com.fullskele.eruditetweaks.tweaks.quark.ArchaeologistAdditions;
import com.google.common.collect.Range;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import net.minecraft.block.state.IBlockState;
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
    public static final String VERSION = "1.0.6";


    public static File config;
    public static final HashMap<String, Range<Integer>> EXTRA_LITTER_SPAWNS = new HashMap<>();
    public static final Map<Class<? extends EntityAnimal>, Set<Item>> ANIMAL_BREEDING_ITEM_MAP = new HashMap<>();
    public static final HashSet<IBlockState> RABBIT_CROPS = new HashSet<>();
    public static final HashSet<IBlockState> SHEEP_TALL_GRASS = new HashSet<>();
    public static final HashMap<IBlockState, IBlockState> SHEEP_GRASS_CONVERSIONS = new HashMap<>();
    public static HashSet<String> AVOID_ENTITIES_LIST = new HashSet<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.RegisterConfig(event);

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        //May need postinit?
        if (ConfigHandler.DOES_ARCHAEOLOGISTS_CHANGES) ArchaeologistAdditions.genArchaeologistTradesFromConfig();
        ExtraLitterSpawns.registerExtraLitterRanges();

        if (ConfigHandler.TWEAK_VANILLA_ANIMALS) {
            BreedingModifiers.registerVanillaBreedingItems();
            MinecraftForge.EVENT_BUS.register(BreedingModifiers.class);
        }

        if (ConfigHandler.ENABLE_SHEEP_AI || ConfigHandler.ENABLE_RABBIT_AI || ConfigHandler.ENABLE_SCARED_MOBS) {
            MinecraftForge.EVENT_BUS.register(new AnimalSpawnAdditions());
            AnimalSpawnAdditions.setupGriefConfig();
            if (ConfigHandler.ENABLE_SCARED_MOBS) AVOID_ENTITIES_LIST = new HashSet<>(Arrays.asList(ConfigHandler.SCARED_MOBS));
        }

        if (ConfigHandler.ENABLE_SATURATION_REGEN) {
            MinecraftForge.EVENT_BUS.register(new PlayerRegenTweaks());
        }

        if (ConfigHandler.EXTRA_BABY_SPAWNS.length > 0) {
            MinecraftForge.EVENT_BUS.register(new ExtraLitterSpawns());
        }


    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (Loader.isModLoaded("corpsecomplex")) event.registerServerCommand(new CommandDeathScrollClear());
    }

}

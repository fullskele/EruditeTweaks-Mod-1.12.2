package com.fullskele.eruditetweaks;

import com.fullskele.eruditetweaks.tweaks.quark.ArchaeologistAdditions;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Mod(modid = EruditeTweaks.MODID,
        name = EruditeTweaks.NAME,
        version = EruditeTweaks.VERSION,
        dependencies ="after:defiledlands"
)
public class EruditeTweaks {
    public static final String MODID = "eruditetweaks";
    public static final String NAME = "Erudite Tweaks";
    public static final String VERSION = "1.0.0";


    public static File config;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.RegisterConfig(event);

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        //May need postinit?
        ArchaeologistAdditions.genArchaeologistTradesFromConfig();
    }

}

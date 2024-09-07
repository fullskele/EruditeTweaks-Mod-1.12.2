package com.fullskele.eruditetweaks.tweaks.terracart;


import com.fullskele.eruditetweaks.EruditeTweaks;
import com.kreezcraft.terracartreloaded.EntityTerraCart;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@Mod.EventBusSubscriber(modid = EruditeTweaks.MODID)
public class TerracartRegister {

    private static int entityId = 0;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        if (Loader.isModLoaded("terracart")) {
            EntityEntry terraCartEntry = createEntityEntry("terracart", EntityTerraCart.class, 64, 1, true);
            event.getRegistry().register(terraCartEntry);
        }

    }

    private static EntityEntry createEntityEntry(String entityName, Class<? extends Entity> entityClass, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        return EntityEntryBuilder.create()
                .entity(entityClass)
                .id(new ResourceLocation(EruditeTweaks.MODID, entityName), entityId++)
                .name(EruditeTweaks.MODID + "." + entityName)
                .tracker(trackingRange, updateFrequency, sendsVelocityUpdates)
                .build();
    }

}
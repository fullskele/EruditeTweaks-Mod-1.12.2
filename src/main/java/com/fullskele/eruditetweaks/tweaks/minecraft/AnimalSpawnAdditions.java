package com.fullskele.eruditetweaks.tweaks.minecraft;

import com.fullskele.eruditetweaks.ConfigHandler;
import com.fullskele.eruditetweaks.EruditeTweaks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class AnimalSpawnAdditions {
    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        if (ConfigHandler.ENABLE_SHEEP_AI && event.getEntity() instanceof EntitySheep) {

            EntitySheep sheep = (EntitySheep) event.getEntity();

            sheep.tasks.addTask(5, new EntityAIEatGrassCustom(sheep));
        } else if (ConfigHandler.ENABLE_RABBIT_AI && event.getEntity() instanceof EntityRabbit) {
            EntityRabbit rabbit = (EntityRabbit) event.getEntity();
            rabbit.tasks.addTask(5, new AIRaidFarmCustom(rabbit));
        }
    }



    //TODO: Enable piecewise if one config is disabled
    public static void setupGriefConfig() {
        for (String entry : ConfigHandler.SHEEP_GRASS_TO_DIRT) {
            String[] twoEntries = entry.replace(" ", "").split(",");
            if (twoEntries.length != 2) {
                System.err.println("EruditeTweaks - Invalid config entry: " + entry);
                continue;
            }

            String blockString1 = twoEntries[0];
            String blockString2 = twoEntries[1];
            String[] ids1 = blockString1.split(":");
            String[] ids2 = blockString2.split(":");

            ResourceLocation resourceLocation1 = new ResourceLocation(ids1[0], ids1[1]);
            Block block1 = ForgeRegistries.BLOCKS.getValue(resourceLocation1);
            int meta1 = ids1.length > 2 && ids1[2] != null ? Integer.parseInt(ids1[2]) : 0;


            ResourceLocation resourceLocation2 = new ResourceLocation(ids2[0], ids2[1]);
            Block block2 = ForgeRegistries.BLOCKS.getValue(resourceLocation2);
            int meta2 = ids2.length > 2 && ids2[2] != null ? Integer.parseInt(ids2[2]) : 0;


            if (block1 != null && block2 != null) {
                IBlockState blockstate1 = block1.getStateFromMeta(meta1);
                IBlockState blockstate2 = block2.getStateFromMeta(meta2);

                EruditeTweaks.SHEEP_GRASS_CONVERSIONS.put(blockstate1, blockstate2);
            } else {
                System.err.println("EruditeTweaks - Invalid id: " + resourceLocation1 + " OR " + resourceLocation2);
            }
        }

        for (String entry : ConfigHandler.SHEEP_TALL_GRASSES.split(",")) {
            String cleanEntry = entry.replace(" ", "");
            String[] ids = cleanEntry.split(":");

            ResourceLocation resourceLocation = new ResourceLocation(ids[0], ids[1]);
            Block block = ForgeRegistries.BLOCKS.getValue(resourceLocation);
            int meta = ids.length > 2 && ids[2] != null ? Integer.parseInt(ids[2]) : 0;

            if (block != null) {
                IBlockState blockstate = block.getStateFromMeta(meta);
                EruditeTweaks.SHEEP_TALL_GRASS.add(blockstate);
            } else {
                System.err.println("EruditeTweaks - Invalid id: " + resourceLocation);
            }
        }

        for (String entry : ConfigHandler.RABBIT_CROPS_TO_RAID.split(",")) {
            String cleanEntry = entry.replace(" ", "");
            String[] ids = cleanEntry.split(":");

            ResourceLocation resourceLocation = new ResourceLocation(ids[0], ids[1]);
            Block block = ForgeRegistries.BLOCKS.getValue(resourceLocation);
            int meta = ids.length > 2 && ids[2] != null ? Integer.parseInt(ids[2]) : 0;

            if (block != null) {
                IBlockState blockstate = block.getStateFromMeta(meta);
                EruditeTweaks.RABBIT_CROPS.add(blockstate);
            } else {
                System.err.println("EruditeTweaks - Invalid id: " + resourceLocation);
            }
        }
    }
}
package com.fullskele.eruditetweaks.tweaks.minecraft;

import com.fullskele.eruditetweaks.ConfigHandler;
import com.fullskele.eruditetweaks.EruditeTweaks;
import com.google.common.collect.Range;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class ExtraLitterSpawns {

    @SubscribeEvent
    public void onBabyEntitySpawnEvent(BabyEntitySpawnEvent event) {
        EntityAgeable parentA = (EntityAgeable) event.getParentA();
        EntityAgeable parentB = (EntityAgeable) event.getParentB();

        ResourceLocation resourceLocation = EntityList.getKey(parentA);
        if (resourceLocation != null && EruditeTweaks.extraLitterRanges.containsKey(resourceLocation.toString())) {
            Range<Integer> spawnRange = EruditeTweaks.extraLitterRanges.get(resourceLocation.toString());
            World world = parentA.world;
            int randomLoopAmount = spawnRange.lowerEndpoint() + world.rand.nextInt(spawnRange.upperEndpoint() - spawnRange.lowerEndpoint() + 1);
            for (int i = 0; i < randomLoopAmount; i++) {
                EntityAgeable child = parentA.createChild(parentB);
                child.setPosition(parentA.posX, parentA.posY, parentA.posZ);
                child.setGrowingAge(-24000);
                world.spawnEntity(child);
            }
        }
    }

    public static void registerExtraLitterRanges() {
        for (String entry : ConfigHandler.EXTRA_BABY_SPAWNS) {
            String[] sections = entry.trim().split(",");
            if (sections.length != 2) {
                System.err.println("EruditeTweaks: Incorrect format in entry: " + entry);
                continue;
            }
            Range<Integer> range = parseRange(sections[1]);
            if (range == null) {
                System.err.println("EruditeTweaks: Incorrect range in entry: " + entry);
                continue;
            }
            if (EruditeTweaks.extraLitterRanges.containsKey(sections[0])) {
                System.err.println("EruditeTweaks: Skipping duplicate entry: " + entry);
                continue;
            }
            EruditeTweaks.extraLitterRanges.put(sections[0], range);
        }
    }

    private static Range<Integer> parseRange(String rangeStr) {
        String[] rangeParts = rangeStr.split("-");
        if (rangeParts.length == 2) {
            try {
                int min = Integer.parseInt(rangeParts[0]);
                int max = Integer.parseInt(rangeParts[1]);
                return Range.closed(min, max);
            } catch (NumberFormatException e) {
                System.err.println("EruditeTweaks: Error parsing range: " + rangeStr);
            }
        }
        return null;
    }
}

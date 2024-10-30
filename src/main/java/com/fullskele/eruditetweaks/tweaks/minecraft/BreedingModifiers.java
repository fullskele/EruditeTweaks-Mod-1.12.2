package com.fullskele.eruditetweaks.tweaks.minecraft;

import com.fullskele.eruditetweaks.ConfigHandler;
import com.fullskele.eruditetweaks.EruditeTweaks;
import gloomyfolken.hooklib.api.Hook;
import gloomyfolken.hooklib.api.HookContainer;
import gloomyfolken.hooklib.api.OnReturn;
import gloomyfolken.hooklib.api.ReturnValue;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

@HookContainer
public class BreedingModifiers {

    @SubscribeEvent
    public static void onAnimalSpawn(LivingSpawnEvent event) {
        if (event.getWorld().isRemote) return;
        if (!ConfigHandler.TWEAK_VANILLA_ANIMALS) return;
        if (!(event.getEntity() instanceof EntityAnimal)) return;
        EntityAnimal animal = (EntityAnimal) event.getEntity();

        //TODO: Make mapping of EntityX.class and foods
        if (EruditeTweaks.ANIMAL_BREEDING_ITEM_MAP.containsKey(animal.getClass())) {
            animal.tasks.taskEntries.removeIf(entry -> entry.action instanceof EntityAITempt);

            animal.tasks.addTask(3, new EntityAITempt(animal, 1.2, false, EruditeTweaks.ANIMAL_BREEDING_ITEM_MAP.get(animal.getClass())));
        }
    }


    @Hook
    @OnReturn
    public static boolean isBreedingItem(EntityPig pig, ItemStack p_70877_1_, @ReturnValue boolean originalResult) {
        if (!ConfigHandler.TWEAK_VANILLA_ANIMALS) return originalResult;
        Set<Item> breedingItems = EruditeTweaks.ANIMAL_BREEDING_ITEM_MAP.get(pig.getClass());
        return breedingItems != null && breedingItems.contains(p_70877_1_.getItem());
    }

    @Hook
    @OnReturn
    public static boolean isBreedingItem(AbstractHorse horse, ItemStack p_70877_1_, @ReturnValue boolean originalResult) {
        if (!ConfigHandler.TWEAK_VANILLA_ANIMALS) return originalResult;
        Set<Item> breedingItems = EruditeTweaks.ANIMAL_BREEDING_ITEM_MAP.get(horse.getClass());
        return breedingItems != null && breedingItems.contains(p_70877_1_.getItem());
    }

    @Hook
    @OnReturn
    public static boolean isBreedingItem(EntityAnimal animal, ItemStack p_70877_1_, @ReturnValue boolean originalResult) {
        //TODO: Check mapping of EntityX.class (cow, sheep, llama)
        if (!ConfigHandler.TWEAK_VANILLA_ANIMALS) return originalResult;
        Set<Item> breedingItems = EruditeTweaks.ANIMAL_BREEDING_ITEM_MAP.get(animal.getClass());
        return breedingItems != null && breedingItems.contains(p_70877_1_.getItem());
    }

    @Hook
    @OnReturn
    public static boolean isBreedingItem(EntityChicken chicken, ItemStack p_70877_1_, @ReturnValue boolean originalResult) {
        if (!ConfigHandler.TWEAK_VANILLA_ANIMALS) return originalResult;
        Set<Item> breedingItems = EruditeTweaks.ANIMAL_BREEDING_ITEM_MAP.get(chicken.getClass());
        return breedingItems != null && breedingItems.contains(p_70877_1_.getItem());
    }

    @Hook
    @OnReturn
    public static boolean isBreedingItem(EntityRabbit rabbit, ItemStack p_70877_1_, @ReturnValue boolean originalResult) {
        if (!ConfigHandler.TWEAK_VANILLA_ANIMALS) return originalResult;
        Set<Item> breedingItems = EruditeTweaks.ANIMAL_BREEDING_ITEM_MAP.get(rabbit.getClass());
        return breedingItems != null && breedingItems.contains(p_70877_1_.getItem());
    }

    public static void registerVanillaBreedingItems() {
        final String[][] ALL_FOOD_ITEMS = {
                ConfigHandler.PIG_FOODS.split(","),
                ConfigHandler.HORSE_FOODS.split(","),
                ConfigHandler.COW_FOODS.split(","),
                ConfigHandler.SHEEP_FOODS.split(","),
                ConfigHandler.LLAMA_FOODS.split(","),
                ConfigHandler.CHICKEN_FOODS.split(","),
                ConfigHandler.RABBIT_FOODS.split(",")
        };

        final Class[] CLASS_MAP = new Class[] {
                EntityPig.class,
                AbstractHorse.class,
                EntityCow.class,
                EntitySheep.class,
                EntityLlama.class,
                EntityChicken.class,
                EntityRabbit.class
        };

        int i = 0;
        for (String[] splitsAnimal : ALL_FOOD_ITEMS) {
            Set<Item> itemSet = new HashSet<>();
            for (String itemString : splitsAnimal) {
                Item item = Item.getByNameOrId(itemString);
                if (item != null) {
                    itemSet.add(item);
                }
            }
            EruditeTweaks.ANIMAL_BREEDING_ITEM_MAP.put(CLASS_MAP[i], itemSet);
            i++;
        }
    }
}

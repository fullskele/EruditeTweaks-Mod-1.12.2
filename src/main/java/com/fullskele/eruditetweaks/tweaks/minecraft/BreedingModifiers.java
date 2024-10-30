package com.fullskele.eruditetweaks.tweaks.minecraft;

import com.fullskele.eruditetweaks.ConfigHandler;
import gloomyfolken.hooklib.api.Hook;
import gloomyfolken.hooklib.api.HookContainer;
import gloomyfolken.hooklib.api.OnReturn;
import gloomyfolken.hooklib.api.ReturnValue;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@HookContainer
public class BreedingModifiers {

    @SubscribeEvent
    public static void onAnimalSpawn(LivingSpawnEvent event) {
        if (event.getWorld().isRemote) return;
        if (!ConfigHandler.TWEAK_VANILLA_ANIMALS) return;
        if (!(event.getEntity() instanceof EntityAnimal)) return;
        EntityAnimal animal = (EntityAnimal) event.getEntity();

        //TODO: Make mapping of EntityX.class and foods
        if (animal instanceof EntityPig) {
            animal.tasks.addTask(3, new EntityAITempt(animal, 1.25D, Items.APPLE, false));
        }
    }

    @Hook
    @OnReturn
    public static boolean isBreedingItem(EntityPig pig, ItemStack p_70877_1_, @ReturnValue boolean originalResult) {
        if (!ConfigHandler.TWEAK_VANILLA_ANIMALS) return originalResult;
        return (p_70877_1_.getItem().equals(Items.APPLE));
    }

    @Hook
    @OnReturn
    public static boolean isBreedingItem(AbstractHorse horse, ItemStack p_70877_1_, @ReturnValue boolean originalResult) {
        if (!ConfigHandler.TWEAK_VANILLA_ANIMALS) return originalResult;
        return (p_70877_1_.getItem().equals(Items.APPLE));
    }

    @Hook
    @OnReturn
    public static boolean isBreedingItem(EntityAnimal animal, ItemStack p_70877_1_, @ReturnValue boolean originalResult) {
        //TODO: Check mapping of EntityX.class (cow, sheep, llama)
        if (!ConfigHandler.TWEAK_VANILLA_ANIMALS) return originalResult;
        return (p_70877_1_.getItem().equals(Items.APPLE));
    }

    @Hook
    @OnReturn
    public static boolean isBreedingItem(EntityChicken chicken, ItemStack p_70877_1_, @ReturnValue boolean originalResult) {
        if (!ConfigHandler.TWEAK_VANILLA_ANIMALS) return originalResult;
        return (p_70877_1_.getItem().equals(Items.APPLE));
    }

    @Hook
    @OnReturn
    public static boolean isBreedingItem(EntityRabbit rabbit, ItemStack p_70877_1_, @ReturnValue boolean originalResult) {
        if (!ConfigHandler.TWEAK_VANILLA_ANIMALS) return originalResult;
        return (p_70877_1_.getItem().equals(Items.APPLE));
    }
}

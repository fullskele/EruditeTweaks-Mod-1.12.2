package com.fullskele.eruditetweaks.tweaks.minecraft;

import com.fullskele.eruditetweaks.ConfigHandler;
import com.fullskele.eruditetweaks.EruditeTweaks;
import gloomyfolken.hooklib.api.*;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    public static boolean isBreedingItem(EntityAnimal animal, ItemStack p_70877_1_, @ReturnValue boolean originalResult) {
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

    //https://github.com/hohserg1/HookLib/blob/3dd339c502b453d344a6249a2f285d08c0b6e7d4/src/main/java/gloomyfolken/hooklib/api/MethodLens.java#L23

    @MethodLens
    public static boolean handleEating(AbstractHorse abstractHorse, EntityPlayer p_190678_1_, ItemStack p_190678_2_) {
        //Don't type anything here, dummy method
        throw new NotImplementedException();
    }

    //Replaces handleEating call for all horse types
    @Hook(targetMethod = "processInteract")
    @OnMethodCall(value = "handleEating", shift = Shift.INSTEAD)
    public static boolean processInteract(EntityHorse entityHorse, EntityPlayer player, EnumHand heldItem) {
        //TODO Make this condition based on config
        if (true) return modifiedHandleEating(entityHorse, player, player.getHeldItem(heldItem));

        return originalHandleEating(entityHorse, player, player.getHeldItem(heldItem));
    }

    @Hook(targetMethod = "processInteract")
    @OnMethodCall(value = "handleEating", shift = Shift.INSTEAD)
    public static boolean processInteract(AbstractChestHorse abstractChestHorse, EntityPlayer player, EnumHand heldItem) {
        if ((abstractChestHorse instanceof EntityMule || abstractChestHorse instanceof EntityDonkey)) {
            //TODO Make this condition based on config
            if (ConfigHandler.TWEAK_VANILLA_ANIMALS) return modifiedHandleEating(abstractChestHorse, player, player.getHeldItem(heldItem));

            return originalHandleEating(abstractChestHorse, player, player.getHeldItem(heldItem));
        }

        if (ConfigHandler.TWEAK_VANILLA_ANIMALS) return modifiedLLamaHandleEating(abstractChestHorse, player, player.getHeldItem(heldItem));

        return originalLlamaHandleEating(abstractChestHorse, player, player.getHeldItem(heldItem));
    }

    //Custom logic for handleEating for horses
    private static boolean modifiedHandleEating(AbstractHorse abstractHorse, EntityPlayer player, ItemStack heldItem) {
        boolean flag = false;
        float f = 0.0F;
        int i = 0;
        int j = 0;
        Item item = heldItem.getItem();
        if (EruditeTweaks.ANIMAL_BREEDING_ITEM_MAP.get(EntityHorse.class).contains(item)) {
            f = 10.0F;
            i = 240;
            j = 10;
            if (abstractHorse.isTame() && abstractHorse.getGrowingAge() == 0 && !abstractHorse.isInLove()) {
                flag = true;
                abstractHorse.setInLove(player);
            }
        }

        if (abstractHorse.getHealth() < abstractHorse.getMaxHealth() && f > 0.0F) {
            abstractHorse.heal(f);
            flag = true;
        }

        if (abstractHorse.isChild() && i > 0) {
            abstractHorse.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, abstractHorse.posX + (double) (abstractHorse.world.rand.nextFloat() * abstractHorse.width * 2.0F) - (double) abstractHorse.width, abstractHorse.posY + 0.5 + (double) (abstractHorse.world.rand.nextFloat() * abstractHorse.height), abstractHorse.posZ + (double) (abstractHorse.world.rand.nextFloat() * abstractHorse.width * 2.0F) - (double) abstractHorse.width, 0.0, 0.0, 0.0, new int[0]);
            if (!abstractHorse.world.isRemote) {
                abstractHorse.addGrowth(i);
            }

            flag = true;
        }

        if (j > 0 && (flag || !abstractHorse.isTame()) && abstractHorse.getTemper() < abstractHorse.getMaxTemper()) {
            flag = true;
            if (!abstractHorse.world.isRemote) {
                abstractHorse.increaseTemper(j);
            }
        }

        if (flag) {
            if (!abstractHorse.isSilent()) {
                abstractHorse.world.playSound((EntityPlayer)null, abstractHorse.posX, abstractHorse.posY, abstractHorse.posZ, SoundEvents.ENTITY_HORSE_EAT, abstractHorse.getSoundCategory(), 1.0F, 1.0F + (abstractHorse.world.rand.nextFloat() - abstractHorse.world.rand.nextFloat()) * 0.2F);
            }
            //TODO Fix mouth animations?
        }

        return flag;
    }

    //Base logic for handleEating for horses
    //Replace in future HookLib ver
    private static boolean originalHandleEating(AbstractHorse abstractHorse, EntityPlayer player, ItemStack heldItem) {
        boolean flag = false;
        float f = 0.0F;
        int i = 0;
        int j = 0;
        Item item = heldItem.getItem();
        if (item == Items.WHEAT) {
            f = 2.0F;
            i = 20;
            j = 3;
        } else if (item == Items.SUGAR) {
            f = 1.0F;
            i = 30;
            j = 3;
        } else if (item == Item.getItemFromBlock(Blocks.HAY_BLOCK)) {
            f = 20.0F;
            i = 180;
        } else if (item == Items.APPLE) {
            f = 3.0F;
            i = 60;
            j = 3;
        } else if (item == Items.GOLDEN_CARROT) {
            f = 4.0F;
            i = 60;
            j = 5;
            if (abstractHorse.isTame() && abstractHorse.getGrowingAge() == 0 && !abstractHorse.isInLove()) {
                flag = true;
                abstractHorse.setInLove(player);
            }
        } else if (item == Items.GOLDEN_APPLE) {
            f = 10.0F;
            i = 240;
            j = 10;
            if (abstractHorse.isTame() && abstractHorse.getGrowingAge() == 0 && !abstractHorse.isInLove()) {
                flag = true;
                abstractHorse.setInLove(player);
            }
        }

        if (abstractHorse.getHealth() < abstractHorse.getMaxHealth() && f > 0.0F) {
            abstractHorse.heal(f);
            flag = true;
        }

        if (abstractHorse.isChild() && i > 0) {
            abstractHorse.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, abstractHorse.posX + (double)(abstractHorse.world.rand.nextFloat() * abstractHorse.width * 2.0F) - (double)abstractHorse.width, abstractHorse.posY + 0.5 + (double)(abstractHorse.world.rand.nextFloat() * abstractHorse.height), abstractHorse.posZ + (double)(abstractHorse.world.rand.nextFloat() * abstractHorse.width * 2.0F) - (double)abstractHorse.width, 0.0, 0.0, 0.0, new int[0]);
            if (!abstractHorse.world.isRemote) {
                abstractHorse.addGrowth(i);
            }

            flag = true;
        }

        if (j > 0 && (flag || !abstractHorse.isTame()) && abstractHorse.getTemper() < abstractHorse.getMaxTemper()) {
            flag = true;
            if (!abstractHorse.world.isRemote) {
                abstractHorse.increaseTemper(j);
            }
        }

        if (flag) {
            if (!abstractHorse.isSilent()) {
                abstractHorse.world.playSound((EntityPlayer)null, abstractHorse.posX, abstractHorse.posY, abstractHorse.posZ, SoundEvents.ENTITY_HORSE_EAT, abstractHorse.getSoundCategory(), 1.0F, 1.0F + (abstractHorse.world.rand.nextFloat() - abstractHorse.world.rand.nextFloat()) * 0.2F);
            }
            //TODO Fix mouth animations?
        }

        return flag;
    }

    private static boolean modifiedLLamaHandleEating(AbstractChestHorse entityLlama, EntityPlayer player, ItemStack heldItem) {
        int lvt_3_1_ = 0;
        int lvt_4_1_ = 0;
        float lvt_5_1_ = 0.0F;
        boolean lvt_6_1_ = false;
        Item lvt_7_1_ = heldItem.getItem();
        if (EruditeTweaks.ANIMAL_BREEDING_ITEM_MAP.get(EntityLlama.class).contains(lvt_7_1_)) {
            lvt_3_1_ = 90;
            lvt_4_1_ = 6;
            lvt_5_1_ = 10.0F;
            if (entityLlama.isTame() && entityLlama.getGrowingAge() == 0) {
                lvt_6_1_ = true;
                entityLlama.setInLove(player);
            }
        }

        if (entityLlama.getHealth() < entityLlama.getMaxHealth() && lvt_5_1_ > 0.0F) {
            entityLlama.heal(lvt_5_1_);
            lvt_6_1_ = true;
        }

        if (entityLlama.isChild() && lvt_3_1_ > 0) {
            entityLlama.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, entityLlama.posX + (double)(entityLlama.world.rand.nextFloat() * entityLlama.width * 2.0F) - (double)entityLlama.width, entityLlama.posY + 0.5 + (double)(entityLlama.world.rand.nextFloat() * entityLlama.height), entityLlama.posZ + (double)(entityLlama.world.rand.nextFloat() * entityLlama.width * 2.0F) - (double)entityLlama.width, 0.0, 0.0, 0.0, new int[0]);
            if (!entityLlama.world.isRemote) {
                entityLlama.addGrowth(lvt_3_1_);
            }

            lvt_6_1_ = true;
        }

        if (lvt_4_1_ > 0 && (lvt_6_1_ || !entityLlama.isTame()) && entityLlama.getTemper() < entityLlama.getMaxTemper()) {
            lvt_6_1_ = true;
            if (!entityLlama.world.isRemote) {
                entityLlama.increaseTemper(lvt_4_1_);
            }
        }

        if (lvt_6_1_ && !entityLlama.isSilent()) {
            entityLlama.world.playSound((EntityPlayer)null, entityLlama.posX, entityLlama.posY, entityLlama.posZ, SoundEvents.ENTITY_LLAMA_EAT, entityLlama.getSoundCategory(), 1.0F, 1.0F + (entityLlama.world.rand.nextFloat() - entityLlama.world.rand.nextFloat()) * 0.2F);
        }

        return lvt_6_1_;
    }

    private static boolean originalLlamaHandleEating(AbstractChestHorse entityLlama, EntityPlayer player, ItemStack heldItem) {
        int lvt_3_1_ = 0;
        int lvt_4_1_ = 0;
        float lvt_5_1_ = 0.0F;
        boolean lvt_6_1_ = false;
        Item lvt_7_1_ = heldItem.getItem();
        if (lvt_7_1_ == Items.WHEAT) {
            lvt_3_1_ = 10;
            lvt_4_1_ = 3;
            lvt_5_1_ = 2.0F;
        } else if (lvt_7_1_ == Item.getItemFromBlock(Blocks.HAY_BLOCK)) {
            lvt_3_1_ = 90;
            lvt_4_1_ = 6;
            lvt_5_1_ = 10.0F;
            if (entityLlama.isTame() && entityLlama.getGrowingAge() == 0) {
                lvt_6_1_ = true;
                entityLlama.setInLove(player);
            }
        }

        if (entityLlama.getHealth() < entityLlama.getMaxHealth() && lvt_5_1_ > 0.0F) {
            entityLlama.heal(lvt_5_1_);
            lvt_6_1_ = true;
        }

        if (entityLlama.isChild() && lvt_3_1_ > 0) {
            entityLlama.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, entityLlama.posX + (double)(entityLlama.world.rand.nextFloat() * entityLlama.width * 2.0F) - (double)entityLlama.width, entityLlama.posY + 0.5 + (double)(entityLlama.world.rand.nextFloat() * entityLlama.height), entityLlama.posZ + (double)(entityLlama.world.rand.nextFloat() * entityLlama.width * 2.0F) - (double)entityLlama.width, 0.0, 0.0, 0.0, new int[0]);
            if (!entityLlama.world.isRemote) {
                entityLlama.addGrowth(lvt_3_1_);
            }

            lvt_6_1_ = true;
        }

        if (lvt_4_1_ > 0 && (lvt_6_1_ || !entityLlama.isTame()) && entityLlama.getTemper() < entityLlama.getMaxTemper()) {
            lvt_6_1_ = true;
            if (!entityLlama.world.isRemote) {
                entityLlama.increaseTemper(lvt_4_1_);
            }
        }

        if (lvt_6_1_ && !entityLlama.isSilent()) {
            entityLlama.world.playSound((EntityPlayer)null, entityLlama.posX, entityLlama.posY, entityLlama.posZ, SoundEvents.ENTITY_LLAMA_EAT, entityLlama.getSoundCategory(), 1.0F, 1.0F + (entityLlama.world.rand.nextFloat() - entityLlama.world.rand.nextFloat()) * 0.2F);
        }

        return lvt_6_1_;
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

        final Class[] CLASS_MAP = new Class[]{
                EntityPig.class,
                EntityHorse.class,
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


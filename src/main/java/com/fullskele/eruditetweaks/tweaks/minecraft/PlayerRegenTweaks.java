package com.fullskele.eruditetweaks.tweaks.minecraft;

import com.fullskele.eruditetweaks.ConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerRegenTweaks {

    //Every few ticks, if player has full hunger and has positive saturation,
    // increase health and subtract from saturation, if the player has lower than max health.
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayer player = event.player;

        if (player.world.isRemote) return;

        if (player.ticksExisted % ConfigHandler.SATURATION_CONVERT_INTERVAL != 0) return;

        FoodStats foodStats = player.getFoodStats();

        boolean fullHunger = foodStats.getFoodLevel() >= 20;
        boolean hasSaturation = foodStats.getSaturationLevel() > (ConfigHandler.SATURATION_DECREASE-0.1F);
        boolean needsHealing = player.getHealth() < player.getMaxHealth();

        if (fullHunger && hasSaturation && needsHealing) {
            player.heal(ConfigHandler.SATURATION_CONVERT_AMOUNT);

            float newSaturation = Math.max(0.0f, foodStats.getSaturationLevel() - ConfigHandler.SATURATION_DECREASE);
            foodStats.setFoodSaturationLevel(newSaturation);
        }
    }
}

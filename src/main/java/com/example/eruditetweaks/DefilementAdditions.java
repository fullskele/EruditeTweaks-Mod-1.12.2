package com.example.eruditetweaks;

import gloomyfolken.hooklib.api.Hook;
import gloomyfolken.hooklib.api.HookContainer;
import gloomyfolken.hooklib.api.OnMethodCall;
import static gloomyfolken.hooklib.api.Shift.INSTEAD;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.event.RegistryEvent;

import lykrast.defiledlands.common.init.ModRecipes;
import lykrast.defiledlands.common.util.CorruptionRecipes;

import java.util.HashMap;
import java.util.Map;

import static lykrast.defiledlands.common.util.CorruptionRecipes.register;

@HookContainer
public class DefilementAdditions {

    @Hook
    @OnMethodCall(value = "init",shift = INSTEAD)
    public static void registerRecipes(ModRecipes modRecipes, RegistryEvent.Register<IRecipe> event) {

        Map<IBlockState, IBlockState> blockConversions = parseConfigList();

        for (IBlockState blockState : blockConversions.keySet()) {
            register(blockState, blockConversions.get(blockState));
        }

        if (ConfigHandler.DOES_DEFILEMENT_BASE)
            CorruptionRecipes.init();
    }



    private static Map<IBlockState, IBlockState> parseConfigList() {
        Map<IBlockState, IBlockState> blockMap = new HashMap<>();

        for (String entry : ConfigHandler.DEFILEMENT_CONVERSIONS) {
            String[] twoEntries = entry.replace(" ", "").split(",");
            if (twoEntries.length != 2) {
                System.err.println("Invalid config entry: " + entry);
                continue;
            }

            String blockString1 = twoEntries[0];
            String blockString2 = twoEntries[1];
            String[] ids1 = blockString1.split(":");
            String[] ids2 = blockString2.split(":");

            ResourceLocation resourceLocation1 = new ResourceLocation(ids1[0], ids1[1]);
            Block block1 = ForgeRegistries.BLOCKS.getValue(resourceLocation1);
            int meta1 = Integer.parseInt(ids1[2]);


            ResourceLocation resourceLocation2 = new ResourceLocation(ids2[0], ids2[1]);
            Block block2 = ForgeRegistries.BLOCKS.getValue(resourceLocation2);
            int meta2 = Integer.parseInt(ids2[2]);


            if (block1 != null && block2 != null) {
                IBlockState blockstate1 = block1.getStateFromMeta(meta1);
                IBlockState blockstate2 = block2.getStateFromMeta(meta2);

                blockMap.put(blockstate1, blockstate2);
            } else {
                System.err.println("Invalid entry: " + resourceLocation1 + " OR " + resourceLocation2);
            }
        }

        return blockMap;
    }
}

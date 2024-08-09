package com.example.eruditetweaks;

import gloomyfolken.hooklib.api.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import vazkii.quark.world.entity.EntityArchaeologist;
import vazkii.quark.world.feature.Archaeologist;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static gloomyfolken.hooklib.api.Shift.INSTEAD;

@HookContainer
public class ArchaeologistAdditions {
    @FieldLens
    public static FieldAccessor<EntityArchaeologist, MerchantRecipeList> buyingList;

    public static List<List<WeightedArchaeologistTrade>> tradeList;

    static {
        tradeList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            tradeList.add(new ArrayList<>());
        }
    }

    @Hook
    @OnMethodCall(value = "populateBuyingList",shift = INSTEAD)
    public static void processInteract(EntityArchaeologist entityArchaeologist, EntityPlayer player, EnumHand hand) {
        newPopulateBuyingList(entityArchaeologist);
    }

    @Hook
    @OnMethodCall(value = "populateBuyingList",shift = INSTEAD)
    public static void getRecipes(EntityArchaeologist entityArchaeologist, @Nonnull EntityPlayer player) {
        newPopulateBuyingList(entityArchaeologist);
    }




    private static void newPopulateBuyingList(EntityArchaeologist entityArchaeologist) {
        if (!ConfigHandler.DOES_ARCHAEOLOGISTS_CHANGES) {
            defaultPopulateBuyingList(entityArchaeologist);
            return;
        }

        if (buyingList.get(entityArchaeologist) == null)
            buyingList.set(entityArchaeologist, new MerchantRecipeList());

        Random rand = new Random();
        for (List<WeightedArchaeologistTrade> trades : tradeList) {
            if (!trades.isEmpty()) {
                WeightedArchaeologistTrade randomTrade = WeightedRandom.getRandomItem(rand, trades);
                if (randomTrade.getBuyStack().getItem() == Items.AIR || randomTrade.getSellStack().getItem() == Items.AIR) {
                    continue;
                }

                int buyPrice = entityArchaeologist.world.rand.nextInt((randomTrade.getMaxAmount() - randomTrade.getMinAmount()) + 1) + randomTrade.getMinAmount();
                int sellAmount = entityArchaeologist.world.rand.nextInt((randomTrade.getMaxPrice() - randomTrade.getMinPrice()) + 1) + randomTrade.getMinPrice();

                buyingList.get(entityArchaeologist).add(new MerchantRecipe(new ItemStack(randomTrade.getBuyStack().getItem(), buyPrice, randomTrade.getBuyStack().getMetadata()), new ItemStack(randomTrade.getSellStack().getItem(), sellAmount, randomTrade.getSellStack().getMetadata())));
            }
        }
    }

    //Yeah.. apologies if there's a better way to make this work
    private static void defaultPopulateBuyingList(EntityArchaeologist entityArchaeologist) {
        if (buyingList.get(entityArchaeologist) == null)
            buyingList.set(entityArchaeologist, new MerchantRecipeList());

        Random r = entityArchaeologist.world.rand;

        buyingList.get(entityArchaeologist).add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2 + r.nextInt(3)), new ItemStack(Items.BONE, 3 + r.nextInt(3))));
        if (r.nextBoolean())
            buyingList.get(entityArchaeologist).add(new MerchantRecipe(new ItemStack(Items.BONE, 10 + r.nextInt(5)), new ItemStack(Items.EMERALD, 1)));
        else buyingList.get(entityArchaeologist).add(new MerchantRecipe(new ItemStack(Items.GUNPOWDER, 7 + r.nextInt(10)), new ItemStack(Items.EMERALD, 1)));

        if (r.nextBoolean())
            buyingList.get(entityArchaeologist).add(new MerchantRecipe(new ItemStack(Items.COAL, 16 + r.nextInt(10)), new ItemStack(Items.EMERALD, 1)));
        if (r.nextBoolean())
            buyingList.get(entityArchaeologist).add(new MerchantRecipe(new ItemStack(Items.EMERALD, 12 + r.nextInt(10)), new ItemStack(Items.DIAMOND, 1)));
        if (r.nextBoolean()) {
            if (r.nextBoolean())
                buyingList.get(entityArchaeologist).add(new MerchantRecipe(new ItemStack(Items.EMERALD, 8 + r.nextInt(5)), new ItemStack(Items.IRON_PICKAXE, 1)));
            else buyingList.get(entityArchaeologist).add(new MerchantRecipe(new ItemStack(Items.EMERALD, 6 + r.nextInt(4)), new ItemStack(Items.IRON_SHOVEL, 1)));
        }

        if (Archaeologist.enableHat && Archaeologist.sellHat)
            buyingList.get(entityArchaeologist).add(new MerchantRecipe(new ItemStack(Items.EMERALD, 6 + r.nextInt(4)), ItemStack.EMPTY, new ItemStack(Archaeologist.archaeologist_hat, 1), 0, 1));

    }

    public static void genArchaeologistTradesFromConfig() {
        if (!ConfigHandler.DOES_ARCHAEOLOGISTS_CHANGES || !Loader.isModLoaded("quark"))
            return;
        for (int i = 0; i < ConfigHandler.ARCHAEOLOGIST_TRADES.length; i++) {
            // Assuming ARCHAEOLOGIST_TRADES is a 2D array of strings
            for (String tradeLine : ConfigHandler.ARCHAEOLOGIST_TRADES[i]) {
                String[] tradeSplit = tradeLine.replace(" ", "").split(",");
                if (tradeSplit.length != 9) {
                    if (tradeSplit.length != 0)
                        System.err.println("Invalid trade format: " + tradeLine);
                    continue;
                }

                try {
                    ItemStack buyStack = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tradeSplit[0])).getDefaultInstance();
                    buyStack.setItemDamage(Integer.parseInt(tradeSplit[1]));
                    int minCost = Integer.parseInt(tradeSplit[2]);
                    int maxCost = Integer.parseInt(tradeSplit[3]);
                    ItemStack sellStack = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tradeSplit[4])).getDefaultInstance();
                    sellStack.setItemDamage(Integer.parseInt(tradeSplit[5]));
                    int minSold = Integer.parseInt(tradeSplit[6]);
                    int maxSold = Integer.parseInt(tradeSplit[7]);
                    float weight = Float.parseFloat(tradeSplit[8]);

                    WeightedArchaeologistTrade trade = new WeightedArchaeologistTrade(buyStack, sellStack, minCost, maxCost, minSold, maxSold, weight);
                    ArchaeologistAdditions.tradeList.get(i).add(trade);

                } catch (Exception e) {
                    System.err.println("Error parsing trade line: " + tradeLine);
                    e.printStackTrace();
                }
            }
        }
    }
}


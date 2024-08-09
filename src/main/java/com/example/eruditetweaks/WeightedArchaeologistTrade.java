package com.example.eruditetweaks;

import net.minecraft.item.ItemStack;

import net.minecraft.util.WeightedRandom;

public class WeightedArchaeologistTrade extends WeightedRandom.Item {
    private final ItemStack buyStack;
    private final ItemStack sellStack;
    private final int minAmount;
    private final int maxAmount;
    private final int minPrice;
    private final int maxPrice;

    public WeightedArchaeologistTrade(ItemStack buyStack, ItemStack sellStack, int minAmount, int maxAmount, int minprice, int maxprice, float weight) {
        super((int) weight);
        this.buyStack = buyStack;
        this.sellStack = sellStack;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.minPrice = minprice;
        this.maxPrice = maxprice;
    }

    public ItemStack getBuyStack() {
        return buyStack;
    }

    public ItemStack getSellStack() { return sellStack; }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getMinPrice() { return minPrice; }

    public int getMaxPrice() { return maxPrice; }
}

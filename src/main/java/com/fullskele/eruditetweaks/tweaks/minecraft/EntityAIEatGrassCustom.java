package com.fullskele.eruditetweaks.tweaks.minecraft;

import com.fullskele.eruditetweaks.EruditeTweaks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIEatGrassCustom extends EntityAIEatGrass {

    private EntityLiving sheep;
    private World world;
    int eatingTimer;

    public EntityAIEatGrassCustom(EntityLiving entityLiving)
    {
        super(entityLiving);
        this.sheep = entityLiving;
        this.world = entityLiving.world;
        this.setMutexBits(7);
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.sheep.getRNG().nextInt(this.sheep.isChild() ? 50 : 1000) != 0)
        {
            return false;
        }
        else
        {
            BlockPos pos = new BlockPos(this.sheep.posX, this.sheep.posY, this.sheep.posZ);
            IBlockState state = this.world.getBlockState(pos);
            // return if state is contained in tallGrass hashSet || or pos.down blockstate hashMap
            return (EruditeTweaks.SHEEP_TALL_GRASS.contains(state)) || EruditeTweaks.SHEEP_GRASS_CONVERSIONS.containsKey(this.world.getBlockState(pos.down()));
        }
    }

    @Override
    public void startExecuting()
    {
        this.eatingTimer = 40;
        this.world.setEntityState(this.sheep, (byte)10);
        this.sheep.getNavigator().clearPath();
    }

    @Override
    public void resetTask()
    {
        this.eatingTimer = 0;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return this.eatingTimer > 0;
    }

    @Override
    public int getEatingGrassTimer()
    {
        return this.eatingTimer;
    }

    @Override
    public void updateTask()
    {
        this.eatingTimer = Math.max(0, this.eatingTimer - 1);

        if (this.eatingTimer == 4)
        {
            BlockPos pos = new BlockPos(this.sheep.posX, this.sheep.posY, this.sheep.posZ);
            IBlockState state = this.world.getBlockState(pos);

            //Return if state is contained in tallGrass hashSet
            if (EruditeTweaks.SHEEP_TALL_GRASS.contains(state))
            {
                if (this.world.getGameRules().getBoolean("mobGriefing"))
                {
                    this.world.destroyBlock(pos, false);
                }

                this.sheep.eatGrassBonus();
            }
            else {
                BlockPos posDown = pos.down();
                IBlockState stateDown = world.getBlockState(posDown);

                // change to if state is contained in grassBlock hashMap
                if (EruditeTweaks.SHEEP_GRASS_CONVERSIONS.containsKey(stateDown)) {

                    if (this.world.getGameRules().getBoolean("mobGriefing")) {
                        this.world.playEvent(2001, posDown, Block.getIdFromBlock(stateDown.getBlock()));
                        // replace blockstate with value of the key from above in hashMap
                        this.world.setBlockState(posDown, EruditeTweaks.SHEEP_GRASS_CONVERSIONS.get(stateDown));
                    }

                    this.sheep.eatGrassBonus();
                }
            }
        }
    }
}


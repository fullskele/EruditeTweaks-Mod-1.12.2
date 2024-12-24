package com.fullskele.eruditetweaks.tweaks.minecraft;

import com.fullskele.eruditetweaks.EruditeTweaks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockPotato;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

class AIRaidFarmCustom extends EntityAIMoveToBlock {
    private final EntityRabbit rabbit;
    private boolean wantsToRaid;
    private boolean canRaid;

    public AIRaidFarmCustom(EntityRabbit p_i45860_1_) {
        super(p_i45860_1_, 0.699999988079071, 16);
        this.rabbit = p_i45860_1_;
    }

    public boolean shouldExecute() {
        if (this.runDelay <= 0) {
            if (!ForgeEventFactory.getMobGriefingEvent(this.rabbit.world, this.rabbit)) {
                return false;
            }

            this.canRaid = false;
            this.wantsToRaid = true;
        }

        return super.shouldExecute();
    }

    public boolean shouldContinueExecuting() {
        return this.canRaid && super.shouldContinueExecuting();
    }

    public void updateTask() {
        super.updateTask();
        this.rabbit.getLookHelper().setLookPosition((double) this.destinationBlock.getX() + 0.5, (double) (this.destinationBlock.getY() + 1), (double) this.destinationBlock.getZ() + 0.5, 10.0F, (float) this.rabbit.getVerticalFaceSpeed());
        if (this.getIsAboveDestination()) {
            World world = this.rabbit.world;
            BlockPos blockpos = this.destinationBlock.up();
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            if (this.canRaid && block instanceof BlockCrops) {
                Integer integer = (Integer) iblockstate.getValue(BlockCrops.AGE);
                if (integer == 0) {
                    world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
                    world.destroyBlock(blockpos, true);
                } else {
                    world.setBlockState(blockpos, iblockstate.withProperty(BlockCrops.AGE, integer - 1), 2);
                    world.playEvent(2001, blockpos, Block.getStateId(iblockstate));
                }

                createEatingParticles();
            }

            this.canRaid = false;
            this.runDelay = 10;
        }

    }

    protected boolean shouldMoveTo(World p_179488_1_, BlockPos p_179488_2_) {
        Block block = p_179488_1_.getBlockState(p_179488_2_).getBlock();

        if (block instanceof BlockFarmland && this.wantsToRaid && !this.canRaid) {
            p_179488_2_ = p_179488_2_.up();
            IBlockState iblockstate = p_179488_1_.getBlockState(p_179488_2_);
            block = iblockstate.getBlock();

            if (EruditeTweaks.RABBIT_CROPS.contains(iblockstate)) {
                this.canRaid = true;
                return true;
            }
        }

        return false;
    }

    //TODO: Make this take the blockstate as input
    protected void createEatingParticles() {
        BlockPotato blockPotato = (BlockPotato)Blocks.POTATOES;
        IBlockState iblockstate = blockPotato.withAge(blockPotato.getMaxAge());
        rabbit.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, rabbit.posX + (double)(rabbit.world.rand.nextFloat() * rabbit.width * 2.0F) - (double)rabbit.width, rabbit.posY + 0.5 + (double)(rabbit.world.rand.nextFloat() * rabbit.height), rabbit.posZ + (double)(rabbit.world.rand.nextFloat() * rabbit.width * 2.0F) - (double)rabbit.width, 0.0, 0.0, 0.0, new int[]{Block.getStateId(iblockstate)});
    }
}



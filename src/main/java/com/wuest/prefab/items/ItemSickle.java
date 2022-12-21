package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Utils;
import com.wuest.prefab.gui.GuiLangKeys;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;

public class ItemSickle extends TieredItem {
    public static HashSet<Block> effectiveBlocks = new HashSet<>();
    protected int breakRadius = 0;
    protected Tier toolMaterial;

    public ItemSickle(Tier toolMaterial) {
        super(toolMaterial, new Item.Properties());
        this.breakRadius = 1 + toolMaterial.getLevel();
        this.toolMaterial = toolMaterial;
    }

    public static void setEffectiveBlocks() {
        effectiveBlocks.clear();

        effectiveBlocks.addAll(Utils.getBlocksWithTagKey(BlockTags.LEAVES));
        effectiveBlocks.addAll(Utils.getBlocksWithTagKey(BlockTags.SMALL_FLOWERS));
        effectiveBlocks.add(Blocks.TALL_GRASS);
        effectiveBlocks.add(Blocks.DEAD_BUSH);
        effectiveBlocks.add(Blocks.ROSE_BUSH);
        effectiveBlocks.add(Blocks.PEONY);
        effectiveBlocks.add(Blocks.GRASS);
        effectiveBlocks.add(Blocks.SEAGRASS);
        effectiveBlocks.add(Blocks.TALL_SEAGRASS);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Block block = state.getBlock();

        if (!ItemSickle.effectiveBlocks.contains(block) && block != Blocks.COBWEB && state.getMaterial() != Material.LEAVES) {
            return super.getDestroySpeed(stack, state);
        } else {
            return 15.0F;
        }
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the
     * "Use Item" statistic.
     */
    @Override
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos,
                             LivingEntity entityLiving) {
        if (!worldIn.isClientSide) {
            stack.hurtAndBreak(1, entityLiving, (livingEntity) -> livingEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND));

            if ((double) state.getDestroySpeed(worldIn, pos) != 0.0D && !(state.getBlock() instanceof LeavesBlock)) {
                stack.hurtAndBreak(1, entityLiving, (livingEntity) -> livingEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            } else if ((state.getBlock() instanceof BushBlock || state.getBlock() instanceof LeavesBlock)
                    && entityLiving instanceof Player) {
                BlockPos corner1 = pos.north(this.breakRadius).east(this.breakRadius).above(this.breakRadius);
                BlockPos corner2 = pos.south(this.breakRadius).west(this.breakRadius).below(this.breakRadius);

                for (BlockPos currentPos : BlockPos.betweenClosed(corner1, corner2)) {
                    BlockState currentState = worldIn.getBlockState(currentPos);

                    if (currentState != null && ItemSickle.effectiveBlocks.contains(currentState.getBlock())) {
                        worldIn.destroyBlock(currentPos, true);
                    }
                }
            }
        }

        return true;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip,
                                TooltipFlag advanced) {
        super.appendHoverText(stack, worldIn, tooltip, advanced);

        boolean advancedKeyDown = Screen.hasShiftDown();

        if (!advancedKeyDown) {
            tooltip.add(GuiLangKeys.translateToComponent(GuiLangKeys.SHIFT_TOOLTIP));
        } else {
            tooltip.add(GuiLangKeys.translateToComponent(GuiLangKeys.SICKLE_DESC));
        }
    }
}

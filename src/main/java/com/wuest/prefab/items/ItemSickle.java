package com.wuest.prefab.items;

import com.wuest.prefab.gui.GuiLangKeys;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;

public class ItemSickle extends ToolItem {
    public static HashSet<Block> effectiveBlocks = new HashSet<>();
    protected int breakRadius = 0;
    protected IItemTier toolMaterial;

    public ItemSickle(IItemTier toolMaterial) {
        super(1.0f, -2.4000000953674316f, toolMaterial, effectiveBlocks, new Item.Properties().tab(ItemGroup.TAB_TOOLS));
        this.breakRadius = 1 + toolMaterial.getLevel();
        this.toolMaterial = toolMaterial;
    }

    public static void setEffectiveBlocks() {
        effectiveBlocks.clear();

        effectiveBlocks.addAll(BlockTags.LEAVES.getValues());
        effectiveBlocks.addAll(BlockTags.SMALL_FLOWERS.getValues());
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
    public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos,
                            LivingEntity entityLiving) {
        if (!worldIn.isClientSide) {
            stack.hurtAndBreak(1, entityLiving, (livingEntity) -> livingEntity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));

            if ((double) state.getDestroySpeed(worldIn, pos) != 0.0D && !(state.getBlock() instanceof LeavesBlock)) {
                stack.hurtAndBreak(1, entityLiving, (livingEntity) -> livingEntity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
            } else if ((state.getBlock() instanceof BushBlock || state.getBlock() instanceof LeavesBlock)
                    && entityLiving instanceof PlayerEntity) {
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
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                                ITooltipFlag advanced) {
        super.appendHoverText(stack, worldIn, tooltip, advanced);

        boolean advancedKeyDown = Screen.hasShiftDown();

        if (!advancedKeyDown) {
            tooltip.add(GuiLangKeys.translateToComponent(GuiLangKeys.SHIFT_TOOLTIP));
        } else {
            tooltip.add(GuiLangKeys.translateToComponent(GuiLangKeys.SICKLE_DESC));
        }
    }
}

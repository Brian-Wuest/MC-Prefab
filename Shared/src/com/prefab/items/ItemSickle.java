package com.prefab.items;

import com.prefab.PrefabBase;
import com.prefab.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
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

import java.util.HashSet;

public class ItemSickle extends Item {
    public static HashSet<Block> effectiveBlocks = new HashSet<>();
    public static TagKey<Block> MOWABLE = TagKey.create(
            Registries.BLOCK,
            Identifier.fromNamespaceAndPath(PrefabBase.MODID, "mowable"));
    protected int breakRadius = 0;
    public ToolMaterial toolMaterial;

    public ItemSickle(ToolMaterial toolMaterial, Item.Properties itemProperties) {
        super(itemProperties.tool(toolMaterial, MOWABLE, 0.0F, -3.0F, 0.0F));
        this.breakRadius = 1 + (int)toolMaterial.attackDamageBonus();
        this.toolMaterial = toolMaterial;
    }

    public static void setEffectiveBlocks() {
        effectiveBlocks.clear();

        effectiveBlocks.addAll(Utils.getBlocksWithTagKey(MOWABLE));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Block block = state.getBlock();

        if (!ItemSickle.effectiveBlocks.contains(block) && block != Blocks.COBWEB && state.getTags().noneMatch(blockTagKey -> blockTagKey.equals(BlockTags.LEAVES))) {
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
        if (!worldIn.isClientSide()) {
            stack.hurtAndBreak(1, entityLiving, EquipmentSlot.MAINHAND);

            if (entityLiving instanceof Player) {
                if (state.getTags().anyMatch(blockTagKey -> blockTagKey.equals(BlockTags.LEAVES) || blockTagKey.equals(MOWABLE))
                    || state.getBlock() instanceof BushBlock) {
                    this.reapArea(pos, worldIn);
                } else {
                    // Not the right kind of block, deal extra damage to the tool.
                    stack.hurtAndBreak(1, entityLiving, EquipmentSlot.MAINHAND);
                }
            }
        }

        return true;
    }

    private void reapArea(BlockPos pos, Level worldIn) {
        BlockPos corner1 = pos.north(this.breakRadius).east(this.breakRadius).above(this.breakRadius);
        BlockPos corner2 = pos.south(this.breakRadius).west(this.breakRadius).below(this.breakRadius);

        for (BlockPos currentPos : BlockPos.betweenClosed(corner1, corner2)) {
            BlockState currentState = worldIn.getBlockState(currentPos);

            if (ItemSickle.effectiveBlocks.contains(currentState.getBlock())) {
                worldIn.destroyBlock(currentPos, true);
            }
        }
    }
}

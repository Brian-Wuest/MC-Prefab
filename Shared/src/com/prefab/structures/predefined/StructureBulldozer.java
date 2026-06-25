package com.prefab.structures.predefined;

import com.prefab.PrefabBase;
import com.prefab.structures.base.BuildClear;
import com.prefab.structures.base.BuildingMethods;
import com.prefab.structures.base.Structure;
import com.prefab.structures.config.BulldozerConfiguration;
import com.prefab.structures.config.StructureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

/**
 * @author WuestMan
 */
public class StructureBulldozer extends Structure {

    protected static Item diamondPickaxe;
    protected static Item diamondShovel;
    protected static Item diamondAxe;
    protected static ItemStack diamondPickaxeStack;
    protected static ItemStack diamondShovelStack;
    protected static ItemStack diamondAxeStack;

    static {
        StructureBulldozer.diamondAxe = Items.DIAMOND_AXE;
        StructureBulldozer.diamondPickaxe = Items.DIAMOND_PICKAXE;
        StructureBulldozer.diamondShovel = Items.DIAMOND_SHOVEL;
        StructureBulldozer.diamondPickaxeStack = new ItemStack(Items.DIAMOND_PICKAXE);
        StructureBulldozer.diamondShovelStack = new ItemStack(Items.DIAMOND_SHOVEL);
        StructureBulldozer.diamondAxeStack = new ItemStack(Items.DIAMOND_AXE);
    }

    /**
     * Initializes a new instance of the {@link StructureBulldozer} class.
     */
    public StructureBulldozer() {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(Direction.SOUTH);
        clearedSpace.getShape().setHeight(15);
        clearedSpace.getShape().setLength(16);
        clearedSpace.getShape().setWidth(16);
        clearedSpace.getStartingPosition().setSouthOffset(1);
        clearedSpace.getStartingPosition().setEastOffset(8);
        clearedSpace.getStartingPosition().setHeightOffset(1);

        this.setClearSpace(clearedSpace);
        this.setBlocks(new ArrayList<>());
    }

    /**
     * This method is to process before a clear space block is set to air.
     *
     */
    @Override
    protected Boolean BlockShouldBeClearedDuringConstruction(StructureConfiguration configuration, Level world, BlockPos originalPos, BlockPos blockPos) {
        BlockState state = world.getBlockState(blockPos);
        BulldozerConfiguration specificConfiguration = (BulldozerConfiguration) configuration;

        boolean pickAxeEffective = StructureBulldozer.diamondPickaxe.isCorrectToolForDrops(diamondPickaxeStack, state);
        boolean axeEffective = StructureBulldozer.diamondAxe.isCorrectToolForDrops(diamondAxeStack, state);
        boolean shovelEffective = StructureBulldozer.diamondShovel.isCorrectToolForDrops(diamondShovelStack, state);

        if (!specificConfiguration.creativeMode &&
                PrefabBase.serverConfiguration.allowBulldozerToCreateDrops
                && ((state.requiresCorrectToolForDrops() && pickAxeEffective || axeEffective || shovelEffective)
                || !state.requiresCorrectToolForDrops())
                && state.getDestroySpeed(world, blockPos) >= 0.0f) {
            Block.dropResources(state, world, blockPos);
        }

        if (specificConfiguration.creativeMode && state.getBlock() instanceof LiquidBlock) {
            // This is a fluid block, replace it with stone; so it can be cleared.
            BuildingMethods.ReplaceBlock(world, blockPos, Blocks.STONE);
        }

        return true;
    }

    @Override
    public void BeforeHangingEntityRemoved(HangingEntity hangingEntity) {
        // Only generate drops for this hanging entity if the bulldozer allows it.
        // By default the base class doesn't allow hanging entities to generate drops.
        if (PrefabBase.serverConfiguration.allowBulldozerToCreateDrops) {
            hangingEntity.dropItem(this.world, null);
        }
    }
}
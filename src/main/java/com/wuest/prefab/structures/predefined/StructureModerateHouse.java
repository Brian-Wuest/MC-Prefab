package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.config.EntityPlayerConfiguration;
import com.wuest.prefab.structures.base.BuildBlock;
import com.wuest.prefab.structures.base.BuildClear;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.ModerateHouseConfiguration;
import com.wuest.prefab.structures.config.StructureConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * @author WuestMan
 */
public class StructureModerateHouse extends Structure {
    private static ArrayList<BlockPos> torchPositions = null;
    private BlockPos chestPosition = null;
    private ArrayList<BlockPos> furnacePosition = null;
    private BlockPos trapDoorPosition = null;

    public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing, ModerateHouseConfiguration.HouseStyle houseStyle) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
        clearedSpace.getShape().setHeight(houseStyle.getHeight());
        clearedSpace.getShape().setLength(houseStyle.getLength());
        clearedSpace.getShape().setWidth(houseStyle.getWidth());
        clearedSpace.getStartingPosition().setSouthOffset(1);
        clearedSpace.getStartingPosition().setEastOffset(houseStyle.getEastOffSet());
        clearedSpace.getStartingPosition().setHeightOffset(houseStyle.getDownOffSet() * -1);

        BlockPos cornerPos = originalPos.east(houseStyle.getEastOffSet()).south().down(houseStyle.getDownOffSet());

        Structure.ScanStructure(
                world,
                originalPos,
                cornerPos,
                cornerPos.south(houseStyle.getLength()).west(houseStyle.getWidth()).up(houseStyle.getHeight()),
                "../src/main/resources/" + houseStyle.getStructureLocation(),
                clearedSpace,
                playerFacing, false, false);
    }

    public static void FillChest(World world, BlockPos itemPosition, ModerateHouseConfiguration configuration, EntityPlayer player) {
        // Add each stone tool to the chest and leather armor.
        TileEntity tileEntity = world.getTileEntity(itemPosition);

        if (tileEntity instanceof TileEntityChest) {
            TileEntityChest chestTile = (TileEntityChest) tileEntity;

            int itemSlot = 0;

            // Add the tools.
            if (Prefab.proxy.proxyConfiguration.addAxe) {
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_AXE));
            }

            if (Prefab.proxy.proxyConfiguration.addHoe) {
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_HOE));
            }

            if (Prefab.proxy.proxyConfiguration.addPickAxe) {
                // Trigger the "Time to Mine" achievement and the better
                // pick axe achievement.
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_PICKAXE));
            }

            if (Prefab.proxy.proxyConfiguration.addShovel) {
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_SHOVEL));
            }

            if (Prefab.proxy.proxyConfiguration.addSword) {
                // Include the swift blade if WuestUtilities has registered the
                // swift blades.
                ResourceLocation name = new ResourceLocation("repurpose", "itemSwiftBladeStone");
                Item sword = Item.REGISTRY.getObject(name);

                if (sword == null) {
                    sword = Items.STONE_SWORD;
                }

                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(sword));
            }

            if (Prefab.proxy.proxyConfiguration.addArmor) {
                // Add the armor.
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_BOOTS));
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_CHESTPLATE));
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_HELMET));
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_LEGGINGS));
            }

            if (Prefab.proxy.proxyConfiguration.addFood) {
                // Add some bread.
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.BREAD, 20));
            }

            if (Prefab.proxy.proxyConfiguration.addCrops) {
                // Add potatoes.
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.POTATO, 3));

                // Add carrots.
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.CARROT, 3));

                // Add seeds.
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.WHEAT_SEEDS, 3));
            }

            if (Prefab.proxy.proxyConfiguration.addCobble) {
                // Add Cobblestone.
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.COBBLESTONE), 64));
            }

            if (Prefab.proxy.proxyConfiguration.addDirt) {
                // Add Dirt.
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.DIRT), 64));
            }

            if (Prefab.proxy.proxyConfiguration.addSaplings) {
                // Add oak saplings.
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 3));
            }

            if (Prefab.proxy.proxyConfiguration.addTorches) {
                // Add a set of 20 torches.
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.TORCH), 20));
            }
        }
    }

    @Override
    protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
                                                   EnumFacing assumedNorth, Block foundBlock, IBlockState blockState, EntityPlayer player) {
        if (foundBlock instanceof BlockFurnace) {
            if (this.furnacePosition == null) {
                this.furnacePosition = new ArrayList<BlockPos>();
            }

            this.furnacePosition.add(block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing));
        } else if (foundBlock instanceof BlockChest && !((ModerateHouseConfiguration) configuration).addChests) {
            return true;
        } else if (foundBlock instanceof BlockChest && this.chestPosition == null) {
            this.chestPosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock instanceof BlockTrapDoor) {
            // The trap door will still be added, but the mine shaft may not be
            // built.
            this.trapDoorPosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        }

        return false;
    }

    /**
     * This method is used after the main building is build for any additional
     * structures or modifications.
     *
     * @param configuration The structure configuration.
     * @param world         The current world.
     * @param originalPos   The original position clicked on.
     * @param assumedNorth  The assumed northern direction.
     * @param player        The player which initiated the construction.
     */
    @Override
    public void AfterBuilding(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player) {
        ModerateHouseConfiguration houseConfig = (ModerateHouseConfiguration) configuration;
        EntityPlayerConfiguration playerConfig = EntityPlayerConfiguration.loadFromEntityData((EntityPlayerMP) player);

        if (this.furnacePosition != null) {
            for (BlockPos furnacePos : this.furnacePosition) {
                // Fill the furnace.
                TileEntity tileEntity = world.getTileEntity(furnacePos);

                if (tileEntity instanceof TileEntityFurnace) {
                    TileEntityFurnace furnaceTile = (TileEntityFurnace) tileEntity;
                    furnaceTile.setInventorySlotContents(1, new ItemStack(Items.COAL, 20));
                }
            }
        }

        if (this.chestPosition != null && !playerConfig.builtStarterHouse && houseConfig.addChestContents) {
            // Fill the chest if the player hasn't generated the starting house yet.
            StructureModerateHouse.FillChest(world, this.chestPosition, houseConfig, player);
        }

        if (this.trapDoorPosition != null && this.trapDoorPosition.getY() > 15 && houseConfig.addMineshaft) {
            // Build the mineshaft.
            StructureAlternateStart.PlaceMineShaft(world, this.trapDoorPosition.down(), houseConfig.houseFacing, false);
        }

        // Make sure to set this value so the player cannot fill the chest a second time.
        playerConfig.builtStarterHouse = true;
        playerConfig.saveToPlayer(player);
    }

}
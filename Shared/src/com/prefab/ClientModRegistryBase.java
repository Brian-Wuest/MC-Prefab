package com.prefab;

import com.prefab.base.BaseConfig;
import com.prefab.config.EntityPlayerConfiguration;
import com.prefab.gui.GuiBase;
import com.prefab.gui.screens.GuiStructureScanner;
import com.prefab.structures.gui.GuiStructure;
import com.prefab.structures.items.StructureItem;
import com.prefab.blocks.BlockCustomWall;
import com.prefab.blocks.BlockGrassSlab;
import com.prefab.blocks.BlockGrassStairs;
import com.prefab.config.StructureScannerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ClientModRegistryBase {
    public static ArrayList<StructureScannerConfig> structureScanners;

    public static EntityPlayerConfiguration playerConfig = new EntityPlayerConfiguration();

    /**
     * The hashmap of mod guis.
     */
    public static HashMap<StructureItem, GuiStructure> ModGuis = new HashMap<>();

    static {
        ClientModRegistryBase.structureScanners = new ArrayList<>();
    }

    public static void registerModComponents() {
        ClientModRegistryBase.RegisterGuis();
    }

    public static void openGuiForItem(UseOnContext itemUseContext) {
        for (Map.Entry<StructureItem, GuiStructure> entry : ClientModRegistryBase.ModGuis.entrySet()) {
            if (entry.getKey() == itemUseContext.getItemInHand().getItem()) {
                GuiStructure screen = entry.getValue();
                screen.pos = itemUseContext.getClickedPos();

                Minecraft.getInstance().setScreen(screen);
            }
        }
    }

    public static void openGuiForBlock(BlockPos blockPos, Level world, BaseConfig config) {
        GuiBase screen = null;

        if (config instanceof StructureScannerConfig) {
            screen = new GuiStructureScanner(blockPos, world, (StructureScannerConfig) config);
        }

        if (screen != null) {
            Minecraft.getInstance().setScreen(screen);
        }
    }

    /**
     * This is called within a Mixin as the BlockColors and ItemColors classes are otherwise null when this class is called.
     */
    public static void RegisterBlockRenderer() {
        // Register the block renderer.
        Minecraft.getInstance().getBlockColors().register((state, worldIn, pos, tintIndex) -> worldIn != null && pos != null
                ? BiomeColors.getAverageGrassColor(worldIn, pos)
                : GrassColor.get(0.5D, 1.0D), ModRegistryBase.GrassWall, ModRegistryBase.GrassSlab, ModRegistryBase.GrassStairs);

        // Register the item renderer.
        /*Minecraft.getInstance().itemColors.register((stack, tintIndex) -> {
            // Get the item for this stack.
            Item item = stack.getItem();

            if (item instanceof BlockItem) {
                // Get the block for this item and determine if it's a grass stairs.
                BlockItem itemBlock = (BlockItem) item;
                boolean paintBlock = false;

                if (itemBlock.getBlock() instanceof BlockCustomWall) {
                    BlockCustomWall customWall = (BlockCustomWall) itemBlock.getBlock();

                    if (customWall.BlockVariant == BlockCustomWall.EnumType.GRASS) {
                        paintBlock = true;
                    }
                } else if (itemBlock.getBlock() instanceof BlockGrassSlab) {
                    paintBlock = true;
                } else if (itemBlock.getBlock() instanceof BlockGrassStairs) {
                    paintBlock = true;
                }

                if (paintBlock) {
                    BlockPos pos = Minecraft.getInstance().player.blockPosition();
                    ClientLevel world = Minecraft.getInstance().level;
                    return BiomeColors.getAverageGrassColor(world, pos);
                }
            }

            return -1;
        }, new Block[]{ModRegistryBase.GrassWall, ModRegistryBase.GrassSlab, ModRegistryBase.GrassStairs});*/
    }

    /**
     * Adds all of the Mod Guis to the HasMap.
     */
    public static void RegisterGuis() {
        for (Consumer<Object> consumer : ModRegistryBase.guiRegistrations) {
            consumer.accept(null);
        }
    }
}

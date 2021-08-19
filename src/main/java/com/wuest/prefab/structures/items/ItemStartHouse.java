package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.gui.GuiStartHouseChooser;
import com.wuest.prefab.structures.predefined.StructureAlternateStart;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ALL")
public class ItemStartHouse extends StructureItem {
    public ItemStartHouse() {
        super();
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void Initialize() {
        ModRegistry.guiRegistrations.add(x -> this.RegisterGui(GuiStartHouseChooser.class));
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide()) {
            if (context.getClickedFace() == Direction.UP) {
                if (!Prefab.useScanningMode) {
                    // Open the client side gui to determine the house options.
                    Prefab.proxy.openGuiForItem(context);
                } else {
                    this.scanningMode(context);
                }
                return InteractionResult.PASS;
            }
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void scanningMode(UseOnContext context) {
        /*StructureAlternateStart.ScanBasicHouseStructure(
                context.getLevel(),
                context.getClickedPos(),
                context.getPlayer().getDirection());*/

        /*StructureAlternateStart.ScanRanchStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());*/

		/*StructureAlternateStart.ScanLoftStructure(
				context.getWorld(),
				context.getPos(),
				context.getPlayer().getHorizontalFacing());*/

        /*StructureAlternateStart.ScanHobbitStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing());*/

        /*StructureAlternateStart.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing(), "desert_house", false, false);*/

		/*StructureAlternateStart.ScanDesert2Structure(
				context.getWorld(),
				context.getPos(),
				context.getPlayer().getHorizontalFacing());*/

		/*StructureAlternateStart.ScanSubAquaticStructure(
				context.getWorld(),
				context.getPos(),
				context.getPlayer().getHorizontalFacing());*/

		/*StructureAlternateStart.ScanStructure(
				context.getWorld(),
				context.getPos(),
				context.getPlayer().getHorizontalFacing(), "snowy_house", false, false);*/

        StructureAlternateStart.ScanModernHouseStructure(
                context.getLevel(),
                context.getClickedPos(),
                context.getPlayer().getDirection());
    }
}
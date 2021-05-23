package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Gui.GuiStartHouseChooser;
import com.wuest.prefab.Structures.Predefined.StructureAlternateStart;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;

/**
 * @author WuestMan
 */
@SuppressWarnings("ALL")
public class ItemStartHouse extends StructureItem {
	public ItemStartHouse( ) {
		super();
	}

	/**
	 * Initializes common fields/properties for this structure item.
	 */
	@Override
	protected void Initialize() {
		if (Prefab.proxy.isClient) {
			this.RegisterGui(GuiStartHouseChooser.class);
		}
	}

	/**
	 * Does something when the item is right-clicked.
	 */
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		if (context.getLevel().isClientSide()) {
			if (context.getClickedFace() == Direction.UP) {
				if (!Prefab.useScanningMode) {
					// Open the client side gui to determine the house options.
					Prefab.proxy.openGuiForItem(context);
				} else {
					this.scanningMode(context);
				}
				return ActionResultType.PASS;
			}
		}

		return ActionResultType.FAIL;
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureAlternateStart.ScanBasicHouseStructure(
				context.getLevel(),
				context.getClickedPos(),
				context.getPlayer().getDirection());

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
	}
}
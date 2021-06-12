package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Gui.GuiAdvancedWareHouse;
import com.wuest.prefab.Structures.Predefined.StructureWarehouse;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemAdvancedWareHouse extends ItemWareHouse {
	public ItemAdvancedWareHouse( ) {
		super();
	}

	/**
	 * Initializes common fields/properties for this structure item.
	 */
	@Override
	protected void Initialize() {
		if (Prefab.proxy.isClient) {
			this.RegisterGui(GuiAdvancedWareHouse.class);
		}
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureWarehouse.ScanStructure(
				context.getLevel(),
				context.getClickedPos(),
				context.getPlayer().getDirection(),
				true);
	}
}
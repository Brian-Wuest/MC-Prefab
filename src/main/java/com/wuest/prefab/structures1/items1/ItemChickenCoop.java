package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Gui.GuiChickenCoop;
import com.wuest.prefab.Structures.Predefined.StructureChickenCoop;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemChickenCoop extends StructureItem {
	public ItemChickenCoop( ) {
		super();
	}

	/**
	 * Initializes common fields/properties for this structure item.
	 */
	@Override
	protected void Initialize() {
		if (Prefab.proxy.isClient) {
			this.RegisterGui(GuiChickenCoop.class);
		}
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureChickenCoop.ScanStructure(
				context.getLevel(),
				context.getClickedPos(),
				context.getPlayer().getDirection());
	}
}

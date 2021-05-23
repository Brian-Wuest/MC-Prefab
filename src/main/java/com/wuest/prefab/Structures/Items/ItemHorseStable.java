package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Gui.GuiHorseStable;
import com.wuest.prefab.Structures.Predefined.StructureHorseStable;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemHorseStable extends StructureItem {
	public ItemHorseStable( ) {
		super();
	}

	/**
	 * Initializes common fields/properties for this structure item.
	 */
	@Override
	protected void Initialize() {
		if (Prefab.proxy.isClient) {
			this.RegisterGui(GuiHorseStable.class);
		}
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureHorseStable.ScanStructure(
				context.getLevel(),
				context.getClickedPos(),
				context.getPlayer().getDirection());
	}
}

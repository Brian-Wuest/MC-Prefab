package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Gui.GuiMonsterMasher;
import com.wuest.prefab.Structures.Predefined.StructureMonsterMasher;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemMonsterMasher extends StructureItem {
	public ItemMonsterMasher( ) {
		super();
	}

	/**
	 * Initializes common fields/properties for this structure item.
	 */
	@Override
	protected void Initialize() {
		if (Prefab.proxy.isClient) {
			this.RegisterGui(GuiMonsterMasher.class);
		}
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureMonsterMasher.ScanStructure(
				context.getLevel(),
				context.getClickedPos(),
				context.getPlayer().getDirection());
	}
}

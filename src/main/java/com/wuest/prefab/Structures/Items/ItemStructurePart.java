package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Gui.GuiStructurePart;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * @author WuestMan
 */
public class ItemStructurePart extends StructureItem {
	public ItemStructurePart() {
		super(new Item.Properties()
				.tab(ItemGroup.TAB_MISC)
				.durability(10));
	}

	/**
	 * Initializes common fields/properties for this structure item.
	 */
	@Override
	protected void Initialize() {
		if (Prefab.proxy.isClient) {
			this.RegisterGui(GuiStructurePart.class);
		}
	}
}

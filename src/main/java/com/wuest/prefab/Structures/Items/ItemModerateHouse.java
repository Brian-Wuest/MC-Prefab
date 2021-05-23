package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Config.ModerateHouseConfiguration;
import com.wuest.prefab.Structures.Gui.GuiModerateHouse;
import com.wuest.prefab.Structures.Predefined.StructureModerateHouse;
import net.minecraft.item.ItemUseContext;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class ItemModerateHouse extends StructureItem {
	/**
	 * Initializes a new instance of the {@link ItemModerateHouse} class.
	 */
	public ItemModerateHouse( ) {
		super();
	}

	/**
	 * Initializes common fields/properties for this structure item.
	 */
	@Override
	protected void Initialize() {
		if (Prefab.proxy.isClient) {
			this.RegisterGui(GuiModerateHouse.class);
		}
	}

	@Override
	public void scanningMode(ItemUseContext context) {
		StructureModerateHouse.ScanStructure(
				context.getLevel(),
				context.getClickedPos(),
				context.getPlayer().getDirection(),
				ModerateHouseConfiguration.HouseStyle.MOUNTAIN_HOME);
	}
}

package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;

/**
 * @author WuestMan
 */
@SuppressWarnings("NullableProblems")
public class StructureItem extends Item {

	/**
	 * Initializes a new instance of the StructureItem class.
	 */
	public StructureItem() {
		super(new Item.Properties().group(ItemGroup.MISC));
		this.Initialize();
	}

	public StructureItem(Item.Properties properties) {
		super(properties);
		this.Initialize();
	}

	/**
	 * Does something when the item is right-clicked.
	 */
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (context.getWorld().isRemote) {
			if (context.getFace() == Direction.UP) {
				if (Prefab.useScanningMode) {
					this.scanningMode(context);
				} else {
					// Open the client side gui to determine the house options.
					Prefab.proxy.openGuiForItem(context);
				}

				return ActionResultType.PASS;
			}
		}

		return ActionResultType.FAIL;
	}

	public void scanningMode(ItemUseContext context) {
	}

	/**
	 * Initializes common fields/properties for this structure item.
	 */
	protected void Initialize() {
	}
}

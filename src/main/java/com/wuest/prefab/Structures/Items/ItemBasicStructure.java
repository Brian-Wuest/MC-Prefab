package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Structures.Config.BasicStructureConfiguration;
import com.wuest.prefab.Structures.Config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.Structures.Config.Enums.AdvancedAquaBaseOptions;
import com.wuest.prefab.Structures.Config.Enums.NetherGateOptions;
import com.wuest.prefab.Structures.Config.Enums.SugarCaneFarmOptions;
import com.wuest.prefab.Structures.Predefined.StructureBasic;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;

/**
 * This class is used for basic structures to show the basic GUI.
 *
 * @author WuestMan
 */
@SuppressWarnings({"AccessStaticViaInstance", "ConstantConditions"})
public class ItemBasicStructure extends StructureItem {
	public final EnumBasicStructureName structureType;

	public ItemBasicStructure(EnumBasicStructureName structureType) {
		super();

		this.structureType = structureType;
	}

	public static ItemStack getBasicStructureItemInHand(PlayerEntity player) {
		ItemStack stack = player.getOffhandItem();

		// Get off hand first since that is the right-click hand if there is
		// something in there.
		if (!(stack.getItem() instanceof ItemBasicStructure)) {
			if (player.getMainHandItem().getItem() instanceof ItemBasicStructure) {
				stack = player.getMainHandItem();
			} else {
				stack = null;
			}
		}

		return stack;
	}

	/**
	 * Does something when the item is right-clicked.
	 */
	@Override
	public void scanningMode(ItemUseContext context) {
		StructureBasic basicStructure = new StructureBasic();
		ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
		BasicStructureConfiguration structureConfiguration = new BasicStructureConfiguration();
		structureConfiguration.basicStructureName = ((ItemBasicStructure) stack.getItem()).structureType;
		structureConfiguration.chosenOption = AdvancedAquaBaseOptions.Default;

		boolean isWaterStructure = structureConfiguration.basicStructureName == EnumBasicStructureName.AquaBase
				|| structureConfiguration.basicStructureName == EnumBasicStructureName.AdvancedAquaBase;

		basicStructure.ScanStructure(
				context.getLevel(),
				context.getClickedPos(),
				context.getPlayer().getDirection(),
				structureConfiguration, isWaterStructure, isWaterStructure);
	}
}
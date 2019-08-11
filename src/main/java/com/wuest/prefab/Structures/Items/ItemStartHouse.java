package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Config.VillagerHouseConfiguration;
import com.wuest.prefab.Structures.Gui.GuiStartHouseChooser;
import com.wuest.prefab.Structures.Gui.GuiStructure;
import com.wuest.prefab.Structures.Predefined.StructureAlternateStart;
import com.wuest.prefab.Structures.Predefined.StructureVillagerHouses;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author WuestMan
 */
public class ItemStartHouse extends StructureItem {
	public ItemStartHouse(String name) {
		super(name);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public GuiStructure getScreen() {
		return null;
	}

	/**
	 * Does something when the item is right-clicked.
	 */
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (context.getWorld().isRemote) {
			if (context.getFace() == Direction.UP) {
				if (!Prefab.useScanningMode) {
					// Open the client side gui to determine the house options.
					GuiStartHouseChooser screen = new GuiStartHouseChooser(context.getPos());
					Minecraft.getInstance().displayGuiScreen(screen);
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

        StructureAlternateStart.ScanStructure(
                context.getWorld(),
                context.getPos(),
                context.getPlayer().getHorizontalFacing(), "snowy_house", false, false);
	}
}
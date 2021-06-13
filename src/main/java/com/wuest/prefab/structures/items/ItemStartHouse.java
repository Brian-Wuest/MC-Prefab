package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.gui.GuiStartHouseChooser;
import com.wuest.prefab.structures.predefined.StructureAlternateStart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author WuestMan
 */
public class ItemStartHouse extends StructureItem {
    public ItemStartHouse(String name) {
        super(name);
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void PostInit() {
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiStartHouseChooser.class);
        }
    }

    @Override
    public void scanningMode(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand) {
        StructureAlternateStart.ScanBasicHouseStructure(
                world,
                hitBlockPos,
                player.getHorizontalFacing());

        /*StructureAlternateStart.ScanRanchStructure(
                world,
                hitBlockPos,
                player.getHorizontalFacing());*/

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
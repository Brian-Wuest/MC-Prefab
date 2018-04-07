package com.wuest.prefab.Render;

import com.wuest.prefab.Structures.Capabilities.IStructureConfigurationCapability;
import com.wuest.prefab.Structures.Config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.Structures.Items.ItemBasicStructure;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * This class is used to get model locations for the various items in this mod.
 * 
 * @author WuestMan
 *
 */
public class PrefabModelMesher implements ItemMeshDefinition
{

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		IStructureConfigurationCapability structureCapability = ItemBasicStructure.getStackCapability(stack);
		ResourceLocation resourceLocation = null;

		if (structureCapability != null)
		{
			resourceLocation = structureCapability.getConfiguration().basicStructureName.getResourceLocation();
		}

		if (resourceLocation == null)
		{
			resourceLocation = EnumBasicStructureName.AdvancedCoop.getResourceLocation();
		}

		return new ModelResourceLocation(resourceLocation, "inventory");

	}
}
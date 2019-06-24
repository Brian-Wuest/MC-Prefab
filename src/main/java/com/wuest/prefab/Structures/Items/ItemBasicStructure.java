package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Capabilities.IStructureConfigurationCapability;
import com.wuest.prefab.Structures.Capabilities.StructureConfigurationCapability;
import com.wuest.prefab.Structures.Capabilities.Storage.StructureConfigurationStorage;
import com.wuest.prefab.Structures.Config.BasicStructureConfiguration;
import com.wuest.prefab.Structures.Config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.Structures.Gui.GuiBasicStructure;
import com.wuest.prefab.Structures.Predefined.StructureBasic;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

/**
 * This class is used for basic structures to show the basic GUI.
 * 
 * @author WuestMan
 *
 */
public class ItemBasicStructure extends StructureItem
{

	public ItemBasicStructure(String name)
	{
		super(name, new GuiBasicStructure());
	}

	/**
	 * Does something when the item is right-clicked.
	 */
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		if (context.getWorld().isRemote)
		{
			if (context.getFace() == Direction.UP)
			{
				
				/*StructureBasic basicStructure = new StructureBasic(); ItemStack stack = player.getHeldItem(hand);
				IStructureConfigurationCapability capability =
				stack.getCapability(ModRegistry.StructureConfiguration, Direction.NORTH);
				BasicStructureConfiguration structureConfiguration = capability.getConfiguration();
				basicStructure.ScanStructure(world, hitBlockPos, player.getHorizontalFacing(),
				structureConfiguration, false, false);*/
					 
				// Open the client side gui to determine the house options.
				this.screen.pos = context.getPos();
				
				Minecraft.getInstance().displayGuiScreen(this.screen);
				// context.getPlayer().openGui(Prefab.instance, this.guiId, context.getPlayer().world, context.getPos().getX(), context.getPos().getY(), context.getPos().getZ());

				return ActionResultType.PASS;
			}
		}

		return ActionResultType.FAIL;
	}

	/**
	 * Override this method to change the NBT data being sent to the client. You should ONLY override this when you have
	 * no other choice, as this might change behavior client side!
	 *
	 * @param stack The stack to send the NBT tag for
	 * @return The NBT tag
	 */
	@Override
	public CompoundNBT getShareTag(ItemStack stack)
	{
		if (stack.getTag() == null || stack.getTag().isEmpty())
		{
			// Make sure to serialize the NBT for this stack so the information is pushed to the client and the
			// appropriate Icon is displayed for this stack.
			stack.setTag(stack.serializeNBT());
		}

		return stack.getTag();
	}

	public static ItemStack getBasicStructureItemInHand(PlayerEntity player)
	{
		ItemStack stack = player.getHeldItemOffhand();

		// Get off hand first since that is the right-click hand if there is
		// something in there.
		if (stack == null || !(stack.getItem() instanceof ItemBasicStructure))
		{
			if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemBasicStructure)
			{
				stack = player.getHeldItemMainhand();
			}
			else
			{
				stack = null;
			}
		}

		return stack;
	}

	public static IStructureConfigurationCapability getStackCapability(ItemStack stack)
	{
		LazyOptional<IStructureConfigurationCapability> stackCapability = stack.getCapability(ModRegistry.StructureConfiguration);
		
		if (stackCapability.isPresent())
		{
			return stackCapability.map(capability -> {
			
				CompoundNBT forgeCapabilities = stack.getChildTag("ForgeCaps");
				if (forgeCapabilities != null)
				{
					if (capability.getDirty() && forgeCapabilities.contains(Prefab.MODID + ":structuresconfiguration"))
					{
						StructureConfigurationCapability capabilityTemp = new StructureConfigurationCapability();
						StructureConfigurationStorage storage = new StructureConfigurationStorage();
						storage.readNBT(ModRegistry.StructureConfiguration, capabilityTemp, Direction.NORTH,
							forgeCapabilities.get(Prefab.MODID + ":structuresconfiguration"));

						if (!capabilityTemp.getConfiguration().basicStructureName.getName().equals(capability.getConfiguration().basicStructureName.getName()))
						{
							capability.setConfiguration(capabilityTemp.getConfiguration());
							capability.setDirty(false);
						}
					}
				}
				
				return capability;
			}).orElse(null);
		}

		return null;
	}
}
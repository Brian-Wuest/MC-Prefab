package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Base.EnumStructureMaterial;
import com.wuest.prefab.Structures.Predefined.StructureInstantBridge;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * @author WuestMan
 */
public class InstantBridgeConfiguration extends StructureConfiguration {
	/**
	 * Determines how long this bridge is.
	 */
	public int bridgeLength;

	/**
	 * Determines the type of material to build the bridge with.
	 */
	public EnumStructureMaterial bridgeMaterial;

	/**
	 * Determine is a roof is included.
	 */
	public boolean includeRoof;

	/**
	 * Determines how tall the inside of the bridge is if there is a roof.
	 */
	public int interiorHeight;

	/**
	 * Initializes any properties for this class.
	 */
	@Override
	public void Initialize() {
		super.Initialize();
		this.bridgeLength = 25;
		this.bridgeMaterial = EnumStructureMaterial.Cobblestone;
		this.interiorHeight = 3;
		this.includeRoof = true;
	}

	/**
	 * Custom method to read the CompoundNBT message.
	 *
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the CompoundNBT.
	 */
	@Override
	public InstantBridgeConfiguration ReadFromCompoundNBT(CompoundNBT messageTag) {
		InstantBridgeConfiguration config = new InstantBridgeConfiguration();

		return (InstantBridgeConfiguration) super.ReadFromCompoundNBT(messageTag, config);
	}

	@Override
	protected void ConfigurationSpecificBuildStructure(PlayerEntity player, ServerWorld world, BlockPos hitBlockPos) {
		StructureInstantBridge structure = StructureInstantBridge.CreateInstance();

		if (structure.BuildStructure(this, world, hitBlockPos, Direction.NORTH, player)) {
			this.DamageHeldItem(player, ModRegistry.InstantBridge.get());
		}
	}

	/**
	 * Custom method which can be overridden to write custom properties to the tag.
	 *
	 * @param tag The CompoundNBT to write the custom properties too.
	 * @return The updated tag.
	 */
	@Override
	protected CompoundNBT CustomWriteToCompoundNBT(CompoundNBT tag) {
		tag.putInt("bridgeLength", this.bridgeLength);
		tag.putInt("bridgeMaterial", this.bridgeMaterial.getNumber());
		tag.putBoolean("includeRoof", this.includeRoof);
		tag.putInt("interiorHeight", this.interiorHeight);
		return tag;
	}

	/**
	 * Custom method to read the CompoundNBT message.
	 *
	 * @param messageTag The message to create the configuration from.
	 * @param config     The configuration to read the settings into.
	 */
	@Override
	protected void CustomReadFromNBTTag(CompoundNBT messageTag, StructureConfiguration config) {
		if (messageTag.contains("bridgeLength")) {
			((InstantBridgeConfiguration) config).bridgeLength = messageTag.getInt("bridgeLength");
		}

		if (messageTag.contains("bridgeMaterial")) {
			((InstantBridgeConfiguration) config).bridgeMaterial = EnumStructureMaterial.getMaterialByNumber(messageTag.getInt("bridgeMaterial"));
		}

		if (messageTag.contains("includeRoof")) {
			((InstantBridgeConfiguration) config).includeRoof = messageTag.getBoolean("includeRoof");
		}

		if (messageTag.contains("interiorHeight")) {
			((InstantBridgeConfiguration) config).interiorHeight = messageTag.getInt("interiorHeight");
		}
	}
}

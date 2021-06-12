package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Base.EnumStairsMaterial;
import com.wuest.prefab.Structures.Base.EnumStructureMaterial;
import com.wuest.prefab.Structures.Predefined.StructurePart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * @author WuestMan
 */
public class StructurePartConfiguration extends StructureConfiguration {
	/**
	 * Determines the type of material to build the part with.
	 */
	public EnumStructureMaterial partMaterial;

	/**
	 * Determines the material used for stairs and roofs.
	 */
	public EnumStairsMaterial stairsMaterial;

	/**
	 * The height for this non-stairs style.
	 */
	public int generalWidth;

	/**
	 * The width for this non-stairs style.
	 */
	public int generalHeight;

	/**
	 * The width of stairs when the style is set to Stairs.
	 */
	public int stairWidth;

	/**
	 * The height of the stairs when the style is set to Stairs.
	 */
	public int stairHeight;

	/**
	 * The style of this part.
	 */
	public EnumStyle style;

	/**
	 * Initializes any properties for this class.
	 */
	@Override
	public void Initialize() {
		super.Initialize();

		this.partMaterial = EnumStructureMaterial.Cobblestone;
		this.stairsMaterial = EnumStairsMaterial.Cobblestone;
		this.style = EnumStyle.DoorWay;
		this.stairHeight = 3;
		this.stairWidth = 2;
		this.generalHeight = 3;
		this.generalWidth = 3;
	}

	/**
	 * Custom method to read the CompoundNBT message.
	 *
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the CompoundNBT.
	 */
	@Override
	public StructurePartConfiguration ReadFromCompoundNBT(CompoundNBT messageTag) {
		StructurePartConfiguration config = new StructurePartConfiguration();

		return (StructurePartConfiguration) super.ReadFromCompoundNBT(messageTag, config);
	}

	@Override
	protected void ConfigurationSpecificBuildStructure(PlayerEntity player, ServerWorld world, BlockPos hitBlockPos) {
		StructurePart structure = StructurePart.CreateInstance();

		if (structure.BuildStructure(this, world, hitBlockPos, Direction.NORTH, player)) {
			this.DamageHeldItem(player, ModRegistry.StructurePart.get());
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
		tag.putString("material", this.partMaterial.name());
		tag.putString("style", this.style.name());
		tag.putInt("stair_height", this.stairHeight);
		tag.putInt("stair_width", this.stairWidth);
		tag.putInt("general_height", this.generalHeight);
		tag.putInt("general_width", this.generalWidth);
		tag.putString("stairs_material", this.stairsMaterial.name());
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
		if (messageTag.contains("material")) {
			((StructurePartConfiguration) config).partMaterial = EnumStructureMaterial.valueOf(messageTag.getString("material"));
		}

		if (messageTag.contains("stairs_material")) {
			((StructurePartConfiguration) config).stairsMaterial = EnumStairsMaterial.valueOf(messageTag.getString("stairs_material"));
		}

		if (messageTag.contains("style")) {
			((StructurePartConfiguration) config).style = EnumStyle.valueOf(messageTag.getString("style"));
		}

		if (messageTag.contains("stair_height")) {
			((StructurePartConfiguration) config).stairHeight = messageTag.getInt("stair_height");
		}

		if (messageTag.contains("stair_width")) {
			((StructurePartConfiguration) config).stairWidth = messageTag.getInt("stair_width");
		}

		if (messageTag.contains("general_height")) {
			((StructurePartConfiguration) config).generalHeight = messageTag.getInt("general_height");
		}

		if (messageTag.contains("general_width")) {
			((StructurePartConfiguration) config).generalWidth = messageTag.getInt("general_width");
		}
	}

	/**
	 * @author WuestMan
	 */
	public enum EnumStyle {
		DoorWay("prefab.gui.part_style.door_way", "textures/gui/doorway_topdown.png", 164, 141),
		Floor("prefab.gui.part_style.floor", "textures/gui/floor_topdown.png", 166, 121),
		Frame("prefab.gui.part_style.frame", "textures/gui/frame_topdown.png", 167, 145),
		Gate("prefab.gui.part_style.gate", "textures/gui/gate_topdown.png", 162, 141),
		Roof("prefab.gui.part_style.roof", "textures/gui/roof_topdown.png", 167, 113),
		Stairs("prefab.gui.part_style.stairs", "textures/gui/stairs_topdown.png", 142, 169),
		Wall("prefab.gui.part_style.wall", "textures/gui/wall_topdown.png", 166, 154);

		public final String translateKey;
		public final String resourceLocation;
		public final int imageWidth;
		public final int imageHeight;

		EnumStyle(String translateKey, String resourceLocation, int imageWidth, int imageHeight) {
			this.translateKey = translateKey;
			this.resourceLocation = resourceLocation;
			this.imageHeight = imageHeight;
			this.imageWidth = imageWidth;
		}

		public static EnumStyle getByOrdinal(int ordinal) {
			for (EnumStyle dimension : EnumStyle.values()) {
				if (dimension.ordinal() == ordinal) {
					return dimension;
				}
			}

			return EnumStyle.DoorWay;
		}

		public ResourceLocation getPictureLocation() {
			if (this.resourceLocation != null) {
				return new ResourceLocation("prefab", this.resourceLocation);
			}

			return null;
		}
	}
}
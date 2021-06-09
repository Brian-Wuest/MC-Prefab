package com.wuest.prefab.Gui;

import com.wuest.prefab.Blocks.FullDyeColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * This class contains the keys for the language files.
 *
 * @author WuestMan
 */
public class GuiLangKeys {
	@Unlocalized(name = "Note: If you're facing north, choose south so the pier is going away from you. The white border is just to show the size.")
	public static final String FISH_POND_STRUCTURE_FACING = "prefab.gui.fishpond.structure.facing";

	@Unlocalized(name = "This structure is 32x32 blocks in size.")
	public static final String PRODUCE_FARM_SIZE = "prefab.gui.producefarm.structure.size";

	@Unlocalized(name = "This structure is 38x38 blocks in size.")
	public static final String TREE_FARM_SIZE = "prefab.gui.treefarm.structure.size";

	@Unlocalized(name = "General")
	public static final String STARTER_TAB_GENERAL = "prefab.gui.starter.tab.general";

	@Unlocalized(name = "Config")
	public static final String STARTER_TAB_CONFIG = "prefab.gui.starter.tab.config";

	@Unlocalized(name = "Blocks/Size")
	public static final String STARTER_TAB_BLOCK = "prefab.gui.starter.tab.block";

	@Unlocalized(name = "House Style")
	public static final String STARTER_HOUSE_STYLE = "prefab.gui.starter.style.label";

	@Unlocalized(name = "Floor Block Type")
	public static final String STARTER_HOUSE_FLOOR_LABEL = "prefab.gui.starter.floortype.label";

	@Unlocalized(name = "Ceiling Block Type")
	public static final String STARTER_HOUSE_CEILING_LABEL = "prefab.gui.starter.ceilingtype.label";

	@Unlocalized(name = "Wall Wood Type")
	public static final String STARTER_HOUSE_WALL_LABEL = "prefab.gui.starter.walltype.label";

	@Unlocalized(name = "Interior Depth")
	public static final String STARTER_HOUSE_DEPTH_LABEL = "prefab.gui.starter.depth.label";

	@Unlocalized(name = "Interior Width")
	public static final String STARTER_HOUSE_WIDTH_LABEL = "prefab.gui.starter.width.label";

	@Unlocalized(name = "Basic House")
	public static final String STARTER_HOUSE_BASIC_DISPLAY = "prefab.gui.starter.basic.display";

	@Unlocalized(name = "A basic stone and brick house.")
	public static final String STARTER_HOUSE_BASIC_NOTES = "prefab.gui.starter.basic.notes";

	@Unlocalized(name = "Ranch Style")
	public static final String STARTER_HOUSE_RANCH_DISPLAY = "prefab.gui.starter.ranch.display";

	@Unlocalized(name = "A house designed in a ranch style.")
	public static final String STARTER_HOUSE_RANCH_NOTES = "prefab.gui.starter.ranch.notes";

	@Unlocalized(name = "Loft Style")
	public static final String STARTER_HOUSE_LOFT_DISPLAY = "prefab.gui.starter.loft.display";

	@Unlocalized(name = "A house designed with a lofted area for the chests.")
	public static final String STARTER_HOUSE_LOFT_NOTES = "prefab.gui.starter.loft.notes";

	@Unlocalized(name = "Hobbit Style")
	public static final String STARTER_HOUSE_HOBBIT_DISPLAY = "prefab.gui.starter.hobbit.display";

	@Unlocalized(name = "A house designed into the ground like a hobbit.")
	public static final String STARTER_HOUSE_HOBBIT_NOTES = "prefab.gui.starter.hobbit.notes";

	@Unlocalized(name = "Desert Style")
	public static final String STARTER_HOUSE_DESERT_DISPLAY = "prefab.gui.starter.desert.display";

	@Unlocalized(name = "Desert Style 2")
	public static final String STARTER_HOUSE_DESERT_DISPLAY2 = "prefab.gui.starter.desert.display2";

	@Unlocalized(name = "A house designed like a pyramid.")
	public static final String STARTER_HOUSE_DESERT_NOTES = "prefab.gui.starter.desert.notes";

	@Unlocalized(name = "A house designed for desert living.")
	public static final String STARTER_HOUSE_DESERT_NOTES2 = "prefab.gui.starter.desert.notes2";

	@Unlocalized(name = "Snowy Style")
	public static final String STARTER_HOUSE_SNOWY_DISPLAY = "prefab.gui.starter.snowy.display";

	@Unlocalized(name = "A house designed like an igloo.")
	public static final String STARTER_HOUSE_SNOWY_NOTES = "prefab.gui.starter.snowy.notes";

	@Unlocalized(name = "Subaquatic Style")
	public static final String STARTER_HOUSE_SUBAQUATIC_DISPLAY = "prefab.gui.starter.subaquatic.display";

	@Unlocalized(name = "A house designed for under water life.")
	public static final String STARTER_HOUSE_SUBAQUATIC_NOTES = "prefab.gui.starter.subaquatic.notes";

	@Unlocalized(name = "Add Torches")
	public static final String STARTER_HOUSE_ADD_TORCHES = "prefab.gui.starter.addtorches";

	@Unlocalized(name = "Add Bed")
	public static final String STARTER_HOUSE_ADD_BED = "prefab.gui.starter.addbed";

	@Unlocalized(name = "Add Crafting Table")
	public static final String STARTER_HOUSE_ADD_CRAFTING_TABLE = "prefab.gui.starter.addcraftingtable";

	@Unlocalized(name = "Add Furnace")
	public static final String STARTER_HOUSE_ADD_FURNACE = "prefab.gui.starter.addfurnace";

	@Unlocalized(name = "Add Chest")
	public static final String STARTER_HOUSE_ADD_CHEST = "prefab.gui.starter.addchest";

	@Unlocalized(name = "Add Chest Contents")
	public static final String STARTER_HOUSE_ADD_CHEST_CONTENTS = "prefab.gui.starter.addchestcontents";

	@Unlocalized(name = "Add Farm")
	public static final String STARTER_HOUSE_ADD_FARM = "prefab.gui.starter.addfarm";

	@Unlocalized(name = "Floor Stone Type")
	public static final String STARTER_HOUSE_FLOOR_STONE = "prefab.gui.starter.floorstone";

	@Unlocalized(name = "Ceiling Stone Type")
	public static final String STARTER_HOUSE_CEILING_TYPE = "prefab.gui.starter.ceilingtype";

	@Unlocalized(name = "Wall Wood Type")
	public static final String STARTER_HOUSE_WALL_TYPE = "prefab.gui.starter.walltype";

	@Unlocalized(name = "Is Ceiling Flat")
	public static final String STARTER_HOUSE_CEILING_FLAT = "prefab.gui.starter.ceilingflat";

	@Unlocalized(name = "Build Mineshaft")
	public static final String STARTER_HOUSE_BUILD_MINESHAFT = "prefab.gui.starter.buildmineshaft";

	@Unlocalized(name = "Stonebrick")
	public static final String CEILING_BLOCK_TYPE_STONE = "prefab.ceiling.block.type.stone";

	@Unlocalized(name = "Brick")
	public static final String CEILING_BLOCK_TYPE_BRICK = "prefab.ceiling.block.type.brick";

	@Unlocalized(name = "Sandstone")
	public static final String CEILING_BLOCK_TYPE_SAND = "prefab.ceiling.block.type.sand";

	@Unlocalized(name = "Oak")
	public static final String WALL_BLOCK_TYPE_OAK = "prefab.wall.block.type.oak";

	@Unlocalized(name = "Spruce")
	public static final String WALL_BLOCK_TYPE_SPRUCE = "prefab.wall.block.type.spruce";

	@Unlocalized(name = "Birch")
	public static final String WALL_BLOCK_TYPE_BIRCH = "prefab.wall.block.type.birch";

	@Unlocalized(name = "Jungle")
	public static final String WALL_BLOCK_TYPE_JUNGLE = "prefab.wall.block.type.jungle";

	@Unlocalized(name = "Acacia")
	public static final String WALL_BLOCK_TYPE_ACACIA = "prefab.wall.block.type.acacia";

	@Unlocalized(name = "Dark Oak")
	public static final String WALL_BLOCK_TYPE_DARK_OAK = "prefab.wall.block.type.darkoak";

	@Unlocalized(name = "Facing")
	public static final String GUI_STRUCTURE_FACING = "prefab.gui.structure.facing";

	@Unlocalized(name = "Glass Color")
	public static final String GUI_STRUCTURE_GLASS = "prefab.gui.structure.glass";

	@Unlocalized(name = "Bed Color")
	public static final String GUI_STRUCTURE_BED_COLOR = "prefab.gui.structure.bed_color";

	@Unlocalized(name = "The red box in the image on the right shows the block you clicked on.")
	public static final String GUI_BLOCK_CLICKED = "prefab.gui.structure.block.clicked";

	@Unlocalized(name = "Note: If you're facing north, choose south so the doors are facing you.")
	public static final String GUI_DOOR_FACING = "prefab.gui.structure.door.facing";

	@Unlocalized(name = "Note: If you're facing north, choose south so the structure is facing you.")
	public static final String GUI_STRUCTURE_FACING_PLAYER = "prefab.gui.structure.facing.player";

	@Unlocalized(name = "Cannot build structure due to protected blocks/area or unbreakable blocks are in the area. Block Name: %1$s Block Position: x=%2$s, y=%3$s, z=%4$s")
	public static final String GUI_STRUCTURE_NOBUILD = "prefab.gui.structure.nobuild";

	@Unlocalized(name = "Build!")
	public static final String GUI_BUTTON_BUILD = "prefab.gui.button.build";

	@Unlocalized(name = "Cancel")
	public static final String GUI_BUTTON_CANCEL = "prefab.gui.button.cancel";

	@Unlocalized(name = "Preview!")
	public static final String GUI_BUTTON_PREVIEW = "prefab.gui.button.preview";

	@Unlocalized(name = "Structure Complete!")
	public static final String GUI_PREVIEW_COMPLETE = "prefab.gui.preview.complete";

	@Unlocalized(name = "Right-click on any block in the world to remove the preview.")
	public static final String GUI_PREVIEW_NOTICE = "prefab.gui.preview.notice";

	@Unlocalized(name = "north")
	public static final String GUI_NORTH = "prefab.gui.north";

	@Unlocalized(name = "south")
	public static final String GUI_SOUTH = "prefab.gui.south";

	@Unlocalized(name = "east")
	public static final String GUI_EAST = "prefab.gui.east";

	@Unlocalized(name = "west")
	public static final String GUI_WEST = "prefab.gui.west";

	@Unlocalized(name = "white")
	public static final String GUI_WHITE = "prefab.gui.white";

	@Unlocalized(name = "orange")
	public static final String GUI_ORANGE = "prefab.gui.orange";

	@Unlocalized(name = "magenta")
	public static final String GUI_MAGENTA = "prefab.gui.magenta";

	@Unlocalized(name = "light_blue")
	public static final String GUI_LIGHT_BLUE = "prefab.gui.light_blue";

	@Unlocalized(name = "light_gray")
	public static final String GUI_LIGHT_GRAY = "prefab.gui.light_gray";

	@Unlocalized(name = "yellow")
	public static final String GUI_YELLOW = "prefab.gui.yellow";

	@Unlocalized(name = "lime")
	public static final String GUI_LIME = "prefab.gui.lime";

	@Unlocalized(name = "pink")
	public static final String GUI_PINK = "prefab.gui.pink";

	@Unlocalized(name = "gray")
	public static final String GUI_GRAY = "prefab.gui.gray";

	@Unlocalized(name = "silver")
	public static final String GUI_SILVER = "prefab.gui.silver";

	@Unlocalized(name = "cyan")
	public static final String GUI_CYAN = "prefab.gui.cyan";

	@Unlocalized(name = "purple")
	public static final String GUI_PURPLE = "prefab.gui.purple";

	@Unlocalized(name = "blue")
	public static final String GUI_BLUE = "prefab.gui.blue";

	@Unlocalized(name = "brown")
	public static final String GUI_BROWN = "prefab.gui.brown";

	@Unlocalized(name = "green")
	public static final String GUI_GREEN = "prefab.gui.green";

	@Unlocalized(name = "red")
	public static final String GUI_RED = "prefab.gui.red";

	@Unlocalized(name = "black")
	public static final String GUI_BLACK = "prefab.gui.black";

	@Unlocalized(name = "clear")
	public static final String GUI_CLEAR = "prefab.gui.clear";

	@Unlocalized(name = "Flat Roof")
	public static final String VILLAGER_HOUSE_FLAT_ROOF = "prefab.gui.villager.house.flat";

	@Unlocalized(name = "Angled Roof")
	public static final String VILLAGER_HOUSE_ANGLED_ROOF = "prefab.gui.villager.house.angled";

	@Unlocalized(name = "Fenced Roof")
	public static final String VILLAGER_HOUSE_FENCED_ROOF = "prefab.gui.villager.house.fenced";

	@Unlocalized(name = "Blacksmith")
	public static final String VILLAGER_HOUSE_BLACKSMITH = "prefab.gui.villager.blacksmith";

	@Unlocalized(name = "Long House")
	public static final String VILLAGER_HOUSE_LONGHOUSE = "prefab.gui.villager.long_house";

	@Unlocalized(name = "Spruce House")
	public static final String MODERATE_HOUSE_SPRUCE = "prefab.gui.moderate_house.spruce";

	@Unlocalized(name = "Acacia House")
	public static final String MODERATE_HOUSE_ACACIA = "prefab.gui.moderate_house.acacia";

	@Unlocalized(name = "Earthen Home")
	public static final String MODERATE_EARTHEN_HOME = "prefab.gui.moderate_house.earthen";

	@Unlocalized(name = "Jungle Home")
	public static final String MODERATE_JUNGLE_HOME = "prefab.gui.moderate_house.jungle";

	@Unlocalized(name = "Workshop")
	public static final String MODERATE_WORKSHOP_HOME = "prefab.gui.moderate_house.workshop";

	@Unlocalized(name = "Nether House")
	public static final String MODERATE_NETHER_HOME = "prefab.gui.moderate_house.nether";

	@Unlocalized(name = "Mountain House")
	public static final String MODERATE_MOUNTAIN_HOME = "prefab.gui.moderate_house.mountain";

	@Unlocalized(name = "Press§9 Shift §7for more information.")
	public static final String SHIFT_TOOLTIP = "prefab.gui.tooltip.shift";

	@Unlocalized(name = "Initially invisible, becomes visible when receiving a redstone signal.")
	public static final String BOUNDARY_TOOLTIP = "prefab.gui.tooltip.boundary_block";

	@Unlocalized(name = "This item will clear out a 16x16x16 space and provide the drops from each block.")
	public static final String GUI_BULLDOZER_DESCRIPTION = "prefab.gui.bulldozer_desc";

	@Unlocalized(name = "The area cleared will be in the direction you are facing.")
	public static final String GUI_CLEARED_AREA = "prefab.gui.cleared_area";

	@Unlocalized(name = "Needs to be imbued with the power of the earth and magic. Try combining with tripple compressed stone in an anvil.")
	public static final String BULLDOZER_UNPOWERED_TOOLTIP = "prefab.gui.tooltip.bulldozer_unpowered";

	@Unlocalized(name = "Powered with the strength of the earth and magic, you believe a large area can be cleared.")
	public static final String BULLDOZER_POWERED_TOOLTIP = "prefab.gui.tooltip.bulldozer_powered";

	@Unlocalized(name = "Bridge Length")
	public static final String BRIDGE_LENGTH = "prefab.gui.bridge_length";

	@Unlocalized(name = "Bridge Material")
	public static final String BRIDGE_MATERIAL = "prefab.gui.bridge_material";

	@Unlocalized(name = "Include Roof")
	public static final String INCLUDE_ROOF = "prefab.gui.bridge_include_roof";

	@Unlocalized(name = "Interior Height")
	public static final String INTERIOR_HEIGHT = "prefab.gui.bridge_interior_height";

	@Unlocalized(name = "Wall")
	public static final String WALL = "prefab.gui.part_style.wall";

	@Unlocalized(name = "Gate")
	public static final String GATE = "prefab.gui.part_style.gate";

	@Unlocalized(name = "Frame")
	public static final String FRAME = "prefab.gui.part_style.frame";

	@Unlocalized(name = "Stairs")
	public static final String STAIRS = "prefab.gui.part_style.stairs";

	@Unlocalized(name = "Circle")
	public static final String CIRCLE = "prefab.gui.part_style.circle";

	@Unlocalized(name = "Style")
	public static final String STYLE = "prefab.gui.style";

	@Unlocalized(name = "Material")
	public static final String MATERIAL = "prefab.gui.material";

	@Unlocalized(name = "Height")
	public static final String HEIGHT = "prefab.gui.height";

	@Unlocalized(name = "Width")
	public static final String WIDTH = "prefab.gui.width";

	@Unlocalized(name = "Length")
	public static final String LENGTH = "prefab.gui.length";

	@Unlocalized(name = "Stairs Height")
	public static final String STAIRS_HEIGHT = "prefab.gui.stairs_height";

	@Unlocalized(name = "Stairs Width")
	public static final String STAIRS_WIDTH = "prefab.gui.stairs_width";

	@Unlocalized(name = "Doorway")
	public static final String DOOR_WAY = "prefab.gui.part_style.door_way";

	@Unlocalized(name = "Roof")
	public static final String ROOF = "prefab.gui.part_style.roof";

	@Unlocalized(name = "Floor")
	public static final String FLOOR = "prefab.gui.part_style.floor";

	@Unlocalized(name = "Building Options")
	public static final String BUILDING_OPTIONS = "prefab.gui.building_options";

	/**
	 * Translates the specified language key for the current language.
	 *
	 * @param translateKey The language key to use.
	 * @return The translated language key.
	 */
	public static String translateString(String translateKey) {
		if (I18n.exists(translateKey)) {
			return I18n.get(translateKey);
		} else {
			return GuiLangKeys.getUnLocalized(translateKey);
		}
	}

	/**
	 * Gets the unlocalized version of this translation key. If the translation key does not exist the key is returned
	 * instead of the unlocalized value.
	 *
	 * @param translateKey The translation key to get the unlocalized value for.
	 * @return The unlocalized value or the passed in key.
	 */
	public static String getUnLocalized(String translateKey) {
		for (Field field : GuiLangKeys.class.getDeclaredFields()) {
			String value = "";

			try {
				value = field.get(null).toString();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

			if (translateKey.equals(value)) {
				Annotation[] annotations = field.getDeclaredAnnotations();

				for (Annotation annotation : annotations) {
					if (annotation instanceof Unlocalized) {

						return ((Unlocalized) annotation).name();
					}
				}
			}
		}

		return translateKey;
	}

	public static String translateFacing(Direction facing) {
		return GuiLangKeys.translateString("prefab.gui." + facing.getName());
	}

	public static String translateDye(DyeColor dyeColor) {
		return GuiLangKeys.translateString("prefab.gui." + dyeColor.getName());
	}

	public static String translateFullDye(FullDyeColor dyeColor) {
		return GuiLangKeys.translateString("prefab.gui." + dyeColor.getName());
	}

	/**
	 * An annotation which allows the UI to get the unlocalized name;
	 *
	 * @author WuestMan
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(java.lang.annotation.ElementType.FIELD)
	public @interface Unlocalized {
		String name() default "";
	}
}

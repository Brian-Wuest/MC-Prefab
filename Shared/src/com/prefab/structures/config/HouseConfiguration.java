package com.prefab.structures.config;

import com.prefab.ModRegistryBase;
import com.prefab.PrefabBase;
import com.prefab.blocks.FullDyeColor;
import com.prefab.network.ServerToClientTypes;
import com.prefab.config.EntityPlayerConfiguration;
import com.prefab.gui.GuiLangKeys;
import com.prefab.network.message.TagMessage;
import com.prefab.structures.base.BuildBlock;
import com.prefab.structures.predefined.StructureHouse;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;


/**
 * This class is used to determine the configuration for a particular house.
 *
 * @author WuestMan
 */
public class HouseConfiguration extends StructureConfiguration {
    private static final String addTorchesTag = "addTorches";
    private static final String addBedTag = "addBed";
    private static final String addCraftingTableTag = "addCraftingTable";
    private static final String addFurnaceTag = "addFurnace";
    private static final String addChestTag = "addChest";
    private static final String addChestContentsTag = "addChestContents";
    private static final String addMineShaftTag = "addMineShaft";
    private static final String hitXTag = "hitX";
    private static final String hitYTag = "hitY";
    private static final String hitZTag = "hitZ";
    private static final String houseFacingTag = "houseFacing";
    private static final String houseStyleTag = "houseStyle";
    private static final String glassColorTag = "glassColor";
    private static final String bedColorTag = "bedColor";

    public boolean addTorches;
    public boolean addBed;
    public boolean addCraftingTable;
    public boolean addFurnace;
    public boolean addChest;
    public boolean addChestContents;
    public boolean addMineShaft;
    public HouseStyle houseStyle;
    public FullDyeColor glassColor;
    public DyeColor bedColor;

    /**
     * Initializes a new instance of the {@link HouseConfiguration} class.
     */
    public HouseConfiguration() {
        super();
    }

    @Override
    public void Initialize() {
        super.Initialize();
        this.houseStyle = HouseStyle.BASIC;
        this.glassColor = FullDyeColor.LIGHT_GRAY;
        this.bedColor = DyeColor.RED;
        this.addTorches = true;
        this.addBed = true;
        this.addCraftingTable = true;
        this.addFurnace = true;
        this.addChest = true;
        this.addChestContents = true;
        this.addMineShaft = true;
    }

    @Override
    public CompoundTag WriteToCompoundTag() {
        CompoundTag tag = new CompoundTag();

        // This tag should only be written for options which will NOT be overwritten by server options.
        // Server configuration settings will be used for all other options.
        // This is so the server admin can force a player to not use something.
        tag.putBoolean(HouseConfiguration.addTorchesTag, this.addTorches);
        tag.putBoolean(HouseConfiguration.addBedTag, this.addBed);
        tag.putBoolean(HouseConfiguration.addCraftingTableTag, this.addCraftingTable);
        tag.putBoolean(HouseConfiguration.addFurnaceTag, this.addFurnace);
        tag.putBoolean(HouseConfiguration.addChestTag, this.addChest);
        tag.putBoolean(HouseConfiguration.addChestContentsTag, this.addChestContents);
        tag.putBoolean(HouseConfiguration.addMineShaftTag, this.addMineShaft);
        tag.putInt(HouseConfiguration.hitXTag, this.pos.getX());
        tag.putInt(HouseConfiguration.hitYTag, this.pos.getY());
        tag.putInt(HouseConfiguration.hitZTag, this.pos.getZ());
        tag.putString(HouseConfiguration.houseFacingTag, this.houseFacing.getSerializedName());
        tag.putInt(HouseConfiguration.houseStyleTag, this.houseStyle.value);
        tag.putInt(HouseConfiguration.glassColorTag, this.glassColor.getId());
        tag.putInt(HouseConfiguration.bedColorTag, this.bedColor.getId());

        return tag;
    }

    /**
     * Custom method to read the CompoundNBT message.
     *
     * @param tag The message to create the configuration from.
     * @return An new configuration object with the values derived from the CompoundNBT.
     */
    @Override
    public HouseConfiguration ReadFromCompoundTag(CompoundTag tag) {
        HouseConfiguration config = null;

        if (tag != null) {
            config = new HouseConfiguration();

            if (tag.contains(HouseConfiguration.addTorchesTag)) {
                config.addTorches = tag
                        .getBoolean(HouseConfiguration.addTorchesTag).orElse(true);
            }

            if (tag.contains(HouseConfiguration.addBedTag)) {
                config.addBed = tag
                        .getBoolean(HouseConfiguration.addBedTag).orElse(true);
            }

            if (tag.contains(HouseConfiguration.addCraftingTableTag)) {
                config.addCraftingTable = tag
                        .getBoolean(HouseConfiguration.addCraftingTableTag).orElse(true);
            }

            if (tag.contains(HouseConfiguration.addFurnaceTag)) {
                config.addFurnace = tag
                        .getBoolean(HouseConfiguration.addFurnaceTag).orElse(true);
            }

            if (tag.contains(HouseConfiguration.addChestTag)) {
                config.addChest = tag
                        .getBoolean(HouseConfiguration.addChestTag).orElse(true);
            }

            if (tag.contains(HouseConfiguration.addChestContentsTag)) {
                config.addChestContents = tag
                        .getBoolean(HouseConfiguration.addChestContentsTag).orElse(true);
            }

            if (tag.contains(HouseConfiguration.addMineShaftTag)) {
                config.addMineShaft = tag
                        .getBoolean(HouseConfiguration.addMineShaftTag).orElse(true);
            }

            if (tag.contains(HouseConfiguration.hitXTag)) {
                config.pos = new BlockPos(
                        tag.getInt(HouseConfiguration.hitXTag).orElse(0),
                        tag.getInt(HouseConfiguration.hitYTag).orElse(0),
                        tag.getInt(HouseConfiguration.hitZTag).orElse(0));
            }

            if (tag.contains(HouseConfiguration.houseFacingTag)) {
                config.houseFacing = BuildBlock.getDirectionByName(
                        tag.getString(HouseConfiguration.houseFacingTag).orElse("south"));
            }

            if (tag.contains(HouseConfiguration.houseStyleTag)) {
                config.houseStyle = HouseStyle.ValueOf(
                        tag.getInt(HouseConfiguration.houseStyleTag).orElse(0));
            }

            if (tag.contains(HouseConfiguration.glassColorTag)) {
                config.glassColor = FullDyeColor.byId(tag
                        .getInt(HouseConfiguration.glassColorTag).orElse(0));
            }

            if (tag.contains(HouseConfiguration.bedColorTag)) {
                config.bedColor = DyeColor.byId(tag
                        .getInt(HouseConfiguration.bedColorTag).orElse(0));
            }
        }

        return config;
    }

    /**
     * This is used to actually build the structure as it creates the structure instance and calls build structure.
     *
     * @param player      The player which requested the build.
     * @param world       The world instance where the build will occur.
     * @param hitBlockPos This hit block position.
     */
    @Override
    protected void ConfigurationSpecificBuildStructure(ServerPlayer player, ServerLevel world, BlockPos hitBlockPos) {
        // Build the alternate starter house instead.
        StructureHouse structure = StructureHouse.CreateInstance(this.houseStyle.getStructureLocation(), StructureHouse.class);
        boolean houseBuilt = structure.BuildStructure(this, world, hitBlockPos, player);

        // The house was successfully built, remove the item from the inventory.
        if (houseBuilt) {
            EntityPlayerConfiguration playerConfig = EntityPlayerConfiguration.loadFromEntity(player);
            playerConfig.builtStarterHouse = true;

            this.RemoveStructureItemFromPlayer(player, ModRegistryBase.House);

            // Make sure to send a message to the client to sync up the server player information and the client player
            // information.
            TagMessage tagMessage = new TagMessage();
            tagMessage.setMessageTag(playerConfig.createPlayerTag());

            PrefabBase.networkWrapper.sendToClient(ServerToClientTypes.PLAYER_CONFIG_SYNC, (ServerPlayer) player, tagMessage);
        }
    }

    /**
     * This enum is used to contain the different type of starting houses available to the player.
     *
     * @author WuestMan
     */
    public enum HouseStyle {
        BASIC(
                0,
                GuiLangKeys.HOUSE_BASIC_DISPLAY,
                ResourceLocation.tryBuild("prefab", "textures/gui/house_basic.png"),
                "assets/prefab/structures/house_basic.gz"),
        RANCH(1, GuiLangKeys.HOUSE_RANCH_DISPLAY, ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_ranch.png"),
                "assets/prefab/structures/house_ranch.gz"),
        LOFT(2, GuiLangKeys.HOUSE_LOFT_DISPLAY, ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_loft.png"),
                "assets/prefab/structures/house_loft.gz"),
        HOBBIT(3, GuiLangKeys.HOUSE_HOBBIT_DISPLAY, ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_hobbit.png"),
                "assets/prefab/structures/house_hobbit.gz"),
        DESERT(4, GuiLangKeys.HOUSE_DESERT_DISPLAY, ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_desert.png"),
                "assets/prefab/structures/house_desert.gz"),
        SNOWY(5, GuiLangKeys.HOUSE_SNOWY_DISPLAY, ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_snowy.png"),
                "assets/prefab/structures/house_snow.gz"),
        DESERT2(6,
                GuiLangKeys.HOUSE_DESERT_DISPLAY2,
                ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_desert_2.png"),
                "assets/prefab/structures/house_desert_2.gz"),
        SUBAQUATIC(7,
                GuiLangKeys.HOUSE_SUBAQUATIC_DISPLAY,
                ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_subaquatic.png"),
                "assets/prefab/structures/house_sub_aqua.gz"),
        MODERN(8,
                GuiLangKeys.HOUSE_MODERN_DISPLAY,
                ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_modern.png"),
                "assets/prefab/structures/house_modern.gz"),
        CAMPSITE(9,
                GuiLangKeys.HOUSE_CAMPING_DISPLAY,
                ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_campsite.png"),
                "assets/prefab/structures/house_campsite.gz"),
        IZBA(10,
                GuiLangKeys.HOUSE_IZBA_DISPLAY,
                ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_izba.png"),
                "assets/prefab/structures/house_izba.gz"),
        TOWER(11,
                GuiLangKeys.HOUSE_TOWER_DISPLAY,
                ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_tower.png"),
                "assets/prefab/structures/house_tower.gz"),
        CABIN(12,
                GuiLangKeys.HOUSE_CABIN_DISPLAY,
                ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_cabin.png"),
                "assets/prefab/structures/house_cabin.gz"),
        TREE(13,
                GuiLangKeys.HOUSE_TREE_HOUSE_DISPLAY,
                ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_tree.png"),
                "assets/prefab/structures/house_tree.gz"),
        MUSHROOM(14,
                GuiLangKeys.HOUSE_MUSHROOM_HOUSE_DISPLAY,
                ResourceLocation.tryBuild(PrefabBase.MODID, "textures/gui/house_mushroom.png"),
                "assets/prefab/structures/house_mushroom.gz");

        private final int value;
        private final String displayName;
        private final ResourceLocation housePicture;
        private final String structureLocation;

        HouseStyle(int newValue, String displayName, ResourceLocation housePicture, String structureLocation) {
            this.value = newValue;
            this.displayName = displayName;
            this.housePicture = housePicture;
            this.structureLocation = structureLocation;
        }

        /**
         * Returns a house style based off of an integer value.
         *
         * @param value The integer value representing the house style.
         * @return The house style found or HouseStyle.Basic if none found.
         */
        public static HouseStyle ValueOf(int value) {
            HouseStyle returnValue = HouseStyle.BASIC;

            for (HouseStyle current : HouseStyle.values()) {
                if (current.value == value) {
                    returnValue = current;
                    break;
                }
            }

            return returnValue;
        }

        /**
         * Gets a unique identifier for this style.
         *
         * @return An integer representing the ID of this style.
         */
        public int getValue() {
            return value;
        }

        /**
         * Gets the display name for this style.
         *
         * @return A string representing the name of this style.
         */
        public String getDisplayName() {
            return GuiLangKeys.translateString(this.displayName);
        }

        public String getTranslationString() {
            return this.displayName;
        }

        /**
         * Gets the picture used in the GUI for this style.
         *
         * @return A resource location representing the image to use for this style.
         */
        public ResourceLocation getHousePicture() {
            return this.housePicture;
        }

        /**
         * Gets a string for the resource location of this style.
         *
         * @return A string representing the location of the structure asset in the mod.
         */
        public String getStructureLocation() {
            return this.structureLocation;
        }
    }
}

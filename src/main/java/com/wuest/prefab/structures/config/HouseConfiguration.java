package com.wuest.prefab.structures.config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.config.EntityPlayerConfiguration;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.proxy.messages.PlayerEntityTagMessage;
import com.wuest.prefab.structures.predefined.StructureAlternateStart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

/**
 * This class is used to determine the configuration for a particular house.
 *
 * @author WuestMan
 */
public class HouseConfiguration extends StructureConfiguration {
    private static String addTorchesTag = "addTorches";
    private static String addBedTag = "addBed";
    private static String addCraftingTableTag = "addCraftingTable";
    private static String addFurnaceTag = "addFurnace";
    private static String addChestTag = "addChest";
    private static String addChestContentsTag = "addChestContents";
    private static String addMineShaftTag = "addMineShaft";
    private static String hitXTag = "hitX";
    private static String hitYTag = "hitY";
    private static String hitZTag = "hitZ";
    private static String houseFacingTag = "houseFacing";
    private static String houseStyleTag = "houseStyle";
    private static String glassColorTag = "glassColor";
    private static String bedColorTag = "bedColor";

    public boolean addTorches;
    public boolean addBed;
    public boolean addCraftingTable;
    public boolean addFurnace;
    public boolean addChest;
    public boolean addChestContents;
    public boolean addMineShaft;
    public HouseStyle houseStyle;
    public FullDyeColor glassColor;
    public EnumDyeColor bedColor;

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
        this.bedColor =EnumDyeColor.RED;
        this.addTorches = true;
        this.addBed = true;
        this.addCraftingTable = true;
        this.addFurnace = true;
        this.addChest = true;
        this.addChestContents = true;
        this.addMineShaft = true;
    }

    @Override
    public NBTTagCompound WriteToNBTTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();

        // This tag should only be written for options which will NOT be overwritten by server options.
        // Server configuration settings will be used for all other options.
        // This is so the server admin can force a player to not use something.
        tag.setBoolean(HouseConfiguration.addTorchesTag, this.addTorches);
        tag.setBoolean(HouseConfiguration.addBedTag, this.addBed);
        tag.setBoolean(HouseConfiguration.addCraftingTableTag, this.addCraftingTable);
        tag.setBoolean(HouseConfiguration.addFurnaceTag, this.addFurnace);
        tag.setBoolean(HouseConfiguration.addChestTag, this.addChest);
        tag.setBoolean(HouseConfiguration.addChestContentsTag, this.addChestContents);
        tag.setBoolean(HouseConfiguration.addMineShaftTag, this.addMineShaft);
        tag.setInteger(HouseConfiguration.hitXTag, this.pos.getX());
        tag.setInteger(HouseConfiguration.hitYTag, this.pos.getY());
        tag.setInteger(HouseConfiguration.hitZTag, this.pos.getZ());
        tag.setString(HouseConfiguration.houseFacingTag, this.houseFacing.getName());
        tag.setInteger(HouseConfiguration.houseStyleTag, this.houseStyle.value);
        tag.setString(HouseConfiguration.glassColorTag, this.glassColor.getSerializedName().toUpperCase());
        tag.setString(HouseConfiguration.bedColorTag, this.bedColor.getName().toUpperCase());

        return tag;
    }

    /**
     * Custom method to read the NBTTagCompound message.
     *
     * @param tag The message to create the configuration from.
     * @return An new configuration object with the values derived from the NBTTagCompound.
     */
    @Override
    public HouseConfiguration ReadFromCompoundNBT(NBTTagCompound tag) {
        HouseConfiguration config = null;

        if (tag != null) {
            config = new HouseConfiguration();

            if (tag.hasKey(HouseConfiguration.addTorchesTag)) {
                config.addTorches = tag.getBoolean(HouseConfiguration.addTorchesTag);
            }

            if (tag.hasKey(HouseConfiguration.addBedTag)) {
                config.addBed = tag.getBoolean(HouseConfiguration.addBedTag);
            }

            if (tag.hasKey(HouseConfiguration.addCraftingTableTag)) {
                config.addCraftingTable = tag.getBoolean(HouseConfiguration.addCraftingTableTag);
            }

            if (tag.hasKey(HouseConfiguration.addFurnaceTag)) {
                config.addFurnace = tag.getBoolean(HouseConfiguration.addFurnaceTag);
            }

            if (tag.hasKey(HouseConfiguration.addChestTag)) {
                config.addChest = tag.getBoolean(HouseConfiguration.addChestTag);
            }

            if (tag.hasKey(HouseConfiguration.addChestContentsTag)) {
                config.addChestContents = tag.getBoolean(HouseConfiguration.addChestContentsTag);
            }

            if (tag.hasKey(HouseConfiguration.addMineShaftTag)) {
                config.addMineShaft = tag.getBoolean(HouseConfiguration.addMineShaftTag);
            }

            if (tag.hasKey(HouseConfiguration.hitXTag)) {
                config.pos = new BlockPos(tag.getInteger(HouseConfiguration.hitXTag), tag.getInteger(HouseConfiguration.hitYTag), tag.getInteger(HouseConfiguration.hitZTag));
            }

            if (tag.hasKey(HouseConfiguration.houseFacingTag)) {
                config.houseFacing = EnumFacing.byName(tag.getString(HouseConfiguration.houseFacingTag));
            }

            if (tag.hasKey(HouseConfiguration.houseStyleTag)) {
                config.houseStyle = HouseStyle.ValueOf(tag.getInteger(HouseConfiguration.houseStyleTag));
            }

            if (tag.hasKey(HouseConfiguration.glassColorTag)) {
                config.glassColor = FullDyeColor.valueOf(tag.getString(HouseConfiguration.glassColorTag));
            }

            if (tag.hasKey(HouseConfiguration.bedColorTag)) {
                config.bedColor =EnumDyeColor.valueOf(tag.getString(HouseConfiguration.bedColorTag));
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
    protected void ConfigurationSpecificBuildStructure(EntityPlayer player, WorldServer world, BlockPos hitBlockPos) {
        boolean houseBuilt = true;

        // Build the alternate starter house instead.
        StructureAlternateStart structure = StructureAlternateStart.CreateInstance(this.houseStyle.getStructureLocation(), StructureAlternateStart.class);
        houseBuilt = structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player);

        // The house was successfully built, remove the item from the inventory.
        if (houseBuilt) {
            EntityPlayerConfiguration playerConfig = EntityPlayerConfiguration.loadFromEntityData(player);
            playerConfig.builtStarterHouse = true;
            playerConfig.saveToPlayer(player);

            this.RemoveStructureItemFromPlayer(player, ModRegistry.StartHouse);

            // Make sure to send a message to the client to sync up the server player information and the client player
            // information.
            Prefab.network.sendTo(new PlayerEntityTagMessage(playerConfig.getModIsPlayerNewTag(player)), (EntityPlayerMP) player);
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
                GuiLangKeys.STARTER_HOUSE_BASIC_DISPLAY,
                new ResourceLocation("prefab", "textures/gui/basic_house.png"),
                GuiLangKeys.STARTER_HOUSE_BASIC_NOTES,
                163,
                146,
                "assets/prefab/structures/basic_house.zip"),
        RANCH(1, GuiLangKeys.STARTER_HOUSE_RANCH_DISPLAY, new ResourceLocation("prefab", "textures/gui/ranch_house.png"), GuiLangKeys.STARTER_HOUSE_RANCH_NOTES, 152, 89,
                "assets/prefab/structures/ranch_house.zip"),
        LOFT(2, GuiLangKeys.STARTER_HOUSE_LOFT_DISPLAY, new ResourceLocation("prefab", "textures/gui/loft_house.png"), GuiLangKeys.STARTER_HOUSE_LOFT_NOTES, 152, 87,
                "assets/prefab/structures/loft_house.zip"),
        HOBBIT(3, GuiLangKeys.STARTER_HOUSE_HOBBIT_DISPLAY, new ResourceLocation("prefab", "textures/gui/hobbit_house.png"), GuiLangKeys.STARTER_HOUSE_HOBBIT_NOTES, 151, 133,
                "assets/prefab/structures/hobbit_house.zip"),
        DESERT(4, GuiLangKeys.STARTER_HOUSE_DESERT_DISPLAY, new ResourceLocation("prefab", "textures/gui/desert_house.png"), GuiLangKeys.STARTER_HOUSE_DESERT_NOTES, 152, 131,
                "assets/prefab/structures/desert_house.zip"),
        SNOWY(5, GuiLangKeys.STARTER_HOUSE_SNOWY_DISPLAY, new ResourceLocation("prefab", "textures/gui/snowy_house.png"), GuiLangKeys.STARTER_HOUSE_SNOWY_NOTES, 150, 125,
                "assets/prefab/structures/snowy_house.zip");

        private final int value;
        private final String displayName;
        private final ResourceLocation housePicture;
        private final String houseNotes;
        private final int imageWidth;
        private final int imageHeight;
        private final String structureLocation;

        HouseStyle(int newValue, String displayName, ResourceLocation housePicture, String houseNotes, int imageWidth, int imageHeight, String structureLocation) {
            this.value = newValue;
            this.displayName = displayName;
            this.housePicture = housePicture;
            this.houseNotes = houseNotes;
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
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

        /**
         * Gets the notes for this house style.
         *
         * @return A string representing the translated notes for this style.
         */
        public String getHouseNotes() {
            return GuiLangKeys.translateString(this.houseNotes);
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
         * Gets the width of the image to use with this style.
         *
         * @return An integer representing the image width.
         */
        public int getImageWidth() {
            return this.imageWidth;
        }

        /**
         * Gets the height of the image to use with this style.
         *
         * @return An integer representing the image height.
         */
        public int getImageHeight() {
            return this.imageHeight;
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

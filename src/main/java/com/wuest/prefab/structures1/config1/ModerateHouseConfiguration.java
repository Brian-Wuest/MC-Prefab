package com.wuest.prefab.structures.config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.structures.predefined.StructureModerateHouse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class is used for the moderate houses in the mod.
 *
 * @author WuestMan
 */
public class ModerateHouseConfiguration extends StructureConfiguration {
    public static String tagKey = "houseConfig";
    private static String houseStyleTag = "houseStyle";
    private static String addChestTag = "addChests";
    private static String addChestContentsTag = "addChestContents";
    private static String addMineshaftTag = "addMineshaft";

    /**
     * The house style.
     */
    public HouseStyle houseStyle;

    /**
     * Determines if the chests are included in the structure generation.
     */
    public boolean addChests;

    /**
     * Determines if the chest items are included in the structure generation.
     */
    public boolean addChestContents;

    /**
     * Determines if the mineshaft is generated in the structure generation.
     */
    public boolean addMineshaft;

    /**
     * Initliazes a new instance of the {@link ModerateHouseConfiguration} class.
     */
    public ModerateHouseConfiguration() {
        super();

        this.addChests = true;
        this.addChestContents = true;
        this.addMineshaft = true;
        this.houseStyle = HouseStyle.SPRUCE_HOME;
    }

    @Override
    public void Initialize() {
        super.Initialize();
        this.houseStyle = HouseStyle.SPRUCE_HOME;
    }

    @Override
    protected NBTTagCompound CustomWriteToNBTTagCompound(NBTTagCompound tag) {
        tag.setInteger(ModerateHouseConfiguration.houseStyleTag, this.houseStyle.value);
        tag.setBoolean(ModerateHouseConfiguration.addChestTag, this.addChests);
        tag.setBoolean(ModerateHouseConfiguration.addChestContentsTag, this.addChestContents);
        tag.setBoolean(ModerateHouseConfiguration.addMineshaftTag, this.addMineshaft);

        return tag;
    }

    @Override
    protected void CustomReadFromNBTTag(NBTTagCompound messageTag, StructureConfiguration config) {
        ModerateHouseConfiguration houseConfiguration = ((ModerateHouseConfiguration) config);

        if (messageTag.hasKey(ModerateHouseConfiguration.houseStyleTag)) {
            houseConfiguration.houseStyle = HouseStyle.ValueOf(messageTag.getInteger(ModerateHouseConfiguration.houseStyleTag));
        }

        if (messageTag.hasKey(ModerateHouseConfiguration.addChestTag)) {
            houseConfiguration.addChests = messageTag.getBoolean(ModerateHouseConfiguration.addChestTag);
        }

        if (messageTag.hasKey(ModerateHouseConfiguration.addChestContentsTag)) {
            houseConfiguration.addChestContents = messageTag.getBoolean(ModerateHouseConfiguration.addChestContentsTag);
        }

        if (messageTag.hasKey(ModerateHouseConfiguration.addMineshaftTag)) {
            houseConfiguration.addMineshaft = messageTag.getBoolean(ModerateHouseConfiguration.addMineshaftTag);
        }
    }

    /**
     * Custom method to read the NBTTagCompound message.
     *
     * @param messageTag The message to create the configuration from.
     * @return An new configuration object with the values derived from the NBTTagCompound.
     */
    @Override
    public ModerateHouseConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) {
        ModerateHouseConfiguration config = new ModerateHouseConfiguration();

        return (ModerateHouseConfiguration) super.ReadFromNBTTagCompound(messageTag, config);
    }

    /**
     * This is used to actually build the structure as it creates the structure instance and calls build structure.
     *
     * @param player      The player which requested the build.
     * @param world       The world instance where the build will occur.
     * @param hitBlockPos This hit block position.
     */
    @Override
    protected void ConfigurationSpecificBuildStructure(EntityPlayer player, World world, BlockPos hitBlockPos) {
        StructureModerateHouse structure = StructureModerateHouse.CreateInstance(this.houseStyle.getStructureLocation(), StructureModerateHouse.class);

        if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player)) {
            player.inventory.clearMatchingItems(ModRegistry.ModerateHouse, -1, 1, null);
            player.inventoryContainer.detectAndSendChanges();
        }
    }

    /**
     * This house style is used to determine what type of houses are available to the user.
     *
     * @author WuestMan
     */
    public enum HouseStyle {
        SPRUCE_HOME(0, GuiLangKeys.MODERATE_HOUSE_SPRUCE, new ResourceLocation("prefab", "textures/gui/moderate_house_spruce_topdown.png"), 176, 154,
                "assets/prefab/structures/moderate_house_spruce.zip", 31, 31, 23, 8, 1),
        ACACIA_HOME(1, GuiLangKeys.MODERATE_HOUSE_ACACIA, new ResourceLocation("prefab", "textures/gui/moderate_house_acacia_topdown.png"), 176, 154,
                "assets/prefab/structures/moderate_house_acacia.zip", 31, 31, 21, 12, 6),
        EARTHEN_HOME(2, GuiLangKeys.MODERATE_EARTHEN_HOME, new ResourceLocation("prefab", "textures/gui/moderate_house_earthen_topdown.png"), 174, 146,
                "assets/prefab/structures/moderate_house_earthen.zip", 16, 16, 16, 8, 6),
        JUNGLE_TREE_HOME(3, GuiLangKeys.MODERATE_JUNGLE_HOME, new ResourceLocation("prefab", "textures/gui/moderate_house_jungle_topdown.png"), 88, 164,
                "assets/prefab/structures/moderate_house_jungle.zip", 16, 16, 41, 8, 1);

        private final int value;
        private final String displayName;
        private final ResourceLocation housePicture;
        private final int imageWidth;
        private final int imageHeight;
        private final String structureLocation;
        private final int width;
        private final int length;
        private final int height;
        private final int eastOffSet;
        private final int downOffSet;

        HouseStyle(int newValue, String displayName, ResourceLocation housePicture, int imageWidth, int imageHeight, String structureLocation, int width, int length, int height, int eastOffSet, int downOffSet) {
            this.value = newValue;
            this.displayName = displayName;
            this.housePicture = housePicture;
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            this.structureLocation = structureLocation;
            this.width = width;
            this.length = length;
            this.height = height;
            this.eastOffSet = eastOffSet;
            this.downOffSet = downOffSet;
        }

        public static HouseStyle ValueOf(int value) {
            switch (value) {
                case 0: {
                    return HouseStyle.SPRUCE_HOME;
                }

                case 1: {
                    return HouseStyle.ACACIA_HOME;
                }
                case 2: {
                    return HouseStyle.EARTHEN_HOME;
                }
                case 3: {
                    return HouseStyle.JUNGLE_TREE_HOME;
                }
                default: {
                    return HouseStyle.SPRUCE_HOME;
                }
            }
        }

        public int getValue() {
            return value;
        }

        public String getDisplayName() {
            return GuiLangKeys.translateString(this.displayName);
        }

        public ResourceLocation getHousePicture() {
            return this.housePicture;
        }

        public int getImageWidth() {
            return this.imageWidth;
        }

        public int getImageHeight() {
            return this.imageHeight;
        }

        public int getWidth() {
            return this.width;
        }

        public int getLength() {
            return this.length;
        }

        public int getHeight() {
            return this.height;
        }

        public int getEastOffSet() {
            return this.eastOffSet;
        }

        public int getDownOffSet() {
            return this.downOffSet;
        }

        public String getStructureLocation() {
            return this.structureLocation;
        }
    }
}
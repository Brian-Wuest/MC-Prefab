package com.wuest.prefab.structures.config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.structures.predefined.StructureHouseAdvanced;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;

/**
 * This class is used for the moderate houses in the mod.
 *
 * @author WuestMan
 */
public class HouseAdvancedConfiguration extends StructureConfiguration {
    private static final String houseStyleTag = "houseStyle";
    private static final String addMineshaftTag = "addMineshaft";
    private static final String bedColorTag = "bedColor";

    /**
     * The house style.
     */
    public HouseStyle houseStyle;

    /**
     * Determines if the mineshaft is generated in the structure generation.
     */
    public boolean addMineshaft;

    public DyeColor bedColor;

    /**
     * Initializes a new instance of the {@link HouseAdvancedConfiguration} class.
     */
    public HouseAdvancedConfiguration() {
        super();

        this.addMineshaft = true;
        this.houseStyle = HouseStyle.MANOR;
        this.bedColor = DyeColor.RED;
    }

    @Override
    public void Initialize() {
        super.Initialize();
        this.houseStyle = HouseStyle.MANOR;
        this.bedColor = DyeColor.RED;
    }

    @Override
    protected CompoundTag CustomWriteToCompoundTag(CompoundTag tag) {
        tag.putInt(HouseAdvancedConfiguration.houseStyleTag, this.houseStyle.value);
        tag.putBoolean(HouseAdvancedConfiguration.addMineshaftTag, this.addMineshaft);
        tag.putInt(HouseAdvancedConfiguration.bedColorTag, this.bedColor.getId());

        return tag;
    }

    @Override
    protected void CustomReadFromNBTTag(CompoundTag messageTag, StructureConfiguration config) {
        HouseAdvancedConfiguration houseConfiguration = ((HouseAdvancedConfiguration) config);

        if (messageTag.contains(HouseAdvancedConfiguration.houseStyleTag)) {
            houseConfiguration.houseStyle = HouseStyle.ValueOf(messageTag.getInt(HouseAdvancedConfiguration.houseStyleTag));
        }

        if (messageTag.contains(HouseAdvancedConfiguration.addMineshaftTag)) {
            houseConfiguration.addMineshaft = messageTag.getBoolean(HouseAdvancedConfiguration.addMineshaftTag);
        }

        if (messageTag.contains(HouseAdvancedConfiguration.bedColorTag)) {
            houseConfiguration.bedColor = DyeColor.byId(messageTag.getInt(HouseAdvancedConfiguration.bedColorTag));
        }
    }

    /**
     * Custom method to read the CompoundNBT message.
     *
     * @param messageTag The message to create the configuration from.
     * @return A new configuration object with the values derived from the CompoundNBT.
     */
    @Override
    public HouseAdvancedConfiguration ReadFromCompoundTag(CompoundTag messageTag) {
        HouseAdvancedConfiguration config = new HouseAdvancedConfiguration();

        return (HouseAdvancedConfiguration) super.ReadFromCompoundTag(messageTag, config);
    }

    /**
     * This is used to actually build the structure as it creates the structure instance and calls build structure.
     *
     * @param player      The player which requested the build.
     * @param world       The world instance where the build will occur.
     * @param hitBlockPos This hit block position.
     */
    @Override
    protected void ConfigurationSpecificBuildStructure(Player player, ServerLevel world, BlockPos hitBlockPos) {
        StructureHouseAdvanced structure = StructureHouseAdvanced.CreateInstance(this.houseStyle.getStructureLocation(), StructureHouseAdvanced.class);

        if (structure.BuildStructure(this, world, hitBlockPos, player)) {
            this.RemoveStructureItemFromPlayer(player, ModRegistry.AdvancedHouse.get());
        }
    }

    /**
     * This house style is used to determine what type of houses are available to the user.
     *
     * @author WuestMan
     */
    public enum HouseStyle {
        MANOR(0, GuiLangKeys.ADVANCED_HOUSE_MANOR, new ResourceLocation("prefab", "textures/gui/house_advanced_manor.png"),
                "assets/prefab/structures/house_advanced_manor.zip"),
        WORKSHOP(1, GuiLangKeys.ADVANCED_HOUSE_WORKSHOP, new ResourceLocation("prefab", "textures/gui/house_advanced_workshop.png"),
                "assets/prefab/structures/house_advanced_workshop.zip"),
        ESTATE(2, GuiLangKeys.ADVANCED_HOUSE_ESTATE, new ResourceLocation("prefab", "textures/gui/house_advanced_estate.png"),
                "assets/prefab/structures/house_advanced_estate.zip");

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

        public static HouseStyle ValueOf(int value) {
            for (HouseStyle currentValue : HouseStyle.values()) {
                if (currentValue.value == value) {
                    return currentValue;
                }
            }

            return HouseStyle.MANOR;
        }

        public int getValue() {
            return value;
        }

        public String getDisplayName() {
            return GuiLangKeys.translateString(this.displayName);
        }

        public String getTranslationKey() {
            return this.displayName;
        }

        public ResourceLocation getHousePicture() {
            return this.housePicture;
        }

        public String getStructureLocation() {
            return this.structureLocation;
        }
    }
}
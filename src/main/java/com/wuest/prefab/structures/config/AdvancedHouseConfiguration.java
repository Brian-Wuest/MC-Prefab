package com.wuest.prefab.structures.config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.structures.predefined.StructureAdvancedHouse;
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
public class AdvancedHouseConfiguration extends StructureConfiguration {
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
     * Initializes a new instance of the {@link AdvancedHouseConfiguration} class.
     */
    public AdvancedHouseConfiguration() {
        super();

        this.addMineshaft = true;
        this.houseStyle = HouseStyle.SPRUCE_HOME;
        this.bedColor = DyeColor.RED;
    }

    @Override
    public void Initialize() {
        super.Initialize();
        this.houseStyle = HouseStyle.SPRUCE_HOME;
        this.bedColor = DyeColor.RED;
    }

    @Override
    protected CompoundTag CustomWriteToCompoundTag(CompoundTag tag) {
        tag.putInt(AdvancedHouseConfiguration.houseStyleTag, this.houseStyle.value);
        tag.putBoolean(AdvancedHouseConfiguration.addMineshaftTag, this.addMineshaft);
        tag.putInt(AdvancedHouseConfiguration.bedColorTag, this.bedColor.getId());

        return tag;
    }

    @Override
    protected void CustomReadFromNBTTag(CompoundTag messageTag, StructureConfiguration config) {
        AdvancedHouseConfiguration houseConfiguration = ((AdvancedHouseConfiguration) config);

        if (messageTag.contains(AdvancedHouseConfiguration.houseStyleTag)) {
            houseConfiguration.houseStyle = HouseStyle.ValueOf(messageTag.getInt(AdvancedHouseConfiguration.houseStyleTag));
        }

        if (messageTag.contains(AdvancedHouseConfiguration.addMineshaftTag)) {
            houseConfiguration.addMineshaft = messageTag.getBoolean(AdvancedHouseConfiguration.addMineshaftTag);
        }

        if (messageTag.contains(AdvancedHouseConfiguration.bedColorTag)) {
            houseConfiguration.bedColor = DyeColor.byId(messageTag.getInt(AdvancedHouseConfiguration.bedColorTag));
        }
    }

    /**
     * Custom method to read the CompoundNBT message.
     *
     * @param messageTag The message to create the configuration from.
     * @return A new configuration object with the values derived from the CompoundNBT.
     */
    @Override
    public AdvancedHouseConfiguration ReadFromCompoundTag(CompoundTag messageTag) {
        AdvancedHouseConfiguration config = new AdvancedHouseConfiguration();

        return (AdvancedHouseConfiguration) super.ReadFromCompoundTag(messageTag, config);
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
        StructureAdvancedHouse structure = StructureAdvancedHouse.CreateInstance(this.houseStyle.getStructureLocation(), StructureAdvancedHouse.class);

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
        SPRUCE_HOME(0, GuiLangKeys.ADVANCED_HOUSE_CABIN, new ResourceLocation("prefab", "textures/gui/advanced_house_manor.png"),
                "assets/prefab/structures/advanced_house_manor.zip");

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

            return HouseStyle.SPRUCE_HOME;
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

        public String getStructureLocation() {
            return this.structureLocation;
        }
    }
}
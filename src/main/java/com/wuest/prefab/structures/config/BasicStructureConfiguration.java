package com.wuest.prefab.structures.config;

import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.structures.config.enums.*;
import com.wuest.prefab.structures.items.ItemBasicStructure;
import com.wuest.prefab.structures.predefined.StructureBasic;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * This class is used for the basic structures in the mod.
 *
 * @author WuestMan
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BasicStructureConfiguration extends StructureConfiguration {
    private static final String structureEnumNameTag = "structureEnumName";
    private static final String structureDisplayNameTag = "structureDisplayName";
    private static final String bedColorTag = "bedColor";
    private static final String glassColorTag = "glassColor";
    private static final String chosenOptionTag = "chosenOption";

    /**
     * This field is used to contain the {@link EnumBasicStructureName} used by this instance.
     */
    public EnumBasicStructureName basicStructureName;

    /**
     * This field is used to contain the display name for the structure.
     */
    public String structureDisplayName;

    public DyeColor bedColor;

    public FullDyeColor glassColor;

    public BaseOption chosenOption;

    /**
     * Initializes a new instance of the BasicStructureConfiguration class.
     */
    public BasicStructureConfiguration() {
        super();
    }

    /**
     * Gets the display name for this structure.
     *
     * @return The unlocalized display name for this structure
     */
    public String getDisplayName() {
        if (this.basicStructureName == EnumBasicStructureName.Custom) {
            return this.structureDisplayName;
        } else {
            return this.basicStructureName.getItemTranslationString();
        }
    }

    /**
     * Determines if this is a custom structure.
     *
     * @return A value indicating whether this is a custom structure.
     */
    public boolean IsCustomStructure() {
        return this.basicStructureName == EnumBasicStructureName.Custom;
    }

    @Override
    public void Initialize() {
        super.Initialize();
        this.houseFacing = Direction.NORTH;
        this.basicStructureName = EnumBasicStructureName.AquaBase;
        this.bedColor = DyeColor.RED;
        this.glassColor = FullDyeColor.CLEAR;
        this.chosenOption = this.basicStructureName.baseOption.getSpecificOptions(false).get(0);
    }

    @Override
    protected void CustomReadFromNBTTag(CompoundNBT messageTag, StructureConfiguration config) {
        BasicStructureConfiguration basicConfig = (BasicStructureConfiguration) config;

        if (messageTag.contains(BasicStructureConfiguration.structureEnumNameTag)) {
            basicConfig.basicStructureName = EnumBasicStructureName.valueOf(messageTag.getString(BasicStructureConfiguration.structureEnumNameTag));
        }

        if (messageTag.contains(BasicStructureConfiguration.structureDisplayNameTag)) {
            basicConfig.structureDisplayName = messageTag.getString(BasicStructureConfiguration.structureDisplayNameTag);
        }

        if (messageTag.contains(BasicStructureConfiguration.bedColorTag)) {
            basicConfig.bedColor = DyeColor.valueOf(messageTag.getString(BasicStructureConfiguration.bedColorTag));
        }

        if (messageTag.contains(BasicStructureConfiguration.glassColorTag)) {
            basicConfig.glassColor = FullDyeColor.valueOf(messageTag.getString(BasicStructureConfiguration.glassColorTag));
        }

        if (messageTag.contains(BasicStructureConfiguration.chosenOptionTag)) {
            basicConfig.chosenOption = BaseOption.getOptionByTranslationString(messageTag.getString(BasicStructureConfiguration.chosenOptionTag));
        }
    }

    @Override
    protected CompoundNBT CustomWriteToCompoundNBT(CompoundNBT tag) {
        tag.putString(BasicStructureConfiguration.structureEnumNameTag, this.basicStructureName.name());

        if (this.structureDisplayName != null) {
            tag.putString(BasicStructureConfiguration.structureDisplayNameTag, this.structureDisplayName);
        }

        tag.putString(BasicStructureConfiguration.bedColorTag, this.bedColor.getSerializedName().toUpperCase());
        tag.putString(BasicStructureConfiguration.glassColorTag, this.glassColor.getSerializedName().toUpperCase());
        tag.putString(BasicStructureConfiguration.chosenOptionTag, this.chosenOption.getTranslationString());

        return tag;
    }

    /**
     * Reads information from an NBTTagCompound.
     *
     * @param messageTag The tag to read the data from.
     * @return An instance of {@link BasicStructureConfiguration} with vaules pulled from the NBTTagCompound.
     */
    @Override
    public BasicStructureConfiguration ReadFromCompoundNBT(CompoundNBT messageTag) {
        BasicStructureConfiguration config = new BasicStructureConfiguration();

        return (BasicStructureConfiguration) super.ReadFromCompoundNBT(messageTag, config);
    }

    /**
     * This is used to actually build the structure as it creates the structure instance and calls build structure.
     *
     * @param player      The player which requested the build.
     * @param world       The world instance where the build will occur.
     * @param hitBlockPos This hit block position.
     */
    @Override
    protected void ConfigurationSpecificBuildStructure(PlayerEntity player, ServerWorld world, BlockPos hitBlockPos) {
        String assetLocation = "";

        if (!this.IsCustomStructure()) {
            assetLocation = this.chosenOption.getAssetLocation();
        }

        StructureBasic structure = StructureBasic.CreateInstance(assetLocation, StructureBasic.class);

        if (structure.BuildStructure(this, world, hitBlockPos, Direction.NORTH, player)) {
            ItemStack stack = ItemBasicStructure.getBasicStructureItemInHand(player);

            if (!stack.isDamageableItem()) {
                if (stack.getCount() == 1) {
                    player.inventory.removeItem(stack);
                } else {
                    stack.setCount(stack.getCount() - 1);
                }

                player.containerMenu.broadcastChanges();
            } else {
                // The item has durability; damage it since the building was constructed.
                this.DamageHeldItem(player, stack.getItem());
            }
        }
    }

    /**
     * This enum is used to list the names of the basic structures and provide other information necessary.
     *
     * @author WuestMan
     */
    @SuppressWarnings("SpellCheckingInspection")
    public enum EnumBasicStructureName {
        Custom("custom", null, null, null),
        MachineryTower("machinery_tower", "item.prefab.item_machinery_tower", "item_machinery_tower", MachineryTowerOptions.Default),
        DefenseBunker("defense_bunker", "item.prefab.item_defense_bunker", "item_defense_bunker", DefenseBunkerOptions.Default),
        MineshaftEntrance("mineshaft_entrance", "item.prefab.item_mineshaft_entrance", "item_mineshaft_entrance", MineshaftEntranceOptions.Default),
        EnderGateway("ender_gateway", "item.prefab.item_ender_gateway", "item_ender_gateway", EnderGatewayOptions.Default),
        AquaBase("aqua_base", "item.prefab.item_aqua_base", "item_aqua_base", AquaBaseOptions.Default),
        GrassyPlain("grassy_plain", "item.prefab.item_grassy_plain", "item_grassy_plain", GrassyPlainOptions.Default),
        MagicTemple("magic_temple", "item.prefab.item_magic_temple", "item_magic_temple", MagicTempleOptions.Default),
        WatchTower("watch_tower", "item.prefab.item_watch_tower", "item_watch_tower", WatchTowerOptions.Default),
        WelcomeCenter("welcome_center", "item.prefab.item_welcome_center", "item_welcome_center", WelcomeCenterOptions.Default),
        Jail("jail", "item.prefab.item_jail", "item_jail", JailOptions.Default),
        Saloon("saloon", "item.prefab.item_saloon", "item_saloon", SaloonOptions.Default),
        SkiLodge("ski_lodge", "item.prefab.item_ski_lodge", "item_ski_lodge", SkiLodgeOptions.Default),
        WindMill("wind_mill", "item.prefab.item_wind_mill", "item_wind_mill", WindMillOptions.Default),
        TownHall("town_hall", "item.prefab.item_town_hall", "item_town_hall", TownHallOptions.Default),
        NetherGate("nether_gate", "item.prefab.item_nether_gate", "item_nether_gate", NetherGateOptions.AncientSkull),
        AdvancedAquaBase("advanced_aqua_base", "item.prefab.item_advanced_aqua_base", "item_advanced_aqua_base", AdvancedAquaBaseOptions.Default),
        WorkShop("workshop", "item.prefab.item_workshop", "item_workshop", WorkshopOptions.Default),
        VillagerHouses("villager_houses", "item.prefab.item_villager_houses", "item_villager_houses", VillagerHouseOptions.FLAT_ROOF),
        AdvancedWarehouse("advanced_warehouse", "item.prefab.item_advanced_warehouse", "item_advanced_warehouse", AdvancedWarehouseOptions.Default),
        Warehouse("warehouse", "item.prefab.item_warehouse", "item_warehouse", WarehouseOptions.Default),
        ModernBuildings("modern_buildings", "item.prefab.item_modern_buildings", "item_modern_buildings", ModernBuildingsOptions.Mall),
        StarterFarm("starter_farm", "item.prefab.item_starter_farm", "item_starter_farm", StarterFarmOptions.ElevatedFarm),
        ModerateFarm("moderate_farm", "item.prefab.item_moderate_farm", "item_moderate_farm", ModerateFarmOptions.AutomatedFarm),
        AdvancedFarm("advanced_farm", "item.prefab.item_advanced_farm", "item_advanced_farm", AdvancedFarmOptions.AutomatedBeeFarm);

        private final String name;
        private final String itemTranslationString;
        private final BaseOption baseOption;
        private ResourceLocation itemTextureLocation;

        /**
         * This is a basic structure which doesn't have any (or limited) custom processing.
         *
         * @param name             - This is the name for this structure. This is used for comparative purposes in
         *                         item stacks.
         * @param itemTranslationString  - This is the localization key to determine the displayed name to the user.
         * @param itemTextureLocation - This is the resource location for the item's texture when it's in the players
         *                         and or in inventories/the world.
         */
        EnumBasicStructureName(
                String name,
                String itemTranslationString,
                String itemTextureLocation,
                BaseOption baseOption) {
            this.name = name;
            this.itemTranslationString = itemTranslationString;

            if (itemTextureLocation != null) {
                this.itemTextureLocation = new ResourceLocation("prefab", itemTextureLocation);
            }

            this.baseOption = baseOption;
        }

        /**
         * The enum name.
         *
         * @return The enum name.
         */
        public String getName() {
            return this.name;
        }

        /**
         * The unlocalized name.
         *
         * @return The unlocalized name for this structure.
         */
        public String getItemTranslationString() {
            return this.itemTranslationString;
        }

        /**
         * This is the resource location for the item's texture when it's in the players and or in inventories/the
         * world.
         *
         * @return The resource location for the item texture.
         */
        public ResourceLocation getItemTextureLocation() {
            return this.itemTextureLocation;
        }

        public BaseOption getBaseOption() {
            return this.baseOption;
        }
    }
}

package com.wuest.prefab.structures.config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.proxy.CommonProxy;
import com.wuest.prefab.structures.custom.base.CustomStructureInfo;
import com.wuest.prefab.structures.items.ItemBlueprint;
import com.wuest.prefab.structures.predefined.StructureCustom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class CustomStructureConfiguration extends StructureConfiguration {
    private static final String bedColorTag = "bedColor";
    private static final String glassColorTag = "glassColor";
    private static final String customStructureTag = "customStructureName";

    public String customStructureName;
    public DyeColor bedColor;
    public FullDyeColor glassColor;

    public CustomStructureInfo customStructureInfo;

    public CustomStructureConfiguration() {
        super();
    }

    @Override
    public void Initialize() {
        super.Initialize();
        this.houseFacing = Direction.NORTH;
        this.customStructureName = "";
        this.bedColor = DyeColor.RED;
        this.glassColor = FullDyeColor.CLEAR;
    }

    @Override
    protected void CustomReadFromNBTTag(CompoundTag messageTag, StructureConfiguration config) {
        CustomStructureConfiguration basicConfig = (CustomStructureConfiguration) config;

        if (messageTag.contains(CustomStructureConfiguration.customStructureTag)) {
            basicConfig.customStructureName = messageTag.getString(CustomStructureConfiguration.customStructureTag);
        }

        if (messageTag.contains(CustomStructureConfiguration.bedColorTag)) {
            basicConfig.bedColor = DyeColor.byId(messageTag.getInt(CustomStructureConfiguration.bedColorTag));
        }

        if (messageTag.contains(CustomStructureConfiguration.glassColorTag)) {
            basicConfig.glassColor = FullDyeColor.ById(messageTag.getInt(CustomStructureConfiguration.glassColorTag));
        }
    }

    @Override
    protected CompoundTag CustomWriteToCompoundTag(CompoundTag tag) {
        tag.putString(CustomStructureConfiguration.customStructureTag, this.customStructureName);
        tag.putInt(CustomStructureConfiguration.bedColorTag, this.bedColor.getId());
        tag.putInt(CustomStructureConfiguration.glassColorTag, this.glassColor.getId());

        return tag;
    }

    /**
     * Reads information from an NBTTagCompound.
     *
     * @param messageTag The tag to read the data from.
     * @return An instance of {@link CustomStructureConfiguration} with vaules pulled from the NBTTagCompound.
     */
    @Override
    public CustomStructureConfiguration ReadFromCompoundTag(CompoundTag messageTag) {
        CustomStructureConfiguration config = new CustomStructureConfiguration();

        return (CustomStructureConfiguration) super.ReadFromCompoundTag(messageTag, config);
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
        this.findCustomStructure();

        StructureCustom structure = StructureCustom.CreateInstanceFromFile(this.customStructureInfo.structureFilePath, StructureCustom.class);

        if (structure.BuildStructure(this, world, hitBlockPos, player)) {
            ItemStack stack = ItemBlueprint.getCustomStructureStackInHand(player);

            if (stack != ItemStack.EMPTY) {
                if (!stack.isDamageableItem()) {
                    if (stack.getCount() == 1) {
                        player.getInventory().removeItem(stack);
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
    }

    protected void findCustomStructure() {
        for (CustomStructureInfo info : CommonProxy.CustomStructures) {
            if (info.displayName.equalsIgnoreCase(this.customStructureName.toLowerCase())) {
                this.customStructureInfo = info;
                return;
            }
        }
    }
}

package com.prefab;

import io.netty.util.internal.StringUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;

public class Utils {
    public static String[] WrapString(String value) {
        return Utils.WrapString(value, 50);
    }

    public static String[] WrapString(String value, int width) {
        String result = WordUtils.wrap(value, width);
        String[] results = result.split("\n");

        String[] returnValue = new String[results.length];

        for (int i = 0; i < results.length; i++) {
            returnValue[i] = results[i].trim();
        }

        return returnValue;
    }

    public static ArrayList<Component> WrapStringToLiterals(String value) {
        return Utils.WrapStringToLiterals(value, 50);
    }

    /**
     * This is a wrapper method to make sure that when minecraft changes the name of the StringTextComponent again it's a single place update.
     *
     * @param value The text to create the object from.
     * @return A StringTextComponent object.
     */
    public static MutableComponent createTextComponent(String value) {
        return Component.literal(value);
    }

    public static ArrayList<Component> WrapStringToLiterals(String value, int width) {
        String[] values = Utils.WrapString(value, width);
        ArrayList<Component> returnValue = new ArrayList<>();

        for (String stringValue : values) {
            returnValue.add(Utils.createTextComponent(stringValue));
        }

        return returnValue;
    }

    public static Direction getDirectionByName(String name) {
        if (!StringUtils.isBlank(name)) {
            for (Direction direction : Direction.values()) {
                if (direction.toString().equalsIgnoreCase(name)) {
                    return direction;
                }
            }
        }

        return Direction.NORTH;
    }

    /**
     * Gets a collection of all blocks with the associated tag key.
     *
     * @param tagKey The tagkey to look for.
     * @return A collection containing the blocks.
     */
    public static @NotNull ArrayList<Block> getBlocksWithTagKey(TagKey<Block> tagKey) {
        ArrayList<Block> blocks = new ArrayList<>();

        for (Holder<Block> blockHolder : BuiltInRegistries.BLOCK.getTagOrEmpty(tagKey)) {
            blocks.add(blockHolder.value());
        }

        return blocks;
    }

    /**
     * Determines if the current block state has a tag.
     *
     * @param blockState The block state to check.
     * @param location   The resource location of the tag to check for.
     * @return True if the tag exists on the block state; otherwise false.
     */
    public static boolean doesBlockStateHaveTag(BlockState blockState, ResourceLocation location) {
        for (TagKey<Block> tagKey : blockState.getTags().toList()) {
            if (tagKey.location().toString().equalsIgnoreCase(location.toString())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets a collection of all item stacks with the associated tag.
     *
     * @param resourceLocation The resource location to check.
     * @return A collection of found blocks.
     */
    public static ArrayList<ItemStack> getItemStacksWithTag(ResourceLocation resourceLocation) {
        TagKey<Item> tags = TagKey.create(Registries.ITEM, resourceLocation);
        ArrayList<ItemStack> itemStacks = new ArrayList<>();

        for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tags)) {
            itemStacks.add(new ItemStack(holder.value()));
        }

        return itemStacks;
    }

    public static BlockState readBlockState(CompoundTag tag) {
        if (!tag.contains("Name", 8)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(tag.getString("Name")));
            BlockState blockState = block.defaultBlockState();

            if (tag.contains("Properties", 10)) {
                CompoundTag compoundTag = tag.getCompound("Properties");
                StateDefinition<Block, BlockState> stateDefinition = block.getStateDefinition();

                for(String s : compoundTag.getAllKeys()) {
                    Property<?> property = stateDefinition.getProperty(s);
                    if (property != null) {
                        blockState = setValueHelper(blockState, property, s, compoundTag, tag);
                    }
                }
            }

            return blockState;
        }
    }

    private static <S extends StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(S blockState, Property<T> property, String tagKey, CompoundTag compoundTag, CompoundTag originalTag) {
        Optional<T> optional = property.getValue(compoundTag.getString(tagKey));
        if (optional.isPresent()) {
            return blockState.setValue(property, optional.get());
        } else {
            PrefabBase.logger.warn("Unable to read property: {} with value: {} for blockstate: {}", tagKey, compoundTag.getString(tagKey), originalTag.toString());
            return blockState;
        }
    }
}

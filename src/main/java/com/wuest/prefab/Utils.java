package com.wuest.prefab;

import com.wuest.prefab.proxy.messages.TagMessage;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import io.netty.util.internal.StringUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
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
import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.InvocationTargetException;
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

    public static ArrayList<MutableComponent> WrapStringToLiterals(String value) {
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

    public static ArrayList<MutableComponent> WrapStringToLiterals(String value, int width) {
        String[] values = Utils.WrapString(value, width);
        ArrayList<MutableComponent> returnValue = new ArrayList<>();

        for (String stringValue : values) {
            returnValue.add(Utils.createTextComponent(stringValue));
        }

        return returnValue;
    }

    public static TagMessage createMessage(CompoundTag tag) {
        return new TagMessage(tag);
    }

    public static <T extends TagMessage> T createGenericMessage(CompoundTag tag, Class<T> tClass) {
        try {
            return tClass.getConstructor(CompoundTag.class).newInstance(tag);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static StructureTagMessage createStructureMessage(CompoundTag tag, StructureTagMessage.EnumStructureConfiguration structureConfiguration) {
        return new StructureTagMessage(tag, structureConfiguration);
    }

    public static Direction getDirectionByName(String name) {
        if (!StringUtil.isNullOrEmpty(name)) {
            for (Direction direction : Direction.values()) {
                if (direction.getSerializedName().equalsIgnoreCase(name)) {
                    return direction;
                }
            }
        }

        return Direction.NORTH;
    }

    /**
     * Gets a collection of all blocks with the associated tag.
     * @param resourceLocation The resource location to check.
     * @return A collection of found blocks.
     */
    public static ArrayList<Block> getBlocksWithTagLocation(ResourceLocation resourceLocation) {
        TagKey<Block> tags = TagKey.create(Registries.BLOCK, resourceLocation);
        ArrayList<Block> blocks = new ArrayList<>();

        for (Holder<Block> blockHolder : BuiltInRegistries.BLOCK.getTagOrEmpty(tags)) {
            blocks.add(blockHolder.value());
        }

        return blocks;
    }

    /**
     * Gets a collection of all blocks with the associated tag key.
     * @param tagKey The tagkey to look for.
     * @return A collection containing the blocks.
     */
    public static ArrayList<Block> getBlocksWithTagKey(TagKey<Block> tagKey) {
        ArrayList<Block> blocks = new ArrayList<>();

        for (Holder<Block> blockHolder : BuiltInRegistries.BLOCK.getTagOrEmpty(tagKey)) {
            blocks.add(blockHolder.value());
        }

        return blocks;
    }

    /**
     * Determines if a particular block has a tag.
     * @param block The block to check.
     * @param location The resource location of the tag to check for.
     * @return True if the tag was found; otherwise false.
     */
    public static boolean doesBlockHaveTag(Block block, ResourceLocation location) {
        ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);
        TagKey<Block> tags = TagKey.create(Registries.BLOCK, location);

        for (Holder<Block> blockHolder : BuiltInRegistries.BLOCK.getTagOrEmpty(tags)) {
            if (blockHolder.is(blockKey)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the current block state has a tag.
     * @param blockState The block state to check.
     * @param location The resource location of the tag to check for.
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

    /**
     * Determines if the current item has a tag.
     * @param item The item to check.
     * @param location The resource location of the tag to check for.
     * @return True if the tag exists on the item; otherwise false.
     */
    public static boolean doesItemHaveTag(Item item, ResourceLocation location) {
        ResourceLocation blockKey = BuiltInRegistries.ITEM.getKey(item);
        TagKey<Item> tags = TagKey.create(Registries.ITEM, location);

        for (Holder<Item> blockHolder : BuiltInRegistries.ITEM.getTagOrEmpty(tags)) {
            if (blockHolder.is(blockKey)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the current item stack has a tag.
     * @param itemStack The item stack to check.
     * @param location The resource location of the tag to check for.
     * @return True if the tag exists on the item stack; otherwise false.
     */
    public static boolean doesItemStackHaveTag(ItemStack itemStack, ResourceLocation location) {
        for (TagKey<Item> tagKey : itemStack.getTags().toList()) {
            if (tagKey.location().toString().equalsIgnoreCase(location.toString())) {
                return true;
            }
        }

        return false;
    }

    public static BlockState readBlockState(CompoundTag tag) {
        if (!tag.contains("Name", 8)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(tag.getString("Name")));
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
            Prefab.LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", tagKey, compoundTag.getString(tagKey), originalTag.toString());
            return blockState;
        }
    }
}

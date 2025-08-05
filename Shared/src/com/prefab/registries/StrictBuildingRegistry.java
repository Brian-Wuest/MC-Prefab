package com.prefab.registries;

import com.prefab.PrefabBase;
import com.prefab.Utils;
import com.prefab.config.ModConfiguration;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StrictBuildingRegistry {
    private final ArrayList<ResourceLocation> overwritableBlocks;

    public StrictBuildingRegistry() {
        this.overwritableBlocks = new ArrayList<>();
    }

    public ArrayList<ResourceLocation> getOverwritableBlocks() {
        return overwritableBlocks;
    }

    // make method which takes configuration to build out the properties for overwritable blocks.
    public void processModConfiguration(ModConfiguration modConfiguration) {
        if(modConfiguration.strictModeOptions.enabled) {
            this.processBlocks(null, modConfiguration);

            this.processTags(modConfiguration);
        }
    }

    private void processBlocks(ArrayList<ResourceLocation> resourceLocations, ModConfiguration modConfiguration) {
        // This method could be called from the tag method when it has the blocks to process, or when processing blocks
        // stand alone.
        if (resourceLocations == null) {
            resourceLocations = processStringCollection(modConfiguration.strictModeOptions.overwritableBlocks);
        }

        if (!resourceLocations.isEmpty()) {
            // Go through each resource location and make sure it's a valid block.
            HashMap<String, ResourceLocation> validBlockKeys = new HashMap<>();

            for (ResourceLocation resourceLocation : resourceLocations) {
                // Don't put duplicate blocks in the hashmap
                if (!validBlockKeys.containsKey(resourceLocation.getPath())) {
                    Optional<Block> foundBlock = BuiltInRegistries.BLOCK.getOptional(resourceLocation);

                    if (foundBlock.isPresent()) {
                        validBlockKeys.put(resourceLocation.getPath(), resourceLocation);
                    } else {
                        PrefabBase.logger.warn("""
                                    Strict Building Mode Processing: The resource location: "{}" is not a valid block.\r
                                    Please check your spelling or if you have a missing mod.\r
                                    Resource locations are case-insensitive.""",
                                resourceLocation.getPath().toLowerCase());
                    }
                }
            }

            // All the blocks in the validBlockKeys are okay to add to the main collection.
            this.overwritableBlocks.addAll(validBlockKeys.values());
        }
    }

    private void processTags(ModConfiguration modConfiguration) {
        ArrayList<ResourceLocation> tags = this.processStringCollection(modConfiguration.strictModeOptions.overwritableTags);

        if (!tags.isEmpty()) {
            Map<String, TagKey<Block>> registeredTags = BuiltInRegistries.BLOCK.getTagNames()
                    .distinct()
                    .collect(Collectors.toMap(
                            x -> x.location().getPath().toLowerCase(),
                            x -> x,
                            // Don't merge the results as they aren't compatible in the case of collisions
                            // Just take the original record.
                            // The core minecraft code should have take care of this by not allowing duplicates
                            // But a mod might have overridden that for something, but we cannot allow that.
                            (s, a) -> s));

            // We have tags to process, go through each one and get the blocks from each.
            for (ResourceLocation tag : tags) {
                TagKey<Block> registeredTag = registeredTags.get(tag.getPath().toLowerCase());

                // If the registeredTag is null then it's not in the game.
                // Maybe a missing mod or it was mistyped.
                // Log a message and continue
                if (registeredTag == null) {
                    PrefabBase.logger.warn("""
                                    Strict Building Mode Processing: Unable to find tag with resource location: {}.\r
                                    Please check your spelling or if you have a missing mod.\r
                                    Resource locations are case-insensitive.\r
                                    Built-in tags should have a root of "minecraft". Example: minecraft:leaves""",
                            tag.getPath().toLowerCase());
                    continue;
                }

                ArrayList<Block> blocks = Utils.getBlocksWithTagKey(registeredTag);

                if (blocks.isEmpty()) {
                    PrefabBase.logger.info("""
                            Strict Building Mod Processing: There are no blocks registered with tag's resource location: {}\r
                            If this is unexpected, you may be missing a mod which adds blocks to this tag.\r
                            Resource locations are case-insensitive.""",
                            tag.getPath().toLowerCase());
                    continue;
                }

                ArrayList<ResourceLocation> validBlocks = new ArrayList<>();

                for (Block block : blocks) {
                     validBlocks.add(BuiltInRegistries.BLOCK.getKey(block));
                }

                this.overwritableBlocks.addAll(validBlocks);
            }
        }
    }

    private ArrayList<ResourceLocation> processStringCollection(@NotNull ArrayList<String> collection) {
        ArrayList<ResourceLocation> resourceLocations = new ArrayList<>();

        for (String value : collection) {
            if (!StringUtils.isBlank(value)) {
                ResourceLocation resourceLocation = ResourceLocation.tryParse(value);

                if (resourceLocation != null) {
                    resourceLocations.add(resourceLocation);
                }
            }
        }

        return resourceLocations;
    }
}

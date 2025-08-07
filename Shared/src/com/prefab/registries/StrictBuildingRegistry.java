package com.prefab.registries;

import com.prefab.PrefabBase;
import com.prefab.Utils;
import com.prefab.config.ModConfiguration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collector;

public class StrictBuildingRegistry {
    private final ArrayList<ResourceLocation> overwritableBlockResourceLocations;
    private final ArrayList<Block> overwritableBlocks;

    public StrictBuildingRegistry() {
        this.overwritableBlockResourceLocations = new ArrayList<>();
        this.overwritableBlocks = new ArrayList<>();
    }

    public ArrayList<ResourceLocation> getOverwritableBlockResourceLocations() {
        return this.overwritableBlockResourceLocations;
    }

    public ArrayList<Block> getOverwritableBlocks() {
        return this.overwritableBlocks;
    }

    // make method which takes configuration to build out the properties for overwritable blocks.
    public void processModConfiguration(ModConfiguration modConfiguration) {
        if(modConfiguration.strictModeOptions.enabled) {
            this.processBlocks(modConfiguration);

            this.processTags(modConfiguration);
        }
    }

    private void processBlocks(ModConfiguration modConfiguration) {
        ArrayList<ResourceLocation> resourceLocations = processStringCollection(modConfiguration.strictModeOptions.overwritableBlocks);

        // Go through each resource location and make sure it's a valid block.
        HashMap<String, ResourceLocation> validBlockKeys = new HashMap<>();
        HashMap<String, Block> validBlocks = new HashMap<>();

        for (ResourceLocation resourceLocation : resourceLocations) {
            // Don't put duplicate blocks in the hashmap
            if (!validBlockKeys.containsKey(resourceLocation.getPath())) {
                Optional<Block> foundBlock = BuiltInRegistries.BLOCK.getOptional(resourceLocation);

                if (foundBlock.isPresent()) {
                    validBlockKeys.put(resourceLocation.getPath(), resourceLocation);
                    validBlocks.put(resourceLocation.getPath(), foundBlock.get());
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
        this.overwritableBlockResourceLocations.addAll(validBlockKeys.values());
        this.overwritableBlocks.addAll(validBlocks.values());
    }

    private void processTags(ModConfiguration modConfiguration) {
        ArrayList<ResourceLocation> tags = this.processStringCollection(modConfiguration.strictModeOptions.overwritableTags);

        if (!tags.isEmpty()) {
            // Use a custom collector to pull all the tag names from the registry.
            // We need to make sure that the registry path is unique and cannot guarantee that the minecraft code
            // caught a duplicate registration.
            HashMap<String, TagKey<Block>> registeredTags = BuiltInRegistries.BLOCK.getTagNames().collect(Collector.of(
                    HashMap::new,
                    (map, tag) -> {
                        if (!map.containsKey(tag.location().getPath().toLowerCase())) {
                            map.put(tag.location().getPath().toLowerCase(), tag);
                        }
                    },
                    (map1, map2) -> {
                        // Merge two maps together for parallel streams (if this ever comes up).
                        map1.putAll(map2);
                        return map1;
                    },
                    Collector.Characteristics.IDENTITY_FINISH
            ));

            ArrayList<Block> allValidTagBlocks = new ArrayList<>();

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

                allValidTagBlocks.addAll(blocks);
            }

            ArrayList<ResourceLocation> blockKeys = new ArrayList<>();

            // Now that we have all the blocks, grab all the resource locations and make sure
            // That we don't have duplicates from the main collection
            for (int i = 0; i < allValidTagBlocks.size(); i++) {
                Block block = allValidTagBlocks.get(i);
                ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);

                Optional<ResourceLocation> matchingResourceLocations = this.getOverwritableBlockResourceLocations()
                        .stream()
                        .filter(x -> x.getPath().equalsIgnoreCase(blockKey.getPath()))
                        .findFirst();

                if (matchingResourceLocations.isEmpty()) {
                    blockKeys.add(blockKey);
                }
                else {
                    // This block is already in the list of overwritable blocks, no need to add it again.
                    // This means it must also be removed from the current collection being looped on.
                    // FYI - This just means that a previous tag registered this block already.
                    // OR it was added as a part of the overwritable blocks array first!
                    allValidTagBlocks.remove(i);

                    // Decrement the current index so we don't skip over any items.
                    i--;
                }
            }

            this.overwritableBlockResourceLocations.addAll(blockKeys);
            this.overwritableBlocks.addAll(allValidTagBlocks);
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

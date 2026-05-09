package com.prefab.fabric.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.prefab.PrefabBase;
import com.prefab.fabric.ModRegistry;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.core.HolderLookup;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

/**
 * Custom recipe condition.
 * Need this specifically for Fabric as Fabric and NeoForge have their own implementations even those they
 * look and behave exactly the same (with slightly different wording of class names).
 *
 * @param configName The configuration name to evaluate
 */
public record RecipeEnabledCondition(String configName) implements ResourceCondition {
    public static final MapCodec<RecipeEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(
            inst -> inst.group(
            Codec.STRING.fieldOf("configName").forGetter(RecipeEnabledCondition::configName)
    ).apply(inst, RecipeEnabledCondition::new));

    @Override
    public ResourceConditionType<?> getType() {
        return ModRegistry.RECIPE_ENABLED;
    }

    @Override
    public boolean test(HolderLookup.@Nullable Provider registryLookup) {
        return StringUtils.isBlank(configName)
                || !PrefabBase.configuration.recipes.containsKey(configName)
                || PrefabBase.configuration.recipes.get(configName);
    }
}

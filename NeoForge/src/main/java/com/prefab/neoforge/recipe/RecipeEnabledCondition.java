package com.prefab.neoforge.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.prefab.PrefabBase;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Custom recipe condition.
 * Need this specifically for NeoForge as NeoForge and Fabric  have their own implementations even those they
 * look and behave exactly the same (with slightly different wording of class names).
 *
 * @param configName The configuration name to evaluate
 */
public record RecipeEnabledCondition(String configName) implements ICondition {
    public static final MapCodec<RecipeEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.fieldOf("value").forGetter(RecipeEnabledCondition::configName)
    ).apply(inst, RecipeEnabledCondition::new));

    @Override
    public boolean test(@NotNull IContext context) {
        return StringUtils.isBlank(configName)
                || !PrefabBase.configuration.recipes.containsKey(configName)
                || PrefabBase.configuration.recipes.get(configName);
    }

    @Override
    public @NotNull MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}

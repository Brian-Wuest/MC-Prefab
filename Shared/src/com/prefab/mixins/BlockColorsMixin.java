package com.prefab.mixins;

import com.prefab.ModRegistryBase;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BlockColors.class)
public class BlockColorsMixin {

    @Inject(method = "createDefault", at = @At("RETURN"))
    private static void create(CallbackInfoReturnable<BlockColors> ci) {
        BlockColors blockColors = ci.getReturnValue();

        blockColors.register(List.of(BlockTintSources.grassBlock()), ModRegistryBase.GrassWall);
        blockColors.register(List.of(BlockTintSources.grassBlock()), ModRegistryBase.GrassSlab);
        blockColors.register(List.of(BlockTintSources.grassBlock()), ModRegistryBase.GrassStairs);
    }
}

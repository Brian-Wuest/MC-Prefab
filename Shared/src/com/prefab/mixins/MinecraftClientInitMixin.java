package com.prefab.mixins;

import com.prefab.ClientModRegistryBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientInitMixin {
    // signal that we want to inject into a method
    @Inject(
            method = "<init>",  // the jvm bytecode signature for the constructor
            at = @At("TAIL")  // signal that this void should be run at the method TAIL, meaning the last opcode
    )
    public void constructorHead(
            // you will need to put any parameters the constructor accepts here, they will be the actual passed values
            // it also needs to accept a special argument that mixin passes to this injection method
            GameConfig gameConfig, CallbackInfo ci
    ) {
    }
}

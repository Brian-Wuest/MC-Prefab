package com.prefab.mixins;

import com.mojang.authlib.GameProfile;
import com.prefab.config.EntityPlayerConfiguration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Player.class)
public class SavePlayerDataMixin {
    @Shadow
    @Final
    private GameProfile gameProfile;

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {
        UUID prefabPlayerId = this.gameProfile.getId();
        EntityPlayerConfiguration prefabConfiguration;

        if (!EntityPlayerConfiguration.playerTagData.containsKey(prefabPlayerId)) {
            prefabConfiguration = new EntityPlayerConfiguration();

        } else {
            prefabConfiguration = EntityPlayerConfiguration.playerTagData.get(prefabPlayerId);
        }

        CompoundTag prefabTag = prefabConfiguration.createPlayerTag();
        tag.put("PrefabTag", prefabTag);
        //PrefabBase.logger.info("Saving prefab tag information to player data.", prefabTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
        UUID prefabPlayerId = this.gameProfile.getId();

        EntityPlayerConfiguration prefabConfiguration = new EntityPlayerConfiguration();

        if (tag.contains("PrefabTag")) {
            CompoundTag prefabTag = tag.getCompound("PrefabTag");

            //PrefabBase.logger.info("Loading prefab tag information from player data.", prefabTag);
            prefabConfiguration.loadFromNBTTagCompound(prefabTag);
        }

        //PrefabBase.logger.info("Placing prefab player config data into static dictionary for player id.");

        if (!EntityPlayerConfiguration.playerTagData.containsKey(prefabPlayerId)) {
            EntityPlayerConfiguration.playerTagData.put(prefabPlayerId, prefabConfiguration);
        } else {
            EntityPlayerConfiguration.playerTagData.replace(prefabPlayerId, prefabConfiguration);
        }
    }
}

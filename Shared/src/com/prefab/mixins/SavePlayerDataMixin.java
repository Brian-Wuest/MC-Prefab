package com.prefab.mixins;

import com.mojang.authlib.GameProfile;
import com.prefab.Utils;
import com.prefab.config.EntityPlayerConfiguration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
    private void writeCustomDataToTag(ValueOutput valueOutput, CallbackInfo ci) {
        UUID prefabPlayerId = this.gameProfile.id();
        EntityPlayerConfiguration prefabConfiguration;

        if (!EntityPlayerConfiguration.playerTagData.containsKey(prefabPlayerId)) {
            prefabConfiguration = new EntityPlayerConfiguration();

        } else {
            prefabConfiguration = EntityPlayerConfiguration.playerTagData.get(prefabPlayerId);
        }

        CompoundTag prefabTag = prefabConfiguration.createPlayerTag();
        valueOutput.store("PrefabTag", CompoundTag.CODEC, prefabTag);
        //PrefabBase.logger.info("Saving prefab tag information to player data.", prefabTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readCustomDataFromTag(ValueInput valueInput, CallbackInfo ci) {
        UUID prefabPlayerId = this.gameProfile.id();

        EntityPlayerConfiguration prefabConfiguration = new EntityPlayerConfiguration();

        if (Utils.valueInputContains(valueInput,"PrefabTag")) {
            CompoundTag prefabTag = valueInput.read("PrefabTag", CompoundTag.CODEC).orElse(new CompoundTag());

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

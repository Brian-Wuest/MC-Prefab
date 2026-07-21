package com.prefab.fabric.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.prefab.ModRegistryBase;
import com.prefab.structures.items.ItemBulldozer;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public class AnvilScreenHandlerMixin {
    @Final
    @Shadow
    private DataSlot cost;

    @Inject(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z", ordinal = 0), cancellable = true)
    public void AnvilUpdate(CallbackInfo ci, @Local(ordinal = 1) ItemStack itemStack2, @Local(ordinal = 2) ItemStack itemStack3) {
        // Because this gets injected into the actual class; we can use "this" to represent the AnvilScreenHandler correctly.
        AnvilMenu prefabHandler = (AnvilMenu) (Object) this;
        Item prefabTripleCompressedStone = ModRegistryBase.TripleCompressedStoneItem;
        ItemBulldozer prefabBulldozer = ModRegistryBase.Bulldozer;

        if (itemStack2.getItem() == prefabTripleCompressedStone || itemStack3.getItem() == prefabTripleCompressedStone) {
            if (itemStack2.getItem() == prefabBulldozer || itemStack3.getItem() == prefabBulldozer) {
                this.cost.set(4);

                itemStack3 = new ItemStack(prefabBulldozer);
                prefabBulldozer.setPoweredValue(itemStack3, true);
                itemStack3.setDamageValue(0);

                // In order to get this to work an "accessWidener" is necessary.
                prefabHandler.resultSlots.setItem(0, itemStack3);

                ci.cancel();
            }
        }
    }
}

package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

/**
 * This class is used to create a sword which has the same speed as pre-1.9
 * swords.
 *
 * @author WuestMan
 */
public class ItemSwiftBlade extends SwordItem {
    /*
     * Initializes a new instance of the ItemSwiftBlade class.
     */
    public ItemSwiftBlade(Tier tier, int attackDamageIn, float attackSpeedIn) {
        super(tier, attackDamageIn, attackSpeedIn,
                new Item.Properties().stacksTo(1).defaultDurability(tier.getUses()).tab(ModRegistry.PREFAB_GROUP));
    }

    /**
     * Returns the amount of damage this item will deal. One heart of damage is
     * equal to 2 damage points.
     */
    @Override
    public float getDamage() {
        return this.getTier().getAttackDamageBonus();
    }

    /**
     * Return the name for this tool's material.
     */
    public String getToolMaterialName() {
        return this.getTier().toString();
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on
     * material.
     */
    @Override
    public int getEnchantmentValue() {
        return this.getTier().getEnchantmentValue();
    }

}
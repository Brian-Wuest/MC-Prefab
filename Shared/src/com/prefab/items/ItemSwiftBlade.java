package com.prefab.items;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

/**
 * This class is used to create a sword which has the same speed as pre-1.9
 * swords.
 *
 * @author WuestMan
 */
public class ItemSwiftBlade extends Item {
    ToolMaterial toolMaterial;

    /*
     * Initializes a new instance of the ItemSwiftBlade class.
     */
    public ItemSwiftBlade(ToolMaterial tier, float attackDamageIn, float attackSpeedIn, Item.Properties properties) {
        super(properties.sword(tier, attackDamageIn, attackSpeedIn));
        //super(tier, attackDamageIn, attackSpeedIn, applySwordProperties(tier, properties, attackDamageIn, attackSpeedIn));
        toolMaterial = tier;
    }

    /**
     * Returns the amount of damage this item will deal. One heart of damage is
     * equal to 2 damage points.
     */
    @Override
    public float getAttackDamageBonus(Entity entity, float f, DamageSource damageSource) {
        return this.toolMaterial.attackDamageBonus();
    }
}
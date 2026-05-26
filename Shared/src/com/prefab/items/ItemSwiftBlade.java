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
import net.minecraft.world.item.SwordItem;
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
public class ItemSwiftBlade extends SwordItem {
    ToolMaterial toolMaterial;

    /*
     * Initializes a new instance of the ItemSwiftBlade class.
     */
    public ItemSwiftBlade(ToolMaterial tier, float attackDamageIn, float attackSpeedIn, Item.Properties properties) {
        super(tier, attackDamageIn, attackSpeedIn, applySwordProperties(tier, properties, attackDamageIn, attackSpeedIn));
        toolMaterial = tier;
    }

    public static Item.Properties applySwordProperties(ToolMaterial toolMaterial, Item.Properties properties, float attackDamageIn, float attackSpeedIn) {
        HolderGetter<Block> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return applyCommonProperties(toolMaterial, properties)
                .component(DataComponents.TOOL,
                        new Tool(
                                List.of(Tool.Rule.minesAndDrops(
                                        HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 15.0F),
                                        Tool.Rule.overrideSpeed(
                                                holderGetter.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F)),
                                1.0F, 2))
                .attributes(createSwordAttributes(toolMaterial, attackDamageIn, attackSpeedIn));
    }

    private static Item.Properties applyCommonProperties(ToolMaterial toolMaterial, Item.Properties properties) {
        return properties
                .durability(toolMaterial.durability())
                .repairable(toolMaterial.repairItems())
                .enchantable(toolMaterial.enchantmentValue())
                .stacksTo(1);
    }

    private static ItemAttributeModifiers createSwordAttributes(ToolMaterial toolMaterial, float attackDamageIn, float attackSpeedIn) {
        return ItemAttributeModifiers.builder().add(
                Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                Item.BASE_ATTACK_DAMAGE_ID, attackDamageIn + toolMaterial.attackDamageBonus(),
                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                                Item.BASE_ATTACK_SPEED_ID,
                                attackSpeedIn,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
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
package net.redstone233.tam.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
import net.redstone233.tam.enchantment.ModEnchantments;
import net.redstone233.tam.enchantment.effect.FireBlessingEnchantmentEffect;
import net.redstone233.tam.enchantment.effect.FrostBlessingEnchantmentEffect;
import net.redstone233.tam.enchantment.effect.VampirismEnchantmentEffect;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentGenerator extends FabricDynamicRegistryProvider {
    public ModEnchantmentGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        // 注册吸血附魔
        register(entries, ModEnchantments.VAMPIRISM, Enchantment.builder(Enchantment.definition(
                        wrapperLookup.getWrapperOrThrow(RegistryKeys.ITEM).getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        wrapperLookup.getWrapperOrThrow(RegistryKeys.ITEM).getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        5, // 最大等级 5
                        2, // 重量 2
                        Enchantment.leveledCost(15, 8), // 最小成本: base=15, per_level_above_first=8
                        Enchantment.leveledCost(25, 8), // 最大成本: base=25, per_level_above_first=8
                        8, // 铁砧成本 8
                        AttributeModifierSlot.MAINHAND))
                .exclusiveSet(wrapperLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new VampirismEnchantmentEffect(
                                EnchantmentLevelBasedValue.constant(0.2f), // 基础效率 20%
                                EnchantmentLevelBasedValue.constant(0.1f)  // 每级增加 10%
                        )
                ));

        // 注册火之祝福附魔
        register(entries, ModEnchantments.FIRE_BLESSING, Enchantment.builder(Enchantment.definition(
                        wrapperLookup.getWrapperOrThrow(RegistryKeys.ITEM).getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        wrapperLookup.getWrapperOrThrow(RegistryKeys.ITEM).getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        5, // 最大等级 5
                        3, // 重量 3
                        Enchantment.leveledCost(12, 11), // 最小成本: base=12, per_level_above_first=11
                        Enchantment.leveledCost(22, 11), // 最大成本: base=22, per_level_above_first=11
                        7, // 铁砧成本 7
                        AttributeModifierSlot.MAINHAND))
                .exclusiveSet(wrapperLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new FireBlessingEnchantmentEffect(
                                EnchantmentLevelBasedValue.linear(5.0f, 3.0f) // 基础5秒，每级增加3秒
                        )
                ));

        // 注册冰之祝福附魔
        register(entries, ModEnchantments.FROST_BLESSING, Enchantment.builder(Enchantment.definition(
                        wrapperLookup.getWrapperOrThrow(RegistryKeys.ITEM).getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        wrapperLookup.getWrapperOrThrow(RegistryKeys.ITEM).getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        5, // 最大等级 5
                        3, // 重量 3
                        Enchantment.leveledCost(10, 10), // 最小成本: base=10, per_level_above_first=10
                        Enchantment.leveledCost(20, 10), // 最大成本: base=20, per_level_above_first=10
                        6, // 铁砧成本 6
                        AttributeModifierSlot.MAINHAND))
                .exclusiveSet(wrapperLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new FrostBlessingEnchantmentEffect(
                                EnchantmentLevelBasedValue.linear(4.0f, 2.0f) // 基础4秒，每级增加2秒
                        )
                ));
    }


    private void register(Entries entries, RegistryKey<Enchantment> key, Enchantment.Builder builder, ResourceCondition... resourceConditions) {
        entries.add(key, builder.build(key.getValue()), resourceConditions);
    }

    @Override
    public String getName() {
        return "Mod Enchantments";
    }
}

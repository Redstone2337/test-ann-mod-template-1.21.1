package net.redstone233.tam.item.custom;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.redstone233.tam.core.keys.ModKeys;
import net.redstone233.tam.enchantment.ModEnchantments;

import java.util.List;

public class BlazingFlameSwordItem extends SwordItem {
    public BlazingFlameSwordItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings
                .attributeModifiers(
                        createAttributeModifiers(ToolMaterials.NETHERITE, 500, 3.7f)
                ));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof LivingEntity livingEntity && attacker instanceof PlayerEntity player) {
            livingEntity.isOnFire();
            livingEntity.setOnFire(true);
            livingEntity.setOnFireForTicks(2400);
            livingEntity.setOnFireFor(120.0f);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,1200,4,false,false,false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,1200,4,false,false,false));
        }
        return true;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user instanceof PlayerEntity player && entity instanceof LivingEntity target) {
            if (ModKeys.isUseAbilityKeyPressed()) {
                target.setOnFire(true);
                target.setOnFireFor(180.0f);
                target.setOnFireForTicks(3600);
            }
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST,3600,4));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST,1800,6));
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.FAIL;
        }
    }

/**
 * 获取物品的附魔能力值
 * 这个方法重写了父类的方法，返回一个整数值表示物品的附魔能力
 * 附魔能力值越高，物品越有可能获得更好的附魔效果
 *
 * @return 返回物品的附魔能力值，这里返回15
 */
    @Override
    public int getEnchantability() {
        return 15; // 返回固定的附魔能力值15
    }

/**
 * 检查物品堆栈是否可以附加特定的附魔
 * @param stack 要检查的物品堆栈
 * @param enchantment 要附加的附魔条目
 * @param context 附魔上下文
 * @return 如果可以附加返回true，否则返回false
 */
    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
    // 检查物品堆栈不为空且附魔是否为火焰祝福
        if (!stack.isEmpty() && enchantment.matchesKey(ModEnchantments.FIRE_BLESSING)) {
        // 再次检查附魔是否匹配火焰祝福（此行代码似乎无实际作用）
            enchantment.matchesKey(ModEnchantments.FIRE_BLESSING);
            return true; // 如果条件满足，返回true表示可以附加
        } else {
            return false; // 否则返回false表示不能附加
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user instanceof PlayerEntity player && world.isClient) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,1200,4,false,false,false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 300,4,false,true,true));
            return TypedActionResult.success(player.getStackInHand(hand));
        } else {
            user.sendMessage(Text.literal("似乎并没有正常执行。").formatted(Formatting.RED,Formatting.BOLD),false);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("§c§l火焰之剑").formatted(Formatting.YELLOW, Formatting.BOLD));
        tooltip.add(Text.translatable("tooltip.ability_sword.display1").formatted(Formatting.WHITE)
                .append(Text.translatable("key.use_ability.item",Text.keybind(ModKeys.USE_ABILITY_KEY.getBoundKeyLocalizedText().getString())
                                .formatted(Formatting.GOLD))
                        .append(Text.translatable("tooltip.ability_sword.display2").formatted(Formatting.WHITE))
                ));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("§7§l火焰之剑，拥有火焰之威，").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("§7§l能够点燃敌人，并给予使用者速度和防火效果。").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("§7§l手持该武器时，能够获得饱腹感和防火效果。").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("§7§l使用能力键时，能够点燃敌人，并给予使用者跳跃和生命效果。").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("专属特制武器").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("[稀有度]").append(Text.literal("传说").formatted(Formatting.GOLD,Formatting.BOLD)));
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1,attacker, EquipmentSlot.MAINHAND);
        stack.damage(1,attacker, EquipmentSlot.OFFHAND);
        super.postDamageEntity(stack, target, attacker);
    }
}

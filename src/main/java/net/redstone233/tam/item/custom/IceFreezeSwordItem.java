package net.redstone233.tam.item.custom;

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
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.redstone233.tam.core.keys.ModKeys;

import java.util.List;

public class IceFreezeSwordItem extends SwordItem {
    public IceFreezeSwordItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings
                .attributeModifiers(
                        createAttributeModifiers(ToolMaterials.NETHERITE, 500, 3.7f)
                ));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof LivingEntity livingEntity && attacker instanceof PlayerEntity player) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,300,255,true,true,true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,1200,4,false,false,false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,1200,4,false,false,false));
        }
        return true;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user instanceof PlayerEntity player && entity instanceof LivingEntity target) {
            if (ModKeys.isUseAbilityKeyPressed()) {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,6000,255,true,true,true));
            }
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST,3600,4));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST,1800,6));
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.FAIL;
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
//        tooltip.add(Text.literal("§b§l冰霜之剑").formatted(Formatting.YELLOW, Formatting.BOLD));
        tooltip.add(Text.translatable("tooltip.ability_sword.display1").formatted(Formatting.WHITE)
                .append(Text.translatable("key.use_ability.item",Text.keybind(ModKeys.USE_ABILITY_KEY.getBoundKeyLocalizedText().getString())
                                .formatted(Formatting.GOLD))
                        .append(Text.translatable("tooltip.ability_sword.display2").formatted(Formatting.WHITE))
                ));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("§7§l冰霜之剑，拥有冰霜之威，").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("§7§l能够冻结敌人，并给予使用者速度和防火效果。").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("§7§l使用时，给予使用者跳跃和生命效果。").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("专属特制武器").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("[稀有度]").append(Text.literal("  传说").formatted(Formatting.GOLD,Formatting.BOLD)));
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1,attacker, EquipmentSlot.MAINHAND);
        stack.damage(1,attacker, EquipmentSlot.OFFHAND);
        super.postDamageEntity(stack, target, attacker);
    }
}

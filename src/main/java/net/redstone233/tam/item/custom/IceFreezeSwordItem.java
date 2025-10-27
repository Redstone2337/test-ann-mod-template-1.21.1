package net.redstone233.tam.item.custom;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.redstone233.tam.core.keys.ModKeys;
import net.redstone233.tam.enchantment.ModEnchantments;

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
    public int getEnchantability() {
        return 15;
    }

    /**
     * 检查物品是否可以附魔寒冰祝福
     * @param stack 要检查的物品堆
     * @param enchantment 要附魔的附魔类型
     * @param context 附魔上下文
     * @return 如果物品可以附魔寒冰祝福则返回true，否则返回false
     */
    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        // 检查物品堆不为空且附魔类型为寒冰祝福
        if (!stack.isEmpty() && enchantment.matchesKey(ModEnchantments.FROST_BLESSING)) {
            // 再次检查附魔类型是否为寒冰祝福（这行代码似乎没有实际作用）
            enchantment.matchesKey(ModEnchantments.FROST_BLESSING);
            return true; // 允许附魔
        } else {
            return false; // 不允许附魔
        }
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();

        if (user instanceof PlayerEntity player && entity instanceof LivingEntity target) {
            if (ModKeys.isUseAbilityKeyPressed()) {
                // 冻结生物30秒
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 600, 255, true, true, true));

                // 生成3x3x3冰块结构
                if (!world.isClient) {
                    createIcePrison(world, target);
                }

                // 给玩家添加效果
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 3600, 4));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1800, 6));

                // 消耗耐久度
                stack.damage(5, user, EquipmentSlot.MAINHAND);

                return ActionResult.SUCCESS;
            } else {
                // 如果没有按下能力键，执行原来的效果
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 6000, 255, true, true, true));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 3600, 4));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1800, 6));
                return ActionResult.SUCCESS;
            }
        } else {
            return ActionResult.FAIL;
        }
    }

    /**
     * 创建3x3x3的冰块监狱困住生物
     * @param world 世界
     * @param target 目标生物
     */
    private void createIcePrison(World world, LivingEntity target) {
        BlockPos centerPos = target.getBlockPos();
        BlockState iceState = Blocks.ICE.getDefaultState();
        BlockState packedIceState = Blocks.PACKED_ICE.getDefaultState();

        // 防止生物被挤出，先将生物传送到安全位置
        Vec3d safePos = new Vec3d(centerPos.getX() + 0.5, centerPos.getY(), centerPos.getZ() + 0.5);
        target.teleport(safePos.getX(), safePos.getY(), safePos.getZ(), false);

        // 生成3x3x3的冰块结构
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos currentPos = centerPos.add(x, y, z);

                    // 检查位置是否可替换（空气、水、可替换的植物等）
                    if (world.getBlockState(currentPos).getBlock() == Blocks.AIR ||
                            world.getBlockState(currentPos).getBlock() == Blocks.WATER ||
                            world.getBlockState(currentPos).getBlock() == Blocks.SNOW ||
                            world.getBlockState(currentPos).isReplaceable()) {

                        // 外层使用普通冰，内层使用 packed ice 增加强度
                        if (x == 0 && y == 0 && z == 0) {
                            // 中心位置保持为空，让生物在里面
                            continue;
                        } else if (Math.abs(x) == 1 || Math.abs(y) == 1 || Math.abs(z) == 1) {
                            // 外层使用 packed ice
                            world.setBlockState(currentPos, packedIceState);
                        } else {
                            // 内层使用普通冰
                            world.setBlockState(currentPos, iceState);
                        }
                    }
                }
            }
        }

        // 设置30秒后自动融化冰块
        if (!world.isClient) {
            world.scheduleBlockTick(centerPos, Blocks.ICE, 600); // 30秒 = 600 ticks
            world.scheduleBlockTick(centerPos, Blocks.PACKED_ICE, 600);
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
        tooltip.add(Text.translatable("tooltip.ability_sword.display1").formatted(Formatting.WHITE)
                .append(Text.translatable("key.use_ability.item",Text.keybind(ModKeys.USE_ABILITY_KEY.getBoundKeyLocalizedText().getString())
                                .formatted(Formatting.GOLD))
                        .append(Text.translatable("tooltip.ability_sword.display2").formatted(Formatting.WHITE))
                ));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("§7§l冰霜之剑，拥有冰霜之威，").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("§7§l能够冻结敌人，并给予使用者速度和防火效果。").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("§7§l使用时，给予使用者跳跃和生命效果。").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("§7§l右键生物可生成3x3x3冰块监狱困住敌人。").formatted(Formatting.AQUA));
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
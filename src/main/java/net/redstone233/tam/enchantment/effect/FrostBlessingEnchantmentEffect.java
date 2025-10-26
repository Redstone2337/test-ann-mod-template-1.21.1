package net.redstone233.tam.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record FrostBlessingEnchantmentEffect(EnchantmentLevelBasedValue duration)
        implements EnchantmentEntityEffect {

    public static final MapCodec<FrostBlessingEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    EnchantmentLevelBasedValue.CODEC.fieldOf("duration").forGetter(FrostBlessingEnchantmentEffect::duration)
            ).apply(instance, FrostBlessingEnchantmentEffect::new)
    );

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (context.owner() instanceof LivingEntity target) {
            // 计算冻结时间：每级增加2秒，从4秒开始
            int durationTicks = (int) (this.duration.getValue(level) * 20); // 转换为tick

            // 添加缓慢效果模拟冻结
            target.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    durationTicks,
                    Math.min(level, 3), // 最高3级减速
                    false,
                    true,
                    true
            ));

            // 添加挖掘疲劳效果
            target.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.MINING_FATIGUE,
                    durationTicks,
                    Math.min(level - 1, 2), // 挖掘疲劳等级
                    false,
                    true,
                    true
            ));

            // 如果是玩家，添加视觉冻结效果
            if (target.isPlayer()) {
                target.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.BLINDNESS,
                        Math.min(durationTicks / 2, 100), // 最多5秒失明
                        0,
                        false,
                        true,
                        true
                ));
            }

            // 生成冰冻粒子效果
            spawnFrostParticles(target);
        }
    }

    private void spawnFrostParticles(Entity entity) {
        // 这里可以添加自定义的冰冻粒子效果
        // 暂时使用原版效果
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    net.minecraft.particle.ParticleTypes.SNOWFLAKE,
                    entity.getX(),
                    entity.getY() + entity.getHeight() / 2,
                    entity.getZ(),
                    10,
                    entity.getWidth() / 2,
                    entity.getHeight() / 2,
                    entity.getWidth() / 2,
                    0.1
            );
        }
    }

    @Override
    public MapCodec<FrostBlessingEnchantmentEffect> getCodec() {
        return CODEC;
    }
}
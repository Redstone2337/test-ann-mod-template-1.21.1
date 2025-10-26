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

public record FireBlessingEnchantmentEffect(EnchantmentLevelBasedValue duration)
        implements EnchantmentEntityEffect {

    public static final MapCodec<FireBlessingEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    EnchantmentLevelBasedValue.CODEC.fieldOf("duration").forGetter(FireBlessingEnchantmentEffect::duration)
            ).apply(instance, FireBlessingEnchantmentEffect::new)
    );

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (context.owner() instanceof LivingEntity target) {
            // 计算燃烧时间：每级增加3秒，从5秒开始
            int durationTicks = (int) (this.duration.getValue(level) * 20); // 转换为tick

            // 点燃目标
            target.setOnFireFor(durationTicks / 20); // 转换为秒

            // 添加火焰附加伤害效果
            target.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.WEAKNESS,
                    durationTicks / 2,
                    Math.min(level - 1, 1), // 最高1级虚弱
                    false,
                    true,
                    true
            ));

            // 生成火焰粒子效果
            spawnFireParticles(target);
        }
    }

    private void spawnFireParticles(Entity entity) {
        // 生成火焰粒子效果
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    net.minecraft.particle.ParticleTypes.FLAME,
                    entity.getX(),
                    entity.getY() + entity.getHeight() / 2,
                    entity.getZ(),
                    15,
                    entity.getWidth() / 2,
                    entity.getHeight() / 2,
                    entity.getWidth() / 2,
                    0.05
            );

            serverWorld.spawnParticles(
                    net.minecraft.particle.ParticleTypes.SMOKE,
                    entity.getX(),
                    entity.getY() + entity.getHeight() / 2,
                    entity.getZ(),
                    8,
                    entity.getWidth() / 2,
                    entity.getHeight() / 2,
                    entity.getWidth() / 2,
                    0.02
            );
        }
    }

    @Override
    public MapCodec<FireBlessingEnchantmentEffect> getCodec() {
        return CODEC;
    }
}
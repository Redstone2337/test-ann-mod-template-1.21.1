package net.redstone233.tam.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record VampirismEnchantmentEffect(EnchantmentLevelBasedValue baseEfficiency,
                                         EnchantmentLevelBasedValue levelBonus)
        implements EnchantmentEntityEffect {

    public static final MapCodec<VampirismEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    EnchantmentLevelBasedValue.CODEC.fieldOf("base_efficiency").forGetter(VampirismEnchantmentEffect::baseEfficiency),
                    EnchantmentLevelBasedValue.CODEC.fieldOf("level_bonus").forGetter(VampirismEnchantmentEffect::levelBonus)
            ).apply(instance, VampirismEnchantmentEffect::new)
    );

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (user instanceof LivingEntity attacker && context.owner() instanceof LivingEntity target) {
            // 计算吸血效率：基础20% + 每级10%
            float efficiency = this.baseEfficiency.getValue(level) + (this.levelBonus.getValue(level) * (level - 1));

            // 获取目标的当前血量
            float currentHealth = target.getHealth();
            float maxHealth = target.getMaxHealth();

            // 基于目标最大生命值的百分比来计算吸血量
            // 这样可以避免需要知道具体造成了多少伤害
            float healthToHeal = maxHealth * 0.05f * efficiency * 5; // 调整系数以获得合适的治疗量

            // 确保至少恢复一定量的生命值
            float minHeal = 1.0f + (level * 0.5f); // 1级至少恢复1点，每级增加0.5
            if (healthToHeal < minHeal) {
                healthToHeal = minHeal;
            }

            // 限制最大治疗量
            float maxHeal = 3.0f + (level * 1.0f); // 1级最多恢复3点，每级增加1点
            if (healthToHeal > maxHeal) {
                healthToHeal = maxHeal;
            }

            // 治疗攻击者
            attacker.heal(healthToHeal);

            // 生成治疗粒子效果
            spawnHealParticles(attacker);
        }
    }

    private void spawnHealParticles(LivingEntity entity) {
        // 生成心形粒子效果
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    net.minecraft.particle.ParticleTypes.HEART,
                    entity.getX(),
                    entity.getY() + entity.getHeight() / 2,
                    entity.getZ(),
                    3 + (int)(entity.getHealth() / 4), // 根据血量调整粒子数量
                    entity.getWidth() / 2,
                    entity.getHeight() / 2,
                    entity.getWidth() / 2,
                    0.1
            );
        }
    }

    @Override
    public MapCodec<VampirismEnchantmentEffect> getCodec() {
        return CODEC;
    }
}
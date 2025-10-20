// SuperFurnaceScene.java
package net.redstone233.tam.ponder;

import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.redstone233.tam.TestAnnMod;

/**
 * 超级熔炉场景类
 * 用于展示超级熔炉的建造过程和效果
 */
public class SuperFurnaceScene {

    /**
     * 构建超级熔炉场景
     * @param scene 场景构建器
     * @param util 场景构建工具类
     */
    public static void superFurnace(SceneBuilder scene, SceneBuildingUtil util) {
        // 设置场景标题
        scene.title("super_furnace", "超级熔炉建造指南");
        // 配置5x5的基础底板，原点为0,0,0
        scene.configureBasePlate(0, 0, 5);
        // 显示底板
        scene.showBasePlate();

        // 显示场景标题文本
        scene.overlay().showText(80)
                .colored(PonderPalette.WHITE)
                .text("超级熔炉：大幅提升熔炼效率的特殊结构")
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(90);

        // 第1层建造过程（从下至上开始）
        scene.overlay().showText(60)
                .colored(PonderPalette.BLUE)
                .text("开始建造第1层：3x3石头基底")
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(70);

        // 逐步显示第1层 (位于5x5底座中央的3x3区域)
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                // 显示方块
                scene.world().showSection(util.select().position(x, 1, z), Direction.UP);
                // 生成粒子效果
                Vec3d pos = util.vector().centerOf(x, 1, z);
                scene.effects().emitParticles(
                        pos,
                        scene.effects().simpleParticleEmitter(ParticleTypes.CRIT, new Vec3d(0, 0.1, 0)),
                        2, 1
                );
                scene.idle(2);
            }
        }
        scene.idle(20);

        // 第2层建造过程
        scene.overlay().showText(60)
                .colored(PonderPalette.GREEN)
                .text("建造第2层：注意中间位置")
                .pointAt(util.vector().centerOf(2, 2, 2));
        scene.idle(70);

        // 先显示周围的石头
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                if (!(x == 2 && z == 2)) { // 排除中间位置
                    // 显示方块
                    scene.world().showSection(util.select().position(x, 2, z), Direction.UP);
                    // 生成粒子效果
                    Vec3d pos = util.vector().centerOf(x, 2, z);
                    scene.effects().emitParticles(
                            pos,
                            scene.effects().simpleParticleEmitter(ParticleTypes.CRIT, new Vec3d(0, 0.1, 0)),
                            2, 1
                    );
                    scene.idle(1);
                }
            }
        }
        scene.idle(10);

        // 高亮显示中间的熔炉位置
        scene.overlay().showText(50)
                .colored(PonderPalette.RED)
                .attachKeyFrame()
                .text("中间位置放置熔炉")
                .pointAt(util.vector().centerOf(2, 2, 2));
        scene.idle(60);

        // 显示熔炉并添加特效
        Vec3d furnacePos = util.vector().centerOf(2, 2, 2);
        scene.effects().emitParticles(
                furnacePos,
                scene.effects().simpleParticleEmitter(ParticleTypes.FLAME, new Vec3d(0, 0.2, 0)),
                8, 15
        );
        scene.world().showSection(util.select().position(2, 2, 2), Direction.UP);
        scene.idle(20);

        // 第3层建造过程
        scene.overlay().showText(60)
                .colored(PonderPalette.BLUE)
                .text("建造第3层：3x3石头顶盖")
                .pointAt(util.vector().centerOf(2, 3, 2));
        scene.idle(70);

        // 逐步显示第3层
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                // 显示方块
                scene.world().showSection(util.select().position(x, 3, z), Direction.UP);
                // 生成粒子效果
                Vec3d pos = util.vector().centerOf(x, 3, z);
                scene.effects().emitParticles(
                        pos,
                        scene.effects().simpleParticleEmitter(ParticleTypes.CLOUD, new Vec3d(0, 0.1, 0)),
                        2, 1
                );
                scene.idle(2);
            }
        }
        scene.idle(20);

        // 显示整个建筑结构
        scene.world().showSection(util.select().fromTo(1, 1, 1, 3, 3, 3), Direction.UP);

        // 最终展示
        scene.overlay().showText(100)
                .colored(PonderPalette.FAST)
                .attachKeyFrame()
                .text("完成！超级熔炉可以大幅提升熔炼速度")
                .pointAt(util.vector().centerOf(2, 2, 2));

        // 完成时的粒子环绕效果
        Vec3d center = util.vector().centerOf(2, 2, 2);
        for (int i = 0; i < 8; i++) {
            // 计算粒子位置
            double angle = i * Math.PI / 4;
            Vec3d offset = new Vec3d(
                    Math.cos(angle) * 1.5,
                    Math.sin(angle) * 0.5,
                    Math.sin(angle) * 1.5
            );
            // 生成环绕粒子效果
            scene.effects().emitParticles(
                    center.add(offset),
                    scene.effects().simpleParticleEmitter(
                            ParticleTypes.FLAME,
                            offset.multiply(-0.05)
                    ),
                    2, 5
            );
        }

        // 旋转展示完整结构
        scene.rotateCameraY(180);
        scene.idle(40);
        scene.rotateCameraY(180);
        scene.idle(40);

        // 标记场景完成
        scene.markAsFinished();
    }

    /**
     * 初始化方法
     * 用于注册超级熔炉场景
     */
    public static void init() {
        TestAnnMod.LOGGER.info("Registering SuperFurnaceScene...");
    }
}
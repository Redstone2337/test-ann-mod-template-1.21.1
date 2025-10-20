// SuperSmokerScene.java
package net.redstone233.tam.ponder;

import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.redstone233.tam.TestAnnMod;

/**
 * 超级烟熏炉场景类
 * 用于演示如何建造超级烟熏炉的结构和功能
 */
public class SuperSmokerScene {

    /**
     * 构建超级烟熏炉的场景演示
     * @param scene 场景构建器
     * @param util 场景构建工具类
     */
    public static void superSmoker(SceneBuilder scene, SceneBuildingUtil util) {
        // 设置场景标题和基础底板
        scene.title("super_smoker", "超级烟熏炉建造指南");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        // 显示介绍文本
        scene.overlay().showText(80)
                .colored(PonderPalette.WHITE)
                .text("超级烟熏炉：快速熏制食物的特殊结构")
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(90);

        // 第3层：与第1层对称（从上至下开始）
        scene.overlay().showText(60)
                .colored(PonderPalette.BLUE)
                .text("第3层：与第1层完全对称")
                .pointAt(util.vector().centerOf(2, 3, 2));
        scene.idle(70);

        // 快速显示第3层并添加粒子效果
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                scene.world().showSection(util.select().position(x, 3, z), Direction.UP);
                Vec3d pos = util.vector().centerOf(x, 3, z);
                scene.effects().emitParticles(
                        pos,
                        scene.effects().simpleParticleEmitter(ParticleTypes.CLOUD, new Vec3d(0, 0.1, 0)),
                        1, 1
                );
            }
        }
        scene.idle(30);

        // 第2层建造说明
        scene.overlay().showText(60)
                .colored(PonderPalette.GREEN)
                .text("第2层：保持十字结构，中心放置烟熏炉")
                .pointAt(util.vector().centerOf(2, 2, 2));
        scene.idle(70);

        // 显示第2层
        // 煤炭块位置
        BlockPos[] coalPositions = {
                util.grid().at(1,2,1), util.grid().at(1,2,3),
                util.grid().at(3,2,1), util.grid().at(3,2,3)
        };

        for (BlockPos pos : coalPositions) {
            scene.world().showSection(util.select().position(pos), Direction.UP);
            Vec3d centerPos = util.vector().centerOf(pos);
            scene.effects().emitParticles(
                    centerPos,
                    scene.effects().simpleParticleEmitter(ParticleTypes.SMOKE, new Vec3d(0, 0.1, 0)),
                    2, 1
            );
            scene.idle(2);
        }
        scene.idle(5);

        // 木头位置（排除中心位置）
        BlockPos[] woodPositions = {
                util.grid().at(2,2,1), util.grid().at(1,2,2),
                util.grid().at(2,2,3), util.grid().at(3,2,2)
        };

        for (BlockPos pos : woodPositions) {
            scene.world().showSection(util.select().position(pos), Direction.UP);
            Vec3d centerPos = util.vector().centerOf(pos);
            scene.effects().emitParticles(
                    centerPos,
                    scene.effects().simpleParticleEmitter(ParticleTypes.CAMPFIRE_COSY_SMOKE, new Vec3d(0, 0.1, 0)),
                    2, 1
            );
            scene.idle(2);
        }
        scene.idle(5);

        // 高亮烟熏炉位置
        scene.overlay().showText(50)
                .colored(PonderPalette.RED)
                .attachKeyFrame()
                .text("中心放置烟熏炉")
                .pointAt(util.vector().centerOf(2, 2, 2));
        scene.idle(60);

        // 显示烟熏炉并添加特效
        Vec3d smokerPos = util.vector().centerOf(2, 2, 2);
        scene.effects().emitParticles(
                smokerPos,
                scene.effects().simpleParticleEmitter(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, new Vec3d(0, 0.2, 0)),
                10, 20
        );
        scene.world().showSection(util.select().position(2, 2, 2), Direction.UP);
        scene.idle(20);

        // 第1层：十字形结构说明
        scene.overlay().showText(70)
                .colored(PonderPalette.BLUE)
                .text("第1层：煤炭块与木头十字交错排列")
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(80);

        // 显示第1层十字结构 (位于5x5底座中央的3x3区域)
        // 先显示四个角的煤炭块
        BlockPos[] coalPositions1 = {
                util.grid().at(1,1,1), util.grid().at(1,1,3),
                util.grid().at(3,1,1), util.grid().at(3,1,3)
        };

        // 遍历并显示所有煤炭块位置，添加烟雾粒子效果
        for (BlockPos pos : coalPositions1) {
            scene.world().showSection(util.select().position(pos), Direction.UP);
            Vec3d centerPos = util.vector().centerOf(pos);
            scene.effects().emitParticles(
                    centerPos,
                    scene.effects().simpleParticleEmitter(ParticleTypes.SMOKE, new Vec3d(0, 0.1, 0)),
                    3, 2
            );
            scene.idle(5);
        }
        scene.idle(10);

        // 显示十字形的木头
        BlockPos[] woodPositions1 = {
                util.grid().at(2,1,1), util.grid().at(1,1,2),
                util.grid().at(2,1,3), util.grid().at(3,1,2), util.grid().at(2,1,2)
        };

        // 遍历并显示所有木头位置，添加烟雾粒子效果
        for (BlockPos pos : woodPositions1) {
            scene.world().showSection(util.select().position(pos), Direction.UP);
            Vec3d centerPos = util.vector().centerOf(pos);
            scene.effects().emitParticles(
                    centerPos,
                    scene.effects().simpleParticleEmitter(ParticleTypes.CAMPFIRE_COSY_SMOKE, new Vec3d(0, 0.1, 0)),
                    2, 1
            );
            scene.idle(3);
        }
        scene.idle(20);

        // 显示整个建筑结构
        scene.world().showSection(util.select().fromTo(1, 1, 1, 3, 3, 3), Direction.UP);

        // 最终效果展示
        scene.overlay().showText(100)
                .colored(PonderPalette.FAST)
                .attachKeyFrame()
                .text("完成！超级烟熏炉大幅提升食物熏制速度")
                .pointAt(util.vector().centerOf(2, 2, 2));

        // 完成时的粒子环绕效果
        Vec3d center = util.vector().centerOf(2, 2, 2);
        for (int i = 0; i < 12; i++) {
            double angle = i * Math.PI / 6;
            Vec3d offset = new Vec3d(
                    Math.cos(angle) * 1.5,
                    Math.sin(angle) * 0.5,
                    Math.sin(angle) * 1.5
            );
            scene.effects().emitParticles(
                    center.add(offset),
                    scene.effects().simpleParticleEmitter(
                            ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                            offset.multiply(-0.03)
                    ),
                    2, 8
            );
        }

        // 俯视角度展示十字结构
        scene.rotateCameraY(45);
        scene.idle(20);
        scene.rotateCameraY(45);
        scene.idle(20);
        scene.rotateCameraY(45);
        scene.idle(20);
        scene.rotateCameraY(45);
        scene.idle(20);

        scene.markAsFinished();
    }

    /**
     * 初始化方法
     * 用于注册超级烟熏炉场景
     */
    public static void init() {
        TestAnnMod.LOGGER.info("Registering SuperSmokerScene...");
    }
}
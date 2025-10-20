// SuperBlastFurnaceScene.java
package net.redstone233.tam.ponder;

import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.redstone233.tam.TestAnnMod;

/**
 * 超级高炉场景类，用于展示超级高炉的建造过程
 */
public class SuperBlastFurnaceScene {

    /**
     * 构建超级高炉场景的指导方法
     * @param scene 场景构建器对象
     * @param util 场景构建工具对象
     */
    public static void superBlastFurnace(SceneBuilder scene, SceneBuildingUtil util) {
        // 设置场景标题
        scene.title("super_blast_furnace", "超级高炉建造指南");
        // 配置5x5的底座，原点为0,0,0
        scene.configureBasePlate(0, 0, 5);
        // 显示底座
        scene.showBasePlate();

        // 显示标题文本和说明
        scene.overlay().showText(80)
                .colored(PonderPalette.WHITE)
                .text("超级高炉：高效冶炼矿石的特殊结构")
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(90);

        // 第3层：平滑石顶盖（从上至下开始）
        scene.overlay().showText(60)
                .colored(PonderPalette.BLUE)
                .text("第3层：3x3平滑石顶盖")
                .pointAt(util.vector().centerOf(2, 3, 2));
        scene.idle(70);

        // 显示第3层并添加粒子效果（从上至下）
        // 建筑从(1,1,1)到(3,3,3)的3x3x3区域
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                scene.world().showSection(util.select().position(x, 3, z), Direction.UP);
                Vec3d pos = util.vector().centerOf(x, 3, z);
                scene.effects().emitParticles(
                        pos,
                        scene.effects().simpleParticleEmitter(ParticleTypes.CLOUD, new Vec3d(0, 0.1, 0)),
                        2, 1
                );
                scene.idle(1);
            }
        }
        scene.idle(20);

        // 第2层建造过程
        scene.overlay().showText(60)
                .colored(PonderPalette.GREEN)
                .text("第2层：铁块包围高炉")
                .pointAt(util.vector().centerOf(2, 2, 2));
        scene.idle(70);

        // 显示第2层周围的铁块
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                // 跳过中心位置（用于放置高炉）
                if (!(x == 2 && z == 2)) {
                    scene.world().showSection(util.select().position(x, 2, z), Direction.UP);
                    Vec3d pos = util.vector().centerOf(x, 2, z);
                    scene.effects().emitParticles(
                            pos,
                            scene.effects().simpleParticleEmitter(ParticleTypes.ELECTRIC_SPARK, new Vec3d(0, 0.1, 0)),
                            2, 1
                    );
                    scene.idle(1);
                }
            }
        }
        scene.idle(10);

        // 高亮高炉位置
        scene.overlay().showText(50)
                .colored(PonderPalette.RED)
                .attachKeyFrame()
                .text("中心放置高炉")
                .pointAt(util.vector().centerOf(2, 2, 2));
        scene.idle(60);

        // 显示高炉并添加特效
        Vec3d furnacePos = util.vector().centerOf(2, 2, 2);
        scene.effects().emitParticles(
                furnacePos,
                scene.effects().simpleParticleEmitter(ParticleTypes.FLAME, new Vec3d(0, 0.2, 0)),
                10, 20
        );
        scene.world().showSection(util.select().position(2, 2, 2), Direction.UP);
        scene.idle(20);

        // 第1层：铁块基底
        scene.overlay().showText(60)
                .colored(PonderPalette.BLUE)
                .text("第1层：3x3铁块基底")
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(70);

        // 显示第1层铁块 (位于5x5底座中央的3x3区域)
        for (int x = 1; x <= 3; x++) {
            for (int z = 1; z <= 3; z++) {
                // 显示铁块位置
                scene.world().showSection(util.select().position(x, 1, z), Direction.UP);
                // 添加粒子效果
                Vec3d pos = util.vector().centerOf(x, 1, z);
                scene.effects().emitParticles(
                        pos,
                        scene.effects().simpleParticleEmitter(ParticleTypes.ELECTRIC_SPARK, new Vec3d(0, 0.1, 0)),
                        3, 2
                );
                scene.idle(1);
            }
        }
        scene.idle(20);

        // 结构完成特效
        scene.overlay().showText(80)
                .colored(PonderPalette.FAST)
                .attachKeyFrame()
                .text("与普通高炉对比，超级高炉效率更高")
                .pointAt(util.vector().centerOf(2, 2, 2));

        // 显示整个建筑结构
        scene.world().showSection(util.select().fromTo(1, 1, 1, 3, 3, 3), Direction.UP);

        // 完成时的粒子环绕效果
        Vec3d center = util.vector().centerOf(2, 2, 2);
        for (int i = 0; i < 12; i++) {
            // 计算环绕粒子的位置
            double angle = i * Math.PI / 6;
            Vec3d offset = new Vec3d(
                    Math.cos(angle) * 1.5,
                    Math.sin(angle) * 0.5,
                    Math.sin(angle) * 1.5
            );
            scene.effects().emitParticles(
                    center.add(offset),
                    scene.effects().simpleParticleEmitter(
                            ParticleTypes.FLAME,
                            offset.multiply(-0.05)
                    ),
                    3, 5
            );
        }

        // 旋转展示
        scene.rotateCameraY(90);
        scene.idle(30);
        scene.rotateCameraY(90);
        scene.idle(30);
        scene.rotateCameraY(90);
        scene.idle(30);
        scene.rotateCameraY(90);
        scene.idle(30);

        // 标记场景完成
        scene.markAsFinished();
    }

    /**
     * 初始化方法，用于注册超级高炉场景
     */
    public static void init() {
        TestAnnMod.LOGGER.info("Registering Super Blast Furnace Scene...");
    }
}
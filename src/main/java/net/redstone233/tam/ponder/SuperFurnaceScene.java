package net.redstone233.tam.ponder;

import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.util.math.Direction;
import net.redstone233.tam.TestAnnMod;

public class SuperFurnaceScene {

    public static void superFurnace(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("super_furnace", "超级熔炉建造指南");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        scene.overlay().showText(80)
                .text("超级熔炉：大幅提升熔炼效率的特殊结构")
                .pointAt(util.vector().centerOf(3, 1, 3));
        scene.idle(90);

        // 第1层建造过程
        scene.overlay().showText(60)
                .text("开始建造第1层：3x3石头基底")
                .pointAt(util.vector().centerOf(3, 1, 3));
        scene.idle(70);

        // 逐步显示第1层 (位于5x5底座中央的3x3区域)
        for (int x = 2; x <= 4; x++) {
            for (int z = 2; z <= 4; z++) {
                scene.world().showSection(util.select().position(x, 1, z), Direction.DOWN);
                scene.idle(2);
            }
        }
        scene.idle(20);

        // 第2层建造过程
        scene.overlay().showText(60)
                .text("建造第2层：注意中间位置")
                .pointAt(util.vector().centerOf(3, 2, 3));
        scene.idle(70);

        // 先显示周围的石头
        for (int x = 2; x <= 4; x++) {
            for (int z = 2; z <= 4; z++) {
                if (!(x == 3 && z == 3)) { // 排除中间位置
                    scene.world().showSection(util.select().position(x, 2, z), Direction.DOWN);
                    scene.idle(1);
                }
            }
        }
        scene.idle(10);

        // 高亮显示中间的熔炉位置
        scene.overlay().showText(50)
                .text("中间位置放置熔炉")
                .pointAt(util.vector().centerOf(3, 2, 3));
        scene.idle(60);

        // 显示熔炉（用不同方块表示）
        scene.world().showSection(util.select().position(3, 2, 3), Direction.DOWN);
        scene.idle(20);

        // 第3层建造过程
        scene.overlay().showText(60)
                .text("建造第3层：3x3石头顶盖")
                .pointAt(util.vector().centerOf(3, 3, 3));
        scene.idle(70);

        // 逐步显示第3层
        for (int x = 2; x <= 4; x++) {
            for (int z = 2; z <= 4; z++) {
                scene.world().showSection(util.select().position(x, 3, z), Direction.DOWN);
                scene.idle(2);
            }
        }

        scene.addKeyframe();
        scene.idle(20);

        // 最终展示
        scene.overlay().showText(100)
                .text("完成！超级熔炉可以大幅提升熔炼速度")
                .pointAt(util.vector().centerOf(3, 2, 3));

        // 旋转展示完整结构
        scene.rotateCameraY(180);
        scene.idle(40);
        scene.rotateCameraY(180);
        scene.idle(40);

        scene.markAsFinished();
    }

    public static void init() {
        TestAnnMod.LOGGER.info("Registering SuperFurnaceScene...");
    }
}
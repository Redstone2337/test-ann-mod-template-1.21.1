package net.redstone233.tam.ponder;

import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.util.math.Direction;
import net.redstone233.tam.TestAnnMod;

public class SuperSmokerScene {

    public static void superSmoker(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("super_smoker", "超级烟熏炉建造指南");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        scene.overlay().showText(80)
                .text("超级烟熏炉：快速熏制食物的特殊结构")
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(90);

        // 第1层：十字形结构说明
        scene.overlay().showText(70)
                .text("第1层：煤炭块与木头十字交错排列")
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(80);

        // 显示第1层十字结构
        // 先显示四个角的煤炭块
        scene.world().showSection(util.select().position(1, 1, 1), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(1, 1, 3), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(3, 1, 1), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(3, 1, 3), Direction.DOWN);
        scene.idle(10);

        // 显示十字形的木头
        scene.world().showSection(util.select().position(2, 1, 1), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().position(1, 1, 2), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().position(2, 1, 3), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().position(3, 1, 2), Direction.DOWN);
        scene.idle(3);
        scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
        scene.idle(20);

        // 第2层建造
        scene.overlay().showText(60)
                .text("第2层：保持十字结构，中心放置烟熏炉")
                .pointAt(util.vector().centerOf(2, 2, 2));
        scene.idle(70);

        // 显示第2层
        // 煤炭块位置
        scene.world().showSection(util.select().position(1, 2, 1), Direction.DOWN);
        scene.idle(2);
        scene.world().showSection(util.select().position(1, 2, 3), Direction.DOWN);
        scene.idle(2);
        scene.world().showSection(util.select().position(3, 2, 1), Direction.DOWN);
        scene.idle(2);
        scene.world().showSection(util.select().position(3, 2, 3), Direction.DOWN);
        scene.idle(5);

        // 木头位置
        scene.world().showSection(util.select().position(2, 2, 1), Direction.DOWN);
        scene.idle(2);
        scene.world().showSection(util.select().position(1, 2, 2), Direction.DOWN);
        scene.idle(2);
        scene.world().showSection(util.select().position(2, 2, 3), Direction.DOWN);
        scene.idle(2);
        scene.world().showSection(util.select().position(3, 2, 2), Direction.DOWN);
        scene.idle(5);

        // 高亮烟熏炉位置
        scene.overlay().showText(50)
                .text("中心放置烟熏炉")
                .pointAt(util.vector().centerOf(2, 2, 2));
        scene.idle(60);

        scene.world().showSection(util.select().position(2, 2, 2), Direction.DOWN);
        scene.idle(20);

        // 第3层：与第1层对称
        scene.overlay().showText(60)
                .text("第3层：与第1层完全对称")
                .pointAt(util.vector().centerOf(2, 3, 2));
        scene.idle(70);

        // 快速显示第3层
        scene.world().showSection(util.select().fromTo(1, 3, 1, 3, 3, 3), Direction.DOWN);
        scene.idle(30);

        scene.addKeyframe();
        scene.idle(20);

        // 最终效果展示
        scene.overlay().showText(100)
                .text("完成！超级烟熏炉大幅提升食物熏制速度")
                .pointAt(util.vector().centerOf(2, 2, 2));

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

    public static void init() {
        TestAnnMod.LOGGER.info("Registering SuperSmokerScene...");
    }
}

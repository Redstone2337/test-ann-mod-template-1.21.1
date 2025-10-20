package net.redstone233.tam.ponder;

import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.util.math.Direction;
import net.redstone233.tam.TestAnnMod;

public class SuperBlastFurnaceScene {

    public static void superBlastFurnace(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("super_blast_furnace", "超级高炉建造指南");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        scene.overlay().showText(80)
                .text("超级高炉：高效冶炼矿石的特殊结构")
                .pointAt(util.vector().centerOf(3, 1, 3));
        scene.idle(90);

        // 第1层：铁块基底
        scene.overlay().showText(60)
                .text("第1层：3x3铁块基底")
                .pointAt(util.vector().centerOf(3, 1, 3));
        scene.idle(70);

        // 显示第1层铁块 (位于5x5底座中央的3x3区域)
        scene.world().showSection(util.select().fromTo(2, 1, 2, 4, 1, 4), Direction.DOWN);
        scene.idle(30);

        // 第2层建造过程
        scene.overlay().showText(60)
                .text("第2层：铁块包围高炉")
                .pointAt(util.vector().centerOf(3, 2, 3));
        scene.idle(70);

        // 显示第2层周围的铁块
        for (int x = 2; x <= 4; x++) {
            for (int z = 2; z <= 4; z++) {
                if (!(x == 3 && z == 3)) {
                    scene.world().showSection(util.select().position(x, 2, z), Direction.DOWN);
                    scene.idle(1);
                }
            }
        }
        scene.idle(10);

        // 高亮高炉位置
        scene.overlay().showText(50)
                .text("中心放置高炉")
                .pointAt(util.vector().centerOf(3, 2, 3));
        scene.idle(60);

        scene.world().showSection(util.select().position(3, 2, 3), Direction.DOWN);
        scene.idle(20);

        // 第3层：平滑石顶盖
        scene.overlay().showText(60)
                .text("第3层：3x3平滑石顶盖")
                .pointAt(util.vector().centerOf(3, 3, 3));
        scene.idle(70);

        scene.world().showSection(util.select().fromTo(2, 3, 2, 4, 3, 4), Direction.DOWN);
        scene.idle(30);

        scene.addKeyframe();
        scene.idle(20);

        // 结构对比展示
        scene.overlay().showText(80)
                .text("与普通高炉对比，超级高炉效率更高")
                .pointAt(util.vector().centerOf(3, 2, 3));

        // 旋转展示
        scene.rotateCameraY(90);
        scene.idle(30);
        scene.rotateCameraY(90);
        scene.idle(30);
        scene.rotateCameraY(90);
        scene.idle(30);
        scene.rotateCameraY(90);
        scene.idle(30);

        scene.markAsFinished();
    }

    public static void init() {
        TestAnnMod.LOGGER.info("Registering Super Blast Furnace Scene...");
    }
}
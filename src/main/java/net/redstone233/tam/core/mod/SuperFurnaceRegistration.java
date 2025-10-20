package net.redstone233.tam.core.mod;

import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.util.Identifier;
import net.redstone233.tam.TestAnnMod;
import net.redstone233.tam.ponder.SuperBlastFurnaceScene;
import net.redstone233.tam.ponder.SuperFurnaceScene;
import net.redstone233.tam.ponder.SuperSmokerScene;

public class SuperFurnaceRegistration {
    public static void registerAll(PonderSceneRegistrationHelper<Identifier> helper) {
        // 注册超级熔炉场景
        helper.addStoryBoard(
                Identifier.ofVanilla("furnace"),
                "super_furnace/super_furnace_structure",
                SuperFurnaceScene::superFurnace
        ).highlightAllTags();

        // 注册超级高炉场景
        helper.addStoryBoard(
                Identifier.ofVanilla("blast_furnace"),
                "super_blast_furnace/super_blast_furnace_structure",
                SuperBlastFurnaceScene::superBlastFurnace
        ).highlightAllTags();

        // 注册超级烟熏炉场景
        helper.addStoryBoard(
                Identifier.ofVanilla("smoker"),
                "super_smoker/super_smoker_structure",
                SuperSmokerScene::superSmoker
        ).highlightAllTags();
    }

    public static void init() {
        TestAnnMod.LOGGER.info("Registering SuperFurnaceRegistration...");
    }
}

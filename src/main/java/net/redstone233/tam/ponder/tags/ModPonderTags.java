package net.redstone233.tam.ponder.tags;

import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.redstone233.tam.TestAnnMod;

public class ModPonderTags {
    public static void register(PonderTagRegistrationHelper<Identifier> helper) {
        helper.registerTag(Identifier.of(TestAnnMod.MOD_ID,"furnace"))
                .addToIndex()
                .item(Blocks.FURNACE,true,false)
                .title("高速熔炼系列")
                .description("这里是一些熔炉的建造场景")
                .register();
        helper.addToTag(Identifier.of(TestAnnMod.MOD_ID,"furnace"))
                .add(Identifier.ofVanilla("furnace"))
                .add(Identifier.ofVanilla("stone"))
                .add(Identifier.ofVanilla("blast_furnace"))
                .add(Identifier.ofVanilla("smoker"))
                .add(Identifier.ofVanilla("smooth_stone"))
                .add(Identifier.ofVanilla("iron_block"))
                .add(Identifier.ofVanilla("oak_log"));
    }

    public static void init() {
        TestAnnMod.LOGGER.info("Registering ModPonderTags...");
    }
}

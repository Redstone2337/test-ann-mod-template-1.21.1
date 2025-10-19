package net.redstone233.tam.core.mod;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.util.Identifier;
import net.redstone233.tam.TestAnnMod;
import net.redstone233.tam.ponder.tags.ModPonderTags;
import org.jetbrains.annotations.NotNull;

public class TestAnnModPonderPlugin implements PonderPlugin {
    @Override
    public @NotNull String getModId() {
        return TestAnnMod.MOD_ID;
    }

    @Override
    public void registerScenes(@NotNull PonderSceneRegistrationHelper<Identifier> helper) {
        SuperFurnaceRegistration.registerAll(helper);
    }

    @Override
    public void registerTags(@NotNull PonderTagRegistrationHelper<Identifier> helper) {
        ModPonderTags.register(helper);
    }
}

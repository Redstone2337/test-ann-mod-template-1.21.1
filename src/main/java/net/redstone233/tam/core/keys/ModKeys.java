package net.redstone233.tam.core.keys;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.redstone233.tam.TestAnnMod;
import org.lwjgl.glfw.GLFW;

public class ModKeys {
    public static KeyBinding ANNOUNCEMENT_KEY = new KeyBinding(
            "key.tam.open_config",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "category.tam"
    );

    public static void register() {
        KeyBindingHelper.registerKeyBinding(ANNOUNCEMENT_KEY);

        TestAnnMod.LOGGER.info("Registered keybindings");
    }


    public static boolean isAnnouncementKeyPressed() {
        return ANNOUNCEMENT_KEY.isPressed();
    }
}

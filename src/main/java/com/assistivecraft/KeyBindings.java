package com.assistivecraft;

import com.assistivecraft.gui.AssistiveGuiScreen;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static KeyBinding openGuiKey;

    public static void register() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.assistivecraft.opengui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_GRAVE_ACCENT, // Grave Accent (`) key
            "category.assistivecraft.general"
        ));
    }

    public static void checkKeys(MinecraftClient client) {
        while (openGuiKey.wasPressed()) {
            client.setScreen(new AssistiveGuiScreen());
        }
    }
}

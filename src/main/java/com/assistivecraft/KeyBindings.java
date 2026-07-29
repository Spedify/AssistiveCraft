package com.assistivecraft;

import com.assistivecraft.gui.AssistiveGuiScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static KeyBinding guiKey;
    public static KeyBinding eatKey;
    public static KeyBinding totemKey;

    public static void register() {
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.assistivecraft.gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_GRAVE_ACCENT,
                "category.assistivecraft.main"
        ));

        eatKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.assistivecraft.eat",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.assistivecraft.main"
        ));

        totemKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.assistivecraft.totem",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.assistivecraft.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (guiKey.wasPressed()) {
                client.setScreen(new AssistiveGuiScreen());
            }
        });
    }
}

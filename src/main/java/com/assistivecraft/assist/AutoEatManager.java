package com.assistivecraft.assist;

import com.assistivecraft.KeyBindings;
import com.assistivecraft.ModuleManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class AutoEatManager {
    private static int previousSlot = -1;
    private static boolean isEating = false;

    public static void initialize() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (!ModuleManager.eatShortcut) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            if (client.player.getHungerManager().getFoodLevel() <= ModuleManager.hungerThreshold) {
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();
                context.drawCenteredTextWithShadow(client.textRenderer, Text.literal("§eLOW HUNGER - PRESS " + KeyBindings.eatKey.getBoundKeyLocalizedText().getString()), width / 2, height - 50, 0xFFFF00);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!ModuleManager.eatShortcut || client.player == null) return;

            if (KeyBindings.eatKey.isPressed() && !isEating && client.player.getHungerManager().getFoodLevel() < 20) {
                PlayerInventory inv = client.player.getInventory();
                for (int i = 0; i < 9; i++) {
                    if (inv.getStack(i).isFood()) {
                        previousSlot = inv.selectedSlot;
                        inv.selectedSlot = i;
                        client.options.useKey.setPressed(true);
                        isEating = true;
                        break;
                    }
                }
            }

            if (isEating && (!KeyBindings.eatKey.isPressed() || client.player.getHungerManager().getFoodLevel() >= 20)) {
                client.options.useKey.setPressed(false);
                if (previousSlot != -1) {
                    client.player.getInventory().selectedSlot = previousSlot;
                    previousSlot = -1;
                }
                isEating = false;
            }
        });
    }
}

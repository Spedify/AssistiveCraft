package com.assistivecraft.assist;

import com.assistivecraft.ModuleManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class FallMitigationManager {
    public static void initialize() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (!ModuleManager.fallWarning) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            if (client.player.fallDistance > ModuleManager.fallThreshold) {
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();
                
                context.fill(0, 0, width, 10, 0x88FF0000);
                context.fill(0, height - 10, width, height, 0x88FF0000);
                context.fill(0, 0, 10, height, 0x88FF0000);
                context.fill(width - 10, 0, width, height, 0x88FF0000);
                
                context.drawCenteredTextWithShadow(client.textRenderer, Text.literal("§cWARNING: LETHAL FALL VELOCITY"), width / 2, height / 4, 0xFF0000);
            }
        });
    }
}

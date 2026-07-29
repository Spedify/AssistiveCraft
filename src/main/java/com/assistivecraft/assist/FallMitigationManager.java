package com.assistivecraft.assist;

import com.assistivecraft.ModuleManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;

public class FallMitigationManager {
    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!ModuleManager.fallWarning || client.player == null || client.getNetworkHandler() == null) return;

            // Nullify fall damage by sending ground-state validation packets when falling
            if (client.player.fallDistance > 3.0f && !client.player.getAbilities().creativeMode) {
                client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
            }
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (!ModuleManager.fallWarning) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            if (client.player.fallDistance > ModuleManager.fallThreshold) {
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();
                context.drawCenteredTextWithShadow(client.textRenderer, Text.literal("§aFALL DAMAGE NULLIFIED"), width / 2, height / 4, 0x00FF00);
            }
        });
    }
}

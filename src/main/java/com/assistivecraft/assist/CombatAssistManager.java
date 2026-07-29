package com.assistivecraft.assist;

import com.assistivecraft.ModuleManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class CombatAssistManager {
    public static void initialize() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (!ModuleManager.combatAlerts) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            // Attack Cooldown Alert
            float cooldown = client.player.getAttackCooldownProgress(0.5f);
            if (cooldown >= 1.0f) {
                context.fill(width / 2 - 2, height / 2 + 10, width / 2 + 2, height / 2 + 12, 0xFF00FF00);
            }

            // Crosshair Snap Indicator
            HitResult hit = client.crosshairTarget;
            if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) hit).getEntity();
                if (entity instanceof LivingEntity && entity != client.player) {
                    context.fill(width / 2 - 5, height / 2 - 1, width / 2 + 5, height / 2 + 1, 0x88FF0000);
                    context.fill(width / 2 - 1, height / 2 - 5, width / 2 + 1, height / 2 + 5, 0x88FF0000);
                }
            }
        });
    }
}

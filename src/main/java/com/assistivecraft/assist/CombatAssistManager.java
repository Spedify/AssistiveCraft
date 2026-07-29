package com.assistivecraft.assist;

import com.assistivecraft.ModuleManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class CombatAssistManager {
    private static final float SMOOTHNESS_FACTOR = 0.35f; // Lower = smoother tracking, Higher = faster snap

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!ModuleManager.combatAlerts || client.player == null || client.world == null || client.interactionManager == null) return;

            PlayerEntity closestTarget = null;
            double closestDistance = Double.MAX_VALUE;

            for (Entity entity : client.world.getEntities()) {
                if (entity == client.player || !(entity instanceof PlayerEntity) || !entity.isAlive()) continue;
                if (((PlayerEntity) entity).isSpectator() || ((PlayerEntity) entity).isCreative()) continue;

                double dist = client.player.squaredDistanceTo(entity);
                if (dist < 36.0 && dist < closestDistance) { // Within 6 blocks
                    closestDistance = dist;
                    closestTarget = (PlayerEntity) entity;
                }
            }

            if (closestTarget != null) {
                // Target angle calculation
                double dx = closestTarget.getX() - client.player.getX();
                double dy = (closestTarget.getY() + closestTarget.getEyeHeight(closestTarget.getPose())) - client.player.getEyeY();
                double dz = closestTarget.getZ() - client.player.getZ();
                double distXZ = Math.sqrt(dx * dx + dz * dz);

                float targetYaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
                float targetPitch = (float) (-Math.toDegrees(Math.atan2(dy, distXZ)));

                // Smooth interpolation to prevent violent mouse jolts
                float currentYaw = client.player.getYaw();
                float currentPitch = client.player.getPitch();

                float yawDiff = wrapDegrees(targetYaw - currentYaw);
                float pitchDiff = targetPitch - currentPitch;

                client.player.setYaw(currentYaw + (yawDiff * SMOOTHNESS_FACTOR));
                client.player.setPitch(currentPitch + (pitchDiff * SMOOTHNESS_FACTOR));

                // Auto attack when cooldown is ready
                if (client.player.getAttackCooldownProgress(0.5f) >= 1.0f) {
                    client.interactionManager.attackEntity(client.player, closestTarget);
                    client.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
                }
            }
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (!ModuleManager.combatAlerts) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            context.drawCenteredTextWithShadow(client.textRenderer, Text.literal("§cKILL-AURA ACTIVE"), width / 2, height - 70, 0xFF0000);
        });
    }

    private static float wrapDegrees(float degrees) {
        degrees %= 360.0f;
        if (degrees >= 180.0f) degrees -= 360.0f;
        if (degrees < -180.0f) degrees += 360.0f;
        return degrees;
    }
}

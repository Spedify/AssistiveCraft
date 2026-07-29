package com.assistivecraft.assist;

import com.assistivecraft.ModuleManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class CombatAssistManager {
    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!ModuleManager.combatAlerts || client.player == null || client.world == null || client.interactionManager == null) return;

            LivingEntity closestTarget = null;
            double closestDistance = Double.MAX_VALUE;

            for (Entity entity : client.world.getEntities()) {
                if (entity == client.player || !(entity instanceof LivingEntity) || !entity.isAlive()) continue;
                if (((LivingEntity) entity).isDead()) continue;

                double dist = client.player.squaredDistanceTo(entity);
                if (dist < 36.0 && dist < closestDistance) { // Within 6 blocks attack range
                    closestDistance = dist;
                    closestTarget = (LivingEntity) entity;
                }
            }

            if (closestTarget != null) {
                // Aim assist: lock client look angles directly onto target coordinates
                double dx = closestTarget.getX() - client.player.getX();
                double dy = (closestTarget.getY() + closestTarget.getEyeHeight(closestTarget.getPose())) - client.player.getEyeY();
                double dz = closestTarget.getZ() - client.player.getZ();
                double distXZ = Math.sqrt(dx * dx + dz * dz);

                float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
                float pitch = (float) (-Math.toDegrees(Math.atan2(dy, distXZ)));

                client.player.setYaw(yaw);
                client.player.setPitch(pitch);

                // Attack instantly when cooldown is ready and target is in range
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
}

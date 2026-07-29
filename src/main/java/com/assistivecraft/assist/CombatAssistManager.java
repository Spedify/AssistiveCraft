package com.assistivecraft.assist;

import com.assistivecraft.ModuleManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.Random;

public class CombatAssistManager {
    private static final float SMOOTHNESS_FACTOR = 0.22f;
    private static final Random RANDOM = new Random();
    private static int clickHoldTicks = 0;

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // FIX: If left mouse button is physically held down by the user, release our override so they can break blocks!
            if (client != null && client.mouse.isCursorLocked() && client.options.attackKey.isPressed() && !ModuleManager.combatAlerts) {
                return;
            }

            if (!ModuleManager.combatAlerts || client == null || client.player == null || client.world == null) {
                if (client != null && client.options != null) {
                    client.options.attackKey.setPressed(false);
                }
                return;
            }

            // FIX: Do NOT override attack input if the user is holding down left-click to mine/break a block
            if (client.options.attackKey.isPressed() && client.crosshairTarget != null && client.crosshairTarget.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
                return;
            }

            PlayerEntity closestTarget = null;
            double closestDistance = Double.MAX_VALUE;

            for (Entity entity : client.world.getEntities()) {
                if (entity == client.player || !(entity instanceof PlayerEntity) || !entity.isAlive()) continue;
                if (((PlayerEntity) entity).isSpectator() || ((PlayerEntity) entity).isCreative()) continue;

                double dist = client.player.squaredDistanceTo(entity);
                if (dist < 25.0 && dist < closestDistance) {
                    closestDistance = dist;
                    closestTarget = (PlayerEntity) entity;
                }
            }

            if (closestTarget != null) {
                double dx = closestTarget.getX() - client.player.getX();
                double dy = (closestTarget.getY() + closestTarget.getEyeHeight(closestTarget.getPose())) - client.player.getEyeY();
                double dz = closestTarget.getZ() - client.player.getZ();
                double distXZ = Math.sqrt(dx * dx + dz * dz);

                float targetYaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
                float targetPitch = (float) (-Math.toDegrees(Math.atan2(dy, distXZ)));

                float currentYaw = client.player.getYaw();
                float currentPitch = client.player.getPitch();

                float yawDiff = wrapDegrees(targetYaw - currentYaw);
                float pitchDiff = targetPitch - currentPitch;

                client.player.setYaw(currentYaw + (yawDiff * SMOOTHNESS_FACTOR));
                client.player.setPitch(currentPitch + (pitchDiff * SMOOTHNESS_FACTOR));

                // FIX: Strictly wait for attack cooldown progress to reach 100% (1.0f) before initiating physical click input
                if (client.player.getAttackCooldownProgress(0.0f) >= 1.0f) {
                    client.options.attackKey.setPressed(true);
                    clickHoldTicks = 1 + RANDOM.nextInt(2);
                } else if (clickHoldTicks > 0) {
                    clickHoldTicks--;
                    if (clickHoldTicks == 0) {
                        client.options.attackKey.setPressed(false);
                    }
                }
            } else {
                client.options.attackKey.setPressed(false);
            }
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (!ModuleManager.combatAlerts) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            context.drawCenteredTextWithShadow(client.textRenderer, Text.literal("§aCOOLDOWN-SYNCED AIM ACTIVE"), width / 2, height - 70, 0x00FF00);
        });
    }

    private static float wrapDegrees(float degrees) {
        degrees %= 360.0f;
        if (degrees >= 180.0f) degrees -= 360.0f;
        if (degrees < -180.0f) degrees += 360.0f;
        return degrees;
    }
}

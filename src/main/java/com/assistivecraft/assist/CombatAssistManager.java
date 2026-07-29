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
    private static final float SMOOTHNESS_FACTOR = 0.25f;
    private static final Random RANDOM = new Random();
    private static int clickHoldTicks = 0;

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Restore native user control over attack key if module is off or user is breaking blocks
            if (client == null || client.player == null || client.world == null) return;

            if (!ModuleManager.combatAlerts) {
                return;
            }

            if (client.options.attackKey.isPressed() && client.crosshairTarget != null && client.crosshairTarget.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
                return;
            }

            PlayerEntity closestTarget = null;
            double closestDistance = Double.MAX_VALUE;

            for (Entity entity : client.world.getEntities()) {
                if (entity == client.player || !(entity instanceof PlayerEntity) || !entity.isAlive()) continue;
                if (((PlayerEntity) entity).isSpectator() || ((PlayerEntity) entity).isCreative()) continue;

                double dist = client.player.squaredDistanceTo(entity);
                // Strict reach validation (default weapon reach range check: ~3.0 to 4.0 blocks squared = ~16.0)
                if (dist <= 9.0 && dist < closestDistance) {
                    closestDistance = dist;
                    closestTarget = (PlayerEntity) entity;
                }
            }

            if (closestTarget != null) {
                // Smooth tracking view adjustment
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

                // STRICT VANILLA CHECK: getAttackCooldownProgress(0.0f) ensures vanilla cooldown bar is 100% full (1.0)
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
            context.drawCenteredTextWithShadow(client.textRenderer, Text.literal("§aSTRICT COOLDOWN AIM-ASSIST ACTIVE"), width / 2, height - 70, 0x00FF00);
        });
    }

    private static float wrapDegrees(float degrees) {
        degrees %= 360.0f;
        if (degrees >= 180.0f) degrees -= 360.0f;
        if (degrees < -180.0f) degrees += 360.0f;
        return degrees;
    }
}

package com.assistivecraft.assist;

import com.assistivecraft.ModuleManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.AxeItem;
import net.minecraft.text.Text;

import java.util.Random;

public class CombatAssistManager {
    private static final float SMOOTHNESS_FACTOR = 0.25f;
    private static final Random RANDOM = new Random();
    private static int clickHoldTicks = 0;
    
    // Local client-side tracking to bypass multiplayer desync
    private static int localCooldownTicks = 0;
    private static int maxCooldownTicks = 12; // Standard sword cooldown (~0.6 seconds / 12 ticks)

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client == null || client.player == null || client.world == null) return;

            if (!ModuleManager.combatAlerts) {
                localCooldownTicks = 0;
                return;
            }

            if (client.options.attackKey.isPressed() && client.crosshairTarget != null && client.crosshairTarget.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
                return;
            }

            // Dynamically calculate weapon recovery time based on held item
            ItemStack heldItem = client.player.getMainHandStack();
            if (heldItem.getItem() instanceof SwordItem) {
                maxCooldownTicks = 12; // ~0.6s for swords
            } else if (heldItem.getItem() instanceof AxeItem) {
                maxCooldownTicks = 20; // ~1.0s for axes
            } else {
                maxCooldownTicks = 10; // Default fallback
            }

            // Decrement local cooldown counter every tick
            if (localCooldownTicks > 0) {
                localCooldownTicks--;
            }

            PlayerEntity closestTarget = null;
            double closestDistance = Double.MAX_VALUE;

            for (Entity entity : client.world.getEntities()) {
                if (entity == client.player || !(entity instanceof PlayerEntity) || !entity.isAlive()) continue;
                if (((PlayerEntity) entity).isSpectator() || ((PlayerEntity) entity).isCreative()) continue;

                double dist = client.player.squaredDistanceTo(entity);
                // Strict 3-block survival reach squared = 9.0
                if (dist <= 9.0 && dist < closestDistance) {
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

                // LOCAL COOLDOWN CHECK: Fully independent of server packet lag
                if (localCooldownTicks <= 0) {
                    client.options.attackKey.setPressed(true);
                    localCooldownTicks = maxCooldownTicks; // Reset timer locally
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
            context.drawCenteredTextWithShadow(client.textRenderer, Text.literal("§aLOCAL SYNC COOLDOWN ACTIVE"), width / 2, height - 70, 0x00FF00);
        });
    }

    private static float wrapDegrees(float degrees) {
        degrees %= 360.0f;
        if (degrees >= 180.0f) degrees -= 360.0f;
        if (degrees < -180.0f) degrees += 360.0f;
        return degrees;
    }
}

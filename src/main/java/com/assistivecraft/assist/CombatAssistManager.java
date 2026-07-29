package com.assistivecraft.assist;

import com.assistivecraft.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

public class CombatAssistManager {

    private static boolean attackKeyHeldByAssist = false;

    public static void onClientTick(MinecraftClient client) {
        ModuleManager mm = ModuleManager.INSTANCE;
        if (client.player == null || client.world == null) return;
        if (client.currentScreen != null) return;

        LivingEntity target = null;

        if (mm.crosshairAssistEnabled) {
            target = findNearestHostileInCone(client, mm.crosshairAssistFovDegrees, mm.interactionDistance * 3);
            if (target != null) {
                applyCrosshairAssist(client, target, mm.crosshairAssistSpeed);
            }
        }

        if (mm.attackSyncEnabled) {
            applyAttackSync(client, mm.interactionDistance);
        } else if (attackKeyHeldByAssist) {
            client.options.attackKey.setPressed(false);
            attackKeyHeldByAssist = false;
        }
    }

    private static LivingEntity findNearestHostileInCone(MinecraftClient client, float fovDegrees, double range) {
        ClientPlayerEntity player = client.player;
        Vec3d eyePos = player.getCameraPosVec(1.0f);
        Vec3d look = player.getRotationVec(1.0f);
        double cosThreshold = Math.cos(Math.toRadians(fovDegrees));

        return client.world.getEntitiesByClass(HostileEntity.class,
                        player.getBoundingBox().expand(range),
                        e -> e.isAlive())
                .stream()
                .filter(e -> {
                    // Calculate trajectory targeting the EXACT center of the bounding box
                    Vec3d toEntity = e.getBoundingBox().getCenter().subtract(eyePos).normalize();
                    return toEntity.dotProduct(look) >= cosThreshold;
                })
                .min(Comparator.comparingDouble(e -> e.squaredDistanceTo(player)))
                .orElse(null);
    }

    private static void applyCrosshairAssist(MinecraftClient client, LivingEntity target, float speed) {
        ClientPlayerEntity player = client.player;
        Vec3d eyePos = player.getCameraPosVec(1.0f);
        
        // Locks camera targeting directly to the center point of the entity's hitbox
        Vec3d targetCenter = target.getBoundingBox().getCenter();
        Vec3d toTarget = targetCenter.subtract(eyePos);

        double dx = toTarget.x, dy = toTarget.y, dz = toTarget.z;
        double horizontalDist = Math.sqrt(dx * dx + dz * dz);
        float desiredYaw = (float) (MathHelper.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0f;
        float desiredPitch = (float) -(MathHelper.atan2(dy, horizontalDist) * (180.0 / Math.PI));

        float newYaw = MathHelper.lerpAngleDegrees(speed, player.getYaw(), desiredYaw);
        float newPitch = MathHelper.clamp(
                MathHelper.lerp(speed, player.getPitch(), desiredPitch), -90.0f, 90.0f);

        player.setYaw(newYaw);
        player.setPitch(newPitch);
    }

    private static void applyAttackSync(MinecraftClient client, double interactionDistance) {
        ClientPlayerEntity player = client.player;

        if (!(client.crosshairTarget instanceof EntityHitResult entityHit)) return;
        if (client.crosshairTarget.getType() != HitResult.Type.ENTITY) return;
        Entity targetEntity = entityHit.getEntity();
        if (!(targetEntity instanceof LivingEntity)) return;
        if (player.squaredDistanceTo(targetEntity) > interactionDistance * interactionDistance) return;

        float cooldown = player.getAttackCooldownProgress(0.5f);
        if (cooldown >= 1.0f) {
            client.options.attackKey.setPressed(true);
            attackKeyHeldByAssist = true;
        } else if (attackKeyHeldByAssist) {
            client.options.attackKey.setPressed(false);
            attackKeyHeldByAssist = false;
        }
    }
}

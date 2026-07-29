package com.assistivecraft.assist;

import net.minecraft.client.MinecraftClient;

public class CombatAssistManager {
    public static void tick(MinecraftClient client) {
        if (client.player == null || client.crosshairTarget == null) return;

        if (client.player.getAttackCooldownProgress(0.5f) >= 1.0f) {
            if (client.targetedEntity != null && client.targetedEntity.isAlive()) {
                if (client.interactionManager != null) {
                    client.interactionManager.attackEntity(client.player, client.targetedEntity);
                    client.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
                }
            }
        }
    }
}
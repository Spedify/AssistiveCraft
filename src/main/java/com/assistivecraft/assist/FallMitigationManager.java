package com.assistivecraft.assist;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class FallMitigationManager {
    public static void tick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        if (client.player.fallDistance > 3.0f && !client.player.isOnGround()) {
            for (int i = 0; i < 9; i++) {
                if (client.player.getInventory().getStack(i).isOf(Items.WATER_BUCKET)) {
                    client.player.getInventory().selectedSlot = i;
                    if (client.player.getPitch() < 85.0f) {
                        client.player.setPitch(90.0f);
                    }
                    if (client.player.fallDistance > 4.5f) {
                        if (client.interactionManager != null) {
                            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
                        }
                    }
                    break;
                }
            }
        }
    }
}
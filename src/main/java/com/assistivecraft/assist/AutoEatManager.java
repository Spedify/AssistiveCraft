package com.assistivecraft.assist;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class AutoEatManager {
    public static void tick(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player == null) return;

        if (player.getHungerManager().getFoodLevel() <= 14) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (stack.isFood()) {
                    player.getInventory().selectedSlot = i;
                    client.options.useKey.setPressed(true);
                    return;
                }
            }
        } else {
            if (!client.options.useKey.isPressed()) {
                client.options.useKey.setPressed(false);
            }
        }
    }
}
package com.assistivecraft.assist;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class TotemSwapManager {
    public static void tick(MinecraftClient client) {
        if (client.player == null) return;

        if (!client.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
            for (int i = 9; i < 45; i++) {
                if (client.player.getInventory().getStack(i).isOf(Items.TOTEM_OF_UNDYING)) {
                    if (client.interactionManager != null) {
                        client.interactionManager.clickSlot(
                            client.player.playerScreenHandler.syncId,
                            i,
                            40,
                            SlotActionType.SWAP,
                            client.player
                        );
                    }
                    break;
                }
            }
        }
    }
}
package com.assistivecraft.assist;

import com.assistivecraft.KeyBindings;
import com.assistivecraft.ModuleManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public class TotemSwapManager {
    public static void initialize() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (!ModuleManager.totemAlert) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            if (client.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();
                context.drawCenteredTextWithShadow(client.textRenderer, Text.literal("§cAUTO-TOTEM ACTIVE"), width / 2, height - 60, 0xFF0000);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!ModuleManager.totemAlert || client.player == null || client.interactionManager == null) return;

            // Automatically check if offhand is empty or not a totem, and swap instantly
            if (client.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
                PlayerInventory inv = client.player.getInventory();
                for (int i = 0; i < 36; i++) {
                    if (inv.getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                        int syncId = client.player.playerScreenHandler.syncId;
                        int slotId = i < 9 ? i + 36 : i;
                        
                        client.interactionManager.clickSlot(syncId, slotId, 40, SlotActionType.SWAP, client.player);
                        break;
                    }
                }
            }

            // Manual hotkey support as a fallback backup
            while (KeyBindings.totemKey.wasPressed()) {
                PlayerInventory inv = client.player.getInventory();
                if (client.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) return;

                for (int i = 0; i < 36; i++) {
                    if (inv.getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                        int syncId = client.player.playerScreenHandler.syncId;
                        int slotId = i < 9 ? i + 36 : i;
                        
                        client.interactionManager.clickSlot(syncId, slotId, 40, SlotActionType.SWAP, client.player);
                        break;
                    }
                }
            }
        });
    }
}

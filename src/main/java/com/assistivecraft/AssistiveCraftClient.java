package com.assistivecraft;

import com.assistivecraft.assist.CombatAssistManager;
import com.assistivecraft.render.EntityOutlineRenderer;
import com.assistivecraft.render.OreOutlineRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class AssistiveCraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModuleManager mm = ModuleManager.INSTANCE;

        // Client Tick Event Loop
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            // Combat Assist handles its own internal checks for sub-modules
            CombatAssistManager.onClientTick(client);

            // Additional utility ticks can be called here if needed
        });

        // World Rendering Event Loop
        WorldRenderEvents.LAST.register(context -> {
            EntityOutlineRenderer.onWorldRenderLast(context);
            OreOutlineRenderer.onWorldRenderLast(context);
        });
    }
}

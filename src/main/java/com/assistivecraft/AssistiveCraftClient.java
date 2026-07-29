package com.assistivecraft;

import com.assistivecraft.assist.AutoEatManager;
import com.assistivecraft.assist.CombatAssistManager;
import com.assistivecraft.assist.FallMitigationManager;
import com.assistivecraft.assist.TotemSwapManager;
import com.assistivecraft.render.EntityOutlineRenderer;
import com.assistivecraft.render.OreOutlineRenderer;
import com.assistivecraft.render.ProjectilePathRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class AssistiveCraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindings.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            KeyBindings.checkKeys(client);

            if (ModuleManager.autoEatEnabled) AutoEatManager.tick(client);
            if (ModuleManager.combatAssistEnabled) CombatAssistManager.tick(client);
            if (ModuleManager.fallMitigationEnabled) FallMitigationManager.tick(client);
            if (ModuleManager.totemSwapEnabled) TotemSwapManager.tick(client);
        });

        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            if (ModuleManager.entityOutlineEnabled) EntityOutlineRenderer.render(context);
            if (ModuleManager.oreOutlineEnabled) OreOutlineRenderer.render(context);
            if (ModuleManager.projectilePathEnabled) ProjectilePathRenderer.render(context);
        });
    }
}
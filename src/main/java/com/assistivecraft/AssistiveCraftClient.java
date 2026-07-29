package com.assistivecraft;

import com.assistivecraft.assist.AutoEatManager;
import com.assistivecraft.assist.CombatAssistManager;
import com.assistivecraft.assist.FallMitigationManager;
import com.assistivecraft.assist.TotemSwapManager;
import com.assistivecraft.render.EntityOutlineRenderer;
import com.assistivecraft.render.OreOutlineRenderer;
import com.assistivecraft.render.ProjectilePathRenderer;
import net.fabricmc.api.ClientModInitializer;

public class AssistiveCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBindings.register();
        
        CombatAssistManager.initialize();
        TotemSwapManager.initialize();
        AutoEatManager.initialize();
        FallMitigationManager.initialize();
        
        EntityOutlineRenderer.initialize();
        OreOutlineRenderer.initialize();
        ProjectilePathRenderer.initialize();
    }
}

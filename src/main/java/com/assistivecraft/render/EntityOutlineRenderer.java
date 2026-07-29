package com.assistivecraft.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class EntityOutlineRenderer {
    public static void render(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        for (Entity entity : client.world.getEntities()) {
            if (entity != client.player) {
                entity.setGlowing(true);
            }
        }
    }
}
package com.assistivecraft.render;

import com.assistivecraft.ModuleManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class EntityOutlineRenderer {
    public static void initialize() {
        WorldRenderEvents.LAST.register(context -> {
            if (!ModuleManager.entityIndicators) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.world == null) return;

            Camera camera = context.camera();
            Vec3d camPos = camera.getPos();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest(); // Renders through walls

            VertexConsumer consumer = context.consumers().getBuffer(RenderLayer.getLines());

            for (Entity entity : client.world.getEntities()) {
                if (entity == client.player || !(entity instanceof LivingEntity)) continue;
                if (entity.squaredDistanceTo(client.player) > 4096) continue; // 64 blocks range

                Box box = entity.getBoundingBox().offset(-camPos.x, -camPos.y, -camPos.z);
                WorldRenderer.drawBox(context.matrixStack(), consumer, box, 1.0f, 0.0f, 0.0f, 1.0f);
            }

            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        });
    }
}

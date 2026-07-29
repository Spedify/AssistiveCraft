package com.assistivecraft.render;

import com.assistivecraft.ModuleManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BowItem;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class ProjectilePathRenderer {
    public static void initialize() {
        WorldRenderEvents.LAST.register(context -> {
            if (!ModuleManager.projectilePath) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || !(client.player.getMainHandStack().getItem() instanceof BowItem)) return;

            int useTicks = client.player.getItemUseTime();
            if (useTicks == 0) return;

            float charge = BowItem.getPullProgress(useTicks);
            if (charge < 0.1f) return;

            Camera camera = context.camera();
            Vec3d camPos = camera.getPos();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();

            MatrixStack matrices = context.matrixStack();
            VertexConsumer consumer = context.consumers().getBuffer(RenderLayer.getLines());

            double yaw = Math.toRadians(client.player.getYaw());
            double pitch = Math.toRadians(client.player.getPitch());
            
            double posX = client.player.getX() - Math.cos(yaw) * 0.16;
            double posY = client.player.getEyeY() - 0.1;
            double posZ = client.player.getZ() - Math.sin(yaw) * 0.16;

            double velX = -Math.sin(yaw) * Math.cos(pitch) * charge * 3.0;
            double velY = -Math.sin(pitch) * charge * 3.0;
            double velZ = Math.cos(yaw) * Math.cos(pitch) * charge * 3.0;

            Vec3d pos = new Vec3d(posX, posY, posZ);
            
            matrices.push();
            matrices.translate(-camPos.x, -camPos.y, -camPos.z);

            for (int i = 0; i < 100; i++) {
                Vec3d nextPos = pos.add(velX, velY, velZ);
                
                HitResult result = client.world.raycast(new RaycastContext(pos, nextPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, client.player));
                if (result.getType() != HitResult.Type.MISS) {
                    nextPos = result.getPos();
                }

                consumer.vertex(matrices.peek().getPositionMatrix(), (float) pos.x, (float) pos.y, (float) pos.z).color(255, 255, 0, 255).normal(0, 1, 0).next();
                consumer.vertex(matrices.peek().getPositionMatrix(), (float) nextPos.x, (float) nextPos.y, (float) nextPos.z).color(255, 255, 0, 255).normal(0, 1, 0).next();

                if (result.getType() != HitResult.Type.MISS) break;

                pos = nextPos;
                velX *= 0.99;
                velY = (velY * 0.99) - 0.05;
                velZ *= 0.99;
            }

            matrices.pop();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        });
    }
}

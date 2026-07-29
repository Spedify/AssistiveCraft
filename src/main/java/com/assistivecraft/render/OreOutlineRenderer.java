package com.assistivecraft.render;

import com.assistivecraft.ModuleManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class OreOutlineRenderer {
    public static void initialize() {
        WorldRenderEvents.LAST.register(context -> {
            if (!ModuleManager.oreHighlights) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.world == null) return;

            Camera camera = context.camera();
            Vec3d camPos = camera.getPos();
            BlockPos playerPos = client.player.getBlockPos();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest(); // Forces rendering straight through terrain

            VertexConsumer consumer = context.consumers().getBuffer(RenderLayer.getLines());

            int radius = 24;
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos pos = playerPos.add(x, y, z);
                        BlockState state = client.world.getBlockState(pos);

                        if (state.isOf(Blocks.DIAMOND_ORE) || state.isOf(Blocks.DEEPSLATE_DIAMOND_ORE) ||
                            state.isOf(Blocks.ANCIENT_DEBRIS) || state.isOf(Blocks.GOLD_ORE)) {
                            
                            Box box = new Box(pos).offset(-camPos.x, -camPos.y, -camPos.z);
                            WorldRenderer.drawBox(context.matrixStack(), consumer, box, 0.0f, 1.0f, 1.0f, 1.0f);
                        }
                    }
                }
            }

            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        });
    }
}

package com.assistivecraft.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    // Allows WASD player movement while inventory screens are open
}
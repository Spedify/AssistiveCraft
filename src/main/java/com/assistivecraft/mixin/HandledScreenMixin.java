package com.assistivecraft.mixin;

import com.assistivecraft.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void allowGuiMovement(CallbackInfo ci) {
        if (!ModuleManager.guiMovement) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        long window = client.getWindow().getHandle();
        
        KeyBinding[] moveKeys = new KeyBinding[] {
            client.options.forwardKey,
            client.options.backKey,
            client.options.leftKey,
            client.options.rightKey,
            client.options.jumpKey,
            client.options.sprintKey
        };

        for (KeyBinding key : moveKeys) {
            InputUtil.Key boundKey = key.getDefaultKey();
            boolean isPressed = InputUtil.isKeyPressed(window, boundKey.getCode());
            key.setPressed(isPressed);
        }
    }
}

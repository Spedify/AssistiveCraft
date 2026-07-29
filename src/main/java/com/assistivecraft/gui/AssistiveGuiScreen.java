package com.assistivecraft.gui;

import com.assistivecraft.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class AssistiveGuiScreen extends Screen {

    public AssistiveGuiScreen() {
        super(Text.literal("AssistiveCraft Settings"));
    }

    @Override
    protected void init() {
        int y = this.height / 4;

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Mob Outlines: " + (ModuleManager.entityOutlineEnabled ? "ON" : "OFF")),
            button -> {
                ModuleManager.entityOutlineEnabled = !ModuleManager.entityOutlineEnabled;
                button.setMessage(Text.literal("Mob Outlines: " + (ModuleManager.entityOutlineEnabled ? "ON" : "OFF")));
            }).dimensions(this.width / 2 - 100, y, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Auto Eat: " + (ModuleManager.autoEatEnabled ? "ON" : "OFF")),
            button -> {
                ModuleManager.autoEatEnabled = !ModuleManager.autoEatEnabled;
                button.setMessage(Text.literal("Auto Eat: " + (ModuleManager.autoEatEnabled ? "ON" : "OFF")));
            }).dimensions(this.width / 2 - 100, y + 24, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Combat Assist: " + (ModuleManager.combatAssistEnabled ? "ON" : "OFF")),
            button -> {
                ModuleManager.combatAssistEnabled = !ModuleManager.combatAssistEnabled;
                button.setMessage(Text.literal("Combat Assist: " + (ModuleManager.combatAssistEnabled ? "ON" : "OFF")));
            }).dimensions(this.width / 2 - 100, y + 48, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Auto Totem: " + (ModuleManager.totemSwapEnabled ? "ON" : "OFF")),
            button -> {
                ModuleManager.totemSwapEnabled = !ModuleManager.totemSwapEnabled;
                button.setMessage(Text.literal("Auto Totem: " + (ModuleManager.totemSwapEnabled ? "ON" : "OFF")));
            }).dimensions(this.width / 2 - 100, y + 72, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
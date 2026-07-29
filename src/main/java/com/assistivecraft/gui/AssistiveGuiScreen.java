package com.assistivecraft.gui;

import com.assistivecraft.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class AssistiveGuiScreen extends Screen {
    public AssistiveGuiScreen() {
        super(Text.literal("AssistiveCraft Configuration"));
    }

    @Override
    protected void init() {
        int startY = 40;
        int btnWidth = 150;
        int btnHeight = 20;
        int col1 = this.width / 2 - 160;
        int col2 = this.width / 2 + 10;

        this.addDrawableChild(ButtonWidget.builder(getToggleText("Combat Alerts", ModuleManager.combatAlerts), btn -> {
            ModuleManager.combatAlerts = !ModuleManager.combatAlerts;
            btn.setMessage(getToggleText("Combat Alerts", ModuleManager.combatAlerts));
        }).dimensions(col1, startY, btnWidth, btnHeight).build());

        this.addDrawableChild(ButtonWidget.builder(getToggleText("Totem Alert & Macro", ModuleManager.totemAlert), btn -> {
            ModuleManager.totemAlert = !ModuleManager.totemAlert;
            btn.setMessage(getToggleText("Totem Alert & Macro", ModuleManager.totemAlert));
        }).dimensions(col1, startY + 25, btnWidth, btnHeight).build());

        this.addDrawableChild(ButtonWidget.builder(getToggleText("Auto-Eat Shortcut", ModuleManager.eatShortcut), btn -> {
            ModuleManager.eatShortcut = !ModuleManager.eatShortcut;
            btn.setMessage(getToggleText("Auto-Eat Shortcut", ModuleManager.eatShortcut));
        }).dimensions(col1, startY + 50, btnWidth, btnHeight).build());

        this.addDrawableChild(ButtonWidget.builder(getToggleText("Fall Warning", ModuleManager.fallWarning), btn -> {
            ModuleManager.fallWarning = !ModuleManager.fallWarning;
            btn.setMessage(getToggleText("Fall Warning", ModuleManager.fallWarning));
        }).dimensions(col1, startY + 75, btnWidth, btnHeight).build());

        this.addDrawableChild(ButtonWidget.builder(getToggleText("GUI Movement", ModuleManager.guiMovement), btn -> {
            ModuleManager.guiMovement = !ModuleManager.guiMovement;
            btn.setMessage(getToggleText("GUI Movement", ModuleManager.guiMovement));
        }).dimensions(col2, startY, btnWidth, btnHeight).build());

        this.addDrawableChild(ButtonWidget.builder(getToggleText("Entity Indicators", ModuleManager.entityIndicators), btn -> {
            ModuleManager.entityIndicators = !ModuleManager.entityIndicators;
            btn.setMessage(getToggleText("Entity Indicators", ModuleManager.entityIndicators));
        }).dimensions(col2, startY + 25, btnWidth, btnHeight).build());

        this.addDrawableChild(ButtonWidget.builder(getToggleText("Ore Highlights", ModuleManager.oreHighlights), btn -> {
            ModuleManager.oreHighlights = !ModuleManager.oreHighlights;
            btn.setMessage(getToggleText("Ore Highlights", ModuleManager.oreHighlights));
        }).dimensions(col2, startY + 50, btnWidth, btnHeight).build());

        this.addDrawableChild(ButtonWidget.builder(getToggleText("Projectile Path", ModuleManager.projectilePath), btn -> {
            ModuleManager.projectilePath = !ModuleManager.projectilePath;
            btn.setMessage(getToggleText("Projectile Path", ModuleManager.projectilePath));
        }).dimensions(col2, startY + 75, btnWidth, btnHeight).build());
    }

    private Text getToggleText(String name, boolean state) {
        return Text.literal(name + ": " + (state ? "§aON" : "§cOFF"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}

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
        ModuleManager mm = ModuleManager.INSTANCE;
        int startX = this.width / 2 - 100;
        int currentY = 30;

        // ==================== COMBAT CATEGORY ====================
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal((mm.combatCategoryExpanded ? "▼ " : "► ") + "Combat Assist"),
            btn -> {
                mm.combatCategoryExpanded = !mm.combatCategoryExpanded;
                this.clearAndInit();
            }
        ).dimensions(startX, currentY, 200, 20).build());
        currentY += 24;

        if (mm.combatCategoryExpanded) {
            // Individual Toggle: Aim / Crosshair Assist
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Aim Assist: " + (mm.crosshairAssistEnabled ? "ON" : "OFF")),
                btn -> {
                    mm.crosshairAssistEnabled = !mm.crosshairAssistEnabled;
                    btn.setMessage(Text.literal("Aim Assist: " + (mm.crosshairAssistEnabled ? "ON" : "OFF")));
                }
            ).dimensions(startX + 10, currentY, 180, 20).build());
            currentY += 22;

            // Individual Toggle: Auto Attack Sync
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Attack Sync: " + (mm.attackSyncEnabled ? "ON" : "OFF")),
                btn -> {
                    mm.attackSyncEnabled = !mm.attackSyncEnabled;
                    btn.setMessage(Text.literal("Attack Sync: " + (mm.attackSyncEnabled ? "ON" : "OFF")));
                }
            ).dimensions(startX + 10, currentY, 180, 20).build());
            currentY += 22;

            // Individual Toggle: Auto Totem Swap
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Auto Totem: " + (mm.totemSwapEnabled ? "ON" : "OFF")),
                btn -> {
                    mm.totemSwapEnabled = !mm.totemSwapEnabled;
                    btn.setMessage(Text.literal("Auto Totem: " + (mm.totemSwapEnabled ? "ON" : "OFF")));
                }
            ).dimensions(startX + 10, currentY, 180, 20).build());
            currentY += 26;
        }

        // ==================== OUTLINES CATEGORY ====================
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal((mm.outlinesCategoryExpanded ? "▼ " : "► ") + "Visual & Outlines"),
            btn -> {
                mm.outlinesCategoryExpanded = !mm.outlinesCategoryExpanded;
                this.clearAndInit();
            }
        ).dimensions(startX, currentY, 200, 20).build());
        currentY += 24;

        if (mm.outlinesCategoryExpanded) {
            // Individual Toggle: Mob Outline
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Mob Outlines: " + (mm.entityOutlinesEnabled ? "ON" : "OFF")),
                btn -> {
                    mm.entityOutlinesEnabled = !mm.entityOutlinesEnabled;
                    btn.setMessage(Text.literal("Mob Outlines: " + (mm.entityOutlinesEnabled ? "ON" : "OFF")));
                }
            ).dimensions(startX + 10, currentY, 180, 20).build());
            currentY += 22;

            // Individual Toggle: Diamond ESP
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Diamond ESP: " + (mm.oreOutlinesEnabled ? "ON" : "OFF")),
                btn -> {
                    mm.oreOutlinesEnabled = !mm.oreOutlinesEnabled;
                    btn.setMessage(Text.literal("Diamond ESP: " + (mm.oreOutlinesEnabled ? "ON" : "OFF")));
                }
            ).dimensions(startX + 10, currentY, 180, 20).build());
            currentY += 22;

            // Individual Toggle: Trajectory / Projectile Path
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Projectile Path: " + (mm.projectilePathEnabled ? "ON" : "OFF")),
                btn -> {
                    mm.projectilePathEnabled = !mm.projectilePathEnabled;
                    btn.setMessage(Text.literal("Projectile Path: " + (mm.projectilePathEnabled ? "ON" : "OFF")));
                }
            ).dimensions(startX + 10, currentY, 180, 20).build());
            currentY += 26;
        }

        // ==================== UTILITY CATEGORY ====================
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal((mm.utilityCategoryExpanded ? "▼ " : "► ") + "Utilities"),
            btn -> {
                mm.utilityCategoryExpanded = !mm.utilityCategoryExpanded;
                this.clearAndInit();
            }
        ).dimensions(startX, currentY, 200, 20).build());
        currentY += 24;

        if (mm.utilityCategoryExpanded) {
            // Individual Toggle: Auto Eat
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Auto Eat: " + (mm.autoEatEnabled ? "ON" : "OFF")),
                btn -> {
                    mm.autoEatEnabled = !mm.autoEatEnabled;
                    btn.setMessage(Text.literal("Auto Eat: " + (mm.autoEatEnabled ? "ON" : "OFF")));
                }
            ).dimensions(startX + 10, currentY, 180, 20).build());
            currentY += 22;

            // Individual Toggle: Fall Mitigation
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Fall Assist: " + (mm.fallMitigationEnabled ? "ON" : "OFF")),
                btn -> {
                    mm.fallMitigationEnabled = !mm.fallMitigationEnabled;
                    btn.setMessage(Text.literal("Fall Assist: " + (mm.fallMitigationEnabled ? "ON" : "OFF")));
                }
            ).dimensions(startX + 10, currentY, 180, 20).build());
            currentY += 26;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
}

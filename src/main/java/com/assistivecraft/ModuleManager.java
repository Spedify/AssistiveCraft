package com.assistivecraft;

public class ModuleManager {
    public static final ModuleManager INSTANCE = new ModuleManager();

    // --- CATEGORY DROPDOWN EXPANSION STATES ---
    public boolean combatCategoryExpanded = true;
    public boolean outlinesCategoryExpanded = true;
    public boolean utilityCategoryExpanded = true;

    // --- COMBAT INDIVIDUAL TOGGLES & SETTINGS ---
    public boolean crosshairAssistEnabled = false;
    public float crosshairAssistSpeed = 0.15f;
    public float crosshairAssistFovDegrees = 60.0f;
    
    public boolean attackSyncEnabled = false;
    public double interactionDistance = 4.5;
    
    public boolean totemSwapEnabled = false;

    // --- OUTLINES INDIVIDUAL TOGGLES & SETTINGS ---
    public boolean entityOutlinesEnabled = false;
    
    public boolean oreOutlinesEnabled = false;
    public int oreOutlineRadius = 16;
    
    public boolean projectilePathEnabled = false;

    // --- UTILITY INDIVIDUAL TOGGLES & SETTINGS ---
    public boolean autoEatEnabled = false;
    public int autoEatHungerThreshold = 14;
    
    public boolean fallMitigationEnabled = false;
}

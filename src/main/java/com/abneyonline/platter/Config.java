package com.abneyonline.platter;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Config {
    public static final String CATEGORY_GENERAL = "general";

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.IntValue PLATTER_SLOTS;
    public static ForgeConfigSpec.IntValue PLATTER_RADIUS;
    public static ForgeConfigSpec.IntValue PLATTER_PERIOD;


    static {

        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("Platter Settings").push(CATEGORY_GENERAL);

        PLATTER_SLOTS = COMMON_BUILDER.comment("Item slots for the platter [default: 9]").defineInRange("platter_slots", 9, 0, Integer.MAX_VALUE);
        PLATTER_RADIUS = COMMON_BUILDER.comment("Radius for the platter to feed [default: 5]").defineInRange("platter_radius", 5, 0, 32);
        PLATTER_PERIOD = COMMON_BUILDER.comment("Time (in seconds) to wait between platter searches [default: 60]").defineInRange("platter_period", 60, 0, Integer.MAX_VALUE);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }
}

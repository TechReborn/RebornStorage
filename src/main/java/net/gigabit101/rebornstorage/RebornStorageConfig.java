package net.gigabit101.rebornstorage;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class RebornStorageConfig
{
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_MULTIBLOCK = "multiblock";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.IntValue FRAME_COST;
    public static ForgeConfigSpec.IntValue HEAT_COST;
    public static ForgeConfigSpec.IntValue CPU_COST;
    public static ForgeConfigSpec.IntValue STORAGE_COST;

    public static ForgeConfigSpec.IntValue MULTIBLOCK_MAX_XSIZE;
    public static ForgeConfigSpec.IntValue MULTIBLOCK_MAX_YSIZE;
    public static ForgeConfigSpec.IntValue MULTIBLOCK_MAX_ZSIZE;

    public static ForgeConfigSpec.IntValue MULTIBLOCK_MIN_XSIZE;
    public static ForgeConfigSpec.IntValue MULTIBLOCK_MIN_YSIZE;
    public static ForgeConfigSpec.IntValue MULTIBLOCK_MIN_ZSIZE;

    public static ForgeConfigSpec.IntValue ADVANCED_WIRELESS_TRANSMITTER_RANGE;
    public static ForgeConfigSpec.IntValue ADVANCED_WIRELESS_TRANSMITTER_POWER_COST;


    static
    {
        COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);

        FRAME_COST = COMMON_BUILDER.comment("Power requirement for Frame blocks").defineInRange("multiblock_crafter_frame_cost", 0, 0, 1000);
        HEAT_COST = COMMON_BUILDER.comment("Power requirement for Heat exchanger blocks").defineInRange("multiblock_crafter_heat_cost", 0, 0, 1000);
        CPU_COST = COMMON_BUILDER.comment("Power requirement for Crafting Cpu blocks").defineInRange("multiblock_crafter_cpu_cost", 5, 0, 1000);
        STORAGE_COST = COMMON_BUILDER.comment("Power requirement for Storage blocks").defineInRange("multiblock_crafter_storage_cost", 10, 0, 1000);

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Multiblock Settings").push(CATEGORY_MULTIBLOCK);

        MULTIBLOCK_MAX_XSIZE = COMMON_BUILDER.comment("Maximum X size").defineInRange("multiblock_crafter_max_x", 16, 3, 64);
        MULTIBLOCK_MAX_YSIZE = COMMON_BUILDER.comment("Maximum Y size").defineInRange("multiblock_crafter_max_y", 16, 3, 64);
        MULTIBLOCK_MAX_ZSIZE = COMMON_BUILDER.comment("Maximum Z size").defineInRange("multiblock_crafter_max_z", 16, 3, 64);

        MULTIBLOCK_MIN_XSIZE = COMMON_BUILDER.comment("Maximum X size").defineInRange("multiblock_crafter_min_x", 3, 3, 64);
        MULTIBLOCK_MIN_YSIZE = COMMON_BUILDER.comment("Maximum Y size").defineInRange("multiblock_crafter_min_y", 3, 3, 64);
        MULTIBLOCK_MIN_ZSIZE = COMMON_BUILDER.comment("Maximum Z size").defineInRange("multiblock_crafter_min_z", 3, 3, 64);

        ADVANCED_WIRELESS_TRANSMITTER_RANGE = COMMON_BUILDER.defineInRange("advanced_wireless_transmitter_range", 1000, 0, 100000);
        ADVANCED_WIRELESS_TRANSMITTER_POWER_COST = COMMON_BUILDER.defineInRange("advanced_wireless_transmitter_cost", 100, 0, 100000);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path)
    {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        spec.setConfig(configData);
    }
}

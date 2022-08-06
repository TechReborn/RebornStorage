package net.gigabit101.rebornstorage;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.refinedmods.refinedstorage.RSBlocks;
import net.gigabit101.rebornstorage.init.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RebornStorageDataGenerator
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();

        if (event.includeServer())
        {
            generator.addProvider(true, new GeneratorRecipes(generator));
            generator.addProvider(true,new GeneratorLoots(generator));
        }

        if (event.includeClient())
        {
            generator.addProvider(true,new GeneratorBlockTags(generator, event.getExistingFileHelper()));
            generator.addProvider(true,new GeneratorLanguage(generator));
            generator.addProvider(true,new GeneratorBlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(true,new GeneratorItemModels(generator, event.getExistingFileHelper()));
        }
    }

    static class GeneratorBlockStates extends BlockStateProvider
    {
        public GeneratorBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper)
        {
            super(gen, Constants.MOD_ID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels()
        {
        }

        public void registerSidedBlock(Block block, String folder)
        {
            horizontalBlock(block, models().orientableWithBottom(getResourceLocation(block).getPath(),
                    modLoc("block/" + folder + "/side"),
                    modLoc("block/" + folder + "/front"),
                    modLoc("block/" + folder + "/bottom"),
                    modLoc("block/" + folder + "/top")));
        }

        public ResourceLocation getResourceLocation(Block block)
        {
            return Registry.BLOCK.getKey(block);
        }
    }

    static class GeneratorItemModels extends ItemModelProvider
    {
        public GeneratorItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper)
        {
            super(generator, Constants.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels()
        {
//            ModBlocks.CHARGERS.forEach((chargerTypes, blockSupplier) -> registerDefaultItemBlockModel(blockSupplier.get()));
//            ModBlocks.POWER_CELLS.forEach((chargerTypes, blockSupplier) -> registerDefaultItemBlockModel(blockSupplier.get()));
//
//            ModItems.BATTERIES.forEach((batteryTypes, itemSupplier) -> singleTexture(getResourceLocation(itemSupplier.get()).getPath(),
//                    mcLoc("item/generated"), "layer0", modLoc("item/" + batteryTypes.getName())));
//
//            ModItems.DRILL_BIT_TYPES.forEach((batteryTypes, itemSupplier) -> singleTexture(getResourceLocation(itemSupplier.get()).getPath(),
//                    mcLoc("item/generated"), "layer0", modLoc("item/" + batteryTypes.getName())));
//
//            ModItems.DRILL_HANDLE_TYPES.forEach((batteryTypes, itemSupplier) -> singleTexture(getResourceLocation(itemSupplier.get()).getPath(),
//                    mcLoc("item/generated"), "layer0", modLoc("item/drill_handle")));
        }

        public void registerDefaultItemBlockModel(Block block)
        {
            String path = getResourceLocation(block).getPath();
            getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
        }

        public ResourceLocation getResourceLocation(Item item)
        {
            return Registry.ITEM.getKey(item);
        }

        public ResourceLocation getResourceLocation(Block block)
        {
            return Registry.BLOCK.getKey(block);
        }

        @Override
        public @NotNull String getName()
        {
            return "Item Models";
        }
    }

    static class GeneratorLanguage extends LanguageProvider
    {
        public GeneratorLanguage(DataGenerator gen)
        {
            super(gen, Constants.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations()
        {
//            addBlock(ModBlocks.CHARGERS.get(ChargerTypes.BASIC), "Basic Charger");
//            addBlock(ModBlocks.CHARGERS.get(ChargerTypes.ADVANCED), "Advanced Charger");
//            addBlock(ModBlocks.CHARGERS.get(ChargerTypes.ULTIMATE), "Ultimate Charger");
//            addBlock(ModBlocks.CHARGERS.get(ChargerTypes.CREATIVE), "Creative Charger");
//
//            addBlock(ModBlocks.POWER_CELLS.get(PowerCellTypes.BASIC), "Basic Energy cell");
//            addBlock(ModBlocks.POWER_CELLS.get(PowerCellTypes.ADVANCED), "Advanced Energy cell");
//            addBlock(ModBlocks.POWER_CELLS.get(PowerCellTypes.ULTIMATE), "Ultimate Energy cell");
//            addBlock(ModBlocks.POWER_CELLS.get(PowerCellTypes.CREATIVE), "Creative Energy cell");
//
//            addItem(ModItems.DRILL_BIT_TYPES.get(DrillBitTypes.IRON), "Iron Drill bit");
//            addItem(ModItems.DRILL_BIT_TYPES.get(DrillBitTypes.GOLD), "Gold Drill bit");
//            addItem(ModItems.DRILL_BIT_TYPES.get(DrillBitTypes.DIAMOND), "Diamond Drill bit");
//            addItem(ModItems.DRILL_BIT_TYPES.get(DrillBitTypes.NETHERITE), "Netherite Drill bit");
//
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.WHITE), "White Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.ORANGE), "Orange Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.MAGENTA), "Magenta Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.LIGHT_BLUE), "Light Blue Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.YELLOW), "Yellow Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.LIME), "Lime Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.PINK), "Pink Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.GRAY), "Gray Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.LIGHT_GRAY), "Light Gray Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.CYAN), "Cyan Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.PURPLE), "Purple Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.BLUE), "Blue Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.BROWN), "Brown Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.GREEN), "Green Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.RED), "Red Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.BLACK), "Black Drill handle");
//
//
//            add("itemGroup.techmod", "Tech-Mod");
//            add("screen.techmod.energy", "Energy: %s/%s FE");
//            add("screen.techmod.no_fuel", "Fuel source empty");
//            add("screen.techmod.burn_time", "Burn time left: %ss");
//            add("item.techmod.basic_battery", "Basic Battery");
//            add("item.techmod.advanced_battery", "Advanced Battery");
//            add("item.techmod.ultimate_battery", "Ultimate Battery");
        }
    }


    static class GeneratorLoots extends LootTableProvider
    {
        public GeneratorLoots(DataGenerator dataGeneratorIn)
        {
            super(dataGeneratorIn);
        }

        @Override
        protected @NotNull List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables()
        {
            return ImmutableList.of(Pair.of(Blocks::new, LootContextParamSets.BLOCK));
        }

        private static class Blocks extends BlockLoot
        {
            @Override
            protected void addTables()
            {
                ModBlocks.BLOCKS.getEntries().forEach(blockRegistryObject -> this.add(blockRegistryObject.get(), LootTable.lootTable().withPool(create(blockRegistryObject.get()))));
            }

            public LootPool.Builder create(Block block)
            {
                return LootPool.lootPool().name(getResourceLocation(block).toString())
                        .setRolls(ConstantValue.exactly(1)).when(ExplosionCondition.survivesExplosion())
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)));

            }

            public ResourceLocation getResourceLocation(Block block)
            {
                return Registry.BLOCK.getKey(block);
            }

            @Override
            protected @NotNull Iterable<Block> getKnownBlocks()
            {
                return ImmutableList.of(
                        ModBlocks.BLOCK_MULTI_FRAME.get(),
                        ModBlocks.BLOCK_MULTI_HEAT.get(),
                        ModBlocks.BLOCK_MULTI_CPU.get(),
                        ModBlocks.BLOCK_MULTI_STORAGE.get(),
                        ModBlocks.BLOCK_ADVANCED_WIRELESS_TRANSMITTER.get()
                );
            }
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, @NotNull ValidationContext validationtracker)
        {
            map.forEach((name, table) -> LootTables.validate(validationtracker, name, table));
        }
    }

    static class GeneratorRecipes extends RecipeProvider
    {
        public GeneratorRecipes(DataGenerator generator)
        {
            super(generator);
        }

        @Override
        protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer)
        {
//            Block block = ModBlocks.CHARGERS.get(ChargerTypes.BASIC).get();
//            ShapedRecipeBuilder.shaped(block)
//                    .define('i', Tags.Items.INGOTS_IRON)
//                    .define('r', Tags.Items.DUSTS_REDSTONE)
//                    .define('l', Tags.Items.STORAGE_BLOCKS_COAL)
//                    .define('d', Tags.Items.GEMS_LAPIS)
//                    .pattern("iri")
//                    .pattern("drd")
//                    .pattern("ili")
//                    .unlockedBy("has_diamonds", has(Tags.Items.GEMS_DIAMOND)).save(consumer);
        }
    }

    static class GeneratorBlockTags extends BlockTagsProvider
    {
        public GeneratorBlockTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper)
        {
            super(generator, Constants.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags()
        {
            ModBlocks.BLOCKS.getEntries().forEach(blockRegistryObject -> addMineable(blockRegistryObject.get()));

            RSBlocks.COLORED_BLOCKS.forEach(registryObject -> addMineable(registryObject.get()));
            RSBlocks.STORAGE_BLOCKS.forEach((itemStorageType, storageBlockRegistryObject) -> addMineable(storageBlockRegistryObject.get()));
            RSBlocks.FLUID_STORAGE_BLOCKS.forEach((fluidStorageType, fluidStorageBlockRegistryObject) -> addMineable(fluidStorageBlockRegistryObject.get()));

            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                    RSBlocks.IMPORTER.get(),
                    RSBlocks.EXPORTER.get(),
                    RSBlocks.EXTERNAL_STORAGE.get(),
                    RSBlocks.DISK_DRIVE.get(),
                    RSBlocks.INTERFACE.get(),
                    RSBlocks.STORAGE_MONITOR.get(),
                    RSBlocks.FLUID_INTERFACE.get(),
                    RSBlocks.CONSTRUCTOR.get(),
                    RSBlocks.DESTRUCTOR.get(),
                    RSBlocks.PORTABLE_GRID.get(),
                    RSBlocks.CREATIVE_PORTABLE_GRID.get(),
                    RSBlocks.CABLE.get()
            );
        }

        public void addMineable(Block block)
        {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
        }
    }
}

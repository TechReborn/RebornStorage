package net.gigabit101.rebornstorage.init;

import net.gigabit101.rebornstorage.blocks.BlockMultiCrafter;
import net.gigabit101.rebornstorage.Constants;
import net.gigabit101.rebornstorage.tiles.TileMultiCrafter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILES_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Constants.MOD_ID);

    public static final RegistryObject<Block> BLOCK_MULTI_FRAME = BLOCKS.register("multiblock_frame", BlockMultiCrafter::new);
    public static final RegistryObject<Block> BLOCK_MULTI_HEAT = BLOCKS.register("multiblock_heat", BlockMultiCrafter::new);
    public static final RegistryObject<Block> BLOCK_MULTI_CPU = BLOCKS.register("multiblock_cpu", BlockMultiCrafter::new);
    public static final RegistryObject<Block> BLOCK_MULTI_STORAGE = BLOCKS.register("multiblock_storage", BlockMultiCrafter::new);

    public static final RegistryObject<BlockEntityType<TileMultiCrafter>> CRAFTER_TILE =
            TILES_ENTITIES.register("multiblock_frame", () -> BlockEntityType.Builder.of(TileMultiCrafter::new, ModBlocks.BLOCK_MULTI_FRAME.get(), ModBlocks.BLOCK_MULTI_STORAGE.get(), ModBlocks.BLOCK_MULTI_CPU.get(), ModBlocks.BLOCK_MULTI_HEAT.get()).build(null));
}

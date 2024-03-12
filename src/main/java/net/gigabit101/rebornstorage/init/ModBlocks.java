package net.gigabit101.rebornstorage.init;

import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationManager;
import net.gigabit101.rebornstorage.blockentities.BlockEntityAdvancedWirelessTransmitter;
import net.gigabit101.rebornstorage.blocks.BlockAdvancedWirelessTransmitter;
import net.gigabit101.rebornstorage.blocks.BlockMultiCrafter;
import net.gigabit101.rebornstorage.Constants;
import net.gigabit101.rebornstorage.blockentities.BlockEntityMultiCrafter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILES_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<Block, Block> BLOCK_MULTI_FRAME = BLOCKS.register("multiblock_frame", BlockMultiCrafter::new);
    public static final DeferredHolder<Block, Block> BLOCK_MULTI_HEAT = BLOCKS.register("multiblock_heat", BlockMultiCrafter::new);
    public static final DeferredHolder<Block, Block> BLOCK_MULTI_CPU = BLOCKS.register("multiblock_cpu", BlockMultiCrafter::new);
    public static final DeferredHolder<Block, Block> BLOCK_MULTI_STORAGE = BLOCKS.register("multiblock_storage", BlockMultiCrafter::new);

    public static final DeferredHolder<Block, Block> BLOCK_ADVANCED_WIRELESS_TRANSMITTER = BLOCKS.register("advanced_wireless_transmitter", BlockAdvancedWirelessTransmitter::new);

    public static final  DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityMultiCrafter>> CRAFTER_TILE = TILES_ENTITIES.register("multiblock_frame", () -> BlockEntityType.Builder.of(BlockEntityMultiCrafter::new, ModBlocks.BLOCK_MULTI_FRAME.get(), ModBlocks.BLOCK_MULTI_STORAGE.get(), ModBlocks.BLOCK_MULTI_CPU.get(), ModBlocks.BLOCK_MULTI_HEAT.get()).build(null));
    public static final  DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityAdvancedWirelessTransmitter>> ADVANCED_WIRELESS_TRANSMITTER = TILES_ENTITIES.register("advanced_wireless_transmitter", () -> BlockEntityType.Builder.of(BlockEntityAdvancedWirelessTransmitter::new, ModBlocks.BLOCK_ADVANCED_WIRELESS_TRANSMITTER.get()).build(null));

    static
    {
        BlockEntitySynchronizationManager.registerParameter(BlockEntityAdvancedWirelessTransmitter.RANGE);
    }
}

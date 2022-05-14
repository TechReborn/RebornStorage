package net.gigabit101.rebornstorage.init;

import com.refinedmods.refinedstorage.api.storage.StorageType;
import com.refinedmods.refinedstorage.block.BaseBlock;
import com.refinedmods.refinedstorage.item.blockitem.BaseBlockItem;
import net.gigabit101.rebornstorage.client.CreativeTabRebornStorage;
import net.gigabit101.rebornstorage.items.ItemWirelessGrid;
import net.gigabit101.rebornstorage.items.ItemFluidRebornStorageCell;
import net.gigabit101.rebornstorage.items.ItemRebornStorageCell;
import net.gigabit101.rebornstorage.Constants;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems
{
    public static final Item.Properties ITEM_GROUP = new Item.Properties().tab(CreativeTabRebornStorage.INSTANCE);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    public static final Item.Properties PROPERTIES = new Item.Properties().tab(CreativeTabRebornStorage.INSTANCE);


    //ItemBlocks
    public static final RegistryObject<Item> BLOCK_MULTI_FRAME_ITEM = ITEMS.register("multiblock_frame", () -> new BlockItem(ModBlocks.BLOCK_MULTI_FRAME.get(), ITEM_GROUP));
    public static final RegistryObject<Item> BLOCK_MULTI_HEAT_ITEM = ITEMS.register("multiblock_heat", () -> new BlockItem(ModBlocks.BLOCK_MULTI_HEAT.get(), ITEM_GROUP));
    public static final RegistryObject<Item> BLOCK_MULTI_FRAME_CPU = ITEMS.register("multiblock_cpu", () -> new BlockItem(ModBlocks.BLOCK_MULTI_CPU.get(), ITEM_GROUP));
    public static final RegistryObject<Item> BLOCK_MULTI_FRAME_STORAGE = ITEMS.register("multiblock_storage", () -> new BlockItem(ModBlocks.BLOCK_MULTI_STORAGE.get(), ITEM_GROUP));

    public static final RegistryObject<Item> STORAGE_DISK_256k = ITEMS.register("small_item_disk", () -> new ItemRebornStorageCell(256000, StorageType.ITEM));
    public static final RegistryObject<Item> STORAGE_DISK_1024k = ITEMS.register("medium_item_disk", () -> new ItemRebornStorageCell(1024000, StorageType.ITEM));
    public static final RegistryObject<Item> STORAGE_DISK_4096K = ITEMS.register("large_item_disk", () -> new ItemRebornStorageCell(4096000, StorageType.ITEM));
    public static final RegistryObject<Item> STORAGE_DISK_16384K = ITEMS.register("larger_item_disk", () -> new ItemRebornStorageCell(16384000, StorageType.ITEM));

    public static final RegistryObject<Item> STORAGE_DISK_FLUID_256k = ITEMS.register("small_fluid_disk", () -> new ItemFluidRebornStorageCell(4096000, StorageType.FLUID));
    public static final RegistryObject<Item> STORAGE_DISK_FLUID_1024k = ITEMS.register("medium_fluid_disk", () -> new ItemFluidRebornStorageCell(16384000, StorageType.FLUID));
    public static final RegistryObject<Item> STORAGE_DISK_FLUID_4096K = ITEMS.register("large_fluid_disk", () -> new ItemFluidRebornStorageCell(49152000, StorageType.FLUID));
    public static final RegistryObject<Item> STORAGE_DISK_FLUID_16384K = ITEMS.register("larger_fluid_disk", () -> new ItemFluidRebornStorageCell(147456000, StorageType.FLUID));

    public static final RegistryObject<Item> STORAGE_DISK_256k_PART = ITEMS.register("small_item_disk_part", () -> new Item(PROPERTIES));
    public static final RegistryObject<Item> STORAGE_DISK_1024k_PART = ITEMS.register("medium_item_disk_part", () -> new Item(PROPERTIES));
    public static final RegistryObject<Item> STORAGE_DISK_4096K_PART = ITEMS.register("large_item_disk_part", () -> new Item(PROPERTIES));
    public static final RegistryObject<Item> STORAGE_DISK_16384K_PART = ITEMS.register("larger_item_disk_part", () -> new Item(PROPERTIES));

    public static final RegistryObject<Item> STORAGE_DISK_256k_FLUID_PART = ITEMS.register("small_fluid_disk_part", () -> new Item(PROPERTIES));
    public static final RegistryObject<Item> STORAGE_DISK_1024k_FLUID_PART = ITEMS.register("medium_fluid_disk_part", () -> new Item(PROPERTIES));
    public static final RegistryObject<Item> STORAGE_DISK_4096K_FLUID_PART = ITEMS.register("large_fluid_disk_part", () -> new Item(PROPERTIES));
    public static final RegistryObject<Item> STORAGE_DISK_16384K_FLUID_PART = ITEMS.register("larger_fluid_disk_part", () -> new Item(PROPERTIES));

    public static final RegistryObject<Item> RAW_SUPER_ADVANCED_PROCESSOR = ITEMS.register("raw_super_advanced_processor", () -> new Item(PROPERTIES));
    public static final RegistryObject<Item> SUPER_ADVANCED_PROCESSOR = ITEMS.register("super_advanced_processor", () -> new Item(PROPERTIES));

    public static final RegistryObject<Item> WIRELESS_GRID = ITEMS.register("super_wireless_crafting_grid", () -> new ItemWirelessGrid(PROPERTIES.stacksTo(1).rarity(Rarity.EPIC), ItemWirelessGrid.Type.NORMAL, () -> 100000));
    public static final RegistryObject<Item> CREATIVE_WIRELESS_GRID = ITEMS.register("creative_super_wireless_crafting_grid", () -> new ItemWirelessGrid(PROPERTIES.stacksTo(1).rarity(Rarity.EPIC), ItemWirelessGrid.Type.CREATIVE, () -> 100000));

    public static final RegistryObject<Item> ADVANCED_WIRELESS_TRANSMITTER_ITEM = ITEMS.register("advanced_wireless_transmitter", () -> new BaseBlockItem((BaseBlock) ModBlocks.BLOCK_ADVANCED_WIRELESS_TRANSMITTER.get(), ITEM_GROUP));
}

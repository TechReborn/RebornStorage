package net.gigabit101.rebornstorage.items;

import com.refinedmods.refinedstorage.RSItems;
import com.refinedmods.refinedstorage.api.storage.StorageType;
import com.refinedmods.refinedstorage.api.storage.disk.IStorageDisk;
import com.refinedmods.refinedstorage.api.storage.disk.IStorageDiskProvider;
import com.refinedmods.refinedstorage.api.storage.disk.StorageDiskSyncData;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.storage.FluidStorageType;
import com.refinedmods.refinedstorage.item.FluidStoragePartItem;
import com.refinedmods.refinedstorage.render.Styles;
import net.gigabit101.rebornstorage.init.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class ItemRebornStorageCell extends Item implements IStorageDiskProvider
{
    private static final String NBT_ID = "id";

    private final int capacity;
    private final StorageType storageType;

    public ItemRebornStorageCell(int capacity, StorageType storageType)
    {
        super(new Item.Properties().stacksTo(1));
        this.capacity = capacity;
        this.storageType = storageType;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected)
    {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (!world.isClientSide && !stack.hasTag() && entity instanceof Player)
        {
            UUID id = UUID.randomUUID();
            API.instance().getStorageDiskManager((ServerLevel) world).set(id, API.instance().createDefaultItemDisk((ServerLevel) world, getCapacity(stack), (Player) entity));
            API.instance().getStorageDiskManager((ServerLevel) world).markForSaving();
            setId(stack, id);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<net.minecraft.network.chat.Component> tooltip, TooltipFlag flag)
    {
        super.appendHoverText(stack, level, tooltip, flag);
        if (isValid(stack))
        {
            UUID id = getId(stack);

            API.instance().getStorageDiskSync().sendRequest(id);

            StorageDiskSyncData data = API.instance().getStorageDiskSync().getData(id);

            if (data != null)
            {
                if (data.getCapacity() == -1)
                {
                    tooltip.add(Component.translatable("misc.refinedstorage.storage.stored", API.instance().getQuantityFormatter().format(data.getStored())).setStyle(Styles.GRAY));
                } else
                {
                    tooltip.add(Component.translatable("misc.refinedstorage.storage.stored_capacity", API.instance().getQuantityFormatter().format(data.getStored()), API.instance().getQuantityFormatter().format(data.getCapacity())).setStyle(Styles.GRAY));
                }
            }
            if (flag.isAdvanced())
            {
                tooltip.add(Component.literal(id.toString()));
            }
        }
    }

    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand)
    {
        ItemStack diskStack = player.getItemInHand(hand);

        if (!level.isClientSide && player.isCrouching())
        {
            IStorageDisk<?> disk = API.instance().getStorageDiskManager((ServerLevel)level).getByStack(diskStack);
            if (disk != null && disk.getStored() == 0)
            {
                ItemStack storagePart = getStoragePart(diskStack);
                if (!player.getInventory().add(storagePart.copy()))
                {
                    Containers.dropItemStack(level, player.getX(), player.getY(), player.getZ(), storagePart);
                }
                API.instance().getStorageDiskManager((ServerLevel)level).remove(this.getId(diskStack));
                API.instance().getStorageDiskManager((ServerLevel)level).markForSaving();
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, new ItemStack((ItemLike) RSItems.STORAGE_HOUSING.get()));
            }
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, diskStack);
    }

    public ItemStack getStoragePart(ItemStack stack)
    {
        if(stack.getItem() == ModItems.STORAGE_DISK_256k.get()) return new ItemStack(ModItems.STORAGE_DISK_256k_PART);
        if(stack.getItem() == ModItems.STORAGE_DISK_1024k.get()) return new ItemStack(ModItems.STORAGE_DISK_1024k_PART);
        if(stack.getItem() == ModItems.STORAGE_DISK_4096K.get()) return new ItemStack(ModItems.STORAGE_DISK_4096K_PART);
        if(stack.getItem() == ModItems.STORAGE_DISK_16384K.get()) return new ItemStack(ModItems.STORAGE_DISK_16384K_PART);
        return ItemStack.EMPTY;
    }

    @Override
    public int getEntityLifespan(ItemStack stack, Level world)
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public UUID getId(ItemStack disk)
    {
        return disk.getTag().getUUID(NBT_ID);
    }

    @Override
    public void setId(ItemStack disk, UUID id)
    {
        disk.setTag(new CompoundTag());
        disk.getTag().putUUID(NBT_ID, id);
    }

    @Override
    public boolean isValid(ItemStack disk)
    {
        return disk.hasTag() && disk.getTag().hasUUID(NBT_ID);
    }

    @Override
    public int getCapacity(ItemStack disk)
    {
        return this.capacity;
    }

    @Override
    public StorageType getType()
    {
        return storageType;
    }
}

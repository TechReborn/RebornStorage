package net.gigabit101.rebornstorage.items;

import com.refinedmods.refinedstorage.api.storage.StorageType;
import com.refinedmods.refinedstorage.api.storage.disk.IStorageDiskProvider;
import com.refinedmods.refinedstorage.api.storage.disk.StorageDiskSyncData;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.storage.FluidStorageType;
import com.refinedmods.refinedstorage.render.Styles;
import net.gigabit101.rebornstorage.client.CreativeTabRebornStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class ItemFluidRebornStorageCell extends Item implements IStorageDiskProvider
{
    private static final String NBT_ID = "Id";

    private final int capacity;

    public ItemFluidRebornStorageCell(int capacity, StorageType storageType)
    {
        super(new Properties().tab(CreativeTabRebornStorage.INSTANCE).stacksTo(1));
        this.capacity = capacity;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected)
    {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (!world.isClientSide && !stack.hasTag() && entity instanceof Player)
        {
            UUID id = UUID.randomUUID();
            API.instance().getStorageDiskManager((ServerLevel)world).set(id, API.instance().createDefaultFluidDisk((ServerLevel)world, this.getCapacity(stack), (Player)entity));
            API.instance().getStorageDiskManager((ServerLevel)world).markForSaving();
            this.setId(stack, id);
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
                    tooltip.add(new TranslatableComponent("misc.refinedstorage.storage.stored", API.instance().getQuantityFormatter().format(data.getStored())).setStyle(Styles.GRAY));
                } else
                {
                    tooltip.add(new TranslatableComponent("misc.refinedstorage.storage.stored_capacity", API.instance().getQuantityFormatter().format(data.getStored()), API.instance().getQuantityFormatter().format(data.getCapacity())).setStyle(Styles.GRAY));
                }
            }
            if (flag.isAdvanced())
            {
                tooltip.add(new TextComponent(id.toString()));
            }
        }
    }


    @Override
    public int getEntityLifespan(ItemStack stack, Level world)
    {
        return 2147483647;
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
        return StorageType.FLUID;
    }
}

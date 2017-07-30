package RebornStorage.items;

import RebornStorage.init.EnumItemStorage;
import RebornStorage.lib.ModInfo;
import com.raoulvdberge.refinedstorage.api.storage.IStorageDisk;
import com.raoulvdberge.refinedstorage.api.storage.IStorageDiskProvider;
import com.raoulvdberge.refinedstorage.api.storage.StorageDiskType;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ItemRebornStorageCell extends ItemBase implements IStorageDiskProvider<ItemStack>
{
	public static final String[] types = new String[] {"256k", "1024k", "4096k", "16384k" };

	public ItemRebornStorageCell()
    {
		this.setUnlocalizedName(ModInfo.MOD_ID + ".storagecell");
		this.setHasSubtypes(true);
		this.setRegistryName(ModInfo.MOD_ID, "storagecell");
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if(isInCreativeTab(tab)){
			for (int meta = 0; meta < EnumItemStorage.values().length; meta++)
			{
				subItems.add(API.instance().getDefaultStorageDiskBehavior().initDisk(StorageDiskType.ITEMS, new ItemStack(this, 1, meta)));
			}
		}
	}

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected)
    {
        super.onUpdate(stack, world, entity, slot, selected);
        if (!stack.hasTagCompound())
        {
            API.instance().getDefaultStorageDiskBehavior().initDisk(StorageDiskType.ITEMS, stack);
        }
    }

    @Override
    public void addInformation(ItemStack disk, World world, List<String> tooltip, ITooltipFlag flag) {
        IStorageDisk storage = create(disk);
        if (storage.isValid(disk))
        {
            if (storage.getCapacity() == -1)
            {
                tooltip.add(I18n.format("misc.refinedstorage:storage.stored", storage.getStored()));
            }
            else
            {
                tooltip.add(I18n.format("misc.refinedstorage:storage.stored_capacity", storage.getStored(), storage.getCapacity()));
            }
        }
    }

	@Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        super.onCreated(stack, world, player);
        API.instance().getDefaultStorageDiskBehavior().initDisk(StorageDiskType.ITEMS, stack);
    }

    @Override
    public int getEntityLifespan(ItemStack stack, World world)
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack)
    {
        return API.instance().getDefaultStorageDiskBehavior().getShareTag(StorageDiskType.ITEMS, stack);
    }

    @Nonnull
    @Override
    public IStorageDisk<ItemStack> create(ItemStack disk)
    {
        return API.instance().getDefaultStorageDiskBehavior().createItemStorage(disk.getTagCompound(), EnumItemStorage.getById(disk.getItemDamage()).getCap());
    }

    @Override
    public String getUnlocalizedName()
    {
        return "item." + ModInfo.MOD_ID + ":" + "storagecell";
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        int meta = itemStack.getItemDamage();
        if (meta < 0 || meta >= EnumItemStorage.values().length)
        {
            meta = 0;
        }
        return super.getUnlocalizedName() + "." + types[meta];
    }
}

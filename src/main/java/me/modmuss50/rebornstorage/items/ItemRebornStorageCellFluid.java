package me.modmuss50.rebornstorage.items;

import com.raoulvdberge.refinedstorage.api.storage.disk.IStorageDisk;
import com.raoulvdberge.refinedstorage.api.storage.disk.StorageDiskType;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import com.raoulvdberge.refinedstorage.block.FluidStorageType;
import me.modmuss50.rebornstorage.init.EnumFluidStorage;
import me.modmuss50.rebornstorage.lib.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ItemRebornStorageCellFluid extends ItemRebornStorageCellBase {
	public static final String[] types = new String[] { "1024k", "4096k", "16384k", "32768k" };

	public ItemRebornStorageCellFluid() {
		setUnlocalizedName(ModInfo.MOD_ID + ".storagecellfluid");
		setHasSubtypes(true);
		setRegistryName("storagecellfluid");
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			for (int meta = 0; meta < EnumFluidStorage.values().length; meta++) {
				list.add(new ItemStack(this, 1, meta));
			}
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= types.length) {
			meta = 0;
		}
		return super.getUnlocalizedName() + "." + types[meta];
	}

	@Override
	public int getCapacity(ItemStack disk) {
		return FluidStorageType.getById(disk.getItemDamage()).getCapacity();
	}

	@Override
	public StorageDiskType getType() {
		return StorageDiskType.FLUID;
	}

	@Override
	public IStorageDisk createDefaultDisk(World world, int capacity) {
		return API.instance().createDefaultFluidDisk(world, capacity);
	}
}

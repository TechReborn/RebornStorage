package me.modmuss50.rebornstorage.items;

import com.raoulvdberge.refinedstorage.api.storage.disk.IStorageDisk;
import com.raoulvdberge.refinedstorage.api.storage.disk.StorageDiskType;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import com.raoulvdberge.refinedstorage.block.ItemStorageType;
import me.modmuss50.rebornstorage.init.EnumItemStorage;
import me.modmuss50.rebornstorage.lib.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ItemRebornStorageCell extends ItemRebornStorageCellBase {
	public static final String[] types = new String[] { "256k", "1024k", "4096k", "16384k" };

	public ItemRebornStorageCell() {
		this.setUnlocalizedName(ModInfo.MOD_ID + ".storagecell");
		this.setHasSubtypes(true);
		this.setRegistryName(ModInfo.MOD_ID, "storagecell");
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (isInCreativeTab(tab)) {
			for (int meta = 0; meta < EnumItemStorage.values().length; meta++) {
				subItems.add(new ItemStack(this, 1, meta));
			}
		}
	}

	@Override
	public String getUnlocalizedName() {
		return "item." + ModInfo.MOD_ID + ":" + "storagecell";
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= EnumItemStorage.values().length) {
			meta = 0;
		}
		return super.getUnlocalizedName() + "." + types[meta];
	}

	@Override
	public int getCapacity(ItemStack disk) {
		return ItemStorageType.getById(disk.getItemDamage()).getCapacity();
	}

	@Override
	public StorageDiskType getType() {
		return StorageDiskType.ITEM;
	}

	@Override
	public IStorageDisk createDefaultDisk(World world, int capacity) {
		return API.instance().createDefaultItemDisk(world, capacity);
	}
}

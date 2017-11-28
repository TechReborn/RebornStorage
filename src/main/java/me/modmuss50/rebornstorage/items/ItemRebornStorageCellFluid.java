package me.modmuss50.rebornstorage.items;

import me.modmuss50.rebornstorage.init.EnumFluidStorage;
import me.modmuss50.rebornstorage.lib.ModInfo;
import com.raoulvdberge.refinedstorage.api.storage.IStorageDisk;
import com.raoulvdberge.refinedstorage.api.storage.IStorageDiskProvider;
import com.raoulvdberge.refinedstorage.api.storage.StorageDiskType;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ItemRebornStorageCellFluid extends ItemBase implements IStorageDiskProvider<FluidStack> {
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
				list.add(API.instance().getDefaultStorageDiskBehavior().initDisk(StorageDiskType.FLUIDS, new ItemStack(this, 1, meta)));
			}
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.onUpdate(stack, world, entity, slot, selected);
		if (!stack.hasTagCompound()) {
			API.instance().getDefaultStorageDiskBehavior().initDisk(StorageDiskType.FLUIDS, stack);
		}
	}

	@Override
	public void addInformation(ItemStack disk, World world, List<String> tooltip, ITooltipFlag flag) {
		IStorageDisk storage = create(disk);
		if (storage.isValid(disk)) {
			if (storage.getCapacity() == -1) {
				tooltip.add(I18n.format("misc.refinedstorage:storage.stored", storage.getStored()));
			} else {
				tooltip.add(I18n.format("misc.refinedstorage:storage.stored_capacity", storage.getStored(), storage.getCapacity()));
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
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		super.onCreated(stack, world, player);
		API.instance().getDefaultStorageDiskBehavior().initDisk(StorageDiskType.FLUIDS, stack);
	}

	@Override
	public int getEntityLifespan(ItemStack stack, World world) {
		return Integer.MAX_VALUE;
	}

	@Override
	public NBTTagCompound getNBTShareTag(ItemStack stack) {
		return API.instance().getDefaultStorageDiskBehavior().getShareTag(StorageDiskType.FLUIDS, stack);
	}

	@Nonnull
	@Override
	public IStorageDisk<FluidStack> create(ItemStack disk) {
		return API.instance().getDefaultStorageDiskBehavior().createFluidStorage(disk.getTagCompound(), EnumFluidStorage.getById(disk.getItemDamage()).getCap());
	}
}

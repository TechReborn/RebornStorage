package me.modmuss50.rebornstorage.items;

import com.raoulvdberge.refinedstorage.RSItems;
import com.raoulvdberge.refinedstorage.api.storage.disk.IStorageDisk;
import com.raoulvdberge.refinedstorage.api.storage.disk.IStorageDiskProvider;
import com.raoulvdberge.refinedstorage.api.storage.disk.IStorageDiskSyncData;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import me.modmuss50.rebornstorage.init.ModItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public abstract class ItemRebornStorageCellBase extends ItemBase implements IStorageDiskProvider {
	private static final String NBT_ID = "id";

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.onUpdate(stack, world, entity, slot, selected);
		if (!world.isRemote) {
			if (!isValid(stack)) {
				API.instance().getOneSixMigrationHelper().migrateDisk(world, stack);
			}

			if (!stack.hasTagCompound()) {
				UUID id = UUID.randomUUID();
				API.instance().getStorageDiskManager(world).set(id, createDefaultDisk(world, getCapacity(stack)));
				API.instance().getStorageDiskManager(world).markForSaving();
				setId(stack, id);
			}
		}
	}

	public abstract IStorageDisk createDefaultDisk(World world, int capacity);

	@Override
	public void addInformation(ItemStack disk, World world, List<String> tooltip, ITooltipFlag flag) {
		if (isValid(disk)) {
			UUID id = getId(disk);
			API.instance().getStorageDiskSync().sendRequest(id);
			IStorageDiskSyncData data = API.instance().getStorageDiskSync().getData(id);
			if (data != null) {
				if (data.getCapacity() == -1) {
					tooltip.add(I18n.format("misc.refinedstorage:storage.stored", API.instance().getQuantityFormatter().format(data.getStored())));
				} else {
					tooltip.add(I18n.format("misc.refinedstorage:storage.stored_capacity", API.instance().getQuantityFormatter().format(data.getStored()), API.instance().getQuantityFormatter().format(data.getCapacity())));
				}
			}
			if (flag.isAdvanced()) {
				tooltip.add(id.toString());
			}
		}
	}

	@Override
	public int getEntityLifespan(ItemStack stack, World world) {
		return Integer.MAX_VALUE;
	}

	@Override
	public UUID getId(ItemStack disk) {
		return disk.getTagCompound().getUniqueId(NBT_ID);
	}

	@Override
	public void setId(ItemStack disk, UUID id) {
		disk.setTagCompound(new NBTTagCompound());
		disk.getTagCompound().setUniqueId(NBT_ID, id);
	}

	@Override
	public boolean isValid(ItemStack disk) {
		return disk.hasTagCompound() && disk.getTagCompound().hasUniqueId(NBT_ID);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		if(!worldIn.isRemote && playerIn.isSneaking()){
			IStorageDisk disk = API.instance().getStorageDiskManager(worldIn).getByStack(stack);
			if(disk != null && disk.getStored() == 0){
				ItemStack part = new ItemStack(ModItems.REBORN_STORAGE_PART, 1, stack.getItemDamage() + (this.getClass() == ItemRebornStorageCellFluid.class ? 4 : 0));

				if (!playerIn.inventory.addItemStackToInventory(part.copy())) {
					InventoryHelper.spawnItemStack(worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ(), part);
				}

				API.instance().getStorageDiskManager(worldIn).remove(getId(stack));
				API.instance().getStorageDiskManager(worldIn).markForSaving();

				return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(RSItems.STORAGE_HOUSING));
			}

		}
		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

}

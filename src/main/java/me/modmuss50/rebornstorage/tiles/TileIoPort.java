package me.modmuss50.rebornstorage.tiles;

import javax.annotation.Nullable;

import me.modmuss50.rebornstorage.multiblocks.MultiBlockCrafter;
import me.modmuss50.rebornstorage.tiles.CraftingNode.CachingItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by tomKPZ on 05/09/2018.
 */
public class TileIoPort extends TileMultiCrafter implements IItemHandler {
	@Override
	public boolean hasCapability(Capability<?> capability,
	                             @Nullable
		                             EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return getVarient().equals("io");
		}
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability,
	                           @Nullable
		                           EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) this;
		}
		return super.getCapability(capability, facing);
	}

	private class Slot {
		private IItemHandler inv;
		private int slot;

		Slot(IItemHandler inv, int slot) {
		this.inv = inv;
		this.slot = slot;
		}

		public ItemStack extractItem(int amount, boolean simulate) {
		return inv.extractItem(slot, amount, simulate);
		}

		public ItemStack getStack() {
		return inv.getStackInSlot(slot);
		}

		public ItemStack insertItem(ItemStack stack, boolean simulate) {
		return inv.insertItem(slot, stack, simulate);
		}
	}

	private Slot getFirstAvailable() {
		MultiBlockCrafter multiBlock = getMultiBlock();
		if (multiBlock == null || !multiBlock.isAssembled()) {
			return null;
		}
		for (int i = 1; i <= multiBlock.pages; ++i) {
			CachingItemHandler inv = multiBlock.getInvForPage(i);
			if (!inv.isFull()) {
				return new Slot(inv, inv.getFirstAvailable());
			}
		}
		return null;
	}

	private Slot getLastUsed() {
		MultiBlockCrafter multiBlock = getMultiBlock();
		if (multiBlock == null || !multiBlock.isAssembled()) {
			return null;
		}
		for (int i = multiBlock.pages; i >= 1; --i) {
			CachingItemHandler inv = multiBlock.getInvForPage(i);
			if (!inv.isEmpty()) {
				return new Slot(inv, inv.getLastUsed());
			}
		}
		return null;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot != 1) {
			return ItemStack.EMPTY;
		}
		Slot lastUsed = getLastUsed();
		return lastUsed == null ? ItemStack.EMPTY : lastUsed.extractItem(amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	@Override
	public int getSlots() {
		// Slot 0 is used for insertion and slot 1 is used for extraction.
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot != 1) {
			return ItemStack.EMPTY;
		}
		Slot lastUsed = getLastUsed();
		return lastUsed == null ? ItemStack.EMPTY : lastUsed.getStack();
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (slot != 0) {
			return stack;
		}
		Slot firstAvailable = getFirstAvailable();
		return firstAvailable == null ? stack : firstAvailable.insertItem(stack, simulate);
	}
}

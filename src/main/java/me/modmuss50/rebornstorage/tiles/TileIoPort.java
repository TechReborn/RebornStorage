package me.modmuss50.rebornstorage.tiles;

import javax.annotation.Nullable;

import me.modmuss50.rebornstorage.client.gui.GuiMultiCrafter;
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

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		int page = slot / GuiMultiCrafter.maxSlotsPerPage + 1;
		slot %= GuiMultiCrafter.maxSlotsPerPage;
		return getMultiBlock().getInvForPage(page).extractItem(slot, amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	@Override
	public int getSlots() {
		return GuiMultiCrafter.maxSlotsPerPage * getMultiBlock().pages;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		int page = slot / GuiMultiCrafter.maxSlotsPerPage + 1;
		slot %= GuiMultiCrafter.maxSlotsPerPage;
		return getMultiBlock().getInvForPage(page).getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		int page = slot / GuiMultiCrafter.maxSlotsPerPage + 1;
		slot %= GuiMultiCrafter.maxSlotsPerPage;
		return getMultiBlock().getInvForPage(page).insertItem(slot, stack, simulate);
	}
}

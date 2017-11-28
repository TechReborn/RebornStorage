package me.modmuss50.rebornstorage.client;

import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

/**
 * Created by Gigabit101 on 06/01/2017.
 */
public class SlotFiltered extends SlotItemHandler {
	public SlotFiltered(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(
		@Nullable
			ItemStack stack) {
		if (!stack.isEmpty() && stack.getItem() instanceof ICraftingPatternProvider) {
			return true;
		}
		return false;
	}
}

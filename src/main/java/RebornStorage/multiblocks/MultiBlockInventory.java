package RebornStorage.multiblocks;

import net.minecraft.item.ItemStack;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.util.Inventory;

public class MultiBlockInventory extends Inventory {

	MultiblockControllerBase controllerBase;

	public MultiBlockInventory(int size, String invName, int invStackLimit, MultiblockControllerBase controllerBase ) {
		super(size, invName, invStackLimit, null);
		this.controllerBase = controllerBase;
	}

	@Override
	public void markDirty() {
		//nope
	}

}

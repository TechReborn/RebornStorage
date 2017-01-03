package RebornStorage.tiles;

import RebornStorage.multiblocks.MultiBlockCrafter;
import com.raoulvdberge.refinedstorage.RS;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternProvider;
import com.raoulvdberge.refinedstorage.api.network.INetworkMaster;
import com.raoulvdberge.refinedstorage.api.network.INetworkNode;
import com.raoulvdberge.refinedstorage.inventory.ItemHandlerUpgrade;
import com.raoulvdberge.refinedstorage.tile.data.TileDataManager;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.MultiblockValidationException;
import reborncore.common.multiblock.rectangular.RectangularMultiblockTileEntityBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mark on 03/01/2017.
 */
public class TileMultiCrafter extends RectangularMultiblockTileEntityBase implements ICraftingPatternContainer, INetworkNode {

	@Override
	public void isGoodForFrame() throws MultiblockValidationException {

	}

	@Override
	public void isGoodForSides() throws MultiblockValidationException {

	}

	@Override
	public void isGoodForTop() throws MultiblockValidationException {

	}

	@Override
	public void isGoodForBottom() throws MultiblockValidationException {

	}

	@Override
	public void isGoodForInterior() throws MultiblockValidationException {

	}

	@Override
	public void onMachineActivated() {

	}

	@Override
	public void onMachineDeactivated() {

	}

	@Override
	public MultiblockControllerBase createNewMultiblock() {
		return new MultiBlockCrafter(getWorld());
	}

	@Override
	public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
		return MultiBlockCrafter.class;
	}

	public MultiBlockCrafter getMultiBlock() {
		return (MultiBlockCrafter) getMultiblockController();
	}

	//RS stuff
	private List<ICraftingPattern> actualPatterns = new ArrayList();
	private ItemHandlerUpgrade upgrades = new ItemHandlerUpgrade(4, this, new int[] { 2 });
	private boolean triggeredAutocrafting = false;
	protected TileDataManager dataManager;
	protected int ticks;
	protected INetworkMaster network;

	public TileMultiCrafter() {
		this.dataManager = new TileDataManager(this);
	}

	private void rebuildPatterns() {
		this.actualPatterns.clear();

		if (getMultiBlock() == null) {
			return;
		}
		for (int i = 0; i < getMultiBlock().inv.getSlots(); ++i) {
			ItemStack patternStack = getMultiBlock().inv.getStackInSlot(i);
			if (patternStack != null) {
				ICraftingPattern pattern = ((ICraftingPatternProvider) patternStack.getItem()).create(this.getWorld(), patternStack, this);
				if (pattern.isValid()) {
					this.actualPatterns.add(pattern);
				}
			}
		}

	}

	public int getEnergyUsage() {
		if (getMultiBlock() == null) {
			return 0;
		}
		int usage = RS.INSTANCE.config.crafterUsage + this.upgrades.getEnergyUsage();

		for (int i = 0; i < getMultiBlock().inv.getSlots(); ++i) {
			if (getMultiBlock().inv.getStackInSlot(i) != null) {
				usage += RS.INSTANCE.config.crafterPerPatternUsage;
			}
		}

		return usage;
	}

	public void update() {
		if (!this.getWorld().isRemote && this.ticks == 0) {
			this.rebuildPatterns();
		}
		this.rebuildPatterns();
		if (!this.getWorld().isRemote) {
			++this.ticks;
			this.dataManager.detectAndSendChanges();
		}
		updateNode();
	}

	public void updateNode() {
		if (this.triggeredAutocrafting && this.getWorld().isBlockPowered(this.pos)) {
			Iterator var1 = this.actualPatterns.iterator();

			while (var1.hasNext()) {
				ICraftingPattern pattern = (ICraftingPattern) var1.next();
				Iterator var3 = pattern.getOutputs().iterator();

				while (var3.hasNext()) {
					ItemStack output = (ItemStack) var3.next();
					this.network.scheduleCraftingTask(output, 1, 3);
				}
			}
		}

	}

	public void onConnectionChange(INetworkMaster network, boolean state) {
		if (!state) {
			network.getCraftingTasks().stream().filter((task) -> task.getPattern().getContainer().getPosition().equals(this.pos)).forEach(network::cancelCraftingTask);
		}

		network.rebuildPatterns();
	}

	public int getSpeedUpdateCount() {
		return this.upgrades.getUpgradeCount(2);
	}

	public IItemHandler getFacingInventory() {
		return null;
	}

	@Override
	public TileEntity getFacingTile() {
		return null;
	}

	public List<ICraftingPattern> getPatterns() {
		return this.actualPatterns;
	}

	@Override
	public BlockPos getPosition() {
		return null;
	}

	@Override
	public void onConnected(INetworkMaster iNetworkMaster) {
		network = iNetworkMaster;
		onConnectionChange(network, true);
	}

	@Override
	public void onDisconnected(INetworkMaster iNetworkMaster) {
		onConnectionChange(network, false);
		network = null;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public boolean canConduct(EnumFacing enumFacing) {
		return true;
	}

	@Override
	public INetworkMaster getNetwork() {
		return network;
	}

	@Override
	public World getNodeWorld() {
		return world;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (getMultiblockController() != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (getMultiblockController() != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getMultiBlock().inv);
		}
		return super.getCapability(capability, facing);
	}

}

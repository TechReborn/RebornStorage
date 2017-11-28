package RebornStorage.tiles;

import RebornStorage.init.ModBlocks;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternProvider;
import com.raoulvdberge.refinedstorage.api.network.INetwork;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNode;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CraftingNode implements INetworkNode, ICraftingPatternContainer {

	TileMultiCrafter multiCrafter;
	List<ICraftingPattern> patterns = new ArrayList<>();
	@Nullable
	INetwork network;
	int ticks = 0;

	public CraftingNode(TileMultiCrafter multiCrafter) {
		this.multiCrafter = multiCrafter;
	}

	public void rebuildPatterns() {
		this.patterns.clear();

		ICraftingPatternContainer patternContainer = this;

		if(multiCrafter.inv != null && multiCrafter.getMultiBlock() != null){
			patterns.addAll(Arrays.stream(multiCrafter.inv.contents)
				.filter(stack -> !stack.isEmpty())
				.map(stack -> ((ICraftingPatternProvider)stack.getItem()).create(multiCrafter.getWorld(), stack, patternContainer))
				.filter(ICraftingPattern::isValid)
				.collect(Collectors.toList()));
		}

		if(getNetwork() != null){
			getNetwork().getCraftingManager().rebuild();
		}

	}

	protected void stateChange(INetwork network, boolean state) {
		if (!state) {
			network.getCraftingManager().getTasks().stream()
				.filter((task) -> task.getPattern().getContainer().getPosition().equals(getPos()))
				.forEach((task) -> network.getCraftingManager().cancel(task));
			patterns.clear();
		}
		network.getCraftingManager().rebuild();
	}

	@Override
	public int getEnergyUsage() {
		return 1;
	}

	@Nonnull
	@Override
	public ItemStack getItemStack() {
		return multiCrafter.getStack();
	}

	@Override
	public void onConnected(INetwork iNetwork) {
		this.network = iNetwork;
		stateChange(network, true);
		rebuildPatterns();
	}

	@Override
	public void onDisconnected(INetwork iNetwork) {
		this.network = null;
		patterns.clear();
		stateChange(iNetwork, true);
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Nullable
	@Override
	public INetwork getNetwork() {
		return network;
	}

	@Override
	public void update() {
		ticks++;
		if(ticks == 1){
			rebuildPatterns();
		}
	}

	@Override
	public NBTTagCompound write(NBTTagCompound nbtTagCompound) {
		return null;
	}

	@Override
	public BlockPos getPos() {
		return multiCrafter.getPos();
	}

	@Override
	public World getWorld() {
		return multiCrafter.getWorld();
	}

	@Override
	public void markDirty() {

	}

	@Override
	public String getId() {
		return "rebornstorage-multiblockcrafter";
	}

	@Override
	public int getSpeedUpdateCount() {
		if(multiCrafter.getMultiBlock() == null){
			return 0;
		}
		return multiCrafter.getMultiBlock().speed;
	}

	@Override
	public IItemHandler getFacingInventory() {
		return null;
	}

	@Override
	public TileEntity getFacingTile() {
		return null;
	}

	@Override
	public List<ICraftingPattern> getPatterns() {
		return patterns;
	}

	@Override
	public BlockPos getPosition() {
		return getPos();
	}

	@Override
	public boolean isBlocked() {
		return false;
	}

	@Override
	public void setBlocked(boolean b) {

	}
}

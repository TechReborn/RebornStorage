package RebornStorage.tiles;

import RebornStorage.RebornStorage;
import RebornStorage.init.ModBlocks;
import com.raoulvdberge.refinedstorage.api.network.INetwork;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNode;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CraftingNode implements INetworkNode {

	TileMultiCrafter multiCrafter;

	public CraftingNode(TileMultiCrafter multiCrafter) {
		this.multiCrafter = multiCrafter;
	}

	@Override
	public int getEnergyUsage() {
		return 1;
	}

	@Nonnull
	@Override
	public ItemStack getItemStack() {
		return new ItemStack(ModBlocks.BLOCK_MULTI_CRAFTER);
	}

	@Override
	public void onConnected(INetwork iNetwork) {

	}

	@Override
	public void onDisconnected(INetwork iNetwork) {

	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Nullable
	@Override
	public INetwork getNetwork() {
		return null;
	}

	@Override
	public void update() {

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
		return null;
	}
}

package RebornStorage.tiles;

import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.api.network.INetworkMaster;
import com.raoulvdberge.refinedstorage.apiimpl.network.node.INetworkNodeHolder;
import com.raoulvdberge.refinedstorage.apiimpl.network.node.NetworkNode;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 23/02/2017.
 */
public class NetworkNodeMultiCrafter extends NetworkNode implements ICraftingPatternContainer {

	TileMultiCrafter crafter;

	public NetworkNodeMultiCrafter(TileMultiCrafter crafter) {
		super(crafter);
		this.crafter = crafter;
	}

	@Override
	public int getSpeedUpdateCount() {
		if (crafter.getMultiBlock() == null) {
			return 0;
		}
		return crafter.getMultiBlock().speed;
	}

	@Override
	public IItemHandler getFacingInventory() {
		return null;
	}

	@Override
	public List<ICraftingPattern> getPatterns() {
		if (crafter.getMultiBlock() == null) {
			return new ArrayList<>();
		}
		return crafter.getMultiBlock().actualPatterns;
	}

	@Override
	public BlockPos getPosition() {
		return crafter.getPos();
	}

	@Override
	public boolean isBlocked() {
		return false;
	}

	@Override
	public void setBlocked(boolean blocked) {

	}

	@Override
	public int getEnergyUsage() {
		if (crafter.getMultiBlock() == null) {
			return 0;
		}
		return crafter.getMultiBlock().powerUsage;
	}

	@Override
	public String getId() {
		return "rebornstorage.multicrafter";
	}

	@Override
	protected void onConnectedStateChange(INetworkMaster network, boolean state) {
		if (crafter.getMultiBlock() != null) {
			crafter.getMultiBlock().onConnectionChange(network, state, crafter.pos());
		}
		crafter.master = network;
	}
}

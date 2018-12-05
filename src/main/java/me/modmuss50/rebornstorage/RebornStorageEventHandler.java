package me.modmuss50.rebornstorage;

import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingManager;
import me.modmuss50.rebornstorage.lib.ModInfo;
import me.modmuss50.rebornstorage.tiles.CraftingNode;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.RebornCore;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

//This ensures that the network can only update at most once per tick
@Mod.EventBusSubscriber(modid = ModInfo.MOD_ID)
@RebornRegistry(modID = ModInfo.MOD_ID)
public class RebornStorageEventHandler {

	@ConfigRegistry(comment = "Enable debug log output (warning can be spammy)")
	public static boolean debugLogging = false;

	private static Queue<Pair<ICraftingManager, RebuildReason>> rebuildQueue = new LinkedList<>();

	public static void queue(ICraftingManager craftingManager, CraftingNode node, String reason){
		for(ICraftingManager queued : rebuildQueue.stream().map(Pair::getLeft).collect(Collectors.toList())){
			if(queued.equals(craftingManager)){
				return;
			}
		}
		rebuildQueue.add(Pair.of(craftingManager, new RebuildReason(node.getPos(), node.getWorld().provider.getDimension(), reason)));
	}

	@SubscribeEvent
	public static void tick(TickEvent.WorldTickEvent event){
		if(event.phase == TickEvent.Phase.END && event.world.provider.getDimension() == 0){
			Pair<ICraftingManager, RebuildReason> rebuildReasonPair = rebuildQueue.poll();
			if(rebuildReasonPair != null){
				if(debugLogging){
					RebornCore.logHelper.info("Triggering cached crafting manager rebuild pos:" + rebuildReasonPair.getRight().toString());
				}
				rebuildReasonPair.getLeft().rebuild();
			}
		}
	}

	private static class RebuildReason {

		BlockPos pos;
		int worldId;
		String reason;

		public RebuildReason(BlockPos pos, int worldId, String reason) {
			this.pos = pos;
			this.worldId = worldId;
			this.reason = reason;
		}

		@Override
		public String toString() {
			return "RebuildReason{" +
				"pos=" + pos +
				", worldId=" + worldId +
				", reason='" + reason + '\'' +
				'}';
		}
	}

}

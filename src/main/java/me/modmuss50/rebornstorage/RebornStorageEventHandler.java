package me.modmuss50.rebornstorage;

import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingManager;
import me.modmuss50.rebornstorage.lib.ModInfo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedList;
import java.util.Queue;


//This ensures that the network can only update at most once per tick
@Mod.EventBusSubscriber(modid = ModInfo.MOD_ID)
public class RebornStorageEventHandler {

	private static Queue<ICraftingManager> rebuildQueue = new LinkedList<>();

	public static void queue(ICraftingManager craftingManager){
		if(!rebuildQueue.contains(craftingManager)){
			rebuildQueue.add(craftingManager);
		}
	}

	@SubscribeEvent
	public static void tick(TickEvent.WorldTickEvent event){
		if(event.phase == TickEvent.Phase.END && event.world.provider.getDimension() == 0){
			ICraftingManager manager = rebuildQueue.poll();
			if(manager != null){
				manager.rebuild();
			}
		}
	}

}

package net.gigabit101.rebornstorage;

import com.refinedmods.refinedstorage.api.autocrafting.ICraftingManager;
import net.gigabit101.rebornstorage.core.multiblock.MultiblockRegistry;
import net.gigabit101.rebornstorage.nodes.CraftingNode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class RebornStorageEventHandler
{
    public static boolean debugLogging = false;

    private static Queue<Pair<ICraftingManager, RebuildReason>> rebuildQueue = new LinkedList<>();

    public static void queue(ICraftingManager craftingManager, CraftingNode node, String reason)
    {
        if (node.getLevel().isClientSide)
        {
            return;
        }
        for (ICraftingManager queued : rebuildQueue.stream().map(Pair::getLeft).collect(Collectors.toList()))
        {
            if (queued.equals(craftingManager))
            {
                return;
            }
        }
        rebuildQueue.add(Pair.of(craftingManager, new RebuildReason(node.getPos(), node.getLevel().dimension().registry(), reason)));
    }

    @SubscribeEvent
    public static void tick(TickEvent.LevelTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            MultiblockRegistry.tickStart(event.level);
        }
        if (event.phase == TickEvent.Phase.END && !event.level.isClientSide())
        {
            Pair<ICraftingManager, RebuildReason> rebuildReasonPair = rebuildQueue.poll();
            if (rebuildReasonPair != null)
            {
                if (debugLogging)
                {
                    RebornStorage.logger.error("Triggering cached crafting manager rebuild pos:" + rebuildReasonPair.getRight().toString());
                }
                rebuildReasonPair.getLeft().invalidate();
            }
        }
    }

    private static class RebuildReason
    {
        BlockPos pos;
        ResourceLocation worldId;
        String reason;

        public RebuildReason(BlockPos pos, ResourceLocation worldId, String reason)
        {
            this.pos = pos;
            this.worldId = worldId;
            this.reason = reason;
        }

        @Override
        public String toString()
        {
            return "RebuildReason{" + "pos=" + pos + ", worldId=" + worldId + ", reason='" + reason + '\'' + '}';
        }
    }
}

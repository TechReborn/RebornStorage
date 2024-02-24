package net.gigabit101.rebornstorage.core.multiblock.events;

import net.gigabit101.rebornstorage.core.multiblock.MultiblockRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

public class MultiblockEventHandler
{
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onChunkLoad(ChunkEvent.Load event)
    {
        LevelChunk chunk = (LevelChunk) event.getChunk();
        LevelAccessor level = event.getChunk().getWorldForge();
        MultiblockRegistry.onChunkLoaded((Level) level, chunk);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onWorldUnload(LevelEvent.Unload event)
    {
        MultiblockRegistry.onWorldUnloaded((Level) event.getLevel());
    }
}

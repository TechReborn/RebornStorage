package net.gigabit101.rebornstorage.core.multiblock.events;

import net.gigabit101.rebornstorage.core.multiblock.MultiblockRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
    public void onWorldUnload(WorldEvent.Unload event)
    {
        MultiblockRegistry.onWorldUnloaded((Level) event.getWorld());
    }
}

package net.gigabit101.rebornstorage.core.multiblock.events;

import net.gigabit101.rebornstorage.core.multiblock.MultiblockRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.TickEvent;

public class MultiblockServerTickHandler
{
    @SubscribeEvent
    public void onWorldTick(TickEvent.LevelTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START) MultiblockRegistry.tickStart(event.level);
    }
}

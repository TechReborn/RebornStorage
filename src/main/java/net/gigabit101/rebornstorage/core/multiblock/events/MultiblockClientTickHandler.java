package net.gigabit101.rebornstorage.core.multiblock.events;

import net.gigabit101.rebornstorage.core.multiblock.MultiblockRegistry;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.TickEvent;

public class MultiblockClientTickHandler
{
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START) MultiblockRegistry.tickStart(Minecraft.getInstance().level);
    }
}

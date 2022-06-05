package net.gigabit101.rebornstorage;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RebornStorageClient
{
    @SubscribeEvent
    public static void textureStitch(TextureStitchEvent.Pre event)
    {
        event.addSprite(new ResourceLocation(Constants.MOD_ID, "items/grid"));
    }
}

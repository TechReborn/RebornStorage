package RebornStorage;

import RebornStorage.init.ModBlocks;
import RebornStorage.init.ModItems;
import RebornStorage.init.ModRecipes;
import RebornStorage.lib.ModInfo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
@Mod(name = ModInfo.MOD_NAME, modid = ModInfo.MOD_ID, version = ModInfo.MOD_VERSION)
public class RebornStorage
{
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        ModItems.init();
        ModBlocks.init();
        ModRecipes.init();
    }
}

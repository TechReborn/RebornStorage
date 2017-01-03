package RebornStorage;

import RebornStorage.init.ModBlocks;
import RebornStorage.init.ModItems;
import RebornStorage.init.ModRecipes;
import RebornStorage.lib.ModInfo;
import RebornStorage.proxys.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
@Mod(name = ModInfo.MOD_NAME, modid = ModInfo.MOD_ID, version = ModInfo.MOD_VERSION)
public class RebornStorage
{
    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_LOC, serverSide = ModInfo.COMMON_PROXY_LOC)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
        ModItems.init();
        ModBlocks.init();
        ModRecipes.init();
        proxy.registerRenders();
    }
}

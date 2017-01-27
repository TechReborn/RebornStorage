package RebornStorage;

import RebornStorage.client.GuiHandler;
import RebornStorage.init.ModBlocks;
import RebornStorage.init.ModItems;
import RebornStorage.init.ModRecipes;
import RebornStorage.lib.CommandBuildMultiBlock;
import RebornStorage.lib.ModInfo;
import RebornStorage.packet.Packets;
import RebornStorage.proxys.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
@Mod(name = ModInfo.MOD_NAME, modid = ModInfo.MOD_ID, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCUIES)
public class RebornStorage
{
    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_LOC, serverSide = ModInfo.COMMON_PROXY_LOC)
    public static CommonProxy proxy;

	@Mod.Instance
	public static RebornStorage INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
//        ModItems.init();
        ModBlocks.init();
        ModRecipes.init();
        proxy.registerRenders();
	    NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
	    MinecraftForge.EVENT_BUS.register(Packets.class);
    }

//    @Mod.EventHandler
//    public void serverStarted(FMLServerStartingEvent event){
//    	event.registerServerCommand(new CommandBuildMultiBlock());
//    }
}

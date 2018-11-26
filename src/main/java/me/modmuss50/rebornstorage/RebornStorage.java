package me.modmuss50.rebornstorage;

import me.modmuss50.rebornstorage.client.GuiHandler;
import me.modmuss50.rebornstorage.init.ModBlocks;
import me.modmuss50.rebornstorage.init.ModItems;
import me.modmuss50.rebornstorage.init.ModRecipes;
import me.modmuss50.rebornstorage.lib.CommandBuildMultiBlock;
import me.modmuss50.rebornstorage.lib.ModInfo;
import me.modmuss50.rebornstorage.packet.Packets;
import me.modmuss50.rebornstorage.proxys.CommonProxy;
import me.modmuss50.rebornstorage.tiles.CraftingNode;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import com.raoulvdberge.refinedstorage.util.StackUtils;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
@Mod(name = ModInfo.MOD_NAME, modid = ModInfo.MOD_ID, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCUIES, acceptedMinecraftVersions = "[1.12,1.12.2]", guiFactory = "me.modmuss50.rebornstorage.RebornStorageConfigGui")
public class RebornStorage {
	@SidedProxy(clientSide = ModInfo.CLIENT_PROXY_LOC, serverSide = ModInfo.COMMON_PROXY_LOC)
	public static CommonProxy proxy;

	@Mod.Instance
	public static RebornStorage INSTANCE;

	public static final String MULTI_BLOCK_ID = ModInfo.MOD_ID + ":multiblockcrafter";

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		ModItems.init();
		ModBlocks.init();
		proxy.registerRenders();
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(Packets.class);

		API.instance().getNetworkNodeRegistry().add(MULTI_BLOCK_ID, (tag, world, pos) -> {
			CraftingNode node = new CraftingNode(world, pos);
			StackUtils.readItems(node.patterns, 0, tag);
			if (API.instance().getOneSixMigrationHelper().migratePatternInventory(node.patterns)) {
				node.markDirty();
			}
			return node;
		});

		//Stops inv sorter from working in the container, fixing possible crashes
		FMLInterModComms.sendMessage("inventorysorter", "containerblacklist", "me.modmuss50.rebornstorage.client.gui.ContainerMultiCrafter");

	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		ModRecipes.init();
	}

	@Mod.EventHandler
	public void serverStarted(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandBuildMultiBlock());
	}
}

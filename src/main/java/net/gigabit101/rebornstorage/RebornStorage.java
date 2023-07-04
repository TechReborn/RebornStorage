package net.gigabit101.rebornstorage;

import com.refinedmods.refinedstorage.api.IRSAPI;
import com.refinedmods.refinedstorage.api.RSAPIInject;
import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.screen.KeyInputListener;
import com.refinedmods.refinedstorage.util.StackUtils;
import net.gigabit101.rebornstorage.client.KeyBindings;
import net.gigabit101.rebornstorage.core.multiblock.events.MultiblockClientTickHandler;
import net.gigabit101.rebornstorage.core.multiblock.events.MultiblockEventHandler;
import net.gigabit101.rebornstorage.core.multiblock.events.MultiblockServerTickHandler;
import net.gigabit101.rebornstorage.grid.crafting.WirelessCraftingGridGridFactory;
import net.gigabit101.rebornstorage.init.ModBlocks;
import net.gigabit101.rebornstorage.init.ModContainers;
import net.gigabit101.rebornstorage.init.ModItems;
import net.gigabit101.rebornstorage.init.ModScreens;
import net.gigabit101.rebornstorage.multiblocks.MultiBlockCrafter;
import net.gigabit101.rebornstorage.nodes.AdvancedWirelessTransmitterNode;
import net.gigabit101.rebornstorage.nodes.CraftingNode;
import net.gigabit101.rebornstorage.packet.PacketChangeMode;
import net.gigabit101.rebornstorage.packet.PacketHandler;
import net.gigabit101.rebornstorage.blockentities.BlockEntityMultiCrafter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

import javax.swing.text.TabExpander;
import javax.swing.text.TextAction;

@Mod(Constants.MOD_ID)
public class RebornStorage
{
    @RSAPIInject
    public static IRSAPI RSAPI;

    public static Logger logger = LogManager.getLogger();

    public static RebornStorage INSTANCE;

    public RebornStorage()
    {
        INSTANCE = this;
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        RebornStorageConfig.loadConfig(RebornStorageConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID + "-common.toml"));
        ModItems.ITEMS.register(eventBus);
        ModBlocks.BLOCKS.register(eventBus);
        ModBlocks.TILES_ENTITIES.register(eventBus);
        ModContainers.CONTAINERS.register(eventBus);
        eventBus.addListener(this::preInit);
        eventBus.addListener(this::clientInit);
        eventBus.addListener(this::registerCreativeTab);
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().size(4).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("grid").icon(new ResourceLocation(Constants.MOD_ID, "items/grid")).size(1).build());
        MinecraftForge.EVENT_BUS.register(new MultiblockEventHandler());
        MinecraftForge.EVENT_BUS.register(new MultiblockServerTickHandler());
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(this::keyRegisterEvent);
            MinecraftForge.EVENT_BUS.addListener(this::onKeyInput);
        });
    }

    @SubscribeEvent
    public void preInit(FMLCommonSetupEvent event)
    {
        PacketHandler.register();
        API.instance().getNetworkNodeRegistry().add(Constants.MULTI_BLOCK_ID, (tag, world, pos) ->
        {
            CraftingNode node = new CraftingNode(world, pos);
            StackUtils.readItems(node.patterns, 0, tag);
            return node;
        });
        API.instance().getNetworkNodeRegistry().add(AdvancedWirelessTransmitterNode.ID, (tag, world, pos) -> readAndReturn(tag, new AdvancedWirelessTransmitterNode(world, pos)));
        API.instance().getGridManager().add(WirelessCraftingGridGridFactory.ID, new WirelessCraftingGridGridFactory());
    }

    @SuppressWarnings("all")
    @SubscribeEvent
    public void clientInit(FMLClientSetupEvent event)
    {
        ModScreens.init();
        MinecraftForge.EVENT_BUS.register(new MultiblockClientTickHandler());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLOCK_ADVANCED_WIRELESS_TRANSMITTER.get(), RenderType.cutout());
    }

    @SubscribeEvent
    public void keyRegisterEvent(RegisterKeyMappingsEvent event)
    {
        event.register(KeyBindings.OPEN_WIRELESS_CRAFTING_GRID);
        event.register(KeyBindings.MODE_SWITCH_WIRELESS_CRAFTING_GRID);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key e)
    {
        if (Minecraft.getInstance().player != null)
        {
            if (KeyBindings.OPEN_WIRELESS_CRAFTING_GRID.consumeClick())
            {
                KeyInputListener.findAndOpen(ModItems.WIRELESS_GRID.get(), ModItems.CREATIVE_WIRELESS_GRID.get());
            }
            if (KeyBindings.MODE_SWITCH_WIRELESS_CRAFTING_GRID.consumeClick() && e.getAction() == 1)
            {
                PacketHandler.sendToServer(new PacketChangeMode());
            }
        }
    }

    private void registerCreativeTab(RegisterEvent event) {
        ResourceKey<CreativeModeTab> TAB = ResourceKey.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, new ResourceLocation(Constants.MOD_ID, "creative_tab"));
        event.register(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, creativeModeTabRegisterHelper ->
        {
            creativeModeTabRegisterHelper.register(TAB, CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.BLOCK_MULTI_STORAGE.get()))
                    .title(Component.translatable("itemGroup." + Constants.MOD_ID))
                    .displayItems((params, output) -> {
                        ModItems.ITEMS.getEntries().forEach(itemRegistryObject -> output.accept(itemRegistryObject.get()));
                    })
                    .build());
        });
    }

    public static MultiBlockCrafter getMultiBlock(Level world, BlockPos pos)
    {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof BlockEntityMultiCrafter)
        {
            return (MultiBlockCrafter) ((BlockEntityMultiCrafter) tileEntity).getMultiblockController();
        }
        return null;
    }

    private static INetworkNode readAndReturn(CompoundTag tag, NetworkNode node)
    {
        node.read(tag);
        return node;
    }

}

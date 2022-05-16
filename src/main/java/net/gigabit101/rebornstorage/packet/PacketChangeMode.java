package net.gigabit101.rebornstorage.packet;

import com.refinedmods.refinedstorage.integration.curios.CuriosIntegration;
import net.gigabit101.rebornstorage.init.ModItems;
import net.gigabit101.rebornstorage.items.ItemWirelessGrid;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class PacketChangeMode
{
    public PacketChangeMode() {}

    public static void encode(PacketChangeMode packetGui, FriendlyByteBuf buf) {}

    public static PacketChangeMode decode(FriendlyByteBuf buf)
    {
        return new PacketChangeMode();
    }

    public static class Handler
    {
        public static void handle(final PacketChangeMode message, Supplier<NetworkEvent.Context> ctx)
        {
            Set<Item> validItems = new HashSet(Arrays.asList(ModItems.WIRELESS_GRID.get(), ModItems.CREATIVE_WIRELESS_GRID.get()));

            ctx.get().enqueueWork(() ->
            {
                ServerPlayer player = ctx.get().getSender();
                if (player == null) return;
                Container inv = player.getInventory();
                int slotFound = -1;

                //Loop the players inventory looking for our item
                for(int i = 0; i < inv.getContainerSize(); ++i)
                {
                    ItemStack slot = inv.getItem(i);
                    if (validItems.contains(slot.getItem()))
                    {
                        if (slotFound != -1) {
                            return;
                        }
                        slotFound = i;
                    }
                }

                //If we don't find our stack and Curio is loaded look in the curio slots
                if (CuriosIntegration.isLoaded() && slotFound == -1)
                {
                    Optional<ImmutableTriple<String, Integer, ItemStack>> curio = CuriosApi.getCuriosHelper().findEquippedCurio((stack) -> validItems.contains(stack.getItem()), player);
                    if (curio.isPresent())
                    {
                        //if we find our stack update its nbt/mode
                        updateStack(curio.get().getRight(), player);
                        return;
                    }
                }
                if (slotFound != -1)
                {
                    //If we find our stack before Curio update this stack
                    updateStack(player.getInventory().getItem(slotFound), player);
                }

            });
            ctx.get().setPacketHandled(true);
        }
    }

    public static void updateStack(ItemStack stack, Player player)
    {
        if (player.level.isClientSide)
            return;
        if(stack.getItem() instanceof ItemWirelessGrid itemWirelessGrid)
        {
            ItemWirelessGrid.MODE current = itemWirelessGrid.getMode(stack);
            switch (current)
            {
                case CRAFTING:
                    itemWirelessGrid.setMode(stack, ItemWirelessGrid.MODE.FLUID);
                    player.sendMessage(new TextComponent(ChatFormatting.GOLD + "MODE: " + ItemWirelessGrid.MODE.FLUID.name()), Util.NIL_UUID);
                    break;
//                    player.displayClientMessage(new TextComponent(ChatFormatting.GOLD + "MODE: " + ItemWirelessGrid.MODE.FLUID.name()), true);
                case FLUID:
                    itemWirelessGrid.setMode(stack, ItemWirelessGrid.MODE.MONITOR);
                    player.sendMessage(new TextComponent(ChatFormatting.GOLD + "MODE: " + ItemWirelessGrid.MODE.MONITOR.name()), Util.NIL_UUID);
                    break;
//                    player.displayClientMessage(new TextComponent(ChatFormatting.GOLD + "MODE: " + ItemWirelessGrid.MODE.MONITOR.name()), true);
                case MONITOR:
                    itemWirelessGrid.setMode(stack, ItemWirelessGrid.MODE.CRAFTING);
                    player.sendMessage(new TextComponent(ChatFormatting.GOLD + "MODE: " + ItemWirelessGrid.MODE.CRAFTING.name()), Util.NIL_UUID);
                    break;
//                    player.displayClientMessage(new TextComponent(ChatFormatting.GOLD + "MODE: " + ItemWirelessGrid.MODE.CRAFTING.name()), true);
            }
        }
    }
}

package net.gigabit101.rebornstorage.packet;

import com.refinedmods.refinedstorage.integration.curios.CuriosIntegration;
import net.gigabit101.rebornstorage.Constants;
import net.gigabit101.rebornstorage.init.ModItems;
import net.gigabit101.rebornstorage.items.ItemWirelessGrid;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PacketChangeMode implements CustomPacketPayload
{
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "changemode");

    public PacketChangeMode() {}

    public static PacketChangeMode decode(FriendlyByteBuf buf)
    {
        return new PacketChangeMode();
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf)
    {

    }

    @Override
    public @NotNull ResourceLocation id()
    {
        return ID;
    }

    public static class Handler
    {
        public static void handle(final PacketChangeMode message, PlayPayloadContext ctx)
        {
            Set<Item> validItems = new HashSet(Arrays.asList(ModItems.WIRELESS_GRID.get(), ModItems.CREATIVE_WIRELESS_GRID.get()));

            ctx.workHandler().execute(() ->
            {
                ServerPlayer player = (ServerPlayer) ctx.player().get();
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
        }
    }

    public static void updateStack(ItemStack stack, Player player)
    {
        if (player.level().isClientSide)
            return;
        if(stack.getItem() instanceof ItemWirelessGrid itemWirelessGrid)
        {
            ItemWirelessGrid.MODE current = itemWirelessGrid.getMode(stack);
            switch (current)
            {
                case CRAFTING:
                    itemWirelessGrid.setMode(stack, ItemWirelessGrid.MODE.FLUID);
                    player.displayClientMessage(Component.literal(ChatFormatting.GOLD + "MODE: " + ItemWirelessGrid.MODE.FLUID.name()), false);
                    break;
                case FLUID:
                    itemWirelessGrid.setMode(stack, ItemWirelessGrid.MODE.MONITOR);
                    player.displayClientMessage(Component.literal(ChatFormatting.GOLD + "MODE: " + ItemWirelessGrid.MODE.MONITOR.name()), false);
                    break;
                case MONITOR:
                    itemWirelessGrid.setMode(stack, ItemWirelessGrid.MODE.CRAFTING);
                    player.displayClientMessage(Component.literal(ChatFormatting.GOLD + "MODE: " + ItemWirelessGrid.MODE.CRAFTING.name()), false);
                    break;
            }
        }
    }
}

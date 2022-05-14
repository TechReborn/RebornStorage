package net.gigabit101.rebornstorage.items;

import com.refinedmods.refinedstorage.api.network.item.INetworkItem;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import com.refinedmods.refinedstorage.item.NetworkItem;
import net.gigabit101.rebornstorage.grid.pattern.WirelessPatternGridNetworkItem;
import net.gigabit101.rebornstorage.grid.crafting.WirelessCraftingGridNetworkItem;
import net.gigabit101.rebornstorage.grid.monitor.WirelessCraftingMonitorNetworkItemExt;
import net.gigabit101.rebornstorage.grid.fluid.WirelessFluidGridNetworkItemExt;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ItemWirelessGrid extends NetworkItem
{
    public enum Type
    {
        NORMAL,
        CREATIVE;
    }

    Type type;

    public ItemWirelessGrid(Properties item, Type type, Supplier<Integer> energyCapacity)
    {
        super(item, type == Type.CREATIVE, energyCapacity);
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        if(player.isCrouching())
        {
            ItemStack stack = player.getItemInHand(hand);
            MODE current = getMode(stack);
            switch (current)
            {
                case CRAFTING:
                    setMode(stack, MODE.FLUID);
                    player.displayClientMessage(new TextComponent(ChatFormatting.GOLD + "MODE: " + MODE.FLUID.name()), true);
                    return InteractionResultHolder.success(stack);
                case FLUID:
                    setMode(stack, MODE.MONITOR);
                    player.displayClientMessage(new TextComponent(ChatFormatting.GOLD + "MODE: " + MODE.MONITOR.name()), true);
                    return InteractionResultHolder.success(stack);
                case MONITOR:
                    setMode(stack, MODE.CRAFTING);
                    player.displayClientMessage(new TextComponent(ChatFormatting.GOLD + "MODE: " + MODE.PATTERN.name()), true);
                    return InteractionResultHolder.success(stack);
//                case PATTERN:
//                    setMode(stack, MODE.CRAFTING);
//                    player.displayClientMessage(new TextComponent(ChatFormatting.GOLD + "MODE: " + MODE.CRAFTING.name()), true);
//                    return InteractionResultHolder.success(stack);
            }
        }
        return super.use(level, player, hand);
    }

    public Type getType()
    {
        return type;
    }

    public void setMode(ItemStack stack, MODE mode)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("mode", mode.name());
    }

    public MODE getMode(ItemStack stack)
    {
        CompoundTag compoundTag = stack.getOrCreateTag();
        if(compoundTag.contains("mode"))
        {
            return MODE.valueOf(compoundTag.getString("mode"));
        }
        return MODE.CRAFTING;
    }

    @NotNull
    @Override
    public INetworkItem provide(INetworkItemManager iNetworkItemManager, Player player, ItemStack itemStack, PlayerSlot playerSlot)
    {
        switch (getMode(itemStack))
        {
            case CRAFTING:
                return new WirelessCraftingGridNetworkItem(iNetworkItemManager, player, itemStack, playerSlot);
            case FLUID:
                return new WirelessFluidGridNetworkItemExt(iNetworkItemManager, player, itemStack, playerSlot);
            case MONITOR:
                return new WirelessCraftingMonitorNetworkItemExt(iNetworkItemManager, player, itemStack, playerSlot);
//            case PATTERN:
//                return new WirelessPatternGridNetworkItem(iNetworkItemManager, player, itemStack, playerSlot);

        }
        return new WirelessCraftingGridNetworkItem(iNetworkItemManager, player, itemStack, playerSlot);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
    {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(new TextComponent(ChatFormatting.GOLD + "MODE: " + getMode(stack)));
    }

    public enum MODE
    {
        CRAFTING,
        FLUID,
        MONITOR,
        PATTERN
    }
}

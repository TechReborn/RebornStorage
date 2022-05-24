package net.gigabit101.rebornstorage.items;

import net.gigabit101.rebornstorage.client.CreativeTabRebornStorage;
import net.gigabit101.rebornstorage.init.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.PatchouliAPI;

public class ItemBook extends Item
{
    public ItemBook()
    {
        super(new Properties().tab(CreativeTabRebornStorage.INSTANCE).stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if(player instanceof ServerPlayer serverPlayer)
        {
            PatchouliAPI.get().openBookGUI(serverPlayer, Registry.ITEM.getKey(this));
        }
        return InteractionResultHolder.success(stack);
    }
}

package net.gigabit101.rebornstorage.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemBook extends Item
{
    public ItemBook()
    {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if(player instanceof ServerPlayer serverPlayer)
        {
            //Not updated yet
//            PatchouliAPI.get().openBookGUI(serverPlayer, Registry.ITEM.getKey(this));
        }
        return InteractionResultHolder.success(stack);
    }
}

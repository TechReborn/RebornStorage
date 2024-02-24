package net.gigabit101.rebornstorage.client;

import com.refinedmods.refinedstorage.api.autocrafting.ICraftingPatternProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SlotFiltered extends SlotItemHandler
{
    public SlotFiltered(IItemHandler inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack)
    {
        return stack.getItem() instanceof ICraftingPatternProvider;
    }
}

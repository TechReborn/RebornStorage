package RebornStorage.client;

import com.raoulvdberge.refinedstorage.item.ItemPattern;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by Gigabit101 on 06/01/2017.
 */
public class SlotFiltered extends Slot
{
    public SlotFiltered(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nullable ItemStack stack)
    {
        if(stack.getItem() instanceof ItemPattern)
        {
            return true;
        }
        return false;
    }
}

package net.gigabit101.rebornstorage.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ContainerBase extends AbstractContainerMenu
{
    public ContainerBase(@Nullable MenuType<?> menuType, int id)
    {
        super(menuType, id);
    }

    public void drawPlayersInv(Inventory player, int x, int y)
    {
        int i;
        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlot(new Slot(player, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }
    }

    public void drawPlayersHotBar(Inventory player, int x, int y)
    {
        int i;
        for (i = 0; i < 9; ++i)
        {
            this.addSlot(new Slot(player, i, x + i * 18, y));
        }
    }

    @Override
    public boolean stillValid(Player player)
    {
        return false;
    }

    public static boolean canStacksMerge(ItemStack stack1, ItemStack stack2)
    {
        if (stack1.isEmpty() || stack2.isEmpty()) return false;
        if (!ItemStack.matches(stack1, stack2)) return false;
        if (!ItemStack.isSameItemSameTags(stack1, stack2)) return false;
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex)
    {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = (Slot) slots.get(slotIndex);
        int numSlots = slots.size();
        if (slot != null && slot.hasItem())
        {
            ItemStack stackInSlot = slot.getItem();
            originalStack = stackInSlot.copy();
            if (slotIndex >= numSlots - 9 * 4 && tryShiftItem(stackInSlot, numSlots))
            {
                // NOOP
            } else if (slotIndex >= numSlots - 9 * 4 && slotIndex < numSlots - 9)
            {
                if (!shiftItemStack(stackInSlot, numSlots - 9, numSlots))
                {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= numSlots - 9 && slotIndex < numSlots)
            {
                if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots - 9))
                {
                    return ItemStack.EMPTY;
                }
            } else if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots))
            {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stackInSlot, originalStack);
            if (stackInSlot.getCount() <= 0)
            {
                slot.set(ItemStack.EMPTY);
            } else
            {
                slot.setChanged();
            }
            if (stackInSlot.getCount() == originalStack.getCount())
            {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stackInSlot);
        }
        return originalStack;
    }

    protected boolean shiftItemStack(ItemStack stackToShift, int start, int end)
    {
        boolean changed = false;
        if (stackToShift.isStackable())
        {
            for (int slotIndex = start; stackToShift.getCount() > 0 && slotIndex < end; slotIndex++)
            {
                Slot slot = (Slot) slots.get(slotIndex);
                ItemStack stackInSlot = slot.getItem();
                if (!stackInSlot.isEmpty() && canStacksMerge(stackInSlot, stackToShift))
                {
                    int resultingStackSize = stackInSlot.getCount() + stackToShift.getCount();
                    int max = Math.min(stackToShift.getMaxStackSize(), slot.getMaxStackSize());
                    if (resultingStackSize <= max)
                    {
                        stackToShift.setCount(0);
                        stackInSlot.setCount(resultingStackSize);
                        slot.setChanged();
                        changed = true;
                    } else if (stackInSlot.getCount() < max)
                    {
                        stackToShift.setCount(stackToShift.getCount() - (max - stackInSlot.getCount()));
                        stackInSlot.setCount(max);
                        slot.setChanged();
                        changed = true;
                    }
                }
            }
        }
        if (stackToShift.getCount() > 0)
        {
            for (int slotIndex = start; stackToShift.getCount() > 0 && slotIndex < end; slotIndex++)
            {
                Slot slot = (Slot) slots.get(slotIndex);
                ItemStack stackInSlot = slot.getItem();
                if (stackInSlot.isEmpty())
                {
                    int max = Math.min(stackToShift.getMaxStackSize(), slot.getMaxStackSize());
                    stackInSlot = stackToShift.copy();
                    stackInSlot.setCount(Math.min(stackToShift.getCount(), max));
                    stackToShift.setCount(stackToShift.getCount() - stackInSlot.getCount());
                    slot.set(stackInSlot);
                    slot.setChanged();
                    changed = true;
                }
            }
        }
        return changed;
    }

    private boolean tryShiftItem(ItemStack stackToShift, int numSlots)
    {
        for (int machineIndex = 0; machineIndex < numSlots - 9 * 4; machineIndex++)
        {
            Slot slot = (Slot) slots.get(machineIndex);
            if (!slot.mayPlace(stackToShift)) continue;
            if (shiftItemStack(stackToShift, machineIndex, machineIndex + 1)) return true;
        }
        return false;
    }
}

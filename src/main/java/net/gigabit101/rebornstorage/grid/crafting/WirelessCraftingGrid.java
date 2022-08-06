package net.gigabit101.rebornstorage.grid.crafting;

import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.api.network.grid.ICraftingGridListener;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.api.util.Action;
import com.refinedmods.refinedstorage.api.util.IStackList;
import com.refinedmods.refinedstorage.blockentity.grid.WirelessGrid;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import com.refinedmods.refinedstorage.util.StackUtils;
import net.gigabit101.rebornstorage.RebornStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class WirelessCraftingGrid extends WirelessGrid
{
    @Nullable
    private final MinecraftServer server;
    private final Level level;
    private final Set<ICraftingGridListener> listeners = new HashSet<>();
    private boolean queuedSave;

    private final AbstractContainerMenu craftingMenu = new AbstractContainerMenu(null, 0)
    {
        @Override
        public boolean stillValid(@NotNull Player player)
        {
            return false;
        }

        @Override
        public ItemStack quickMoveStack(Player player, int i)
        {
            return ItemStack.EMPTY;
        }

        @Override
        public void slotsChanged(@NotNull Container container)
        {
            if (server != null)
            {
                onCraftingMatrixChanged();
            }
        }
    };

    private CraftingRecipe currentRecipe;
    private final CraftingContainer craftingContainer = new CraftingContainer(craftingMenu, 3, 3)
    {
        @Override
        public void setChanged()
        {
            super.setChanged();
            if (!queuedSave && server != null)
            {
                queuedSave = true;
                server.doRunTask(new TickTask(0, () ->
                {
                    if (!getStack().hasTag())
                    {
                        getStack().setTag(new CompoundTag());
                    }

                    StackUtils.writeItems(craftingContainer, 1, Objects.requireNonNull(getStack().getTag()));
                    queuedSave = false;
                }));
            }
        }
    };
    private final ResultContainer craftingResultContainer = new ResultContainer();

    public WirelessCraftingGrid(ItemStack stack, Level level, @Nullable MinecraftServer server, PlayerSlot slot)
    {
        super(stack, server, slot);
        this.server = server;
        this.level = level;
        if (stack.hasTag())
        {
            StackUtils.readItems(craftingContainer, 1, stack.getTag());
        }
    }

    @Override
    public Component getTitle()
    {
        return Component.translatable("gui.refinedstorage.crafting_grid");
    }

    @Override
    public GridType getGridType()
    {
        return GridType.CRAFTING;
    }

    @Override
    public CraftingContainer getCraftingMatrix()
    {
        return craftingContainer;
    }

    @Override
    public ResultContainer getCraftingResult()
    {
        return craftingResultContainer;
    }

    @Override
    public void onCraftingMatrixChanged()
    {
        if (currentRecipe == null || !currentRecipe.matches(craftingContainer, level))
        {
            currentRecipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingContainer, level).orElse(null);
        }
        if (currentRecipe == null)
        {
            craftingResultContainer.setItem(0, ItemStack.EMPTY);
        } else
        {
            craftingResultContainer.setItem(0, currentRecipe.assemble(craftingContainer));
        }

        listeners.forEach(ICraftingGridListener::onCraftingMatrixChanged);

        if (!getStack().hasTag())
        {
            getStack().setTag(new CompoundTag());
        }

        StackUtils.writeItems(craftingContainer, 1, getStack().getTag());
    }

    @Override
    public void onCrafted(Player player, @Nullable IStackList<ItemStack> availableItems, @Nullable IStackList<ItemStack> usedItems)
    {
        RebornStorage.RSAPI.getCraftingGridBehavior().onCrafted(this, currentRecipe, player, availableItems, usedItems);
    }

    @Override
    public void onClear(Player player)
    {
        INetwork network = getNetwork();

        if (network != null && network.getSecurityManager().hasPermission(Permission.INSERT, player))
        {
            for (int i = 0; i < craftingContainer.getContainerSize(); ++i)
            {
                ItemStack slot = craftingContainer.getItem(i);

                if (!slot.isEmpty())
                {
                    craftingContainer.setItem(i, network.insertItem(slot, slot.getCount(), Action.PERFORM));

                    network.getItemStorageTracker().changed(player, slot.copy());
                }
            }
        }
    }

    @Override
    public void onCraftedShift(Player player)
    {
        RebornStorage.RSAPI.getCraftingGridBehavior().onCraftedShift(this, player);
    }

    @Override
    public void onRecipeTransfer(Player player, ItemStack[][] recipe)
    {
        RebornStorage.RSAPI.getCraftingGridBehavior().onRecipeTransfer(this, player, recipe);
    }

    @Override
    public void addCraftingListener(ICraftingGridListener listener)
    {
        listeners.add(listener);
    }

    @Override
    public void removeCraftingListener(ICraftingGridListener listener)
    {
        listeners.remove(listener);
    }
}

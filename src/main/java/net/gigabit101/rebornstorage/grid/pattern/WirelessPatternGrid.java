package net.gigabit101.rebornstorage.grid.pattern;

import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.api.network.grid.IGridTab;
import com.refinedmods.refinedstorage.api.network.grid.INetworkAwareGrid;
import com.refinedmods.refinedstorage.api.network.grid.handler.IFluidGridHandler;
import com.refinedmods.refinedstorage.api.network.grid.handler.IItemGridHandler;
import com.refinedmods.refinedstorage.api.storage.cache.IStorageCache;
import com.refinedmods.refinedstorage.api.storage.cache.IStorageCacheListener;
import com.refinedmods.refinedstorage.api.util.IFilter;
import com.refinedmods.refinedstorage.api.util.IStackList;
import com.refinedmods.refinedstorage.blockentity.grid.WirelessGrid;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import com.refinedmods.refinedstorage.item.NetworkItem;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WirelessPatternGrid implements INetworkAwareGrid
{
    private final MinecraftServer server;
    private final ResourceKey<Level> nodeDimension;
    private final BlockPos nodePos;
    private ItemStack stack;

    public WirelessPatternGrid(ItemStack stack, @javax.annotation.Nullable MinecraftServer server, PlayerSlot slot)
    {
        this.stack = stack;
        this.nodePos = new BlockPos(NetworkItem.getX(stack), NetworkItem.getY(stack), NetworkItem.getZ(stack));
        this.server = server;
        this.nodeDimension = NetworkItem.getDimension(stack);
    }

    public ItemStack getStack()
    {
        return this.stack;
    }

    @Override
    public GridType getGridType()
    {
        return GridType.PATTERN;
    }

    @Override
    public IStorageCacheListener createListener(ServerPlayer serverPlayer)
    {
        return null;
    }

    @javax.annotation.Nullable
    public IStorageCache getStorageCache() {
        INetwork network = this.getNetwork();
        return network != null ? network.getFluidStorageCache() : null;
    }

    @Nullable
    @Override
    public IItemGridHandler getItemHandler()
    {
        return null;
    }

    @Nullable
    @Override
    public IFluidGridHandler getFluidHandler()
    {
        return null;
    }

    @Override
    public Component getTitle()
    {
        return new TranslatableComponent("gui.refinedstorage.fluid_grid");
    }

    @Override
    public int getViewType()
    {
        return 0;
    }

    @Override
    public int getSortingType()
    {
        return 0;
    }

    @Override
    public int getSortingDirection()
    {
        return 0;
    }

    @Override
    public int getSearchBoxMode()
    {
        return 0;
    }

    @Override
    public int getTabSelected()
    {
        return 0;
    }

    @Override
    public int getTabPage()
    {
        return 0;
    }

    @Override
    public int getTotalTabPages()
    {
        return 0;
    }

    @Override
    public int getSize()
    {
        return 0;
    }

    @Override
    public void onViewTypeChanged(int i)
    {

    }

    @Override
    public void onSortingTypeChanged(int i)
    {

    }

    @Override
    public void onSortingDirectionChanged(int i)
    {

    }

    @Override
    public void onSearchBoxModeChanged(int i)
    {

    }

    @Override
    public void onSizeChanged(int i)
    {

    }

    @Override
    public void onTabSelectionChanged(int i)
    {

    }

    @Override
    public void onTabPageChanged(int i)
    {

    }

    @Override
    public List<IFilter> getFilters()
    {
        return null;
    }

    @Override
    public List<IGridTab> getTabs()
    {
        return null;
    }

    @Override
    public IItemHandlerModifiable getFilter()
    {
        return null;
    }

    @Nullable
    @Override
    public CraftingContainer getCraftingMatrix()
    {
        return null;
    }

    @Nullable
    @Override
    public ResultContainer getCraftingResult()
    {
        return null;
    }

    @Override
    public void onCraftingMatrixChanged()
    {

    }

    @Override
    public void onCrafted(Player player, @Nullable IStackList<ItemStack> iStackList, @Nullable IStackList<ItemStack> iStackList1)
    {

    }

    @Override
    public void onClear(Player player)
    {

    }

    @Override
    public void onCraftedShift(Player player)
    {

    }

    @Override
    public void onRecipeTransfer(Player player, ItemStack[][] itemStacks)
    {

    }

    @Override
    public void onClosed(Player player)
    {
        INetwork network = this.getNetwork();
        if (network != null) {
            network.getNetworkItemManager().close(player);
        }
    }

    @Override
    public boolean isGridActive()
    {
        return true;
    }

    @Override
    public int getSlotId()
    {
        return 0;
    }

    @javax.annotation.Nullable
    public INetwork getNetwork() {
        Level level = this.server.getLevel(this.nodeDimension);
        return level != null ? NetworkUtils.getNetworkFromNode(NetworkUtils.getNodeFromBlockEntity(level.getBlockEntity(this.nodePos))) : null;
    }
}

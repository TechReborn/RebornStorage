package RebornStorage.init;

import RebornStorage.items.ItemRebornStorageCell;
import RebornStorage.items.ItemRebornStorageCellFluid;
import RebornStorage.items.ItemStoragePart;
import com.raoulvdberge.refinedstorage.block.EnumItemStorageType;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ModItems
{
    public static Item REBORN_STORAGE_CELL;
    public static Item REBORN_STORAGE_CELL_FLUID;
    public static Item REBORN_STORAGE_PART;

    public static void init()
    {
        REBORN_STORAGE_CELL = new ItemRebornStorageCell();
        GameRegistry.register(REBORN_STORAGE_CELL);

        REBORN_STORAGE_CELL_FLUID = new ItemRebornStorageCellFluid();
        GameRegistry.register(REBORN_STORAGE_CELL_FLUID);

        REBORN_STORAGE_PART = new ItemStoragePart();
        GameRegistry.register(REBORN_STORAGE_PART);

        EnumHelper.addEnum(EnumItemStorageType.class, "256k", new Class[]{int.class, int.class, String.class}, new Object[]{6, 256000, "256k"});
    }
}

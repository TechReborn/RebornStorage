package RebornStorage.items;

import RebornStorage.lib.ModInfo;
import com.raoulvdberge.refinedstorage.item.ItemStorageDisk;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ItemRebornStorageCell extends ItemStorageDisk {
	public static final String[] types = new String[] { "", "", "", "", "", "256k", "1024k", "4096k", "16384k" };

	public ItemRebornStorageCell() {
		this.setUnlocalizedName(ModInfo.MOD_ID + ".storagecell");
		this.setHasSubtypes(true);
		this.setRegistryName(ModInfo.MOD_ID, "storagecell");
	}

	//    @Override
	//    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list)
	//    {
	//        for (int meta = 5; meta < types.length; meta++)
	//        {
	//            list.add(ItemStorageNBT.createStackWithNBT(new ItemStack(item, 1, meta)));
	//        }
	//    }
	//
	//    @Override
	//    public String getUnlocalizedName()
	//    {
	//        return "item." + ModInfo.MOD_ID + ":" + "storagecell";
	//    }
	//
	//    @Override
	//    public String getUnlocalizedName(ItemStack itemStack)
	//    {
	//        int meta = itemStack.getItemDamage();
	//        if (meta < 0 || meta >= types.length)
	//        {
	//            meta = 0;
	//        }
	//        return super.getUnlocalizedName() + "." + types[meta];
	//    }
	//
	//    @Override
	//    public void addInformation(ItemStack disk, EntityPlayer player, List<String> tooltip, boolean advanced) {
	//        if (ItemStorageNBT.isValid(disk)) {
	//            String capacity = types[disk.getItemDamage()];
	//            tooltip.add(I18n.format("misc.refinedstorage:storage.stored_capacity", ItemStorageNBT.getStoredFromNBT(disk.getTagCompound()), capacity));
	//        }
	//        //TODO remove this
	//        for(EnumItemStorageType t: EnumItemStorageType.values())
	//        {
	//            tooltip.add(t.getName() + " " + t.getCapacity());
	//        }
	//    }
	//
	//    @Override
	//    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	//    {
	//        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	//        if (!stack.hasTagCompound())
	//        {
	//            ItemStorageNBT.createStackWithNBT(stack);
	//        }
	//    }
	//
	//    @Override
	//    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	//    {
	//        super.onCreated(stack, worldIn, playerIn);
	//        ItemStorageNBT.createStackWithNBT(stack);
	//    }
}

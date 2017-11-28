package me.modmuss50.rebornstorage.init;

/**
 * Created by Gigabit101 on 24/02/2017.
 */
public enum EnumFluidStorage {
	TYPE_1024K(0, "1024k", 1024000),
	TYPE_4096K(1, "4096K", 4096000),
	TYPE_16384K(2, "16384K", 16384000),
	TYPE_32768K(3, "32768K", 32768000);

	int meta;
	String name;
	int cap;

	private EnumFluidStorage(int meta, String name, int cap) {
		this.meta = meta;
		this.name = name;
		this.cap = cap;
	}

	public int getCap() {
		return cap;
	}

	public int getMeta() {
		return meta;
	}

	public String getName() {
		return name;
	}

	public static EnumFluidStorage getById(int id) {
		EnumFluidStorage[] var1 = values();
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			EnumFluidStorage type = var1[var3];
			if (type.getMeta() == id) {
				return type;
			}
		}
		return null;
	}
}

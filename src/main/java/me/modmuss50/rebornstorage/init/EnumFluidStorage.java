package me.modmuss50.rebornstorage.init;

/**
 * Created by Gigabit101 on 24/02/2017.
 */
public enum EnumFluidStorage {
	TYPE_16384K(0, "16384K", 16384000),
	TYPE_32768K(1, "32768K", 32768000),
	TYPE_131M(2, "131m", 131072000),
	TYPE_524M(3, "524m", 524288000);

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

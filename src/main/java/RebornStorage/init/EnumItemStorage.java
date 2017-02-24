package RebornStorage.init;

/**
 * Created by Gigabit101 on 24/02/2017.
 */
public enum EnumItemStorage
{
    TYPE_256k(0, "256k", 256000),
    TYPE_1024K(1, "1024k", 1024000),
    TYPE_4096K(2, "4096K", 4096000),
    TYPE_16384K(3, "16384K", 16384000);

    int meta;
    String name;
    int cap;

    private EnumItemStorage(int meta, String name, int cap)
    {
        this.meta = meta;
        this.name = name;
        this.cap = cap;
    }

    public int getCap()
    {
        return cap;
    }

    public int getMeta()
    {
        return meta;
    }

    public String getName()
    {
        return name;
    }

    public static EnumItemStorage getById(int id)
    {
        EnumItemStorage[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            EnumItemStorage type = var1[var3];
            if(type.getMeta() == id) {
                return type;
            }
        }
        return null;
    }
}

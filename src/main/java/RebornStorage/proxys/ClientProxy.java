package RebornStorage.proxys;


import RebornStorage.init.ModelHelper;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenders()
    {
        ModelHelper.init();
    }
}

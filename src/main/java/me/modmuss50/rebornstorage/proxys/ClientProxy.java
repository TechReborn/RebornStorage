package me.modmuss50.rebornstorage.proxys;

import com.raoulvdberge.refinedstorage.apiimpl.API;
import me.modmuss50.rebornstorage.client.gui.ContainerMultiCrafter;
import me.modmuss50.rebornstorage.init.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

/**
 * Created by Gigabit101 on 03/01/2017.
 */
public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenders() {
		ModelHelper.init();

		API.instance().addPatternRenderHandler(itemStack -> {
			Container container = Minecraft.getMinecraft().player.openContainer;
			if(container instanceof ContainerMultiCrafter){
				for (int i = 0; i < 78; i++) {
					if(container.getSlot(i).getStack() == itemStack){
						return true;
					}
				}

			}
			return false;
		});
	}
}

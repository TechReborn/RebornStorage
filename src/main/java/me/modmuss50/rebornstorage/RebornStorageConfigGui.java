package me.modmuss50.rebornstorage;

import me.modmuss50.rebornstorage.lib.ModInfo;
import reborncore.common.config.ConfigGuiFactory;

public class RebornStorageConfigGui extends ConfigGuiFactory {
	@Override
	public String getModID() {
		return ModInfo.MOD_ID;
	}

	@Override
	public String getModName() {
		return ModInfo.MOD_NAME;
	}
}

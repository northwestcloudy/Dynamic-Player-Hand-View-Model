package com.northwestcloudymac.dynamicplayermodel.compat;

import com.northwestcloudymac.dynamicplayermodel.gui.DynamicPlayerModelConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public final class DynamicPlayerModelModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return DynamicPlayerModelConfigScreen::new;
	}
}

package com.northwestcloudymac.dynamicplayermodel;

import com.northwestcloudymac.dynamicplayermodel.config.PlayerScaleConfig;
import java.nio.file.Path;

public final class DynamicPlayerModel {
	public static final String MOD_ID = "dynamic_player_model";

	private DynamicPlayerModel() {
	}

	public static void initialize(Path configDirectory) {
		PlayerScaleConfig.initialize(configDirectory);
	}
}

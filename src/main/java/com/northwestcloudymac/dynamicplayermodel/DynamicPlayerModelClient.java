package com.northwestcloudymac.dynamicplayermodel;

import com.northwestcloudymac.dynamicplayermodel.config.PlayerScaleConfig;
import com.northwestcloudymac.dynamicplayermodel.gui.DynamicPlayerModelConfigScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class DynamicPlayerModelClient implements ClientModInitializer {
	public static final String MOD_ID = "dynamic_player_model";
	private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
			Identifier.fromNamespaceAndPath(MOD_ID, "main")
	);
	private static KeyMapping openConfigKey;
	private static KeyMapping toggleHideEmptyHandsKey;

	@Override
	public void onInitializeClient() {
		PlayerScaleConfig.load();
		registerKeyMappings();
	}

	private static void registerKeyMappings() {
		openConfigKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.dynamic_player_model.open_config",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_F8,
				CATEGORY
		));
		toggleHideEmptyHandsKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.dynamic_player_model.toggle_hide_empty_hands",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN,
				CATEGORY
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openConfigKey.consumeClick()) {
				if (!(client.gui.screen() instanceof DynamicPlayerModelConfigScreen)) {
					client.gui.setScreen(new DynamicPlayerModelConfigScreen(client.gui.screen()));
				}
			}

			while (toggleHideEmptyHandsKey.consumeClick()) {
				PlayerScaleConfig config = PlayerScaleConfig.get();
				config.hideEmptyHands = !config.hideEmptyHands;
				PlayerScaleConfig.save();
			}
		});
	}
}

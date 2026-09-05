package com.northwestcloudymac.dynamicplayermodel.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
import com.northwestcloudymac.dynamicplayermodel.DynamicPlayerModel;
import com.northwestcloudymac.dynamicplayermodel.config.PlayerScaleConfig;
import com.northwestcloudymac.dynamicplayermodel.gui.DynamicPlayerModelConfigScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;

@Mod(value = DynamicPlayerModel.MOD_ID, dist = Dist.CLIENT)
public final class DynamicPlayerModelNeoForgeClient {
	private static final KeyMapping.Category CATEGORY = new KeyMapping.Category(
			Identifier.fromNamespaceAndPath(DynamicPlayerModel.MOD_ID, "main")
	);
	private static KeyMapping openConfigKey;
	private static KeyMapping toggleHideEmptyHandsKey;

	public DynamicPlayerModelNeoForgeClient(IEventBus modBus, ModContainer container) {
		DynamicPlayerModel.initialize(FMLPaths.CONFIGDIR.get());
		modBus.addListener(this::registerKeyMappings);
		NeoForge.EVENT_BUS.addListener(this::onClientTick);
		container.registerExtensionPoint(
				IConfigScreenFactory.class,
				(minecraft, parent) -> new DynamicPlayerModelConfigScreen(parent)
		);
	}

	private void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.registerCategory(CATEGORY);
		openConfigKey = new KeyMapping(
				"key.dynamic_player_model.open_config",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_F8,
				CATEGORY
		);
		toggleHideEmptyHandsKey = new KeyMapping(
				"key.dynamic_player_model.toggle_hide_empty_hands",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN,
				CATEGORY
		);
		event.register(openConfigKey);
		event.register(toggleHideEmptyHandsKey);
	}

	private void onClientTick(ClientTickEvent.Post event) {
		if (openConfigKey == null || toggleHideEmptyHandsKey == null) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();
		while (openConfigKey.consumeClick()) {
			if (!(minecraft.gui.screen() instanceof DynamicPlayerModelConfigScreen)) {
				minecraft.gui.setScreen(new DynamicPlayerModelConfigScreen(minecraft.gui.screen()));
			}
		}

		while (toggleHideEmptyHandsKey.consumeClick()) {
			PlayerScaleConfig config = PlayerScaleConfig.get();
			config.hideEmptyHands = !config.hideEmptyHands;
			PlayerScaleConfig.save();
		}
	}
}

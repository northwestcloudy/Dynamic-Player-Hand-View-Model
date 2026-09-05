package com.northwestcloudymac.dynamicplayermodel.forge;

import com.mojang.blaze3d.platform.InputConstants;
import com.northwestcloudymac.dynamicplayermodel.DynamicPlayerModel;
import com.northwestcloudymac.dynamicplayermodel.config.PlayerScaleConfig;
import com.northwestcloudymac.dynamicplayermodel.gui.DynamicPlayerModelConfigScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.lwjgl.glfw.GLFW;

@Mod(DynamicPlayerModel.MOD_ID)
public final class DynamicPlayerModelForgeClient {
	private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
			Identifier.fromNamespaceAndPath(DynamicPlayerModel.MOD_ID, "main")
	);
	private static KeyMapping openConfigKey;
	private static KeyMapping toggleHideEmptyHandsKey;

	public DynamicPlayerModelForgeClient(FMLJavaModLoadingContext context) {
		DynamicPlayerModel.initialize(FMLPaths.CONFIGDIR.get());
		context.registerExtensionPoint(
				ConfigScreenHandler.ConfigScreenFactory.class,
				() -> new ConfigScreenHandler.ConfigScreenFactory(DynamicPlayerModelConfigScreen::new)
		);
	}

	@Mod.EventBusSubscriber(
			modid = DynamicPlayerModel.MOD_ID,
			value = Dist.CLIENT,
			bus = Mod.EventBusSubscriber.Bus.FORGE
	)
	public static final class ClientModEvents {
		private ClientModEvents() {
		}

		@SubscribeEvent
		public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
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
	}

	@Mod.EventBusSubscriber(
			modid = DynamicPlayerModel.MOD_ID,
			value = Dist.CLIENT,
			bus = Mod.EventBusSubscriber.Bus.FORGE
	)
	public static final class ClientGameEvents {
		private ClientGameEvents() {
		}

		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent.Post event) {
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
}

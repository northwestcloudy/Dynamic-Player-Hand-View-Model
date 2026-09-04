package com.northwestcloudymac.dynamicplayermodel.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.northwestcloudymac.dynamicplayermodel.DynamicPlayerModelClient;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PlayerScaleConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicPlayerModelClient.MOD_ID);
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final int CURRENT_VERSION = 4;
	private static final float MIN_SCALE = 0.05F;
	private static final float MAX_SCALE = 1.0F;
	private static final float MIN_EMPTY_HAND_SCALE = 0.5F;
	private static final float MIN_HAND_SCALE = 0.01F;
	private static final float MAX_HAND_SCALE = 1.0F;
	private static final float MIN_POSITION = -0.5F;
	private static final float MAX_POSITION = 0.5F;
	private static final float MIN_ROTATION = -45.0F;
	private static final float MAX_ROTATION = 45.0F;

	private static PlayerScaleConfig instance = defaults();

	public int configVersion;
	public boolean enabled;
	public boolean scaleOwnPlayer;
	public boolean scaleOtherPlayers;
	public boolean scaleHeldItems;
	public boolean scaleArmor;
	public float headScale;
	public float bodyScale;
	public boolean viewModelEnabled;
	public boolean compactEmptyHands;
	public boolean hideEmptyHands;
	public boolean twoHandedMaps;
	public float emptyHandScale;
	public float mainHandScale;
	public float mainPositionX;
	public float mainPositionY;
	public float mainPositionZ;
	public float mainRotationX;
	public float mainRotationY;
	public float mainRotationZ;
	public float offHandScale;
	public float offPositionX;
	public float offPositionY;
	public float offPositionZ;
	public float offRotationX;
	public float offRotationY;
	public float offRotationZ;

	private static PlayerScaleConfig defaults() {
		PlayerScaleConfig config = new PlayerScaleConfig();
		config.configVersion = CURRENT_VERSION;
		config.enabled = true;
		config.scaleOwnPlayer = true;
		config.scaleOtherPlayers = false;
		config.scaleHeldItems = true;
		config.scaleArmor = false;
		config.headScale = 1.0F;
		config.bodyScale = 0.45F;
		config.resetViewModel();
		return config;
	}

	public static void load() {
		Path path = configPath();

		if (Files.notExists(path)) {
			instance = defaults();
			save(path, instance);
			return;
		}

		try (Reader reader = Files.newBufferedReader(path)) {
			PlayerScaleConfig parsed = GSON.fromJson(reader, PlayerScaleConfig.class);
			instance = sanitize(parsed == null ? defaults() : parsed);
			save(path, instance);
		} catch (IOException | JsonParseException e) {
			LOGGER.warn("Could not read Dynamic Player Model config; using defaults", e);
			instance = defaults();
			save(path, instance);
		}
	}

	public static PlayerScaleConfig get() {
		return instance;
	}

	public static void save() {
		save(configPath(), instance);
	}

	public static void resetToDefaults() {
		instance = defaults();
		save();
	}

	public void resetViewModel() {
		viewModelEnabled = true;
		compactEmptyHands = true;
		hideEmptyHands = false;
		twoHandedMaps = true;
		emptyHandScale = 0.82F;
		mainHandScale = 1.0F;
		mainPositionX = 0.0F;
		mainPositionY = 0.0F;
		mainPositionZ = 0.0F;
		mainRotationX = 0.0F;
		mainRotationY = 0.0F;
		mainRotationZ = 0.0F;
		offHandScale = 1.0F;
		offPositionX = 0.0F;
		offPositionY = 0.0F;
		offPositionZ = 0.0F;
		offRotationX = 0.0F;
		offRotationY = 0.0F;
		offRotationZ = 0.0F;
	}

	public boolean shouldScale(AvatarRenderState state) {
		if (!enabled || state == null) {
			return false;
		}

		LocalPlayer player = Minecraft.getInstance().player;
		boolean ownPlayer = player != null && state.id == player.getId();
		return ownPlayer ? scaleOwnPlayer : scaleOtherPlayers;
	}

	public float headScale() {
		return clamp(headScale);
	}

	public float bodyScale() {
		return clamp(bodyScale);
	}

	private static PlayerScaleConfig sanitize(PlayerScaleConfig config) {
		if (config.configVersion < 2) {
			config.resetViewModel();
		}

		if (config.configVersion < 3) {
			config.scaleArmor = false;
		}

		if (config.configVersion < 4) {
			config.twoHandedMaps = true;
		}

		if (config.configVersion < CURRENT_VERSION) {
			config.configVersion = CURRENT_VERSION;
		}

		config.headScale = clamp(config.headScale);
		config.bodyScale = clamp(config.bodyScale);
		config.emptyHandScale = clamp(config.emptyHandScale, MIN_EMPTY_HAND_SCALE, 1.0F, 0.82F);
		config.mainHandScale = clamp(config.mainHandScale, MIN_HAND_SCALE, MAX_HAND_SCALE, 1.0F);
		config.offHandScale = clamp(config.offHandScale, MIN_HAND_SCALE, MAX_HAND_SCALE, 1.0F);
		config.mainPositionX = clamp(config.mainPositionX, MIN_POSITION, MAX_POSITION, 0.0F);
		config.mainPositionY = clamp(config.mainPositionY, MIN_POSITION, MAX_POSITION, 0.0F);
		config.mainPositionZ = clamp(config.mainPositionZ, MIN_POSITION, MAX_POSITION, 0.0F);
		config.offPositionX = clamp(config.offPositionX, MIN_POSITION, MAX_POSITION, 0.0F);
		config.offPositionY = clamp(config.offPositionY, MIN_POSITION, MAX_POSITION, 0.0F);
		config.offPositionZ = clamp(config.offPositionZ, MIN_POSITION, MAX_POSITION, 0.0F);
		config.mainRotationX = clamp(config.mainRotationX, MIN_ROTATION, MAX_ROTATION, 0.0F);
		config.mainRotationY = clamp(config.mainRotationY, MIN_ROTATION, MAX_ROTATION, 0.0F);
		config.mainRotationZ = clamp(config.mainRotationZ, MIN_ROTATION, MAX_ROTATION, 0.0F);
		config.offRotationX = clamp(config.offRotationX, MIN_ROTATION, MAX_ROTATION, 0.0F);
		config.offRotationY = clamp(config.offRotationY, MIN_ROTATION, MAX_ROTATION, 0.0F);
		config.offRotationZ = clamp(config.offRotationZ, MIN_ROTATION, MAX_ROTATION, 0.0F);
		return config;
	}

	private static float clamp(float value) {
		if (Float.isNaN(value)) {
			return 1.0F;
		}

		return Math.max(MIN_SCALE, Math.min(MAX_SCALE, value));
	}

	private static float clamp(float value, float min, float max, float fallback) {
		if (!Float.isFinite(value)) {
			return fallback;
		}

		return Math.max(min, Math.min(max, value));
	}

	private static void save(Path path, PlayerScaleConfig config) {
		try {
			Files.createDirectories(path.getParent());

			try (Writer writer = Files.newBufferedWriter(path)) {
				GSON.toJson(config, writer);
			}
		} catch (IOException e) {
			LOGGER.warn("Could not write Dynamic Player Model config", e);
		}
	}

	private static Path configPath() {
		return FabricLoader.getInstance().getConfigDir().resolve("dynamic_player_model.json");
	}
}

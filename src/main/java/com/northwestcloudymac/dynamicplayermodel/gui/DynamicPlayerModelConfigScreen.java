package com.northwestcloudymac.dynamicplayermodel.gui;

import com.northwestcloudymac.dynamicplayermodel.config.PlayerScaleConfig;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class DynamicPlayerModelConfigScreen extends Screen {
	private static final int BUTTON_HEIGHT = 20;
	private static final int ROW_GAP = 2;
	private static final int TAB_GAP = 4;

	private final Screen parent;
	private Page page = Page.PLAYER;

	public DynamicPlayerModelConfigScreen(Screen parent) {
		super(Component.translatable("screen.dynamic_player_model.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		PlayerScaleConfig config = PlayerScaleConfig.get();
		int panelWidth = Math.min(450, this.width - 24);
		int left = (this.width - panelWidth) / 2;
		int columnWidth = (panelWidth - TAB_GAP) / 2;
		int y = Math.max(8, (this.height - 224) / 2);

		this.addRenderableWidget(new StringWidget(left, y, panelWidth, 16, this.title, this.font));
		y += 20;
		addTabs(left, y, panelWidth);
		y += BUTTON_HEIGHT + 4;

		if (page == Page.PLAYER) {
			addPlayerSettings(config, left, y, panelWidth, columnWidth);
		} else {
			addViewModelSettings(config, left, y, panelWidth, columnWidth, page == Page.MAIN_HAND);
		}
	}

	private void addTabs(int left, int y, int panelWidth) {
		int tabWidth = (panelWidth - TAB_GAP * 2) / 3;
		addTab(left, y, tabWidth, Page.PLAYER, "screen.dynamic_player_model.tab.player");
		addTab(left + tabWidth + TAB_GAP, y, tabWidth, Page.MAIN_HAND, "screen.dynamic_player_model.tab.main_hand");
		addTab(left + (tabWidth + TAB_GAP) * 2, y, tabWidth, Page.OFF_HAND, "screen.dynamic_player_model.tab.off_hand");
	}

	private void addTab(int x, int y, int width, Page target, String translationKey) {
		Button button = Button.builder(Component.translatable(translationKey), ignored -> {
			page = target;
			rebuildWidgets();
		}).bounds(x, y, width, BUTTON_HEIGHT).build();
		button.active = page != target;
		this.addRenderableWidget(button);
	}

	private void addPlayerSettings(
			PlayerScaleConfig config,
			int left,
			int y,
			int panelWidth,
			int columnWidth
	) {
		this.addRenderableWidget(toggleButton(left, y, panelWidth, "screen.dynamic_player_model.enabled",
				() -> config.enabled, value -> config.enabled = value));
		y += rowHeight();

		this.addRenderableWidget(ValueSlider.percent(left, y, panelWidth, "screen.dynamic_player_model.head_scale",
				0.05F, 1.0F, config.headScale(), value -> config.headScale = value));
		y += rowHeight();

		this.addRenderableWidget(ValueSlider.percent(left, y, panelWidth, "screen.dynamic_player_model.body_scale",
				0.05F, 1.0F, config.bodyScale(), value -> config.bodyScale = value));
		y += rowHeight();

		this.addRenderableWidget(toggleButton(left, y, columnWidth, "screen.dynamic_player_model.scale_own_player",
				() -> config.scaleOwnPlayer, value -> config.scaleOwnPlayer = value));
		this.addRenderableWidget(toggleButton(left + columnWidth + TAB_GAP, y, columnWidth,
				"screen.dynamic_player_model.scale_other_players",
				() -> config.scaleOtherPlayers, value -> config.scaleOtherPlayers = value));
		y += rowHeight();

		this.addRenderableWidget(toggleButton(left, y, columnWidth, "screen.dynamic_player_model.scale_armor",
				() -> config.scaleArmor, value -> config.scaleArmor = value));
		this.addRenderableWidget(toggleButton(left + columnWidth + TAB_GAP, y, columnWidth,
				"screen.dynamic_player_model.scale_held_items",
				() -> config.scaleHeldItems, value -> config.scaleHeldItems = value));
		y += rowHeight();

		this.addRenderableWidget(Button.builder(Component.translatable("screen.dynamic_player_model.preset.chibi"), button -> {
			config.headScale = 1.0F;
			config.bodyScale = 0.35F;
			rebuildWidgets();
		}).bounds(left, y, columnWidth, BUTTON_HEIGHT).build());
		this.addRenderableWidget(Button.builder(Component.translatable("screen.dynamic_player_model.preset.small"), button -> {
			config.headScale = 0.65F;
			config.bodyScale = 0.65F;
			rebuildWidgets();
		}).bounds(left + columnWidth + TAB_GAP, y, columnWidth, BUTTON_HEIGHT).build());
		y += rowHeight();

		addBottomButtons(left, y, panelWidth, columnWidth, () -> {
			PlayerScaleConfig.resetToDefaults();
			rebuildWidgets();
		});
	}

	private void addViewModelSettings(
			PlayerScaleConfig config,
			int left,
			int y,
			int panelWidth,
			int columnWidth,
			boolean mainHand
	) {
		this.addRenderableWidget(toggleButton(left, y, columnWidth, "screen.dynamic_player_model.view_model_enabled",
				() -> config.viewModelEnabled, value -> config.viewModelEnabled = value));
		this.addRenderableWidget(toggleButton(left + columnWidth + TAB_GAP, y, columnWidth,
				"screen.dynamic_player_model.compact_empty_hands",
				() -> config.compactEmptyHands, value -> config.compactEmptyHands = value));
		y += rowHeight();

		this.addRenderableWidget(toggleButton(left, y, columnWidth, "screen.dynamic_player_model.hide_empty_hands",
				() -> config.hideEmptyHands, value -> config.hideEmptyHands = value));
		this.addRenderableWidget(mapModeButton(left + columnWidth + TAB_GAP, y, columnWidth, config));
		y += rowHeight();

		this.addRenderableWidget(ValueSlider.percent(left, y, columnWidth, "screen.dynamic_player_model.hand_scale",
				0.01F, 1.0F, mainHand ? config.mainHandScale : config.offHandScale,
				value -> setHandScale(config, mainHand, value)));
		this.addRenderableWidget(ValueSlider.percent(left + columnWidth + TAB_GAP, y, columnWidth,
				"screen.dynamic_player_model.empty_hand_scale", 0.5F, 1.0F, config.emptyHandScale,
				value -> config.emptyHandScale = value));
		y += rowHeight();

		this.addRenderableWidget(ValueSlider.decimal(left, y, columnWidth, "screen.dynamic_player_model.position_x",
				-0.5F, 0.5F, handPosition(config, mainHand, Axis.X),
				value -> setHandPosition(config, mainHand, Axis.X, value)));
		this.addRenderableWidget(ValueSlider.decimal(left + columnWidth + TAB_GAP, y, columnWidth,
				"screen.dynamic_player_model.position_y", -0.5F, 0.5F, handPosition(config, mainHand, Axis.Y),
				value -> setHandPosition(config, mainHand, Axis.Y, value)));
		y += rowHeight();

		this.addRenderableWidget(ValueSlider.decimal(left, y, panelWidth, "screen.dynamic_player_model.position_z",
				-0.5F, 0.5F, handPosition(config, mainHand, Axis.Z),
				value -> setHandPosition(config, mainHand, Axis.Z, value)));
		y += rowHeight();

		this.addRenderableWidget(ValueSlider.degrees(left, y, columnWidth, "screen.dynamic_player_model.rotation_x",
				-45.0F, 45.0F, handRotation(config, mainHand, Axis.X),
				value -> setHandRotation(config, mainHand, Axis.X, value)));
		this.addRenderableWidget(ValueSlider.degrees(left + columnWidth + TAB_GAP, y, columnWidth,
				"screen.dynamic_player_model.rotation_y", -45.0F, 45.0F, handRotation(config, mainHand, Axis.Y),
				value -> setHandRotation(config, mainHand, Axis.Y, value)));
		y += rowHeight();

		this.addRenderableWidget(ValueSlider.degrees(left, y, panelWidth, "screen.dynamic_player_model.rotation_z",
				-45.0F, 45.0F, handRotation(config, mainHand, Axis.Z),
				value -> setHandRotation(config, mainHand, Axis.Z, value)));
		y += rowHeight();

		addBottomButtons(left, y, panelWidth, columnWidth, () -> {
			config.resetViewModel();
			rebuildWidgets();
		});
	}

	private void addBottomButtons(int left, int y, int panelWidth, int columnWidth, Runnable resetAction) {
		this.addRenderableWidget(Button.builder(Component.translatable("screen.dynamic_player_model.reset"), button -> resetAction.run())
				.bounds(left, y, columnWidth, BUTTON_HEIGHT).build());
		this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
				.bounds(left + columnWidth + TAB_GAP, y, panelWidth - columnWidth - TAB_GAP, BUTTON_HEIGHT).build());
	}

	@Override
	public void removed() {
		PlayerScaleConfig.save();
	}

	@Override
	public void onClose() {
		this.minecraft.gui.setScreen(parent);
	}

	private static int rowHeight() {
		return BUTTON_HEIGHT + ROW_GAP;
	}

	private static Button toggleButton(
			int x,
			int y,
			int width,
			String translationKey,
			Supplier<Boolean> getter,
			Consumer<Boolean> setter
	) {
		return Button.builder(toggleLabel(translationKey, getter.get()), button -> {
			boolean value = !getter.get();
			setter.accept(value);
			button.setMessage(toggleLabel(translationKey, value));
		}).bounds(x, y, width, BUTTON_HEIGHT).build();
	}

	private static Component toggleLabel(String translationKey, boolean enabled) {
		return Component.translatable(
				"screen.dynamic_player_model.toggle",
				Component.translatable(translationKey),
				Component.translatable(enabled ? "options.on" : "options.off")
		);
	}

	private static Button mapModeButton(int x, int y, int width, PlayerScaleConfig config) {
		return Button.builder(mapModeLabel(config.twoHandedMaps), button -> {
			config.twoHandedMaps = !config.twoHandedMaps;
			button.setMessage(mapModeLabel(config.twoHandedMaps));
		}).bounds(x, y, width, BUTTON_HEIGHT).build();
	}

	private static Component mapModeLabel(boolean twoHanded) {
		return Component.translatable(
				"screen.dynamic_player_model.value",
				Component.translatable("screen.dynamic_player_model.map_mode"),
				Component.translatable(twoHanded
						? "screen.dynamic_player_model.map_mode.two_handed"
						: "screen.dynamic_player_model.map_mode.one_handed")
		);
	}

	private static float handPosition(PlayerScaleConfig config, boolean mainHand, Axis axis) {
		return switch (axis) {
			case X -> mainHand ? config.mainPositionX : config.offPositionX;
			case Y -> mainHand ? config.mainPositionY : config.offPositionY;
			case Z -> mainHand ? config.mainPositionZ : config.offPositionZ;
		};
	}

	private static void setHandPosition(PlayerScaleConfig config, boolean mainHand, Axis axis, float value) {
		if (mainHand) {
			switch (axis) {
				case X -> config.mainPositionX = value;
				case Y -> config.mainPositionY = value;
				case Z -> config.mainPositionZ = value;
			}
		} else {
			switch (axis) {
				case X -> config.offPositionX = value;
				case Y -> config.offPositionY = value;
				case Z -> config.offPositionZ = value;
			}
		}
	}

	private static float handRotation(PlayerScaleConfig config, boolean mainHand, Axis axis) {
		return switch (axis) {
			case X -> mainHand ? config.mainRotationX : config.offRotationX;
			case Y -> mainHand ? config.mainRotationY : config.offRotationY;
			case Z -> mainHand ? config.mainRotationZ : config.offRotationZ;
		};
	}

	private static void setHandRotation(PlayerScaleConfig config, boolean mainHand, Axis axis, float value) {
		if (mainHand) {
			switch (axis) {
				case X -> config.mainRotationX = value;
				case Y -> config.mainRotationY = value;
				case Z -> config.mainRotationZ = value;
			}
		} else {
			switch (axis) {
				case X -> config.offRotationX = value;
				case Y -> config.offRotationY = value;
				case Z -> config.offRotationZ = value;
			}
		}
	}

	private static void setHandScale(PlayerScaleConfig config, boolean mainHand, float value) {
		if (mainHand) {
			config.mainHandScale = value;
		} else {
			config.offHandScale = value;
		}
	}

	private enum Page {
		PLAYER,
		MAIN_HAND,
		OFF_HAND
	}

	private enum Axis {
		X,
		Y,
		Z
	}

	private enum ValueFormat {
		PERCENT,
		DECIMAL,
		DEGREES
	}

	private static final class ValueSlider extends AbstractSliderButton {
		private final String translationKey;
		private final float min;
		private final float max;
		private final float step;
		private final ValueFormat format;
		private final Consumer<Float> setter;

		private ValueSlider(
				int x,
				int y,
				int width,
				String translationKey,
				float min,
				float max,
				float step,
				float initialValue,
				ValueFormat format,
				Consumer<Float> setter
		) {
			super(x, y, width, BUTTON_HEIGHT, Component.empty(), toSliderValue(initialValue, min, max));
			this.translationKey = translationKey;
			this.min = min;
			this.max = max;
			this.step = step;
			this.format = format;
			this.setter = setter;
			updateMessage();
		}

		private static ValueSlider percent(
				int x,
				int y,
				int width,
				String translationKey,
				float min,
				float max,
				float initialValue,
				Consumer<Float> setter
		) {
			return new ValueSlider(x, y, width, translationKey, min, max, 0.01F, initialValue, ValueFormat.PERCENT, setter);
		}

		private static ValueSlider decimal(
				int x,
				int y,
				int width,
				String translationKey,
				float min,
				float max,
				float initialValue,
				Consumer<Float> setter
		) {
			return new ValueSlider(x, y, width, translationKey, min, max, 0.01F, initialValue, ValueFormat.DECIMAL, setter);
		}

		private static ValueSlider degrees(
				int x,
				int y,
				int width,
				String translationKey,
				float min,
				float max,
				float initialValue,
				Consumer<Float> setter
		) {
			return new ValueSlider(x, y, width, translationKey, min, max, 1.0F, initialValue, ValueFormat.DEGREES, setter);
		}

		@Override
		protected void updateMessage() {
			float currentValue = currentValue();
			Object displayValue = switch (format) {
				case PERCENT -> Math.round(currentValue * 100.0F) + "%";
				case DECIMAL -> String.format(Locale.ROOT, "%.2f", currentValue);
				case DEGREES -> Math.round(currentValue) + "\u00b0";
			};
			setMessage(Component.translatable(
					"screen.dynamic_player_model.value",
					Component.translatable(translationKey),
					displayValue
			));
		}

		@Override
		protected void applyValue() {
			float currentValue = currentValue();
			this.value = toSliderValue(currentValue, min, max);
			setter.accept(currentValue);
			updateMessage();
		}

		private float currentValue() {
			float raw = (float) (min + (max - min) * this.value);
			return Math.round(raw / step) * step;
		}

		private static double toSliderValue(float value, float min, float max) {
			float clamped = Math.max(min, Math.min(max, value));
			return (clamped - min) / (max - min);
		}
	}
}

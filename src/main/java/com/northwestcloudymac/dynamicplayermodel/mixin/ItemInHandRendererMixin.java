// SPDX-License-Identifier: MIT
// First-person transform structure adapted from View Model by I-No-oNe (MIT).
// Source revision and license: THIRD_PARTY_NOTICES.md and LICENSES/View-Model-MIT.txt.

package com.northwestcloudymac.dynamicplayermodel.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.northwestcloudymac.dynamicplayermodel.config.PlayerScaleConfig;
import com.northwestcloudymac.dynamicplayermodel.render.ViewModelTransform;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	@Unique
	private boolean dynamicPlayerModel$viewTransformPushed;

	// The second isEmpty call is vanilla's off-hand check for two-handed maps.
	@ModifyExpressionValue(
			method = "submitArmWithItem",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z",
					ordinal = 1
			)
	)
	private boolean dynamicPlayerModel$selectMapRenderMode(boolean offHandEmpty) {
		return offHandEmpty && PlayerScaleConfig.get().twoHandedMaps;
	}

	@Inject(
			method = "submitArmWithItem",
			at = @At("HEAD"),
			cancellable = true
	)
	private void dynamicPlayerModel$beforeRenderArmWithItem(
			AbstractClientPlayer player,
			float partialTick,
			float pitch,
			InteractionHand hand,
			float swingProgress,
			ItemStack itemStack,
			float equippedProgress,
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			int light,
			CallbackInfo ci
	) {
		dynamicPlayerModel$viewTransformPushed = false;

		PlayerScaleConfig config = PlayerScaleConfig.get();
		boolean holdingMap = player.getMainHandItem().has(DataComponents.MAP_ID)
				|| player.getOffhandItem().has(DataComponents.MAP_ID);

		if (!holdingMap && config.viewModelEnabled && config.hideEmptyHands && itemStack.isEmpty()) {
			ci.cancel();
			return;
		}

		if (!holdingMap) {
			dynamicPlayerModel$viewTransformPushed = ViewModelTransform.pushAndApply(poseStack, hand, itemStack);
		}
	}

	@Inject(
			method = "submitArmWithItem",
			at = @At("RETURN")
	)
	private void dynamicPlayerModel$afterRenderArmWithItem(
			AbstractClientPlayer player,
			float partialTick,
			float pitch,
			InteractionHand hand,
			float swingProgress,
			ItemStack itemStack,
			float equippedProgress,
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			int light,
			CallbackInfo ci
	) {
		if (dynamicPlayerModel$viewTransformPushed) {
			poseStack.popPose();
			dynamicPlayerModel$viewTransformPushed = false;
		}
	}
}

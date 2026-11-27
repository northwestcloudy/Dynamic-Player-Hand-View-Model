// SPDX-License-Identifier: MIT
// First-person transform structure adapted from View Model by I-No-oNe (MIT).
// Source revision and license: THIRD_PARTY_NOTICES.md and LICENSES/View-Model-MIT.txt.

package com.northwestcloudymac.dynamicplayermodel.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.northwestcloudymac.dynamicplayermodel.config.PlayerScaleConfig;
import com.northwestcloudymac.dynamicplayermodel.render.ViewModelTransform;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class FirstPersonHandsAndItemsRendererMixin {
	@ModifyExpressionValue(
			method = "submitArmWithItem",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"
			),
			slice = @Slice(from = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/renderer/state/level/FirstPersonHandsAndItemsRenderState;offHandItem:Lnet/minecraft/world/item/ItemStack;"
			)),
			allow = 1
	)
	private boolean dynamicPlayerModel$selectMapRenderMode(boolean offHandEmpty) {
		return offHandEmpty && PlayerScaleConfig.get().twoHandedMaps;
	}

	@WrapMethod(method = "submitArmWithItem")
	private void dynamicPlayerModel$renderArmWithItem(
			PlayerRenderState playerState,
			FirstPersonHandsAndItemsRenderState handsState,
			float partialTick,
			float pitch,
			InteractionHand hand,
			float swingProgress,
			ItemStack itemStack,
			float equippedProgress,
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			int light,
			Operation<Void> original
	) {
		PlayerScaleConfig config = PlayerScaleConfig.get();
		if (!config.viewModelEnabled || handsState.isScoping || playerState.avatarRenderState == null
				|| handsState.mainHandItem.has(DataComponents.MAP_ID) || handsState.offHandItem.has(DataComponents.MAP_ID)) {
			original.call(playerState, handsState, partialTick, pitch, hand, swingProgress, itemStack,
					equippedProgress, poseStack, submitNodeCollector, light);
			return;
		}
		if (config.hideEmptyHands && itemStack.isEmpty()) {
			return;
		}
		poseStack.pushPose();
		try {
			ViewModelTransform.apply(poseStack, hand, playerState.avatarRenderState.mainArm, itemStack.isEmpty(), config);
			original.call(playerState, handsState, partialTick, pitch, hand, swingProgress, itemStack,
					equippedProgress, poseStack, submitNodeCollector, light);
		} finally {
			poseStack.popPose();
		}
	}
}

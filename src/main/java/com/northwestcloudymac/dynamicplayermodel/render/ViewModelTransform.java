// SPDX-License-Identifier: MIT
// First-person transform structure adapted from View Model by I-No-oNe (MIT).
// Source revision and license: THIRD_PARTY_NOTICES.md and LICENSES/View-Model-MIT.txt.

package com.northwestcloudymac.dynamicplayermodel.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.northwestcloudymac.dynamicplayermodel.config.PlayerScaleConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public final class ViewModelTransform {
	private ViewModelTransform() {
	}

	public static boolean pushAndApply(PoseStack poseStack, InteractionHand hand, ItemStack itemStack) {
		PlayerScaleConfig config = PlayerScaleConfig.get();

		if (!config.viewModelEnabled || itemStack.has(DataComponents.MAP_ID)) {
			return false;
		}

		boolean mainHand = hand == InteractionHand.MAIN_HAND;
		float scale = mainHand ? config.mainHandScale : config.offHandScale;
		float positionX = mainHand ? config.mainPositionX : config.offPositionX;
		float positionY = mainHand ? config.mainPositionY : config.offPositionY;
		float positionZ = mainHand ? config.mainPositionZ : config.offPositionZ;
		float rotationX = mainHand ? config.mainRotationX : config.offRotationX;
		float rotationY = mainHand ? config.mainRotationY : config.offRotationY;
		float rotationZ = mainHand ? config.mainRotationZ : config.offRotationZ;

		poseStack.pushPose();
		poseStack.mulPose(Axis.XP.rotationDegrees(rotationX));
		poseStack.mulPose(Axis.YP.rotationDegrees(rotationY));
		poseStack.mulPose(Axis.ZP.rotationDegrees(rotationZ));
		poseStack.translate(positionX, positionY, positionZ);
		poseStack.scale(scale, scale, scale);

		if (config.compactEmptyHands && itemStack.isEmpty()) {
			float side = renderedArm(hand) == HumanoidArm.RIGHT ? 1.0F : -1.0F;
			poseStack.translate(0.025F * side, 0.05F, -0.08F);
			poseStack.scale(config.emptyHandScale, config.emptyHandScale, config.emptyHandScale);
		}

		return true;
	}

	private static HumanoidArm renderedArm(InteractionHand hand) {
		HumanoidArm mainArm = Minecraft.getInstance().player == null
				? HumanoidArm.RIGHT
				: Minecraft.getInstance().player.getMainArm();
		return hand == InteractionHand.MAIN_HAND ? mainArm : mainArm.getOpposite();
	}
}

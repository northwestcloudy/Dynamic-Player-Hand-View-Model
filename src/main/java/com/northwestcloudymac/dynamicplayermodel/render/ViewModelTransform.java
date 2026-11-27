// SPDX-License-Identifier: MIT
// First-person transform structure adapted from View Model by I-No-oNe (MIT).
// Source revision and license: THIRD_PARTY_NOTICES.md and LICENSES/View-Model-MIT.txt.

package com.northwestcloudymac.dynamicplayermodel.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.northwestcloudymac.dynamicplayermodel.config.PlayerScaleConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public final class ViewModelTransform {
	private ViewModelTransform() {
	}

	public static void apply(PoseStack poseStack, InteractionHand hand, HumanoidArm mainArm,
			boolean emptyHand, PlayerScaleConfig config) {
		boolean mainHand = hand == InteractionHand.MAIN_HAND;
		float scale = mainHand ? config.mainHandScale : config.offHandScale;
		float positionX = mainHand ? config.mainPositionX : config.offPositionX;
		float positionY = mainHand ? config.mainPositionY : config.offPositionY;
		float positionZ = mainHand ? config.mainPositionZ : config.offPositionZ;
		float rotationX = mainHand ? config.mainRotationX : config.offRotationX;
		float rotationY = mainHand ? config.mainRotationY : config.offRotationY;
		float rotationZ = mainHand ? config.mainRotationZ : config.offRotationZ;

		poseStack.rotateDegrees(Axis.XP, rotationX);
		poseStack.rotateDegrees(Axis.YP, rotationY);
		poseStack.rotateDegrees(Axis.ZP, rotationZ);
		poseStack.translate(positionX, positionY, positionZ);
		poseStack.scale(scale, scale, scale);

		if (config.compactEmptyHands && emptyHand) {
			HumanoidArm arm = mainHand ? mainArm : mainArm.getOpposite();
			float side = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
			poseStack.translate(0.025F * side, 0.05F, -0.08F);
			poseStack.scale(config.emptyHandScale, config.emptyHandScale, config.emptyHandScale);
		}
	}
}

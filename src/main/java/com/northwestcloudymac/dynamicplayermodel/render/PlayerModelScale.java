package com.northwestcloudymac.dynamicplayermodel.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.world.entity.EquipmentSlot;

public final class PlayerModelScale {
	private static final float BODY_HEIGHT = 12.0F;
	private static final float LEG_HEIGHT = 12.0F;
	private static final float ARM_TOP_OFFSET = 2.0F;

	private PlayerModelScale() {
	}

	public static void applyPlayerScale(PlayerModel model, float headScale, float bodyScale) {
		Anchors anchors = calculateAnchors(model, bodyScale);
		float rightArmX = model.rightArm.x * bodyScale;
		float leftArmX = model.leftArm.x * bodyScale;
		float rightLegX = model.rightLeg.x * bodyScale;
		float leftLegX = model.leftLeg.x * bodyScale;

		scaleAndPosition(model.head, headScale, model.head.x, anchors.bodyTopY(), model.head.z);
		resetChildOverlay(model.hat);
		scaleAndPosition(model.body, bodyScale, model.body.x, anchors.bodyTopY(), model.body.z);
		resetChildOverlay(model.jacket);

		scaleAndPosition(model.rightArm, bodyScale, rightArmX, anchors.armY(), model.rightArm.z);
		resetChildOverlay(model.rightSleeve);
		scaleAndPosition(model.leftArm, bodyScale, leftArmX, anchors.armY(), model.leftArm.z);
		resetChildOverlay(model.leftSleeve);

		scaleAndPosition(model.rightLeg, bodyScale, rightLegX, anchors.legY(), model.rightLeg.z);
		resetChildOverlay(model.rightPants);
		scaleAndPosition(model.leftLeg, bodyScale, leftLegX, anchors.legY(), model.leftLeg.z);
		resetChildOverlay(model.leftPants);
	}

	public static void resetPlayerScale(PlayerModel model) {
		resetHumanoidScale(model);
		scale(model.leftSleeve, 1.0F);
		scale(model.rightSleeve, 1.0F);
		scale(model.leftPants, 1.0F);
		scale(model.rightPants, 1.0F);
		scale(model.jacket, 1.0F);
	}

	public static void applyArmorTransform(PoseStack poseStack, EquipmentSlot slot, float headScale, float bodyScale) {
		float scale = slot == EquipmentSlot.HEAD ? headScale : bodyScale;
		float groundOffset = (BODY_HEIGHT + LEG_HEIGHT) * (1.0F - bodyScale) / 16.0F;
		poseStack.translate(0.0F, groundOffset, 0.0F);
		poseStack.scale(scale, scale, scale);
	}

	public static void resetHumanoidScale(HumanoidModel<?> model) {
		scale(model.head, 1.0F);
		scale(model.hat, 1.0F);
		scale(model.body, 1.0F);
		scale(model.rightArm, 1.0F);
		scale(model.leftArm, 1.0F);
		scale(model.rightLeg, 1.0F);
		scale(model.leftLeg, 1.0F);
	}

	private static Anchors calculateAnchors(HumanoidModel<?> model, float bodyScale) {
		float feetY = Math.max(model.rightLeg.y, model.leftLeg.y) + LEG_HEIGHT;
		float legY = feetY - LEG_HEIGHT * bodyScale;
		float bodyTopY = legY - BODY_HEIGHT * bodyScale;
		float armY = bodyTopY + ARM_TOP_OFFSET * bodyScale;
		return new Anchors(bodyTopY, armY, legY);
	}

	private static void scaleAndPosition(ModelPart part, float scale, float x, float y, float z) {
		scale(part, scale);
		part.setPos(x, y, z);
	}

	private static void resetChildOverlay(ModelPart part) {
		scale(part, 1.0F);
		part.setPos(0.0F, 0.0F, 0.0F);
	}

	private static void scale(ModelPart part, float scale) {
		part.xScale = scale;
		part.yScale = scale;
		part.zScale = scale;
	}

	private record Anchors(float bodyTopY, float armY, float legY) {
	}
}

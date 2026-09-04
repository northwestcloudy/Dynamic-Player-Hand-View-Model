package com.northwestcloudymac.dynamicplayermodel.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.northwestcloudymac.dynamicplayermodel.config.PlayerScaleConfig;
import com.northwestcloudymac.dynamicplayermodel.render.PlayerModelScale;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin {
	@Unique
	private boolean dynamicPlayerModel$armorTransformPushed;

	@Inject(method = "renderArmorPiece", at = @At("HEAD"))
	private void dynamicPlayerModel$beforeRenderArmorPiece(
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			ItemStack itemStack,
			EquipmentSlot slot,
			int light,
			HumanoidRenderState state,
			CallbackInfo ci
	) {
		dynamicPlayerModel$armorTransformPushed = false;
		PlayerScaleConfig config = PlayerScaleConfig.get();

		if (!(state instanceof AvatarRenderState avatarState)
				|| !config.scaleArmor
				|| !config.shouldScale(avatarState)
				|| !HumanoidArmorLayer.shouldRender(itemStack, slot)) {
			return;
		}

		poseStack.pushPose();
		PlayerModelScale.applyArmorTransform(poseStack, slot, config.headScale(), config.bodyScale());
		dynamicPlayerModel$armorTransformPushed = true;
	}

	@Inject(method = "renderArmorPiece", at = @At("RETURN"))
	private void dynamicPlayerModel$afterRenderArmorPiece(
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			ItemStack itemStack,
			EquipmentSlot slot,
			int light,
			HumanoidRenderState state,
			CallbackInfo ci
	) {
		if (dynamicPlayerModel$armorTransformPushed) {
			poseStack.popPose();
			dynamicPlayerModel$armorTransformPushed = false;
		}
	}
}

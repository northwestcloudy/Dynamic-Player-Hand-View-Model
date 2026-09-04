package com.northwestcloudymac.dynamicplayermodel.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.northwestcloudymac.dynamicplayermodel.config.PlayerScaleConfig;
import com.northwestcloudymac.dynamicplayermodel.render.PlayerModelScale;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin {
	@Unique
	private ModelPart dynamicPlayerModel$heldItemArm;
	@Unique
	private float dynamicPlayerModel$heldItemArmXScale = 1.0F;
	@Unique
	private float dynamicPlayerModel$heldItemArmYScale = 1.0F;
	@Unique
	private float dynamicPlayerModel$heldItemArmZScale = 1.0F;

	@Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
	private void dynamicPlayerModel$scalePlayerParts(AvatarRenderState state, CallbackInfo ci) {
		PlayerModel model = (PlayerModel) (Object) this;
		PlayerScaleConfig config = PlayerScaleConfig.get();

		if (!config.shouldScale(state)) {
			PlayerModelScale.resetPlayerScale(model);
			return;
		}

		PlayerModelScale.applyPlayerScale(model, config.headScale(), config.bodyScale());
	}

	@Inject(method = "translateToHand(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At("HEAD"))
	private void dynamicPlayerModel$beforeTranslateToHand(AvatarRenderState state, HumanoidArm arm, PoseStack poseStack, CallbackInfo ci) {
		PlayerScaleConfig config = PlayerScaleConfig.get();

		if (config.scaleHeldItems || !config.shouldScale(state)) {
			dynamicPlayerModel$heldItemArm = null;
			return;
		}

		PlayerModel model = (PlayerModel) (Object) this;
		ModelPart armPart = model.getArm(arm);
		dynamicPlayerModel$heldItemArm = armPart;
		dynamicPlayerModel$heldItemArmXScale = armPart.xScale;
		dynamicPlayerModel$heldItemArmYScale = armPart.yScale;
		dynamicPlayerModel$heldItemArmZScale = armPart.zScale;
		armPart.xScale = 1.0F;
		armPart.yScale = 1.0F;
		armPart.zScale = 1.0F;
	}

	@Inject(method = "translateToHand(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At("TAIL"))
	private void dynamicPlayerModel$afterTranslateToHand(AvatarRenderState state, HumanoidArm arm, PoseStack poseStack, CallbackInfo ci) {
		if (dynamicPlayerModel$heldItemArm == null) {
			return;
		}

		dynamicPlayerModel$heldItemArm.xScale = dynamicPlayerModel$heldItemArmXScale;
		dynamicPlayerModel$heldItemArm.yScale = dynamicPlayerModel$heldItemArmYScale;
		dynamicPlayerModel$heldItemArm.zScale = dynamicPlayerModel$heldItemArmZScale;
		dynamicPlayerModel$heldItemArm = null;
	}
}

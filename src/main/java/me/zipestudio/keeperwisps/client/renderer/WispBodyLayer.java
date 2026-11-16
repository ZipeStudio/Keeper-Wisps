package me.zipestudio.keeperwisps.client.renderer;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

import me.zipestudio.keeperwisps.client.model.WispModel;

//? if >=1.21.11 {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.*;
//?} else {
/*import net.minecraft.client.renderer.*;
import net.minecraft.world.item.*;

import me.zipestudio.keeperwisps.entity.WispEntity;
*///?}

//? if >=1.21.11 {
public class WispBodyLayer extends RenderLayer<WispRenderState, WispModel> {
//?} else {
/*public class WispBodyLayer extends RenderLayer<WispEntity, WispModel> {
*///?}

	private static final float SPIN_DEGREES_PER_TICK = 2.25F;

	//? if >=1.21.11 {
	public WispBodyLayer(RenderLayerParent<WispRenderState, WispModel> parent) {
		super(parent);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, WispRenderState state, float yRot, float xRot) {
		if (!state.displayItem.isEmpty()) {
			poseStack.pushPose();
			this.translateToItem(poseStack, state.ageInTicks);
			state.displayItem.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
			poseStack.popPose();
		}

		RenderType renderType = RenderTypes.entityTranslucent(WispRenderer.getTexture(state.variant));
		//? if >=26.3 {
		submitNodeCollector.order(1).submitModel(this.getParentModel(), state, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
		//?} else {
		/*submitNodeCollector.order(1).submitModel(this.getParentModel(), state, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
		*///?}
	}
	//?} else {
	/*private final ItemRenderer itemRenderer;

	public WispBodyLayer(RenderLayerParent<WispEntity, WispModel> parent, ItemRenderer itemRenderer) {
		super(parent);
		this.itemRenderer = itemRenderer;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffers, int light, WispEntity wisp, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack stack = wisp.getDisplayItem();
		if (!stack.isEmpty()) {
			poseStack.pushPose();
			this.translateToItem(poseStack, ageInTicks);
			this.itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, poseStack, buffers, wisp.level(), wisp.getId());
			poseStack.popPose();
			if (buffers instanceof MultiBufferSource.BufferSource bufferSource) {
				bufferSource.endBatch();
			}
		}

		WispModel model = this.getParentModel();
		VertexConsumer consumer = buffers.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(wisp)));
		//? if >=1.21 {
		model.renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, -1);
		//?} else {
		/^model.renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		^///?}
	}
	*///?}

	private void translateToItem(PoseStack poseStack, float ageInTicks) {
		this.getParentModel().translateToItem(poseStack);
		//? if >=26.3 {
		poseStack.rotate(Axis.ZP.rotationDegrees(180.0F));
		poseStack.rotate(Axis.YP.rotationDegrees(ageInTicks * SPIN_DEGREES_PER_TICK));
		//?} else {
		/*poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		poseStack.mulPose(Axis.YP.rotationDegrees(ageInTicks * SPIN_DEGREES_PER_TICK));
		*///?}
		poseStack.translate(0.0F, -0.15F, 0.0F);
	}

}

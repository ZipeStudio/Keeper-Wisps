package me.zipestudio.keeperwisps.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import me.zipestudio.keeperwisps.KeeperWisps;

//? if >=1.21.11 {
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import me.zipestudio.keeperwisps.client.renderer.WispRenderState;
//?} else {
/*import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.RenderType;

import me.zipestudio.keeperwisps.entity.WispEntity;
*///?}

//? if >=1.21.11 {
public class WispModel extends EntityModel<WispRenderState> {
//?} else {
/*public class WispModel extends HierarchicalModel<WispEntity> {
*///?}

	public static final ModelLayerLocation LAYER = new ModelLayerLocation(KeeperWisps.id("wisp"), "main");

	private final ModelPart modelRoot;
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart item;
	//? if >=1.21.11 {
	private final KeyframeAnimation idleAnimation;
	//?}

	public WispModel(ModelPart modelRoot) {
		//? if >=1.21.11 {
		super(modelRoot, RenderTypes::entityTranslucent);
		//?} else {
		/*super(RenderType::entityTranslucent);
		*///?}
		this.modelRoot = modelRoot;
		this.root = modelRoot.getChild("root");
		this.body = this.root.getChild("body");
		this.item = this.body.getChild("item");
		//? if >=1.21.11 {
		this.idleAnimation = WispAnimations.IDLE.bake(modelRoot);
		//?}
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition parts = mesh.getRoot();

		PartDefinition root = parts.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-4.5F, -4.5F, -4.5F, 9.0F, 9.0F, 9.0F), PartPose.offset(0.0F, -4.5F, 0.0F));

		body.addOrReplaceChild("left_foot", CubeListBuilder.create()
				.texOffs(0, 18).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F), PartPose.offset(2.5F, 4.5F, -3.5F));
		body.addOrReplaceChild("right_foot", CubeListBuilder.create()
				.texOffs(4, 18).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F), PartPose.offset(-2.5F, 4.5F, -3.5F));

		body.addOrReplaceChild("left_wing", CubeListBuilder.create()
				.texOffs(8, 18).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 3.0F), PartPose.offsetAndRotation(4.25F, 0.5F, 4.25F, 0.0F, 22.5F * Mth.DEG_TO_RAD, 0.0F));
		body.addOrReplaceChild("right_wing", CubeListBuilder.create()
				.texOffs(14, 18).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 3.0F), PartPose.offsetAndRotation(-4.25F, 0.5F, 4.25F, 0.0F, -22.5F * Mth.DEG_TO_RAD, 0.0F));

		body.addOrReplaceChild("item", CubeListBuilder.create(), PartPose.ZERO);

		return LayerDefinition.create(mesh, 64, 32);
	}

	//? if >=1.21.11 {
	@Override
	public void setupAnim(WispRenderState state) {
		super.setupAnim(state);
		this.body.yRot = state.yRot * Mth.DEG_TO_RAD;
		this.body.xRot = state.xRot * Mth.DEG_TO_RAD;
		this.idleAnimation.apply(state.idleAnimationState, state.ageInTicks);
	}
	//?} else {
	/*@Override
	public void setupAnim(WispEntity wisp, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.body.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.body.xRot = headPitch * Mth.DEG_TO_RAD;
		this.animate(wisp.idleAnimationState, WispAnimations.IDLE, ageInTicks);
	}
	*///?}

	public void translateToItem(PoseStack poseStack) {
		this.modelRoot.translateAndRotate(poseStack);
		this.root.translateAndRotate(poseStack);
		this.body.translateAndRotate(poseStack);
		this.item.translateAndRotate(poseStack);
	}

	//? if <1.21.11 {
	/*@Override
	public ModelPart root() {
		return this.modelRoot;
	}
	*///?}

}

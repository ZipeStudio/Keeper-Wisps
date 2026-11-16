package me.zipestudio.keeperwisps.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.*;

import net.minecraft.client.renderer.entity.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import me.zipestudio.keeperwisps.KeeperWisps;
import me.zipestudio.keeperwisps.client.model.WispModel;
import me.zipestudio.keeperwisps.entity.*;

//? if >=1.21.11 {
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemDisplayContext;
//?} else {
/*import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
*///?}

//? if >=1.21.11 {
public class WispRenderer extends MobRenderer<WispEntity, WispRenderState, WispModel> {
//?} else {
/*public class WispRenderer extends MobRenderer<WispEntity, WispModel> {
*///?}

	private static final Map<WispVariant, Identifier> TEXTURES = Util.make(new EnumMap<>(WispVariant.class), map -> {
		for (WispVariant variant : WispVariant.values()) {
			map.put(variant, KeeperWisps.id("textures/entity/wisp/wisp_" + variant.getSerializedName() + ".png"));
		}
	});

	private static final float PULSE_STRENGTH = 0.2F;
	private static final float READY_SCALE = 0.25F;
	private static final float READY_BREATH = 0.12F;
	private static final float READY_BREATH_SPEED = 0.25F;

	public WispRenderer(EntityRendererProvider.Context context) {
		super(context, new WispModel(context.bakeLayer(WispModel.LAYER)), 0.3F);
		//? if >=1.21.11 {
		this.addLayer(new WispBodyLayer(this));
		//?} else {
		/*this.addLayer(new WispBodyLayer(this, context.getItemRenderer()));
		*///?}
	}

	public static Identifier getTexture(WispVariant variant) {
		return TEXTURES.get(variant);
	}

	//? if >=1.21.11 {
	@Override
	public WispRenderState createRenderState() {
		return new WispRenderState();
	}

	@Override
	public void extractRenderState(WispEntity entity, WispRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.variant = entity.getVariant();
		state.idleAnimationState.copyFrom(entity.idleAnimationState);
		state.wispScale = getScale(entity, partialTicks);
		this.itemModelResolver.updateForNonLiving(state.displayItem, entity.getDisplayItem(), ItemDisplayContext.GROUND, entity);
	}

	@Override
	public Identifier getTextureLocation(WispRenderState state) {
		return getTexture(state.variant);
	}

	@Override
	protected RenderType getRenderType(WispRenderState state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing) {
		return null;
	}
	//?} else {
	/*@Override
	public Identifier getTextureLocation(WispEntity wisp) {
		return getTexture(wisp.getVariant());
	}

	@Override
	protected RenderType getRenderType(WispEntity wisp, boolean bodyVisible, boolean translucent, boolean glowing) {
		return null;
	}
	*///?}

	@Override
	protected int getBlockLightLevel(WispEntity wisp, BlockPos pos) {
		return 15;
	}

	//? if >=1.21.11 {
	@Override
	protected float getFlipDegrees() {
		return 0.0F;
	}

	@Override
	protected void scale(WispRenderState state, PoseStack poseStack) {
		poseStack.scale(state.wispScale, state.wispScale, state.wispScale);
	}
	//?} else {
	/*@Override
	protected float getFlipDegrees(WispEntity wisp) {
		return 0.0F;
	}

	@Override
	protected void scale(WispEntity wisp, PoseStack poseStack, float partialTick) {
		float scale = getScale(wisp, partialTick);
		poseStack.scale(scale, scale, scale);
	}
	*///?}

	private static float getScale(WispEntity wisp, float partialTick) {
		float scale = 1.0F;
		if (wisp.getDyingTicks() > 0) {
			scale *= Math.max(0.0F, 1.0F - (wisp.getDyingTicks() + partialTick) / WispEntity.DEATH_TICKS);
		}
		float ready = wisp.getReadyProgress(partialTick);
		if (ready > 0.001F) {
			float breath = READY_BREATH * Mth.sin((wisp.tickCount + partialTick) * READY_BREATH_SPEED) * ready;
			scale *= 1.0F + (READY_SCALE * ready) + breath;
		}
		if (wisp.getRetrievePulseTicks() > 0) {
			float progress = 1.0F - Math.max(0.0F, wisp.getRetrievePulseTicks() - partialTick) / WispEntity.RETRIEVE_PULSE_TICKS;
			scale *= 1.0F - PULSE_STRENGTH * Mth.sin(progress * Mth.TWO_PI) * (1.0F - progress);
		}
		return scale;
	}

}

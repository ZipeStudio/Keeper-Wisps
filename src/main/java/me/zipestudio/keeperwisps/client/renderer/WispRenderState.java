package me.zipestudio.keeperwisps.client.renderer;

//? if >=1.21.11 {
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.AnimationState;

import me.zipestudio.keeperwisps.entity.WispVariant;

public class WispRenderState extends LivingEntityRenderState {

	public final AnimationState idleAnimationState = new AnimationState();
	public final ItemStackRenderState displayItem = new ItemStackRenderState();
	public WispVariant variant = WispVariant.GREEN;
	public float wispScale = 1.0F;

}
//?}

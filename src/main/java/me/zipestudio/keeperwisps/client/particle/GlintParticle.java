package me.zipestudio.keeperwisps.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
//? if >=1.21.11 {
import net.minecraft.util.RandomSource;
//?}

//? if >=1.21.11 {
public class GlintParticle extends SingleQuadParticle {
//?} else {
/*public class GlintParticle extends TextureSheetParticle {
*///?}

	private static final int FULL_BRIGHT = 0xF000F0;

	private final SpriteSet sprites;

	protected GlintParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites, int color) {
		//? if >=1.21.11 {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.first());
		//?} else {
		/*super(level, x, y, z, xSpeed, ySpeed, zSpeed);
		*///?}
		this.sprites = sprites;
		this.xd = xSpeed;
		this.yd = ySpeed;
		this.zd = zSpeed;
		this.friction = 0.9F;
		this.gravity = 0.0F;
		this.hasPhysics = false;
		this.lifetime = 4 + this.random.nextInt(3);
		this.quadSize = 0.08F + this.random.nextFloat() * 0.06F;
		this.setColor(((color >> 16) & 0xFF) / 255.0F, ((color >> 8) & 0xFF) / 255.0F, (color & 0xFF) / 255.0F);
		this.setSpriteFromAge(sprites);
	}

	@Override
	public void tick() {
		super.tick();
		this.setSpriteFromAge(this.sprites);
	}

	//? if >=1.21.11 {
	@Override
	protected Layer getLayer() {
		return Layer.TRANSLUCENT;
	}
	//?} else {
	/*@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	*///?}

	//? if >=26.1 {
	@Override
	protected int getLightCoords(float a) {
		return FULL_BRIGHT;
	}
	//?} else {
	/*@Override
	protected int getLightColor(float partialTick) {
		return FULL_BRIGHT;
	}
	*///?}

	public static class Provider implements ParticleProvider<SimpleParticleType> {

		private final SpriteSet sprites;
		private final int color;

		public Provider(SpriteSet sprites, int color) {
			this.sprites = sprites;
			this.color = color;
		}

		//? if >=1.21.11 {
		@Override
		public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			return new GlintParticle(level, x, y, z, xAux, yAux, zAux, this.sprites, this.color);
		}
		//?} else {
		/*@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new GlintParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites, this.color);
		}
		*///?}

	}

}

package me.zipestudio.keeperwisps.registry;

import net.minecraft.resources.Identifier;
//? if >=1.21.2 {
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
//?}
import net.minecraft.world.entity.*;

import me.zipestudio.keeperwisps.KeeperWisps;
import me.zipestudio.keeperwisps.entity.WispEntity;

public class KeeperWispsEntities {

	public static final Identifier WISP_ID = KeeperWisps.id("wisp");

	public static final EntityType<WispEntity> WISP = EntityType.Builder.<WispEntity>of(WispEntity::new, MobCategory.MISC)
			.sized(0.6F, 0.6F)
			//? if >=1.21 {
			.eyeHeight(WispEntity.EYE_HEIGHT)
			//?}
			.fireImmune()
			.clientTrackingRange(8)
			.updateInterval(2)
			//? if >=1.21.2 {
			.build(ResourceKey.create(Registries.ENTITY_TYPE, WISP_ID));
			//?} else {
			/*.build(WISP_ID.toString());
			*///?}

}

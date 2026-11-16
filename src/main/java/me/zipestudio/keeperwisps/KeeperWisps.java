package me.zipestudio.keeperwisps;

import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;
import org.slf4j.*;

import me.zipestudio.keeperwisps.compat.AccessoryCompats;
import me.zipestudio.keeperwisps.config.KeeperWispsConfig;

public class KeeperWisps {

	public static final String MOD_NAME = /*$ mod_name*/ "Keeper Wisps";
	public static final String MOD_ID = /*$ mod_id*/ "keeper_wisps";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static Identifier id(String path) {
		//? if >=1.21 {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
		//?} else {
		/*return Identifier.tryBuild(MOD_ID, path);
		 *///?}
	}

	public static MutableComponent text(String path, Object... args) {
		return Component.translatable(String.format("%s.%s", MOD_ID, path), args);
	}

	public static void onInitialize() {
		KeeperWispsConfig.reload();
		AccessoryCompats.init();
		LOGGER.info("{} Initialized", MOD_NAME);
	}

}

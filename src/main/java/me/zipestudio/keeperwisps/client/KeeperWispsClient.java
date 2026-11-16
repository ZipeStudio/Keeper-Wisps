package me.zipestudio.keeperwisps.client;

import org.slf4j.*;

import me.zipestudio.keeperwisps.KeeperWisps;

public class KeeperWispsClient {

	public static final Logger LOGGER = LoggerFactory.getLogger(KeeperWisps.MOD_NAME + "/Client");

	public static void onInitializeClient() {
		LOGGER.info("{} Client Initialized", KeeperWisps.MOD_NAME);
	}

}

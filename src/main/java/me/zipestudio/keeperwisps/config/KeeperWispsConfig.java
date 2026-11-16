package me.zipestudio.keeperwisps.config;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import lombok.*;
import org.slf4j.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import me.zipestudio.keeperwisps.KeeperWisps;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?} elif neoforge {
/*import net.neoforged.fml.loading.FMLPaths;
*///?} elif forge {
/*import net.minecraftforge.fml.loading.FMLPaths;
*///?}

@Getter
@AllArgsConstructor
public class KeeperWispsConfig {

	private static final float DEFAULT_EXCESS_XP_REFUND = 0.10F;
	private static final int DEFAULT_FIRST_WISP_LEVEL = 8;
	private static final int DEFAULT_LEVELS_PER_WISP = 1;

	public static final Codec<KeeperWispsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.optionalFieldOf("excess_xp_refund", DEFAULT_EXCESS_XP_REFUND).forGetter(KeeperWispsConfig::getExcessXpRefund),
			Codec.INT.optionalFieldOf("first_wisp_level", DEFAULT_FIRST_WISP_LEVEL).forGetter(KeeperWispsConfig::getFirstWispLevel),
			Codec.INT.optionalFieldOf("levels_per_wisp", DEFAULT_LEVELS_PER_WISP).forGetter(KeeperWispsConfig::getLevelsPerWisp)
	).apply(instance, KeeperWispsConfig::new));

	private static final File CONFIG_FILE = getConfigDir().resolve(KeeperWisps.MOD_ID + ".json5").toFile();
	private static final Logger LOGGER = LoggerFactory.getLogger(KeeperWisps.MOD_NAME + "/Config");
	private static KeeperWispsConfig INSTANCE;

	private float excessXpRefund;
	private int firstWispLevel;
	private int levelsPerWisp;

	@SuppressWarnings("unused")
	private KeeperWispsConfig() {
		throw new IllegalArgumentException();
	}

	public static KeeperWispsConfig getInstance() {
		return INSTANCE == null ? reload() : INSTANCE;
	}

	public static KeeperWispsConfig reload() {
		return INSTANCE = KeeperWispsConfig.read();
	}

	private static KeeperWispsConfig read() {
		if (!CONFIG_FILE.exists()) {
			writeDefault();
		}
		try (JsonReader reader = new JsonReader(Files.newBufferedReader(CONFIG_FILE.toPath(), StandardCharsets.UTF_8))) {
			reader.setLenient(true);
			JsonElement json = JsonParser.parseReader(reader);
			return CODEC.parse(JsonOps.INSTANCE, json)
					.resultOrPartial(error -> LOGGER.error("Failed to parse config: {}", error))
					.orElseGet(KeeperWispsConfig::createDefault);
		} catch (IOException | JsonParseException e) {
			LOGGER.error("Failed to read config", e);
			return createDefault();
		}
	}

	private static KeeperWispsConfig createDefault() {
		return new KeeperWispsConfig(DEFAULT_EXCESS_XP_REFUND, DEFAULT_FIRST_WISP_LEVEL, DEFAULT_LEVELS_PER_WISP);
	}

	private static Path getConfigDir() {
		//? if fabric {
		return FabricLoader.getInstance().getConfigDir();
		//?} else {
		/*return FMLPaths.CONFIGDIR.get();
		*///?}
	}

	private static void writeDefault() {
		String text = """
				// Keeper Wisps server config.
				// Changes apply after restarting the server (or the game in singleplayer).
				{
					// Share of the experience above the last Wisp's level (12 with the defaults)
					// that goes into the Wisps.
					// The vanilla amount always comes back (7 points per level, up to 100): part of it
					// in the Wisps, the rest drops on the floor. Everything above the last Wisp's level
					// is multiplied by this value and split evenly between the Wisps.
					//   0.0  - everything above the last Wisp's level is lost
					//   0.1  - default, 10%% of it comes back
					//   1.0  - all of it comes back
					// Range: 0.0 - 1.0
					"excess_xp_refund": %s,

					// The level you need for the first Wisp to spawn.
					"first_wisp_level": %s,

					// How many levels each Wisp after the first one costs. With the defaults the
					// first Wisp needs 8 levels, the second 9, and the fifth 12.
					"levels_per_wisp": %s
				}
				""".formatted(DEFAULT_EXCESS_XP_REFUND, DEFAULT_FIRST_WISP_LEVEL, DEFAULT_LEVELS_PER_WISP);
		try {
			Files.createDirectories(CONFIG_FILE.toPath().getParent());
			Files.writeString(CONFIG_FILE.toPath(), text, StandardCharsets.UTF_8);
		} catch (IOException e) {
			LOGGER.error("Failed to create config", e);
		}
	}

}

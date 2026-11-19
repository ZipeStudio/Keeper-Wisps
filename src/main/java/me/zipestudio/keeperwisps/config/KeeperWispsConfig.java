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
import java.util.List;

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
	private static final boolean DEFAULT_REQUIRE_EYE_CONTACT = true;

	private static final List<String> KEYS = List.of("excess_xp_refund", "first_wisp_level", "levels_per_wisp", "require_eye_contact");

	public static final Codec<KeeperWispsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.optionalFieldOf("excess_xp_refund", DEFAULT_EXCESS_XP_REFUND).forGetter(KeeperWispsConfig::getExcessXpRefund),
			Codec.INT.optionalFieldOf("first_wisp_level", DEFAULT_FIRST_WISP_LEVEL).forGetter(KeeperWispsConfig::getFirstWispLevel),
			Codec.INT.optionalFieldOf("levels_per_wisp", DEFAULT_LEVELS_PER_WISP).forGetter(KeeperWispsConfig::getLevelsPerWisp),
			Codec.BOOL.optionalFieldOf("require_eye_contact", DEFAULT_REQUIRE_EYE_CONTACT).forGetter(KeeperWispsConfig::isRequireEyeContact)
	).apply(instance, KeeperWispsConfig::new));

	private static final File CONFIG_FILE = getConfigDir().resolve(KeeperWisps.MOD_ID + ".json5").toFile();
	private static final Logger LOGGER = LoggerFactory.getLogger(KeeperWisps.MOD_NAME + "/Config");
	private static KeeperWispsConfig INSTANCE;

	private float excessXpRefund;
	private int firstWispLevel;
	private int levelsPerWisp;
	private boolean requireEyeContact;

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
			write(createDefault());
		}
		try (JsonReader reader = new JsonReader(Files.newBufferedReader(CONFIG_FILE.toPath(), StandardCharsets.UTF_8))) {
			reader.setLenient(true);
			JsonElement json = JsonParser.parseReader(reader);
			DataResult<KeeperWispsConfig> result = CODEC.parse(JsonOps.INSTANCE, json);
			KeeperWispsConfig config = result
					.resultOrPartial(error -> LOGGER.error("Failed to parse config: {}", error))
					.orElseGet(KeeperWispsConfig::createDefault);
			if (result.result().isPresent() && json.isJsonObject() && !json.getAsJsonObject().keySet().containsAll(KEYS)) {
				write(config);
			}
			return config;
		} catch (IOException | JsonParseException e) {
			LOGGER.error("Failed to read config", e);
			return createDefault();
		}
	}

	private static KeeperWispsConfig createDefault() {
		return new KeeperWispsConfig(DEFAULT_EXCESS_XP_REFUND, DEFAULT_FIRST_WISP_LEVEL, DEFAULT_LEVELS_PER_WISP, DEFAULT_REQUIRE_EYE_CONTACT);
	}

	private static Path getConfigDir() {
		//? if fabric {
		return FabricLoader.getInstance().getConfigDir();
		//?} else {
		/*return FMLPaths.CONFIGDIR.get();
		*///?}
	}

	private static void write(KeeperWispsConfig config) {
		String text = """
				// Keeper Wisps config, changes apply after a restart.
				// All options are described on the mod page.
				{
					// Share of the experience above the last Wisp's level that the Wisps keep, from 0.0 to 1.0
					"excess_xp_refund": %s,

					// Level needed for the first Wisp
					"first_wisp_level": %s,

					// Extra levels needed for each next Wisp
					"levels_per_wisp": %s,

					// Whether the Wisp has to look back at you before you can take your items
					"require_eye_contact": %s
				}
				""".formatted(config.getExcessXpRefund(), config.getFirstWispLevel(), config.getLevelsPerWisp(), config.isRequireEyeContact());
		try {
			Files.createDirectories(CONFIG_FILE.toPath().getParent());
			Files.writeString(CONFIG_FILE.toPath(), text, StandardCharsets.UTF_8);
		} catch (IOException e) {
			LOGGER.error("Failed to write config", e);
		}
	}

}

/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import vazkii.psi.common.platform.PsiConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes.BOOLEAN;
import static io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes.INTEGER;

import io.github.fablabsmc.fablabs.api.fiber.v1.builder.ConfigTreeBuilder;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.NumberConfigType;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public final class FiberPsiConfig {

	private static final System.Logger LOGGER = System.getLogger(FiberPsiConfig.class.getName());
	private static final Client CLIENT = new Client();
	private static final Common COMMON = new Common();

	private FiberPsiConfig() {}

	public static void setup() {
		Path configDirectory = FabricLoader.getInstance().getConfigDir();
		try {
			Files.createDirectories(configDirectory);
		} catch (IOException e) {
			LOGGER.log(System.Logger.Level.WARNING, "Failed to create the Psi config directory", e);
		}

		JanksonValueSerializer serializer = new JanksonValueSerializer(false);
		ConfigTree common = COMMON.configure(ConfigTree.builder());
		setupConfig(common, configDirectory.resolve("psi-common.json5"), serializer);
		PsiConfig.setCommon(COMMON);

		if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			ConfigTree client = CLIENT.configure(ConfigTree.builder());
			setupConfig(client, configDirectory.resolve("psi-client.json5"), serializer);
			PsiConfig.setClient(CLIENT);
		}
	}

	private static void setupConfig(ConfigTree config, Path path, JanksonValueSerializer serializer) {
		writeDefaultConfig(config, path, serializer);
		try (InputStream stream = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ, StandardOpenOption.CREATE))) {
			FiberSerialization.deserialize(config, stream, serializer);
		} catch (IOException | ValueDeserializationException e) {
			LOGGER.log(System.Logger.Level.ERROR, "Error loading Psi configuration from " + path, e);
		}
	}

	private static void writeDefaultConfig(ConfigTree config, Path path, JanksonValueSerializer serializer) {
		try (OutputStream stream = new BufferedOutputStream(Files.newOutputStream(path,
				StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW))) {
			FiberSerialization.serialize(config, stream, serializer);
		} catch (FileAlreadyExistsException ignored) {
			// Keep the user's existing configuration.
		} catch (IOException e) {
			LOGGER.log(System.Logger.Level.ERROR, "Error writing the default Psi configuration to " + path, e);
		}
	}

	private static final class Client implements PsiConfig.ClientConfigAccess {

		private static final NumberConfigType<Integer> PSI_BAR_SCALE = INTEGER.withMinimum(1).withMaximum(5);

		private final PropertyMirror<Boolean> useShaders = PropertyMirror.create(BOOLEAN);
		private final PropertyMirror<Boolean> psiBarOnRight = PropertyMirror.create(BOOLEAN);
		private final PropertyMirror<Boolean> contextSensitiveBar = PropertyMirror.create(BOOLEAN);
		private final PropertyMirror<Integer> maxPsiBarScale = PropertyMirror.create(PSI_BAR_SCALE);
		private final PropertyMirror<Boolean> pauseGameInProgrammer = PropertyMirror.create(BOOLEAN);
		private final PropertyMirror<Boolean> letterNumberGridCoordinates = PropertyMirror.create(BOOLEAN);

		private ConfigTree configure(ConfigTreeBuilder builder) {
			builder.fork("client")
					.beginValue("useShaders", BOOLEAN, true)
					.withComment("Controls whether Psi's shaders are used.")
					.finishValue(useShaders::mirror)
					.beginValue("psiBarOnRight", BOOLEAN, true)
					.withComment("Controls whether the Psi Bar is rendered on the right of the screen.")
					.finishValue(psiBarOnRight::mirror)
					.beginValue("contextSensitiveBar", BOOLEAN, true)
					.withComment("Controls whether a full Psi Bar is hidden while holding an item that uses Psi.")
					.finishValue(contextSensitiveBar::mirror)
					.beginValue("maxPsiBarScale", PSI_BAR_SCALE, 3)
					.withComment("The maximum Psi Bar scale. Range: 1 through 5.")
					.finishValue(maxPsiBarScale::mirror)
					.beginValue("pauseGameInProgrammer", BOOLEAN, true)
					.withComment("Controls whether the Spell Programmer pauses a singleplayer game.")
					.finishValue(pauseGameInProgrammer::mirror)
					.beginValue("changeGridCoordinatesToLetterNumber", BOOLEAN, false)
					.withComment("Displays Programmer coordinates as a letter and a number when enabled.")
					.finishValue(letterNumberGridCoordinates::mirror)
					.finishBranch();
			return builder.build();
		}

		@Override
		public boolean useShaders() {
			return useShaders.getValue();
		}

		@Override
		public boolean psiBarOnRight() {
			return psiBarOnRight.getValue();
		}

		@Override
		public boolean contextSensitiveBar() {
			return contextSensitiveBar.getValue();
		}

		@Override
		public int maxPsiBarScale() {
			return maxPsiBarScale.getValue();
		}

		@Override
		public boolean pauseGameInProgrammer() {
			return pauseGameInProgrammer.getValue();
		}

		@Override
		public boolean letterNumberGridCoordinates() {
			return letterNumberGridCoordinates.getValue();
		}
	}

	private static final class Common implements PsiConfig.CommonConfigAccess {

		private static final NumberConfigType<Integer> SPELL_CACHE_SIZE = INTEGER.withMinimum(0).withMaximum(Integer.MAX_VALUE);
		private static final NumberConfigType<Integer> CAD_HARVEST_LEVEL = INTEGER.withMinimum(0).withMaximum(255);

		private final PropertyMirror<Boolean> magiPsiClientSide = PropertyMirror.create(BOOLEAN);
		private final PropertyMirror<Integer> spellCacheSize = PropertyMirror.create(SPELL_CACHE_SIZE);
		private final PropertyMirror<Integer> cadHarvestLevel = PropertyMirror.create(CAD_HARVEST_LEVEL);

		private ConfigTree configure(ConfigTreeBuilder builder) {
			builder.fork("common")
					.beginValue("magiPsiClientSide", BOOLEAN, false)
					.withComment("Disables Magical Psi server features so it can be used as a client-only mod.")
					.finishValue(magiPsiClientSide::mirror)
					.beginValue("spellCacheSize", SPELL_CACHE_SIZE, 200)
					.withComment("How many compiled spells are kept in the cache.")
					.finishValue(spellCacheSize::mirror)
					.beginValue("cadHarvestLevel", CAD_HARVEST_LEVEL, 3)
					.withComment("The CAD harvest level used by block-breaking spells.")
					.finishValue(cadHarvestLevel::mirror)
					.finishBranch();
			return builder.build();
		}

		@Override
		public boolean magiPsiClientSide() {
			return magiPsiClientSide.getValue();
		}

		@Override
		public int spellCacheSize() {
			return spellCacheSize.getValue();
		}

		@Override
		public int cadHarvestLevel() {
			return cadHarvestLevel.getValue();
		}
	}

}

/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import org.apache.commons.lang3.tuple.Pair;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.platform.PsiConfig;

@EventBusSubscriber(modid = PsiAPI.MOD_ID)
public final class NeoForgePsiConfig {

	private static final Client CLIENT;
	private static final ModConfigSpec CLIENT_SPEC;
	private static final Common COMMON;
	private static final ModConfigSpec COMMON_SPEC;

	static {
		Pair<Client, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Client::new);
		CLIENT = pair.getLeft();
		CLIENT_SPEC = pair.getRight();
	}

	static {
		Pair<Common, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Common::new);
		COMMON = pair.getLeft();
		COMMON_SPEC = pair.getRight();
	}

	private NeoForgePsiConfig() {}

	public static void setup(ModContainer container, Dist dist) {
		container.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
		if(dist.isClient()) {
			container.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
		}
	}

	@SubscribeEvent
	public static void onConfigLoading(ModConfigEvent.Loading event) {
		update(event.getConfig());
	}

	@SubscribeEvent
	public static void onConfigReloading(ModConfigEvent.Reloading event) {
		update(event.getConfig());
	}

	private static void update(ModConfig config) {
		if(!PsiAPI.MOD_ID.equals(config.getModId())) {
			return;
		}
		switch(config.getType()) {
		case COMMON -> PsiConfig.setCommon(COMMON);
		case CLIENT -> PsiConfig.setClient(CLIENT);
		default -> {
			// Psi has no server config.
		}
		}
	}

	private static final class Client implements PsiConfig.ClientConfigAccess {

		private final ModConfigSpec.BooleanValue useShaders;
		private final ModConfigSpec.BooleanValue psiBarOnRight;
		private final ModConfigSpec.BooleanValue contextSensitiveBar;
		private final ModConfigSpec.BooleanValue pauseGameInProgrammer;
		private final ModConfigSpec.IntValue maxPsiBarScale;
		private final ModConfigSpec.BooleanValue changeGridCoordinatesToLetterNumber;

		private Client(ModConfigSpec.Builder builder) {
			useShaders = builder.comment("Controls whether Psi's shaders are used. If you're using the GLSL Shaders mod and are having graphical troubles with Psi stuff, you may want to turn this off.")
					.define("client.useShaders", true);
			psiBarOnRight = builder.comment("Controls whether the Psi Bar should be rendered on the right of the screen or not.")
					.define("client.psiBarOnRight", true);
			contextSensitiveBar = builder.comment("Controls whether the Psi Bar should be hidden if it's full and the player is holding an item that uses Psi.")
					.define("client.contextSensitiveBar", true);
			maxPsiBarScale = builder.comment("The maximum scale your Psi bar can be. This prevents it from being too large on a bigger GUI scale. This is maximum amount of \"on screen pixels\" each actual pixel can take.")
					.defineInRange("client.maxPsiBarScale", 3, 1, 5);
			pauseGameInProgrammer = builder.comment("Controls whether the Spell Programmer screen will pause the game in singleplayer.")
					.define("client.pauseGameInProgrammer", true);
			changeGridCoordinatesToLetterNumber = builder.comment("Controls whether or not the Programmer will display the coordinates as a pair of two numbers or as a letter and a number")
					.define("client.changeGridCoordinatesToLetterNumber", false);
		}

		@Override
		public boolean useShaders() {
			return useShaders.get();
		}

		@Override
		public boolean psiBarOnRight() {
			return psiBarOnRight.get();
		}

		@Override
		public boolean contextSensitiveBar() {
			return contextSensitiveBar.get();
		}

		@Override
		public int maxPsiBarScale() {
			return maxPsiBarScale.get();
		}

		@Override
		public boolean pauseGameInProgrammer() {
			return pauseGameInProgrammer.get();
		}

		@Override
		public boolean letterNumberGridCoordinates() {
			return changeGridCoordinatesToLetterNumber.get();
		}
	}

	private static final class Common implements PsiConfig.CommonConfigAccess {

		private final ModConfigSpec.BooleanValue magiPsiClientSide;
		private final ModConfigSpec.IntValue spellCacheSize;
		private final ModConfigSpec.IntValue cadHarvestLevel;

		private Common(ModConfigSpec.Builder builder) {
			magiPsiClientSide = builder.comment("Set this to true to disable all server side features from Magical Psi, to allow you to use it purely as a client side mod")
					.define("common.magiPsiClientSide", false);
			spellCacheSize = builder.comment("How many compiled spells should be kept in a cache. Probably best not to mess with it if you don't know what you're doing.")
					.defineInRange("common.spellCacheSize", 200, 0, Integer.MAX_VALUE);
			cadHarvestLevel = builder.comment("The harvest level of a CAD for the purposes of block breaking spells. Defaults to 3 (diamond level)")
					.defineInRange("common.cadHarvestLevel", 3, 0, 255);
		}

		@Override
		public boolean magiPsiClientSide() {
			return magiPsiClientSide.get();
		}

		@Override
		public int spellCacheSize() {
			return spellCacheSize.get();
		}

		@Override
		public int cadHarvestLevel() {
			return cadHarvestLevel.get();
		}
	}

}

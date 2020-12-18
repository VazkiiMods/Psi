/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraftforge.common.ForgeConfigSpec;

import org.apache.commons.lang3.tuple.Pair;

public class ConfigHandler {

	public static class Client {

		public final ForgeConfigSpec.BooleanValue useShaders;
		public final ForgeConfigSpec.BooleanValue psiBarOnRight;
		public final ForgeConfigSpec.BooleanValue contextSensitiveBar;
		public final ForgeConfigSpec.BooleanValue pauseGameInProgrammer;
		public final ForgeConfigSpec.IntValue maxPsiBarScale;

		public Client(ForgeConfigSpec.Builder builder) {
			useShaders = builder.comment("Controls whether Psi's shaders are used. If you're using the GLSL Shaders mod and are having graphical troubles with Psi stuff, you may want to turn this off.")
					.define("client.useShaders", true);

			psiBarOnRight = builder.comment("Controls whether the Psi Bar should be rendered on the right of the screen or not.")
					.define("client.psiBarOnRight", true);

			contextSensitiveBar = builder.comment("Controls whether the Psi Bar should be hidden if it's full and the player is holding an item that uses Psi.")
					.define("client.contextSensitiveBar", true);

			maxPsiBarScale = builder.comment("The maximum scale your Psi bar can be. This prevents it from being too large on a bigger GUI scale. This is maximum amount of \\\"on screen pixels\\\" each actual pixel can take.")
					.defineInRange("client.maxPsiBarScale", 3, 1, 5);

			pauseGameInProgrammer = builder.comment("Controls whether the Spell Programmer screen will pause the game in singleplayer.")
					.define("client.pauseGameInProgrammer", true);
		}

	}

	public static final Client CLIENT;
	public static final ForgeConfigSpec CLIENT_SPEC;

	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static class Common {

		public final ForgeConfigSpec.BooleanValue magiPsiClientSide;
		public final ForgeConfigSpec.IntValue spellCacheSize;
		public final ForgeConfigSpec.IntValue cadHarvestLevel;

		public Common(ForgeConfigSpec.Builder builder) {

			magiPsiClientSide = builder.comment("Set this to true to disable all server side features from Magical Psi, to allow you to use it purely as a client side mod")
					.define("common.magiPsiClientSide", false);

			spellCacheSize = builder.comment("How many compiled spells should be kept in a cache. Probably best not to mess with it if you don't know what you're doing.")
					.defineInRange("common.spellCacheSize", 200, 0, Integer.MAX_VALUE);

			cadHarvestLevel = builder.comment("The harvest level of a CAD for the purposes of block breaking spells. Defaults to 3 (diamond level)")
					.defineInRange("common.cadHarvestLevel", 3, 0, 255);

		}
	}

	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

}

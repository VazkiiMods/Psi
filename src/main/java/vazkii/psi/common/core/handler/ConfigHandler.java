/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/01/2016, 22:27:53 (GMT)]
 */
package vazkii.psi.common.core.handler;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.common.lib.LibMisc;

import java.io.File;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ConfigHandler {

	public static Configuration config;

	public static boolean usePersistentData = true;
	public static boolean useShaders = true;
	public static boolean psiBarOnRight = true;
	public static boolean contextSensitiveBar = true;
	public static boolean useVanillaParticleLimiter = true;
	public static boolean magipsiClientSide = false;
	public static int maxPsiBarScale = 3;

	public static int spellCacheSize = 100;
	public static int cadHarvestLevel = 2;

	public static void init(File configFile) {
		config = new Configuration(configFile);

		config.load();
		load();
	}

	public static void load() {
		String desc;

		desc = "Controls whether Psi is allowed to save and load Persistent Data outside your instance. This data is stored where .minecraft would be by default and is independent of instance, world or modpack, and allows you to instantly get back to the highest level you were at previously in your last world.";
		usePersistentData = loadPropBool("Use Persistent Data", desc, usePersistentData);

		desc = "Controls whether Psi's shaders are used. If you're using the GLSL Shaders mod and are having graphical troubles with Psi stuff, you may want to turn this off.";
		useShaders = loadPropBool("Use Shaders", desc, useShaders);

		desc = "Controls whether the Psi Bar should be rendered on the right of the screen or not.";
		psiBarOnRight = loadPropBool("Psi Bar on the Right", desc, psiBarOnRight);

		desc = "Controls whether the Psi Bar should be hidden if it's full and the player is holding an item that uses Psi.";
		contextSensitiveBar = loadPropBool("Context Sensitive Bar", desc, contextSensitiveBar);
		
		desc = "Controls whether the \"Particles\" setting in the Vanilla options menu is accounted for when creating particles. Set to false to always have particles even if you change the Vanilla setting.";
		useVanillaParticleLimiter = loadPropBool("Use Vanilla Particle Limiter", desc, useVanillaParticleLimiter);

		desc = "The maximum scale your Psi bar can be. This prevents it from being too large on a bigger GUI scale. This is maximum amount of \"on screen pixels\" each actual pixel can take.";
		maxPsiBarScale = loadPropInt("Maximum Psi Bar Scale", desc, maxPsiBarScale);
		
		desc = "How many compiled spells should be kept in a cache. Probably best not to mess with it if you don't know what you're doing.";
		spellCacheSize = loadPropInt("Spell Cache Size", desc, spellCacheSize);

		desc = "The harvest level of a CAD for the purposes of block breaking spells. 3 is diamond level. Defaults to 2 (iron level)";
		cadHarvestLevel = loadPropInt("CAD Harvest Level", desc, cadHarvestLevel);
		
		if(Loader.isModLoaded("magipsi"))
			magipsiClientSide = loadPropBool("Client Side Magical Psi", "Set this to true to disable all server side features from Magical Psi, to allow you to use it purely as a client side mod", false);

		if(config.hasChanged())
			config.save();
	}

	public static int loadPropInt(String propName, String desc, int default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.setComment(desc);

		//		if(adaptor != null)
		//			adaptor.adaptPropertyInt(prop, prop.getInt(default_));

		return prop.getInt(default_);
	}

	public static double loadPropDouble(String propName, String desc, double default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.setComment(desc);

		//		if(adaptor != null)
		//			adaptor.adaptPropertyDouble(prop, prop.getDouble(default_));

		return prop.getDouble(default_);
	}

	public static boolean loadPropBool(String propName, String desc, boolean default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.setComment(desc);

		//		if(adaptor != null)
		//			adaptor.adaptPropertyBool(prop, prop.getBoolean(default_));

		return prop.getBoolean(default_);
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if(eventArgs.getModID().equals(LibMisc.MOD_ID))
			load();
	}

}

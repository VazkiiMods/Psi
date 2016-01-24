/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [08/01/2016, 22:27:53 (GMT)]
 */
package vazkii.psi.common.core.handler;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.common.lib.LibMisc;

public class ConfigHandler {

	public static Configuration config;
	
	public static boolean useShaders = true;
	public static boolean psiBarOnRight = true;
	public static boolean useVanillaParticleLimiter = true;
	public static int spellCacheSize = 100;
	public static int cadHarvestLevel = 2;

	public static void init(File configFile) {
		config = new Configuration(configFile);

		config.load();
		load();
		
		MinecraftForge.EVENT_BUS.register(new ChangeListener());
	}

	public static void load() {
		String desc;
		
		desc = "Controls whether Psi's shaders are used. If you're using the GLSL Shaders mod and are having graphical troubles with Psi stuff, you may want to turn this off.";
		useShaders = loadPropBool("Use Shaders", desc, useShaders);
		
		desc = "Controls whether the Psi Bar should be rendered on the right of the screen or not.";
		psiBarOnRight = loadPropBool("Psi Bar on the Right", desc, psiBarOnRight);
		
		desc = "Controls whether the \"Particles\" setting in the Vanilla options menu is accounted for when creating particles. Set to false to always have particles even if you change the Vanilla setting.";
		useVanillaParticleLimiter = loadPropBool("Use Vanilla Particle Limiter", desc, useVanillaParticleLimiter);
		
		desc = "How many compiled spells should be kept in a cache. Probably best not to mess with it if you don't know what you're doing.";
		spellCacheSize = loadPropInt("Spell Cache Size", desc, spellCacheSize);
		
		desc = "The harvest level of a CAD for the purposes of block breaking spells. 3 is diamond level. Defaults to 2 (iron level)";
		cadHarvestLevel = loadPropInt("CAD Harvest Level", desc, cadHarvestLevel);
		
		if(config.hasChanged())
			config.save();
	}
	
	public static int loadPropInt(String propName, String desc, int default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.comment = desc;

//		if(adaptor != null)
//			adaptor.adaptPropertyInt(prop, prop.getInt(default_));

		return prop.getInt(default_);
	}

	public static double loadPropDouble(String propName, String desc, double default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.comment = desc;

//		if(adaptor != null)
//			adaptor.adaptPropertyDouble(prop, prop.getDouble(default_));

		return prop.getDouble(default_);
	}

	public static boolean loadPropBool(String propName, String desc, boolean default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.comment = desc;

//		if(adaptor != null)
//			adaptor.adaptPropertyBool(prop, prop.getBoolean(default_));

		return prop.getBoolean(default_);
	}
	
	public static class ChangeListener {

		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
			if(eventArgs.modID.equals(LibMisc.MOD_ID))
				load();
		}

	}
	
}

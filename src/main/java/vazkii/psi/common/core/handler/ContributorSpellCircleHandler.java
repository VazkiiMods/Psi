/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.apache.logging.log4j.Level;

import vazkii.psi.api.cad.CADTakeEvent;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.Psi;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibMisc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ContributorSpellCircleHandler {

	private static volatile Map<String, int[]> colormap = Collections.emptyMap();
	private static boolean startedLoading = false;

	public static void load(Properties props) {
		Map<String, int[]> m = new HashMap<>();
		for (String key : props.stringPropertyNames()) {
			String value = props.getProperty(key).replace("#", "0x");
			try {
				int[] values = Stream.of(value.split(",")).mapToInt(el -> Integer.parseInt(el.substring(2), 16)).toArray();
				m.put(key, values);
			} catch (NumberFormatException e) {
				Psi.logger.log(Level.ERROR, "Contributor " + key + " has an invalid hexcode!");
			}
		}
		colormap = m;
	}

	public static void firstStart() {
		if (!startedLoading) {
			new ThreadContributorListLoader();
			startedLoading = true;
		}
	}

	public static int[] getColors(String name) {
		return colormap.getOrDefault(name, new int[] { ICADColorizer.DEFAULT_SPELL_COLOR });
	}

	public static boolean isContributor(String name) {
		return colormap.containsKey(name);
	}

	@SubscribeEvent
	public static void onCadTake(CADTakeEvent event) {
		if (ContributorSpellCircleHandler.isContributor(event.getPlayer().getName().getString().toLowerCase()) && !((ICAD) event.getCad().getItem()).getComponentInSlot(event.getCad(), EnumCADComponent.DYE).isEmpty()) {
			ItemStack dyeStack = ((ICAD) event.getCad().getItem()).getComponentInSlot(event.getCad(), EnumCADComponent.DYE);
			((ICADColorizer) dyeStack.getItem()).setContributorName(dyeStack, event.getPlayer().getName().getString());
			ItemCAD.setComponent(event.getCad(), dyeStack);
		}
	}

	@SubscribeEvent
	public static void craftColorizer(PlayerEvent.ItemCraftedEvent event) {
		if (ContributorSpellCircleHandler.isContributor(event.getPlayer().getName().getString().toLowerCase()) && event.getCrafting().getItem() instanceof ICADColorizer) {
			((ICADColorizer) event.getCrafting().getItem()).setContributorName(event.getCrafting(), event.getPlayer().getName().getString());
		}
	}

	private static class ThreadContributorListLoader extends Thread {

		public ThreadContributorListLoader() {
			setName("Psi Contributor Spell Circle Loader Thread");
			setDaemon(true);
			setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(Psi.logger));
			start();
		}

		@Override
		public void run() {
			try {
				URL url = new URL("https://raw.githubusercontent.com/Vazkii/Psi/master/contributors.properties");
				Properties props = new Properties();
				try (InputStreamReader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {
					props.load(reader);
					load(props);
				}
			} catch (IOException e) {
				Psi.logger.info("Could not load contributors list. Either you're offline or github is down. Nothing to worry about, carry on~");
			}
		}

	}
}

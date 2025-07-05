/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.Psi;
import vazkii.psi.common.lib.LibMisc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

@EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class ContributorSpellCircleHandler {

	private static volatile Map<String, int[]> colormap = Collections.emptyMap();
	private static boolean startedLoading = false;

	public static void load(Properties props) {
		Map<String, int[]> m = new HashMap<>();
		for(String key : props.stringPropertyNames()) {
			String value = props.getProperty(key).replace("#", "0x");
			try {
				int[] values = Stream.of(value.split(",")).mapToInt(el -> Integer.parseInt(el.substring(2), 16)).toArray();
				m.put(key, values);
			} catch (NumberFormatException | StringIndexOutOfBoundsException e) {
				Psi.logger.error("Contributor {} has an invalid hexcode!", key);
			}
		}
		colormap = m;
	}

	public static void firstStart() {
		if(!startedLoading) {
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
	public static void craftColorizer(PlayerEvent.ItemCraftedEvent event) {
		if(ContributorSpellCircleHandler.isContributor(event.getEntity().getName().getString().toLowerCase(Locale.ROOT)) && event.getCrafting().getItem() instanceof ICADColorizer) {
			((ICADColorizer) event.getCrafting().getItem()).setContributorName(event.getCrafting(), event.getEntity().getName().getString());
		}
	}

	private static class ThreadContributorListLoader extends Thread {

		public ThreadContributorListLoader() {
			setName("Psi Contributor Spell Circle Loader Thread");
			setDaemon(true);
			setUncaughtExceptionHandler((thread, err) -> Psi.logger.error("Caught off-thread exception from {}: ", thread.getName(), err));
			start();
		}

		@Override
		public void run() {
			try {
				URL url = new URI("https://raw.githubusercontent.com/VazkiiMods/Psi/master/contributors.properties").toURL();
				Properties props = new Properties();
				try (InputStreamReader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {
					props.load(reader);
					load(props);
				}
			} catch (IOException e) {
				Psi.logger.info("Could not load contributors list. Either you're offline or github is down. Nothing to worry about, carry on~");
			} catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
	}
}

package vazkii.psi.client.core.handler;

import net.minecraft.util.DefaultUncaughtExceptionHandler;
import org.apache.logging.log4j.Level;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.Psi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

public final class ContributorSpellCircleHandler {

	private static volatile Map<String, int[]> colormap = Collections.emptyMap();
	private static boolean startedLoading = false;


	public static void load(Properties props) {
		Map<String, int[]> m = new HashMap<>();
		for (String key : props.stringPropertyNames()) {
			String value = props.getProperty(key);
			try {
				int[] values = Stream.of(value.split(",")).mapToInt(Integer::parseInt).toArray();
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
		return colormap.getOrDefault(name, new int[ICADColorizer.DEFAULT_SPELL_COLOR]);
	}

	public static boolean isContributor(String name) {
		return colormap.containsKey(name);
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
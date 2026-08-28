/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.platform;

import java.util.Objects;

public final class PsiConfig {

	private static CommonConfigAccess common;
	private static ClientConfigAccess client;

	private PsiConfig() {}

	public static CommonConfigAccess common() {
		if(common == null) {
			throw new IllegalStateException("Psi common configuration has not been initialized");
		}
		return common;
	}

	public static ClientConfigAccess client() {
		if(client == null) {
			throw new IllegalStateException("Psi client configuration has not been initialized");
		}
		return client;
	}

	public static void setCommon(CommonConfigAccess access) {
		common = replace(common, access, "common");
	}

	public static void setClient(ClientConfigAccess access) {
		client = replace(client, access, "client");
	}

	private static <T> T replace(T current, T replacement, String name) {
		Objects.requireNonNull(replacement);
		if(current != null && current != replacement) {
			throw new IllegalStateException("Psi " + name + " configuration was initialized more than once");
		}
		return replacement;
	}

	public interface CommonConfigAccess {

		boolean magiPsiClientSide();

		int spellCacheSize();

		int cadHarvestLevel();

	}

	public interface ClientConfigAccess {

		boolean useShaders();

		boolean psiBarOnRight();

		boolean contextSensitiveBar();

		int maxPsiBarScale();

		boolean pauseGameInProgrammer();

		boolean letterNumberGridCoordinates();
	}

}

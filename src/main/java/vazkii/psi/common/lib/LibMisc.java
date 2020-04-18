/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.lib;

public final class LibMisc {

	// Mod Constants
	public static final String MOD_ID = "psi";
	public static final String MOD_NAME = "Psi";
	public static final String BUILD = "GRADLE:BUILD";
	public static final String VERSION = "GRADLE:VERSION-" + BUILD;
	public static final String DEPENDENCIES = "required-after:autoreglib";
	public static final String PREFIX_MOD = MOD_ID + ":";
	public static final boolean BETA_TESTING = true;

	// Network Contants
	public static final String NETWORK_CHANNEL = MOD_ID;

	// Proxy Constants
	public static final String PROXY_COMMON = "vazkii.psi.common.core.proxy.CommonProxy";
	public static final String PROXY_CLIENT = "vazkii.psi.client.core.proxy.ClientProxy";
	public static final String GUI_FACTORY = "vazkii.psi.client.core.proxy.GuiFactory";

}

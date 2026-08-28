/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.platform;

import net.minecraft.world.item.CreativeModeTab;

public final class PsiCreativeTabs {
	private static final PsiCreativeTabService SERVICE = PsiServices.load(PsiCreativeTabService.class);

	private PsiCreativeTabs() {}

	public static CreativeModeTab.Builder builder() {
		return SERVICE.builder();
	}
}

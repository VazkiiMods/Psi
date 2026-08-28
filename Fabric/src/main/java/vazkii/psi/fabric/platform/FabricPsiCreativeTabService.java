/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.platform;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab;

import vazkii.psi.common.platform.PsiCreativeTabService;

public final class FabricPsiCreativeTabService implements PsiCreativeTabService {
	@Override
	public CreativeModeTab.Builder builder() {
		return FabricItemGroup.builder();
	}
}

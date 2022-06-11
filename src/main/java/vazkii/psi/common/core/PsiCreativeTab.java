/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nonnull;

public class PsiCreativeTab extends CreativeModeTab {

	public static final PsiCreativeTab INSTANCE = new PsiCreativeTab();

	public PsiCreativeTab() {
		super(LibMisc.MOD_ID);
		hideTitle();
		setBackgroundImage(new ResourceLocation(LibMisc.MOD_ID, LibResources.GUI_CREATIVE));
	}

	@Nonnull
	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ModItems.cadAssemblyIron);
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}

}

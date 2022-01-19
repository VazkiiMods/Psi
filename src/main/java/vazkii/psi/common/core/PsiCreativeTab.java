/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nonnull;

public class PsiCreativeTab extends ItemGroup {

	public static final PsiCreativeTab INSTANCE = new PsiCreativeTab();
	private NonNullList<ItemStack> list;

	public PsiCreativeTab() {
		super(LibMisc.MOD_ID);
		hideTitle();
		setBackgroundSuffix(LibResources.GUI_CREATIVE);
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

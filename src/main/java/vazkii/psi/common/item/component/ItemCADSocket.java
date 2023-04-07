/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.component;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.cad.EnumCADComponent;

public class ItemCADSocket extends ItemCADComponent {

	public static final int MAX_SOCKETS = 12;

	public ItemCADSocket(Item.Properties properties) {
		super(properties);
	}

	@Override
	public EnumCADComponent getComponentType(ItemStack stack) {
		return EnumCADComponent.SOCKET;
	}

}

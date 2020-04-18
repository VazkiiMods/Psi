/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * This interface represents a CAD Assembler tile. You probably shouldn't implement this.
 */
public interface ITileCADAssembler {
	int getComponentSlot(EnumCADComponent componentType);

	ItemStack getCachedCAD(PlayerEntity player);

	void clearCachedCAD();

	ItemStack getStackForComponent(EnumCADComponent componentType);

	boolean setStackForComponent(EnumCADComponent componentType, ItemStack component);

	ItemStack getSocketableStack();

	ISocketableCapability getSocketable();

	boolean setSocketableStack(ItemStack stack);

	void onCraftCAD(ItemStack cad);

	boolean isBulletSlotEnabled(int slot);
}

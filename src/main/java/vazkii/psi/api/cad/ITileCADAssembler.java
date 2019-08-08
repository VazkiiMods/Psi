/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [02/01/2019, 23:52:03 (GMT)]
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

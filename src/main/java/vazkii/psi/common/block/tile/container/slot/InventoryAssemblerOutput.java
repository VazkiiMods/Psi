/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.common.block.tile.TileCADAssembler;

/**
 * @author WireSegal
 *         Created at 4:44 PM on 1/2/19.
 */
public class InventoryAssemblerOutput implements Container {

	private final Player player;
	private final TileCADAssembler assembler;

	public InventoryAssemblerOutput(Player player, TileCADAssembler assembler) {
		this.player = player;
		this.assembler = assembler;
	}

	private ItemStack getStack() {
		return assembler.getCachedCAD(player);
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return getStack().isEmpty();
	}

	@NotNull
	@Override
	public ItemStack getItem(int index) {
		return getStack();
	}

	@NotNull
	@Override
	public ItemStack removeItem(int index, int count) {
		return getStack();
	}

	@NotNull
	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return getStack();
	}

	@Override
	public void setItem(int index, @NotNull ItemStack stack) {
		// NO-OP
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public void setChanged() {
		// NO-OP
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return true;
	}

	@Override
	public void startOpen(@NotNull Player player) {
		// NO-OP
	}

	@Override
	public void stopOpen(@NotNull Player player) {
		// NO-OP
	}

	@Override
	public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
		return false;
	}

	@Override
	public void clearContent() {
		// NO-OP
	}
}

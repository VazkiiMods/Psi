/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [02/01/2019, 21:44:13 (GMT)]
 */
package vazkii.psi.common.block.tile.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import vazkii.psi.common.block.tile.TileCADAssembler;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 4:44 PM on 1/2/19.
 */
public class InventoryAssemblerOutput implements IInventory {

	private final PlayerEntity player;
	private final TileCADAssembler assembler;

	public InventoryAssemblerOutput(PlayerEntity player, TileCADAssembler assembler) {
		this.player = player;
		this.assembler = assembler;
	}

	private ItemStack getStack() {
		return assembler.getCachedCAD(player);
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return getStack().isEmpty();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int index) {
		return getStack();
	}

	@Nonnull
	@Override
	public String getName() {
		return "Result";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		return hasCustomName() ? new StringTextComponent(getName()) : new TranslationTextComponent(getName());
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int index, int count) {
		return getStack();
	}

	@Nonnull
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return getStack();
	}

	@Override
	public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
		// NO-OP
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void markDirty() {
		// NO-OP
	}

	@Override
	public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
		return true;
	}

	@Override
	public void openInventory(@Nonnull PlayerEntity player) {
		// NO-OP
	}

	@Override
	public void closeInventory(@Nonnull PlayerEntity player) {
		// NO-OP
	}

	@Override
	public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// NO-OP
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		// NO-OP
	}
}

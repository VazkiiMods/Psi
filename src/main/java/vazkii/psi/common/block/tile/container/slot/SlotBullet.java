/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [10/01/2016, 17:30:17 (GMT)]
 */
package vazkii.psi.common.block.tile.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.component.ItemCADSocket;

public class SlotBullet extends Slot {

	TileCADAssembler assembler;
	int socketSlot;

	public SlotBullet(TileCADAssembler inventoryIn, int index, int xPosition, int yPosition, int socketSlot) {
		super(inventoryIn, index, xPosition, yPosition);
		assembler = inventoryIn;
		this.socketSlot = socketSlot;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if(stack.getItem() instanceof ISpellContainer) {
			ISpellContainer container = (ISpellContainer) stack.getItem();
			if(container.containsSpell(stack))
				return assembler.isBulletSlotEnabled(socketSlot);
		}
		
		return false;
	}

}

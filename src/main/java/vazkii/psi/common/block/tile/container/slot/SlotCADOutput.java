/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [10/01/2016, 17:46:41 (GMT)]
 */
package vazkii.psi.common.block.tile.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import vazkii.psi.api.cad.CADTakeEvent;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.core.handler.PsiSoundHandler;

import javax.annotation.Nonnull;

public class SlotCADOutput extends Slot {

	private final TileCADAssembler assembler;

	public SlotCADOutput(IInventory outputInventory, TileCADAssembler assembler, int xPosition, int yPosition) {
		super(outputInventory, 0, xPosition, yPosition);
		this.assembler = assembler;
	}
	
	@Nonnull
	@Override
	public ItemStack onTake(EntityPlayer playerIn, @Nonnull ItemStack stack) {
		super.onTake(playerIn, stack);
		assembler.onCraftCAD(stack);
		return stack;
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		CADTakeEvent event = new CADTakeEvent(getStack(), assembler, playerIn);
		float sound = event.getSound();
		if (MinecraftForge.EVENT_BUS.post(event)) {
			BlockPos assemblerPos = this.assembler.getPos();
			String cancelMessage = event.getCancellationMessage();
			if (!playerIn.world.isRemote) {
				if (cancelMessage != null && !cancelMessage.isEmpty())
					playerIn.sendMessage(new TextComponentTranslation(cancelMessage).setStyle(new Style().setColor(TextFormatting.RED)));
				playerIn.world.playSound(null, assemblerPos.getX(), assemblerPos.getY(), assemblerPos.getZ(), PsiSoundHandler.compileError, SoundCategory.BLOCKS, sound, 1F);
			}
			return false;
		}
		return super.canTakeStack(playerIn);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
}

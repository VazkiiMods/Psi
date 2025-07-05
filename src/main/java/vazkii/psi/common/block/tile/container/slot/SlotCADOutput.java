/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile.container.slot;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.cad.CADTakeEvent;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.core.handler.PsiSoundHandler;

public class SlotCADOutput extends Slot {

	private final TileCADAssembler assembler;

	public SlotCADOutput(Container outputInventory, TileCADAssembler assembler, int xPosition, int yPosition) {
		super(outputInventory, 0, xPosition, yPosition);
		this.assembler = assembler;
	}

	@NotNull
	@Override
	public void onTake(Player playerIn, @NotNull ItemStack stack) {
		super.onTake(playerIn, stack);
		assembler.onCraftCAD(stack);
		//return stack;
	}

	@Override
	public boolean mayPickup(Player playerIn) {
		CADTakeEvent event = new CADTakeEvent(getItem(), assembler, playerIn);
		float sound = event.getSound();
		if(NeoForge.EVENT_BUS.post(event).isCanceled()) {
			BlockPos assemblerPos = this.assembler.getBlockPos();
			String cancelMessage = event.getCancellationMessage();
			if(!playerIn.level().isClientSide) {
				if(cancelMessage != null && !cancelMessage.isEmpty()) {
					playerIn.sendSystemMessage(Component.translatable(cancelMessage).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
				}
				playerIn.level().playSound(null, assemblerPos.getX(), assemblerPos.getY(), assemblerPos.getZ(), PsiSoundHandler.compileError, SoundSource.BLOCKS, sound, 1F);
			}
			return false;
		}
		return super.mayPickup(playerIn);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
}

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

public class SlotCADOutput extends SlotExtractOnly {

	TileCADAssembler assmbler;

	public SlotCADOutput(TileCADAssembler inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		assmbler = inventoryIn;
	}
	
	@Override
	public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
		super.onTake(playerIn, stack);
		assmbler.onCraftCAD(stack);
		return stack;
	}

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        CADTakeEvent event = new CADTakeEvent(this, playerIn, 0.5f);
        float sound = event.getSound();
        if (MinecraftForge.EVENT_BUS.post(event)) {
            BlockPos assemblerPos = this.assmbler.getPos();
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
}

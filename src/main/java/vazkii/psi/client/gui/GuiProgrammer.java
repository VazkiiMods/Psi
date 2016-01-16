/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 16:22:49 (GMT)]
 */
package vazkii.psi.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellGrid;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.spell.base.ModSpellPieces;

public class GuiProgrammer extends GuiScreen {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_PROGRAMMER);

	TileProgrammer programmer;
	int xSize, ySize;
	
	public GuiProgrammer(TileProgrammer programmer) {
		this.programmer = programmer;
	}
	
	@Override
	public void initGui() {
		xSize = 174;
		ySize = 174;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		GlStateManager.color(1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 7, y + 7, 0);
        Spell spell = new Spell();
        SpellGrid grid = spell.grid;
        grid.gridData[2][3] = ModSpellPieces.trickDebug.get(spell);
        grid.gridData[3][3] = ModSpellPieces.selectorCaster.get(spell);
        spell.draw();
        GlStateManager.popMatrix();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
}

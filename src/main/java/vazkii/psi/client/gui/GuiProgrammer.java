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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellGrid;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.core.helper.RenderHelper;
import vazkii.psi.client.gui.button.GuiButtonSpellPiece;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.lib.LibResources;

public class GuiProgrammer extends GuiScreen {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_PROGRAMMER);

	TileProgrammer programmer;
	Spell spell;
	
	public List<String> tooltip = new ArrayList();
	
	int xSize, ySize, padLeft, padTop, left, top, gridLeft, gridTop;
	int cursorX, cursorY;
	int selectedX, selectedY;
    boolean panelEnabled;
    int panelX, panelY, panelWidth, panelHeight;
    List<GuiButton> panelButtons = new ArrayList();
    GuiTextField searchField;
	
	public GuiProgrammer(TileProgrammer programmer) {
		this.programmer = programmer;
		spell = new Spell();
	}
	
	@Override
	public void initGui() {
		xSize = 174;
		ySize = 174;
		padLeft = 7;
		padTop = 7;
        left = (width - xSize) / 2;
        top = (height - ySize) / 2;
        gridLeft = left + padLeft;
        gridTop = top + padTop;
        panelWidth = 100;
        panelHeight = 125;
		cursorX = cursorY = selectedX = selectedY = -1;
		searchField = new GuiTextField(0, fontRendererObj, 0, 0, 70, 10);
		searchField.setCanLoseFocus(false);
		searchField.setFocused(true);
		searchField.setEnableBackgroundDrawing(false);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		tooltip.clear();
		drawDefaultBackground();
		
		GlStateManager.color(1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);

        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
        
        cursorX = (mouseX - gridLeft) / 18;
        cursorY = (mouseY - gridTop) / 18;
        if(panelEnabled || cursorX > 8 || cursorY > 8 || cursorX < 0 || cursorY < 0) {
        	cursorX = -1;
        	cursorY = -1;
        }
        
        GlStateManager.pushMatrix();
        GlStateManager.translate(gridLeft, gridTop, 0);
        spell.draw();
        GlStateManager.popMatrix();
        
		mc.getTextureManager().bindTexture(texture);

        if(selectedX > -1 && selectedY > -1)
            drawTexturedModalRect(gridLeft + selectedX * 18, gridTop + selectedY * 18, 32, ySize, 16, 16);
        if(cursorX > -1 && cursorY > -1) {
        	SpellPiece pieceAt = spell.grid.gridData[cursorX][cursorY];
        	if(pieceAt != null)
        		pieceAt.getTooltip(tooltip);
        	
        	if(cursorX == selectedX && cursorY == selectedY)
        		drawTexturedModalRect(gridLeft + cursorX * 18, gridTop + cursorY * 18, 16, ySize, 8, 16);
        	else drawTexturedModalRect(gridLeft + cursorX * 18, gridTop + cursorY * 18, 16, ySize, 16, 16);
        }
        
        if(panelEnabled) {
        	drawRect(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0x88000000);
    		GlStateManager.color(1F, 1F, 1F);
    		drawTexturedModalRect(searchField.xPosition - 14, searchField.yPosition - 2, 0, ySize + 16, 12, 12);
        	searchField.drawTextBox();
        }
        
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if(!tooltip.isEmpty())
			RenderHelper.renderTooltip(mouseX, mouseY, tooltip);
		}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		if(panelEnabled) {
			searchField.mouseClicked(mouseX, mouseY, mouseButton);

			if(mouseX < panelX || mouseY < panelY || mouseX > panelX + panelWidth || mouseY > panelY + panelHeight)
				closePanel();
		} else {
			SpellGrid grid = spell.grid;
	        if(cursorX > -1 && cursorY > -1) {
	        	selectedX = cursorX;
	        	selectedY = cursorY;
	        	if(mouseButton == 1) {
	        		if(isShiftKeyDown())
	        			spell.grid.gridData[selectedX][selectedY] = null;
	        		else openPanel();
	        	}
	        }
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		super.keyTyped(par1, par2);

		if(panelEnabled)
			searchField.textboxKeyTyped(par1, par2);
	}

	private void openPanel() {
		closePanel();
		panelEnabled = true;
		
		panelX = gridLeft + (selectedX + 1) * 18;
		panelY = gridTop;
		
		int i = 0;
		for(String key : PsiAPI.spellPieceRegistry.getKeys()) {
			Class<? extends SpellPiece> clazz = PsiAPI.spellPieceRegistry.getObject(key);
			SpellPiece p = SpellPiece.create(clazz, spell);
			List<SpellPiece> pieces = new ArrayList();
			p.getShownPieces(pieces);
			
			for(SpellPiece piece : pieces) {
				panelButtons.add(new GuiButtonSpellPiece(this, p, panelX + 4 + (i % 5) * 18, panelY + 20 + (i / 5) * 18));
				i++;
			}
		}
		
		searchField.xPosition = panelX + 18;
		searchField.yPosition = panelY + 4;

		buttonList.addAll(panelButtons);
	}
	
	private void closePanel() {
		panelEnabled = false;
		buttonList.removeAll(panelButtons);
		panelButtons.clear();
		searchField.setText("");
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		
		if(button.id == 9000)
			closePanel();
		
		if(button instanceof GuiButtonSpellPiece) {
			SpellPiece piece = ((GuiButtonSpellPiece) button).piece.copy();
			spell.grid.gridData[selectedX][selectedY] = piece;
			closePanel();
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}

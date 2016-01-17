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

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellGrid;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.core.helper.RenderHelper;
import vazkii.psi.client.gui.button.GuiButtonSideConfig;
import vazkii.psi.client.gui.button.GuiButtonSpellPiece;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.NetworkHandler;
import vazkii.psi.common.network.message.MessageSpellModified;
import vazkii.psi.common.spell.SpellCompiler;

public class GuiProgrammer extends GuiScreen {

	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_PROGRAMMER);

	public TileProgrammer programmer;
	public List<String> tooltip = new ArrayList();

	SpellCompiler compiler;
	
	int xSize, ySize, padLeft, padTop, left, top, gridLeft, gridTop;
	int cursorX, cursorY;
	int selectedX, selectedY;
	boolean panelEnabled, configEnabled;
	int panelX, panelY, panelWidth, panelHeight;
	List<GuiButton> panelButtons = new ArrayList();
	List<GuiButton> configButtons = new ArrayList();
	GuiTextField searchField;
	GuiTextField spellNameField;

	public GuiProgrammer(TileProgrammer programmer) {
		this.programmer = programmer;
		compiler = new SpellCompiler(programmer.spell);
	}

	@Override
	public void initGui() {
		xSize = 174;
		ySize = 184;
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

		spellNameField = new GuiTextField(0, fontRendererObj, left + xSize - 130, top + ySize - 14, 120, 10);
		spellNameField.setCanLoseFocus(false);
		spellNameField.setFocused(true);
		spellNameField.setEnableBackgroundDrawing(false);
		spellNameField.setMaxStringLength(20);

		if(programmer.spell == null)
			programmer.spell = new Spell();
		
		spellNameField.setText(programmer.spell.name);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int color = 4210752;

		GlStateManager.pushMatrix();
		tooltip.clear();
		drawDefaultBackground();

		GlStateManager.color(1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);

		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		drawTexturedModalRect(left - 48, top + 5, xSize, 0, 48, 30);
		
		int statusX = left - 16;
		int statusY = top + 13;
		drawTexturedModalRect(statusX, statusY, compiler.isErrored() ? 12 : 0, ySize + 28, 12, 12);
		if(mouseX > statusX && mouseY > statusY && mouseX < statusX + 12 && mouseY < statusY + 12) {
			if(compiler.isErrored()) {
				tooltip.add(EnumChatFormatting.RED + StatCollector.translateToLocal("psimisc.errored"));
				tooltip.add(EnumChatFormatting.GRAY + compiler.getError());
				Pair<Integer, Integer> errorPos = compiler.getErrorLocation();
				if(errorPos != null && errorPos.getLeft() != -1 && errorPos.getRight() != -1)
					tooltip.add(EnumChatFormatting.GRAY + "[" + (errorPos.getLeft() + 1) + ", " + (errorPos.getRight() + 1) + "]");
			} else tooltip.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("psimisc.compiled"));
		}
		
		ItemStack cad = PsiAPI.getPlayerCAD(mc.thePlayer);
		if(cad != null) {
			int cadX = left - 42;
			int cadY = top + 12;
			mc.getRenderItem().renderItemAndEffectIntoGUI(cad, cadX, cadY);
			if(mouseX > cadX && mouseY > cadY && mouseX < cadX + 16 && mouseY < cadY + 16) {
				List<String> itemTooltip = cad.getTooltip(mc.thePlayer, false);
		        for (int i = 0; i < itemTooltip.size(); ++i)
		            if (i == 0)
		            	itemTooltip.set(i, cad.getRarity().rarityColor + itemTooltip.get(i));
		            else itemTooltip.set(i, EnumChatFormatting.GRAY + itemTooltip.get(i));
				
				tooltip.addAll(itemTooltip);
			}
		}
		mc.getTextureManager().bindTexture(texture);
		
		SpellMetadata meta = null;
		if(!compiler.isErrored()) {
			int i = 0;
			meta = compiler.getCompiledSpell().metadata;
			
			for(EnumSpellStat stat : meta.stats.keySet()) {
				int statX = left + xSize + 3;
				int statY = top + 20 + i * 20;
				int val = meta.stats.get(stat);
				
				EnumCADStat cadStat = stat.getTarget();
				int cadVal = 0;
				if(cadStat == null)
					cadVal = -1;
				else if(cad != null) {
					ICAD cadItem = (ICAD) cad.getItem();
					cadVal = cadItem.getStatValue(cad, cadStat);
				}
				String s = "" + val;
				if(cadVal != -1)
					s += "/" + cadVal;
				
				GlStateManager.color(1F, 1F, 1F);
				drawTexturedModalRect(statX, statY, (stat.ordinal() + 1) * 12, ySize + 16, 12, 12);
				mc.fontRendererObj.drawString(s, statX + 16, statY + 2, (cadStat != null && cadVal < val) ? 0xFF6666 : 0xFFFFFF);
				mc.getTextureManager().bindTexture(texture);
				
				if(mouseX > statX && mouseY > statY && mouseX < statX + 12 && mouseY < statY + 12) {
					tooltip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal(stat.getName()));
					tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal(stat.getDesc()));
				}
				
				i++;
			}
		}
		GlStateManager.color(1F, 1F, 1F);
		
		if(configEnabled) {
			drawTexturedModalRect(left - 81, top + 55, xSize, 30, 81, 115);

			SpellPiece piece = programmer.spell.grid.gridData[selectedX][selectedY];
			int i = 0;

			for(String s : piece.params.keySet()) {
				int x = left - 75;
				int y = top + 70 + i * 26;

				GlStateManager.color(1F, 1F, 1F);
				mc.getTextureManager().bindTexture(texture);
				drawTexturedModalRect(x + 50, y - 8, xSize, 145, 24, 24);

				String localized = StatCollector.translateToLocal(s);
				mc.fontRendererObj.drawString(localized, x, y, color);
				i++;
			}
		}

		cursorX = (mouseX - gridLeft) / 18;
		cursorY = (mouseY - gridTop) / 18;
		if(panelEnabled || cursorX > 8 || cursorY > 8 || cursorX < 0 || cursorY < 0 || mouseX < gridLeft || mouseY < gridTop) {
			cursorX = -1;
			cursorY = -1;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(gridLeft, gridTop, 0);
		programmer.spell.draw();
		
		if(compiler.isErrored()) {
			Pair<Integer, Integer> errorPos = compiler.getErrorLocation();
			if(errorPos != null && errorPos.getLeft() != -1 && errorPos.getRight() != -1) {
				int errorX = errorPos.getLeft() * 18 + 12;
				int errorY = errorPos.getRight() * 18 + 8;
				mc.fontRendererObj.drawStringWithShadow("!!", errorX, errorY, 0xFF0000);
			}
		}
		
		GlStateManager.popMatrix();
		GlStateManager.color(1F, 1F, 1F);
		GlStateManager.translate(0, 0, 1);

		mc.getTextureManager().bindTexture(texture);

		if(selectedX != -1 && selectedY != -1)
			drawTexturedModalRect(gridLeft + selectedX * 18, gridTop + selectedY * 18, 32, ySize, 16, 16);
		if(cursorX != -1 && cursorY != -1) {
			SpellPiece pieceAt = programmer.spell.grid.gridData[cursorX][cursorY];
			if(pieceAt != null)
				pieceAt.getTooltip(tooltip);

			if(cursorX == selectedX && cursorY == selectedY)
				drawTexturedModalRect(gridLeft + cursorX * 18, gridTop + cursorY * 18, 16, ySize, 8, 16);
			else drawTexturedModalRect(gridLeft + cursorX * 18, gridTop + cursorY * 18, 16, ySize, 16, 16);
		}

		mc.fontRendererObj.drawString(StatCollector.translateToLocal("psimisc.name"), left + padLeft, spellNameField.yPosition + 1, color);
		spellNameField.drawTextBox();
		if(panelEnabled) {
			tooltip.clear();
			mc.getTextureManager().bindTexture(texture);

			drawRect(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0x88000000);
			GlStateManager.color(1F, 1F, 1F);
			drawTexturedModalRect(searchField.xPosition - 14, searchField.yPosition - 2, 0, ySize + 16, 12, 12);
			searchField.drawTextBox();
		}


		super.drawScreen(mouseX, mouseY, partialTicks);

		if(!tooltip.isEmpty())
			RenderHelper.renderTooltip(mouseX, mouseY, tooltip);

		GlStateManager.popMatrix();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if(panelEnabled) {
			searchField.mouseClicked(mouseX, mouseY, mouseButton);

			if(mouseX < panelX || mouseY < panelY || mouseX > panelX + panelWidth || mouseY > panelY + panelHeight)
				closePanel();
		} else {
			spellNameField.mouseClicked(mouseX, mouseY, mouseButton);

			SpellGrid grid = programmer.spell.grid;
			if(cursorX != -1 && cursorY != -1) {
				selectedX = cursorX;
				selectedY = cursorY;
				if(mouseButton == 1) {
					if(isShiftKeyDown()) {
						programmer.spell.grid.gridData[selectedX][selectedY] = null;
						onSpellChanged(false);
						return;
					}
					openPanel();
				}
				onSelectedChanged();
			}
		}
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		super.keyTyped(par1, par2);

		if(panelEnabled) {
			searchField.textboxKeyTyped(par1, par2);
			updatePanelButtons();
		} else {
			boolean pieceHandled = false;
			
			if(selectedX != -1 && selectedY != -1) {
				SpellPiece piece = programmer.spell.grid.gridData[selectedX][selectedY];
				if(piece != null && piece.interceptKeystrokes()) {
					if(piece.onKeyPressed(par1, par2))
						onSpellChanged(false);
					pieceHandled = true;
				}
			}
			
			if(!pieceHandled) {
				String lastName = spellNameField.getText();
				spellNameField.textboxKeyTyped(par1, par2);
				String currName = spellNameField.getText();
				if(!lastName.equals(currName)) {
					programmer.spell.name = currName;
					onSpellChanged(true);
				}
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);

		if(button.id == 9000)
			closePanel();

		if(button instanceof GuiButtonSpellPiece) {
			SpellPiece piece = ((GuiButtonSpellPiece) button).piece.copy();
			programmer.spell.grid.gridData[selectedX][selectedY] = piece;
			piece.isInGrid = true;
			piece.x = selectedX;
			piece.y = selectedY;
			onSpellChanged(false);
			closePanel();
		} else if(button instanceof GuiButtonSideConfig) {
			((GuiButtonSideConfig) button).onClick();
			onSpellChanged(false);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private void openPanel() {
		closePanel();
		panelEnabled = true;

		panelX = gridLeft + (selectedX + 1) * 18;
		panelY = gridTop;

		searchField.xPosition = panelX + 18;
		searchField.yPosition = panelY + 4;
		spellNameField.setCanLoseFocus(true);
		spellNameField.setFocused(false);

		updatePanelButtons();
	}

	private void updatePanelButtons() {
		int i = 0;

		buttonList.removeAll(panelButtons);
		panelButtons.clear();

		for(String key : PsiAPI.spellPieceRegistry.getKeys()) {
			Class<? extends SpellPiece> clazz = PsiAPI.spellPieceRegistry.getObject(key);
			SpellPiece p = SpellPiece.create(clazz, programmer.spell);
			List<SpellPiece> pieces = new ArrayList();
			p.getShownPieces(pieces);

			for(SpellPiece piece : pieces)
				if(StatCollector.translateToLocal(piece.getUnlocalizedName()).toLowerCase().contains(searchField.getText().toLowerCase())) {
					panelButtons.add(new GuiButtonSpellPiece(this, piece, panelX + 4 + (i % 5) * 18, panelY + 20 + (i / 5) * 18));
					i++;
				}
		}

		buttonList.addAll(panelButtons);
	}

	private void closePanel() {
		panelEnabled = false;
		buttonList.removeAll(panelButtons);
		panelButtons.clear();
		spellNameField.setCanLoseFocus(false);
		spellNameField.setFocused(true);
	}

	public void onSpellChanged(boolean nameOnly) {
		NetworkHandler.INSTANCE.sendToServer(new MessageSpellModified(programmer.getPos(), programmer.spell));
		programmer.onSpellChanged();
		onSelectedChanged();
		
		if(!nameOnly)
			compiler = new SpellCompiler(programmer.spell);
	}

	public void onSelectedChanged() {
		buttonList.removeAll(configButtons);
		configButtons.clear();

		if(selectedX != -1 && selectedY != -1) {
			SpellPiece piece = programmer.spell.grid.gridData[selectedX][selectedY];
			if(piece != null && piece.hasConfig()) {
				int i = 0;
				for(String paramName : piece.params.keySet()) {
					SpellParam param = piece.params.get(paramName);
					int x = left - 17;
					int y = top + 70 + i * 26;
					for(SpellParam.Side side : SpellParam.Side.class.getEnumConstants()) {
						if(!side.isEnabled() && !param.canDisable)
							continue;

						int xp = x + side.offx * 8;
						int yp = y + side.offy * 8;
						configButtons.add(new GuiButtonSideConfig(this, selectedX, selectedY, paramName, side, xp, yp));
					}

					i++;
				}

				buttonList.addAll(configButtons);
				configEnabled = true;
				return;
			}
		}

		configEnabled = false;
	}

}

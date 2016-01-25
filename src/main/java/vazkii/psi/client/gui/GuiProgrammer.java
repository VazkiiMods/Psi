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
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

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
import vazkii.psi.api.spell.PieceGroup;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellGrid;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.core.helper.RenderHelper;
import vazkii.psi.client.gui.button.GuiButtonPage;
import vazkii.psi.client.gui.button.GuiButtonSideConfig;
import vazkii.psi.client.gui.button.GuiButtonSpellPiece;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.NetworkHandler;
import vazkii.psi.common.network.message.MessageSpellModified;
import vazkii.psi.common.spell.SpellCompiler;

public class GuiProgrammer extends GuiScreen {

	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_PROGRAMMER);
	private static final int PIECES_PER_PAGE = 25;
	
	public TileProgrammer programmer;
	public List<String> tooltip = new ArrayList();

	SpellCompiler compiler;
	
	int xSize, ySize, padLeft, padTop, left, top, gridLeft, gridTop;
	int cursorX, cursorY;
	int selectedX, selectedY;
	boolean panelEnabled, configEnabled;
	int panelX, panelY, panelWidth, panelHeight;
	int page = 0;
	boolean scheduleButtonUpdate = false;
	List<SpellPiece> visiblePieces = new ArrayList();
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
		int color = 0xFFFFFF;
		
		if(scheduleButtonUpdate) {
			updatePanelButtons();
			scheduleButtonUpdate = false;
		}

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
				if(stat == EnumSpellStat.COST)
					s += " (" + ItemCAD.getRealCost(cad, val) + ")";
				
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
			String configStr = StatCollector.translateToLocal("psimisc.config");
			mc.fontRendererObj.drawString(configStr, left - mc.fontRendererObj.getStringWidth(configStr) - 2, top + 45, 0xFFFFFF);
			
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

		if(LibMisc.BETA_TESTING) {
			String betaTest = StatCollector.translateToLocal("psimisc.wip");
			mc.fontRendererObj.drawStringWithShadow(betaTest, left + xSize / 2 - mc.fontRendererObj.getStringWidth(betaTest) / 2, top - 12, 0xFFFFFF);
		}
		mc.fontRendererObj.drawStringWithShadow(StatCollector.translateToLocal("psimisc.name"), left + padLeft, spellNameField.yPosition + 1, color);
		spellNameField.drawTextBox();
		if(panelEnabled) {
			tooltip.clear();
			mc.getTextureManager().bindTexture(texture);

			drawRect(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0x88000000);
			GlStateManager.color(1F, 1F, 1F);
			drawTexturedModalRect(searchField.xPosition - 14, searchField.yPosition - 2, 0, ySize + 16, 12, 12);
			searchField.drawTextBox();
			
			String s = (page + 1) + "/" + getPageCount();
			fontRendererObj.drawStringWithShadow(s, panelX + panelWidth / 2 - fontRendererObj.getStringWidth(s) / 2, panelY + panelHeight - 12, 0xFFFFFF);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);

		if(!tooltip.isEmpty())
			RenderHelper.renderTooltip(mouseX, mouseY, tooltip);

		GlStateManager.popMatrix();
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();

		int w = Mouse.getEventDWheel();
		int max = getPageCount();
		if(w != 0) {
			int next = page - w / Math.abs(w);
			
			if(next >= 0 && next < max) {
				page = next;
				updatePanelButtons();
			}
		}
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
			String last = searchField.getText();
			searchField.textboxKeyTyped(par1, par2);
			if(!searchField.getText().equals(last)) {
				page = 0;
				updatePanelButtons();
			}
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
			
			if(par2 == Keyboard.KEY_DELETE) {
				if(isShiftKeyDown() && isCtrlKeyDown()) {
					if(!programmer.spell.grid.isEmpty()) {
						programmer.spell = new Spell();
						spellNameField.setText("");
						onSpellChanged(false);
						return;
					}
				} if(selectedX != -1 && selectedY != -1 && programmer.spell.grid.gridData[selectedX][selectedY] != null) {
					programmer.spell.grid.gridData[selectedX][selectedY] = null;
					onSpellChanged(false);
					return;
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
		} else if(button instanceof GuiButtonPage) {
			int max = getPageCount();
			int next = page + (((GuiButtonPage) button).right ? 1 : -1);
			
			if(next >= 0 && next < max) {
				page = next;
				scheduleButtonUpdate = true;
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private void openPanel() {
		closePanel();
		panelEnabled = true;
		page = Math.min(page, getPageCount() - 1);

		panelX = gridLeft + (selectedX + 1) * 18;
		panelY = gridTop;

		searchField.xPosition = panelX + 18;
		searchField.yPosition = panelY + 4;
		spellNameField.setCanLoseFocus(true);
		spellNameField.setFocused(false);

		updatePanelButtons();
	}

	private void updatePanelButtons() {
		buttonList.removeAll(panelButtons);
		panelButtons.clear();
		visiblePieces.clear();

		PlayerData data = PlayerDataHandler.get(mc.thePlayer);
		for(String key : PsiAPI.spellPieceRegistry.getKeys()) {
			Class<? extends SpellPiece> clazz = PsiAPI.spellPieceRegistry.getObject(key);
			PieceGroup group = PsiAPI.groupsForPiece.get(clazz);
			if(!mc.thePlayer.capabilities.isCreativeMode && (group == null || !data.isPieceGroupUnlocked(group.name)))
				continue;
			
			SpellPiece p = SpellPiece.create(clazz, programmer.spell);
			if(StatCollector.translateToLocal(p.getUnlocalizedName()).toLowerCase().contains(searchField.getText().toLowerCase()))
				p.getShownPieces(visiblePieces);
		}
		
		visiblePieces.sort((SpellPiece a, SpellPiece b) -> {
			return a.getSortingName().compareTo(b.getSortingName());
		});

		int start = page * PIECES_PER_PAGE;
		
		for(int i = start; i < visiblePieces.size(); i++) {
			int c = i - start;
			if(c >= PIECES_PER_PAGE)
				break;
			
			SpellPiece piece = visiblePieces.get(i);
			panelButtons.add(new GuiButtonSpellPiece(this, piece, panelX + 5 + (c % 5) * 18, panelY + 20 + (c / 5) * 18));
		}
		
		if(page > 0)
			panelButtons.add(new GuiButtonPage(this, panelX + 4, panelY + panelHeight - 15, false));
		
		if(page < getPageCount() - 1)
			panelButtons.add(new GuiButtonPage(this, panelX + panelWidth - 22, panelY + panelHeight - 15, true));
		
		buttonList.addAll(panelButtons);
	}

	private int getPageCount() {
		return visiblePieces.size() / PIECES_PER_PAGE + 1;
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
		spellNameField.setEnabled(true);
		
		if(selectedX != -1 && selectedY != -1) {
			SpellPiece piece = programmer.spell.grid.gridData[selectedX][selectedY];
			if(piece != null) {
				boolean intercept = piece.interceptKeystrokes();
				spellNameField.setEnabled(!intercept);
				
				if(piece.hasConfig()) {
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
		}

		configEnabled = false;
	}

}

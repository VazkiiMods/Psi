/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 16:22:49 (GMT)]
 */
package vazkii.psi.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.util.RenderHelper;
import vazkii.arl.util.TooltipHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.PieceGroup;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellGrid;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellParam.Side;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.core.helper.SharingHelper;
import vazkii.psi.client.gui.button.GuiButtonIO;
import vazkii.psi.client.gui.button.GuiButtonPage;
import vazkii.psi.client.gui.button.GuiButtonSideConfig;
import vazkii.psi.client.gui.button.GuiButtonSpellPiece;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.message.MessageSpellModified;
import vazkii.psi.common.spell.SpellCompiler;
import vazkii.psi.common.spell.constant.PieceConstantNumber;

public class GuiProgrammer extends GuiScreen {

	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_PROGRAMMER);
	private static final int PIECES_PER_PAGE = 25;

	private static final Pattern inPattern = Pattern.compile("^in:(\\w+)(?:\\s?(.*))?$");
	private static final Pattern outPattern = Pattern.compile("^out:(\\w+)(?:\\s(.*))?$");

	public TileProgrammer programmer;
	public List<String> tooltip = new ArrayList();
	public Stack<Spell> undoSteps = new Stack();
	public Stack<Spell> redoSteps = new Stack();
	public static SpellPiece clipboard = null;

	SpellCompiler compiler;

	int xSize, ySize, padLeft, padTop, left, top, gridLeft, gridTop;
	int cursorX, cursorY;
	static int selectedX, selectedY;
	boolean panelEnabled, configEnabled, commentEnabled;
	int panelX, panelY, panelWidth, panelHeight;
	int page = 0;
	boolean scheduleButtonUpdate = false;
	List<SpellPiece> visiblePieces = new ArrayList();
	List<GuiButton> panelButtons = new ArrayList();
	List<GuiButton> configButtons = new ArrayList();
	GuiTextField searchField;
	GuiTextField spellNameField;
	GuiTextField commentField;
	
	public boolean takingScreenshot = false;
	public boolean shareToReddit = false;
	boolean spectator;

	public GuiProgrammer(TileProgrammer programmer) {
		this.programmer = programmer;
		compiler = new SpellCompiler(programmer.spell);
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);

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
		cursorX = cursorY = -1;
		searchField = new GuiTextField(0, fontRendererObj, 0, 0, 70, 10);
		searchField.setCanLoseFocus(false);
		searchField.setFocused(true);
		searchField.setEnableBackgroundDrawing(false);

		spectator = programmer.playerLock != null && !programmer.playerLock.isEmpty() && !programmer.playerLock.equals(mc.player.getName());

		spellNameField = new GuiTextField(0, fontRendererObj, left + xSize - 130, top + ySize - 14, 120, 10);
		spellNameField.setEnableBackgroundDrawing(false);
		spellNameField.setMaxStringLength(20);
		spellNameField.setEnabled(!spectator);
		
		commentField = new GuiTextField(0, fontRendererObj, left, top + ySize / 2 - 10, xSize, 20);
		commentField.setEnabled(false);
		commentField.setVisible(false);
		commentField.setMaxStringLength(500);

		if(programmer.spell == null)
			programmer.spell = new Spell();

		spellNameField.setText(programmer.spell.name);

		buttonList.clear();
		onSelectedChanged();
		buttonList.add(new GuiButtonIO(this, left + xSize + 2, top + ySize - (spectator ? 16 : 32), true));
		if(!spectator)
			buttonList.add(new GuiButtonIO(this, left + xSize + 2, top + ySize - 16, false));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(programmer == null || programmer.getWorld().getTileEntity(programmer.getPos()) != programmer || !programmer.canPlayerInteract(mc.player)) {
			mc.displayGuiScreen(null);
			return;
		}

		String comment = "";
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
				tooltip.add(TextFormatting.RED + I18n.format("psimisc.errored"));
				tooltip.add(TextFormatting.GRAY + I18n.format(compiler.getError()));
				Pair<Integer, Integer> errorPos = compiler.getErrorLocation();
				if(errorPos != null && errorPos.getLeft() != -1 && errorPos.getRight() != -1)
					tooltip.add(TextFormatting.GRAY + "[" + (errorPos.getLeft() + 1) + ", " + (errorPos.getRight() + 1) + "]");
			} else tooltip.add(TextFormatting.GREEN + I18n.format("psimisc.compiled"));
		}

		ItemStack cad = PsiAPI.getPlayerCAD(mc.player);
		if(!cad.isEmpty()) {
			int cadX = left - 42;
			int cadY = top + 12;
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			mc.getRenderItem().renderItemAndEffectIntoGUI(cad, cadX, cadY);
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
			GlStateManager.disableRescaleNormal();

			if(mouseX > cadX && mouseY > cadY && mouseX < cadX + 16 && mouseY < cadY + 16) {
				List<String> itemTooltip = cad.getTooltip(mc.player, ()->false);
				for (int i = 0; i < itemTooltip.size(); ++i)
					if (i == 0)
						itemTooltip.set(i, cad.getRarity().rarityColor + itemTooltip.get(i));
					else itemTooltip.set(i, TextFormatting.GRAY + itemTooltip.get(i));

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
				int statY = top + (takingScreenshot ? 40 : 20) + i * 20;
				int val = meta.stats.get(stat);

				EnumCADStat cadStat = stat.getTarget();
				int cadVal = 0;
				if(cadStat == null)
					cadVal = -1;
				else if(!cad.isEmpty()) {
					ICAD cadItem = (ICAD) cad.getItem();
					cadVal = cadItem.getStatValue(cad, cadStat);
				}
				String s = "" + val;
				if(stat == EnumSpellStat.COST)
					s += " (" + Math.max(0, ItemCAD.getRealCost(cad, ItemStack.EMPTY, val)) + ")";
				else s += "/" + (cadVal == -1 ? "\u221E" : cadVal);

				GlStateManager.color(1F, 1F, 1F);
				drawTexturedModalRect(statX, statY, (stat.ordinal() + 1) * 12, ySize + 16, 12, 12);
				mc.fontRendererObj.drawString(s, statX + 16, statY + 2, cadStat != null && cadVal < val && cadVal != -1 ? 0xFF6666 : 0xFFFFFF);
				mc.getTextureManager().bindTexture(texture);

				if(mouseX > statX && mouseY > statY && mouseX < statX + 12 && mouseY < statY + 12) {
					tooltip.add(TextFormatting.AQUA + I18n.format(stat.getName()));
					tooltip.add(TextFormatting.GRAY + I18n.format(stat.getDesc()));
				}

				i++;
			}
		}
		GlStateManager.color(1F, 1F, 1F);

		SpellPiece piece = null;
		if(programmer.spell.grid.exists(selectedX, selectedY))
			piece = programmer.spell.grid.gridData[selectedX][selectedY];
		
		if(configEnabled && !takingScreenshot) {
			drawTexturedModalRect(left - 81, top + 55, xSize, 30, 81, 115);
			String configStr = I18n.format("psimisc.config");
			mc.fontRendererObj.drawString(configStr, left - mc.fontRendererObj.getStringWidth(configStr) - 2, top + 45, 0xFFFFFF);

			int i = 0;
			if(piece != null) {
				int param = -1;
				for(int j = 0; j < 4; j++)
					if(Keyboard.isKeyDown(Keyboard.KEY_1 + j))
						param = j;

				for(String s : piece.params.keySet()) {
					int x = left - 75;
					int y = top + 70 + i * 26;

					GlStateManager.color(1F, 1F, 1F);
					mc.getTextureManager().bindTexture(texture);
					drawTexturedModalRect(x + 50, y - 8, xSize, 145, 24, 24);

					String localized = I18n.format(s);
					if(i == param)
						localized = TextFormatting.UNDERLINE + localized;

					mc.fontRendererObj.drawString(localized, x, y, color);
					i++;
				}
			}
		}

		cursorX = (mouseX - gridLeft) / 18;
		cursorY = (mouseY - gridTop) / 18;
		if(panelEnabled || cursorX > 8 || cursorY > 8 || cursorX < 0 || cursorY < 0 || mouseX < gridLeft || mouseY < gridTop) {
			cursorX = -1;
			cursorY = -1;
		}
		int tooltipX = mouseX;
		int tooltipY = mouseY;
		
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

		if(selectedX != -1 && selectedY != -1 && !takingScreenshot)
			drawTexturedModalRect(gridLeft + selectedX * 18, gridTop + selectedY * 18, 32, ySize, 16, 16);
		
		if(isAltKeyDown()) {
			tooltip.clear();
			cursorX = selectedX;
			cursorY = selectedY;
			tooltipX = gridLeft + cursorX * 18 + 10;
			tooltipY = gridTop + cursorY * 18 + 8;
		}
		
		if(cursorX != -1 && cursorY != -1) {
			SpellPiece pieceAt = programmer.spell.grid.gridData[cursorX][cursorY];
			if(pieceAt != null) {
				pieceAt.getTooltip(tooltip);
				comment = pieceAt.comment;
			}

			if(!takingScreenshot) {
				if(cursorX == selectedX && cursorY == selectedY)
					drawTexturedModalRect(gridLeft + cursorX * 18, gridTop + cursorY * 18, 16, ySize, 8, 16);
				else drawTexturedModalRect(gridLeft + cursorX * 18, gridTop + cursorY * 18, 16, ySize, 16, 16);
			}
		}

		int topy = top - 12;
		if(!takingScreenshot) {
			int topyText = topy;
			if(spectator) {
				String betaTest = TextFormatting.RED + I18n.format("psimisc.spectator");
				mc.fontRendererObj.drawStringWithShadow(betaTest, left + xSize / 2 - mc.fontRendererObj.getStringWidth(betaTest) / 2, topyText, 0xFFFFFF);
				topyText -= 10;
			}
			if(LibMisc.BETA_TESTING) {
				String betaTest = I18n.format("psimisc.wip");
				mc.fontRendererObj.drawStringWithShadow(betaTest, left + xSize / 2 - mc.fontRendererObj.getStringWidth(betaTest) / 2, topyText, 0xFFFFFF);
			}
			if(piece != null) {
				String name = I18n.format(piece.getUnlocalizedName());
				mc.fontRendererObj.drawStringWithShadow(name, left + xSize / 2 - mc.fontRendererObj.getStringWidth(name) / 2, topyText, 0xFFFFFF);
			}
			
			String coords = "";
			if(SpellGrid.exists(cursorX, cursorY))
				coords = I18n.format("psimisc.programmerCoords", selectedX + 1, selectedY + 1, cursorX + 1, cursorY + 1);
			else coords = I18n.format("psimisc.programmerCoordsNoCursor", selectedX + 1, selectedY + 1);
			mc.fontRendererObj.drawString(coords, left + 4, topy + ySize + 14, 0x44FFFFFF);
		}

		mc.fontRendererObj.drawStringWithShadow(I18n.format("psimisc.name"), left + padLeft, spellNameField.yPosition + 1, color);
		spellNameField.drawTextBox();
		if(panelEnabled) {
			tooltip.clear();
			mc.getTextureManager().bindTexture(texture);

			drawRect(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0x88000000);
			GlStateManager.color(1F, 1F, 1F);
			drawTexturedModalRect(searchField.xPosition - 14, searchField.yPosition - 2, 0, ySize + 16, 12, 12);
			searchField.drawTextBox();

			String s = page + 1 + "/" + getPageCount();
			fontRendererObj.drawStringWithShadow(s, panelX + panelWidth / 2 - fontRendererObj.getStringWidth(s) / 2, panelY + panelHeight - 12, 0xFFFFFF);
		}
		
		commentField.drawTextBox();
		if(commentEnabled) {
			String s = I18n.format("psimisc.enterCommit");
			mc.fontRendererObj.drawStringWithShadow(s, left + xSize / 2 - mc.fontRendererObj.getStringWidth(s) / 2, commentField.yPosition + 24, 0xFFFFFF);
			s = I18n.format("psimisc.semicolonLine");
			mc.fontRendererObj.drawStringWithShadow(s, left + xSize / 2 - mc.fontRendererObj.getStringWidth(s) / 2, commentField.yPosition + 34, 0xFFFFFF);
		}
		GlStateManager.color(1F, 1F, 1F);
		
		if(!takingScreenshot) {
			mc.getTextureManager().bindTexture(texture);
			int helpX = left + xSize + 2;
			int helpY = top + ySize - (spectator ? 32 : 48);
			boolean overHelp = mouseX > helpX && mouseY > helpY && mouseX < helpX + 12 && mouseY < helpY + 12;
			drawTexturedModalRect(helpX, helpY, xSize + (overHelp ? 12 : 0), ySize + 9, 12, 12);
			
			if(overHelp && !isAltKeyDown()) {
				TooltipHandler.addToTooltip(tooltip, "psimisc.programmerHelp");
				String ctrl = I18n.format(Minecraft.IS_RUNNING_ON_MAC ? "psimisc.ctrlMac" : "psimisc.ctrlWindows");
				TooltipHandler.tooltipIfShift(tooltip, () -> {
					for(int i = 0; i < 20; i++)
						TooltipHandler.addToTooltip(tooltip, "psi.programmerReference" + i, ctrl);
				});
			}
		}
		List<String> legitTooltip = null;
		if(isAltKeyDown())
			legitTooltip = new ArrayList(tooltip);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if(isAltKeyDown())
			tooltip = legitTooltip;

		if(!takingScreenshot) {
			if(!tooltip.isEmpty())
				RenderHelper.renderTooltip(tooltipX, tooltipY, tooltip);
			
			if(comment != null && !comment.isEmpty()) {
				List l = Arrays.asList(comment.split(";"));
				RenderHelper.renderTooltipGreen(tooltipX, tooltipY - 9 - l.size() * 10, l);
			}
		}

		GlStateManager.popMatrix();
		
		if(takingScreenshot) {
			String name = spellNameField.getText();
			NBTTagCompound cmp = new NBTTagCompound();
			if(programmer.spell != null)
				programmer.spell.writeToNBT(cmp);
			String export = cmp.toString();
			
			if(shareToReddit)
				SharingHelper.uploadAndShare(name, export);
			else SharingHelper.uploadAndOpen(name, export);
			
			takingScreenshot = false;
			shareToReddit = false;
		}
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
		} else if(!commentEnabled) {
			spellNameField.mouseClicked(mouseX, mouseY, mouseButton);
			if(commentField.getVisible())
				commentField.mouseClicked(mouseX, mouseY, mouseButton);

			if(cursorX != -1 && cursorY != -1) {
				selectedX = cursorX;
				selectedY = cursorY;
				if(mouseButton == 1 && !spectator) {
					if(isShiftKeyDown()) {
						pushState(true);
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
		if(par2 == Keyboard.KEY_ESCAPE) {
			if(panelEnabled) {
				closePanel();
				return;
			}
			
			if(commentEnabled) {
				closeComment(false);
				return;
			}
		}

		super.keyTyped(par1, par2);

		if(spectator)
			return;

		if(panelEnabled) {
			String last = searchField.getText();
			searchField.textboxKeyTyped(par1, par2);
			if(!searchField.getText().equals(last)) {
				page = 0;
				updatePanelButtons();
			}

			if(panelButtons.size() >= 1 && par2 == Keyboard.KEY_RETURN)
				actionPerformed(panelButtons.get(0));
		} else if(commentEnabled) {
			if(par2 == Keyboard.KEY_RETURN)
				closeComment(true);
			commentField.textboxKeyTyped(par1, par2);
		} else {
			boolean pieceHandled = false;
			boolean intercepts = false;
			SpellPiece piece = null;
			if(selectedX != -1 && selectedY != -1) {
				piece = programmer.spell.grid.gridData[selectedX][selectedY];
				if(piece != null && piece.interceptKeystrokes()) {
					intercepts = true;
					if(piece.onKeyPressed(par1, par2, false)) {
						pushState(true);
						piece.onKeyPressed(par1, par2, true);
						onSpellChanged(false);
						pieceHandled = true;
					}
				}
			}

			boolean shift = isShiftKeyDown();
			boolean ctrl = isCtrlKeyDown();

			if(!pieceHandled) {
				if((par2 == Keyboard.KEY_DELETE || par2 == Keyboard.KEY_BACK) && !spellNameField.isFocused()) {
					if(shift && ctrl) {
						if(!programmer.spell.grid.isEmpty()) {
							pushState(true);
							programmer.spell = new Spell();
							spellNameField.setText("");
							onSpellChanged(false);
							return;
						}
					} if(selectedX != -1 && selectedY != -1 && piece != null) {
						pushState(true);
						programmer.spell.grid.gridData[selectedX][selectedY] = null;
						onSpellChanged(false);
						return;
					}
				}
				
				String lastName = spellNameField.getText();
				spellNameField.textboxKeyTyped(par1, par2);
				String currName = spellNameField.getText();
				if(!lastName.equals(currName)) {
					programmer.spell.name = currName;
					onSpellChanged(true);
				}
				
				if(par2 == Keyboard.KEY_TAB && !intercepts)
					spellNameField.setFocused(!spellNameField.isFocused());
				else if(!spellNameField.isFocused()) {
					if(ctrl) {
						switch(par2) {
						case Keyboard.KEY_UP:
							if(programmer.spell.grid.shift(Side.TOP, false)) {
								pushState(true);
								programmer.spell.grid.shift(Side.TOP, true);
								onSpellChanged(false);
							}
							break;
						case Keyboard.KEY_LEFT:
							if(programmer.spell.grid.shift(Side.LEFT, false)) {
								pushState(true);
								programmer.spell.grid.shift(Side.LEFT, true);
								onSpellChanged(false);
							}
							break;
						case Keyboard.KEY_RIGHT:
							if(programmer.spell.grid.shift(Side.RIGHT, false)) {
								pushState(true);
								programmer.spell.grid.shift(Side.RIGHT, true);
								onSpellChanged(false);
							}
							break;
						case Keyboard.KEY_DOWN:
							if(programmer.spell.grid.shift(Side.BOTTOM, false)) {
								pushState(true);
								programmer.spell.grid.shift(Side.BOTTOM, true);
								onSpellChanged(false);
							}
							break;
						case Keyboard.KEY_Z:
							if(!undoSteps.isEmpty()) {
								redoSteps.add(programmer.spell.copy());
								programmer.spell = undoSteps.pop();
								onSpellChanged(false);
							}
							break;
						case Keyboard.KEY_Y:
							if(!redoSteps.isEmpty()) {
								pushState(false);
								programmer.spell = redoSteps.pop();
								onSpellChanged(false);
							}
							break;
						case Keyboard.KEY_C:
							if(piece != null)
								clipboard = piece.copy();
							break;
						case Keyboard.KEY_X:
							if(piece != null) {
								clipboard = piece.copy();
								pushState(true);
								programmer.spell.grid.gridData[selectedX][selectedY] = null;
								onSpellChanged(false);
							}
							break;
						case Keyboard.KEY_V:
							if(SpellGrid.exists(selectedX, selectedY) && clipboard != null) {
								pushState(true);
								SpellPiece copy = clipboard.copy();
								copy.x = selectedX;
								copy.y = selectedY;
								programmer.spell.grid.gridData[selectedX][selectedY] = copy;
								onSpellChanged(false);
							}
							break;
						case Keyboard.KEY_D:
							if(piece != null) {
								commentField.setVisible(true);
								commentField.setFocused(true);
								commentField.setEnabled(true);
								spellNameField.setEnabled(false);
								commentField.setText(piece.comment);
								commentEnabled = true;
							}
							break;
						case Keyboard.KEY_G:
							shareToReddit = false;
							if(shift && isAltKeyDown())
								takingScreenshot = true;
							break;
						case Keyboard.KEY_R:
							shareToReddit = true;
							if(shift && isAltKeyDown())
								takingScreenshot = true;
							break;
						}
					} else {
						int param = -1;
						for(int i = 0; i < 4; i++)
							if(Keyboard.isKeyDown(Keyboard.KEY_1 + i))
								param = i;

						switch(par2) {
						case Keyboard.KEY_UP:
							if(!onSideButtonKeybind(piece, param, Side.TOP) && selectedY > 0) {
								selectedY--;
								onSelectedChanged();
							}
							break;
						case Keyboard.KEY_LEFT:
							if(!onSideButtonKeybind(piece, param, Side.LEFT) && selectedX > 0) {
								selectedX--;
								onSelectedChanged();
							}
							break;
						case Keyboard.KEY_RIGHT:
							if(!onSideButtonKeybind(piece, param, Side.RIGHT) && selectedX < SpellGrid.GRID_SIZE - 1) {
								selectedX++;
								onSelectedChanged();
							}
							break;
						case Keyboard.KEY_DOWN:
							if(!onSideButtonKeybind(piece, param, Side.BOTTOM) && selectedY < SpellGrid.GRID_SIZE - 1) {
								selectedY++;
								onSelectedChanged();
							}
							break;
						case Keyboard.KEY_RETURN:
							openPanel();
							break;
						}
					}
				}
			}
		}
	}

	public boolean onSideButtonKeybind(SpellPiece piece, int param, SpellParam.Side side) {
		if(param > -1 && piece != null && piece.params.size() >= param) {
			for(GuiButton b : configButtons) {
				GuiButtonSideConfig config = (GuiButtonSideConfig) b;
				if(config.matches(param, side)) {
					if(side != Side.OFF && piece.paramSides.get(piece.params.get(config.paramName)) == side) {
						side = Side.OFF;
						continue;
					}

					try {
						actionPerformed(config);
						return true;
					} catch(IOException e) {
						return false;
					}
				}
			}
		}

		return side == Side.OFF;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);

		if(button.id == 9000)
			closePanel();

		if(button instanceof GuiButtonSpellPiece) {
			pushState(true);
			SpellPiece piece = ((GuiButtonSpellPiece) button).piece.copy();
			if(piece.getPieceType() == EnumPieceType.TRICK && spellNameField.getText().isEmpty()) {
				String pieceName = I18n.format(piece.getUnlocalizedName());
				String patternStr = I18n.format("psimisc.trickPattern");
				Pattern pattern = Pattern.compile(patternStr);
				Matcher matcher = pattern.matcher(pieceName);
				if(matcher.matches()) {
					String spellName = matcher.group(1);
					programmer.spell.name = spellName;
					spellNameField.setText(spellName);
				}
			}
			programmer.spell.grid.gridData[selectedX][selectedY] = piece;
			piece.isInGrid = true;
			piece.x = selectedX;
			piece.y = selectedY;
			onSpellChanged(false);
			closePanel();
		} else if(button instanceof GuiButtonSideConfig) {
			if(!spectator) {
				pushState(true);
				((GuiButtonSideConfig) button).onClick();
				onSpellChanged(false);
			}
		} else if(button instanceof GuiButtonPage) {
			int max = getPageCount();
			int next = page + (((GuiButtonPage) button).right ? 1 : -1);

			if(next >= 0 && next < max) {
				page = next;
				scheduleButtonUpdate = true;
			}
		} else if(button instanceof GuiButtonIO) {
			if(isShiftKeyDown()) {
				if(((GuiButtonIO) button).out) {
					NBTTagCompound cmp = new NBTTagCompound();
					if(programmer.spell != null)
						programmer.spell.writeToNBT(cmp);
					setClipboardString(cmp.toString());
				} else {
					if(spectator)
						return;

					String cb = getClipboardString();
					try {
						NBTTagCompound cmp = JsonToNBT.getTagFromJson(cb);
						Spell spell = Spell.createFromNBT(cmp);
						PlayerData data = PlayerDataHandler.get(mc.player);
						for(int i = 0; i < SpellGrid.GRID_SIZE; i++)
							for(int j = 0; j < SpellGrid.GRID_SIZE; j++) {
								SpellPiece piece = spell.grid.gridData[i][j];
								if(piece != null) {
									PieceGroup group = PsiAPI.groupsForPiece.get(piece.getClass());
									if(!mc.player.capabilities.isCreativeMode && (group == null || !data.isPieceGroupUnlocked(group.name))) {
										mc.player.sendMessage(new TextComponentTranslation("psimisc.missingPieces").setStyle(new Style().setColor(TextFormatting.RED)));
										return;
									}
								}
							}

						pushState(true);
						programmer.spell = spell;
						spellNameField.setText(spell.name);
						onSpellChanged(false);
					} catch(Throwable t) {
						mc.player.sendMessage(new TextComponentTranslation("psimisc.malformedJson").setStyle(new Style().setColor(TextFormatting.RED)));
					}
				}
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
		searchField.setText("");
		spellNameField.setFocused(false);

		updatePanelButtons();
	}

	private void updatePanelButtons() {
		buttonList.removeAll(panelButtons);
		panelButtons.clear();
		visiblePieces.clear();

		PlayerData data = PlayerDataHandler.get(mc.player);
		for(String key : PsiAPI.spellPieceRegistry.getKeys()) {
			Class<? extends SpellPiece> clazz = PsiAPI.spellPieceRegistry.getObject(key);
			PieceGroup group = PsiAPI.groupsForPiece.get(clazz);
			if(!mc.player.capabilities.isCreativeMode && (group == null || !data.isPieceGroupUnlocked(group.name)))
				continue;

			SpellPiece p = SpellPiece.create(clazz, programmer.spell);
			if(shouldShow(p))
				p.getShownPieces(visiblePieces);
		}

		if(visiblePieces.isEmpty()) {
			try {
				String text = searchField.getText();
				if(!text.isEmpty() && text.length() < 5 && !text.matches(".*(?:F|D|f|d).*")) {
					Double.parseDouble(text);
					SpellPiece p = SpellPiece.create(PieceConstantNumber.class, programmer.spell);
					((PieceConstantNumber) p).valueStr = text;
					visiblePieces.add(p);
				}
			} catch(NumberFormatException e) {}
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
			panelButtons.add(new GuiButtonSpellPiece(this, piece, panelX + 5 + c % 5 * 18, panelY + 20 + c / 5 * 18));
		}

		if(page > 0)
			panelButtons.add(new GuiButtonPage(this, panelX + 4, panelY + panelHeight - 15, false));

		if(page < getPageCount() - 1)
			panelButtons.add(new GuiButtonPage(this, panelX + panelWidth - 22, panelY + panelHeight - 15, true));

		buttonList.addAll(panelButtons);
	}

	private boolean shouldShow(SpellPiece p) {
		String searchToken = searchField.getText().toLowerCase();
		String nameToken = searchToken;

		Matcher inMatcher = inPattern.matcher(searchToken);
		Matcher outMatcher = outPattern.matcher(searchToken);

		if(inMatcher.matches()) {
			String in = inMatcher.group(1);
			boolean hasIn = false;
			for(SpellParam param : p.params.values()) {
				String type = param.getRequiredTypeString().toLowerCase();
				if(type.contains(in))
					hasIn = true;
			}

			if(!hasIn)
				return false;

			nameToken = inMatcher.group(2);
		} else if(outMatcher.matches()) {
			String out = outMatcher.group(1);

			String type = p.getEvaluationTypeString().toLowerCase();
			if(!type.contains(out))
				return false;

			nameToken = outMatcher.group(2);
		}
		
		String haystack = I18n.format(p.getUnlocalizedName()).toLowerCase(); 
		Predicate<String> pred = haystack::contains;
		
		if(nameToken == null)
			nameToken = "";
		
		if(nameToken.startsWith("_")) {
			nameToken = nameToken.substring(1);
			pred = haystack::endsWith;
		} else if(nameToken.endsWith("_")) {
			nameToken = nameToken.substring(0, nameToken.length() - 1);
			pred = haystack::startsWith;
		}

		return pred.apply(nameToken);
	}

	private int getPageCount() {
		return visiblePieces.size() / PIECES_PER_PAGE + 1;
	}

	private void closePanel() {
		panelEnabled = false;
		buttonList.removeAll(panelButtons);
		panelButtons.clear();
	}

	private void closeComment(boolean save) {
		SpellPiece piece = null;
		if(selectedX != -1 && selectedY != -1)
			piece = programmer.spell.grid.gridData[selectedX][selectedY];
			
		if(save && piece != null) {
			String text = commentField.getText();
			pushState(true);
			piece.comment = text;
			onSpellChanged(false);
		}
		
		spellNameField.setEnabled(!spectator && (piece == null || !piece.interceptKeystrokes()));
		commentField.setFocused(false);
		commentField.setVisible(false);
		commentField.setEnabled(false);
		commentField.setText("");
		commentEnabled = false;
	}
	
	public void onSpellChanged(boolean nameOnly) {
		if(!spectator) {
			MessageSpellModified message = new MessageSpellModified(programmer.getPos(), programmer.spell);
			NetworkHandler.INSTANCE.sendToServer(message);
		}

		programmer.onSpellChanged();
		onSelectedChanged();
		spellNameField.setFocused(nameOnly);

		if(!nameOnly || compiler != null && compiler.getError() != null && compiler.getError().equals(SpellCompilationException.NO_NAME) || programmer.spell.name.isEmpty())
			compiler = new SpellCompiler(programmer.spell);
	}

	public void pushState(boolean wipeRedo) {
		if(wipeRedo)
			redoSteps.clear();
		undoSteps.push(programmer.spell.copy());
		if(undoSteps.size() > 25)
			undoSteps.remove(0);
	}

	public void onSelectedChanged() {
		buttonList.removeAll(configButtons);
		configButtons.clear();
		spellNameField.setEnabled(!spectator);
		spellNameField.setFocused(false);

		if(selectedX != -1 && selectedY != -1) {
			SpellPiece piece = programmer.spell.grid.gridData[selectedX][selectedY];
			if(piece != null) {
				boolean intercept = piece.interceptKeystrokes();
				spellNameField.setEnabled(!spectator && !intercept);

				if(piece.hasConfig()) {
					int i = 0;
					for(String paramName : piece.params.keySet()) {
						SpellParam param = piece.params.get(paramName);
						int x = left - 17;
						int y = top + 70 + i * 26;
						for(SpellParam.Side side : ImmutableSet.of(Side.TOP, Side.BOTTOM, Side.LEFT, Side.RIGHT, Side.OFF)) {
							if(!side.isEnabled() && !param.canDisable)
								continue;

							int xp = x + side.offx * 8;
							int yp = y + side.offy * 8;
							configButtons.add(new GuiButtonSideConfig(this, selectedX, selectedY, i, paramName, side, xp, yp));
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
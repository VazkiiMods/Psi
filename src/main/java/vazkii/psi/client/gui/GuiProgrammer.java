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
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.ImmutableSet;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.PieceGroup;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellGrid;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellParam.Side;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.core.helper.RenderHelper;
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
import vazkii.psi.common.network.NetworkHandler;
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
	boolean panelEnabled, configEnabled;
	int panelX, panelY, panelWidth, panelHeight;
	int page = 0;
	boolean scheduleButtonUpdate = false;
	List<SpellPiece> visiblePieces = new ArrayList();
	List<GuiButton> panelButtons = new ArrayList();
	List<GuiButton> configButtons = new ArrayList();
	GuiTextField searchField;
	GuiTextField spellNameField;
	boolean spectator;

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
		cursorX = cursorY = -1;
		searchField = new GuiTextField(0, fontRendererObj, 0, 0, 70, 10);
		searchField.setCanLoseFocus(false);
		searchField.setFocused(true);
		searchField.setEnableBackgroundDrawing(false);

		spectator = programmer.playerLock != null && !programmer.playerLock.isEmpty() && !programmer.playerLock.equals(mc.thePlayer.getName());

		spellNameField = new GuiTextField(0, fontRendererObj, left + xSize - 130, top + ySize - 14, 120, 10);
		spellNameField.setEnableBackgroundDrawing(false);
		spellNameField.setMaxStringLength(20);
		spellNameField.setEnabled(!spectator);

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
		if(programmer == null || programmer.getWorld().getTileEntity(programmer.getPos()) != programmer) {
			mc.displayGuiScreen(null);
			return;
		}

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
				tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal(compiler.getError()));
				Pair<Integer, Integer> errorPos = compiler.getErrorLocation();
				if(errorPos != null && errorPos.getLeft() != -1 && errorPos.getRight() != -1)
					tooltip.add(EnumChatFormatting.GRAY + "[" + (errorPos.getLeft() + 1) + ", " + (errorPos.getRight() + 1) + "]");
			} else tooltip.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("psimisc.compiled"));
		}

		ItemStack cad = PsiAPI.getPlayerCAD(mc.thePlayer);
		if(cad != null) {
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
				if(stat == EnumSpellStat.COST)
					s += " (" + Math.max(0, ItemCAD.getRealCost(cad, null, val)) + ")";
				else s += "/" + (cadVal == -1 ? "\u221E" : cadVal);

				GlStateManager.color(1F, 1F, 1F);
				drawTexturedModalRect(statX, statY, (stat.ordinal() + 1) * 12, ySize + 16, 12, 12);
				mc.fontRendererObj.drawString(s, statX + 16, statY + 2, cadStat != null && cadVal < val && cadVal != -1 ? 0xFF6666 : 0xFFFFFF);
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

					String localized = StatCollector.translateToLocal(s);
					if(i == param)
						localized = EnumChatFormatting.UNDERLINE + localized;

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

		int topy = top - 12;
		if(spectator) {
			String betaTest = EnumChatFormatting.RED + StatCollector.translateToLocal("psimisc.spectator");
			mc.fontRendererObj.drawStringWithShadow(betaTest, left + xSize / 2 - mc.fontRendererObj.getStringWidth(betaTest) / 2, topy, 0xFFFFFF);
			topy -= 10;
		}
		if(LibMisc.BETA_TESTING) {
			String betaTest = StatCollector.translateToLocal("psimisc.wip");
			mc.fontRendererObj.drawStringWithShadow(betaTest, left + xSize / 2 - mc.fontRendererObj.getStringWidth(betaTest) / 2, topy, 0xFFFFFF);
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

			String s = page + 1 + "/" + getPageCount();
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
		if(panelEnabled && par2 == Keyboard.KEY_ESCAPE) {
			closePanel();
			return;
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

			if(panelButtons.size() == 1 && par2 == Keyboard.KEY_RETURN)
				actionPerformed(panelButtons.get(0));
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

			if(par2 == Keyboard.KEY_DELETE) {
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

			if(!pieceHandled) {
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
						PlayerData data = PlayerDataHandler.get(mc.thePlayer);
						for(int i = 0; i < SpellGrid.GRID_SIZE; i++)
							for(int j = 0; j < SpellGrid.GRID_SIZE; j++) {
								SpellPiece piece = spell.grid.gridData[i][j];
								if(piece != null) {
									PieceGroup group = PsiAPI.groupsForPiece.get(piece.getClass());
									if(!mc.thePlayer.capabilities.isCreativeMode && (group == null || !data.isPieceGroupUnlocked(group.name))) {
										mc.thePlayer.addChatComponentMessage(new ChatComponentTranslation("psimisc.missingPieces").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
										return;
									}
								}
							}

						pushState(true);
						programmer.spell = spell;
						spellNameField.setText(spell.name);
						onSpellChanged(false);
					} catch(Throwable t) {
						mc.thePlayer.addChatComponentMessage(new ChatComponentTranslation("psimisc.malformedJson").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
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

		PlayerData data = PlayerDataHandler.get(mc.thePlayer);
		for(String key : PsiAPI.spellPieceRegistry.getKeys()) {
			Class<? extends SpellPiece> clazz = PsiAPI.spellPieceRegistry.getObject(key);
			PieceGroup group = PsiAPI.groupsForPiece.get(clazz);
			if(!mc.thePlayer.capabilities.isCreativeMode && (group == null || !data.isPieceGroupUnlocked(group.name)))
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

		if(nameToken == null)
			nameToken = "";

		return StatCollector.translateToLocal(p.getUnlocalizedName()).toLowerCase().contains(nameToken);
	}

	private int getPageCount() {
		return visiblePieces.size() / PIECES_PER_PAGE + 1;
	}

	private void closePanel() {
		panelEnabled = false;
		buttonList.removeAll(panelButtons);
		panelButtons.clear();
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
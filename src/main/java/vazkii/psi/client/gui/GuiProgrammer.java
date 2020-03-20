package vazkii.psi.client.gui;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.*;
import vazkii.psi.client.core.helper.SharingHelper;
import vazkii.psi.client.gui.button.GuiButtonHelp;
import vazkii.psi.client.gui.button.GuiButtonIO;
import vazkii.psi.client.gui.button.GuiButtonSideConfig;
import vazkii.psi.client.gui.widget.*;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageSpellModified;
import vazkii.psi.common.spell.SpellCompiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class GuiProgrammer extends Screen {

	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_PROGRAMMER);
	public static final RenderType LAYER;
	static {
		RenderType.State glState = RenderType.State.builder()
						.texture(new RenderState.TextureState(texture, false, false))
						.lightmap(new RenderState.LightmapState(true))
						.alpha(new RenderState.AlphaState(0.004F)).build(false);
		LAYER = RenderType.of(LibMisc.PREFIX_MOD + "programmer", DefaultVertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 64, glState);
	}

	public final TileProgrammer programmer;
	public Spell spell;
	public List<ITextComponent> tooltip = new ArrayList<>();

	public final Stack<Spell> undoSteps = new Stack<>();
	public final Stack<Spell> redoSteps = new Stack<>();
	public static SpellPiece clipboard = null;

	public SpellCompiler compiler;

	public int xSize, ySize, padLeft, padTop, left, top, gridLeft, gridTop;
	public int cursorX, cursorY;
	public static int selectedX, selectedY;
	public boolean commentEnabled;

	public GuiButtonHelp helpButton;
	public TextFieldWidget spellNameField;
	public TextFieldWidget commentField;
	public PiecePanelWidget panelWidget;
	public SideConfigWidget configWidget;
	public SpellCostsWidget spellCostsWidget;
	public StatusWidget statusWidget;
	public ITooltipFlag tooltipFlag;

	public boolean takingScreenshot = false;
	public boolean shareToReddit = false;
	boolean spectator;


	public GuiProgrammer(TileProgrammer programmer) {
		this(programmer, programmer.spell);
	}


	public GuiProgrammer(TileProgrammer tile, Spell spell) {
		super(new StringTextComponent(""));
		programmer = tile;
		this.spell = spell;
		compiler = new SpellCompiler(spell);
	}

	@Override
	protected void init() {
		xSize = 174;
		ySize = 184;
		padLeft = 7;
		padTop = 7;
		left = (width - xSize) / 2;
		top = (height - ySize) / 2;
		gridLeft = left + padLeft;
		gridTop = top + padTop;
		cursorX = cursorY = -1;
		tooltipFlag = minecraft.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL;

		if (programmer == null)
			spectator = false;
		else
			spectator = !programmer.playerLock.isEmpty() && !programmer.playerLock.equals(minecraft.player.getName().getString());

		statusWidget = addButton(new StatusWidget(left - 48, top + 5, 48, 30, "", this));
		spellCostsWidget = addButton(new SpellCostsWidget(left + xSize + 3, top + (takingScreenshot ? 40 : 20), 100, 126, "", this));
		panelWidget = addButton(new PiecePanelWidget(0, 0, 100, 125, "", this));
		helpButton = addButton(new GuiButtonHelp(left + xSize + 2, top + ySize - (spectator ? 32 : 48), this));
		configWidget = addButton(new SideConfigWidget(left - 81, top + 55, 81, 115, this));

		spellNameField = addButton(new CallbackTextFieldWidget(font, left + xSize - 130, top + ySize - 14, 120, 10, button -> {
			spell.name = spellNameField.getText();
			onSpellChanged(true);
		}));
		spellNameField.setEnableBackgroundDrawing(false);
		spellNameField.setMaxStringLength(20);
		spellNameField.setEnabled(!spectator);

		commentField = addButton(new CallbackTextFieldWidget(font, left, top + ySize / 2 - 10, xSize, 20, button -> {

		}));
		commentField.setEnabled(false);
		commentField.setVisible(false);
		commentField.setMaxStringLength(500);

		panelWidget.searchField = addButton(new CallbackTextFieldWidget(font, 0, 0, 70, 10, button -> {
			panelWidget.page = 0;
			panelWidget.updatePanelButtons();
		}));
		panelWidget.searchField.setEnabled(false);
		panelWidget.searchField.setVisible(false);
		panelWidget.searchField.setEnableBackgroundDrawing(false);

		if (spell == null)
			spell = new Spell();
		if (programmer != null && programmer.spell == null)
			programmer.spell = spell;


		spellNameField.setText(spell.name);

		panelWidget.populatePanelButtons();

		onSelectedChanged();

		/*
		 * Export button
		 */

		addButton(new GuiButtonIO(left + xSize + 2, top + ySize - (spectator ? 16 : 32), true, this, button -> {
			if (hasShiftDown()) {
				CompoundNBT cmp = new CompoundNBT();
				if (spell != null)
					spell.writeToNBT(cmp);
				minecraft.keyboardListener.setClipboardString(cmp.toString());
			}
		}));

		/*
		 * Import button
		 */
		if (!spectator)
			addButton(new GuiButtonIO(left + xSize + 2, top + ySize - 16, false, this, button -> {
				if (hasShiftDown()) {
					String cb = minecraft.keyboardListener.getClipboardString();

					try {
						cb = cb.replaceAll("([^a-z0-9])\\d+:", "$1"); // backwards compatibility with pre 1.12 nbt json
						CompoundNBT cmp = JsonToNBT.getTagFromJson(cb);
						spell = Spell.createFromNBT(cmp);
						PlayerDataHandler.PlayerData data = PlayerDataHandler.get(minecraft.player);
						for (int i = 0; i < SpellGrid.GRID_SIZE; i++)
							for (int j = 0; j < SpellGrid.GRID_SIZE; j++) {
								SpellPiece piece = spell.grid.gridData[i][j];
								if (piece != null) {
									ResourceLocation group = PsiAPI.advancementGroupsInverse.get(piece.getClass());
									if (!minecraft.player.isCreative() && (group == null || !data.isPieceGroupUnlocked(group, piece.registryKey))) {
										minecraft.player.sendMessage(new TranslationTextComponent("psimisc.missing_pieces").setStyle(new Style().setColor(TextFormatting.RED)));
										return;
									}
								}
							}

						pushState(true);
						spellNameField.setText(spell.name);
						onSpellChanged(false);
					} catch (Throwable t) {
						minecraft.player.sendMessage(new TranslationTextComponent("psimisc.malformed_json", t.getMessage()).setStyle(new Style().setColor(TextFormatting.RED)));
					}
				}
			}));
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		if (programmer != null && (programmer.getWorld().getTileEntity(programmer.getPos()) != programmer || !programmer.canPlayerInteract(minecraft.player))) {
			minecraft.displayGuiScreen(null);
			return;
		}

		String comment = "";
		int color = Psi.magical ? 0 : 0xFFFFFF;


		RenderSystem.pushMatrix();
		renderBackground();

		RenderSystem.color3f(1F, 1F, 1F);
		minecraft.getTextureManager().bindTexture(texture);

		blit(left, top, 0, 0, xSize, ySize);

		//Currently selected piece
		//TODO, refactor name to be more clear
		SpellPiece piece = null;
		if (SpellGrid.exists(selectedX, selectedY))
			piece = spell.grid.gridData[selectedX][selectedY];

		cursorX = (mouseX - gridLeft) / 18;
		cursorY = (mouseY - gridTop) / 18;
		if (panelWidget.panelEnabled || cursorX > 8 || cursorY > 8 || cursorX < 0 || cursorY < 0 || mouseX < gridLeft || mouseY < gridTop) {
			cursorX = -1;
			cursorY = -1;
		}

		RenderSystem.pushMatrix();
		tooltip.clear();
		RenderSystem.translatef(gridLeft, gridTop, 0);
		IRenderTypeBuffer.Impl buffers = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuffer());
		spell.draw(new MatrixStack(), buffers, 0xF000F0);
		buffers.draw();

		if (compiler.isErrored()) {
			Pair<Integer, Integer> errorPos = compiler.getErrorLocation();
			if (errorPos != null && errorPos.getRight() != -1 && errorPos.getLeft() != -1) {
				font.drawStringWithShadow("!!", errorPos.getLeft() * 18 + 12, errorPos.getRight() * 18 + 8, 0xFF0000);
			}
		}
		RenderSystem.popMatrix();
		RenderSystem.color3f(1f, 1f, 1f);
		RenderSystem.translatef(0, 0, 1);
		minecraft.getTextureManager().bindTexture(texture);

		if (selectedX != -1 && selectedY != -1 && !takingScreenshot)
			blit(gridLeft + selectedX * 18, gridTop + selectedY * 18, 32, ySize, 16, 16);

		if (hasAltDown()) {
			tooltip.clear();
			cursorX = selectedX;
			cursorY = selectedY;
			mouseX = gridLeft + cursorX * 18 + 10;
			mouseY = gridTop + cursorY * 18 + 8;
		}

		SpellPiece pieceAtCursor = null;
		if (cursorX != -1 && cursorY != -1) {
			pieceAtCursor = spell.grid.gridData[cursorX][cursorY];
			if (pieceAtCursor != null) {
				pieceAtCursor.getTooltip(tooltip);
				comment = pieceAtCursor.comment;
			}

			if (!takingScreenshot) {
				if (cursorX == selectedX && cursorY == selectedY)
					blit(gridLeft + cursorX * 18, gridTop + cursorY * 18, 16, ySize, 8, 16);
				else blit(gridLeft + cursorX * 18, gridTop + cursorY * 18, 16, ySize, 16, 16);
			}
		}

		int topY = top - 22;

		if (!takingScreenshot) {
			int topYText = topY;
			if (spectator) {
				String spectator = TextFormatting.RED + I18n.format("psimisc.spectator");
				font.drawStringWithShadow(spectator, left + xSize / 2f - font.getStringWidth(spectator) / 2f, topYText, 0xFFFFFF);
				topYText -= 10;
			}
			if (piece != null) {
				String pieceName = I18n.format(piece.getUnlocalizedName());
				font.drawStringWithShadow(pieceName, left + xSize / 2f - font.getStringWidth(pieceName) / 2f, topYText, 0xFFFFFF);
				topYText -= 10;
			}
			if (LibMisc.BETA_TESTING) {
				String betaTest = TextFormatting.GOLD + I18n.format("psimisc.wip");
				font.drawStringWithShadow(betaTest, left + xSize / 2f - font.getStringWidth(betaTest) / 2f, topYText, 0xFFFFFF);

			}

			String coords = "";
			if (SpellGrid.exists(cursorX, cursorY))
				coords = I18n.format("psimisc.programmer_coords", selectedX + 1, selectedY + 1, cursorX + 1, cursorY + 1);
			else
				coords = I18n.format("psimisc.programmer_coords_no_cursor", selectedX + 1, selectedY + 1);
			font.drawString(coords, left + 4, topY + ySize + 24, 0x44FFFFFF);
		}

		if (Psi.magical)
			font.drawString(I18n.format("psimisc.name"), left + padLeft, spellNameField.y + 1, color);
		else
			font.drawStringWithShadow(I18n.format("psimisc.name"), left + padLeft, spellNameField.y + 1, color);

		//Add here comment
		if (commentEnabled) {
			String enterCommit = I18n.format("psimisc.enter_commit");
			font.drawStringWithShadow(enterCommit, left + xSize / 2f - font.getStringWidth(enterCommit) / 2f, commentField.y + 24, 0xFFFFFF);

			String semicolonLine = I18n.format("psimisc.semicolon_line");
			font.drawStringWithShadow(enterCommit, left + xSize / 2f - font.getStringWidth(enterCommit) / 2f, commentField.y + 34, 0xFFFFFF);
		}


		List<ITextComponent> legitTooltip = null;
		if (hasAltDown())
			legitTooltip = new ArrayList<>(tooltip);


		if (hasAltDown())
			tooltip = legitTooltip;


		if (!takingScreenshot && pieceAtCursor != null) {
			if (tooltip != null && !tooltip.isEmpty()) {
				pieceAtCursor.drawTooltip(mouseX, mouseY, tooltip);
			}

			if (comment != null && !comment.isEmpty()) {
				List<ITextComponent> commentList = Arrays.stream(comment.split(";")).map(StringTextComponent::new).collect(Collectors.toList());
				pieceAtCursor.drawCommentText(mouseX, mouseY, commentList);
			}
		}
		super.render(mouseX, mouseY, partialTicks);
		if (!takingScreenshot && tooltip != null && !tooltip.isEmpty()) {
			List<String> textComptoString = new ArrayList<>();
			tooltip.forEach(el -> textComptoString.add(el.getString()));
			GuiUtils.drawHoveringText(textComptoString, mouseX, mouseY, width, height, -1, font);

		}

		RenderSystem.popMatrix();


		if (takingScreenshot) {
			String name = spell.name;
			CompoundNBT cmp = new CompoundNBT();
			if (spell != null)
				spell.writeToNBT(cmp);
			String export = cmp.toString();

			if (shareToReddit)
				SharingHelper.uploadAndShare(name, export);
			else SharingHelper.uploadAndOpen(name, export);

			takingScreenshot = false;
			shareToReddit = false;
		}


	}

	public void removeButtons(List<Button> list) {
		removeButtonList(list);
	}

	private void removeButtonList(List<Button> list) {
		buttons.removeAll(list);
		children.removeAll(list);
	}

	public void addButtons(List<Button> list) {
		list.forEach(this::addButton);
	}

	public void pushState(boolean wipeRedo) {
		if (wipeRedo)
			redoSteps.clear();
		undoSteps.push(spell.copy());
		if (undoSteps.size() > 25)
			undoSteps.remove(0);
	}

	public void onSpellChanged(boolean nameOnly) {
		if (programmer != null) {
			if (!spectator) {
				MessageSpellModified message = new MessageSpellModified(programmer.getPos(), spell);
				MessageRegister.HANDLER.sendToServer(message);
			}

			programmer.spell = spell;
			programmer.onSpellChanged();
		}

		onSelectedChanged();

		if (!nameOnly || compiler != null && compiler.getError() != null && compiler.getError().equals(SpellCompilationException.NO_NAME) || spell.name.isEmpty())
			compiler = new SpellCompiler(spell);
	}

	public void onSelectedChanged() {
		buttons.removeAll(configWidget.configButtons);
		children.removeAll(configWidget.configButtons);
		configWidget.configButtons.clear();

		spellNameField.setEnabled(!spectator);
		if (selectedX != -1 && selectedY != -1) {
			SpellPiece piece = spell.grid.gridData[selectedX][selectedY];
			if (piece != null) {
				boolean intercept = piece.interceptKeystrokes();
				spellNameField.setEnabled(!spectator && !intercept);

				if (piece.hasConfig()) {
					int i = 0;
					for (String paramName : piece.params.keySet()) {
						SpellParam param = piece.params.get(paramName);
						int x = left - 17;
						int y = top + 70 + i * 26;
						for (SpellParam.Side side : ImmutableSet.of(SpellParam.Side.TOP, SpellParam.Side.BOTTOM, SpellParam.Side.LEFT, SpellParam.Side.RIGHT, SpellParam.Side.OFF)) {
							if (!side.isEnabled() && !param.canDisable)
								continue;

							int xp = x + side.offx * 8;
							int yp = y + side.offy * 8;
							configWidget.configButtons.add(new GuiButtonSideConfig(this, selectedX, selectedY, i, paramName, side, xp, yp, button -> {
								if (!spectator) {
									pushState(true);
									GuiButtonSideConfig.performAction(this, selectedX, selectedY, paramName, side);
									onSpellChanged(false);
								}
							}));
						}
						i++;
					}
					configWidget.configButtons.forEach(this::addButton);
					configWidget.configEnabled = true;
					return;
				}
			}
		}
		configWidget.configEnabled = false;
	}

	@Override
	public boolean charTyped(char character, int keyCode) {
		if (programmer != null)
			spell = programmer.spell;
		if (spectator)
			return false;
		super.charTyped(character, keyCode);
		if (!commentEnabled && !spellNameField.isFocused()) {
			SpellPiece piece = null;
			if (selectedX != -1 && selectedY != -1) {
				piece = spell.grid.gridData[selectedX][selectedY];
				if (piece != null && piece.interceptKeystrokes()) {
					if (piece.onCharTyped(character, keyCode, false)) {
						pushState(true);
						piece.onCharTyped(character, keyCode, true);
						onSpellChanged(false);
						return true;
					}
				}
			}
		}
		return false;
	}


	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (programmer != null)
			spell = programmer.spell;
		if (spectator)
			return true;

		if (commentEnabled) {
			switch (keyCode) {
				case GLFW.GLFW_KEY_ENTER:
					closeComment(true);
					return true;
				case GLFW.GLFW_KEY_ESCAPE:
					closeComment(false);
					return true;
			}
		}
		SpellPiece piece = null;
		if (selectedX != -1 && selectedY != -1) {
			piece = spell.grid.gridData[selectedX][selectedY];
			if (piece != null && piece.interceptKeystrokes()) {
				if (piece.onKeyPressed(keyCode, scanCode, false)) {
					pushState(true);
					piece.onKeyPressed(keyCode, scanCode, true);
					onSpellChanged(false);
					return true;
				}
			}
		}
		if (spellNameField.isFocused() && keyCode == GLFW.GLFW_KEY_TAB) {
			spellNameField.setFocused2(false);
			setFocusedDefault(null);
			return true;
		}
		if (!spellNameField.isFocused() && !panelWidget.panelEnabled && !commentEnabled) {
			int param = -1;
			for (int i = 0; i < 4; i++)
				if (InputMappings.isKeyDown(minecraft.getWindow().getHandle(), GLFW.GLFW_KEY_1 + i))
					param = i;
			switch (keyCode) {
				case GLFW.GLFW_KEY_DELETE:
				case GLFW.GLFW_KEY_BACKSPACE:
					if (hasControlDown() && hasShiftDown()) {
						if (!spell.grid.isEmpty()) {
							pushState(true);
							spell = new Spell();
							spellNameField.setText("");
							onSpellChanged(false);
							return true;
						}
					}
					if (piece != null) {
						pushState(true);
						spell.grid.gridData[selectedX][selectedY] = null;
						onSpellChanged(false);
						return true;
					}
					break;
				case GLFW.GLFW_KEY_TAB:
					spellNameField.setFocused2(!spellNameField.isFocused());
					setFocusedDefault(spellNameField);
					return true;
				case GLFW.GLFW_KEY_UP:
					if (hasControlDown()) {
						if (hasShiftDown()) {
							pushState(true);
							spell.grid.mirrorVertical();
							onSpellChanged(false);
							return true;
						} else if (spell.grid.shift(SpellParam.Side.TOP, false)) {
							pushState(true);
							spell.grid.shift(SpellParam.Side.TOP, true);
							onSpellChanged(false);
							return true;
						}
					} else {
						if (!onSideButtonKeybind(piece, param, SpellParam.Side.TOP) && selectedY > 0) {
							selectedY--;
							onSelectedChanged();
							return true;
						}
					}
					break;
				case GLFW.GLFW_KEY_LEFT:
					if (hasControlDown()) {
						if (hasShiftDown()) {
							pushState(true);
							spell.grid.rotate(false);
							onSpellChanged(false);
							return true;
						} else if (spell.grid.shift(SpellParam.Side.LEFT, false)) {
							pushState(true);
							spell.grid.shift(SpellParam.Side.LEFT, true);
							onSpellChanged(false);
							return true;
						}
					} else {
						if (!onSideButtonKeybind(piece, param, SpellParam.Side.LEFT) && selectedX > 0) {
							selectedX--;
							onSelectedChanged();
							return true;
						}
					}
					break;
				case GLFW.GLFW_KEY_RIGHT:
					if (hasControlDown()) {
						if (hasShiftDown()) {
							pushState(true);
							spell.grid.rotate(true);
							onSpellChanged(false);
							return true;
						} else if (spell.grid.shift(SpellParam.Side.RIGHT, false)) {
							pushState(true);
							spell.grid.shift(SpellParam.Side.RIGHT, true);
							onSpellChanged(false);
							return true;
						}
					} else {
						if (!onSideButtonKeybind(piece, param, SpellParam.Side.RIGHT) && selectedX < SpellGrid.GRID_SIZE - 1) {
							selectedX++;
							onSelectedChanged();
							return true;
						}
					}
					break;
				case GLFW.GLFW_KEY_DOWN:
					if (hasControlDown()) {
						if (hasShiftDown()) {
							pushState(true);
							spell.grid.mirrorVertical();
							onSpellChanged(false);
							return true;
						} else if (spell.grid.shift(SpellParam.Side.BOTTOM, false)) {
							pushState(true);
							spell.grid.shift(SpellParam.Side.BOTTOM, true);
							onSpellChanged(false);
							return true;
						}
					} else {
						if (!onSideButtonKeybind(piece, param, SpellParam.Side.BOTTOM) && selectedY < SpellGrid.GRID_SIZE - 1) {
							selectedY++;
							onSelectedChanged();
							return true;
						}
					}
					break;
				case GLFW.GLFW_KEY_Z:
					if (hasControlDown() && !undoSteps.isEmpty()) {
						redoSteps.add(spell.copy());
						spell = undoSteps.pop();
						onSpellChanged(false);
						return true;
					}
					break;
				case GLFW.GLFW_KEY_Y:
					if (hasControlDown() && !redoSteps.isEmpty()) {
						pushState(false);
						spell = redoSteps.pop();
						onSpellChanged(false);
						return true;
					}
					break;
				case GLFW.GLFW_KEY_C:
					if (piece != null && hasControlDown()) {
						clipboard = piece.copy();
						return true;
					}

					break;
				case GLFW.GLFW_KEY_X:
					if (piece != null && hasControlDown()) {
						clipboard = piece.copy();
						pushState(true);
						spell.grid.gridData[selectedX][selectedY] = null;
						onSpellChanged(false);
						return true;
					}
					break;
				case GLFW.GLFW_KEY_V:
					if (SpellGrid.exists(selectedX, selectedY) && clipboard != null && hasControlDown()) {
						pushState(true);
						SpellPiece copy = clipboard.copy();
						copy.x = selectedX;
						copy.y = selectedY;
						spell.grid.gridData[selectedX][selectedY] = copy;
						onSpellChanged(false);
						return true;
					}
					break;
				case GLFW.GLFW_KEY_D:
					if (piece != null && hasControlDown()) {
						commentField.setVisible(true);
						commentField.setFocused2(true);
						commentField.setEnabled(true);
						spellNameField.setEnabled(false);
						commentField.setText(piece.comment);
						commentField.setFocused2(true);
						setFocusedDefault(commentField);
						commentEnabled = true;
						return true;
					}
					break;
				case GLFW.GLFW_KEY_G:
					if (hasControlDown()) {
						shareToReddit = false;
						if (hasShiftDown() && hasAltDown())
							takingScreenshot = true;
						return true;
					}
					break;
				case GLFW.GLFW_KEY_R:
					if (hasControlDown()) {
						shareToReddit = true;
						if (hasShiftDown() && hasAltDown())
							takingScreenshot = true;
						return true;
					}
					break;
				case GLFW.GLFW_KEY_ENTER:
					panelWidget.openPanel();
					return true;
			}
		}
		if (panelWidget.panelEnabled)
			panelWidget.keyPressed(keyCode, scanCode, modifiers);
		else if (keyCode == GLFW.GLFW_KEY_ESCAPE && !commentEnabled) {
			this.onClose();
			return true;
		}
		if (commentField.isFocused())
			commentField.keyPressed(keyCode, scanCode, modifiers);
		if (spellNameField.isFocused())
			spellNameField.keyPressed(keyCode, scanCode, modifiers);
		return false;
	}

	public boolean onSideButtonKeybind(SpellPiece piece, int param, SpellParam.Side side) {
		if (param > -1 && piece != null && piece.params.size() >= param) {
			for (Button button : configWidget.configButtons) {
				GuiButtonSideConfig config = ((GuiButtonSideConfig) button);
				if (config.matches(param, side)) {
					if (side != SpellParam.Side.OFF && piece.paramSides.get(piece.params.get(config.paramName)) == side) {
						side = SpellParam.Side.OFF;
						continue;
					}

					config.onPress();
					return true;
				}
			}
		}
		return side == SpellParam.Side.OFF;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (programmer != null)
			spell = programmer.spell;


		if (!commentEnabled) {
			spellNameField.mouseClicked(mouseX, mouseY, mouseButton);
			if (commentField.getVisible())
				commentField.mouseClicked(mouseX, mouseY, mouseButton);

			if (cursorX != -1 && cursorY != -1) {
				selectedX = cursorX;
				selectedY = cursorY;

				if (mouseButton == 1 && !spectator) {
					if (hasShiftDown()) {
						pushState(true);
						spell.grid.gridData[selectedX][selectedY] = null;
						onSpellChanged(false);
						return true;
					}
				}
				onSelectedChanged();
			}
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public boolean isSpectator() {
		return spectator;
	}

	private void closeComment(boolean save) {
		SpellPiece piece = null;
		if (selectedX != -1 && selectedY != -1)
			piece = spell.grid.gridData[selectedX][selectedY];

		if (save && piece != null) {
			String text = commentField.getText();
			pushState(true);
			piece.comment = text;
			onSpellChanged(false);
		}

		spellNameField.setEnabled(!spectator && (piece == null || !piece.interceptKeystrokes()));
		commentField.setFocused2(false);
		commentField.setVisible(false);
		commentField.setEnabled(false);
		setFocusedDefault(null);
		commentField.setText("");
		commentEnabled = false;
	}


	@Override
	public boolean shouldCloseOnEsc() {
		return !panelWidget.panelEnabled;
	}

	public List<Widget> getButtons() {
		return this.buttons;
	}
}

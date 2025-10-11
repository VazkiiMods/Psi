/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.SpellParam.Side;
import vazkii.psi.client.core.helper.SharingHelper;
import vazkii.psi.client.gui.button.GuiButtonHelp;
import vazkii.psi.client.gui.button.GuiButtonIO;
import vazkii.psi.client.gui.button.GuiButtonSideConfig;
import vazkii.psi.client.gui.widget.*;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageSpellModified;
import vazkii.psi.common.spell.SpellCompiler;
import vazkii.psi.common.spell.other.PieceConnector;

import java.util.*;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class GuiProgrammer extends Screen {

	public static final ResourceLocation texture = ResourceLocation.parse(LibResources.GUI_PROGRAMMER);
	public static final RenderType LAYER;
	public static SpellPiece clipboard = null;
	public static int selectedX, selectedY;

	static {
		RenderType.CompositeState glState = RenderType.CompositeState.builder()
				.setShaderState(new ShaderStateShard(GameRenderer::getPositionColorTexLightmapShader))
				.setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
				.setLightmapState(new RenderStateShard.LightmapStateShard(true))
				.setCullState(new RenderStateShard.CullStateShard(false))
				.setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
				.createCompositeState(false);
		LAYER = RenderType.create(PsiAPI.MOD_ID + ":" + LibBlockNames.PROGRAMMER, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 128, false, false, glState);
	}

	public final TileProgrammer programmer;
	public final Stack<Spell> undoSteps = new Stack<>();
	public final Stack<Spell> redoSteps = new Stack<>();
	public Spell spell;
	public List<Component> tooltip = new ArrayList<>();
	public Either<CompiledSpell, SpellCompilationException> compileResult;
	public int xSize, ySize, padLeft, padTop, left, top, gridLeft, gridTop;
	public int cursorX, cursorY;
	public boolean commentEnabled;

	public GuiButtonHelp helpButton;
	public EditBox spellNameField;
	public EditBox commentField;
	public PiecePanelWidget panelWidget;
	public SideConfigWidget configWidget;
	public SpellCostsWidget spellCostsWidget;
	public StatusWidget statusWidget;
	public TooltipFlag tooltipFlag;

	public boolean mouseMoved = false;
	public boolean takingScreenshot = false;
	public boolean shareToReddit = false;
	boolean spectator;

	public GuiProgrammer(TileProgrammer programmer) {
		this(programmer, programmer.spell);
	}

	public GuiProgrammer(TileProgrammer tile, Spell spell) {
		super(Component.empty());
		programmer = tile;
		this.spell = spell;
		compileResult = new SpellCompiler().compile(spell);
	}

	public static String convertIntToLetter(int i) {
		if(!ConfigHandler.CLIENT.changeGridCoordinatesToLetterNumber.get()) {
			return String.valueOf(i);
		}
		return String.valueOf((char) ((i % 27) + 64));
	}

	@Override
	public void mouseMoved(double xPos, double mouseY) {
		mouseMoved = true;
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
		tooltipFlag = getMinecraft().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;

		if(programmer == null) {
			spectator = false;
		} else {
			spectator = !programmer.playerLock.isEmpty() && getMinecraft().player != null && !programmer.playerLock.equals(getMinecraft().player.getName().getString());
		}

		statusWidget = addRenderableWidget(new StatusWidget(left - 48, top + 5, 48, 30, "", this));
		spellCostsWidget = addRenderableWidget(new SpellCostsWidget(left + xSize + 3, top + (takingScreenshot ? 40 : 20), 100, 126, "", this));
		panelWidget = addRenderableWidget(new PiecePanelWidget(0, 0, 100, 125, "", this));
		helpButton = addRenderableWidget(new GuiButtonHelp(left + xSize + 2, top + ySize - (spectator ? 32 : 48), this));
		configWidget = addRenderableWidget(new SideConfigWidget(left - 81, top + 55, 81, 115, this));

		spellNameField = addRenderableWidget(new CallbackTextFieldWidget(getMinecraft().font, left + xSize - 130, top + ySize - 14, 120, 10, button -> {
			spell.name = spellNameField.getValue();
			onSpellChanged(true);
		}));
		spellNameField.setBordered(false);
		spellNameField.setMaxLength(20);
		spellNameField.setEditable(!spectator);

		commentField = addRenderableWidget(new CallbackTextFieldWidget(getMinecraft().font, left, top + ySize / 2 - 10, xSize, 20, button -> {

		}));
		commentField.setEditable(false);
		commentField.setVisible(false);
		commentField.setMaxLength(500);

		panelWidget.searchField = addRenderableWidget(new CallbackTextFieldWidget(getMinecraft().font, 0, 0, 70, 10, button -> {
			panelWidget.page = 0;
			panelWidget.updatePanelButtons();
		}));
		panelWidget.searchField.setEditable(false);
		panelWidget.searchField.setVisible(false);
		panelWidget.searchField.setBordered(false);

		if(spell == null) {
			spell = new Spell();
		}
		if(programmer != null && programmer.spell == null) {
			programmer.spell = spell;
		}

		spellNameField.setValue(spell.name);

		panelWidget.populatePanelButtons();

		onSelectedChanged();

		/*
		 * Export button
		 */

		addRenderableWidget(new GuiButtonIO(left + xSize + 2, top + ySize - (spectator ? 16 : 32), true, this, button -> {
			if(hasShiftDown()) {
				CompoundTag cmp = new CompoundTag();
				if(spell != null) {
					spell.writeToNBT(cmp);
				}
				getMinecraft().keyboardHandler.setClipboard(cmp.toString());
			}
		}));

		/*
		 * Import button
		 */
		if(!spectator) {
			addRenderableWidget(new GuiButtonIO(left + xSize + 2, top + ySize - 16, false, this, button -> {
				if(hasShiftDown()) {
					String cb = getMinecraft().keyboardHandler.getClipboard();
					LocalPlayer player = Minecraft.getInstance().player;
					if(player == null) {
						return;
					}

					try {
						cb = cb.replaceAll("([^a-z0-9])\\d+:", "$1"); // backwards compatibility with pre 1.12 nbt json
						CompoundTag cmp = TagParser.parseTag(cb);
						if(cmp.contains(Spell.TAG_MODS_REQUIRED)) {
							ListTag mods = (ListTag) cmp.get(Spell.TAG_MODS_REQUIRED);
							if(mods == null) {
								return;
							}

							for(Tag mod : mods) {
								String modName = ((CompoundTag) mod).getString(Spell.TAG_MOD_NAME);
								if(!PsiAPI.SPELL_PIECE_REGISTRY.keySet().stream().map(ResourceLocation::getNamespace).collect(Collectors.toSet()).contains(modName)) {
									player.sendSystemMessage(Component.translatable("psimisc.modnotfound", modName).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
								}
								if(modName.equals("psi")) {
									boolean sendMessage = false;
									String modVersion = ((CompoundTag) mod).getString(Spell.TAG_MOD_VERSION);
									int[] versionEntry = Arrays.stream(modVersion.replaceFirst("^\\D+", "").split("\\D+")).mapToInt(Integer::parseInt).toArray();
									int[] currentVersion = Arrays.stream(ModList.get().getModContainerById("psi").get().getModInfo().getVersion().toString().replaceFirst("^\\D+", "").split("\\D+")).mapToInt(Integer::parseInt).toArray();
									for(int i = 0; i < versionEntry.length; i++) {
										if(versionEntry.length != currentVersion.length) {
											// Newer versions have four digits.
											break;
										}

										if(i + 1 > currentVersion.length) {
											sendMessage = true;
											break;
										}
										if(currentVersion[i] > versionEntry[i]) {
											break;
										} else if(currentVersion[i] < versionEntry[i]) {
											sendMessage = true;
											break;
										}
									}
									if(sendMessage) {
										player.sendSystemMessage(Component.translatable("psimisc.spellonnewerversion").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
									}
								}
							}
						} else {
							player.sendSystemMessage(Component.translatable("psimisc.spellmaynotfunctionasintended").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
						}
						spell = Spell.createFromNBT(cmp);
						if(spell == null) {
							return;
						}
						PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
						for(int i = 0; i < SpellGrid.GRID_SIZE; i++) {
							for(int j = 0; j < SpellGrid.GRID_SIZE; j++) {
								SpellPiece piece = spell.grid.gridData[i][j];
								if(piece != null) {
									Optional<Map.Entry<ResourceKey<Collection<Class<? extends SpellPiece>>>, Collection<Class<? extends SpellPiece>>>> advancementEntry = PsiAPI.ADVANCEMENT_GROUP_REGISTRY.entrySet().stream().filter((entry) -> entry.getValue().contains(piece.getClass())).findFirst();
									if(advancementEntry.isEmpty()) {
										continue;
									}

									if(!player.isCreative() && !data.isPieceGroupUnlocked(advancementEntry.get().getKey().location(), piece.registryKey)) {
										player.sendSystemMessage(Component.translatable("psimisc.missing_pieces").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
										return;
									}
								}
							}
						}

						pushState(true);
						spellNameField.setValue(spell.name);
						onSpellChanged(false);
					} catch (Exception t) {
						player.sendSystemMessage(Component.translatable("psimisc.malformed_json", t.getMessage()).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
						Psi.logger.error("Error importing spell from clipboard", t);
					}
				}
			}));
		}
	}

	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		if(programmer != null && programmer.getLevel() != null && getMinecraft().player != null && (programmer.getLevel().getBlockEntity(programmer.getBlockPos()) != programmer || !programmer.canPlayerInteract(getMinecraft().player))) {
			getMinecraft().setScreen(null);
			return;
		}

		String comment = "";
		int color = Psi.magical ? 0 : 0xFFFFFF;

		graphics.pose().pushPose();
		renderBackground(graphics, mouseX, mouseY, partialTicks);

		graphics.setColor(1F, 1F, 1F, 1F);
		graphics.blit(texture, left, top, 0, 0, xSize, ySize);

		//Currently selected piece
		SpellPiece piece = null;
		if(SpellGrid.exists(selectedX, selectedY)) {
			piece = spell.grid.gridData[selectedX][selectedY];
		}

		cursorX = (mouseX - gridLeft) / 18;
		cursorY = (mouseY - gridTop) / 18;
		if(panelWidget.panelEnabled || cursorX > 8 || cursorY > 8 || cursorX < 0 || cursorY < 0 || mouseX < gridLeft || mouseY < gridTop) {
			cursorX = -1;
			cursorY = -1;
		}

		graphics.pose().pushPose();
		tooltip.clear();
		graphics.pose().translate(gridLeft, gridTop, 0);
		MultiBufferSource.BufferSource buffers = MultiBufferSource.immediate(new ByteBufferBuilder(1536));
		spell.draw(graphics.pose(), buffers, 0xF000F0);
		buffers.endBatch();

		compileResult.right().ifPresent(ex -> {
			Pair<Integer, Integer> errorPos = ex.location;
			if(errorPos != null && errorPos.getRight() != -1 && errorPos.getLeft() != -1) {
				graphics.drawString(getMinecraft().font, "!!", errorPos.getLeft() * 18 + 12, errorPos.getRight() * 18 + 8, 0xFF0000, true);
			}
		});
		graphics.pose().popPose();
		graphics.setColor(1F, 1F, 1F, 1F);
		graphics.pose().translate(0, 0, 1);

		if(selectedX != -1 && selectedY != -1 && !takingScreenshot) {
			graphics.blit(texture, gridLeft + selectedX * 18, gridTop + selectedY * 18, 32, ySize, 16, 16);
		}

		if(hasAltDown()) {
			tooltip.clear();
			cursorX = selectedX;
			cursorY = selectedY;
			mouseX = gridLeft + cursorX * 18 + 10;
			mouseY = gridTop + cursorY * 18 + 8;
		}

		if(takingScreenshot) {
			Set<String> addons = spell.getPieceNamespaces().stream().filter(namespace -> !namespace.equals("psi")).collect(Collectors.toSet());
			if(!addons.isEmpty()) {
				String requiredAddons = ChatFormatting.GREEN + "Required Addons:";
				graphics.drawString(getMinecraft().font, requiredAddons, left - font.width(requiredAddons) - 5, top + 40, 0xFFFFFF, true);
				int i = 1;
				for(String addon : addons) {
					if(ModList.get().getModContainerById(addon).isPresent()) {
						String modName = ModList.get().getModContainerById(addon).get().getModInfo().getDisplayName();
						graphics.drawString(getMinecraft().font, "* " + modName, left - font.width(requiredAddons) - 5, top + 40 + 10 * i, 0xFFFFFF, true);
						i++;
					}
				}
			}
			String version = "Psi " + ModList.get().getModContainerById("psi").get().getModInfo().getVersion().toString();
			graphics.drawString(getMinecraft().font, version, left + xSize / 2f - font.width(version) / 2f, (float) top - 22, 0xFFFFFF, true);
		}

		SpellPiece pieceAtCursor = null;
		if(cursorX != -1 && cursorY != -1) {
			pieceAtCursor = spell.grid.gridData[cursorX][cursorY];
			if(pieceAtCursor != null) {
				pieceAtCursor.getTooltip(tooltip);
				comment = pieceAtCursor.comment;
			}

			if(!takingScreenshot) {
				if(cursorX == selectedX && cursorY == selectedY) {
					graphics.blit(texture, gridLeft + cursorX * 18, gridTop + cursorY * 18, 16, ySize, 8, 16);
				} else {
					graphics.blit(texture, gridLeft + cursorX * 18, gridTop + cursorY * 18, 16, ySize, 16, 16);
				}
			}
		}

		int topY = top - 22;

		if(!takingScreenshot) {
			int topYText = topY;
			if(spectator) {
				String spectator = ChatFormatting.RED + I18n.get("psimisc.spectator");
				graphics.drawString(getMinecraft().font, spectator, left + xSize / 2f - font.width(spectator) / 2f, topYText, 0xFFFFFF, true);
				topYText -= 10;
			}
			if(piece != null) {
				String pieceName = I18n.get(piece.getUnlocalizedName());
				graphics.drawString(getMinecraft().font, pieceName, left + xSize / 2f - font.width(pieceName) / 2f, topYText, 0xFFFFFF, true);
			}

			String coords;
			if(SpellGrid.exists(cursorX, cursorY)) {
				coords = I18n.get("psimisc.programmer_coords", convertIntToLetter(selectedX + 1), selectedY + 1, convertIntToLetter(cursorX + 1), cursorY + 1);
			} else {
				coords = I18n.get("psimisc.programmer_coords_no_cursor", convertIntToLetter(selectedX + 1), selectedY + 1);
			}
			graphics.drawString(getMinecraft().font, coords, left + 4, topY + ySize + 24, 0x44FFFFFF);
			String version = "Psi " + ModList.get().getModContainerById("psi").get().getModInfo().getVersion().toString();
			graphics.drawString(getMinecraft().font, version, left + xSize / 2f - font.width(version) / 2f, topY + ySize + 24 + font.wordWrapHeight(coords, font.width(coords)) + 5, 0x44FFFFFF, true);
		}

		if(Psi.magical) {
			graphics.drawString(getMinecraft().font, I18n.get("psimisc.name"), left + padLeft, spellNameField.getY() + 1, color);
		} else {
			graphics.drawString(getMinecraft().font, I18n.get("psimisc.name"), left + padLeft, spellNameField.getY() + 1, color, true);
		}

		//Add here comment
		if(commentEnabled) {
			String enterCommit = I18n.get("psimisc.enter_commit");
			graphics.drawString(getMinecraft().font, enterCommit, left + xSize / 2f - font.width(enterCommit) / 2f, commentField.getY() + 24, 0xFFFFFF, true);

			String semicolonLine = I18n.get("psimisc.semicolon_line");
			graphics.drawString(getMinecraft().font, semicolonLine, left + xSize / 2f - font.width(semicolonLine) / 2f, commentField.getY() + 34, 0xFFFFFF, true);
		}

		List<Component> legitTooltip = null;
		if(hasAltDown()) {
			legitTooltip = new ArrayList<>(tooltip);
		}

		if(hasAltDown()) {
			tooltip = legitTooltip;
		}

		for(Renderable renderable : this.renderables) {
			renderable.render(graphics, mouseX, mouseY, partialTicks);
		}

		if(!takingScreenshot && tooltip != null && !tooltip.isEmpty() && pieceAtCursor == null && mouseMoved) {
			graphics.renderTooltip(getMinecraft().font, tooltip, Optional.empty(), mouseX, mouseY);
		}
		if(!takingScreenshot && pieceAtCursor != null && mouseMoved) {
			if(tooltip != null && !tooltip.isEmpty()) {
				pieceAtCursor.drawTooltip(graphics, mouseX, mouseY, tooltip, this);
			}

			if(comment != null && !comment.isEmpty()) {
				List<Component> commentList = Arrays.stream(comment.split(";")).map(Component::literal).collect(Collectors.toList());
				pieceAtCursor.drawCommentText(graphics, mouseX, mouseY, commentList, this);
			}
		}

		graphics.pose().popPose();

		if(takingScreenshot) {
			String name = spell.name;
			String export = Spell.CODEC.encode(spell, JsonOps.INSTANCE, JsonOps.INSTANCE.mapBuilder()).toString();

			if(shareToReddit) {
				SharingHelper.uploadAndShare(name, export);
			} else {
				SharingHelper.uploadAndOpen(name, export);
			}

			takingScreenshot = false;
			shareToReddit = false;
		}

	}

	public void addButtons(List<Button> list) {
		list.forEach(this::addRenderableWidget);
	}

	public void pushState(boolean wipeRedo) {
		if(wipeRedo) {
			redoSteps.clear();
		}
		undoSteps.push(spell.copy());
		if(undoSteps.size() > 25) {
			undoSteps.removeFirst();
		}
	}

	public void onSpellChanged(boolean nameOnly) {
		if(programmer != null) {
			if(!spectator) {
				MessageSpellModified message = new MessageSpellModified(programmer.getBlockPos(), spell);
				MessageRegister.sendToServer(message);
			}

			programmer.spell = spell;
			programmer.onSpellChanged();
		}

		onSelectedChanged();

		if(!nameOnly || compileResult.right().filter(ex -> ex.getMessage().equals(SpellCompilationException.NO_NAME)).isPresent() || spell.name.isEmpty()) {
			compileResult = new SpellCompiler().compile(spell);
		}
	}

	public void onSelectedChanged() {
		renderables.removeAll(configWidget.configButtons);
		children().removeAll(configWidget.configButtons);
		configWidget.configButtons.clear();

		spellNameField.setEditable(!spectator);
		if(selectedX != -1 && selectedY != -1) {
			SpellPiece piece = spell.grid.gridData[selectedX][selectedY];
			if(piece != null) {
				boolean intercept = piece.interceptKeystrokes();
				spellNameField.setEditable(!spectator && !intercept);

				if(piece.hasConfig()) {
					int i = 0;
					for(String paramName : piece.params.keySet()) {
						SpellParam<?> param = piece.params.get(paramName);
						int x = left - 17;
						int y = top + 70 + i * 26;
						for(SpellParam.Side side : ImmutableSet.of(SpellParam.Side.TOP, SpellParam.Side.BOTTOM, SpellParam.Side.LEFT, SpellParam.Side.RIGHT, SpellParam.Side.OFF)) {
							if(!side.isEnabled() && !param.canDisable) {
								continue;
							}

							int xp = x + side.offx * 8;
							int yp = y + side.offy * 8;
							configWidget.configButtons.add(new GuiButtonSideConfig(this, selectedX, selectedY, i, paramName, side, xp, yp, button -> {
								if(!spectator) {
									pushState(true);
									GuiButtonSideConfig.performAction(this, selectedX, selectedY, paramName, side);
									onSpellChanged(false);
								}
							}));
						}
						i++;
					}
					configWidget.configButtons.forEach(this::addRenderableWidget);
					configWidget.configEnabled = true;
					return;
				}
			}
		}
		configWidget.configEnabled = false;
	}

	@Override
	public boolean charTyped(char character, int keyCode) {
		if(programmer != null) {
			spell = programmer.spell;
		}
		if(spectator) {
			return false;
		}
		super.charTyped(character, keyCode);
		if(!commentEnabled && !spellNameField.isFocused()) {
			SpellPiece piece;
			if(selectedX != -1 && selectedY != -1) {
				piece = spell.grid.gridData[selectedX][selectedY];
				if(piece != null && piece.interceptKeystrokes()) {
					if(piece.onCharTyped(character, keyCode, false)) {
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
		//TODO(Kamefrede): 1.20 find alternative to this
		//getMinecraft().keyboardHandler.setSendRepeatsToGui(true);
		if(programmer != null) {
			spell = programmer.spell;
		}
		if(keyCode == GLFW.GLFW_KEY_ESCAPE && shouldCloseOnEsc()) {
			this.onClose();
			return true;
		}
		if(spectator) {
			return true;
		}

		if(commentEnabled) {
			switch(keyCode) {
			case GLFW.GLFW_KEY_ENTER:
				closeComment(true);
				return true;
			case GLFW.GLFW_KEY_ESCAPE:
				closeComment(false);
				return true;
			}
		}
		SpellPiece piece = null;
		if(selectedX != -1 && selectedY != -1) {
			piece = spell.grid.gridData[selectedX][selectedY];
			if(piece != null && piece.interceptKeystrokes()) {
				if(piece.onKeyPressed(keyCode, scanCode, false)) {
					pushState(true);
					piece.onKeyPressed(keyCode, scanCode, true);
					onSpellChanged(false);
					return true;
				}
			}
		}
		if(spellNameField.isFocused() && keyCode == GLFW.GLFW_KEY_TAB) {
			spellNameField.setFocused(false);
			return true;
		}
		if(!spellNameField.isFocused() && !panelWidget.panelEnabled && !commentEnabled) {
			int param = -1;
			for(int i = 0; i < 4; i++) {
				if(InputConstants.isKeyDown(getMinecraft().getWindow().getWindow(), GLFW.GLFW_KEY_1 + i)) {
					param = i;
				}
			}
			switch(keyCode) {
			case GLFW.GLFW_KEY_DELETE:
			case GLFW.GLFW_KEY_BACKSPACE:
				if(hasControlDown() && hasShiftDown()) {
					if(!spell.grid.isEmpty()) {
						pushState(true);
						spell = new Spell();
						spellNameField.setValue("");
						onSpellChanged(false);
						return true;
					}
				}
				if(piece != null) {
					pushState(true);
					spell.grid.gridData[selectedX][selectedY] = null;
					onSpellChanged(false);
					return true;
				}
				break;
			case GLFW.GLFW_KEY_TAB:
				spellNameField.setFocused(!spellNameField.isFocused());
				setInitialFocus(spellNameField);
				return true;
			case GLFW.GLFW_KEY_UP:
				if(hasControlDown()) {
					if(hasShiftDown()) {
						pushState(true);
						spell.grid.mirrorVertical();
						onSpellChanged(false);
						return true;
					} else if(spell.grid.shift(SpellParam.Side.TOP, false)) {
						pushState(true);
						spell.grid.shift(SpellParam.Side.TOP, true);
						onSpellChanged(false);
						return true;
					}
				} else {
					if(!onSideButtonKeybind(piece, param, SpellParam.Side.TOP) && selectedY > 0) {
						selectedY--;
						onSelectedChanged();
						if(hasShiftDown() && spell.grid.gridData[selectedX][selectedY] == null) {
							PieceConnector connector = new PieceConnector(spell);
							connector.x = selectedX;
							connector.y = selectedY;
							connector.paramSides.put(connector.target, Side.BOTTOM);
							spell.grid.gridData[selectedX][selectedY] = connector;
							onSpellChanged(false);
						}
						return true;
					}
				}
				break;
			case GLFW.GLFW_KEY_LEFT:
				if(hasControlDown()) {
					if(hasShiftDown()) {
						pushState(true);
						spell.grid.rotate(false);
						onSpellChanged(false);
						return true;
					} else if(spell.grid.shift(SpellParam.Side.LEFT, false)) {
						pushState(true);
						spell.grid.shift(SpellParam.Side.LEFT, true);
						onSpellChanged(false);
						return true;
					}
				} else {
					if(!onSideButtonKeybind(piece, param, SpellParam.Side.LEFT) && selectedX > 0) {
						selectedX--;
						onSelectedChanged();
						if(hasShiftDown() && spell.grid.gridData[selectedX][selectedY] == null) {
							PieceConnector connector = new PieceConnector(spell);
							connector.x = selectedX;
							connector.y = selectedY;
							connector.paramSides.put(connector.target, Side.RIGHT);
							spell.grid.gridData[selectedX][selectedY] = connector;
							onSpellChanged(false);
						}
						return true;
					}
				}
				break;
			case GLFW.GLFW_KEY_RIGHT:
				if(hasControlDown()) {
					if(hasShiftDown()) {
						pushState(true);
						spell.grid.rotate(true);
						onSpellChanged(false);
						return true;
					} else if(spell.grid.shift(SpellParam.Side.RIGHT, false)) {
						pushState(true);
						spell.grid.shift(SpellParam.Side.RIGHT, true);
						onSpellChanged(false);
						return true;
					}
				} else {
					if(!onSideButtonKeybind(piece, param, SpellParam.Side.RIGHT) && selectedX < SpellGrid.GRID_SIZE - 1) {
						selectedX++;
						onSelectedChanged();
						if(hasShiftDown() && spell.grid.gridData[selectedX][selectedY] == null) {
							PieceConnector connector = new PieceConnector(spell);
							connector.x = selectedX;
							connector.y = selectedY;
							connector.paramSides.put(connector.target, Side.LEFT);
							spell.grid.gridData[selectedX][selectedY] = connector;
							onSpellChanged(false);
						}
						return true;
					}
				}
				break;
			case GLFW.GLFW_KEY_DOWN:
				if(hasControlDown()) {
					if(hasShiftDown()) {
						pushState(true);
						spell.grid.mirrorVertical();
						onSpellChanged(false);
						return true;
					} else if(spell.grid.shift(SpellParam.Side.BOTTOM, false)) {
						pushState(true);
						spell.grid.shift(SpellParam.Side.BOTTOM, true);
						onSpellChanged(false);
						return true;
					}
				} else {
					if(!onSideButtonKeybind(piece, param, SpellParam.Side.BOTTOM) && selectedY < SpellGrid.GRID_SIZE - 1) {
						selectedY++;
						onSelectedChanged();
						if(hasShiftDown() && spell.grid.gridData[selectedX][selectedY] == null) {
							PieceConnector connector = new PieceConnector(spell);
							connector.x = selectedX;
							connector.y = selectedY;
							connector.paramSides.put(connector.target, Side.TOP);
							spell.grid.gridData[selectedX][selectedY] = connector;
							onSpellChanged(false);
						}
						return true;
					}
				}
				break;
			case GLFW.GLFW_KEY_Z:
				if(hasControlDown() && !undoSteps.isEmpty()) {
					redoSteps.add(spell.copy());
					spell = undoSteps.pop();
					onSpellChanged(false);
					return true;
				}
				break;
			case GLFW.GLFW_KEY_Y:
				if(hasControlDown() && !redoSteps.isEmpty()) {
					pushState(false);
					spell = redoSteps.pop();
					onSpellChanged(false);
					return true;
				}
				break;
			case GLFW.GLFW_KEY_C:
				if(piece != null && hasControlDown()) {
					clipboard = piece.copy();
					return true;
				}

				break;
			case GLFW.GLFW_KEY_X:
				if(piece != null && hasControlDown()) {
					clipboard = piece.copy();
					pushState(true);
					spell.grid.gridData[selectedX][selectedY] = null;
					onSpellChanged(false);
					return true;
				}
				break;
			case GLFW.GLFW_KEY_V:
				if(SpellGrid.exists(selectedX, selectedY) && clipboard != null && hasControlDown()) {
					SpellPiece copy = clipboard.copy();
					copy.x = selectedX;
					copy.y = selectedY;
					pushState(true);
					spell.grid.gridData[selectedX][selectedY] = copy;
					spell.grid.gridData[selectedX][selectedY].isInGrid = true;
					onSpellChanged(false);
					return true;
				}
				break;
			case GLFW.GLFW_KEY_D:
				if(piece != null && hasControlDown()) {
					commentField.setVisible(true);
					commentField.setFocused(true);
					commentField.setEditable(true);
					spellNameField.setEditable(false);
					commentField.setValue(piece.comment);
					commentField.setFocused(true);
					setInitialFocus(commentField);
					commentEnabled = true;
					return true;
				}
				break;
			case GLFW.GLFW_KEY_G:
				if(hasControlDown()) {
					shareToReddit = false;
					if(hasShiftDown() && hasAltDown()) {
						takingScreenshot = true;
					}
					return true;
				}
				break;
			case GLFW.GLFW_KEY_R:
				if(hasControlDown()) {
					shareToReddit = true;
					if(hasShiftDown() && hasAltDown()) {
						takingScreenshot = true;
					}
					return true;
				}
				break;
			case GLFW.GLFW_KEY_ENTER:
				panelWidget.openPanel();
				return true;
			}
		}
		if(panelWidget.panelEnabled) {
			panelWidget.keyPressed(keyCode, scanCode, modifiers);
		}
		if(commentField.isFocused()) {
			commentField.keyPressed(keyCode, scanCode, modifiers);
		}
		if(spellNameField.isFocused()) {
			spellNameField.keyPressed(keyCode, scanCode, modifiers);
		}
		return false;
	}

	public boolean onSideButtonKeybind(SpellPiece piece, int param, SpellParam.Side side) {
		if(param > -1 && piece != null && piece.params.size() >= param) {
			for(Button button : configWidget.configButtons) {
				GuiButtonSideConfig config = ((GuiButtonSideConfig) button);
				if(config.matches(param, side)) {
					if(side != SpellParam.Side.OFF && piece.paramSides.get(piece.params.get(config.paramName)) == side) {
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
		if(programmer != null) {
			spell = programmer.spell;
		}

		if(!commentEnabled) {
			spellNameField.mouseClicked(mouseX, mouseY, mouseButton);
			if(commentField.isVisible()) {
				commentField.mouseClicked(mouseX, mouseY, mouseButton);
			}

			if(cursorX != -1 && cursorY != -1) {
				selectedX = cursorX;
				selectedY = cursorY;

				if(mouseButton == 1 && !spectator) {
					if(hasShiftDown()) {
						pushState(true);
						spell.grid.gridData[selectedX][selectedY] = null;
						onSpellChanged(false);
						return true;
					}
				}
				onSelectedChanged();
			}
		}

		for(GuiEventListener guieventlistener : this.children()) {
			if(guieventlistener.mouseClicked(mouseX, mouseY, mouseButton)) {
				if(mouseButton == 0) {
					this.setDragging(true);
				}

				return true;
			}
		}

		return false;
	}

	public boolean isSpectator() {
		return spectator;
	}

	private void closeComment(boolean save) {
		SpellPiece piece = null;
		if(selectedX != -1 && selectedY != -1) {
			piece = spell.grid.gridData[selectedX][selectedY];
		}

		if(save && piece != null) {
			String text = commentField.getValue();
			pushState(true);
			piece.comment = text;
			onSpellChanged(false);
		}

		spellNameField.setEditable(!spectator && (piece == null || !piece.interceptKeystrokes()));
		commentField.setFocused(false);
		commentField.setVisible(false);
		commentField.setEditable(false);
		commentField.setValue("");
		commentEnabled = false;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return !panelWidget.panelEnabled && !commentEnabled;
	}

	public List<Renderable> getButtons() {
		return this.renderables;
	}

	@Override
	public boolean isPauseScreen() {
		return ConfigHandler.CLIENT.pauseGameInProgrammer.get();
	}
}

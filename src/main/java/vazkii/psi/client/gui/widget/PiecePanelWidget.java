/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.glfw.GLFW;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.programmer.ProgrammerPopulateEvent;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.client.gui.button.GuiButtonPage;
import vazkii.psi.client.gui.button.GuiButtonSpellPiece;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.spell.constant.PieceConstantNumber;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PiecePanelWidget extends AbstractWidget implements GuiEventListener {

	public final GuiProgrammer parent;
	public boolean panelEnabled = false;
	public final List<Button> panelButtons = new ArrayList<>();
	public int panelCursor;
	public EditBox searchField;
	public int page = 0;
	private static final int PIECES_PER_PAGE = 25;
	public final List<GuiButtonSpellPiece> visibleButtons = new ArrayList<>();

	public PiecePanelWidget(int x, int y, int width, int height, String message, GuiProgrammer programmer) {
		super(x, y, width, height, Component.nullToEmpty(message));
		this.parent = programmer;
	}

	@Override
	public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float pTicks) {
		if(panelEnabled) {
			graphics.fill(getX(), getY(), getY() + width, getY() + height, 0x88000000);

			if(!visibleButtons.isEmpty()) {
				Button button = visibleButtons.get(Math.max(0, Math.min(panelCursor + (page * PIECES_PER_PAGE), visibleButtons.size() - 1)));
				int panelPieceX = button.getX();
				int panelPieceY = button.getY();
				graphics.fill(panelPieceX - 1, panelPieceY - 1, panelPieceX + 17, panelPieceY + 17, 0x559999FF);
			}

			graphics.setColor(1f, 1f, 1f, 1f);
			graphics.blit(GuiProgrammer.texture, searchField.getX() - 14, searchField.getY() - 2, 0, parent.ySize + 16, 12, 12);

			String s = Math.min(Math.max(getPageCount(), 1), page + 1) + "/" + Math.max(getPageCount(), 1);
			graphics.drawString(parent.getMinecraft().font, s, getX() + width / 2f - parent.getMinecraft().font.width(s) / 2f, getY() + height - 12, 0xFFFFFF, true);
		}
	}

	@Override
	public boolean mouseScrolled(double par1, double par2, double par3) {
		if(panelEnabled && par3 != 0) {
			int next = (int) (page - par3 / Math.abs(par3));
			if(next >= 0 && next < getPageCount()) {
				page = next;
				updatePanelButtons();
			}
		}
		return false;
	}

	@Override
	public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
		if(panelEnabled) {
			return searchField.charTyped(p_charTyped_1_, p_charTyped_2_);
		}
		return false;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if(panelEnabled) {
			switch(keyCode) {
			case GLFW.GLFW_KEY_ESCAPE:
				closePanel();
				return true;
			case GLFW.GLFW_KEY_ENTER:
				if(!visibleButtons.isEmpty()) {
					visibleButtons.get(panelCursor).onPress();
					return true;
				}
				return false;
			case GLFW.GLFW_KEY_TAB:
				if(!visibleButtons.isEmpty()) {
					int newCursor = panelCursor + (Screen.hasAltDown() ? -1 : 1);
					if(newCursor >= (Math.min(visibleButtons.size(), 25))) {
						panelCursor = 0;

						return true;
					}

					panelCursor = Math.max(0, Math.min(newCursor, (Math.min(visibleButtons.size(), 25)) - 1));
					return true;
				}
			}
			searchField.keyPressed(keyCode, scanCode, modifiers);
		}
		return false;
	}

	public int getPageCount() {
		return (visibleButtons.size() / PIECES_PER_PAGE) + 1;
	}

	public void populatePanelButtons() {
		PlayerDataHandler.PlayerData playerData = PlayerDataHandler.get(parent.getMinecraft().player);
		ProgrammerPopulateEvent event = new ProgrammerPopulateEvent(parent.getMinecraft().player, PsiAPI.getSpellPieceRegistry());
		List<SpellPiece> shownPieces = new ArrayList<>();
		MinecraftForge.EVENT_BUS.post(event);
		for(ResourceLocation key : event.getSpellPieceRegistry().keySet()) {
			Class<? extends SpellPiece> clazz = event.getSpellPieceRegistry().getOptional(key).get();
			ResourceLocation group = PsiAPI.getGroupForPiece(clazz);

			if(!parent.getMinecraft().player.isCreative() && (group == null || !playerData.isPieceGroupUnlocked(group, key))) {
				continue;
			}

			SpellPiece piece = SpellPiece.create(clazz, parent.spell);
			shownPieces.clear();
			piece.getShownPieces(shownPieces);
			for(SpellPiece shownPiece : shownPieces) {
				GuiButtonSpellPiece spellPieceButton = new GuiButtonSpellPiece(parent, shownPiece, 0, 0, button -> {
					if(parent.isSpectator()) {
						return;
					}
					parent.pushState(true);
					SpellPiece piece1 = ((GuiButtonSpellPiece) button).piece.copyFromSpell(parent.spell);
					if(piece1.getPieceType() == EnumPieceType.TRICK && parent.spellNameField.getValue().isEmpty()) {
						String pieceName = I18n.get(piece1.getUnlocalizedName());
						String patternStr = I18n.get("psimisc.trick_pattern");
						Pattern pattern = Pattern.compile(patternStr);
						Matcher matcher = pattern.matcher(pieceName);
						if(matcher.matches()) {
							String spellName = matcher.group(1);
							parent.spellNameField.setValue(spellName);
							parent.spell.name = spellName;
							parent.onSpellChanged(true);

						}
					}
					parent.spell.grid.gridData[GuiProgrammer.selectedX][GuiProgrammer.selectedY] = piece1;
					parent.spell.grid.gridData[GuiProgrammer.selectedX][GuiProgrammer.selectedY].isInGrid = true;
					parent.spell.grid.gridData[GuiProgrammer.selectedX][GuiProgrammer.selectedY].x = GuiProgrammer.selectedX;
					parent.spell.grid.gridData[GuiProgrammer.selectedX][GuiProgrammer.selectedY].y = GuiProgrammer.selectedY;
					parent.onSpellChanged(false);
					closePanel();
				});
				spellPieceButton.visible = false;
				spellPieceButton.active = false;
				panelButtons.add(spellPieceButton);
				visibleButtons.add(spellPieceButton);
			}

		}

		GuiButtonPage right = new GuiButtonPage(0, 0, true, parent, button -> {
			int max = getPageCount();
			int next = page + (((GuiButtonPage) button).right ? 1 : -1);

			if(next >= 0 && next < max) {
				page = next;
				updatePanelButtons();
			}
		});
		GuiButtonPage left = new GuiButtonPage(0, 0, false, parent, button -> {
			int max = getPageCount();
			int next = page + (((GuiButtonPage) button).right ? 1 : -1);

			if(next >= 0 && next < max) {
				page = next;
				updatePanelButtons();
			}
		});
		left.visible = false;
		left.active = false;
		right.visible = false;
		right.active = false;
		panelButtons.add(left);
		panelButtons.add(right);
		parent.addButtons(panelButtons);
	}

	public void updatePanelButtons() {
		panelCursor = 0;
		visibleButtons.clear();
		parent.getButtons().forEach(button -> {
			if(button instanceof GuiButtonPage || button instanceof GuiButtonSpellPiece) {
				((Button) button).active = false;
				((Button) button).visible = false;
			}
		});

		HashMap<Class<? extends SpellPiece>, Integer> pieceRankings = new HashMap<>();

		String text = searchField.getValue().toLowerCase(Locale.ROOT).trim();
		boolean noSearchTerms = text.isEmpty();

		parent.getButtons().forEach(button -> {
			if(button instanceof GuiButtonSpellPiece) {
				SpellPiece piece = ((GuiButtonSpellPiece) button).getPiece();

				if(noSearchTerms) {
					visibleButtons.add((GuiButtonSpellPiece) button);
				} else {
					int rank = ranking(text, piece);
					if(rank > 0) {
						pieceRankings.put(piece.getClass(), rank);
						visibleButtons.add((GuiButtonSpellPiece) button);
					}
				}
			} else if(button instanceof GuiButtonPage) {
				GuiButtonPage page = (GuiButtonPage) button;
				if(page.isRight() && this.page < getPageCount() - 1) {
					page.setX(getX() + width - 22);
					page.setY(getY() + height - 15);
					page.visible = true;
					page.active = true;

				} else if(!page.isRight() && this.page > 0) {
					page.setX(getX() + 4);
					page.setX(getY() + height - 15);
					page.visible = true;
					page.active = true;
				}
			}
		});

		Comparator<GuiButtonSpellPiece> comparator;

		if(noSearchTerms) {
			comparator = Comparator.comparing(GuiButtonSpellPiece::getPieceSortingName);
		} else {
			comparator = Comparator.comparingInt((p) -> -pieceRankings.get(p.getPiece().getClass()));
			comparator = comparator.thenComparing(GuiButtonSpellPiece::getPieceSortingName);
		}

		visibleButtons.sort(comparator);
		if((!text.isEmpty() && text.length() <= 5 && (text.matches("^-?\\d+(?:\\.\\d*)?") || text.matches("^-?\\d*(?:\\.\\d+)?")))) {
			GuiButtonSpellPiece constantPiece = (GuiButtonSpellPiece) parent.getButtons().stream().filter(el -> {
				if(el instanceof GuiButtonSpellPiece) {
					return ((GuiButtonSpellPiece) el).getPiece() instanceof PieceConstantNumber;
				}
				return false;
			}).findFirst().orElse(null);
			visibleButtons.remove(constantPiece);
			((PieceConstantNumber) constantPiece.getPiece()).valueStr = text;
			visibleButtons.add(0, constantPiece);
		}

		int start = page * PIECES_PER_PAGE;
		for(int i = start; i < visibleButtons.size(); i++) {
			int c = i - start;
			if(c >= PIECES_PER_PAGE) {
				break;
			}

			GuiButtonSpellPiece piece = visibleButtons.get(i);
			GuiButtonSpellPiece buttonSpellPiece = (GuiButtonSpellPiece) parent.getButtons().stream().filter(el -> el.equals(piece)).findFirst().orElse(null);
			buttonSpellPiece.setX(getX() + 5 + c % 5 * 18);
			buttonSpellPiece.setY(getY() + 20 + c / 5 * 18);
			buttonSpellPiece.visible = true;
			buttonSpellPiece.active = true;
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if(parent.cursorX != -1 && parent.cursorY != -1 && !parent.commentEnabled && !parent.isSpectator() && mouseButton == 1 && !panelEnabled) {
			openPanel();
			return true;
		}

		if(panelEnabled && (mouseX < getX() || mouseY < getY() || mouseX > getX() + width || mouseY > getY() + height) && !parent.isSpectator()) {
			closePanel();
			return true;
		}
		return false;
	}

	/**
	 * If a piece has a ranking of <= 0, it's excluded from the search.
	 */

	private int ranking(String token, SpellPiece p) {
		int rank = 0;
		String name = I18n.get(p.getUnlocalizedName()).toLowerCase(Locale.ROOT);
		String desc = I18n.get(p.getUnlocalizedDesc()).toLowerCase(Locale.ROOT);

		for(String nameToken : token.split("\\s+")) {
			if(nameToken.isEmpty()) {
				continue;
			}

			if(nameToken.startsWith("in:")) {
				String clippedToken = nameToken.substring(3);
				if(clippedToken.isEmpty()) {
					continue;
				}

				int maxRank = 0;
				for(SpellParam<?> param : p.params.values()) {
					String type = param.getRequiredTypeString().getString().toLowerCase(Locale.ROOT);
					maxRank = Math.max(maxRank, rankTextToken(type, clippedToken));
				}

				if(maxRank <= 0) {
					return 0;
				}
				rank += maxRank;
			} else if(nameToken.startsWith("out:")) {
				String clippedToken = nameToken.substring(4);
				if(clippedToken.isEmpty()) {
					continue;
				}

				String type = p.getEvaluationTypeString().getString().toLowerCase(Locale.ROOT);

				if(rankTextToken(type, clippedToken) <= 0) {
					return 0;
				}
				rank += rankTextToken(type, clippedToken);
			} else if(nameToken.startsWith("@")) {
				String clippedToken = nameToken.substring(1);
				if(clippedToken.isEmpty()) {
					continue;
				}

				String mod = PsiAPI.getSpellPieceKey(p.getClass()).getNamespace();
				if(mod != null) {
					int modRank = rankTextToken(mod, clippedToken);
					if(modRank <= 0) {
						return 0;
					}
					rank += modRank;
				} else {
					return 0;
				}
			} else {
				int nameRank = rankTextToken(name, nameToken);
				rank += nameRank;
				if(nameRank <= 0 && rankTextToken(desc, nameToken) <= 0) {
					return 0;
				} else {
					rank += rankTextToken(desc, nameToken) / 2;
				}
			}
		}

		return rank;
	}

	private int rankTextToken(String haystack, String token) {
		if(token.isEmpty()) {
			return 0;
		}

		if(token.startsWith("_")) {
			String clippedToken = token.substring(1);
			if(clippedToken.isEmpty()) {
				return 0;
			}
			if(haystack.endsWith(clippedToken)) {
				if(!Character.isLetterOrDigit(haystack.charAt(haystack.length() - clippedToken.length() - 1))) {
					return clippedToken.length() * 3 / 2;
				}
				return clippedToken.length();
			}
		} else if(token.endsWith("_")) {
			String clippedToken = token.substring(0, token.length() - 1);
			if(clippedToken.isEmpty()) {
				return 0;
			}
			if(haystack.startsWith(clippedToken)) {
				if(haystack.length() >= clippedToken.length() + 1 && !Character.isLetterOrDigit(haystack.charAt(clippedToken.length() + 1))) {
					return clippedToken.length() * 2;
				}
				return clippedToken.length();
			}
		} else {
			if(token.startsWith("has:")) {
				token = token.substring(4);
			}

			int idx = haystack.indexOf(token);
			if(idx >= 0) {
				int multiplier = 2;
				if(idx == 0 || !Character.isLetterOrDigit(haystack.charAt(idx - 1))) {
					multiplier += 2;
				}
				if(idx + token.length() + 1 >= haystack.length() ||
						!Character.isLetterOrDigit(haystack.charAt(idx + token.length() + 1))) {
					multiplier++;
				}

				return token.length() * multiplier / 2;
			}
		}

		return 0;
	}

	public void closePanel() {
		panelEnabled = false;
		parent.getButtons().forEach(button -> {
			if(button instanceof GuiButtonSpellPiece || button instanceof GuiButtonPage) {
				((Button) button).visible = false;
				((Button) button).active = false;
			}
		});
		searchField.visible = false;
		searchField.setEditable(false);
		searchField.setFocused(true);
		parent.setFocused(parent.statusWidget);
	}

	public void openPanel() {
		closePanel();
		panelEnabled = true;
		page = Math.min(page, Math.max(0, getPageCount() - 1));
		setX(parent.gridLeft + (GuiProgrammer.selectedX + 1) * 18);
		setY(parent.gridTop);

		searchField.setX(getX() + 18);
		searchField.setY(getY() + 4);
		searchField.setValue("");
		searchField.setVisible(true);
		searchField.active = true;
		searchField.setEditable(true);
		searchField.setFocused(true);
		parent.setFocused(searchField);
		updatePanelButtons();
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
		this.defaultButtonNarrationText(pNarrationElementOutput);
	}
}

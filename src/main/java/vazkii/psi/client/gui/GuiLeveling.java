/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [23/01/2016, 17:40:57 (GMT)]
 */
package vazkii.psi.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import vazkii.arl.util.RenderHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.PieceGroup;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.core.helper.TextHelper;
import vazkii.psi.client.gui.button.GuiButtonLearn;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PersistencyHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageLearnGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiLeveling extends Screen {
	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_LEVELING);

	public final List<ITextComponent> tooltip = new ArrayList<>();
	static float scrollDistanceGroup, scrollDistanceText;
	static int selected;

	ExtendedList listGroups;
	ExtendedList listText;

	int xSize, ySize, left, top;

	Spell spellWrapper;
	PlayerDataHandler.PlayerData data;
	List<PieceGroup> groups;
	final List<SpellPiece> drawPieces = new ArrayList<>();
	List<String> desc;
	final boolean ignoreIntroductionJump;

	public GuiLeveling() {
		this(false);
	}

	public GuiLeveling(boolean skip) {
		super(new StringTextComponent(""));
		ignoreIntroductionJump = skip;
	}

	@Override
	public void init() {
		spellWrapper = new Spell();
		data = PlayerDataHandler.get(minecraft.player);
		initGroupList();

		xSize = 256;
		ySize = 184;
		left = (width - xSize) / 2;
		top = (height - ySize) / 2;
		listGroups = new GroupListWidget(Minecraft.getInstance(), 120, 168, top + 8, top + 176, left + 8, 26);
		select(selected);
	}

	public void initGroupList() {
		groups = new ArrayList<>();

		String last = data.lastSpellGroup;
		PieceGroup lastGroup = PsiAPI.groupsForName.get(last);
		if (lastGroup != null)
			groups.add(lastGroup);

		ArrayList<PieceGroup> available = new ArrayList<>();
		ArrayList<PieceGroup> notAvailable = new ArrayList<>();
		ArrayList<PieceGroup> taken = new ArrayList<>();

		for (PieceGroup group : PsiAPI.groupsForName.values()) {
			if (group == lastGroup)
				continue;

			if(data.isPieceGroupUnlocked(group.name))
				taken.add(group);
			else if(group.isAvailable(data))
				available.add(group);
			else notAvailable.add(group);
		}

		Collections.sort(available);
		Collections.sort(notAvailable);
		Collections.sort(taken);

		groups.addAll(available);
		groups.addAll(notAvailable);
		groups.addAll(taken);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		int level = data.getLevel();
		int points = data.getLevelPoints();
		if (!ignoreIntroductionJump && ((level == 0 && points == 0) || (level == 1 && points == 1 && PersistencyHandler.persistentLevel > 1))) {
			minecraft.displayGuiScreen(new GuiIntroduction(level == 1));
			return;
		}

		tooltip.clear();
		renderBackground();

		RenderSystem.color3f(1F, 1F, 1F);
		minecraft.getTextureManager().bindTexture(texture);
		blit(left, top, 0, 0, xSize, ySize);

		super.render(mouseX, mouseY, partialTicks);

		PieceGroup group = groups.get(selected);

		if (group != null) {
			boolean taken = data.isPieceGroupUnlocked(group.name);
			group.isAvailable(data);

			int lines = (drawPieces.size() - 1) / 6 + 1;

			for(int i = 0; i < drawPieces.size(); i++) {
				int x = left + 134 + i % 6 * 18;
				int y = top + 160 + i / 6 * 18 - (lines - 1) * 18;

				if (i == 0)
					blit(x - 1, y - 1, 0, ySize, 18, 18);

				SpellPiece piece = drawPieces.get(i);
				if (mouseX > x && mouseY > y && mouseX < x + 16 && mouseY < y + 16)
					piece.getTooltip(tooltip);

				RenderSystem.translatef(x, y, 0);
				piece.draw();
				RenderSystem.translatef(-x, -y, 0);
			}

			minecraft.fontRenderer.drawStringWithShadow(I18n.format(group.getUnlocalizedName()), left + 134, top + 12, 0xFFFFFF);

			if (taken) {
				if (listText != null) {
					boolean unicode = font.getBidiFlag();
					font.setBidiFlag(true);
					listText.render(mouseX, mouseY, partialTicks);
					font.setBidiFlag(unicode);
				}
			} else {
				int colorOff = 0x777777;
				int colorOn = 0x77FF77;

				minecraft.fontRenderer.drawStringWithShadow(I18n.format("psimisc.requirements"), left + 134, top + 32, 0xFFFFFF);
				minecraft.fontRenderer.drawString(I18n.format("psimisc.levelDisplay", group.levelRequirement), left + 138, top + 42, data.getLevel() >= group.levelRequirement ? colorOn : colorOff);
				int i = 0;
				for (String s : group.requirements) {
					PieceGroup reqGroup = PsiAPI.groupsForName.get(s);
					minecraft.fontRenderer.drawString(I18n.format(reqGroup.getUnlocalizedName()), left + 138, top + 52 + i * 10, data.isPieceGroupUnlocked(s) ? colorOn : colorOff);

					i++;
				}
			}
		}

		if (LibMisc.BETA_TESTING) {
			String betaTest = I18n.format("psimisc.wip");
			minecraft.fontRenderer.drawStringWithShadow(betaTest, left + xSize / 2f - font.getStringWidth(betaTest) / 2f, top - 12, 0xFFFFFFFF);
		}

		String key = "psimisc.levelInfo";
		if (minecraft.player.isCreative())
			key = "psimisc.levelInfoCreative";
		String s = I18n.format(key, data.getLevel(), data.getLevelPoints());
		font.drawStringWithShadow(s, left + 4, top + ySize + 2, 0xFFFFFF);

		listGroups.render(mouseX, mouseY, partialTicks);

		if (!tooltip.isEmpty()) {
			List<String> vazkiiWhy = new ArrayList<>();
			for (ITextComponent component : tooltip)
				vazkiiWhy.add(component.getString());
			RenderHelper.renderTooltip(mouseX, mouseY, vazkiiWhy);
		}

	}

	public void select(int i) {
		drawPieces.clear();
		PieceGroup group = groups.get(selected);
		if(group != null) {
			addToDrawList(group.mainPiece);
			for (Class<? extends SpellPiece> clazz : group.pieces)
				if (clazz != group.mainPiece)
					addToDrawList(clazz);

			boolean taken = data.isPieceGroupUnlocked(group.name);
			boolean available = taken || group.isAvailable(data);

			children.removeAll(buttons);
			buttons.clear();
			if (!taken && available && data.getLevelPoints() > 0)
				addButton(new GuiButtonLearn(left + xSize, top + ySize - 30, this, button -> {
					PieceGroup group1 = groups.get(selected);
					if (group1 != null) {
						MessageRegister.HANDLER.sendToServer(new MessageLearnGroup(group1.name));
						data.unlockPieceGroup(group1.name);
						Psi.proxy.savePersistency();
						initGroupList();
						select(0);
					}
				}));

			int lines = (drawPieces.size() - 1) / 6 + 1;
			if(taken) {
				desc = TextHelper.renderText(left + 2, 0, 110, group.getUnlocalizedDesc(), false, false);
				listText = new BigTextListWidget(minecraft, 120, 168, top + 23, top + 174 - lines * 18, left + 130, 10);
			} else listText = null;
		}
	}

	public void addToDrawList(Class<? extends SpellPiece> clazz) {
		if (clazz == null)
			return;

		SpellPiece piece = SpellPiece.create(clazz, spellWrapper);
		drawPieces.add(piece);
	}

	class BigTextListWidget extends ExtendedList<BigTextListWidget.BigTextEntry> {

		public BigTextListWidget(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight) {
			super(client, width, height, top, bottom, entryHeight);
			setLeftPos(left);
			setScrollAmount(scrollDistanceText);
		}

		class BigTextEntry extends ExtendedList.AbstractListEntry<BigTextEntry> {
			@Override
			public void render(int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
				font.drawString(desc.get(entryIdx), left + 4, top, 0xFFFFFF);
			}
		}
	}


	class GroupListWidget extends ExtendedList<GroupListWidget.ListEntry> {


		public GroupListWidget(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight) {
			super(client, width, height, top, bottom, entryHeight);
			setScrollAmount(scrollDistanceGroup);
			setLeftPos(left);
		}

		@Override
		public boolean mouseClicked(double par1, double par2, int par3) {
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i) == getEntryAtPosition(par1, par2)) {
					selected = i;
					scrollDistanceText = 0F;
					select(i);
				}
			}
			return super.mouseClicked(par1, par2, par3);
		}

		@Override
		protected boolean isSelectedItem(int p_isSelectedItem_1_) {
			return false;
		}

		class ListEntry extends ExtendedList.AbstractListEntry<ListEntry> {

			private final GuiLeveling parent;

			public ListEntry(GuiLeveling gui) {
				this.parent = gui;
			}

			@Override
			public void render(int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
				PieceGroup group = parent.groups.get(entryIdx);

				if (entryIdx % 2 == 0)
					fill(left, top, left + width, top + entryHeight, 0x1A000000);
				if (entryIdx == selected)
					fill(left, top, left + width, top + entryHeight, 0x44005555);

				boolean taken = data.isPieceGroupUnlocked(group.name);
				boolean available = taken || group.isAvailable(data);
				boolean current = group.name.equals(data.lastSpellGroup);
				int color = 0x777777;

				if (current)
					color = 0xFFFF77;
				else if (taken)
					color = 0x77FF77;
				else if (available)
					color = 0xFFFFFF;

				font.drawString(I18n.format(group.getUnlocalizedName()), left + 3, top + 4, color);
				font.drawString(I18n.format("psimisc.levelDisplay", group.levelRequirement), left + 3, top + 14, color);
			}
		}


	}

}

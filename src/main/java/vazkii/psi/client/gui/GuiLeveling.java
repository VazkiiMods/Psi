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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.GuiScrollingList;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.util.RenderHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.PieceGroup;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.core.helper.TextHelper;
import vazkii.psi.client.gui.button.GuiButtonLearn;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PersistencyHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.message.MessageLearnGroup;

public class GuiLeveling extends GuiScreen {

	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_LEVELING);

	public List<String> tooltip = new ArrayList();

	GuiScrollingList listGroups;
	GuiScrollingList listText;

	int xSize, ySize, left, top;

	Spell spellWrapper;
	PlayerData data;
	List<PieceGroup> groups;
	List<SpellPiece> drawPieces = new ArrayList();
	int selected;
	List<String> desc;
	boolean ignoreIntroductionJump;
	
	public GuiLeveling() {
		this(false);
	}
	
	public GuiLeveling(boolean skip) {
		ignoreIntroductionJump = skip;
		
	}

	@Override
	public void initGui() {
		spellWrapper = new Spell();
		data = PlayerDataHandler.get(mc.player);
		initGroupList();

		xSize = 256;
		ySize = 184;
		left = (width - xSize) / 2;
		top = (height - ySize) / 2;
		listGroups = new GroupList(mc, 120, 168, top + 8, top + 176, left + 8, 26, width, height);
		select(selected);
	}

	public void initGroupList() {
		groups = new ArrayList();

		String last = data.lastSpellGroup;
		PieceGroup lastGroup = PsiAPI.groupsForName.get(last);
		if(lastGroup != null)
			groups.add(lastGroup);

		ArrayList available = new ArrayList();
		ArrayList notAvailable = new ArrayList();
		ArrayList taken = new ArrayList();

		for(PieceGroup group : PsiAPI.groupsForName.values()) {
			if(group == lastGroup)
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
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int level = data.getLevel();
		int points = data.getLevelPoints();
		if(!ignoreIntroductionJump && ((level == 0 && points == 0) || (level == 1 && points == 1 && PersistencyHandler.persistentLevel > 1))) {
			mc.displayGuiScreen(new GuiIntroduction(level == 1));
			return;
		}

		tooltip.clear();
		drawDefaultBackground();

		GlStateManager.color(1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);

		super.drawScreen(mouseX, mouseY, partialTicks);

		PieceGroup group = groups.get(selected);
		boolean taken = data.isPieceGroupUnlocked(group.name);
		group.isAvailable(data);

		if(group != null) {
			int lines = (drawPieces.size() - 1) / 6 + 1;

			for(int i = 0; i < drawPieces.size(); i++) {
				int x = left + 134 + i % 6 * 18;
				int y = top + 160 + i / 6 * 18 - (lines - 1) * 18;

				if(i == 0)
					drawTexturedModalRect(x - 1, y - 1, 0, ySize, 18, 18);

				SpellPiece piece = drawPieces.get(i);
				if(mouseX > x && mouseY > y && mouseX < x + 16 && mouseY < y + 16)
					piece.getTooltip(tooltip);

				GlStateManager.translate(x, y, 0);
				piece.draw();
				GlStateManager.translate(-x, -y, 0);
			}

			mc.fontRenderer.drawStringWithShadow(I18n.translateToLocal(group.getUnlocalizedName()), left + 134, top + 12, 0xFFFFFF);

			if(taken) {
				if(listText != null) {
					boolean unicode = fontRenderer.getUnicodeFlag();
					fontRenderer.setUnicodeFlag(true);
					listText.drawScreen(mouseX, mouseY, partialTicks);
					fontRenderer.setUnicodeFlag(unicode);
				}
			} else {
				int colorOff = 0x777777;
				int colorOn = 0x77FF77;

				mc.fontRenderer.drawStringWithShadow(I18n.translateToLocal("psimisc.requirements"), left + 134, top + 32, 0xFFFFFF);
				mc.fontRenderer.drawString(String.format(I18n.translateToLocal("psimisc.levelDisplay"), group.levelRequirement), left + 138, top + 42, data.getLevel() >= group.levelRequirement ? colorOn : colorOff);
				int i = 0;
				for(String s : group.requirements) {
					PieceGroup reqGroup = PsiAPI.groupsForName.get(s);
					mc.fontRenderer.drawString(I18n.translateToLocal(reqGroup.getUnlocalizedName()), left + 138, top + 52 + i * 10, data.isPieceGroupUnlocked(s) ? colorOn : colorOff);

					i++;
				}
			}
		}

		if(LibMisc.BETA_TESTING) {
			String betaTest = I18n.translateToLocal("psimisc.wip");
			mc.fontRenderer.drawStringWithShadow(betaTest, left + xSize / 2 - mc.fontRenderer.getStringWidth(betaTest) / 2, top - 12, 0xFFFFFFFF);
		}

		String key = "psimisc.levelInfo";
		if(mc.player.capabilities.isCreativeMode)
			key = "psimisc.levelInfoCreative";
		String s = String.format(I18n.translateToLocal(key), data.getLevel(), data.getLevelPoints());
		mc.fontRenderer.drawStringWithShadow(s, left + 4, top + ySize + 2, 0xFFFFFF);

		listGroups.drawScreen(mouseX, mouseY, partialTicks);

		if(!tooltip.isEmpty())
			RenderHelper.renderTooltip(mouseX, mouseY, tooltip);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);

		PieceGroup group = groups.get(selected);
		if(group != null) {
			NetworkHandler.INSTANCE.sendToServer(new MessageLearnGroup(group.name));
			data.unlockPieceGroup(group.name);
			Psi.proxy.savePersistency();
			initGroupList();
			select(0);
		}
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		int mouseX = Mouse.getEventX() * width / mc.displayWidth;
		int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;

		super.handleMouseInput();
		if(listText != null)
			listText.handleMouseInput(mouseX, mouseY);
		listGroups.handleMouseInput(mouseX, mouseY);
	}

	public void select(int i) {
		drawPieces.clear();
		PieceGroup group = groups.get(selected);
		if(group != null) {
			addToDrawList(group.mainPiece);
			for(Class clazz : group.pieces)
				if(clazz != group.mainPiece)
					addToDrawList(clazz);
		}

		boolean taken = data.isPieceGroupUnlocked(group.name);
		boolean available = taken || group.isAvailable(data);

		buttonList.clear();
		if(!taken && available && data.getLevelPoints() > 0)
			buttonList.add(new GuiButtonLearn(this, left + xSize, top + ySize - 30));

		int lines = (drawPieces.size() - 1) / 6 + 1;
		if(taken) {
			desc = TextHelper.renderText(left + 2, 0, 110, group.getUnlocalizedDesc(), false, false);
			listText = new BigTextList(mc, 120, 168, top + 23, top + 174 - lines * 18, left + 130, 10, width, height);
		} else listText = null;
	}

	public void addToDrawList(Class<? extends SpellPiece> clazz) {
		if(clazz == null)
			return;

		SpellPiece piece = SpellPiece.create(clazz, spellWrapper);
		drawPieces.add(piece);
	}

	private class BigTextList extends GuiScrollingList {

		public BigTextList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight) {
			super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
		}

		@Override
		protected int getSize() {
			return desc.size();
		}

		@Override
		protected void elementClicked(int index, boolean doubleClick) {
			// NO-OP
		}

		@Override
		protected boolean isSelected(int index) {
			return false;
		}

		@Override
		protected void drawBackground() {
			// NO-OP
		}

		@Override
		protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
			mc.fontRenderer.drawString(desc.get(slotIdx), left + 4, slotTop, 0xFFFFFF);
		}

		@Override
		protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
			// NO-OP
		}

	}

	private class GroupList extends GuiScrollingList {

		public GroupList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight) {
			super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
			func_27258_a(false); // highlightSelected = false
		}

		@Override
		protected int getSize() {
			return groups.size();
		}

		@Override
		protected void elementClicked(int index, boolean doubleClick) {
			selected = index;
			select(index);
		}

		@Override
		protected boolean isSelected(int index) {
			return selected == index;
		}

		@Override
		protected void drawBackground() {
			// NO-OP
		}

		@Override
		protected void drawSlot(int slotId, int slotRight, int slotTop, int slotBuffer, Tessellator tess) {
			PieceGroup group = groups.get(slotId);

			if(slotId % 2 == 0)
				drawRect(left, slotTop, left + width, slotTop + slotHeight, 0x1A000000);
			if(isSelected(slotId))
				drawRect(left, slotTop, left + width, slotTop + slotHeight, 0x44005555);

			boolean taken = data.isPieceGroupUnlocked(group.name);
			boolean available = taken || group.isAvailable(data);
			boolean current = group.name.equals(data.lastSpellGroup);
			int color = 0x777777;
			
			if(current)
				color = 0xFFFF77;
			else if(taken)
				color = 0x77FF77;
			else if(available)
				color = 0xFFFFFF;

			mc.fontRenderer.drawString(I18n.translateToLocal(group.getUnlocalizedName()), left + 3, slotTop + 4, color);
			mc.fontRenderer.drawString(String.format(I18n.translateToLocal("psimisc.levelDisplay"), group.levelRequirement), left + 3, slotTop + 14, color);
		}

		@Override
		protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
			// NO-OP
		}

	}


}

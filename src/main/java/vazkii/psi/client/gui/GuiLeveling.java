/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [23/01/2016, 17:40:57 (GMT)]
 */
package vazkii.psi.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.GuiScrollingList;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.PieceGroup;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.core.helper.RenderHelper;
import vazkii.psi.client.gui.button.GuiButtonLearn;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.NetworkHandler;
import vazkii.psi.common.network.message.MessageLearnGroup;

public class GuiLeveling extends GuiScreen {
	
	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_LEVELING);
	
	public List<String> tooltip = new ArrayList();
	
	GuiScrollingList listGroups;
	int xSize, ySize, left, top;
	
	Spell spellWrapper;
	PlayerData data;
	List<PieceGroup> groups;
	List<SpellPiece> drawPieces = new ArrayList();
	int selected;
	
	@Override
	public void initGui() {
		spellWrapper = new Spell();
		data = PlayerDataHandler.get(mc.thePlayer);
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
		
		ArrayList available = new ArrayList();
		ArrayList notAvailable = new ArrayList();
		ArrayList taken = new ArrayList();
		
		for(PieceGroup group : PsiAPI.groupsForName.values()) {
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
		tooltip.clear();
		drawDefaultBackground();

		GlStateManager.color(1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		PieceGroup group = groups.get(selected);
		boolean taken = data.isPieceGroupUnlocked(group.name);
		boolean available = taken || group.isAvailable(data);
		
		if(group != null) {
			int lines = (drawPieces.size() - 1) / 6 + 1;
			
			for(int i = 0; i < drawPieces.size(); i++) {
				int x = left + 134 + (i % 6) * 18;
				int y = top + 160 + (i / 6) * 18 - (lines - 1) * 18;
				
				if(i == 0)
					drawTexturedModalRect(x - 1, y - 1, 0, ySize, 18, 18);
				
				SpellPiece piece = drawPieces.get(i);
				if(mouseX > x && mouseY > y && mouseX < x + 16 && mouseY < y + 16)
					piece.getTooltip(tooltip);
				
				GlStateManager.translate(x, y, 0);
				piece.draw();
				GlStateManager.translate(-x, -y, 0);
			}
			
			mc.fontRendererObj.drawStringWithShadow(StatCollector.translateToLocal(group.getUnlocalizedName()), left + 134, top + 12, 0xFFFFFF);
		}
		
		String key = "psimisc.levelInfo";
		if(mc.thePlayer.capabilities.isCreativeMode)
			key = "psimisc.levelInfoCreative";
		String s = String.format(StatCollector.translateToLocal(key), data.getLevel(), data.getLevelPoints()); 
		mc.fontRendererObj.drawStringWithShadow(s, left + 4, top + ySize + 2, 0xFFFFFF);
		
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
			initGroupList();
			
			buttonList.clear();
		}
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
	}
	
	public void addToDrawList(Class<? extends SpellPiece> clazz) {
		if(clazz == null)
			return;
		
		SpellPiece piece = SpellPiece.create(clazz, spellWrapper);
		drawPieces.add(piece);
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
			int color = 0x777777;
			if(available)
				color = 0xFFFFFF;
			if(taken)
				color = 0x77FF77;
			
			mc.fontRendererObj.drawString(StatCollector.translateToLocal(group.getUnlocalizedName()), left + 3, slotTop + 4, color);
			mc.fontRendererObj.drawString(String.format(StatCollector.translateToLocal("psimisc.levelDisplay"), group.levelRequirement), left + 3, slotTop + 14, color);
		}
		
		@Override
		protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
			// NO-OP
		}
		
	}
	
	
}

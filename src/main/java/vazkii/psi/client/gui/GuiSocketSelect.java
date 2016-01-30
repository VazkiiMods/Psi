/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [13/01/2016, 23:05:26 (GMT)]
 */
package vazkii.psi.client.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.client.core.handler.KeybindHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.NetworkHandler;
import vazkii.psi.common.network.message.MessageChangeSocketableSlot;

public class GuiSocketSelect extends GuiScreen {

	private static final ResourceLocation[] signs = new ResourceLocation[] {
		new ResourceLocation(String.format(LibResources.GUI_SIGN, 0)),
		new ResourceLocation(String.format(LibResources.GUI_SIGN, 1)),
		new ResourceLocation(String.format(LibResources.GUI_SIGN, 2)),
		new ResourceLocation(String.format(LibResources.GUI_SIGN, 3)),
		new ResourceLocation(String.format(LibResources.GUI_SIGN, 4)),
		new ResourceLocation(String.format(LibResources.GUI_SIGN, 5)),
		new ResourceLocation(String.format(LibResources.GUI_SIGN, 6)),
		new ResourceLocation(String.format(LibResources.GUI_SIGN, 7)),
		new ResourceLocation(String.format(LibResources.GUI_SIGN, 8)),
		new ResourceLocation(String.format(LibResources.GUI_SIGN, 9)),
		new ResourceLocation(String.format(LibResources.GUI_SIGN, 10)),
		new ResourceLocation(String.format(LibResources.GUI_SIGN, 11)),
		new ResourceLocation(String.format(LibResources.GUI_SIGN, 12))
	};
	
	int timeIn = 0;
	int slotSelected = -1;
	ItemStack socketableStack;
	ISocketable socketable;
	List<Integer> slots;
	
	public GuiSocketSelect(ItemStack stack) {
		socketableStack = stack;
		
		if(stack != null && stack.getItem() instanceof ISocketable)
			socketable = (ISocketable) stack.getItem();
		else throw new IllegalArgumentException("Stack must be non-null and ISocketable");
		
		slots = new ArrayList();
		for(int i = 0; i < ISocketable.MAX_SLOTS; i++)
			if(socketable.showSlotInRadialMenu(stack, i))
				slots.add(i);
	}
	
	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		super.drawScreen(mx, my, partialTicks);
		
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();

		int x = width / 2;
		int y = height / 2;
		int maxRadius = 80;

		boolean mouseIn = true;
		float angle = mouseAngle(x, y, mx, my);
		
		int highlight = 5;

		GlStateManager.enableBlend();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		int segments = slots.size();
		float totalDeg = 0;
		float degPer = 360F / segments;
		
		List<int[]> stringPositions = new ArrayList();
		
		ItemStack cadStack = PsiAPI.getPlayerCAD(Minecraft.getMinecraft().thePlayer);
		slotSelected = -1;
		
		for(int seg = 0; seg < segments; seg++) {
			boolean mouseInSector = mouseIn && angle > totalDeg && angle < totalDeg + degPer;
			float radius = Math.max(0F, Math.min(((float) timeIn + partialTicks - (seg * 6F / segments)) * 40F, maxRadius));
			
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);

			float gs = 0.25F;
			if(seg % 2 == 0)
				gs += 0.1F;
			float r = gs;
			float g = gs;
			float b = gs;
			float a = 0.4F;
			if(mouseInSector) {
				slotSelected = seg;
				
				if(cadStack != null) {
					ICAD cad = (ICAD) cadStack.getItem();
					Color color = new Color(cad.getSpellColor(cadStack));
					r = (float) color.getRed() / 255F;
					g = (float) color.getGreen() / 255F;
					b = (float) color.getBlue() / 255F;
				}
			}
				
			GlStateManager.color(r, g, b, a);
			GL11.glVertex2i(x, y);

			for(float i = degPer; i >= 0; i--) {
				float rad = (float) ((i + totalDeg) / 180F * Math.PI);
				double xp = x + Math.cos(rad) * radius;
				double yp = y + Math.sin(rad) * radius;
				if(i == (int) (degPer / 2))
					stringPositions.add(new int[] { seg, (int) xp, (int) yp, (mouseInSector ? 'n' : 'r') });
				
				GL11.glVertex2d(xp, yp);
			}
			totalDeg += degPer;

			GL11.glVertex2i(x, y);
			GL11.glEnd();

			if(mouseInSector)
				radius -= highlight;
		}
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.enableTexture2D();
		
		for(int[] pos : stringPositions) {
			int slot = slots.get(pos[0]);
			int xp = pos[1];
			int yp = pos[2];
			char c = (char) pos[3];
			
			ItemStack stack = socketable.getBulletInSocket(socketableStack, slot);
			if(stack != null) {
				int xsp = xp - 4;
				int ysp = yp;
				String name = "\u00a7" + c + stack.getDisplayName();
				int width = fontRendererObj.getStringWidth(name);
				
				double mod = 0.6;
				int xdp = (int) ((xp - x) * mod + x);
				int ydp = (int) ((yp - y) * mod + y);
				
				mc.getRenderItem().renderItemIntoGUI(stack, xdp - 8, ydp - 8);

				if(xsp < x)
					xsp -= (width - 8);
				if(ysp < y)
					ysp -= 9;
				
				fontRendererObj.drawStringWithShadow(name, xsp, ysp, 0xFFFFFF);

				mod = 0.8;
				xdp = (int) ((xp - x) * mod + x);
				ydp = (int) ((yp - y) * mod + y);
				
				mc.renderEngine.bindTexture(signs[slot]);
				drawModalRectWithCustomSizedTexture(xdp - 8, ydp - 8, 0, 0, 16, 16, 16, 16);
			}
		}
		
		float stime = 5F;
		float s = (3F * Math.min(stime, timeIn + partialTicks) / stime);
		GlStateManager.scale(s, s, s);
		GlStateManager.translate((float) x / s - 8, (float) y / s - 8, 0);
		mc.getRenderItem().renderItemAndEffectIntoGUI(cadStack, 0, 0);
		
		GlStateManager.popMatrix();
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		
		if(!Keyboard.isKeyDown(KeybindHandler.keybind.getKeyCode())) {
			mc.displayGuiScreen(null);
			if(slotSelected != -1) {
				int slot = slots.get(slotSelected);
				PlayerDataHandler.get(mc.thePlayer).stopLoopcast();
				MessageChangeSocketableSlot message = new MessageChangeSocketableSlot(slot);
				NetworkHandler.INSTANCE.sendToServer(message);
			}
		}
		
		timeIn++;
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	private static float mouseAngle(int x, int y, int mx, int my) {
		Vector2f baseVec = new Vector2f(1F, 0F);
		Vector2f mouseVec = new Vector2f(mx - x, my - y);

		float ang = (float) (Math.acos(Vector2f.dot(baseVec, mouseVec) / (baseVec.length() * mouseVec.length())) * (180F / Math.PI));
		return my < y ? 360F - ang : ang;
	}
	
}

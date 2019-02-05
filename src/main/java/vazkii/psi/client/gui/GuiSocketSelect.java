/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [13/01/2016, 23:05:26 (GMT)]
 */
package vazkii.psi.client.gui;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.network.NetworkMessage;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.cad.ISocketableController;
import vazkii.psi.client.core.handler.KeybindHandler;
import vazkii.psi.client.core.helper.PsiRenderHelper;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.message.MessageChangeControllerSlot;
import vazkii.psi.common.network.message.MessageChangeSocketableSlot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	ItemStack controllerStack;
	ISocketableController controller;
	ItemStack[] controlledStacks;
	int controlSlot;

	ItemStack socketableStack;
	ISocketable socketable;
	List<Integer> slots;

	public GuiSocketSelect(ItemStack stack) {
		mc = Minecraft.getMinecraft();
		
		controllerStack = ItemStack.EMPTY;
		socketableStack = ItemStack.EMPTY;
		
		if(stack.getItem() instanceof ISocketable)
			setSocketable(stack);
		else if(stack.getItem() instanceof ISocketableController) {
			controllerStack = stack;
			controller = (ISocketableController) stack.getItem();
			controlledStacks = controller.getControlledStacks(mc.player, stack);
			controlSlot = controller.getDefaultControlSlot(controllerStack);
			if(controlSlot >= controlledStacks.length)
				controlSlot = 0;

			setSocketable(controlledStacks.length == 0 ? ItemStack.EMPTY : controlledStacks[controlSlot]);
		}
	}

	public void setSocketable(ItemStack stack) {
		slots = new ArrayList<>();
		if(stack.isEmpty())
			return;

		socketableStack = stack;
		socketable = (ISocketable) stack.getItem();

		for(int i = 0; i < ISocketable.MAX_SLOTS; i++)
			if(socketable.showSlotInRadialMenu(stack, i))
				slots.add(i);
	}

	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		super.drawScreen(mx, my, partialTicks);


		int x = width / 2;
		int y = height / 2;
		int maxRadius = 80;

		double angle = mouseAngle(x, y, mx, my);

		int segments = slots.size();
		float step = (float) Math.PI / 180;
		float degPer = (float) Math.PI * 2 / segments;

		ItemStack cadStack = PsiAPI.getPlayerCAD(Minecraft.getMinecraft().player);
		slotSelected = -1;

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buf = tess.getBuffer();


		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		buf.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);

		for(int seg = 0; seg < segments; seg++) {
			boolean mouseInSector = degPer * seg < angle && angle < degPer * (seg + 1);
			float radius = Math.max(0F, Math.min((timeIn + partialTicks - seg * 6F / segments) * 40F, maxRadius));

			int gs = 0x40;
			if(seg % 2 == 0)
				gs += 0x19;
			int r = gs;
			int g = gs;
			int b = gs;
			int a = 0x66;


			if (seg == 0)
				buf.pos(x, y, 0).color(r, g, b, a).endVertex();

			if(mouseInSector) {
				slotSelected = seg;

				if(!cadStack.isEmpty()) {
					int color = Psi.proxy.getColorForCAD(cadStack);
					r = PsiRenderHelper.r(color);
					g = PsiRenderHelper.g(color);
					b = PsiRenderHelper.b(color);
				}
			}

			for(float i = 0; i <= degPer + step; i += step) {
				float rad = i + seg * degPer;
				float xp = x + MathHelper.cos(rad) * radius;
				float yp = y + MathHelper.sin(rad) * radius;

				if (i == 0)
					buf.pos(xp, yp, 0).color(r, g, b, a).endVertex();
				buf.pos(xp, yp, 0).color(r, g, b, a).endVertex();
			}
		}
		tess.draw();

		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.enableTexture2D();

		for(int seg = 0; seg < segments; seg++) {
			boolean mouseInSector = degPer * seg < angle && angle < degPer * (seg + 1);
			float radius = Math.max(0F, Math.min((timeIn + partialTicks - seg * 6F / segments) * 40F, maxRadius));
			float rad = (seg + 0.5f) * degPer;
			float xp = x + MathHelper.cos(rad) * radius;
			float yp = y + MathHelper.sin(rad) * radius;

			ItemStack stack = socketable.getBulletInSocket(socketableStack, seg);
			if(!stack.isEmpty()) {
				float xsp = xp - 4;
				float ysp = yp;
				String name = (mouseInSector ? TextFormatting.UNDERLINE : TextFormatting.RESET) + stack.getDisplayName();
				int width = fontRenderer.getStringWidth(name);

				double mod = 0.6;
				int xdp = (int) ((xp - x) * mod + x);
				int ydp = (int) ((yp - y) * mod + y);

				mc.getRenderItem().renderItemIntoGUI(stack, xdp - 8, ydp - 8);

				if(xsp < x)
					xsp -= width - 8;
				if(ysp < y)
					ysp -= 9;

				fontRenderer.drawStringWithShadow(name, xsp, ysp, 0xFFFFFF);

				mod = 0.8;
				xdp = (int) ((xp - x) * mod + x);
				ydp = (int) ((yp - y) * mod + y);

				mc.renderEngine.bindTexture(signs[seg]);
				drawModalRectWithCustomSizedTexture(xdp - 8, ydp - 8, 0, 0, 16, 16, 16, 16);
			}
		}

		float shift = Math.min(5, timeIn + partialTicks) / 5;
		float scale = 3 * shift;
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		RenderHelper.enableGUIStandardItemLighting();

		if(controlledStacks != null && controlledStacks.length > 0) {
			int xs = width / 2 - 18 * controlledStacks.length / 2;
			int ys = height / 2;

			for(int i = 0; i < controlledStacks.length; i++) {
				float yoff = 25F + maxRadius;
				if(i == controlSlot)
					yoff += 5F;

				GlStateManager.pushMatrix();
				GlStateManager.translate(0, -yoff * shift, 0F);
				mc.getRenderItem().renderItemAndEffectIntoGUI(controlledStacks[i], xs + i * 18, ys);
				GlStateManager.popMatrix();
			}

		}

		if(!socketableStack.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.scale(scale, scale, scale);
			mc.getRenderItem().renderItemAndEffectIntoGUI(socketableStack,
					(int) (x / scale) - 8, (int) (y / scale) - 8);
			GlStateManager.popMatrix();
		}
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();

	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if(!controllerStack.isEmpty() && controlledStacks.length > 0) {
			if(mouseButton == 0) {
				controlSlot++;
				if(controlSlot >= controlledStacks.length)
					controlSlot = 0;
			} else if(mouseButton == 1) {
				controlSlot--;
				if(controlSlot < 0)
					controlSlot = controlledStacks.length - 1;
			}

			setSocketable(controlledStacks[controlSlot]);
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		if(!isKeyDown(KeybindHandler.keybind)) {
			mc.displayGuiScreen(null);
			if(slotSelected != -1) {
				int slot = slots.get(slotSelected);
				PlayerDataHandler.get(mc.player).stopLoopcast();

				NetworkMessage message;
				if(!controllerStack.isEmpty())
					message = new MessageChangeControllerSlot(controlSlot, slot);
				else message = new MessageChangeSocketableSlot(slot);
				NetworkHandler.INSTANCE.sendToServer(message);
			}
		}

		ImmutableSet<KeyBinding> set = ImmutableSet.of(mc.gameSettings.keyBindForward, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindSneak, mc.gameSettings.keyBindSprint, mc.gameSettings.keyBindJump);
		for(KeyBinding k : set)
			KeyBinding.setKeyBindState(k.getKeyCode(), isKeyDown(k));

		timeIn++;
	}

	public boolean isKeyDown(KeyBinding keybind) {
		int key = keybind.getKeyCode();
		if(key < 0) {
			int button = 100 + key;
			return Mouse.isButtonDown(button);
		}
		return Keyboard.isKeyDown(key);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private static double mouseAngle(int x, int y, int mx, int my) {
		return (MathHelper.atan2(my - y, mx - x) + Math.PI * 2) % (Math.PI * 2);
	}

}

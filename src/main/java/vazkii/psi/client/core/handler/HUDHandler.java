/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.gui.PsiHudElementType;
import vazkii.psi.api.gui.RenderPsiHudEvent;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData.Deduction;
import vazkii.psi.common.item.base.IHUDItem;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

import java.util.function.Consumer;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public final class HUDHandler {

	private static final ResourceLocation psiBar = new ResourceLocation(LibResources.GUI_PSI_BAR);
	private static final ResourceLocation psiBarMask = new ResourceLocation(LibResources.GUI_PSI_BAR_MASK);
	private static final ResourceLocation psiBarShatter = new ResourceLocation(LibResources.GUI_PSI_BAR_SHATTER);

	private static final int secondaryTextureUnit = 7;

	private static boolean registeredMask = false;
	private static final int maxRemainingTicks = 30;

	private static ItemStack remainingDisplayStack;
	private static int remainingTime;
	private static int remainingCount;

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onDraw(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.ALL) {
			MainWindow resolution = event.getWindow();
			float partialTicks = event.getPartialTicks();

			if (!MinecraftForge.EVENT_BUS.post(new RenderPsiHudEvent(PsiHudElementType.PSI_BAR))) {
				drawPsiBar(event.getMatrixStack(), resolution, partialTicks);
			}
			if (!MinecraftForge.EVENT_BUS.post(new RenderPsiHudEvent(PsiHudElementType.SOCKETABLE_EQUIPPED_NAME))) {
				renderSocketableEquippedName(event.getMatrixStack(), resolution, partialTicks);
			}
			if (!MinecraftForge.EVENT_BUS.post(new RenderPsiHudEvent(PsiHudElementType.REMAINING_ITEMS))) {
				renderRemainingItems(event.getMatrixStack(), resolution, partialTicks);
			}
			if (!MinecraftForge.EVENT_BUS.post(new RenderPsiHudEvent(PsiHudElementType.HUD_ITEM))) {
				renderHUDItem(event.getMatrixStack(), resolution, partialTicks);
			}
		}
	}

	public static void tick() {

		if (remainingTime > 0) {
			--remainingTime;
		}
	}

	private static boolean showsBar(PlayerData data, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		} else {
			return stack.getCapability(PsiAPI.PSI_BAR_DISPLAY_CAPABILITY).map(c -> c.shouldShow(data)).orElse(false);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void drawPsiBar(MatrixStack ms, MainWindow res, float pticks) {
		Minecraft mc = Minecraft.getInstance();
		ItemStack cadStack = PsiAPI.getPlayerCAD(mc.player);

		if (cadStack.isEmpty()) {
			return;
		}

		ICAD cad = (ICAD) cadStack.getItem();
		PlayerData data = PlayerDataHandler.get(mc.player);

		int totalPsi = data.getTotalPsi();
		int currPsi = data.getAvailablePsi();

		if (ConfigHandler.CLIENT.contextSensitiveBar.get() && currPsi == totalPsi &&
				!showsBar(data, mc.player.getHeldItemMainhand()) &&
				!showsBar(data, mc.player.getHeldItemOffhand())) {
			return;
		}

		ms.push();

		boolean right = ConfigHandler.CLIENT.psiBarOnRight.get();

		int pad = 3;
		int width = 32;
		int height = 140;

		int x = -pad;
		if (right) {
			x = res.getScaledWidth() + pad - width;
		}
		int y = res.getScaledHeight() / 2 - height / 2;

		if (!registeredMask) {
			mc.textureManager.bindTexture(psiBarMask);
			mc.textureManager.bindTexture(psiBarShatter);
			registeredMask = true;
		}

		RenderSystem.enableBlend();
		mc.textureManager.bindTexture(psiBar);
		AbstractGui.drawTexture(ms, x, y, 0, 0, width, height, 64, 256);

		x += 8;
		y += 26;

		width = 16;
		height = 106;

		float r = 0.6F;
		float g = 0.65F;
		float b = 1F;

		int origHeight = height;
		int origY = y;
		int v = 0;

		int texture = 0;
		boolean shaders = ShaderHandler.useShaders;

		if (shaders) {
			RenderSystem.activeTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit);
			texture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		}

		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		for (Deduction d : data.deductions) {
			float a = d.getPercentile(pticks);
			RenderSystem.color4f(r, g, b, a);
			height = (int) Math.ceil(origHeight * (double) d.deduct / totalPsi);
			int effHeight = (int) (origHeight * (double) d.current / totalPsi);
			v = origHeight - effHeight;
			y = origY + v;

			ShaderHandler.useShader(ShaderHandler.psiBar, generateCallback(a, d.shatter, data.overflowed));
			AbstractGui.drawTexture(ms, x, y, 32, v, width, height, 64, 256);
		}

		float textY = origY;
		if (totalPsi > 0) {
			height = (int) ((double) origHeight * (double) data.availablePsi / totalPsi);
			v = origHeight - height;
			y = origY + v;

			if (data.availablePsi != data.lastAvailablePsi) {
				float textHeight = (float) (origHeight
						* (data.availablePsi * pticks + data.lastAvailablePsi * (1.0 - pticks)) / totalPsi);
				textY = origY + (origHeight - textHeight);
			} else {
				textY = y;
			}
		} else {
			height = 0;
		}

		RenderSystem.color3f(r, g, b);
		ShaderHandler.useShader(ShaderHandler.psiBar, generateCallback(1F, false, data.overflowed));
		AbstractGui.drawTexture(ms, x, y, 32, v, width, height, 64, 256);
		ShaderHandler.releaseShader();

		if (shaders) {
			RenderSystem.activeTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit);
			RenderSystem.bindTexture(texture);
			RenderSystem.activeTexture(ARBMultitexture.GL_TEXTURE0_ARB);
		}

		RenderSystem.color3f(1F, 1F, 1F);

		ms.push();
		ms.translate(0F, textY, 0F);
		width = 44;
		height = 3;

		int storedPsi = cad.getStoredPsi(cadStack);

		String s1 = storedPsi == -1 ? "\u221E" : "" + data.availablePsi;
		String s2 = "" + storedPsi;

		int offBar = 22;
		int offStr1 = 7 + mc.fontRenderer.getStringWidth(s1);
		int offStr2 = 7 + mc.fontRenderer.getStringWidth(s2);

		if (!right) {
			offBar = 6;
			offStr1 = -23;
			offStr2 = -23;
		}

		int color = cad.getSpellColor(cadStack);
		RenderSystem.color4f(PsiRenderHelper.r(color) / 255F,
				PsiRenderHelper.g(color) / 255F,
				PsiRenderHelper.b(color) / 255F, 1F);

		AbstractGui.drawTexture(ms, x - offBar, -2, 0, 140, width, height, 64, 256);
		mc.fontRenderer.drawWithShadow(ms, s1, x - offStr1, -11, 0xFFFFFF);
		ms.pop();

		if (storedPsi != -1) {
			ms.push();
			RenderSystem.translatef(0F, Math.max(textY + 3, origY + 100), 0F);
			mc.fontRenderer.drawWithShadow(ms, s2, x - offStr2, 0, 0xFFFFFF);
			ms.pop();
		}
		RenderSystem.disableBlend();
		ms.pop();
	}

	@OnlyIn(Dist.CLIENT)
	private static void renderSocketableEquippedName(MatrixStack ms, MainWindow res, float pticks) {
		Minecraft mc = Minecraft.getInstance();
		ItemStack stack = mc.player.getHeldItem(Hand.MAIN_HAND);
		if (!ISocketable.isSocketable(stack)) {
			return;
		}
		String name = ISocketable.getSocketedItemName(stack, "").getString();
		if (stack.isEmpty() || name.trim().isEmpty()) {
			return;
		}

		int ticks = mc.ingameGUI.remainingHighlightTicks;
		ticks -= 10;

		if (ticks > 0) {
			ISocketable socketable = ISocketable.socketable(stack);
			ItemStack bullet = socketable.getSelectedBullet();

			int alpha = Math.min(255, (int) ((ticks - pticks) * 256.0F / 10.0F));
			int color = ICADColorizer.DEFAULT_SPELL_COLOR + (alpha << 24);

			int x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(name) / 2;
			int y = res.getScaledHeight() - 71;
			if (mc.player.abilities.isCreativeMode) {
				y += 14;
			}

			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			mc.fontRenderer.drawWithShadow(ms, name, x, y, color);

			int w = mc.fontRenderer.getStringWidth(name);
			ms.push();
			ms.translate(x + w, y - 6, 0);
			ms.scale(alpha / 255F, 1F, 1);
			RenderSystem.color3f(1F, 1F, 1F);
			mc.getItemRenderer().renderItemIntoGUI(bullet, 0, 0);
			ms.pop();
			RenderSystem.disableBlend();
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void renderRemainingItems(MatrixStack ms, MainWindow resolution, float partTicks) {
		if (remainingTime > 0 && !remainingDisplayStack.isEmpty()) {
			int pos = maxRemainingTicks - remainingTime;
			Minecraft mc = Minecraft.getInstance();
			int remainingLeaveTicks = 20;
			int x = resolution.getScaledWidth() / 2 + 10 + Math.max(0, pos - remainingLeaveTicks);
			int y = resolution.getScaledHeight() / 2;

			int start = maxRemainingTicks - remainingLeaveTicks;
			float alpha = remainingTime + partTicks > start ? 1F : (remainingTime + partTicks) / start;

			RenderSystem.disableAlphaTest();
			RenderSystem.disableBlend();
			RenderSystem.disableRescaleNormal();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			RenderSystem.color4f(1F, 1F, 1F, alpha);
			RenderSystem.enableLighting();
			RenderSystem.enableColorMaterial();
			int xp = x + (int) (16F * (1F - alpha));
			ms.translate(xp, y, 0F);
			ms.scale(alpha, 1F, 1F);
			mc.getItemRenderer().renderItemAndEffectIntoGUI(remainingDisplayStack, 0, 0);
			ms.scale(1F / alpha, 1F, 1F);
			ms.translate(-xp, -y, 0F);
			RenderHelper.disableStandardItemLighting();
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			RenderSystem.enableBlend();

			String text = remainingDisplayStack.getDisplayName().copy().formatted(TextFormatting.GREEN).getString();
			if (remainingCount >= 0) {
				int max = remainingDisplayStack.getMaxStackSize();
				int stacks = remainingCount / max;
				int rem = remainingCount % max;

				if (stacks == 0) {
					text = "" + remainingCount;
				} else {
					text = remainingCount + " (" + TextFormatting.AQUA + stacks + TextFormatting.RESET + "*"
							+ TextFormatting.GRAY + max + TextFormatting.RESET + "+" + TextFormatting.YELLOW + rem
							+ TextFormatting.RESET + ")";
				}
			} else if (remainingCount == -1) {
				text = "\u221E";
			}

			int color = 0x00FFFFFF | (int) (alpha * 0xFF) << 24;
			mc.fontRenderer.drawWithShadow(ms, text, x + 20, y + 6, color);

			RenderSystem.disableBlend();
			RenderSystem.enableAlphaTest();
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void renderHUDItem(MatrixStack ms, MainWindow resolution, float partTicks) {
		Minecraft mc = Minecraft.getInstance();
		ItemStack stack = mc.player.getHeldItemMainhand();
		if (!stack.isEmpty() && stack.getItem() instanceof IHUDItem) {
			((IHUDItem) stack.getItem()).drawHUD(ms, resolution, partTicks, stack);
		}

		stack = mc.player.getHeldItemOffhand();
		if (!stack.isEmpty() && stack.getItem() instanceof IHUDItem) {
			((IHUDItem) stack.getItem()).drawHUD(ms, resolution, partTicks, stack);
		}
	}

	public static void setRemaining(ItemStack stack, int count) {
		HUDHandler.remainingDisplayStack = stack;
		HUDHandler.remainingCount = count;
		remainingTime = stack.isEmpty() ? 0 : maxRemainingTicks;
	}

	public static void setRemaining(PlayerEntity player, ItemStack displayStack, Pattern pattern) {
		int count = 0;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (!stack.isEmpty() && (pattern == null ? ItemStack.areItemsEqual(displayStack, stack) : pattern.matcher(stack.getTranslationKey()).find())) {
				count += stack.getCount();
			}
		}

		setRemaining(displayStack, count);
	}

	@OnlyIn(Dist.CLIENT)
	private static Consumer<Integer> generateCallback(final float percentile, final boolean shatter, final boolean overflowed) {
		Minecraft mc = Minecraft.getInstance();
		return (Integer shader) -> {
			int percentileUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "percentile");
			int overflowedUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "overflowed");
			int imageUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "image");
			int maskUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "mask");

			RenderSystem.activeTexture(ARBMultitexture.GL_TEXTURE0_ARB);
			mc.textureManager.bindTexture(psiBar);
			ARBShaderObjects.glUniform1iARB(imageUniform, 0);

			RenderSystem.activeTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit);

			RenderSystem.enableTexture();

			mc.textureManager.bindTexture(shatter ? psiBarShatter : psiBarMask);
			ARBShaderObjects.glUniform1iARB(maskUniform, secondaryTextureUnit);

			ARBShaderObjects.glUniform1fARB(percentileUniform, percentile);
			ARBShaderObjects.glUniform1iARB(overflowedUniform, overflowed ? 1 : 0);
		};
	}

}

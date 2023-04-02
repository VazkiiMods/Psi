/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
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

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

	public static final IGuiOverlay PSI_BAR = (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
		if (!MinecraftForge.EVENT_BUS.post(new RenderPsiHudEvent(PsiHudElementType.PSI_BAR))) {
			drawPsiBar(poseStack, partialTick, screenWidth, screenHeight);
		}
	};

	public static final IGuiOverlay SOCKETABLE_EQUIPPED_NAME = (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
		if (!MinecraftForge.EVENT_BUS.post(new RenderPsiHudEvent(PsiHudElementType.SOCKETABLE_EQUIPPED_NAME))) {
			renderSocketableEquippedName(poseStack, partialTick, screenWidth, screenHeight);
		}
	};

	public static final IGuiOverlay REMAINING_ITEMS = (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
		if (!MinecraftForge.EVENT_BUS.post(new RenderPsiHudEvent(PsiHudElementType.REMAINING_ITEMS))) {
			renderRemainingItems(poseStack, partialTick, screenWidth, screenHeight);
		}
	};

	public static final IGuiOverlay HUD_ITEM = (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
		if (!MinecraftForge.EVENT_BUS.post(new RenderPsiHudEvent(PsiHudElementType.HUD_ITEM))) {
			renderHUDItem(poseStack, partialTick, screenWidth, screenHeight);
		}
	};

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void register(RegisterGuiOverlaysEvent event) {
		event.registerAboveAll("psi_bar", PSI_BAR);
		event.registerAboveAll("socketable_equipped_name", SOCKETABLE_EQUIPPED_NAME);
		event.registerAboveAll("remaining_items", REMAINING_ITEMS);
		event.registerAboveAll("hud_item", HUD_ITEM);
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
	public static void drawPsiBar(PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
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
				!showsBar(data, mc.player.getMainHandItem()) &&
				!showsBar(data, mc.player.getOffhandItem())) {
			return;
		}

		poseStack.pushPose();

		boolean right = ConfigHandler.CLIENT.psiBarOnRight.get();

		int pad = 3;
		int width = 32;
		int height = 140;

		int x = -pad;
		if (right) {
			x = screenWidth + pad - width;
		}
		int y = screenHeight / 2 - height / 2;

		if (!registeredMask) {
			RenderSystem.setShaderTexture(0, psiBarMask);
			RenderSystem.setShaderTexture(1, psiBarShatter);
			registeredMask = true;
		}

		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, psiBar);
		GuiComponent.blit(poseStack, x, y, 0, 0, width, height, 64, 256);

		x += 8;
		y += 26;

		width = 16;
		height = 106;

		float r = 0.6F;
		float g = 0.65F;
		float b = 1F;

		if (data.isOverflowed()) {
			r = 1F;
			g = 0.6F;
			b = 0.6F;
		}

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
			float a = d.getPercentile(partialTick);
			RenderSystem.setShaderColor(r, g, b, a);
			height = (int) Math.ceil(origHeight * (double) d.deduct / totalPsi);
			int effHeight = (int) (origHeight * (double) d.current / totalPsi);
			v = origHeight - effHeight;
			y = origY + v;

			ShaderHandler.useShader(ShaderHandler.psiBar, generateCallback(a, d.shatter, data.overflowed));
			GuiComponent.blit(poseStack, x, y, 32, v, width, height, 64, 256);
		}

		float textY = origY;
		if (totalPsi > 0) {
			height = (int) ((double) origHeight * (double) data.availablePsi / totalPsi);
			v = origHeight - height;
			y = origY + v;

			if (data.availablePsi != data.lastAvailablePsi) {
				float textHeight = (float) (origHeight
						* (data.availablePsi * partialTick + data.lastAvailablePsi * (1.0 - partialTick)) / totalPsi);
				textY = origY + (origHeight - textHeight);
			} else {
				textY = y;
			}
		} else {
			height = 0;
		}

		RenderSystem.setShaderColor(r, g, b, 1F);
		ShaderHandler.useShader(ShaderHandler.psiBar, generateCallback(1F, false, data.overflowed));
		GuiComponent.blit(poseStack, x, y, 32, v, width, height, 64, 256);
		ShaderHandler.releaseShader();

		if (shaders) {
			RenderSystem.activeTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit);
			RenderSystem.bindTexture(texture);
			RenderSystem.activeTexture(ARBMultitexture.GL_TEXTURE0_ARB);
		}

		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

		poseStack.pushPose();
		poseStack.translate(0F, textY, 0F);
		width = 44;
		height = 3;

		int storedPsi = cad.getStoredPsi(cadStack);

		String s1 = storedPsi == -1 ? "\u221E" : "" + data.availablePsi;
		String s2 = "" + storedPsi;

		int offBar = 22;
		int offStr1 = 7 + mc.font.width(s1);
		int offStr2 = 7 + mc.font.width(s2);

		if (!right) {
			offBar = 6;
			offStr1 = -23;
			offStr2 = -23;
		}

		int color = cad.getSpellColor(cadStack);
		RenderSystem.setShaderColor(PsiRenderHelper.r(color) / 255F,
				PsiRenderHelper.g(color) / 255F,
				PsiRenderHelper.b(color) / 255F, 1F);

		GuiComponent.blit(poseStack, x - offBar, -2, 0, 140, width, height, 64, 256);
		mc.font.drawShadow(poseStack, s1, x - offStr1, -11, 0xFFFFFF);
		poseStack.popPose();

		if (storedPsi != -1) {
			poseStack.pushPose();
			poseStack.translate(0F, Math.max(textY + 3, origY + 100), 0F);
			mc.font.drawShadow(poseStack, s2, x - offStr2, 0, 0xFFFFFF);
			poseStack.popPose();
		}
		RenderSystem.disableBlend();
		poseStack.popPose();
	}

	@OnlyIn(Dist.CLIENT)
	private static void renderSocketableEquippedName(PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
		Minecraft mc = Minecraft.getInstance();
		ItemStack stack = mc.player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!ISocketable.isSocketable(stack)) {
			return;
		}
		String name = ISocketable.getSocketedItemName(stack, "").getString();
		if (stack.isEmpty() || name.trim().isEmpty()) {
			return;
		}

		int ticks = mc.gui.toolHighlightTimer;
		ticks -= 10;

		if (ticks > 0) {
			ISocketable socketable = ISocketable.socketable(stack);
			ItemStack bullet = socketable.getSelectedBullet();

			int alpha = Math.min(255, (int) ((ticks - partialTick) * 256.0F / 10.0F));
			int color = ICADColorizer.DEFAULT_SPELL_COLOR + (alpha << 24);

			int x = screenWidth / 2 - mc.font.width(name) / 2;
			int y = screenHeight - 71;
			if (mc.player.isCreative()) {
				y += 14;
			}

			mc.font.drawShadow(poseStack, name, x, y, color);

			int w = mc.font.width(name);
			poseStack.pushPose();
			poseStack.translate(x + w, y - 6, 0);
			poseStack.scale(alpha / 255F, 1F, 1);
			PsiRenderHelper.transferMsToGl(poseStack, () -> mc.getItemRenderer().renderGuiItem(bullet, 0, 0));
			poseStack.popPose();
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void renderRemainingItems(PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
		if (remainingTime > 0 && !remainingDisplayStack.isEmpty()) {
			int pos = maxRemainingTicks - remainingTime;
			Minecraft mc = Minecraft.getInstance();
			int remainingLeaveTicks = 20;
			int x = screenWidth / 2 + 10 + Math.max(0, pos - remainingLeaveTicks);
			int y = screenHeight / 2;

			int start = maxRemainingTicks - remainingLeaveTicks;
			float alpha = remainingTime + partialTick > start ? 1F : (remainingTime + partialTick) / start;

			RenderSystem.setShaderColor(1F, 1F, 1F, alpha);
			int xp = x + (int) (16F * (1F - alpha));
			poseStack.pushPose();
			poseStack.translate(xp, y, 0F);
			poseStack.scale(alpha, 1F, 1F);
			PsiRenderHelper.transferMsToGl(poseStack, () -> mc.getItemRenderer().renderAndDecorateItem(remainingDisplayStack, 0, 0));
			poseStack.scale(1F / alpha, 1F, 1F);
			poseStack.translate(-xp, -y, 0F);
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

			String text = remainingDisplayStack.getHoverName().plainCopy().withStyle(ChatFormatting.GREEN).getString();
			if (remainingCount >= 0) {
				int max = remainingDisplayStack.getMaxStackSize();
				int stacks = remainingCount / max;
				int rem = remainingCount % max;

				if (stacks == 0) {
					text = "" + remainingCount;
				} else {
					text = remainingCount + " (" + ChatFormatting.AQUA + stacks + ChatFormatting.RESET + "*"
							+ ChatFormatting.GRAY + max + ChatFormatting.RESET + "+" + ChatFormatting.YELLOW + rem
							+ ChatFormatting.RESET + ")";
				}
			} else if (remainingCount == -1) {
				text = "\u221E";
			}

			int color = 0x00FFFFFF | (int) (alpha * 0xFF) << 24;
			mc.font.drawShadow(poseStack, text, x + 20, y + 6, color);

			poseStack.popPose();
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void renderHUDItem(PoseStack poseStack, float partialTicks, int screenWidth, int screenHeight) {
		Minecraft mc = Minecraft.getInstance();
		ItemStack stack = mc.player.getMainHandItem();
		if (!stack.isEmpty() && stack.getItem() instanceof IHUDItem) {
			((IHUDItem) stack.getItem()).drawHUD(poseStack, partialTicks, screenWidth, screenHeight, stack);
		}

		stack = mc.player.getOffhandItem();
		if (!stack.isEmpty() && stack.getItem() instanceof IHUDItem) {
			((IHUDItem) stack.getItem()).drawHUD(poseStack, partialTicks, screenWidth, screenHeight, stack);
		}
	}

	public static void setRemaining(ItemStack stack, int count) {
		HUDHandler.remainingDisplayStack = stack;
		HUDHandler.remainingCount = count;
		remainingTime = stack.isEmpty() ? 0 : maxRemainingTicks;
	}

	public static void setRemaining(Player player, ItemStack displayStack, Pattern pattern) {
		int count = 0;
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (!stack.isEmpty() && (pattern == null ? ItemStack.isSame(displayStack, stack) : pattern.matcher(stack.getDescriptionId()).find())) {
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
			RenderSystem.setShaderTexture(0, psiBar);
			ARBShaderObjects.glUniform1iARB(imageUniform, 0);

			RenderSystem.activeTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit);

			RenderSystem.enableTexture();

			RenderSystem.setShaderTexture(1, shatter ? psiBarShatter : psiBarMask);
			ARBShaderObjects.glUniform1iARB(maskUniform, secondaryTextureUnit);

			ARBShaderObjects.glUniform1fARB(percentileUniform, percentile);
			ARBShaderObjects.glUniform1iARB(overflowedUniform, overflowed ? 1 : 0);
		};
	}

}

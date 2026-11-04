/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import vazkii.psi.client.core.handler.ClientSpellPredictionHandler;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.client.core.handler.HUDHandler;
import vazkii.psi.client.fx.SparkleParticleData;
import vazkii.psi.client.fx.WispParticleData;
import vazkii.psi.client.gui.GuiFlashRing;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.client.network.PsiClientMessageHandler;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.client.PsiClientRuntime;
import vazkii.psi.mixin.client.AccessorClientAdvancements;

public final class PsiClientHooks implements PsiClientRuntime.Hooks {

	private static final PsiClientHooks INSTANCE = new PsiClientHooks();

	private PsiClientHooks() {}

	public static void install() {
		PsiClientRuntime.install(INSTANCE);
	}

	@Override
	public boolean hasShiftDown() {
		return Screen.hasShiftDown();
	}

	@Override
	public boolean hasControlDown() {
		return Screen.hasControlDown();
	}

	@Override
	public float clientTicks() {
		return ClientTickHandler.total;
	}

	@Override
	public boolean hasAdvancement(ResourceLocation advancementLocation, Player player) {
		if(player instanceof LocalPlayer localPlayer) {
			ClientAdvancements advancements = localPlayer.connection.getAdvancements();
			AdvancementHolder holder = advancements.get(advancementLocation);
			if(holder == null) {
				return false;
			}

			AdvancementProgress progress = ((AccessorClientAdvancements) advancements).psi$getProgress().get(holder);
			return progress != null && progress.isDone();
		}

		if(!(player instanceof ServerPlayer serverPlayer) || serverPlayer.getServer() == null) {
			return false;
		}

		var advancement = serverPlayer.getServer().getAdvancements().get(advancementLocation);
		return advancement != null && serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone();
	}

	@Override
	public void sparkle(Level level, double x, double y, double z, float red, float green, float blue,
			float motionX, float motionY, float motionZ, float size, int lifetime) {
		if(lifetime != 0) {
			level.addParticle(new SparkleParticleData(size, red, green, blue, lifetime, motionX, motionY, motionZ),
					true, x, y, z, motionX, motionY, motionZ);
		}
	}

	@Override
	public void wisp(Level level, double x, double y, double z, float red, float green, float blue,
			float size, float motionX, float motionY, float motionZ, float maxAgeMultiplier) {
		if(maxAgeMultiplier != 0) {
			level.addParticle(new WispParticleData(size, red, green, blue, maxAgeMultiplier),
					true, x, y, z, motionX, motionY, motionZ);
		}
	}

	@Override
	public void showRemainingItems(ItemStack stack, int count) {
		HUDHandler.setRemaining(stack, count);
	}

	@Override
	public void recordPredictedMotion(Vec3 motion) {
		ClientSpellPredictionHandler.recordMotion(motion);
	}

	@Override
	public Vec3 reconcilePredictedMotion(Vec3 authoritativeMotion) {
		return ClientSpellPredictionHandler.reconcileMotion(authoritativeMotion);
	}

	@Override
	public void recordPredictedBlink(Vec3 offset) {
		ClientSpellPredictionHandler.recordBlink(offset);
	}

	@Override
	public Vec3 reconcilePredictedBlink(Vec3 authoritativeOffset) {
		return ClientSpellPredictionHandler.reconcileBlink(authoritativeOffset);
	}

	@Override
	public void showSpamlessChat(Component message, int magic) {
		PsiClientMessageHandler.showSpamlessChat(message, magic);
	}

	@Override
	public void showSpellError(String message, int x, int y) {
		PsiClientMessageHandler.showSpellError(message, x, y);
	}

	@Override
	public void openProgrammer(TileProgrammer programmer) {
		Minecraft.getInstance().setScreen(new GuiProgrammer(programmer));
	}

	@Override
	public void openFlashRing(ItemStack stack) {
		Minecraft.getInstance().setScreen(new GuiFlashRing(stack));
	}

}

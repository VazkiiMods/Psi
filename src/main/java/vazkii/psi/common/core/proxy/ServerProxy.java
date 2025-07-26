/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.proxy;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import vazkii.psi.common.block.tile.TileProgrammer;

public class ServerProxy implements IProxy {
	@Override
	public Player getClientPlayer() {
		return null;
	}

	@Override
	public void sparkleFX(Level world, double x, double y, double z, float r, float g, float b, float motionX, float motionY, float motionZ, float size, int m) {
		//NOOP
	}

	@Override
	public void sparkleFX(double x, double y, double z, float r, float g, float b, float motionX, float motionY, float motionZ, float size, int m) {
		//NOOP
	}

	@Override
	public void wispFX(Level world, double x, double y, double z, float r, float g, float b, float size, float motionX, float motionY, float motionZ, float maxAgeMul) {
		//NOOP
	}

	@Override
	public void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionX, float motionY, float motionZ, float maxAgeMul) {
		//NOOP
	}

	@Override
	public void openProgrammerGUI(TileProgrammer programmer) {
		//NOOP
	}

	@Override
	public void openFlashRingGUI(ItemStack stack) {
		//NOOP
	}

	@Override
	public boolean hasAdvancement(ResourceLocation advancementLocation, Player playerEntity) {
		if(!(playerEntity instanceof ServerPlayer serverPlayer)) {
			return false;
		}

		if(serverPlayer.getServer() == null) {
			return false;
		}

		var advancements = serverPlayer.getServer().getAdvancements();
		var advancement = advancements.get(advancementLocation);

		return advancement != null && serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone();
	}
}

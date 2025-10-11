/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.proxy;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;

import vazkii.psi.common.block.tile.TileProgrammer;

public interface IProxy {
	default void registerHandlers(IEventBus bus) {}

	Player getClientPlayer();

	default Level getClientWorld() {
		return null;
	}

	// Side-safe version of world.addParticle with noDistanceLimit flag set to true
	default void addParticleForce(Level world, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {}

	default boolean hasAdvancement(ResourceLocation advancement, Player playerEntity) {
		return false;
	}

	default int getColorForCAD(ItemStack cadStack) {
		return -1;
	}

	default int getColorForColorizer(ItemStack colorizer) {
		return -1;
	}

	void sparkleFX(Level world, double x, double y, double z, float r, float g, float b, float motionX, float motionY, float motionZ, float size, int m);

	default void sparkleFX(double x, double y, double z, float r, float g, float b, float gravity, float size, int m) {
		sparkleFX(x, y, z, r, g, b, 0, -gravity, 0, size, m);
	}

	void sparkleFX(double x, double y, double z, float r, float g, float b, float motionX, float motionY, float motionZ, float size, int m);

	void wispFX(Level world, double x, double y, double z, float r, float g, float b, float size, float motionX, float motionY, float motionZ, float maxAgeMul);

	default void wispFX(double x, double y, double z, float r, float g, float b, float size, float gravity) {
		wispFX(x, y, z, r, g, b, size, gravity, 1F);
	}

	default void wispFX(double x, double y, double z, float r, float g, float b, float size, float gravity, float maxAgeMul) {
		wispFX(x, y, z, r, g, b, size, 0, -gravity, 0, maxAgeMul);
	}

	void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionX, float motionY, float motionZ, float maxAgeMul);

	void openProgrammerGUI(TileProgrammer programmer);

	void openFlashRingGUI(ItemStack stack);

}

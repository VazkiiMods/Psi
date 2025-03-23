/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
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

import vazkii.psi.common.block.tile.TileProgrammer;

import java.awt.Color;

public interface IProxy {
	default void registerHandlers() {}

	Player getClientPlayer();

	default Level getClientWorld() {
		return null;
	}

	long getWorldElapsedTicks();

	int getClientRenderDistance();

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

	@Deprecated
	default Color getCADColor(ItemStack cadStack) {
		return new Color(getColorForCAD(cadStack));
	}

	@Deprecated
	default Color getColorizerColor(ItemStack colorizer) {
		return new Color(getColorForColorizer(colorizer));
	}

	void sparkleFX(Level world, double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m);

	default void sparkleFX(Level world, double x, double y, double z, float r, float g, float b, float gravity, float size, int m) {
		sparkleFX(world, x, y, z, r, g, b, 0, -gravity, 0, size, m);
	}

	void wispFX(Level world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul);

	default void wispFX(Level world, double x, double y, double z, float r, float g, float b, float size, float gravity) {
		wispFX(world, x, y, z, r, g, b, size, gravity, 1F);
	}

	default void wispFX(Level world, double x, double y, double z, float r, float g, float b, float size, float gravity, float maxAgeMul) {
		wispFX(world, x, y, z, r, g, b, size, 0, -gravity, 0, maxAgeMul);
	}


	void openProgrammerGUI(TileProgrammer programmer);

}

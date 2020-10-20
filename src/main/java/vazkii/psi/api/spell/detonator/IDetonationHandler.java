/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.detonator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import vazkii.psi.api.PsiAPI;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static vazkii.psi.api.spell.SpellContext.MAX_DISTANCE;

/**
 * The handler for an object's detonation behavior.
 *
 * Typically only seen on entities, but can be implemented
 */
public interface IDetonationHandler {

	static IDetonationHandler detonator(Entity entity) {
		return entity.getCapability(PsiAPI.DETONATION_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new);
	}

	static void performDetonation(World world, PlayerEntity player) {
		performDetonation(world, player, player, MAX_DISTANCE, (e) -> true);
	}

	static void performDetonation(World world, PlayerEntity player, double range) {
		performDetonation(world, player, player, range, (e) -> true);
	}

	static void performDetonation(World world, PlayerEntity player, Predicate<Entity> filter) {
		performDetonation(world, player, player, MAX_DISTANCE, filter);
	}

	static void performDetonation(World world, PlayerEntity player, double range, Predicate<Entity> filter) {
		performDetonation(world, player, player, range, filter);
	}

	static void performDetonation(World world, PlayerEntity player, Entity center) {
		performDetonation(world, player, center, MAX_DISTANCE, (e) -> true);
	}

	static void performDetonation(World world, PlayerEntity player, Entity center, double range) {
		performDetonation(world, player, center, range, (e) -> true);
	}

	static void performDetonation(World world, PlayerEntity player, Entity center, Predicate<Entity> filter) {
		performDetonation(world, player, center, MAX_DISTANCE, filter);
	}

	static void performDetonation(World world, PlayerEntity player, Entity center, double range, Predicate<Entity> filter) {
		List<Entity> charges = world.getEntitiesWithinAABB(Entity.class,
				center.getBoundingBox().grow(range),
				entity -> {
					if (entity == null) {
						return false;
					}
					return entity.getCapability(PsiAPI.DETONATION_HANDLER_CAPABILITY).map(detonator -> {
						Vector3d locus = detonator.objectLocus();
						if (locus == null || locus.squareDistanceTo(center.getPosX(), center.getPosY(), center.getPosZ()) > range * range) {
							return false;
						}
						return filter == null || filter.test(entity);
					}).orElse(false);
				});

		List<IDetonationHandler> handlers = charges.stream()
				.map(e -> e.getCapability(PsiAPI.DETONATION_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new))
				.collect(Collectors.toList());

		if (!MinecraftForge.EVENT_BUS.post(new DetonationEvent(player, center, range, handlers))) {
			if (!handlers.isEmpty()) {
				for (IDetonationHandler handler : handlers) {
					handler.detonate();
				}
			}
		}
	}

	/**
	 * The locus of the object. Centered around the entity's lowest y and middle x and z positions.
	 *
	 * Null implies this detonator does not exist in the world.
	 */
	default Vector3d objectLocus() {
		return null;
	}

	void detonate();
}

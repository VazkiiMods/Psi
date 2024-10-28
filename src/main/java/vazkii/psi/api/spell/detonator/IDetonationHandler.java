/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.detonator;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import vazkii.psi.api.PsiAPI;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static vazkii.psi.api.spell.SpellContext.MAX_DISTANCE;

/**
 * The handler for an object's detonation behavior.
 * <p>
 * Typically only seen on entities, but can be implemented
 */
public interface IDetonationHandler {

    static IDetonationHandler detonator(Entity entity) {
        return entity.getCapability(PsiAPI.DETONATION_HANDLER_CAPABILITY);
    }

    static void performDetonation(Level world, Player player) {
        performDetonation(world, player, player, MAX_DISTANCE, (e) -> true);
    }

    static void performDetonation(Level world, Player player, double range) {
        performDetonation(world, player, player, range, (e) -> true);
    }

    static void performDetonation(Level world, Player player, Predicate<Entity> filter) {
        performDetonation(world, player, player, MAX_DISTANCE, filter);
    }

    static void performDetonation(Level world, Player player, double range, Predicate<Entity> filter) {
        performDetonation(world, player, player, range, filter);
    }

    static void performDetonation(Level world, Player player, Entity center) {
        performDetonation(world, player, center, MAX_DISTANCE, (e) -> true);
    }

    static void performDetonation(Level world, Player player, Entity center, double range) {
        performDetonation(world, player, center, range, (e) -> true);
    }

    static void performDetonation(Level world, Player player, Entity center, Predicate<Entity> filter) {
        performDetonation(world, player, center, MAX_DISTANCE, filter);
    }

    static void performDetonation(Level world, Player player, Entity center, double range, Predicate<Entity> filter) {
        List<Entity> charges = world.getEntitiesOfClass(Entity.class,
                center.getBoundingBox().inflate(range),
                entity -> {
                    if (entity == null) {
                        return false;
                    }
                    IDetonationHandler detonator = entity.getCapability(PsiAPI.DETONATION_HANDLER_CAPABILITY);
                    if (detonator != null) {
                        Vec3 locus = detonator.objectLocus();
                        if (locus == null || locus.distanceToSqr(center.getX(), center.getY(), center.getZ()) > range * range) {
                            return false;
                        }
                        return filter == null || filter.test(entity);
                    }
                    return false;
                });

        List<IDetonationHandler> handlers = charges.stream()
                .map(e -> Objects.requireNonNull(e.getCapability(PsiAPI.DETONATION_HANDLER_CAPABILITY)))
                .collect(Collectors.toList());

        if (!NeoForge.EVENT_BUS.post(new DetonationEvent(player, center, range, handlers)).isCanceled()) {
            if (!handlers.isEmpty()) {
                for (IDetonationHandler handler : handlers) {
                    handler.detonate();
                }
            }
        }
    }

    /**
     * The locus of the object. Centered around the entity's lowest y and middle x and z positions.
     * <p>
     * Null implies this detonator does not exist in the world.
     */
    default Vec3 objectLocus() {
        return null;
    }

    void detonate();
}

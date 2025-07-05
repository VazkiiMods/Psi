/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import vazkii.psi.common.Psi;
import vazkii.psi.common.lib.LibEntityNames;
import vazkii.psi.common.lib.LibMisc;

import static net.minecraft.world.entity.MobCategory.MISC;

@EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class ModEntities {
	public static EntityType<EntitySpellProjectile> spellProjectile;
	public static EntityType<EntitySpellCircle> spellCircle;
	public static EntityType<EntitySpellGrenade> spellGrenade;
	public static EntityType<EntitySpellCharge> spellCharge;
	public static EntityType<EntitySpellMine> spellMine;

	@SubscribeEvent
	public static void register(RegisterEvent evt) {
		evt.register(Registries.ENTITY_TYPE, helper -> {
			spellProjectile = EntityType.Builder.of((EntityType.EntityFactory<EntitySpellProjectile>) EntitySpellProjectile::new, MISC)
					.setTrackingRange(256)
					.setUpdateInterval(10)
					.setShouldReceiveVelocityUpdates(true)
					.sized(0, 0)
					.build("");
			spellCircle = EntityType.Builder.of(EntitySpellCircle::new, MISC)
					.setTrackingRange(256)
					.setUpdateInterval(10)
					.setShouldReceiveVelocityUpdates(false)
					.sized(3.0f, 0.3f)
					.fireImmune()
					.build("");
			spellGrenade = EntityType.Builder.of((EntityType.EntityFactory<EntitySpellGrenade>) EntitySpellGrenade::new, MISC)
					.setTrackingRange(256)
					.setUpdateInterval(10)
					.setShouldReceiveVelocityUpdates(true)
					.sized(0, 0)
					.build("");
			spellCharge = EntityType.Builder.of((EntityType.EntityFactory<EntitySpellCharge>) EntitySpellCharge::new, MISC)
					.setTrackingRange(256)
					.setUpdateInterval(10)
					.setShouldReceiveVelocityUpdates(true)
					.sized(0, 0)
					.build("");
			spellMine = EntityType.Builder.of((EntityType.EntityFactory<EntitySpellMine>) EntitySpellMine::new, MISC)
					.setTrackingRange(256)
					.setUpdateInterval(10)
					.setShouldReceiveVelocityUpdates(true)
					.sized(0, 0)
					.build("");

			helper.register(Psi.location(LibEntityNames.SPELL_PROJECTILE), spellProjectile);
			helper.register(Psi.location(LibEntityNames.SPELL_CIRCLE), spellCircle);
			helper.register(Psi.location(LibEntityNames.SPELL_GRENADE), spellGrenade);
			helper.register(Psi.location(LibEntityNames.SPELL_CHARGE), spellCharge);
			helper.register(Psi.location(LibEntityNames.SPELL_MINE), spellMine);
		});
	}
}

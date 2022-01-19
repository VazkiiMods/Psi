/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import vazkii.psi.common.lib.LibEntityNames;
import vazkii.psi.common.lib.LibMisc;

import static net.minecraft.entity.EntityClassification.MISC;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntities {

	@SubscribeEvent
	public static void init(RegistryEvent.Register<EntityType<?>> evt) {
		IForgeRegistry<EntityType<?>> r = evt.getRegistry();
		r.register(EntityType.Builder.of((EntityType.IFactory<EntitySpellProjectile>) EntitySpellProjectile::new, MISC)
				.setTrackingRange(256)
				.setUpdateInterval(10)
				.setShouldReceiveVelocityUpdates(true)
				.sized(0, 0)
				.build("").setRegistryName(LibMisc.MOD_ID, LibEntityNames.SPELL_PROJECTILE));
		r.register(EntityType.Builder.of(EntitySpellCircle::new, MISC)
				.setTrackingRange(256)
				.setUpdateInterval(10)
				.setShouldReceiveVelocityUpdates(false)
				.sized(3.0f, 0.3f)
				.fireImmune()
				.build("").setRegistryName(LibMisc.MOD_ID, LibEntityNames.SPELL_CIRCLE));
		r.register(EntityType.Builder.of((EntityType.IFactory<EntitySpellGrenade>) EntitySpellGrenade::new, MISC)
				.setTrackingRange(256)
				.setUpdateInterval(10)
				.setShouldReceiveVelocityUpdates(true)
				.sized(0, 0)
				.build("").setRegistryName(LibMisc.MOD_ID, LibEntityNames.SPELL_GRENADE));
		r.register(EntityType.Builder.of((EntityType.IFactory<EntitySpellCharge>) EntitySpellCharge::new, MISC)
				.setTrackingRange(256)
				.setUpdateInterval(10)
				.setShouldReceiveVelocityUpdates(true)
				.sized(0, 0)
				.build("").setRegistryName(LibMisc.MOD_ID, LibEntityNames.SPELL_CHARGE));
		r.register(EntityType.Builder.of((EntityType.IFactory<EntitySpellMine>) EntitySpellMine::new, MISC)
				.setTrackingRange(256)
				.setUpdateInterval(10)
				.setShouldReceiveVelocityUpdates(true)
				.sized(0, 0)
				.build("").setRegistryName(LibMisc.MOD_ID, LibEntityNames.SPELL_MINE));
	}
}

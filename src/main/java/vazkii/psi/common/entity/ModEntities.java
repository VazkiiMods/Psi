/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraftforge.registries.RegisterEvent;
import vazkii.psi.common.lib.LibEntityNames;
import vazkii.psi.common.lib.LibMisc;

import static net.minecraft.world.entity.MobCategory.MISC;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntities {

	@SubscribeEvent
	public static void register(RegisterEvent evt) {
		evt.register(ForgeRegistries.Keys.ENTITY_TYPES, helper -> {
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibEntityNames.SPELL_PROJECTILE),
					EntityType.Builder.of((EntityType.EntityFactory<EntitySpellProjectile>) EntitySpellProjectile::new, MISC)
					.setTrackingRange(256)
					.setUpdateInterval(10)
					.setShouldReceiveVelocityUpdates(true)
					.sized(0, 0)
					.build(""));
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibEntityNames.SPELL_CIRCLE),
					EntityType.Builder.of(EntitySpellCircle::new, MISC)
					.setTrackingRange(256)
					.setUpdateInterval(10)
					.setShouldReceiveVelocityUpdates(false)
					.sized(3.0f, 0.3f)
					.fireImmune()
					.build(""));
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibEntityNames.SPELL_GRENADE),
					EntityType.Builder.of((EntityType.EntityFactory<EntitySpellGrenade>) EntitySpellGrenade::new, MISC)
					.setTrackingRange(256)
					.setUpdateInterval(10)
					.setShouldReceiveVelocityUpdates(true)
					.sized(0, 0)
					.build(""));
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibEntityNames.SPELL_CHARGE),
					EntityType.Builder.of((EntityType.EntityFactory<EntitySpellCharge>) EntitySpellCharge::new, MISC)
					.setTrackingRange(256)
					.setUpdateInterval(10)
					.setShouldReceiveVelocityUpdates(true)
					.sized(0, 0)
					.build(""));
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibEntityNames.SPELL_MINE),
					EntityType.Builder.of((EntityType.EntityFactory<EntitySpellMine>) EntitySpellMine::new, MISC)
					.setTrackingRange(256)
					.setUpdateInterval(10)
					.setShouldReceiveVelocityUpdates(true)
					.sized(0, 0)
					.build(""));
		});
	}
}

/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.lib.LibEntityNames;
import vazkii.psi.common.platform.PsiEntityTypes;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.function.Supplier;

import static net.minecraft.world.entity.MobCategory.MISC;

public final class ModEntities {
	public static final RegistryEntry<EntityType<EntitySpellProjectile>> spellProjectile = register(
			LibEntityNames.SPELL_PROJECTILE,
			() -> PsiEntityTypes.create(LibEntityNames.SPELL_PROJECTILE,
					(EntityType.EntityFactory<EntitySpellProjectile>) EntitySpellProjectile::new,
					MISC, 0, 0, 256, 10, true, false));
	public static final RegistryEntry<EntityType<EntitySpellCircle>> spellCircle = register(
			LibEntityNames.SPELL_CIRCLE,
			() -> PsiEntityTypes.create(LibEntityNames.SPELL_CIRCLE,
					EntitySpellCircle::new, MISC, 3.0f, 0.3f, 256, 10, false, true));
	public static final RegistryEntry<EntityType<EntitySpellGrenade>> spellGrenade = register(
			LibEntityNames.SPELL_GRENADE,
			() -> PsiEntityTypes.create(LibEntityNames.SPELL_GRENADE,
					(EntityType.EntityFactory<EntitySpellGrenade>) EntitySpellGrenade::new,
					MISC, 0, 0, 256, 10, true, false));
	public static final RegistryEntry<EntityType<EntitySpellCharge>> spellCharge = register(
			LibEntityNames.SPELL_CHARGE,
			() -> PsiEntityTypes.create(LibEntityNames.SPELL_CHARGE,
					(EntityType.EntityFactory<EntitySpellCharge>) EntitySpellCharge::new,
					MISC, 0, 0, 256, 10, true, false));
	public static final RegistryEntry<EntityType<EntitySpellMine>> spellMine = register(
			LibEntityNames.SPELL_MINE,
			() -> PsiEntityTypes.create(LibEntityNames.SPELL_MINE,
					(EntityType.EntityFactory<EntitySpellMine>) EntitySpellMine::new,
					MISC, 0, 0, 256, 10, true, false));

	private ModEntities() {}

	public static void register() {}

	private static <T extends EntityType<?>> RegistryEntry<T> register(String name, Supplier<T> factory) {
		return PsiRegistries.register(BuiltInRegistries.ENTITY_TYPE, PsiAPI.location(name), factory);
	}
}

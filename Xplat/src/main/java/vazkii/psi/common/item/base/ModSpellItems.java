/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.base;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.capability.PsiCapabilities;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.common.item.ItemChargeSpellBullet;
import vazkii.psi.common.item.ItemCircleSpellBullet;
import vazkii.psi.common.item.ItemGrenadeSpellBullet;
import vazkii.psi.common.item.ItemLoopcastSpellBullet;
import vazkii.psi.common.item.ItemMineSpellBullet;
import vazkii.psi.common.item.ItemProjectileSpellBullet;
import vazkii.psi.common.item.ItemSpellBullet;
import vazkii.psi.common.item.ItemSpellDrive;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.platform.PsiLookups;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.List;
import java.util.function.Supplier;

public final class ModSpellItems {

	public static final RegistryEntry<ItemSpellBullet> spellBullet = item(LibItemNames.SPELL_BULLET, () -> new ItemSpellBullet(properties()));
	public static final RegistryEntry<ItemProjectileSpellBullet> projectileSpellBullet = item(LibItemNames.SPELL_BULLET_PROJECTILE, () -> new ItemProjectileSpellBullet(properties()));
	public static final RegistryEntry<ItemLoopcastSpellBullet> loopSpellBullet = item(LibItemNames.SPELL_BULLET_LOOP, () -> new ItemLoopcastSpellBullet(properties()));
	public static final RegistryEntry<ItemCircleSpellBullet> circleSpellBullet = item(LibItemNames.SPELL_BULLET_CIRCLE, () -> new ItemCircleSpellBullet(properties()));
	public static final RegistryEntry<ItemGrenadeSpellBullet> grenadeSpellBullet = item(LibItemNames.SPELL_BULLET_GRENADE, () -> new ItemGrenadeSpellBullet(properties()));
	public static final RegistryEntry<ItemChargeSpellBullet> chargeSpellBullet = item(LibItemNames.SPELL_BULLET_CHARGE, () -> new ItemChargeSpellBullet(properties()));
	public static final RegistryEntry<ItemMineSpellBullet> mineSpellBullet = item(LibItemNames.SPELL_BULLET_MINE, () -> new ItemMineSpellBullet(properties()));
	public static final RegistryEntry<ItemSpellDrive> spellDrive = item(LibItemNames.SPELL_DRIVE, () -> new ItemSpellDrive(properties()));

	private ModSpellItems() {}

	private static <T extends Item> RegistryEntry<T> item(String name, Supplier<T> factory) {
		return PsiRegistries.register(BuiltInRegistries.ITEM, PsiAPI.location(name), factory);
	}

	private static Item.Properties properties() {
		return new Item.Properties();
	}

	public static void register() {
		PsiLookups.registerItem(PsiCapabilities.SPELL_ACCEPTOR, ISpellAcceptor.class,
				ItemSpellBullet.SpellAcceptor::new,
				List.of(spellBullet, projectileSpellBullet, loopSpellBullet, circleSpellBullet,
						grenadeSpellBullet, chargeSpellBullet, mineSpellBullet));
	}
}

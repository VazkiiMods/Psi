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
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.capability.PsiCapabilities;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.common.item.armor.*;
import vazkii.psi.common.item.tool.*;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.platform.PsiLookups;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.List;

public final class ModPsimetalItems {
	public static final RegistryEntry<ItemPsimetalShovel> SHOVEL = item(
			LibItemNames.PSIMETAL_SHOVEL, () -> new ItemPsimetalShovel(new Item.Properties()));
	public static final RegistryEntry<ItemPsimetalPickaxe> PICKAXE = item(
			LibItemNames.PSIMETAL_PICKAXE, () -> new ItemPsimetalPickaxe(new Item.Properties()));
	public static final RegistryEntry<ItemPsimetalAxe> AXE = item(
			LibItemNames.PSIMETAL_AXE, () -> new ItemPsimetalAxe(new Item.Properties()));
	public static final RegistryEntry<ItemPsimetalSword> SWORD = item(
			LibItemNames.PSIMETAL_SWORD, () -> new ItemPsimetalSword(new Item.Properties()));

	public static final RegistryEntry<ItemPsimetalExosuitHelmet> HELMET = item(
			LibItemNames.PSIMETAL_EXOSUIT_HELMET,
			() -> new ItemPsimetalExosuitHelmet(ArmorItem.Type.HELMET,
					new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(18))));
	public static final RegistryEntry<ItemPsimetalExosuitChestplate> CHESTPLATE = item(
			LibItemNames.PSIMETAL_EXOSUIT_CHESTPLATE,
			() -> new ItemPsimetalExosuitChestplate(ArmorItem.Type.CHESTPLATE,
					new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(18))));
	public static final RegistryEntry<ItemPsimetalExosuitLeggings> LEGGINGS = item(
			LibItemNames.PSIMETAL_EXOSUIT_LEGGINGS,
			() -> new ItemPsimetalExosuitLeggings(ArmorItem.Type.LEGGINGS,
					new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(18))));
	public static final RegistryEntry<ItemPsimetalExosuitBoots> BOOTS = item(
			LibItemNames.PSIMETAL_EXOSUIT_BOOTS,
			() -> new ItemPsimetalExosuitBoots(ArmorItem.Type.BOOTS,
					new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(18))));

	private ModPsimetalItems() {}

	public static void register() {
		List<RegistryEntry<? extends Item>> tools = List.of(SHOVEL, PICKAXE, AXE, SWORD);
		PsiLookups.registerItem(PsiCapabilities.SOCKETABLE, ISocketable.class,
				stack -> new ToolSocketable(stack, 3), tools);
		PsiLookups.registerItem(PsiCapabilities.PSI_BAR_DISPLAY, IPsiBarDisplay.class,
				stack -> new ToolSocketable(stack, 3), tools);
		PsiLookups.registerItem(PsiCapabilities.SPELL_ACCEPTOR, ISpellAcceptor.class,
				stack -> new ToolSocketable(stack, 3), tools);

		List<RegistryEntry<? extends Item>> armor = List.of(HELMET, CHESTPLATE, LEGGINGS, BOOTS);
		PsiLookups.registerItem(PsiCapabilities.SOCKETABLE, ISocketable.class,
				stack -> new ItemPsimetalArmor.ArmorSocketable(stack, 3), armor);
		PsiLookups.registerItem(PsiCapabilities.PSI_BAR_DISPLAY, IPsiBarDisplay.class,
				stack -> new ItemPsimetalArmor.ArmorSocketable(stack, 3), armor);
		PsiLookups.registerItem(PsiCapabilities.SPELL_ACCEPTOR, ISpellAcceptor.class,
				stack -> new ItemPsimetalArmor.ArmorSocketable(stack, 3), armor);
	}

	private static <T extends Item> RegistryEntry<T> item(String name, java.util.function.Supplier<T> factory) {
		return PsiRegistries.register(BuiltInRegistries.ITEM, PsiAPI.location(name), factory);
	}
}

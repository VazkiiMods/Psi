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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.block.base.ModBasicBlocks;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

public final class ModBasicItems {

	public static final RegistryEntry<Item> psidustBlockItem = blockItem(LibBlockNames.PSIDUST_BLOCK, ModBasicBlocks.psidustBlock);
	public static final RegistryEntry<Item> psimetalBlockItem = blockItem(LibBlockNames.PSIMETAL_BLOCK, ModBasicBlocks.psimetalBlock);
	public static final RegistryEntry<Item> psigemBlockItem = blockItem(LibBlockNames.PSIGEM_BLOCK, ModBasicBlocks.psigemBlock);
	public static final RegistryEntry<Item> psimetalPlateBlackItem = blockItem(LibBlockNames.PSIMETAL_PLATE_BLACK, ModBasicBlocks.psimetalPlateBlack);
	public static final RegistryEntry<Item> psimetalPlateBlackLightItem = blockItem(LibBlockNames.PSIMETAL_PLATE_BLACK_LIGHT, ModBasicBlocks.psimetalPlateBlackLight);
	public static final RegistryEntry<Item> psimetalPlateWhiteItem = blockItem(LibBlockNames.PSIMETAL_PLATE_WHITE, ModBasicBlocks.psimetalPlateWhite);
	public static final RegistryEntry<Item> psimetalPlateWhiteLightItem = blockItem(LibBlockNames.PSIMETAL_PLATE_WHITE_LIGHT, ModBasicBlocks.psimetalPlateWhiteLight);
	public static final RegistryEntry<Item> psimetalEbonyItem = blockItem(LibBlockNames.EBONY_PSIMETAL_BLOCK, ModBasicBlocks.psimetalEbony);
	public static final RegistryEntry<Item> psimetalIvoryItem = blockItem(LibBlockNames.IVORY_PSIMETAL_BLOCK, ModBasicBlocks.psimetalIvory);

	public static final RegistryEntry<Item> psidust = item(LibItemNames.PSIDUST);
	public static final RegistryEntry<Item> psimetal = item(LibItemNames.PSIMETAL);
	public static final RegistryEntry<Item> psigem = item(LibItemNames.PSIGEM);
	public static final RegistryEntry<Item> ebonyPsimetal = item(LibItemNames.EBONY_PSIMETAL);
	public static final RegistryEntry<Item> ivoryPsimetal = item(LibItemNames.IVORY_PSIMETAL);
	public static final RegistryEntry<Item> ebonySubstance = item(LibItemNames.EBONY_SUBSTANCE);
	public static final RegistryEntry<Item> ivorySubstance = item(LibItemNames.IVORY_SUBSTANCE);

	private ModBasicItems() {}

	private static RegistryEntry<Item> blockItem(String name, RegistryEntry<? extends net.minecraft.world.level.block.Block> block) {
		return PsiRegistries.register(BuiltInRegistries.ITEM, PsiAPI.location(name),
				() -> new BlockItem(block.get(), new Item.Properties()));
	}

	private static RegistryEntry<Item> item(String name) {
		return PsiRegistries.register(BuiltInRegistries.ITEM, PsiAPI.location(name),
				() -> new Item(new Item.Properties()));
	}

	public static void register() {}
}

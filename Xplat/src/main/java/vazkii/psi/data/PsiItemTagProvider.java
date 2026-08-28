/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;

import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.ModTags;

import java.util.concurrent.CompletableFuture;

public class PsiItemTagProvider extends ItemTagsProvider {

	public PsiItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> pBlockTags) {
		super(output, pLookupProvider, pBlockTags);
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider) {
		tag(ModTags.DUSTS).add(ModItems.psidust.get());
		tag(ModTags.INGOTS).add(ModItems.psimetal.get());
		tag(ModTags.INGOTS).add(ModItems.ebonyPsimetal.get());
		tag(ModTags.INGOTS).add(ModItems.ivoryPsimetal.get());
		tag(ModTags.GEMS).add(ModItems.psigem.get());

		tag(ModTags.PSIDUST).add(ModItems.psidust.get());
		tag(ModTags.EBONY_SUBSTANCE).add(ModItems.ebonySubstance.get());
		tag(ModTags.IVORY_SUBSTANCE).add(ModItems.ivorySubstance.get());

		tag(ModTags.INGOT_PSIMETAL).add(ModItems.psimetal.get());
		copy(ModTags.Blocks.BLOCK_PSIMETAL, ModTags.BLOCK_PSIMETAL);

		tag(ModTags.ENCHANTABLES).add(
				ModItems.psimetalSword.get(),
				ModItems.psimetalAxe.get(),
				ModItems.psimetalPickaxe.get(),
				ModItems.psimetalShovel.get(),
				ModItems.psimetalExosuitHelmet.get(),
				ModItems.psimetalExosuitChestplate.get(),
				ModItems.psimetalExosuitLeggings.get(),
				ModItems.psimetalExosuitBoots.get()
		);

		tag(ItemTags.SWORDS).add(ModItems.psimetalSword.get());
		tag(ItemTags.SWORD_ENCHANTABLE).add(ModItems.psimetalSword.get());

		tag(ItemTags.AXES).add(ModItems.psimetalAxe.get());
		tag(ItemTags.PICKAXES).add(ModItems.psimetalPickaxe.get());
		tag(ItemTags.SHOVELS).add(ModItems.psimetalShovel.get());
		tag(ItemTags.MINING_ENCHANTABLE).add(
				ModItems.psimetalAxe.get(),
				ModItems.psimetalPickaxe.get(),
				ModItems.psimetalShovel.get()
		);

		tag(ItemTags.HEAD_ARMOR).add(ModItems.psimetalExosuitHelmet.get());
		tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(ModItems.psimetalExosuitHelmet.get());

		tag(ItemTags.CHEST_ARMOR).add(ModItems.psimetalExosuitChestplate.get());
		tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(ModItems.psimetalExosuitChestplate.get());

		tag(ItemTags.LEG_ARMOR).add(ModItems.psimetalExosuitLeggings.get());
		tag(ItemTags.LEG_ARMOR_ENCHANTABLE).add(ModItems.psimetalExosuitLeggings.get());

		tag(ItemTags.FOOT_ARMOR).add(ModItems.psimetalExosuitBoots.get());
		tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(ModItems.psimetalExosuitBoots.get());

		tag(ModTags.GEM_PSIGEM).add(ModItems.psigem.get());
		copy(ModTags.Blocks.BLOCK_PSIGEM, ModTags.BLOCK_PSIGEM);

		tag(ModTags.INGOT_EBONY_PSIMETAL).add(ModItems.ebonyPsimetal.get());
		copy(ModTags.Blocks.BLOCK_EBONY_PSIMETAL, ModTags.BLOCK_EBONY_PSIMETAL);
		tag(ModTags.INGOT_IVORY_PSIMETAL).add(ModItems.ivoryPsimetal.get());
		copy(ModTags.Blocks.BLOCK_IVORY_PSIMETAL, ModTags.BLOCK_IVORY_PSIMETAL);
		copy(ModTags.Blocks.STORAGE_BLOCKS, ModTags.STORAGE_BLOCKS);
	}

}

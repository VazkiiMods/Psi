/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.lib;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import vazkii.psi.api.PsiAPI;

public class ModTags {

	public static final TagKey<Item> PSIDUST = tag("psidust");
	public static final TagKey<Item> IVORY_SUBSTANCE = tag("ivory_substance");
	public static final TagKey<Item> EBONY_SUBSTANCE = tag("ebony_substance");

	public static final TagKey<Item> DUSTS = commonItemTag("dusts");
	public static final TagKey<Item> DUSTS_GLOWSTONE = commonItemTag("dusts/glowstone");
	public static final TagKey<Item> DUSTS_REDSTONE = commonItemTag("dusts/redstone");
	public static final TagKey<Item> ENCHANTABLES = commonItemTag("enchantables");
	public static final TagKey<Item> GEMS = commonItemTag("gems");
	public static final TagKey<Item> GEMS_DIAMOND = commonItemTag("gems/diamond");
	public static final TagKey<Item> GEMS_PRISMARINE = commonItemTag("gems/prismarine");
	public static final TagKey<Item> GEMS_QUARTZ = commonItemTag("gems/quartz");
	public static final TagKey<Item> GLASS_BLOCKS = commonItemTag("glass_blocks");
	public static final TagKey<Item> GUNPOWDERS = commonItemTag("gunpowders");
	public static final TagKey<Item> INGOTS = commonItemTag("ingots");
	public static final TagKey<Item> INGOTS_GOLD = commonItemTag("ingots/gold");
	public static final TagKey<Item> INGOTS_IRON = commonItemTag("ingots/iron");
	public static final TagKey<Item> SLIME_BALLS = commonItemTag("slime_balls");
	public static final TagKey<Item> STORAGE_BLOCKS = commonItemTag("storage_blocks");
	public static final TagKey<Item> STRINGS = commonItemTag("strings");

	public static final TagKey<Item> INGOT_PSIMETAL = commonItemTag("ingots/psimetal");
	public static final TagKey<Item> BLOCK_PSIMETAL = commonItemTag("storage_blocks/psimetal");

	public static final TagKey<Item> GEM_PSIGEM = commonItemTag("gems/psigem");
	public static final TagKey<Item> BLOCK_PSIGEM = commonItemTag("storage_blocks/psigem");

	public static final TagKey<Item> INGOT_EBONY_PSIMETAL = commonItemTag("ingots/ebony_psimetal");
	public static final TagKey<Item> BLOCK_EBONY_PSIMETAL = commonItemTag("storage_blocks/ebony_psimetal");

	public static final TagKey<Item> INGOT_IVORY_PSIMETAL = commonItemTag("ingots/ivory_psimetal");
	public static final TagKey<Item> BLOCK_IVORY_PSIMETAL = commonItemTag("storage_blocks/ivory_psimetal");

	private static TagKey<Item> tag(String name) {
		return TagKey.create(Registries.ITEM, prefix(name));
	}

	public static TagKey<Item> commonItemTag(String name) {
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
	}

	public static TagKey<Item> dye(DyeColor color) {
		return commonItemTag("dyes/" + color.getSerializedName());
	}

	public static ResourceLocation prefix(String path) {
		return PsiAPI.location(path);
	}

	public static class Blocks {
		public static final TagKey<Block> STORAGE_BLOCKS = commonBlockTag("storage_blocks");
		public static final TagKey<Block> BLOCK_PSIMETAL = fromTag(ModTags.BLOCK_PSIMETAL);
		public static final TagKey<Block> BLOCK_PSIGEM = fromTag(ModTags.BLOCK_PSIGEM);
		public static final TagKey<Block> BLOCK_EBONY_PSIMETAL = fromTag(ModTags.BLOCK_EBONY_PSIMETAL);
		public static final TagKey<Block> BLOCK_IVORY_PSIMETAL = fromTag(ModTags.BLOCK_IVORY_PSIMETAL);

		public static TagKey<Block> commonBlockTag(String name) {
			return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", name));
		}

		private static TagKey<Block> fromTag(TagKey<?> tag) {
			return TagKey.create(Registries.BLOCK, tag.location());
		}
	}
}

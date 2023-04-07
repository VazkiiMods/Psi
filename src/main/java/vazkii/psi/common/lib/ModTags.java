/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.lib;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

	public static final TagKey<Item> PSIDUST = tag("psidust");
	public static final TagKey<Item> IVORY_SUBSTANCE = tag("ivory_substance");
	public static final TagKey<Item> EBONY_SUBSTANCE = tag("ebony_substance");

	public static final TagKey<Item> INGOT_PSIMETAL = forgeTag("ingots/psimetal");
	public static final TagKey<Item> BLOCK_PSIMETAL = forgeTag("storage_blocks/psimetal");

	public static final TagKey<Item> GEM_PSIGEM = forgeTag("gems/psigem");
	public static final TagKey<Item> BLOCK_PSIGEM = forgeTag("storage_blocks/psigem");

	public static final TagKey<Item> INGOT_EBONY_PSIMETAL = forgeTag("ingots/ebony_psimetal");
	public static final TagKey<Item> BLOCK_EBONY_PSIMETAL = forgeTag("storage_blocks/ebony_psimetal");

	public static final TagKey<Item> INGOT_IVORY_PSIMETAL = forgeTag("ingots/ivory_psimetal");
	public static final TagKey<Item> BLOCK_IVORY_PSIMETAL = forgeTag("storage_blocks/ivory_psimetal");

	private static TagKey<Item> tag(String name) {
		return TagKey.create(Registry.ITEM_REGISTRY, prefix(name));
	}

	private static TagKey<Item> forgeTag(String name) {
		return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", name));
	}

	public static ResourceLocation prefix(String path) {
		return new ResourceLocation(LibMisc.MOD_ID, path);
	}

	public static class Blocks {
		public static final TagKey<Block> BLOCK_PSIMETAL = fromTag(ModTags.BLOCK_PSIMETAL);
		public static final TagKey<Block> BLOCK_PSIGEM = fromTag(ModTags.BLOCK_PSIGEM);
		public static final TagKey<Block> BLOCK_EBONY_PSIMETAL = fromTag(ModTags.BLOCK_EBONY_PSIMETAL);
		public static final TagKey<Block> BLOCK_IVORY_PSIMETAL = fromTag(ModTags.BLOCK_IVORY_PSIMETAL);

		private static TagKey<Block> fromTag(TagKey<?> tag) {
			return TagKey.create(Registry.BLOCK_REGISTRY, tag.location());
		}
	}
}

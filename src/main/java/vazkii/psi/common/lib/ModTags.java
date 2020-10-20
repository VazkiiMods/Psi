/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.lib;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTags {

	public static final ITag.INamedTag<Item> PSIDUST = tag("psidust");
	public static final ITag.INamedTag<Item> IVORY_SUBSTANCE = tag("ivory_substance");
	public static final ITag.INamedTag<Item> EBONY_SUBSTANCE = tag("ebony_substance");

	public static final ITag.INamedTag<Item> INGOT_PSIMETAL = forgeTag("ingots/psimetal");
	public static final ITag.INamedTag<Item> BLOCK_PSIMETAL = forgeTag("storage_blocks/psimetal");

	public static final ITag.INamedTag<Item> GEM_PSIGEM = forgeTag("gems/psigem");
	public static final ITag.INamedTag<Item> BLOCK_PSIGEM = forgeTag("storage_blocks/psigem");

	public static final ITag.INamedTag<Item> INGOT_EBONY_PSIMETAL = forgeTag("ingots/ebony_psimetal");
	public static final ITag.INamedTag<Item> BLOCK_EBONY_PSIMETAL = forgeTag("storage_blocks/ebony_psimetal");

	public static final ITag.INamedTag<Item> INGOT_IVORY_PSIMETAL = forgeTag("ingots/ivory_psimetal");
	public static final ITag.INamedTag<Item> BLOCK_IVORY_PSIMETAL = forgeTag("storage_blocks/ivory_psimetal");

	private static ITag.INamedTag<Item> tag(String name) {
		return ItemTags.makeWrapperTag(prefix(name).toString());
	}

	private static ITag.INamedTag<Item> forgeTag(String name) {
		return ItemTags.makeWrapperTag(new ResourceLocation("forge", name).toString());
	}

	public static ResourceLocation prefix(String path) {
		return new ResourceLocation(LibMisc.MOD_ID, path);
	}

	public static class Blocks {
		public static final ITag.INamedTag<Block> BLOCK_PSIMETAL = fromTag(ModTags.BLOCK_PSIMETAL);
		public static final ITag.INamedTag<Block> BLOCK_PSIGEM = fromTag(ModTags.BLOCK_PSIGEM);
		public static final ITag.INamedTag<Block> BLOCK_EBONY_PSIMETAL = fromTag(ModTags.BLOCK_EBONY_PSIMETAL);
		public static final ITag.INamedTag<Block> BLOCK_IVORY_PSIMETAL = fromTag(ModTags.BLOCK_IVORY_PSIMETAL);

		private static ITag.INamedTag<Block> fromTag(ITag.INamedTag<?> tag) {
			return BlockTags.makeWrapperTag(tag.getName().toString());
		}
	}
}

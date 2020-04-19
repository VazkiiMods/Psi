/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.lib;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModTags {

	public static final Tag<Item> PSIDUST = tag("psidust");
	public static final Tag<Item> IVORY_SUBSTANCE = tag("ivory_substance");
	public static final Tag<Item> EBONY_SUBSTANCE = tag("ebony_substance");

	public static final Tag<Item> INGOT_PSIMETAL = forgeTag("ingots/psimetal");
	public static final Tag<Item> BLOCK_PSIMETAL = forgeTag("storage_blocks/psimetal");

	public static final Tag<Item> GEM_PSIGEM = forgeTag("gems/psigem");
	public static final Tag<Item> BLOCK_PSIGEM = forgeTag("storage_blocks/psigem");

	public static final Tag<Item> INGOT_EBONY_PSIMETAL = forgeTag("ingots/ebony_psimetal");
	public static final Tag<Item> BLOCK_EBONY_PSIMETAL = forgeTag("storage_blocks/ebony_psimetal");

	public static final Tag<Item> INGOT_IVORY_PSIMETAL = forgeTag("ingots/ivory_psimetal");
	public static final Tag<Item> BLOCK_IVORY_PSIMETAL = forgeTag("storage_blocks/ivory_psimetal");

	private static Tag<Item> tag(String name) {
		return new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, name));
	}

	private static Tag<Item> forgeTag(String name) {
		return new ItemTags.Wrapper(new ResourceLocation("forge", name));
	}

	public static class Blocks {
		public static final Tag<Block> BLOCK_PSIMETAL = fromTag(ModTags.BLOCK_PSIMETAL);
		public static final Tag<Block> BLOCK_PSIGEM = fromTag(ModTags.BLOCK_PSIGEM);
		public static final Tag<Block> BLOCK_EBONY_PSIMETAL = fromTag(ModTags.BLOCK_EBONY_PSIMETAL);
		public static final Tag<Block> BLOCK_IVORY_PSIMETAL = fromTag(ModTags.BLOCK_IVORY_PSIMETAL);

		private static Tag<Block> fromTag(Tag<?> tag) {
			return new BlockTags.Wrapper(tag.getId());
		}
	}
}

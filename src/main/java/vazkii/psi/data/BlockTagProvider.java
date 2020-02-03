package vazkii.psi.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.lib.ModTags;

public class BlockTagProvider extends BlockTagsProvider {
	public BlockTagProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void registerTags() {
		getBuilder(ModTags.Blocks.BLOCK_PSIMETAL).add(ModBlocks.psimetalBlock);
		getBuilder(ModTags.Blocks.BLOCK_PSIGEM).add(ModBlocks.psigemBlock);
		getBuilder(ModTags.Blocks.BLOCK_EBONY_PSIMETAL).add(ModBlocks.psimetalEbony);
		getBuilder(ModTags.Blocks.BLOCK_IVORY_PSIMETAL).add(ModBlocks.psimetalIvory);
	}

	@Override
	public String getName() {
		return "Psi block tags";
	}
}

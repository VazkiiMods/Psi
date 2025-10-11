package vazkii.psi.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.common.block.base.ModBlocks;

import java.util.Set;
import java.util.stream.Collectors;

public class PsiBlockLootProvider extends BlockLootSubProvider {

	protected PsiBlockLootProvider(HolderLookup.Provider registries) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
	}

	@Override
	protected void generate() {
		dropSelf(ModBlocks.cadAssembler.get());
		dropSelf(ModBlocks.programmer.get());
		dropSelf(ModBlocks.psidustBlock.get());
		dropSelf(ModBlocks.psimetalBlock.get());
		dropSelf(ModBlocks.psigemBlock.get());
		dropSelf(ModBlocks.psimetalPlateBlack.get());
		dropSelf(ModBlocks.psimetalPlateBlackLight.get());
		dropSelf(ModBlocks.psimetalPlateWhite.get());
		dropSelf(ModBlocks.psimetalPlateWhiteLight.get());
		dropSelf(ModBlocks.psimetalEbony.get());
		dropSelf(ModBlocks.psimetalIvory.get());
	}

	@Override
	protected @NotNull Iterable<Block> getKnownBlocks() {
		return ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toList());
	}
}

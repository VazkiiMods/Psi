package vazkii.psi.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.common.block.base.ModBlocks;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PsiBlockLootProvider extends BlockLootSubProvider {

	public PsiBlockLootProvider(HolderLookup.Provider registries) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
	}

	@Override
	public void generate() {
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

	public Map<ResourceKey<LootTable>, LootTable.Builder> generatedLootTables() {
		return map;
	}

	protected @NotNull Iterable<Block> getKnownBlocks() {
		return List.of(
				ModBlocks.cadAssembler.get(),
				ModBlocks.programmer.get(),
				ModBlocks.psidustBlock.get(),
				ModBlocks.psimetalBlock.get(),
				ModBlocks.psigemBlock.get(),
				ModBlocks.psimetalPlateBlack.get(),
				ModBlocks.psimetalPlateBlackLight.get(),
				ModBlocks.psimetalPlateWhite.get(),
				ModBlocks.psimetalPlateWhiteLight.get(),
				ModBlocks.psimetalEbony.get(),
				ModBlocks.psimetalIvory.get());
	}
}

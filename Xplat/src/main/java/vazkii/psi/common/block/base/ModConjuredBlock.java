/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.base;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.tile.TileConjured;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.platform.PsiBlockEntityTypes;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

public final class ModConjuredBlock {
	private static final BlockBehaviour.StateArgumentPredicate<EntityType<?>> NO_SPAWN =
			(state, world, pos, entityType) -> false;
	private static final BlockBehaviour.StatePredicate NO_SUFFOCATION =
			(state, world, pos) -> false;

	public static final RegistryEntry<BlockConjured> BLOCK = PsiRegistries.register(
			BuiltInRegistries.BLOCK, PsiAPI.location(LibBlockNames.CONJURED),
			() -> new BlockConjured(BlockBehaviour.Properties.of()
					.mapColor(MapColor.NONE)
					.instrument(NoteBlockInstrument.HAT)
					.instabreak()
					.sound(SoundType.GLASS)
					.noOcclusion()
					.noLootTable()
					.lightLevel(state -> state.getValue(BlockConjured.LIGHT) ? 15 : 0)
					.isValidSpawn(NO_SPAWN)
					.isRedstoneConductor(NO_SUFFOCATION)
					.isSuffocating(NO_SUFFOCATION)
					.isViewBlocking(NO_SUFFOCATION)));

	public static final RegistryEntry<BlockEntityType<TileConjured>> TYPE = PsiRegistries.register(
			BuiltInRegistries.BLOCK_ENTITY_TYPE, PsiAPI.location(LibBlockNames.CONJURED),
			() -> PsiBlockEntityTypes.create(TileConjured::new, BLOCK.get()));

	private ModConjuredBlock() {}

	public static void register() {}
}

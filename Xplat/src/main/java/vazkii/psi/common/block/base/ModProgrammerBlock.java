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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.platform.PsiBlockEntityTypes;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

public final class ModProgrammerBlock {

	public static final RegistryEntry<BlockProgrammer> BLOCK = PsiRegistries.register(
			BuiltInRegistries.BLOCK, PsiAPI.location(LibBlockNames.PROGRAMMER),
			() -> new BlockProgrammer(BlockBehaviour.Properties.of()
					.mapColor(MapColor.METAL)
					.instrument(NoteBlockInstrument.IRON_XYLOPHONE)
					.requiresCorrectToolForDrops()
					.strength(5, 10)
					.sound(SoundType.METAL)
					.noOcclusion()));

	public static final RegistryEntry<BlockEntityType<TileProgrammer>> TYPE = PsiRegistries.register(
			BuiltInRegistries.BLOCK_ENTITY_TYPE, PsiAPI.location(LibBlockNames.PROGRAMMER),
			() -> PsiBlockEntityTypes.create(TileProgrammer::new, BLOCK.get()));

	public static final RegistryEntry<Item> ITEM = PsiRegistries.register(
			BuiltInRegistries.ITEM, PsiAPI.location(LibBlockNames.PROGRAMMER),
			() -> new BlockItem(BLOCK.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));

	private ModProgrammerBlock() {}

	public static void register() {}
}

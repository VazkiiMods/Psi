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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

public final class ModBasicBlocks {

	public static final RegistryEntry<Block> psidustBlock = register(LibBlockNames.PSIDUST_BLOCK, false);
	public static final RegistryEntry<Block> psimetalBlock = register(LibBlockNames.PSIMETAL_BLOCK, false);
	public static final RegistryEntry<Block> psigemBlock = register(LibBlockNames.PSIGEM_BLOCK, false);
	public static final RegistryEntry<Block> psimetalPlateBlack = register(LibBlockNames.PSIMETAL_PLATE_BLACK, false);
	public static final RegistryEntry<Block> psimetalPlateBlackLight = register(LibBlockNames.PSIMETAL_PLATE_BLACK_LIGHT, true);
	public static final RegistryEntry<Block> psimetalPlateWhite = register(LibBlockNames.PSIMETAL_PLATE_WHITE, false);
	public static final RegistryEntry<Block> psimetalPlateWhiteLight = register(LibBlockNames.PSIMETAL_PLATE_WHITE_LIGHT, true);
	public static final RegistryEntry<Block> psimetalEbony = register(LibBlockNames.EBONY_PSIMETAL_BLOCK, false);
	public static final RegistryEntry<Block> psimetalIvory = register(LibBlockNames.IVORY_PSIMETAL_BLOCK, false);

	private ModBasicBlocks() {}

	private static RegistryEntry<Block> register(String name, boolean lit) {
		return PsiRegistries.register(BuiltInRegistries.BLOCK, PsiAPI.location(name),
				() -> new Block(properties(lit)));
	}

	private static BlockBehaviour.Properties properties(boolean lit) {
		BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
				.mapColor(MapColor.METAL)
				.instrument(NoteBlockInstrument.IRON_XYLOPHONE)
				.requiresCorrectToolForDrops()
				.strength(5, 10)
				.sound(SoundType.METAL);
		return lit ? properties.lightLevel(state -> 15) : properties;
	}

	public static void register() {}
}

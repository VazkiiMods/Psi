/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.base;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;
import vazkii.psi.common.registry.RegistryEntry;

public class ModBlocks {
	public static final RegistryEntry<vazkii.psi.common.block.BlockCADAssembler> cadAssembler = ModCADAssemblerBlock.BLOCK;
	public static final RegistryEntry<vazkii.psi.common.block.BlockProgrammer> programmer = ModProgrammerBlock.BLOCK;
	public static final RegistryEntry<Block> psidustBlock = ModBasicBlocks.psidustBlock;
	public static final RegistryEntry<Block> psimetalBlock = ModBasicBlocks.psimetalBlock;
	public static final RegistryEntry<Block> psigemBlock = ModBasicBlocks.psigemBlock;
	public static final RegistryEntry<Block> psimetalPlateBlack = ModBasicBlocks.psimetalPlateBlack;
	public static final RegistryEntry<Block> psimetalPlateBlackLight = ModBasicBlocks.psimetalPlateBlackLight;
	public static final RegistryEntry<Block> psimetalPlateWhite = ModBasicBlocks.psimetalPlateWhite;
	public static final RegistryEntry<Block> psimetalPlateWhiteLight = ModBasicBlocks.psimetalPlateWhiteLight;
	public static final RegistryEntry<Block> psimetalEbony = ModBasicBlocks.psimetalEbony;
	public static final RegistryEntry<Block> psimetalIvory = ModBasicBlocks.psimetalIvory;
	public static final RegistryEntry<MenuType<ContainerCADAssembler>> containerCADAssembler = ModCADAssemblerBlock.MENU;
	public static final RegistryEntry<net.minecraft.world.level.block.entity.BlockEntityType<TileCADAssembler>> cadAssemblerType = ModCADAssemblerBlock.TYPE;
	public static final RegistryEntry<BlockEntityType<TileProgrammer>> programmerType = ModProgrammerBlock.TYPE;

}

/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.base;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import vazkii.psi.common.block.BlockCADAssembler;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.TileConjured;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibMisc;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, LibMisc.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, LibMisc.MOD_ID);
	public static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(Registries.MENU, LibMisc.MOD_ID);

	private static final BlockBehaviour.StateArgumentPredicate<EntityType<?>> NO_SPAWN = (state, world, pos, et) -> false;
	private static final BlockBehaviour.StatePredicate NO_SUFFOCATION = (state, world, pos) -> false;

	public static final DeferredHolder<Block, BlockCADAssembler> cadAssembler = BLOCKS.register(LibBlockNames.CAD_ASSEMBLER, () -> new BlockCADAssembler(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL).noOcclusion()));
	public static final DeferredHolder<Block, BlockProgrammer> programmer = BLOCKS.register(LibBlockNames.PROGRAMMER, () -> new BlockProgrammer(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL).noOcclusion()));
	public static DeferredHolder<Block, BlockConjured> conjured = BLOCKS.register(LibBlockNames.CONJURED, () -> new BlockConjured(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().noLootTable().lightLevel(state -> state.getValue(BlockConjured.LIGHT) ? 15 : 0).noOcclusion().isValidSpawn(NO_SPAWN).isRedstoneConductor(NO_SUFFOCATION).isSuffocating(NO_SUFFOCATION).isViewBlocking(NO_SUFFOCATION)));
	public static DeferredHolder<Block, Block> psidustBlock = BLOCKS.register(LibBlockNames.PSIDUST_BLOCK, () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL)));
	public static DeferredHolder<Block, Block> psimetalBlock = BLOCKS.register(LibBlockNames.PSIMETAL_BLOCK, () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL)));
	public static DeferredHolder<Block, Block> psigemBlock = BLOCKS.register(LibBlockNames.PSIGEM_BLOCK, () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL)));
	public static DeferredHolder<Block, Block> psimetalPlateBlack = BLOCKS.register(LibBlockNames.PSIMETAL_PLATE_BLACK, () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL)));
	public static DeferredHolder<Block, Block> psimetalPlateBlackLight = BLOCKS.register(LibBlockNames.PSIMETAL_PLATE_BLACK_LIGHT, () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL).lightLevel((blockState) -> 15)));
	public static DeferredHolder<Block, Block> psimetalPlateWhite = BLOCKS.register(LibBlockNames.PSIMETAL_PLATE_WHITE, () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL)));
	public static DeferredHolder<Block, Block> psimetalPlateWhiteLight = BLOCKS.register(LibBlockNames.PSIMETAL_PLATE_WHITE_LIGHT, () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL).lightLevel((blockstate) -> 15)));
	public static DeferredHolder<Block, Block> psimetalEbony = BLOCKS.register(LibBlockNames.EBONY_PSIMETAL_BLOCK, () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL)));
	public static DeferredHolder<Block, Block> psimetalIvory = BLOCKS.register(LibBlockNames.IVORY_PSIMETAL_BLOCK, () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL)));

	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileCADAssembler>> cadAssemblerType = BLOCK_TYPES.register(LibBlockNames.CAD_ASSEMBLER, () -> BlockEntityType.Builder.of(TileCADAssembler::new, cadAssembler.get()).build(null));
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileProgrammer>> programmerType = BLOCK_TYPES.register(LibBlockNames.PROGRAMMER, () -> BlockEntityType.Builder.of(TileProgrammer::new, programmer.get()).build(null));
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileConjured>> conjuredType = BLOCK_TYPES.register(LibBlockNames.CONJURED, () -> BlockEntityType.Builder.of(TileConjured::new, conjured.get()).build(null));

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerCADAssembler>> containerCADAssembler = MENU.register(LibBlockNames.CAD_ASSEMBLER, () -> IMenuTypeExtension.create(ContainerCADAssembler::fromNetwork));
}

/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.base;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import vazkii.psi.client.gui.GuiCADAssembler;
import vazkii.psi.common.block.BlockCADAssembler;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.TileConjured;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibMisc;

import static vazkii.psi.common.item.base.ModItems.defaultBuilder;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {
	private static final BlockBehaviour.StateArgumentPredicate<EntityType<?>> NO_SPAWN = (state, world, pos, et) -> false;
	private static final BlockBehaviour.StatePredicate NO_SUFFOCATION = (state, world, pos) -> false;

	public static Block cadAssembler;
	public static Block programmer;
	public static Block conjured;
	public static Block psidustBlock;
	public static Block psimetalBlock;
	public static Block psigemBlock;
	public static Block psimetalPlateBlack;
	public static Block psimetalPlateBlackLight;
	public static Block psimetalPlateWhite;
	public static Block psimetalPlateWhiteLight;
	public static Block psimetalEbony;
	public static Block psimetalIvory;

	public static final MenuType<ContainerCADAssembler> containerCADAssembler = IForgeMenuType.create(ContainerCADAssembler::fromNetwork);

	@SubscribeEvent
	public static void register(RegisterEvent evt) {
		evt.register(ForgeRegistries.Keys.BLOCKS, helper -> {
			cadAssembler = new BlockCADAssembler(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(5, 10).sound(SoundType.METAL).noOcclusion());
			programmer = new BlockProgrammer(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(5, 10).sound(SoundType.METAL).noOcclusion());
			conjured = new BlockConjured(Block.Properties.of().mapColor(MapColor.NONE).instrument(NoteBlockInstrument.HAT).instabreak().sound(SoundType.GLASS).noOcclusion().noLootTable().lightLevel(state -> state.getValue(BlockConjured.LIGHT) ? 15 : 0).noOcclusion().isValidSpawn(NO_SPAWN).isRedstoneConductor(NO_SUFFOCATION).isSuffocating(NO_SUFFOCATION).isViewBlocking(NO_SUFFOCATION));
			psidustBlock = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(5, 10).sound(SoundType.METAL));
			psimetalBlock = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(5, 10).sound(SoundType.METAL));
			psigemBlock = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(5, 10).sound(SoundType.METAL));
			psimetalPlateBlack = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(5, 10).sound(SoundType.METAL));
			psimetalPlateBlackLight = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(5, 10).sound(SoundType.METAL).lightLevel((blockState) -> 15));
			psimetalPlateWhite = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(5, 10).sound(SoundType.METAL));
			psimetalPlateWhiteLight = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(5, 10).sound(SoundType.METAL).lightLevel((blockstate) -> 15));
			psimetalEbony = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(5, 10).sound(SoundType.METAL));
			psimetalIvory = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(5, 10).sound(SoundType.METAL));

			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.CAD_ASSEMBLER), cadAssembler);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.PROGRAMMER), programmer);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.CONJURED), conjured);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.PSIDUST_BLOCK), psidustBlock);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_BLOCK), psimetalBlock);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.PSIGEM_BLOCK), psigemBlock);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_PLATE_BLACK), psimetalPlateBlack);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_PLATE_BLACK_LIGHT), psimetalPlateBlackLight);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_PLATE_WHITE), psimetalPlateWhite);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_PLATE_WHITE_LIGHT), psimetalPlateWhiteLight);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.EBONY_PSIMETAL_BLOCK), psimetalEbony);
			helper.register(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.IVORY_PSIMETAL_BLOCK), psimetalIvory);
		});

		evt.register(ForgeRegistries.Keys.ITEMS, helper -> {
			helper.register(ForgeRegistries.BLOCKS.getKey(cadAssembler), new BlockItem(cadAssembler, defaultBuilder().rarity(Rarity.UNCOMMON)));
			helper.register(ForgeRegistries.BLOCKS.getKey(programmer), new BlockItem(programmer, defaultBuilder().rarity(Rarity.UNCOMMON)));
			helper.register(ForgeRegistries.BLOCKS.getKey(psidustBlock), new BlockItem(psidustBlock, defaultBuilder()));
			helper.register(ForgeRegistries.BLOCKS.getKey(psimetalBlock), new BlockItem(psimetalBlock, defaultBuilder()));
			helper.register(ForgeRegistries.BLOCKS.getKey(psigemBlock), new BlockItem(psigemBlock, defaultBuilder()));
			helper.register(ForgeRegistries.BLOCKS.getKey(psimetalPlateBlack), new BlockItem(psimetalPlateBlack, defaultBuilder()));
			helper.register(ForgeRegistries.BLOCKS.getKey(psimetalPlateBlackLight), new BlockItem(psimetalPlateBlackLight, defaultBuilder()));
			helper.register(ForgeRegistries.BLOCKS.getKey(psimetalPlateWhite), new BlockItem(psimetalPlateWhite, defaultBuilder()));
			helper.register(ForgeRegistries.BLOCKS.getKey(psimetalPlateWhiteLight), new BlockItem(psimetalPlateWhiteLight, defaultBuilder()));
			helper.register(ForgeRegistries.BLOCKS.getKey(psimetalEbony), new BlockItem(psimetalEbony, defaultBuilder()));
			helper.register(ForgeRegistries.BLOCKS.getKey(psimetalIvory), new BlockItem(psimetalIvory, defaultBuilder()));
		});

		evt.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, helper -> {
			helper.register(ForgeRegistries.BLOCKS.getKey(cadAssembler), BlockEntityType.Builder.of(TileCADAssembler::new, cadAssembler).build(null));
			helper.register(ForgeRegistries.BLOCKS.getKey(programmer), BlockEntityType.Builder.of(TileProgrammer::new, programmer).build(null));
			helper.register(ForgeRegistries.BLOCKS.getKey(conjured), BlockEntityType.Builder.of(TileConjured::new, conjured).build(null));
		});

		evt.register(ForgeRegistries.Keys.MENU_TYPES, helper -> {
			helper.register(ForgeRegistries.BLOCKS.getKey(cadAssembler), containerCADAssembler);
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				MenuScreens.register(containerCADAssembler, GuiCADAssembler::new);
			});
		});
	}
}

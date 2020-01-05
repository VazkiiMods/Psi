/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [09/01/2016, 23:13:36 (GMT)]
 */
package vazkii.psi.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.psi.common.block.BlockCADAssembler;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.BlockPsiDecorative;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.TileConjured;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibMisc;

import static vazkii.psi.common.item.base.ModItems.defaultBuilder;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ModBlocks {

	public static final Block cadAssembler = new BlockCADAssembler();
	public static final Block programmer = new BlockProgrammer();
	public static final Block conjured = new BlockConjured();
	public static final Block psidustBlock = new BlockPsiDecorative(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL));
	public static final Block psimetalBlock = new BlockPsiDecorative(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL));
	public static final Block psigemBlock = new BlockPsiDecorative(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL));
	public static final Block psimetalPlateBlack = new BlockPsiDecorative(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL));
	public static final Block psimetalPlateBlackLight = new BlockPsiDecorative(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL).lightValue(15));
	public static final Block psimetalPlateWhite = new BlockPsiDecorative(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL));
	public static final Block psimetalPlateWhiteLight = new BlockPsiDecorative(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL).lightValue(15));
	public static final Block psimetalEbony = new BlockPsiDecorative(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL));
	public static final Block psimetalIvory = new BlockPsiDecorative(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL));

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> evt) {
		IForgeRegistry<Block> r = evt.getRegistry();
		r.registerAll(cadAssembler, programmer, conjured);
		r.register(psidustBlock.setRegistryName(LibMisc.MOD_ID, LibBlockNames.PSIDUST_BLOCK));
		r.register(psimetalBlock.setRegistryName(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_BLOCK));
		r.register(psigemBlock.setRegistryName(LibMisc.MOD_ID, LibBlockNames.PSIGEM_BLOCK));
		r.register(psimetalPlateBlack.setRegistryName(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_PLATE_BLACK));
		r.register(psimetalPlateBlackLight.setRegistryName(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_PLATE_BLACK_LIGHT));
		r.register(psimetalPlateWhite.setRegistryName(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_PLATE_WHITE));
		r.register(psimetalPlateWhiteLight.setRegistryName(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_PLATE_WHITE_LIGHT));
		r.register(psimetalEbony.setRegistryName(LibMisc.MOD_ID, LibBlockNames.EBONY_PSIMETAL_BLOCK));
		r.register(psimetalIvory.setRegistryName(LibMisc.MOD_ID, LibBlockNames.IVORY_PSIMETAL_BLOCK));
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		r.register(new BlockItem(cadAssembler, defaultBuilder().rarity(Rarity.UNCOMMON)).setRegistryName(cadAssembler.getRegistryName()));
		r.register(new BlockItem(programmer, defaultBuilder().rarity(Rarity.UNCOMMON)).setRegistryName(programmer.getRegistryName()));
		r.register(new BlockItem(psidustBlock, defaultBuilder()).setRegistryName(psidustBlock.getRegistryName()));
		r.register(new BlockItem(psimetalBlock, defaultBuilder()).setRegistryName(psigemBlock.getRegistryName()));
		r.register(new BlockItem(psigemBlock, defaultBuilder()).setRegistryName(psigemBlock.getRegistryName()));
		r.register(new BlockItem(psimetalPlateBlack, defaultBuilder()).setRegistryName(psimetalPlateBlack.getRegistryName()));
		r.register(new BlockItem(psimetalPlateBlackLight, defaultBuilder()).setRegistryName(psimetalPlateBlackLight.getRegistryName()));
		r.register(new BlockItem(psimetalPlateWhite, defaultBuilder()).setRegistryName(psimetalPlateWhite.getRegistryName()));
		r.register(new BlockItem(psimetalPlateWhiteLight, defaultBuilder()).setRegistryName(psimetalPlateWhiteLight.getRegistryName()));
		r.register(new BlockItem(psimetalEbony, defaultBuilder()).setRegistryName(psimetalEbony.getRegistryName()));
		r.register(new BlockItem(psimetalIvory, defaultBuilder()).setRegistryName(psimetalIvory.getRegistryName()));
	}
	
	@SubscribeEvent
	public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt) {
		IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();
		r.register(TileEntityType.Builder.create(TileCADAssembler::new, cadAssembler).build(null).setRegistryName(cadAssembler.getRegistryName()));
		r.register(TileEntityType.Builder.create(TileProgrammer::new, programmer).build(null).setRegistryName(programmer.getRegistryName()));
		r.register(TileEntityType.Builder.create(TileConjured::new, conjured).build(null).setRegistryName(conjured.getRegistryName()));
	}

	@SubscribeEvent
	public static void registerContainers(RegistryEvent.Register<ContainerType<?>> evt) {
		evt.getRegistry().register(new ContainerType<>((IContainerFactory<ContainerCADAssembler>) ContainerCADAssembler::fromNetwork).setRegistryName(LibMisc.MOD_ID, LibBlockNames.CAD_ASSEMBLER));
	}

}

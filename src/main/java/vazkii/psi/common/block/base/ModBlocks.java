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

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    public static final Block cadAssembler = new BlockCADAssembler();
    public static final Block programmer = new BlockProgrammer();
    public static final Block conjured = new BlockConjured(LibBlockNames.CONJURED, Block.Properties.create(Material.GLASS).variableOpacity().noDrops());
    public static final Block psidustBlock = new BlockPsiDecorative(LibBlockNames.PSIDUST_BLOCK, Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL), defaultBuilder());
    public static final Block psimetalBlock = new BlockPsiDecorative(LibBlockNames.PSIMETAL_BLOCK, Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL), defaultBuilder());
    public static final Block psigemBlock = new BlockPsiDecorative(LibBlockNames.PSIGEM_BLOCK, Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL), defaultBuilder());
    public static final Block psimetalPlateBlack = new BlockPsiDecorative(LibBlockNames.PSIMETAL_PLATE_BLACK, Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL), defaultBuilder());
    public static final Block psimetalPlateBlackLight = new BlockPsiDecorative(LibBlockNames.PSIMETAL_PLATE_BLACK_LIGHT, Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL).lightValue(15), defaultBuilder());
    public static final Block psimetalPlateWhite = new BlockPsiDecorative(LibBlockNames.PSIMETAL_PLATE_WHITE, Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL), defaultBuilder());
    public static final Block psimetalPlateWhiteLight = new BlockPsiDecorative(LibBlockNames.PSIMETAL_PLATE_WHITE_LIGHT, Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL).lightValue(15), defaultBuilder());
    public static final Block psimetalEbony = new BlockPsiDecorative(LibBlockNames.EBONY_PSIMETAL_BLOCK, Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL), defaultBuilder());
    public static final Block psimetalIvory = new BlockPsiDecorative(LibBlockNames.IVORY_PSIMETAL_BLOCK, Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL), defaultBuilder());

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt) {
        IForgeRegistry<Block> r = evt.getRegistry();
        r.registerAll(cadAssembler, programmer);
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
        IForgeRegistry<Item> r = evt.getRegistry();
        r.register(new BlockItem(cadAssembler, defaultBuilder().rarity(Rarity.UNCOMMON)).setRegistryName(cadAssembler.getRegistryName()));
        r.register(new BlockItem(programmer, defaultBuilder().rarity(Rarity.UNCOMMON)).setRegistryName(programmer.getRegistryName()));
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

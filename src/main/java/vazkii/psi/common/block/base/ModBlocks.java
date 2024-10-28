/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.base;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.RegisterEvent;
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

@EventBusSubscriber(modid = LibMisc.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBlocks {
    public static final MenuType<ContainerCADAssembler> containerCADAssembler = IMenuTypeExtension.create(ContainerCADAssembler::fromNetwork);
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
    public static BlockEntityType<TileCADAssembler> cadAssemblerType;
    public static BlockEntityType<TileProgrammer> programmerType;
    public static BlockEntityType<TileConjured> conjuredType;

    @SubscribeEvent
    public static void register(RegisterEvent evt) {
        evt.register(Registries.BLOCK, helper -> {
            cadAssembler = new BlockCADAssembler(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL).noOcclusion());
            programmer = new BlockProgrammer(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL).noOcclusion());
            conjured = new BlockConjured(Block.Properties.of().mapColor(MapColor.NONE).instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().noLootTable().lightLevel(state -> state.getValue(BlockConjured.LIGHT) ? 15 : 0).noOcclusion().isValidSpawn(NO_SPAWN).isRedstoneConductor(NO_SUFFOCATION).isSuffocating(NO_SUFFOCATION).isViewBlocking(NO_SUFFOCATION));
            psidustBlock = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL));
            psimetalBlock = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL));
            psigemBlock = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL));
            psimetalPlateBlack = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL));
            psimetalPlateBlackLight = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL).lightLevel((blockState) -> 15));
            psimetalPlateWhite = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL));
            psimetalPlateWhiteLight = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL).lightLevel((blockstate) -> 15));
            psimetalEbony = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL));
            psimetalIvory = new Block(Block.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5, 10).sound(SoundType.METAL));

            helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibBlockNames.CAD_ASSEMBLER), cadAssembler);
            helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibBlockNames.PROGRAMMER), programmer);
            helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibBlockNames.CONJURED), conjured);
            helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibBlockNames.PSIDUST_BLOCK), psidustBlock);
            helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_BLOCK), psimetalBlock);
            helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibBlockNames.PSIGEM_BLOCK), psigemBlock);
            helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_PLATE_BLACK), psimetalPlateBlack);
            helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_PLATE_BLACK_LIGHT), psimetalPlateBlackLight);
            helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_PLATE_WHITE), psimetalPlateWhite);
            helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibBlockNames.PSIMETAL_PLATE_WHITE_LIGHT), psimetalPlateWhiteLight);
            helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibBlockNames.EBONY_PSIMETAL_BLOCK), psimetalEbony);
            helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibBlockNames.IVORY_PSIMETAL_BLOCK), psimetalIvory);
        });

        evt.register(Registries.ITEM, helper -> {
            helper.register(BuiltInRegistries.BLOCK.getKey(cadAssembler), new BlockItem(cadAssembler, defaultBuilder().rarity(Rarity.UNCOMMON)));
            helper.register(BuiltInRegistries.BLOCK.getKey(programmer), new BlockItem(programmer, defaultBuilder().rarity(Rarity.UNCOMMON)));
            helper.register(BuiltInRegistries.BLOCK.getKey(psidustBlock), new BlockItem(psidustBlock, defaultBuilder()));
            helper.register(BuiltInRegistries.BLOCK.getKey(psimetalBlock), new BlockItem(psimetalBlock, defaultBuilder()));
            helper.register(BuiltInRegistries.BLOCK.getKey(psigemBlock), new BlockItem(psigemBlock, defaultBuilder()));
            helper.register(BuiltInRegistries.BLOCK.getKey(psimetalPlateBlack), new BlockItem(psimetalPlateBlack, defaultBuilder()));
            helper.register(BuiltInRegistries.BLOCK.getKey(psimetalPlateBlackLight), new BlockItem(psimetalPlateBlackLight, defaultBuilder()));
            helper.register(BuiltInRegistries.BLOCK.getKey(psimetalPlateWhite), new BlockItem(psimetalPlateWhite, defaultBuilder()));
            helper.register(BuiltInRegistries.BLOCK.getKey(psimetalPlateWhiteLight), new BlockItem(psimetalPlateWhiteLight, defaultBuilder()));
            helper.register(BuiltInRegistries.BLOCK.getKey(psimetalEbony), new BlockItem(psimetalEbony, defaultBuilder()));
            helper.register(BuiltInRegistries.BLOCK.getKey(psimetalIvory), new BlockItem(psimetalIvory, defaultBuilder()));
        });

        evt.register(Registries.BLOCK_ENTITY_TYPE, helper -> {
            cadAssemblerType = BlockEntityType.Builder.of(TileCADAssembler::new, cadAssembler).build(null);
            programmerType = BlockEntityType.Builder.of(TileProgrammer::new, programmer).build(null);
            conjuredType = BlockEntityType.Builder.of(TileConjured::new, conjured).build(null);

            helper.register(BuiltInRegistries.BLOCK.getKey(cadAssembler), cadAssemblerType);
            helper.register(BuiltInRegistries.BLOCK.getKey(programmer), programmerType);
            helper.register(BuiltInRegistries.BLOCK.getKey(conjured), conjuredType);
        });

        evt.register(Registries.MENU, helper -> {
            helper.register(BuiltInRegistries.BLOCK.getKey(cadAssembler), containerCADAssembler);
        });
    }

    @SubscribeEvent
    public static void register(RegisterMenuScreensEvent evt) {
        evt.register(containerCADAssembler, GuiCADAssembler::new);
    }
}

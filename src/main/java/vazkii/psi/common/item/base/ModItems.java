/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.base;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.item.*;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitBoots;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitChestplate;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitHelmet;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitLeggings;
import vazkii.psi.common.item.component.*;
import vazkii.psi.common.item.tool.ItemPsimetalAxe;
import vazkii.psi.common.item.tool.ItemPsimetalPickaxe;
import vazkii.psi.common.item.tool.ItemPsimetalShovel;
import vazkii.psi.common.item.tool.ItemPsimetalSword;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.lib.LibMisc;

@SuppressWarnings("unused")
public final class ModItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, LibMisc.MOD_ID);

	public static final DeferredHolder<Item, Item> cadAssemblerItem = ITEMS.register(LibBlockNames.CAD_ASSEMBLER, () -> new BlockItem(ModBlocks.cadAssembler.get(), defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> programmerItem = ITEMS.register(LibBlockNames.PROGRAMMER, () -> new BlockItem(ModBlocks.programmer.get(), defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> psidustBlockItem = ITEMS.register(LibBlockNames.PSIDUST_BLOCK, () -> new BlockItem(ModBlocks.psidustBlock.get(), defaultBuilder()));
	public static final DeferredHolder<Item, Item> psimetalBlockItem = ITEMS.register(LibBlockNames.PSIMETAL_BLOCK, () -> new BlockItem(ModBlocks.psimetalBlock.get(), defaultBuilder()));
	public static final DeferredHolder<Item, Item> psigemBlockItem = ITEMS.register(LibBlockNames.PSIGEM_BLOCK, () -> new BlockItem(ModBlocks.psigemBlock.get(), defaultBuilder()));
	public static final DeferredHolder<Item, Item> psimetalPlateBlackItem = ITEMS.register(LibBlockNames.PSIMETAL_PLATE_BLACK, () -> new BlockItem(ModBlocks.psimetalPlateBlack.get(), defaultBuilder()));
	public static final DeferredHolder<Item, Item> psimetalPlateBlackLightItem = ITEMS.register(LibBlockNames.PSIMETAL_PLATE_BLACK_LIGHT, () -> new BlockItem(ModBlocks.psimetalPlateBlackLight.get(), defaultBuilder()));
	public static final DeferredHolder<Item, Item> psimetalPlateWhiteItem = ITEMS.register(LibBlockNames.PSIMETAL_PLATE_WHITE, () -> new BlockItem(ModBlocks.psimetalPlateWhite.get(), defaultBuilder()));
	public static final DeferredHolder<Item, Item> psimetalPlateWhiteLightItem = ITEMS.register(LibBlockNames.PSIMETAL_PLATE_WHITE_LIGHT, () -> new BlockItem(ModBlocks.psimetalPlateWhiteLight.get(), defaultBuilder()));
	public static final DeferredHolder<Item, Item> psimetalEbonyItem = ITEMS.register(LibBlockNames.EBONY_PSIMETAL_BLOCK, () -> new BlockItem(ModBlocks.psimetalEbony.get(), defaultBuilder()));
	public static final DeferredHolder<Item, Item> psimetalIvoryItem = ITEMS.register(LibBlockNames.IVORY_PSIMETAL_BLOCK, () -> new BlockItem(ModBlocks.psimetalIvory.get(), defaultBuilder()));

	public static final DeferredHolder<Item, Item> psidust = ITEMS.register(LibItemNames.PSIDUST, () -> new Item(defaultBuilder()));
	public static final DeferredHolder<Item, Item> psimetal = ITEMS.register(LibItemNames.PSIMETAL, () -> new Item(defaultBuilder()));
	public static final DeferredHolder<Item, Item> psigem = ITEMS.register(LibItemNames.PSIGEM, () -> new Item(defaultBuilder()));
	public static final DeferredHolder<Item, Item> ebonyPsimetal = ITEMS.register(LibItemNames.EBONY_PSIMETAL, () -> new Item(defaultBuilder()));
	public static final DeferredHolder<Item, Item> ivoryPsimetal = ITEMS.register(LibItemNames.IVORY_PSIMETAL, () -> new Item(defaultBuilder()));
	public static final DeferredHolder<Item, Item> ebonySubstance = ITEMS.register(LibItemNames.EBONY_SUBSTANCE, () -> new Item(defaultBuilder()));
	public static final DeferredHolder<Item, Item> ivorySubstance = ITEMS.register(LibItemNames.IVORY_SUBSTANCE, () -> new Item(defaultBuilder()));

	public static final DeferredHolder<Item, ItemCADAssembly> cadAssemblyIron = ITEMS.register(LibItemNames.CAD_ASSEMBLY_IRON, () -> new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_IRON));
	public static final DeferredHolder<Item, ItemCADAssembly> cadAssemblyGold = ITEMS.register(LibItemNames.CAD_ASSEMBLY_GOLD, () -> new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_GOLD));
	public static final DeferredHolder<Item, ItemCADAssembly> cadAssemblyPsimetal = ITEMS.register(LibItemNames.CAD_ASSEMBLY_PSIMETAL, () -> new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_PSIMETAL));
	public static final DeferredHolder<Item, ItemCADAssembly> cadAssemblyIvory = ITEMS.register(LibItemNames.CAD_ASSEMBLY_IVORY_PSIMETAL, () -> new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_IVORY_PSIMETAL));
	public static final DeferredHolder<Item, ItemCADAssembly> cadAssemblyEbony = ITEMS.register(LibItemNames.CAD_ASSEMBLY_EBONY_PSIMETAL, () -> new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_EBONY_PSIMETAL));
	public static final DeferredHolder<Item, ItemCADAssembly> cadAssemblyCreative = ITEMS.register(LibItemNames.CAD_ASSEMBLY_CREATIVE, () -> new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_CREATIVE));

	public static final DeferredHolder<Item, ItemCADCore> cadCoreBasic = ITEMS.register(LibItemNames.CAD_CORE_BASIC, () -> new ItemCADCore(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCADCore> cadCoreOverclocked = ITEMS.register(LibItemNames.CAD_CORE_OVERCLOCKED, () -> new ItemCADCore(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCADCore> cadCoreConductive = ITEMS.register(LibItemNames.CAD_CORE_CONDUCTIVE, () -> new ItemCADCore(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCADCore> cadCoreHyperClocked = ITEMS.register(LibItemNames.CAD_CORE_HYPERCLOCKED, () -> new ItemCADCore(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCADCore> cadCoreRadiative = ITEMS.register(LibItemNames.CAD_CORE_RADIATIVE, () -> new ItemCADCore(defaultBuilder()));

	public static final DeferredHolder<Item, ItemCADSocket> cadSocketBasic = ITEMS.register(LibItemNames.CAD_SOCKET_BASIC, () -> new ItemCADSocket(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCADSocket> cadSocketSignaling = ITEMS.register(LibItemNames.CAD_SOCKET_SIGNALING, () -> new ItemCADSocket(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCADSocket> cadSocketLarge = ITEMS.register(LibItemNames.CAD_SOCKET_LARGE, () -> new ItemCADSocket(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCADSocket> cadSocketTransmissive = ITEMS.register(LibItemNames.CAD_SOCKET_TRANSMISSIVE, () -> new ItemCADSocket(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCADSocket> cadSocketHuge = ITEMS.register(LibItemNames.CAD_SOCKET_HUGE, () -> new ItemCADSocket(defaultBuilder()));

	public static final DeferredHolder<Item, ItemCADBattery> cadBatteryBasic = ITEMS.register(LibItemNames.CAD_BATTERY_BASIC, () -> new ItemCADBattery(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCADBattery> cadBatteryExtended = ITEMS.register(LibItemNames.CAD_BATTERY_EXTENDED, () -> new ItemCADBattery(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCADBattery> cadBatteryUltradense = ITEMS.register(LibItemNames.CAD_BATTERY_ULTRADENSE, () -> new ItemCADBattery(defaultBuilder()));

	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerWhite = ITEMS.register(LibItemNames.CAD_COLORIZER_WHITE, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.WHITE));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerOrange = ITEMS.register(LibItemNames.CAD_COLORIZER_ORANGE, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.ORANGE));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerMagenta = ITEMS.register(LibItemNames.CAD_COLORIZER_MAGENTA, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.MAGENTA));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerLightBlue = ITEMS.register(LibItemNames.CAD_COLORIZER_LIGHT_BLUE, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.LIGHT_BLUE));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerYellow = ITEMS.register(LibItemNames.CAD_COLORIZER_YELLOW, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.YELLOW));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerLime = ITEMS.register(LibItemNames.CAD_COLORIZER_LIME, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.LIME));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerPink = ITEMS.register(LibItemNames.CAD_COLORIZER_PINK, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.PINK));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerGray = ITEMS.register(LibItemNames.CAD_COLORIZER_GRAY, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.GRAY));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerLightGray = ITEMS.register(LibItemNames.CAD_COLORIZER_LIGHT_GRAY, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.LIGHT_GRAY));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerCyan = ITEMS.register(LibItemNames.CAD_COLORIZER_CYAN, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.CYAN));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerPurple = ITEMS.register(LibItemNames.CAD_COLORIZER_PURPLE, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.PURPLE));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerBlue = ITEMS.register(LibItemNames.CAD_COLORIZER_BLUE, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.BLUE));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerBrown = ITEMS.register(LibItemNames.CAD_COLORIZER_BROWN, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.BROWN));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerGreen = ITEMS.register(LibItemNames.CAD_COLORIZER_GREEN, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.GREEN));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerRed = ITEMS.register(LibItemNames.CAD_COLORIZER_RED, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.RED));
	public static final DeferredHolder<Item, ItemCADColorizer> cadColorizerBlack = ITEMS.register(LibItemNames.CAD_COLORIZER_BLACK, () -> new ItemCADColorizer(defaultBuilder(), DyeColor.BLACK));
	public static final DeferredHolder<Item, ItemCADColorizerRainbow> cadColorizerRainbow = ITEMS.register(LibItemNames.CAD_COLORIZER_RAINBOW, () -> new ItemCADColorizerRainbow(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCADColorizerPsi> cadColorizerPsi = ITEMS.register(LibItemNames.CAD_COLORIZER_PSI, () -> new ItemCADColorizerPsi(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCADColorizerEmpty> cadColorizerEmpty = ITEMS.register(LibItemNames.CAD_COLORIZER_EMPTY, () -> new ItemCADColorizerEmpty(defaultBuilder()));

	public static final DeferredHolder<Item, ItemFlashRing> flashRing = ITEMS.register(LibItemNames.FLASH_RING, () -> new ItemFlashRing(defaultBuilder()));

	public static final DeferredHolder<Item, ItemSpellBullet> spellBullet = ITEMS.register(LibItemNames.SPELL_BULLET, () -> new ItemSpellBullet(defaultBuilder()));
	public static final DeferredHolder<Item, ItemProjectileSpellBullet> projectileSpellBullet = ITEMS.register(LibItemNames.SPELL_BULLET_PROJECTILE, () -> new ItemProjectileSpellBullet(defaultBuilder()));
	public static final DeferredHolder<Item, ItemLoopcastSpellBullet> loopSpellBullet = ITEMS.register(LibItemNames.SPELL_BULLET_LOOP, () -> new ItemLoopcastSpellBullet(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCircleSpellBullet> circleSpellBullet = ITEMS.register(LibItemNames.SPELL_BULLET_CIRCLE, () -> new ItemCircleSpellBullet(defaultBuilder()));
	public static final DeferredHolder<Item, ItemGrenadeSpellBullet> grenadeSpellBullet = ITEMS.register(LibItemNames.SPELL_BULLET_GRENADE, () -> new ItemGrenadeSpellBullet(defaultBuilder()));
	public static final DeferredHolder<Item, ItemChargeSpellBullet> chargeSpellBullet = ITEMS.register(LibItemNames.SPELL_BULLET_CHARGE, () -> new ItemChargeSpellBullet(defaultBuilder()));
	public static final DeferredHolder<Item, ItemMineSpellBullet> mineSpellBullet = ITEMS.register(LibItemNames.SPELL_BULLET_MINE, () -> new ItemMineSpellBullet(defaultBuilder()));

	public static final DeferredHolder<Item, ItemSpellDrive> spellDrive = ITEMS.register(LibItemNames.SPELL_DRIVE, () -> new ItemSpellDrive(defaultBuilder()));
	public static final DeferredHolder<Item, ItemDetonator> detonator = ITEMS.register(LibItemNames.DETONATOR, () -> new ItemDetonator(defaultBuilder()));
	public static final DeferredHolder<Item, ItemExosuitController> exosuitController = ITEMS.register(LibItemNames.EXOSUIT_CONTROLLER, () -> new ItemExosuitController(defaultBuilder()));

	public static final DeferredHolder<Item, ItemLightExosuitSensor> exosuitSensorLight = ITEMS.register(LibItemNames.EXOSUIT_SENSOR_LIGHT, () -> new ItemLightExosuitSensor(defaultBuilder()));
	public static final DeferredHolder<Item, ItemHeatExosuitSensor> exosuitSensorHeat = ITEMS.register(LibItemNames.EXOSUIT_SENSOR_HEAT, () -> new ItemHeatExosuitSensor(defaultBuilder()));
	public static final DeferredHolder<Item, ItemStressExosuitSensor> exosuitSensorStress = ITEMS.register(LibItemNames.EXOSUIT_SENSOR_STRESS, () -> new ItemStressExosuitSensor(defaultBuilder()));
	public static final DeferredHolder<Item, ItemWaterExosuitSensor> exosuitSensorWater = ITEMS.register(LibItemNames.EXOSUIT_SENSOR_WATER, () -> new ItemWaterExosuitSensor(defaultBuilder()));
	public static final DeferredHolder<Item, ItemTriggerExosuitSensor> exosuitSensorTrigger = ITEMS.register(LibItemNames.EXOSUIT_SENSOR_TRIGGER, () -> new ItemTriggerExosuitSensor(defaultBuilder()));
	public static final DeferredHolder<Item, ItemCAD> cad = ITEMS.register(LibItemNames.CAD, () -> new ItemCAD(defaultBuilder()));

	public static final DeferredHolder<Item, ItemVectorRuler> vectorRuler = ITEMS.register(LibItemNames.VECTOR_RULER, () -> new ItemVectorRuler(defaultBuilder()));
	public static final DeferredHolder<Item, ItemPsimetalShovel> psimetalShovel = ITEMS.register(LibItemNames.PSIMETAL_SHOVEL, () -> new ItemPsimetalShovel(defaultBuilder()));
	public static final DeferredHolder<Item, ItemPsimetalPickaxe> psimetalPickaxe = ITEMS.register(LibItemNames.PSIMETAL_PICKAXE, () -> new ItemPsimetalPickaxe(defaultBuilder()));
	public static final DeferredHolder<Item, ItemPsimetalAxe> psimetalAxe = ITEMS.register(LibItemNames.PSIMETAL_AXE, () -> new ItemPsimetalAxe(defaultBuilder()));
	public static final DeferredHolder<Item, ItemPsimetalSword> psimetalSword = ITEMS.register(LibItemNames.PSIMETAL_SWORD, () -> new ItemPsimetalSword(defaultBuilder()));

	public static final DeferredHolder<Item, ItemPsimetalExosuitHelmet> psimetalExosuitHelmet = ITEMS.register(LibItemNames.PSIMETAL_EXOSUIT_HELMET, () -> new ItemPsimetalExosuitHelmet(ArmorItem.Type.HELMET, defaultBuilder().durability(ArmorItem.Type.HELMET.getDurability(18))));
	public static final DeferredHolder<Item, ItemPsimetalExosuitChestplate> psimetalExosuitChestplate = ITEMS.register(LibItemNames.PSIMETAL_EXOSUIT_CHESTPLATE, () -> new ItemPsimetalExosuitChestplate(ArmorItem.Type.CHESTPLATE, defaultBuilder().durability(ArmorItem.Type.CHESTPLATE.getDurability(18))));
	public static final DeferredHolder<Item, ItemPsimetalExosuitLeggings> psimetalExosuitLeggings = ITEMS.register(LibItemNames.PSIMETAL_EXOSUIT_LEGGINGS, () -> new ItemPsimetalExosuitLeggings(ArmorItem.Type.LEGGINGS, defaultBuilder().durability(ArmorItem.Type.LEGGINGS.getDurability(18))));
	public static final DeferredHolder<Item, ItemPsimetalExosuitBoots> psimetalExosuitBoots = ITEMS.register(LibItemNames.PSIMETAL_EXOSUIT_BOOTS, () -> new ItemPsimetalExosuitBoots(ArmorItem.Type.BOOTS, defaultBuilder().durability(ArmorItem.Type.BOOTS.getDurability(18))));

	public static Item.Properties defaultBuilder() {
		return new Item.Properties();
	}
}

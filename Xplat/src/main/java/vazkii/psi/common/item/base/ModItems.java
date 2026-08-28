/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.base;

import net.minecraft.world.item.*;

import vazkii.psi.common.block.base.ModCADAssemblerBlock;
import vazkii.psi.common.block.base.ModProgrammerBlock;
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
import vazkii.psi.common.registry.RegistryEntry;

@SuppressWarnings("unused")
public final class ModItems {
	public static final RegistryEntry<Item> cadAssemblerItem = ModCADAssemblerBlock.ITEM;
	public static final RegistryEntry<Item> programmerItem = ModProgrammerBlock.ITEM;
	public static final RegistryEntry<Item> psidustBlockItem = ModBasicItems.psidustBlockItem;
	public static final RegistryEntry<Item> psimetalBlockItem = ModBasicItems.psimetalBlockItem;
	public static final RegistryEntry<Item> psigemBlockItem = ModBasicItems.psigemBlockItem;
	public static final RegistryEntry<Item> psimetalPlateBlackItem = ModBasicItems.psimetalPlateBlackItem;
	public static final RegistryEntry<Item> psimetalPlateBlackLightItem = ModBasicItems.psimetalPlateBlackLightItem;
	public static final RegistryEntry<Item> psimetalPlateWhiteItem = ModBasicItems.psimetalPlateWhiteItem;
	public static final RegistryEntry<Item> psimetalPlateWhiteLightItem = ModBasicItems.psimetalPlateWhiteLightItem;
	public static final RegistryEntry<Item> psimetalEbonyItem = ModBasicItems.psimetalEbonyItem;
	public static final RegistryEntry<Item> psimetalIvoryItem = ModBasicItems.psimetalIvoryItem;

	public static final RegistryEntry<Item> psidust = ModBasicItems.psidust;
	public static final RegistryEntry<Item> psimetal = ModBasicItems.psimetal;
	public static final RegistryEntry<Item> psigem = ModBasicItems.psigem;
	public static final RegistryEntry<Item> ebonyPsimetal = ModBasicItems.ebonyPsimetal;
	public static final RegistryEntry<Item> ivoryPsimetal = ModBasicItems.ivoryPsimetal;
	public static final RegistryEntry<Item> ebonySubstance = ModBasicItems.ebonySubstance;
	public static final RegistryEntry<Item> ivorySubstance = ModBasicItems.ivorySubstance;

	public static final RegistryEntry<ItemCADAssembly> cadAssemblyIron = ModCADComponents.cadAssemblyIron;
	public static final RegistryEntry<ItemCADAssembly> cadAssemblyGold = ModCADComponents.cadAssemblyGold;
	public static final RegistryEntry<ItemCADAssembly> cadAssemblyPsimetal = ModCADComponents.cadAssemblyPsimetal;
	public static final RegistryEntry<ItemCADAssembly> cadAssemblyIvory = ModCADComponents.cadAssemblyIvory;
	public static final RegistryEntry<ItemCADAssembly> cadAssemblyEbony = ModCADComponents.cadAssemblyEbony;
	public static final RegistryEntry<ItemCADAssembly> cadAssemblyCreative = ModCADComponents.cadAssemblyCreative;

	public static final RegistryEntry<ItemCADCore> cadCoreBasic = ModCADComponents.cadCoreBasic;
	public static final RegistryEntry<ItemCADCore> cadCoreOverclocked = ModCADComponents.cadCoreOverclocked;
	public static final RegistryEntry<ItemCADCore> cadCoreConductive = ModCADComponents.cadCoreConductive;
	public static final RegistryEntry<ItemCADCore> cadCoreHyperClocked = ModCADComponents.cadCoreHyperClocked;
	public static final RegistryEntry<ItemCADCore> cadCoreRadiative = ModCADComponents.cadCoreRadiative;

	public static final RegistryEntry<ItemCADSocket> cadSocketBasic = ModCADComponents.cadSocketBasic;
	public static final RegistryEntry<ItemCADSocket> cadSocketSignaling = ModCADComponents.cadSocketSignaling;
	public static final RegistryEntry<ItemCADSocket> cadSocketLarge = ModCADComponents.cadSocketLarge;
	public static final RegistryEntry<ItemCADSocket> cadSocketTransmissive = ModCADComponents.cadSocketTransmissive;
	public static final RegistryEntry<ItemCADSocket> cadSocketHuge = ModCADComponents.cadSocketHuge;

	public static final RegistryEntry<ItemCADBattery> cadBatteryBasic = ModCADComponents.cadBatteryBasic;
	public static final RegistryEntry<ItemCADBattery> cadBatteryExtended = ModCADComponents.cadBatteryExtended;
	public static final RegistryEntry<ItemCADBattery> cadBatteryUltradense = ModCADComponents.cadBatteryUltradense;

	public static final RegistryEntry<ItemCADColorizer> cadColorizerWhite = ModCADComponents.cadColorizerWhite;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerOrange = ModCADComponents.cadColorizerOrange;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerMagenta = ModCADComponents.cadColorizerMagenta;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerLightBlue = ModCADComponents.cadColorizerLightBlue;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerYellow = ModCADComponents.cadColorizerYellow;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerLime = ModCADComponents.cadColorizerLime;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerPink = ModCADComponents.cadColorizerPink;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerGray = ModCADComponents.cadColorizerGray;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerLightGray = ModCADComponents.cadColorizerLightGray;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerCyan = ModCADComponents.cadColorizerCyan;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerPurple = ModCADComponents.cadColorizerPurple;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerBlue = ModCADComponents.cadColorizerBlue;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerBrown = ModCADComponents.cadColorizerBrown;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerGreen = ModCADComponents.cadColorizerGreen;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerRed = ModCADComponents.cadColorizerRed;
	public static final RegistryEntry<ItemCADColorizer> cadColorizerBlack = ModCADComponents.cadColorizerBlack;
	public static final RegistryEntry<ItemCADColorizerRainbow> cadColorizerRainbow = ModCADComponents.cadColorizerRainbow;
	public static final RegistryEntry<ItemCADColorizerPsi> cadColorizerPsi = ModCADComponents.cadColorizerPsi;
	public static final RegistryEntry<ItemCADColorizerEmpty> cadColorizerEmpty = ModCADComponents.cadColorizerEmpty;

	public static final RegistryEntry<ItemFlashRing> flashRing = ModFlashRingItem.FLASH_RING;

	public static final RegistryEntry<ItemSpellBullet> spellBullet = ModSpellItems.spellBullet;
	public static final RegistryEntry<ItemProjectileSpellBullet> projectileSpellBullet = ModSpellItems.projectileSpellBullet;
	public static final RegistryEntry<ItemLoopcastSpellBullet> loopSpellBullet = ModSpellItems.loopSpellBullet;
	public static final RegistryEntry<ItemCircleSpellBullet> circleSpellBullet = ModSpellItems.circleSpellBullet;
	public static final RegistryEntry<ItemGrenadeSpellBullet> grenadeSpellBullet = ModSpellItems.grenadeSpellBullet;
	public static final RegistryEntry<ItemChargeSpellBullet> chargeSpellBullet = ModSpellItems.chargeSpellBullet;
	public static final RegistryEntry<ItemMineSpellBullet> mineSpellBullet = ModSpellItems.mineSpellBullet;

	public static final RegistryEntry<ItemSpellDrive> spellDrive = ModSpellItems.spellDrive;
	public static final RegistryEntry<ItemDetonator> detonator = ModUtilityItems.detonator;
	public static final RegistryEntry<ItemExosuitController> exosuitController = ModUtilityItems.exosuitController;

	public static final RegistryEntry<ItemLightExosuitSensor> exosuitSensorLight = ModUtilityItems.exosuitSensorLight;
	public static final RegistryEntry<ItemHeatExosuitSensor> exosuitSensorHeat = ModUtilityItems.exosuitSensorHeat;
	public static final RegistryEntry<ItemStressExosuitSensor> exosuitSensorStress = ModUtilityItems.exosuitSensorStress;
	public static final RegistryEntry<ItemWaterExosuitSensor> exosuitSensorWater = ModUtilityItems.exosuitSensorWater;
	public static final RegistryEntry<ItemTriggerExosuitSensor> exosuitSensorTrigger = ModUtilityItems.exosuitSensorTrigger;
	public static final RegistryEntry<ItemCAD> cad = ModCADItem.CAD;

	public static final RegistryEntry<ItemVectorRuler> vectorRuler = ModUtilityItems.vectorRuler;
	public static final RegistryEntry<ItemPsimetalShovel> psimetalShovel = ModPsimetalItems.SHOVEL;
	public static final RegistryEntry<ItemPsimetalPickaxe> psimetalPickaxe = ModPsimetalItems.PICKAXE;
	public static final RegistryEntry<ItemPsimetalAxe> psimetalAxe = ModPsimetalItems.AXE;
	public static final RegistryEntry<ItemPsimetalSword> psimetalSword = ModPsimetalItems.SWORD;

	public static final RegistryEntry<ItemPsimetalExosuitHelmet> psimetalExosuitHelmet = ModPsimetalItems.HELMET;
	public static final RegistryEntry<ItemPsimetalExosuitChestplate> psimetalExosuitChestplate = ModPsimetalItems.CHESTPLATE;
	public static final RegistryEntry<ItemPsimetalExosuitLeggings> psimetalExosuitLeggings = ModPsimetalItems.LEGGINGS;
	public static final RegistryEntry<ItemPsimetalExosuitBoots> psimetalExosuitBoots = ModPsimetalItems.BOOTS;

	public static Item.Properties defaultBuilder() {
		return new Item.Properties();
	}
}

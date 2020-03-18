/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/01/2016, 21:48:12 (GMT)]
 */
package vazkii.psi.common.item.base;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import vazkii.arl.item.BasicItem;
import vazkii.psi.common.core.PsiCreativeTab;
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
import vazkii.psi.common.lib.LibItemNames;

public final class ModItems {

	public static final BasicItem psidust = new BasicItem("psidust", defaultBuilder());
	public static final BasicItem psimetal = new BasicItem("psimetal", defaultBuilder());
	public static final BasicItem psigem = new BasicItem("psigem", defaultBuilder());
	public static final BasicItem ebonyPsimetal = new BasicItem("ebony_psimetal", defaultBuilder());
	public static final BasicItem ivoryPsimetal = new BasicItem("ivory_psimetal", defaultBuilder());
	public static final BasicItem ebonySubstance = new BasicItem("ebony_substance", defaultBuilder());
	public static final BasicItem ivorySubstance = new BasicItem("ivory_substance", defaultBuilder());

	public static final BasicItem cadAssemblyIron = new ItemCADAssembly(LibItemNames.CAD_ASSEMBLY_IRON, defaultBuilder(), LibItemNames.CAD_IRON);
	public static final BasicItem cadAssemblyGold = new ItemCADAssembly(LibItemNames.CAD_ASSEMBLY_GOLD, defaultBuilder(), LibItemNames.CAD_GOLD);
	public static final BasicItem cadAssemblyPsimetal = new ItemCADAssembly(LibItemNames.CAD_ASSEMBLY_PSIMETAL, defaultBuilder(), LibItemNames.CAD_PSIMETAL);
	public static final BasicItem cadAssemblyIvory = new ItemCADAssembly(LibItemNames.CAD_ASSEMBLY_IVORY_PSIMETAL, defaultBuilder(), LibItemNames.CAD_IVORY_PSIMETAL);
	public static final BasicItem cadAssemblyEbony = new ItemCADAssembly(LibItemNames.CAD_ASSEMBLY_EBONY_PSIMETAL, defaultBuilder(), LibItemNames.CAD_EBONY_PSIMETAL);
	public static final BasicItem cadAssemblyCreative = new ItemCADAssembly(LibItemNames.CAD_ASSEMBLY_CREATIVE, defaultBuilder(), LibItemNames.CAD_CREATIVE);

	public static final BasicItem cadCoreBasic = new ItemCADCore(LibItemNames.CAD_CORE_BASIC, defaultBuilder());
	public static final BasicItem cadCoreOverclocked = new ItemCADCore(LibItemNames.CAD_CORE_OVERCLOCKED, defaultBuilder());
	public static final BasicItem cadCoreConductive = new ItemCADCore(LibItemNames.CAD_CORE_CONDUCTIVE, defaultBuilder());
	public static final BasicItem cadCoreHyperClocked = new ItemCADCore(LibItemNames.CAD_CORE_HYPERCLOCKED, defaultBuilder());
	public static final BasicItem cadCoreRadiative = new ItemCADCore(LibItemNames.CAD_CORE_RADIATIVE, defaultBuilder());

	public static final BasicItem cadSocketBasic = new ItemCADSocket(LibItemNames.CAD_SOCKET_BASIC, defaultBuilder());
	public static final BasicItem cadSocketSignaling = new ItemCADSocket(LibItemNames.CAD_SOCKET_SIGNALING, defaultBuilder());
	public static final BasicItem cadSocketLarge = new ItemCADSocket(LibItemNames.CAD_SOCKET_LARGE, defaultBuilder());
	public static final BasicItem cadSocketTransmissive = new ItemCADSocket(LibItemNames.CAD_SOCKET_TRANSMISSIVE, defaultBuilder());
	public static final BasicItem cadSocketHuge = new ItemCADSocket(LibItemNames.CAD_SOCKET_HUGE, defaultBuilder());

	public static final BasicItem cadBatteryBasic = new ItemCADBattery(LibItemNames.CAD_BATTERY_BASIC, defaultBuilder());
	public static final BasicItem cadBatteryExtended = new ItemCADBattery(LibItemNames.CAD_BATTERY_EXTENDED, defaultBuilder());
	public static final BasicItem cadBatteryUltradense = new ItemCADBattery(LibItemNames.CAD_BATTERY_ULTRADENSE, defaultBuilder());

	public static final BasicItem cadColorizerWhite = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_WHITE, defaultBuilder(), DyeColor.WHITE);
	public static final BasicItem cadColorizerOrange = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_ORANGE, defaultBuilder(), DyeColor.ORANGE);
	public static final BasicItem cadColorizerMagenta = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_MAGENTA, defaultBuilder(), DyeColor.MAGENTA);
	public static final BasicItem cadColorizerLightBlue = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_LIGHT_BLUE, defaultBuilder(), DyeColor.LIGHT_BLUE);
	public static final BasicItem cadColorizerYellow = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_YELLOW, defaultBuilder(), DyeColor.YELLOW);
	public static final BasicItem cadColorizerLime = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_LIME, defaultBuilder(), DyeColor.LIME);
	public static final BasicItem cadColorizerPink = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_PINK, defaultBuilder(), DyeColor.PINK);
	public static final BasicItem cadColorizerGray = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_GRAY, defaultBuilder(), DyeColor.GRAY);
	public static final BasicItem cadColorizerLightGray = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_LIGHT_GRAY, defaultBuilder(), DyeColor.LIGHT_GRAY);
	public static final BasicItem cadColorizerCyan = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_CYAN, defaultBuilder(), DyeColor.CYAN);
	public static final BasicItem cadColorizerPurple = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_PURPLE, defaultBuilder(), DyeColor.PURPLE);
	public static final BasicItem cadColorizerBlue = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_BLUE, defaultBuilder(), DyeColor.BLUE);
	public static final BasicItem cadColorizerBrown = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_BROWN, defaultBuilder(), DyeColor.BROWN);
	public static final BasicItem cadColorizerGreen = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_GREEN, defaultBuilder(), DyeColor.GREEN);
	public static final BasicItem cadColorizerRed = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_RED, defaultBuilder(), DyeColor.RED);
	public static final BasicItem cadColorizerBlack = new ItemCADColorizer(LibItemNames.CAD_COLORIZER_BLACK, defaultBuilder(), DyeColor.BLACK);
	public static final BasicItem cadColorizerRainbow = new ItemCADColorizerRainbow(LibItemNames.CAD_COLORIZER_RAINBOW, defaultBuilder());
	public static final BasicItem cadColorizerPsi = new ItemCADColorizerPsi(LibItemNames.CAD_COLORIZER_PSI, defaultBuilder());

	public static final BasicItem spellBullet = new ItemSpellBullet(LibItemNames.SPELL_BULLET, defaultBuilder());
	public static final BasicItem projectileSpellBullet = new ItemProjectileSpellBullet(LibItemNames.SPELL_BULLET_PROJECTILE, defaultBuilder());
	public static final BasicItem loopSpellBullet = new ItemLoopcastSpellBullet(LibItemNames.SPELL_BULLET_LOOP, defaultBuilder());
	public static final BasicItem circleSpellBullet = new ItemCircleSpellBullet(LibItemNames.SPELL_BULLET_CIRCLE, defaultBuilder());
	public static final BasicItem grenadeSpellBullet = new ItemGrenadeSpellBullet(LibItemNames.SPELL_BULLET_GRENADE, defaultBuilder());
	public static final BasicItem chargeSpellBullet = new ItemChargeSpellBullet(LibItemNames.SPELL_BULLET_CHARGE, defaultBuilder());
	public static final BasicItem mineSpellBullet = new ItemMineSpellBullet(LibItemNames.SPELL_BULLET_MINE, defaultBuilder());

	public static final BasicItem spellDrive = new ItemSpellDrive(LibItemNames.SPELL_DRIVE, defaultBuilder());
	public static final BasicItem detonator = new ItemDetonator(LibItemNames.DETONATOR, defaultBuilder());
	public static final BasicItem exosuitController = new ItemExosuitController(LibItemNames.EXOSUIT_CONTROLLER, defaultBuilder());

	public static final BasicItem exosuitSensorLight = new ItemLightExosuitSensor(LibItemNames.EXOSUIT_SENSOR_LIGHT, defaultBuilder());
	public static final BasicItem exosuitSensorHeat = new ItemHeatExosuitSensor(LibItemNames.EXOSUIT_SENSOR_HEAT, defaultBuilder());
	public static final BasicItem exosuitSensorStress = new ItemStressExosuitSensor(LibItemNames.EXOSUIT_SENSOR_STRESS, defaultBuilder());
	public static final BasicItem exosuitSensorWater = new ItemWaterExosuitSensor(LibItemNames.EXOSUIT_SENSOR_WATER, defaultBuilder());
	public static final BasicItem cad = new ItemCAD(LibItemNames.CAD, defaultBuilder());

	public static final BasicItem vectorRuler = new ItemVectorRuler(LibItemNames.VECTOR_RULER, defaultBuilder());
	public static final Item psimetalShovel = new ItemPsimetalShovel(LibItemNames.PSIMETAL_SHOVEL, defaultBuilder());
	public static final Item psimetalPickaxe = new ItemPsimetalPickaxe(LibItemNames.PSIMETAL_PICKAXE, defaultBuilder());
	public static final Item psimetalAxe = new ItemPsimetalAxe(LibItemNames.PSIMETAL_AXE, defaultBuilder());
	public static final Item psimetalSword = new ItemPsimetalSword(LibItemNames.PSIMETAL_SWORD, defaultBuilder());
	public static final Item psimetalExosuitHelmet = new ItemPsimetalExosuitHelmet(LibItemNames.PSIMETAL_EXOSUIT_HELMET, EquipmentSlotType.HEAD, defaultBuilder());
	public static final Item psimetalExosuitChestplate = new ItemPsimetalExosuitChestplate(LibItemNames.PSIMETAL_EXOSUIT_CHESTPLATE, EquipmentSlotType.CHEST, defaultBuilder());
	public static final Item psimetalExosuitLeggings = new ItemPsimetalExosuitLeggings(LibItemNames.PSIMETAL_EXOSUIT_LEGGINGS, EquipmentSlotType.LEGS, defaultBuilder());
	public static final Item psimetalExosuitBoots = new ItemPsimetalExosuitBoots(LibItemNames.PSIMETAL_EXOSUIT_BOOTS, EquipmentSlotType.FEET, defaultBuilder());


	public static Item.Properties defaultBuilder() {
		return new Item.Properties().group(PsiCreativeTab.INSTANCE);
	}



}

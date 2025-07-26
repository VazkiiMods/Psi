/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.base;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import vazkii.psi.common.Psi;
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
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.spell.base.ModSpellPieces;

@EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class ModItems {
	public static Item psidust;
	public static Item psimetal;
	public static Item psigem;
	public static Item ebonyPsimetal;
	public static Item ivoryPsimetal;
	public static Item ebonySubstance;
	public static Item ivorySubstance;

	public static Item cadAssemblyIron;
	public static Item cadAssemblyGold;
	public static Item cadAssemblyPsimetal;
	public static Item cadAssemblyIvory;
	public static Item cadAssemblyEbony;
	public static Item cadAssemblyCreative;

	public static Item cadCoreBasic;
	public static Item cadCoreOverclocked;
	public static Item cadCoreConductive;
	public static Item cadCoreHyperClocked;
	public static Item cadCoreRadiative;

	public static Item cadSocketBasic;
	public static Item cadSocketSignaling;
	public static Item cadSocketLarge;
	public static Item cadSocketTransmissive;
	public static Item cadSocketHuge;

	public static Item cadBatteryBasic;
	public static Item cadBatteryExtended;
	public static Item cadBatteryUltradense;

	public static Item cadColorizerWhite;
	public static Item cadColorizerOrange;
	public static Item cadColorizerMagenta;
	public static Item cadColorizerLightBlue;
	public static Item cadColorizerYellow;
	public static Item cadColorizerLime;
	public static Item cadColorizerPink;
	public static Item cadColorizerGray;
	public static Item cadColorizerLightGray;
	public static Item cadColorizerCyan;
	public static Item cadColorizerPurple;
	public static Item cadColorizerBlue;
	public static Item cadColorizerBrown;
	public static Item cadColorizerGreen;
	public static Item cadColorizerRed;
	public static Item cadColorizerBlack;
	public static Item cadColorizerRainbow;
	public static Item cadColorizerPsi;
	public static Item cadColorizerEmpty;

	public static Item flashRing;

	public static Item spellBullet;
	public static Item projectileSpellBullet;
	public static Item loopSpellBullet;
	public static Item circleSpellBullet;
	public static Item grenadeSpellBullet;
	public static Item chargeSpellBullet;
	public static Item mineSpellBullet;

	public static Item spellDrive;
	public static Item detonator;
	public static Item exosuitController;

	public static Item exosuitSensorLight;
	public static Item exosuitSensorHeat;
	public static Item exosuitSensorStress;
	public static Item exosuitSensorWater;
	public static Item exosuitSensorTrigger;
	public static Item cad;

	public static Item vectorRuler;
	public static Item psimetalShovel;
	public static Item psimetalPickaxe;
	public static Item psimetalAxe;
	public static Item psimetalSword;
	public static Item psimetalExosuitHelmet;
	public static Item psimetalExosuitChestplate;
	public static Item psimetalExosuitLeggings;
	public static Item psimetalExosuitBoots;

	@SubscribeEvent
	public static void register(RegisterEvent evt) {
		evt.register(Registries.ITEM, helper -> {
			psidust = new Item(defaultBuilder());
			psimetal = new Item(defaultBuilder());
			psigem = new Item(defaultBuilder());
			ebonyPsimetal = new Item(defaultBuilder());
			ivoryPsimetal = new Item(defaultBuilder());
			ebonySubstance = new Item(defaultBuilder());
			ivorySubstance = new Item(defaultBuilder());

			cadAssemblyIron = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_IRON);
			cadAssemblyGold = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_GOLD);
			cadAssemblyPsimetal = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_PSIMETAL);
			cadAssemblyIvory = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_IVORY_PSIMETAL);
			cadAssemblyEbony = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_EBONY_PSIMETAL);
			cadAssemblyCreative = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_CREATIVE);

			cadCoreBasic = new ItemCADCore(defaultBuilder());
			cadCoreOverclocked = new ItemCADCore(defaultBuilder());
			cadCoreConductive = new ItemCADCore(defaultBuilder());
			cadCoreHyperClocked = new ItemCADCore(defaultBuilder());
			cadCoreRadiative = new ItemCADCore(defaultBuilder());

			cadSocketBasic = new ItemCADSocket(defaultBuilder());
			cadSocketSignaling = new ItemCADSocket(defaultBuilder());
			cadSocketLarge = new ItemCADSocket(defaultBuilder());
			cadSocketTransmissive = new ItemCADSocket(defaultBuilder());
			cadSocketHuge = new ItemCADSocket(defaultBuilder());

			cadBatteryBasic = new ItemCADBattery(defaultBuilder());
			cadBatteryExtended = new ItemCADBattery(defaultBuilder());
			cadBatteryUltradense = new ItemCADBattery(defaultBuilder());

			cadColorizerWhite = new ItemCADColorizer(defaultBuilder(), DyeColor.WHITE);
			cadColorizerOrange = new ItemCADColorizer(defaultBuilder(), DyeColor.ORANGE);
			cadColorizerMagenta = new ItemCADColorizer(defaultBuilder(), DyeColor.MAGENTA);
			cadColorizerLightBlue = new ItemCADColorizer(defaultBuilder(), DyeColor.LIGHT_BLUE);
			cadColorizerYellow = new ItemCADColorizer(defaultBuilder(), DyeColor.YELLOW);
			cadColorizerLime = new ItemCADColorizer(defaultBuilder(), DyeColor.LIME);
			cadColorizerPink = new ItemCADColorizer(defaultBuilder(), DyeColor.PINK);
			cadColorizerGray = new ItemCADColorizer(defaultBuilder(), DyeColor.GRAY);
			cadColorizerLightGray = new ItemCADColorizer(defaultBuilder(), DyeColor.LIGHT_GRAY);
			cadColorizerCyan = new ItemCADColorizer(defaultBuilder(), DyeColor.CYAN);
			cadColorizerPurple = new ItemCADColorizer(defaultBuilder(), DyeColor.PURPLE);
			cadColorizerBlue = new ItemCADColorizer(defaultBuilder(), DyeColor.BLUE);
			cadColorizerBrown = new ItemCADColorizer(defaultBuilder(), DyeColor.BROWN);
			cadColorizerGreen = new ItemCADColorizer(defaultBuilder(), DyeColor.GREEN);
			cadColorizerRed = new ItemCADColorizer(defaultBuilder(), DyeColor.RED);
			cadColorizerBlack = new ItemCADColorizer(defaultBuilder(), DyeColor.BLACK);
			cadColorizerRainbow = new ItemCADColorizerRainbow(defaultBuilder());
			cadColorizerPsi = new ItemCADColorizerPsi(defaultBuilder());
			cadColorizerEmpty = new ItemCADColorizerEmpty(defaultBuilder());

			flashRing = new ItemFlashRing(defaultBuilder());

			spellBullet = new ItemSpellBullet(defaultBuilder());
			projectileSpellBullet = new ItemProjectileSpellBullet(defaultBuilder());
			loopSpellBullet = new ItemLoopcastSpellBullet(defaultBuilder());
			circleSpellBullet = new ItemCircleSpellBullet(defaultBuilder());
			grenadeSpellBullet = new ItemGrenadeSpellBullet(defaultBuilder());
			chargeSpellBullet = new ItemChargeSpellBullet(defaultBuilder());
			mineSpellBullet = new ItemMineSpellBullet(defaultBuilder());

			spellDrive = new ItemSpellDrive(defaultBuilder());
			detonator = new ItemDetonator(defaultBuilder());
			exosuitController = new ItemExosuitController(defaultBuilder());

			exosuitSensorLight = new ItemLightExosuitSensor(defaultBuilder());
			exosuitSensorHeat = new ItemHeatExosuitSensor(defaultBuilder());
			exosuitSensorStress = new ItemStressExosuitSensor(defaultBuilder());
			exosuitSensorWater = new ItemWaterExosuitSensor(defaultBuilder());
			exosuitSensorTrigger = new ItemTriggerExosuitSensor(defaultBuilder());
			cad = new ItemCAD(defaultBuilder());

			vectorRuler = new ItemVectorRuler(defaultBuilder());
			psimetalShovel = new ItemPsimetalShovel(defaultBuilder());
			psimetalPickaxe = new ItemPsimetalPickaxe(defaultBuilder());
			psimetalAxe = new ItemPsimetalAxe(defaultBuilder());
			psimetalSword = new ItemPsimetalSword(defaultBuilder());
			psimetalExosuitHelmet = new ItemPsimetalExosuitHelmet(ArmorItem.Type.HELMET, defaultBuilder().durability(ArmorItem.Type.HELMET.getDurability(18)));
			psimetalExosuitChestplate = new ItemPsimetalExosuitChestplate(ArmorItem.Type.CHESTPLATE, defaultBuilder().durability(ArmorItem.Type.CHESTPLATE.getDurability(18)));
			psimetalExosuitLeggings = new ItemPsimetalExosuitLeggings(ArmorItem.Type.LEGGINGS, defaultBuilder().durability(ArmorItem.Type.LEGGINGS.getDurability(18)));
			psimetalExosuitBoots = new ItemPsimetalExosuitBoots(ArmorItem.Type.BOOTS, defaultBuilder().durability(ArmorItem.Type.BOOTS.getDurability(18)));

			ModSpellPieces.init();

			helper.register(Psi.location(LibItemNames.PSIDUST), psidust);
			helper.register(Psi.location(LibItemNames.PSIMETAL), psimetal);
			helper.register(Psi.location(LibItemNames.PSIGEM), psigem);
			helper.register(Psi.location(LibItemNames.EBONY_PSIMETAL), ebonyPsimetal);
			helper.register(Psi.location(LibItemNames.IVORY_PSIMETAL), ivoryPsimetal);
			helper.register(Psi.location(LibItemNames.EBONY_SUBSTANCE), ebonySubstance);
			helper.register(Psi.location(LibItemNames.IVORY_SUBSTANCE), ivorySubstance);

			helper.register(Psi.location(LibItemNames.CAD_ASSEMBLY_CREATIVE), cadAssemblyCreative);
			helper.register(Psi.location(LibItemNames.CAD_ASSEMBLY_IRON), cadAssemblyIron);
			helper.register(Psi.location(LibItemNames.CAD_ASSEMBLY_GOLD), cadAssemblyGold);
			helper.register(Psi.location(LibItemNames.CAD_ASSEMBLY_PSIMETAL), cadAssemblyPsimetal);
			helper.register(Psi.location(LibItemNames.CAD_ASSEMBLY_IVORY_PSIMETAL), cadAssemblyIvory);
			helper.register(Psi.location(LibItemNames.CAD_ASSEMBLY_EBONY_PSIMETAL), cadAssemblyEbony);

			helper.register(Psi.location(LibItemNames.CAD_CORE_BASIC), cadCoreBasic);
			helper.register(Psi.location(LibItemNames.CAD_CORE_OVERCLOCKED), cadCoreOverclocked);
			helper.register(Psi.location(LibItemNames.CAD_CORE_CONDUCTIVE), cadCoreConductive);
			helper.register(Psi.location(LibItemNames.CAD_CORE_HYPERCLOCKED), cadCoreHyperClocked);
			helper.register(Psi.location(LibItemNames.CAD_CORE_RADIATIVE), cadCoreRadiative);

			helper.register(Psi.location(LibItemNames.CAD_SOCKET_BASIC), cadSocketBasic);
			helper.register(Psi.location(LibItemNames.CAD_SOCKET_SIGNALING), cadSocketSignaling);
			helper.register(Psi.location(LibItemNames.CAD_SOCKET_LARGE), cadSocketLarge);
			helper.register(Psi.location(LibItemNames.CAD_SOCKET_TRANSMISSIVE), cadSocketTransmissive);
			helper.register(Psi.location(LibItemNames.CAD_SOCKET_HUGE), cadSocketHuge);

			helper.register(Psi.location(LibItemNames.CAD_BATTERY_BASIC), cadBatteryBasic);
			helper.register(Psi.location(LibItemNames.CAD_BATTERY_EXTENDED), cadBatteryExtended);
			helper.register(Psi.location(LibItemNames.CAD_BATTERY_ULTRADENSE), cadBatteryUltradense);

			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_WHITE), cadColorizerWhite);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_ORANGE), cadColorizerOrange);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_MAGENTA), cadColorizerMagenta);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_LIGHT_BLUE), cadColorizerLightBlue);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_YELLOW), cadColorizerYellow);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_LIME), cadColorizerLime);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_PINK), cadColorizerPink);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_GRAY), cadColorizerGray);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_LIGHT_GRAY), cadColorizerLightGray);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_CYAN), cadColorizerCyan);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_PURPLE), cadColorizerPurple);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_BLUE), cadColorizerBlue);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_BROWN), cadColorizerBrown);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_GREEN), cadColorizerGreen);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_RED), cadColorizerRed);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_BLACK), cadColorizerBlack);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_RAINBOW), cadColorizerRainbow);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_PSI), cadColorizerPsi);
			helper.register(Psi.location(LibItemNames.CAD_COLORIZER_EMPTY), cadColorizerEmpty);

			helper.register(Psi.location(LibItemNames.FLASH_RING), flashRing);

			helper.register(Psi.location(LibItemNames.SPELL_BULLET), spellBullet);
			helper.register(Psi.location(LibItemNames.SPELL_BULLET_PROJECTILE), projectileSpellBullet);
			helper.register(Psi.location(LibItemNames.SPELL_BULLET_LOOP), loopSpellBullet);
			helper.register(Psi.location(LibItemNames.SPELL_BULLET_CIRCLE), circleSpellBullet);
			helper.register(Psi.location(LibItemNames.SPELL_BULLET_GRENADE), grenadeSpellBullet);
			helper.register(Psi.location(LibItemNames.SPELL_BULLET_CHARGE), chargeSpellBullet);
			helper.register(Psi.location(LibItemNames.SPELL_BULLET_MINE), mineSpellBullet);

			helper.register(Psi.location(LibItemNames.SPELL_DRIVE), spellDrive);
			helper.register(Psi.location(LibItemNames.DETONATOR), detonator);
			helper.register(Psi.location(LibItemNames.EXOSUIT_CONTROLLER), exosuitController);

			helper.register(Psi.location(LibItemNames.EXOSUIT_SENSOR_LIGHT), exosuitSensorLight);
			helper.register(Psi.location(LibItemNames.EXOSUIT_SENSOR_HEAT), exosuitSensorHeat);
			helper.register(Psi.location(LibItemNames.EXOSUIT_SENSOR_STRESS), exosuitSensorStress);
			helper.register(Psi.location(LibItemNames.EXOSUIT_SENSOR_WATER), exosuitSensorWater);
			helper.register(Psi.location(LibItemNames.EXOSUIT_SENSOR_TRIGGER), exosuitSensorTrigger);

			helper.register(Psi.location(LibItemNames.CAD), cad);

			helper.register(Psi.location(LibItemNames.VECTOR_RULER), vectorRuler);

			helper.register(Psi.location(LibItemNames.PSIMETAL_SHOVEL), psimetalShovel);
			helper.register(Psi.location(LibItemNames.PSIMETAL_PICKAXE), psimetalPickaxe);
			helper.register(Psi.location(LibItemNames.PSIMETAL_AXE), psimetalAxe);
			helper.register(Psi.location(LibItemNames.PSIMETAL_SWORD), psimetalSword);

			helper.register(Psi.location(LibItemNames.PSIMETAL_EXOSUIT_HELMET), psimetalExosuitHelmet);
			helper.register(Psi.location(LibItemNames.PSIMETAL_EXOSUIT_CHESTPLATE), psimetalExosuitChestplate);
			helper.register(Psi.location(LibItemNames.PSIMETAL_EXOSUIT_LEGGINGS), psimetalExosuitLeggings);
			helper.register(Psi.location(LibItemNames.PSIMETAL_EXOSUIT_BOOTS), psimetalExosuitBoots);
		});
	}

	public static Item.Properties defaultBuilder() {
		return new Item.Properties();
	}

}

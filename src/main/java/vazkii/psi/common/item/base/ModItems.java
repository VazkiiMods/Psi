/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.base;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.item.*;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitBoots;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitChestplate;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitHelmet;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitLeggings;
import vazkii.psi.common.item.component.ItemCADAssembly;
import vazkii.psi.common.item.component.ItemCADBattery;
import vazkii.psi.common.item.component.ItemCADColorizer;
import vazkii.psi.common.item.component.ItemCADColorizerEmpty;
import vazkii.psi.common.item.component.ItemCADColorizerPsi;
import vazkii.psi.common.item.component.ItemCADColorizerRainbow;
import vazkii.psi.common.item.component.ItemCADCore;
import vazkii.psi.common.item.component.ItemCADSocket;
import vazkii.psi.common.item.tool.ItemPsimetalAxe;
import vazkii.psi.common.item.tool.ItemPsimetalPickaxe;
import vazkii.psi.common.item.tool.ItemPsimetalShovel;
import vazkii.psi.common.item.tool.ItemPsimetalSword;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.spell.base.ModSpellPieces;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
	public static void registerItems(RegistryEvent.Register<Item> evt) {
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
		psimetalExosuitHelmet = new ItemPsimetalExosuitHelmet(EquipmentSlot.HEAD, defaultBuilder());
		psimetalExosuitChestplate = new ItemPsimetalExosuitChestplate(EquipmentSlot.CHEST, defaultBuilder());
		psimetalExosuitLeggings = new ItemPsimetalExosuitLeggings(EquipmentSlot.LEGS, defaultBuilder());
		psimetalExosuitBoots = new ItemPsimetalExosuitBoots(EquipmentSlot.FEET, defaultBuilder());

		ModSpellPieces.init();
		//CapabilityHandler.register();

		IForgeRegistry<Item> r = evt.getRegistry();

		r.register(psidust.setRegistryName(LibMisc.MOD_ID, LibItemNames.PSIDUST));
		r.register(psimetal.setRegistryName(LibMisc.MOD_ID, LibItemNames.PSIMETAL));
		r.register(psigem.setRegistryName(LibMisc.MOD_ID, LibItemNames.PSIGEM));
		r.register(ebonyPsimetal.setRegistryName(LibMisc.MOD_ID, LibItemNames.EBONY_PSIMETAL));
		r.register(ivoryPsimetal.setRegistryName(LibMisc.MOD_ID, LibItemNames.IVORY_PSIMETAL));
		r.register(ebonySubstance.setRegistryName(LibMisc.MOD_ID, LibItemNames.EBONY_SUBSTANCE));
		r.register(ivorySubstance.setRegistryName(LibMisc.MOD_ID, LibItemNames.IVORY_SUBSTANCE));
		r.register(cadAssemblyCreative.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_ASSEMBLY_CREATIVE));

		r.register(cadAssemblyIron.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_ASSEMBLY_IRON));
		r.register(cadAssemblyGold.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_ASSEMBLY_GOLD));
		r.register(cadAssemblyPsimetal.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_ASSEMBLY_PSIMETAL));
		r.register(cadAssemblyIvory.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_ASSEMBLY_IVORY_PSIMETAL));
		r.register(cadAssemblyEbony.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_ASSEMBLY_EBONY_PSIMETAL));

		r.register(cadCoreBasic.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_CORE_BASIC));
		r.register(cadCoreOverclocked.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_CORE_OVERCLOCKED));
		r.register(cadCoreConductive.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_CORE_CONDUCTIVE));
		r.register(cadCoreHyperClocked.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_CORE_HYPERCLOCKED));
		r.register(cadCoreRadiative.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_CORE_RADIATIVE));

		r.register(cadSocketBasic.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_SOCKET_BASIC));
		r.register(cadSocketSignaling.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_SOCKET_SIGNALING));
		r.register(cadSocketLarge.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_SOCKET_LARGE));
		r.register(cadSocketTransmissive.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_SOCKET_TRANSMISSIVE));
		r.register(cadSocketHuge.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_SOCKET_HUGE));

		r.register(cadBatteryBasic.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_BATTERY_BASIC));
		r.register(cadBatteryExtended.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_BATTERY_EXTENDED));
		r.register(cadBatteryUltradense.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_BATTERY_ULTRADENSE));

		r.register(cadColorizerWhite.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_WHITE));
		r.register(cadColorizerOrange.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_ORANGE));
		r.register(cadColorizerMagenta.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_MAGENTA));
		r.register(cadColorizerLightBlue.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_LIGHT_BLUE));
		r.register(cadColorizerYellow.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_YELLOW));
		r.register(cadColorizerLime.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_LIME));
		r.register(cadColorizerPink.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_PINK));
		r.register(cadColorizerGray.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_GRAY));
		r.register(cadColorizerLightGray.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_LIGHT_GRAY));
		r.register(cadColorizerCyan.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_CYAN));
		r.register(cadColorizerPurple.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_PURPLE));
		r.register(cadColorizerBlue.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_BLUE));
		r.register(cadColorizerBrown.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_BROWN));
		r.register(cadColorizerGreen.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_GREEN));
		r.register(cadColorizerRed.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_RED));
		r.register(cadColorizerBlack.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_BLACK));
		r.register(cadColorizerRainbow.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_RAINBOW));
		r.register(cadColorizerPsi.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_PSI));
		r.register(cadColorizerEmpty.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_EMPTY));

		r.register(spellBullet.setRegistryName(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET));
		r.register(projectileSpellBullet.setRegistryName(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET_PROJECTILE));
		r.register(loopSpellBullet.setRegistryName(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET_LOOP));
		r.register(circleSpellBullet.setRegistryName(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET_CIRCLE));
		r.register(grenadeSpellBullet.setRegistryName(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET_GRENADE));
		r.register(chargeSpellBullet.setRegistryName(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET_CHARGE));
		r.register(mineSpellBullet.setRegistryName(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET_MINE));

		r.register(spellDrive.setRegistryName(LibMisc.MOD_ID, LibItemNames.SPELL_DRIVE));
		r.register(detonator.setRegistryName(LibMisc.MOD_ID, LibItemNames.DETONATOR));
		r.register(exosuitController.setRegistryName(LibMisc.MOD_ID, LibItemNames.EXOSUIT_CONTROLLER));

		r.register(exosuitSensorLight.setRegistryName(LibMisc.MOD_ID, LibItemNames.EXOSUIT_SENSOR_LIGHT));
		r.register(exosuitSensorHeat.setRegistryName(LibMisc.MOD_ID, LibItemNames.EXOSUIT_SENSOR_HEAT));
		r.register(exosuitSensorStress.setRegistryName(LibMisc.MOD_ID, LibItemNames.EXOSUIT_SENSOR_STRESS));
		r.register(exosuitSensorWater.setRegistryName(LibMisc.MOD_ID, LibItemNames.EXOSUIT_SENSOR_WATER));
		r.register(exosuitSensorTrigger.setRegistryName(LibMisc.MOD_ID, LibItemNames.EXOSUIT_SENSOR_TRIGGER));

		r.register(cad.setRegistryName(LibMisc.MOD_ID, LibItemNames.CAD));

		r.register(vectorRuler.setRegistryName(LibMisc.MOD_ID, LibItemNames.VECTOR_RULER));

		r.register(psimetalShovel.setRegistryName(LibMisc.MOD_ID, LibItemNames.PSIMETAL_SHOVEL));
		r.register(psimetalPickaxe.setRegistryName(LibMisc.MOD_ID, LibItemNames.PSIMETAL_PICKAXE));
		r.register(psimetalAxe.setRegistryName(LibMisc.MOD_ID, LibItemNames.PSIMETAL_AXE));
		r.register(psimetalSword.setRegistryName(LibMisc.MOD_ID, LibItemNames.PSIMETAL_SWORD));

		r.register(psimetalExosuitHelmet.setRegistryName(LibMisc.MOD_ID, LibItemNames.PSIMETAL_EXOSUIT_HELMET));
		r.register(psimetalExosuitChestplate.setRegistryName(LibMisc.MOD_ID, LibItemNames.PSIMETAL_EXOSUIT_CHESTPLATE));
		r.register(psimetalExosuitLeggings.setRegistryName(LibMisc.MOD_ID, LibItemNames.PSIMETAL_EXOSUIT_LEGGINGS));
		r.register(psimetalExosuitBoots.setRegistryName(LibMisc.MOD_ID, LibItemNames.PSIMETAL_EXOSUIT_BOOTS));

	}

	public static Item.Properties defaultBuilder() {
		return new Item.Properties().tab(PsiCreativeTab.INSTANCE);
	}

}

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
import vazkii.psi.common.core.handler.capability.CapabilityHandler;
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

	public static final Item psidust = new Item(defaultBuilder());
	public static final Item psimetal = new Item(defaultBuilder());
	public static final Item psigem = new Item(defaultBuilder());
	public static final Item ebonyPsimetal = new Item(defaultBuilder());
	public static final Item ivoryPsimetal = new Item(defaultBuilder());
	public static final Item ebonySubstance = new Item(defaultBuilder());
	public static final Item ivorySubstance = new Item(defaultBuilder());

	public static final Item cadAssemblyIron = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_IRON);
	public static final Item cadAssemblyGold = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_GOLD);
	public static final Item cadAssemblyPsimetal = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_PSIMETAL);
	public static final Item cadAssemblyIvory = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_IVORY_PSIMETAL);
	public static final Item cadAssemblyEbony = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_EBONY_PSIMETAL);
	public static final Item cadAssemblyCreative = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_CREATIVE);

	public static final Item cadCoreBasic = new ItemCADCore(defaultBuilder());
	public static final Item cadCoreOverclocked = new ItemCADCore(defaultBuilder());
	public static final Item cadCoreConductive = new ItemCADCore(defaultBuilder());
	public static final Item cadCoreHyperClocked = new ItemCADCore(defaultBuilder());
	public static final Item cadCoreRadiative = new ItemCADCore(defaultBuilder());

	public static final Item cadSocketBasic = new ItemCADSocket(defaultBuilder());
	public static final Item cadSocketSignaling = new ItemCADSocket(defaultBuilder());
	public static final Item cadSocketLarge = new ItemCADSocket(defaultBuilder());
	public static final Item cadSocketTransmissive = new ItemCADSocket(defaultBuilder());
	public static final Item cadSocketHuge = new ItemCADSocket(defaultBuilder());

	public static final Item cadBatteryBasic = new ItemCADBattery(defaultBuilder());
	public static final Item cadBatteryExtended = new ItemCADBattery(defaultBuilder());
	public static final Item cadBatteryUltradense = new ItemCADBattery(defaultBuilder());

	public static final Item cadColorizerWhite = new ItemCADColorizer(defaultBuilder(), DyeColor.WHITE);
	public static final Item cadColorizerOrange = new ItemCADColorizer(defaultBuilder(), DyeColor.ORANGE);
	public static final Item cadColorizerMagenta = new ItemCADColorizer(defaultBuilder(), DyeColor.MAGENTA);
	public static final Item cadColorizerLightBlue = new ItemCADColorizer(defaultBuilder(), DyeColor.LIGHT_BLUE);
	public static final Item cadColorizerYellow = new ItemCADColorizer(defaultBuilder(), DyeColor.YELLOW);
	public static final Item cadColorizerLime = new ItemCADColorizer(defaultBuilder(), DyeColor.LIME);
	public static final Item cadColorizerPink = new ItemCADColorizer(defaultBuilder(), DyeColor.PINK);
	public static final Item cadColorizerGray = new ItemCADColorizer(defaultBuilder(), DyeColor.GRAY);
	public static final Item cadColorizerLightGray = new ItemCADColorizer(defaultBuilder(), DyeColor.LIGHT_GRAY);
	public static final Item cadColorizerCyan = new ItemCADColorizer(defaultBuilder(), DyeColor.CYAN);
	public static final Item cadColorizerPurple = new ItemCADColorizer(defaultBuilder(), DyeColor.PURPLE);
	public static final Item cadColorizerBlue = new ItemCADColorizer(defaultBuilder(), DyeColor.BLUE);
	public static final Item cadColorizerBrown = new ItemCADColorizer(defaultBuilder(), DyeColor.BROWN);
	public static final Item cadColorizerGreen = new ItemCADColorizer(defaultBuilder(), DyeColor.GREEN);
	public static final Item cadColorizerRed = new ItemCADColorizer(defaultBuilder(), DyeColor.RED);
	public static final Item cadColorizerBlack = new ItemCADColorizer(defaultBuilder(), DyeColor.BLACK);
	public static final Item cadColorizerRainbow = new ItemCADColorizerRainbow(defaultBuilder());
	public static final Item cadColorizerPsi = new ItemCADColorizerPsi(defaultBuilder());
	public static final Item cadColorizerEmpty = new ItemCADColorizerEmpty(defaultBuilder());

	public static final Item spellBullet = new ItemSpellBullet(defaultBuilder());
	public static final Item projectileSpellBullet = new ItemProjectileSpellBullet(defaultBuilder());
	public static final Item loopSpellBullet = new ItemLoopcastSpellBullet(defaultBuilder());
	public static final Item circleSpellBullet = new ItemCircleSpellBullet(defaultBuilder());
	public static final Item grenadeSpellBullet = new ItemGrenadeSpellBullet(defaultBuilder());
	public static final Item chargeSpellBullet = new ItemChargeSpellBullet(defaultBuilder());
	public static final Item mineSpellBullet = new ItemMineSpellBullet(defaultBuilder());

	public static final Item spellDrive = new ItemSpellDrive(defaultBuilder());
	public static final Item detonator = new ItemDetonator(defaultBuilder());
	public static final Item exosuitController = new ItemExosuitController(defaultBuilder());

	public static final Item exosuitSensorLight = new ItemLightExosuitSensor(defaultBuilder());
	public static final Item exosuitSensorHeat = new ItemHeatExosuitSensor(defaultBuilder());
	public static final Item exosuitSensorStress = new ItemStressExosuitSensor(defaultBuilder());
	public static final Item exosuitSensorWater = new ItemWaterExosuitSensor(defaultBuilder());
	public static final Item exosuitSensorTrigger = new ItemTriggerExosuitSensor(defaultBuilder());
	public static final Item cad = new ItemCAD(defaultBuilder());

	public static final Item vectorRuler = new ItemVectorRuler(defaultBuilder());
	public static final Item psimetalShovel = new ItemPsimetalShovel(defaultBuilder());
	public static final Item psimetalPickaxe = new ItemPsimetalPickaxe(defaultBuilder());
	public static final Item psimetalAxe = new ItemPsimetalAxe(defaultBuilder());
	public static final Item psimetalSword = new ItemPsimetalSword(defaultBuilder());
	public static final Item psimetalExosuitHelmet = new ItemPsimetalExosuitHelmet(EquipmentSlot.HEAD, defaultBuilder());
	public static final Item psimetalExosuitChestplate = new ItemPsimetalExosuitChestplate(EquipmentSlot.CHEST, defaultBuilder());
	public static final Item psimetalExosuitLeggings = new ItemPsimetalExosuitLeggings(EquipmentSlot.LEGS, defaultBuilder());
	public static final Item psimetalExosuitBoots = new ItemPsimetalExosuitBoots(EquipmentSlot.FEET, defaultBuilder());

	public static Item.Properties defaultBuilder() {
		return new Item.Properties().tab(PsiCreativeTab.INSTANCE);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> evt) {
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

}

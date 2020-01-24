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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.oredict.OreDictionary;
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
	public static final BasicItem cadAssemblyIvory = new ItemCADAssembly(LibItemNames.CAD_IVORY_PSIMETAL, defaultBuilder(), LibItemNames.CAD_IVORY_PSIMETAL);
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


	public static ItemMod cadColorizer;
	public static ItemMod spellBullet;
	public static ItemMod spellDrive;
	public static ItemMod detonator;
	public static ItemMod exosuitController;
	public static ItemMod exosuitSensor;
	public static ItemMod vectorRuler;

	public static ItemMod cad;

	public static ItemModShovel psimetalShovel;
	public static ItemModPickaxe psimetalPickaxe;
	public static ItemModAxe psimetalAxe;
	public static ItemModSword psimetalSword;
	public static ItemModArmor psimetalExosuitHelmet;
	public static ItemModArmor psimetalExosuitChestplate;
	public static ItemModArmor psimetalExosuitLeggings;
	public static ItemModArmor psimetalExosuitBoots;

	public static Item.Properties defaultBuilder() {
		return new Item.Properties().group(PsiCreativeTab.INSTANCE);
	}

	public static void preInit() {

		cadColorizer = new ItemCADColorizer();
		spellBullet = new ItemSpellBullet();
		spellDrive = new ItemSpellDrive();
		detonator = new ItemDetonator();
		exosuitController = new ItemExosuitController();
		exosuitSensor = new ItemExosuitSensor();
		vectorRuler = new ItemVectorRuler();
		
		cad = new ItemCAD();

		psimetalShovel = new ItemPsimetalShovel(LibItemNames.PSIMETAL_SHOVEL);
		psimetalPickaxe = new ItemPsimetalPickaxe(LibItemNames.PSIMETAL_PICKAXE);
		psimetalAxe = new ItemPsimetalAxe(LibItemNames.PSIMETAL_AXE);
		psimetalSword = new ItemPsimetalSword();
		psimetalExosuitHelmet = new ItemPsimetalExosuitHelmet();
		psimetalExosuitChestplate = new ItemPsimetalExosuitChestplate();
		psimetalExosuitLeggings = new ItemPsimetalExosuitLeggings();
		psimetalExosuitBoots = new ItemPsimetalExosuitBoots();
	}
	
	public static void init() {
		// Missing vanilla oredict mappings
		OreDictionary.registerOre("shardPrismarine", new ItemStack(Items.PRISMARINE_SHARD));
		OreDictionary.registerOre("crystalsPrismarine", new ItemStack(Items.PRISMARINE_CRYSTALS));
		OreDictionary.registerOre("coal", new ItemStack(Items.COAL));

		// Psi oredict mappings
		OreDictionary.registerOre("dustPsi", new ItemStack(material, 1, 0));
		OreDictionary.registerOre("ingotPsi", new ItemStack(material, 1, 1));
		OreDictionary.registerOre("gemPsi", new ItemStack(material, 1, 2));
		OreDictionary.registerOre("ingotEbonyPsi", new ItemStack(material, 1, 3));
		OreDictionary.registerOre("ingotIvoryPsi", new ItemStack(material, 1, 4));
		OreDictionary.registerOre("substanceEbony", new ItemStack(material, 1, 5));
		OreDictionary.registerOre("substanceIvory", new ItemStack(material, 1, 6));
	}

}

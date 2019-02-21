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

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.arl.item.*;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.ItemDetonator;
import vazkii.psi.common.item.ItemExosuitController;
import vazkii.psi.common.item.ItemExosuitSensor;
import vazkii.psi.common.item.ItemMaterial;
import vazkii.psi.common.item.ItemSpellBullet;
import vazkii.psi.common.item.ItemSpellDrive;
import vazkii.psi.common.item.ItemVectorRuler;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitBoots;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitChestplate;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitHelmet;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitLeggings;
import vazkii.psi.common.item.component.ItemCADAssembly;
import vazkii.psi.common.item.component.ItemCADBattery;
import vazkii.psi.common.item.component.ItemCADColorizer;
import vazkii.psi.common.item.component.ItemCADCore;
import vazkii.psi.common.item.component.ItemCADSocket;
import vazkii.psi.common.item.tool.ItemPsimetalAxe;
import vazkii.psi.common.item.tool.ItemPsimetalPickaxe;
import vazkii.psi.common.item.tool.ItemPsimetalShovel;
import vazkii.psi.common.item.tool.ItemPsimetalSword;
import vazkii.psi.common.lib.LibItemNames;

public final class ModItems {

	public static ItemMod material;

	public static ItemMod cadAssembly;
	public static ItemMod cadCore;
	public static ItemMod cadSocket;
	public static ItemMod cadBattery;
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

	public static void preInit() {
		material = new ItemMaterial();

		cadAssembly = new ItemCADAssembly();
		cadCore = new ItemCADCore();
		cadSocket = new ItemCADSocket();
		cadBattery = new ItemCADBattery();
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

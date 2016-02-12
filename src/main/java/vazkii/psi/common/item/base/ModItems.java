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
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.ItemMaterial;
import vazkii.psi.common.item.ItemSpellBullet;
import vazkii.psi.common.item.ItemSpellDrive;
import vazkii.psi.common.item.component.ItemCADAssembly;
import vazkii.psi.common.item.component.ItemCADBattery;
import vazkii.psi.common.item.component.ItemCADColorizer;
import vazkii.psi.common.item.component.ItemCADCore;
import vazkii.psi.common.item.component.ItemCADSocket;
import vazkii.psi.common.item.tool.ItemPsimetalAxe;
import vazkii.psi.common.item.tool.ItemPsimetalPickaxe;
import vazkii.psi.common.item.tool.ItemPsimetalShovel;
import vazkii.psi.common.item.tool.ItemPsimetalSword;

public final class ModItems {

	public static ItemMod material;

	public static ItemMod cadAssembly;
	public static ItemMod cadCore;
	public static ItemMod cadSocket;
	public static ItemMod cadBattery;
	public static ItemMod cadColorizer;
	public static ItemMod spellBullet;
	public static ItemMod spellDrive;

	public static ItemMod cad;

	public static ItemModTool psimetalShovel;
	public static ItemModTool psimetalPickaxe;
	public static ItemModTool psimetalAxe;
	public static ItemModSword psimetalSword;

	public static void init() {
		material = new ItemMaterial();

		cadAssembly = new ItemCADAssembly();
		cadCore = new ItemCADCore();
		cadSocket = new ItemCADSocket();
		cadBattery = new ItemCADBattery();
		cadColorizer = new ItemCADColorizer();
		spellBullet = new ItemSpellBullet();
		spellDrive = new ItemSpellDrive();

		cad = new ItemCAD();

		psimetalShovel = new ItemPsimetalShovel();
		psimetalPickaxe = new ItemPsimetalPickaxe();
		psimetalAxe = new ItemPsimetalAxe();
		psimetalSword = new ItemPsimetalSword();

		// Missing vanilla oredict mappings
		OreDictionary.registerOre("shardPrismarine", new ItemStack(Items.prismarine_shard));
		OreDictionary.registerOre("crystalsPrismarine", new ItemStack(Items.prismarine_crystals));
		OreDictionary.registerOre("coal", new ItemStack(Items.coal));

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

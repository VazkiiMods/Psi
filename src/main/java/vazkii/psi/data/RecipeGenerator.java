/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.data.CustomRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.crafting.recipe.*;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.lib.ModTags;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class RecipeGenerator extends RecipeProvider implements IConditionBuilder {

	public RecipeGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		specialRecipe(AssemblyScavengeRecipe.SERIALIZER, consumer);
		specialRecipe(BulletToDriveRecipe.SERIALIZER, consumer);
		specialRecipe(ColorizerChangeRecipe.SERIALIZER, consumer);
		specialRecipe(DriveDuplicateRecipe.SERIALIZER, consumer);
		specialRecipe(SensorAttachRecipe.SERIALIZER, consumer);
		specialRecipe(SensorRemoveRecipe.SERIALIZER, consumer);

		ICriterionInstance hasIron = hasItem(Tags.Items.INGOTS_IRON);
		ICriterionInstance hasPsimetal = hasItem(ModTags.INGOT_PSIMETAL);
		ICriterionInstance hasEbonyPsimetal = hasItem(ModTags.INGOT_EBONY_PSIMETAL);
		ICriterionInstance hasIvoryPsimetal = hasItem(ModTags.INGOT_IVORY_PSIMETAL);
		ICriterionInstance hasPsidust = hasItem(ModTags.PSIDUST);

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.cadAssembler)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('P', Items.PISTON)
				.patternLine("IPI")
				.patternLine("I I")
				.patternLine(" I ")
				.addCriterion("has_iron", hasIron)
				.build(consumer, Psi.location("assembler"));
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.programmer)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('D', ModTags.PSIDUST)
				.patternLine("IDI")
				.patternLine("I I")
				.patternLine(" I ")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("programmer"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.ebonyPsimetal)
				.key('S', ModTags.EBONY_SUBSTANCE)
				.key('I', ModTags.INGOT_PSIMETAL)
				.patternLine("SSS")
				.patternLine("SIS")
				.patternLine("SSS")
				.addCriterion("has_ebony_substance", hasItem(ModItems.ebonySubstance))
				.build(consumer, Psi.location("ebony_psimetal"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.ivoryPsimetal)
				.key('S', ModTags.IVORY_SUBSTANCE)
				.key('I', ModTags.INGOT_PSIMETAL)
				.patternLine("SSS")
				.patternLine("SIS")
				.patternLine("SSS")
				.addCriterion("has_ivory_substance", hasItem(ModItems.ivorySubstance))
				.build(consumer, Psi.location("ivory_psimetal"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyIron)
				.key('I', Tags.Items.INGOTS_IRON)
				.patternLine("III")
				.patternLine("I  ")
				.addCriterion("has_iron", hasIron)
				.build(consumer, Psi.location("cad_assembly_iron"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyGold)
				.key('I', Tags.Items.INGOTS_GOLD)
				.patternLine("III")
				.patternLine("I  ")
				.addCriterion("has_gold", hasItem(Tags.Items.INGOTS_GOLD))
				.build(consumer, Psi.location("cad_assembly_gold"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyPsimetal)
				.key('I', ModTags.INGOT_PSIMETAL)
				.patternLine("III")
				.patternLine("I  ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("cad_assembly_psimetal"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyEbony)
				.key('I', ModTags.INGOT_EBONY_PSIMETAL)
				.patternLine("III")
				.patternLine("I  ")
				.addCriterion("has_ebony_psimetal", hasEbonyPsimetal)
				.build(consumer, Psi.location("cad_assembly_ebony"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyIvory)
				.key('I', ModTags.INGOT_IVORY_PSIMETAL)
				.patternLine("III")
				.patternLine("I  ")
				.addCriterion("has_ivory_psimetal", hasIvoryPsimetal)
				.build(consumer, Psi.location("cad_assembly_ivory"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreBasic)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('D', ModTags.PSIDUST)
				.patternLine(" I ")
				.patternLine("IDI")
				.patternLine(" I ")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("cad_core_basic"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreOverclocked)
				.key('I', ModTags.INGOT_PSIMETAL)
				.key('D', Tags.Items.DUSTS_REDSTONE)
				.patternLine(" I ")
				.patternLine("IDI")
				.patternLine(" I ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("cad_core_overclocked"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreConductive)
				.key('I', ModTags.INGOT_PSIMETAL)
				.key('D', Tags.Items.DUSTS_GLOWSTONE)
				.patternLine(" I ")
				.patternLine("IDI")
				.patternLine(" I ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("cad_core_conductive"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreHyperClocked)
				.key('I', ModTags.INGOT_PSIMETAL)
				.key('D', Tags.Items.DUSTS_REDSTONE)
				.key('G', ModTags.GEM_PSIGEM)
				.patternLine(" G ")
				.patternLine("IDI")
				.patternLine(" G ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("cad_core_hyperclocked"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreRadiative)
				.key('I', ModTags.INGOT_PSIMETAL)
				.key('D', Tags.Items.DUSTS_GLOWSTONE)
				.key('G', ModTags.GEM_PSIGEM)
				.patternLine(" G ")
				.patternLine("IDI")
				.patternLine(" G ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("cad_core_radiative"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketBasic)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('D', ModTags.PSIDUST)
				.patternLine("DI ")
				.patternLine("I  ")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("cad_socket_basic"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketSignaling)
				.key('I', ModTags.INGOT_PSIMETAL)
				.key('D', Tags.Items.DUSTS_REDSTONE)
				.patternLine("DI ")
				.patternLine("I  ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("cad_socket_signaling"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketLarge)
				.key('I', ModTags.INGOT_PSIMETAL)
				.key('D', Tags.Items.DUSTS_GLOWSTONE)
				.patternLine("DI ")
				.patternLine("I  ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("cad_socket_large"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketTransmissive)
				.key('I', ModTags.INGOT_PSIMETAL)
				.key('D', Tags.Items.DUSTS_REDSTONE)
				.key('G', ModTags.GEM_PSIGEM)
				.patternLine("DI ")
				.patternLine("IG ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("cad_socket_transmissive"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketHuge)
				.key('I', ModTags.INGOT_PSIMETAL)
				.key('D', Tags.Items.DUSTS_GLOWSTONE)
				.key('G', ModTags.GEM_PSIGEM)
				.patternLine("DI ")
				.patternLine("IG ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("cad_socket_huge"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadBatteryBasic)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('D', ModTags.PSIDUST)
				.key('G', Tags.Items.INGOTS_GOLD)
				.patternLine("I")
				.patternLine("D")
				.patternLine("G")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("cad_battery_basic"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadBatteryExtended)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('D', ModTags.INGOT_PSIMETAL)
				.key('G', Tags.Items.INGOTS_GOLD)
				.patternLine("I")
				.patternLine("D")
				.patternLine("G")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("cad_battery_extended"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadBatteryUltradense)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('D', ModTags.GEM_PSIGEM)
				.key('G', Tags.Items.INGOTS_GOLD)
				.patternLine("I")
				.patternLine("D")
				.patternLine("G")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("cad_battery_ultradense"));

		for (DyeColor color : DyeColor.values()) {
			ShapedRecipeBuilder.shapedRecipe(Registry.ITEM.getOrDefault(Psi.location(LibItemNames.CAD_COLORIZER + color.getString())))
					.setGroup("psi:colorizer")
					.key('D', ModTags.PSIDUST)
					.key('I', Tags.Items.INGOTS_IRON)
					.key('G', Tags.Items.GLASS)
					.key('C', color.getTag())
					.patternLine(" D ")
					.patternLine("GCG")
					.patternLine(" I ")
					.addCriterion("has_psidust", hasPsidust)
					.build(consumer, Psi.location(LibItemNames.CAD_COLORIZER + color.getString()));
		}

		ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerRainbow)
				.setGroup("psi:colorizer")
				.key('D', ModTags.PSIDUST)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('G', Tags.Items.GLASS)
				.key('C', Tags.Items.GEMS_PRISMARINE)
				.patternLine(" D ")
				.patternLine("GCG")
				.patternLine(" I ")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("cad_colorizer_rainbow"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerPsi)
				.setGroup("psi:colorizer")
				.key('D', ModTags.PSIDUST)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('G', Tags.Items.GLASS)
				.key('C', ModTags.PSIDUST)
				.patternLine(" D ")
				.patternLine("GCG")
				.patternLine(" I ")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("cad_colorizer_psi"));

		ShapedRecipeBuilder.shapedRecipe(ModItems.spellBullet)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('D', ModTags.PSIDUST)
				.patternLine("ID")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("spell_bullet_basic"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.projectileSpellBullet)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('D', ModTags.PSIDUST)
				.key('A', ItemTags.ARROWS)
				.patternLine("AID")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("spell_bullet_projectile"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.projectileSpellBullet)
				.addIngredient(ModItems.spellBullet)
				.addIngredient(ItemTags.ARROWS)
				.addCriterion("has_psidust", hasItem(ModItems.psidust))
				.build(WrapperResult.ofType(BulletUpgradeRecipe.SERIALIZER, consumer), Psi.location("spell_bullet_projectile_upgrade"));

		ShapedRecipeBuilder.shapedRecipe(ModItems.loopSpellBullet)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('D', ModTags.PSIDUST)
				.key('A', Tags.Items.STRING)
				.patternLine("AID")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("spell_bullet_loopcast"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.loopSpellBullet)
				.addIngredient(ModItems.spellBullet)
				.addIngredient(Tags.Items.STRING)
				.addCriterion("has_psidust", hasItem(ModItems.psidust))
				.build(WrapperResult.ofType(BulletUpgradeRecipe.SERIALIZER, consumer), Psi.location("spell_bullet_loopcast_upgrade"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.circleSpellBullet)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('D', ModTags.PSIDUST)
				.key('A', Ingredient.fromItemListStream(Stream.of(
						new Ingredient.TagList(Tags.Items.SLIMEBALLS),
						new Ingredient.SingleItemList(new ItemStack(Items.SNOWBALL)))))
				.patternLine("AID")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("spell_bullet_circle"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.circleSpellBullet)
				.addIngredient(ModItems.spellBullet)
				.addIngredient(Ingredient.fromItemListStream(Stream.of(
						new Ingredient.TagList(Tags.Items.SLIMEBALLS),
						new Ingredient.SingleItemList(new ItemStack(Items.SNOWBALL)))))
				.addCriterion("has_psidust", hasItem(ModItems.psidust))
				.build(WrapperResult.ofType(BulletUpgradeRecipe.SERIALIZER, consumer), Psi.location("spell_bullet_circle_upgrade"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.grenadeSpellBullet)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('D', ModTags.PSIDUST)
				.key('A', Tags.Items.GUNPOWDER)
				.patternLine("AID")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("spell_bullet_grenade"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.grenadeSpellBullet)
				.addIngredient(ModItems.spellBullet)
				.addIngredient(Tags.Items.GUNPOWDER)
				.addCriterion("has_psidust", hasItem(ModItems.psidust))
				.build(WrapperResult.ofType(BulletUpgradeRecipe.SERIALIZER, consumer), Psi.location("spell_bullet_grenade_upgrade"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.chargeSpellBullet)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('D', ModTags.PSIDUST)
				.key('A', Tags.Items.DUSTS_REDSTONE)
				.patternLine("AID")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("spell_bullet_charge"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.chargeSpellBullet)
				.addIngredient(ModItems.spellBullet)
				.addIngredient(Tags.Items.DUSTS_REDSTONE)
				.addCriterion("has_psidust", hasItem(ModItems.psidust))
				.build(WrapperResult.ofType(BulletUpgradeRecipe.SERIALIZER, consumer), Psi.location("spell_bullet_charge_upgrade"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.mineSpellBullet)
				.key('I', Tags.Items.INGOTS_IRON)
				.key('D', ModTags.PSIDUST)
				.key('A', ItemTags.BUTTONS)
				.patternLine("AID")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("spell_bullet_mine"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.mineSpellBullet)
				.addIngredient(ModItems.spellBullet)
				.addIngredient(ItemTags.BUTTONS)
				.addCriterion("has_psidust", hasItem(ModItems.psidust))
				.build(WrapperResult.ofType(BulletUpgradeRecipe.SERIALIZER, consumer), Psi.location("spell_bullet_mine_upgrade"));

		ShapedRecipeBuilder.shapedRecipe(ModItems.spellDrive)
				.key('I', ModTags.INGOT_PSIMETAL)
				.key('R', Tags.Items.DUSTS_REDSTONE)
				.patternLine("I")
				.patternLine("R")
				.patternLine("I")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("spell_drive"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalShovel)
				.key('P', ModTags.INGOT_PSIMETAL)
				.key('G', ModTags.GEM_PSIGEM)
				.key('I', Tags.Items.INGOTS_IRON)
				.patternLine("GP")
				.patternLine(" I")
				.patternLine(" I")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_shovel"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalPickaxe)
				.key('P', ModTags.INGOT_PSIMETAL)
				.key('G', ModTags.GEM_PSIGEM)
				.key('I', Tags.Items.INGOTS_IRON)
				.patternLine("PGP")
				.patternLine(" I ")
				.patternLine(" I ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_pickaxe"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalAxe)
				.key('P', ModTags.INGOT_PSIMETAL)
				.key('G', ModTags.GEM_PSIGEM)
				.key('I', Tags.Items.INGOTS_IRON)
				.patternLine("GP")
				.patternLine("PI")
				.patternLine(" I")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_axe"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalSword)
				.key('P', ModTags.INGOT_PSIMETAL)
				.key('G', ModTags.GEM_PSIGEM)
				.key('I', Tags.Items.INGOTS_IRON)
				.patternLine("P")
				.patternLine("G")
				.patternLine("I")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_sword"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalExosuitHelmet)
				.key('P', ModTags.INGOT_PSIMETAL)
				.key('G', ModTags.GEM_PSIGEM)
				.patternLine("GPG")
				.patternLine("P P")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_exosuit_helmet"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalExosuitChestplate)
				.key('P', ModTags.INGOT_PSIMETAL)
				.key('G', ModTags.GEM_PSIGEM)
				.patternLine("P P")
				.patternLine("GPG")
				.patternLine("PPP")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_exosuit_chestplate"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalExosuitLeggings)
				.key('P', ModTags.INGOT_PSIMETAL)
				.key('G', ModTags.GEM_PSIGEM)
				.patternLine("GPG")
				.patternLine("P P")
				.patternLine("P P")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_exosuit_leggings"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalExosuitBoots)
				.key('P', ModTags.INGOT_PSIMETAL)
				.key('G', ModTags.GEM_PSIGEM)
				.patternLine("G G")
				.patternLine("P P")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_exosuit_boots"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.detonator)
				.key('P', ModTags.PSIDUST)
				.key('B', ItemTags.BUTTONS)
				.key('I', Tags.Items.INGOTS_IRON)
				.patternLine(" B ")
				.patternLine("IPI")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("detonator"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitController)
				.key('R', Tags.Items.DUSTS_REDSTONE)
				.key('G', Tags.Items.GLASS)
				.key('I', ModTags.INGOT_PSIMETAL)
				.patternLine("R")
				.patternLine("G")
				.patternLine("I")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("exosuit_controller"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.vectorRuler)
				.key('D', ModTags.PSIDUST)
				.key('I', Tags.Items.INGOTS_IRON)
				.patternLine("D")
				.patternLine("I")
				.patternLine("I")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("vector_ruler"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitSensorLight)
				.key('M', Tags.Items.DUSTS_GLOWSTONE)
				.key('R', Tags.Items.INGOTS_IRON)
				.key('I', ModTags.INGOT_PSIMETAL)
				.patternLine(" I ")
				.patternLine("IMR")
				.patternLine(" R ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("exosuit_sensor_light"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitSensorWater)
				.key('M', Tags.Items.GEMS_PRISMARINE)
				.key('R', Tags.Items.INGOTS_IRON)
				.key('I', ModTags.INGOT_PSIMETAL)
				.patternLine(" I ")
				.patternLine("IMR")
				.patternLine(" R ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("exosuit_sensor_water"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitSensorHeat)
				.key('M', Items.FIRE_CHARGE)
				.key('R', Tags.Items.INGOTS_IRON)
				.key('I', ModTags.INGOT_PSIMETAL)
				.patternLine(" I ")
				.patternLine("IMR")
				.patternLine(" R ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("exosuit_sensor_heat"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitSensorStress)
				.key('M', Items.GLISTERING_MELON_SLICE)
				.key('R', Tags.Items.INGOTS_IRON)
				.key('I', ModTags.INGOT_PSIMETAL)
				.patternLine(" I ")
				.patternLine("IMR")
				.patternLine(" R ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("exosuit_sensor_stress"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitSensorTrigger)
				.key('M', Items.GUNPOWDER)
				.key('R', Tags.Items.INGOTS_IRON)
				.key('I', ModTags.INGOT_PSIMETAL)
				.patternLine(" I ")
				.patternLine("IMR")
				.patternLine(" R ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("exosuit_sensor_trigger"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerEmpty)
				.key('D', ModTags.PSIDUST)
				.key('G', Tags.Items.GLASS)
				.key('I', Tags.Items.INGOTS_IRON)
				.patternLine(" D ")
				.patternLine("G G")
				.patternLine(" I ")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("cad_colorizer_empty"));
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.psidustBlock.asItem())
				.key('I', ModItems.psidust)
				.patternLine("III")
				.patternLine("III")
				.patternLine("III")
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("psidust_block"));
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.psimetalBlock.asItem())
				.key('I', ModItems.psimetal)
				.patternLine("III")
				.patternLine("III")
				.patternLine("III")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_block"));
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.psigemBlock.asItem())
				.key('I', ModItems.psigem)
				.patternLine("III")
				.patternLine("III")
				.patternLine("III")
				.addCriterion("has_psigem", hasItem(ModItems.psigem))
				.build(consumer, Psi.location("psigem_block"));
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.psimetalEbony.asItem())
				.key('I', ModItems.ebonyPsimetal)
				.patternLine("III")
				.patternLine("III")
				.patternLine("III")
				.addCriterion("has_ebony_psimetal", hasEbonyPsimetal)
				.build(consumer, Psi.location("ebony_block"));
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.psimetalIvory.asItem())
				.key('I', ModItems.ivoryPsimetal)
				.patternLine("III")
				.patternLine("III")
				.patternLine("III")
				.addCriterion("has_ivory_psimetal", hasIvoryPsimetal)
				.build(consumer, Psi.location("ivory_block"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.psidust, 9)
				.addIngredient(ModBlocks.psidustBlock.asItem())
				.addCriterion("has_psidust", hasPsidust)
				.build(consumer, Psi.location("psidust_shapeless"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.psimetal, 9)
				.addIngredient(ModBlocks.psimetalBlock.asItem())
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_shapeless"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.psigem, 9)
				.addIngredient(ModBlocks.psigemBlock.asItem())
				.addCriterion("has_psigem", hasItem(ModItems.psigem))
				.build(consumer, Psi.location("psigem_shapeless"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.ebonyPsimetal, 9)
				.addIngredient(ModBlocks.psimetalEbony.asItem())
				.addCriterion("has_ebony_psimetal", hasEbonyPsimetal)
				.build(consumer, Psi.location("ebony_ingot_shapeless"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.ivoryPsimetal, 9)
				.addIngredient(ModBlocks.psimetalIvory.asItem())
				.addCriterion("has_ivory_psimetal", hasIvoryPsimetal)
				.build(consumer, Psi.location("ivory_ingot_shapeless"));
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.psimetalPlateBlack.asItem())
				.key('C', ItemTags.COALS)
				.key('I', ModTags.INGOT_PSIMETAL)
				.patternLine(" C ")
				.patternLine("CIC")
				.patternLine(" C ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_plate_black"));
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.psimetalPlateWhite.asItem())
				.key('C', Tags.Items.GEMS_QUARTZ)
				.key('I', ModTags.INGOT_PSIMETAL)
				.patternLine(" C ")
				.patternLine("CIC")
				.patternLine(" C ")
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_plate_white"));
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psimetalPlateBlackLight.asItem())
				.addIngredient(Tags.Items.DUSTS_GLOWSTONE)
				.addIngredient(ModBlocks.psimetalPlateBlack.asItem())
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_plate_black_light"));
		ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psimetalPlateWhiteLight.asItem())
				.addIngredient(Tags.Items.DUSTS_GLOWSTONE)
				.addIngredient(ModBlocks.psimetalPlateWhite.asItem())
				.addCriterion("has_psimetal", hasPsimetal)
				.build(consumer, Psi.location("psimetal_plate_white_light"));
	}

	@Override
	public String getName() {
		return "Psi crafting recipes";
	}

	private static void specialRecipe(SpecialRecipeSerializer<?> serializer, Consumer<IFinishedRecipe> consumer) {
		CustomRecipeBuilder.customRecipe(serializer).build(consumer, Psi.location("dynamic/" + serializer.getRegistryName().getPath()).toString());
	}

}

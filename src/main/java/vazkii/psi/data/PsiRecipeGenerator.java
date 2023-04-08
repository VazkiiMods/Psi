/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;

import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.crafting.recipe.*;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.lib.ModTags;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class PsiRecipeGenerator extends RecipeProvider implements IConditionBuilder {

	public PsiRecipeGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		specialRecipe(AssemblyScavengeRecipe.SERIALIZER, consumer);
		specialRecipe(BulletToDriveRecipe.SERIALIZER, consumer);
		specialRecipe(ColorizerChangeRecipe.SERIALIZER, consumer);
		specialRecipe(DriveDuplicateRecipe.SERIALIZER, consumer);
		specialRecipe(SensorAttachRecipe.SERIALIZER, consumer);
		specialRecipe(SensorRemoveRecipe.SERIALIZER, consumer);

		CriterionTriggerInstance hasIron = has(Tags.Items.INGOTS_IRON);
		CriterionTriggerInstance hasPsimetal = has(ModTags.INGOT_PSIMETAL);
		CriterionTriggerInstance hasEbonyPsimetal = has(ModTags.INGOT_EBONY_PSIMETAL);
		CriterionTriggerInstance hasIvoryPsimetal = has(ModTags.INGOT_IVORY_PSIMETAL);
		CriterionTriggerInstance hasPsidust = has(ModTags.PSIDUST);

		ShapedRecipeBuilder.shaped(ModBlocks.cadAssembler)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('P', Items.PISTON)
				.pattern("IPI")
				.pattern("I I")
				.pattern(" I ")
				.unlockedBy("has_iron", hasIron)
				.save(consumer, Psi.location("assembler"));
		ShapedRecipeBuilder.shaped(ModBlocks.programmer)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.pattern("IDI")
				.pattern("I I")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("programmer"));
		ShapedRecipeBuilder.shaped(ModItems.ebonyPsimetal)
				.define('S', ModTags.EBONY_SUBSTANCE)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern("SSS")
				.pattern("SIS")
				.pattern("SSS")
				.unlockedBy("has_ebony_substance", has(ModItems.ebonySubstance))
				.save(consumer, Psi.location("ebony_psimetal"));
		ShapedRecipeBuilder.shaped(ModItems.ivoryPsimetal)
				.define('S', ModTags.IVORY_SUBSTANCE)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern("SSS")
				.pattern("SIS")
				.pattern("SSS")
				.unlockedBy("has_ivory_substance", has(ModItems.ivorySubstance))
				.save(consumer, Psi.location("ivory_psimetal"));
		ShapedRecipeBuilder.shaped(ModItems.cadAssemblyIron)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_iron", hasIron)
				.save(consumer, Psi.location("cad_assembly_iron"));
		ShapedRecipeBuilder.shaped(ModItems.cadAssemblyGold)
				.define('I', Tags.Items.INGOTS_GOLD)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
				.save(consumer, Psi.location("cad_assembly_gold"));
		ShapedRecipeBuilder.shaped(ModItems.cadAssemblyPsimetal)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_assembly_psimetal"));
		ShapedRecipeBuilder.shaped(ModItems.cadAssemblyEbony)
				.define('I', ModTags.INGOT_EBONY_PSIMETAL)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_ebony_psimetal", hasEbonyPsimetal)
				.save(consumer, Psi.location("cad_assembly_ebony"));
		ShapedRecipeBuilder.shaped(ModItems.cadAssemblyIvory)
				.define('I', ModTags.INGOT_IVORY_PSIMETAL)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_ivory_psimetal", hasIvoryPsimetal)
				.save(consumer, Psi.location("cad_assembly_ivory"));
		ShapedRecipeBuilder.shaped(ModItems.cadCoreBasic)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.pattern(" I ")
				.pattern("IDI")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("cad_core_basic"));
		ShapedRecipeBuilder.shaped(ModItems.cadCoreOverclocked)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_REDSTONE)
				.pattern(" I ")
				.pattern("IDI")
				.pattern(" I ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_core_overclocked"));
		ShapedRecipeBuilder.shaped(ModItems.cadCoreConductive)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_GLOWSTONE)
				.pattern(" I ")
				.pattern("IDI")
				.pattern(" I ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_core_conductive"));
		ShapedRecipeBuilder.shaped(ModItems.cadCoreHyperClocked)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_REDSTONE)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern(" G ")
				.pattern("IDI")
				.pattern(" G ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_core_hyperclocked"));
		ShapedRecipeBuilder.shaped(ModItems.cadCoreRadiative)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_GLOWSTONE)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern(" G ")
				.pattern("IDI")
				.pattern(" G ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_core_radiative"));
		ShapedRecipeBuilder.shaped(ModItems.cadSocketBasic)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.pattern("DI ")
				.pattern("I  ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("cad_socket_basic"));
		ShapedRecipeBuilder.shaped(ModItems.cadSocketSignaling)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_REDSTONE)
				.pattern("DI ")
				.pattern("I  ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_socket_signaling"));
		ShapedRecipeBuilder.shaped(ModItems.cadSocketLarge)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_GLOWSTONE)
				.pattern("DI ")
				.pattern("I  ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_socket_large"));
		ShapedRecipeBuilder.shaped(ModItems.cadSocketTransmissive)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_REDSTONE)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("DI ")
				.pattern("IG ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_socket_transmissive"));
		ShapedRecipeBuilder.shaped(ModItems.cadSocketHuge)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_GLOWSTONE)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("DI ")
				.pattern("IG ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_socket_huge"));
		ShapedRecipeBuilder.shaped(ModItems.cadBatteryBasic)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('G', Tags.Items.INGOTS_GOLD)
				.pattern("I")
				.pattern("D")
				.pattern("G")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("cad_battery_basic"));
		ShapedRecipeBuilder.shaped(ModItems.cadBatteryExtended)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.INGOT_PSIMETAL)
				.define('G', Tags.Items.INGOTS_GOLD)
				.pattern("I")
				.pattern("D")
				.pattern("G")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_battery_extended"));
		ShapedRecipeBuilder.shaped(ModItems.cadBatteryUltradense)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.GEM_PSIGEM)
				.define('G', Tags.Items.INGOTS_GOLD)
				.pattern("I")
				.pattern("D")
				.pattern("G")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_battery_ultradense"));

		for (DyeColor color : DyeColor.values()) {
			ShapedRecipeBuilder.shaped(Registry.ITEM.get(Psi.location(LibItemNames.CAD_COLORIZER + color.getSerializedName())))
					.group("psi:colorizer")
					.define('D', ModTags.PSIDUST)
					.define('I', Tags.Items.INGOTS_IRON)
					.define('G', Tags.Items.GLASS)
					.define('C', color.getTag())
					.pattern(" D ")
					.pattern("GCG")
					.pattern(" I ")
					.unlockedBy("has_psidust", hasPsidust)
					.save(consumer, Psi.location(LibItemNames.CAD_COLORIZER + color.getSerializedName()));
		}

		ShapedRecipeBuilder.shaped(ModItems.cadColorizerRainbow)
				.group("psi:colorizer")
				.define('D', ModTags.PSIDUST)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('G', Tags.Items.GLASS)
				.define('C', Tags.Items.GEMS_PRISMARINE)
				.pattern(" D ")
				.pattern("GCG")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("cad_colorizer_rainbow"));
		ShapedRecipeBuilder.shaped(ModItems.cadColorizerPsi)
				.group("psi:colorizer")
				.define('D', ModTags.PSIDUST)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('G', Tags.Items.GLASS)
				.define('C', ModTags.PSIDUST)
				.pattern(" D ")
				.pattern("GCG")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("cad_colorizer_psi"));

		ShapedRecipeBuilder.shaped(ModItems.spellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.pattern("ID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_basic"));
		ShapedRecipeBuilder.shaped(ModItems.projectileSpellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', ItemTags.ARROWS)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_projectile"));
		ShapelessRecipeBuilder.shapeless(ModItems.projectileSpellBullet)
				.requires(ModItems.spellBullet)
				.requires(ItemTags.ARROWS)
				.unlockedBy("has_psidust", has(ModItems.psidust))
				.save(WrapperResult.ofType(BulletUpgradeRecipe.SERIALIZER, consumer), Psi.location("spell_bullet_projectile_upgrade"));

		ShapedRecipeBuilder.shaped(ModItems.loopSpellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', Tags.Items.STRING)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_loopcast"));
		ShapelessRecipeBuilder.shapeless(ModItems.loopSpellBullet)
				.requires(ModItems.spellBullet)
				.requires(Tags.Items.STRING)
				.unlockedBy("has_psidust", has(ModItems.psidust))
				.save(WrapperResult.ofType(BulletUpgradeRecipe.SERIALIZER, consumer), Psi.location("spell_bullet_loopcast_upgrade"));
		ShapedRecipeBuilder.shaped(ModItems.circleSpellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', Ingredient.fromValues(Stream.of(
						new Ingredient.TagValue(Tags.Items.SLIMEBALLS),
						new Ingredient.ItemValue(new ItemStack(Items.SNOWBALL)))))
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_circle"));
		ShapelessRecipeBuilder.shapeless(ModItems.circleSpellBullet)
				.requires(ModItems.spellBullet)
				.requires(Ingredient.fromValues(Stream.of(
						new Ingredient.TagValue(Tags.Items.SLIMEBALLS),
						new Ingredient.ItemValue(new ItemStack(Items.SNOWBALL)))))
				.unlockedBy("has_psidust", has(ModItems.psidust))
				.save(WrapperResult.ofType(BulletUpgradeRecipe.SERIALIZER, consumer), Psi.location("spell_bullet_circle_upgrade"));
		ShapedRecipeBuilder.shaped(ModItems.grenadeSpellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', Tags.Items.GUNPOWDER)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_grenade"));
		ShapelessRecipeBuilder.shapeless(ModItems.grenadeSpellBullet)
				.requires(ModItems.spellBullet)
				.requires(Tags.Items.GUNPOWDER)
				.unlockedBy("has_psidust", has(ModItems.psidust))
				.save(WrapperResult.ofType(BulletUpgradeRecipe.SERIALIZER, consumer), Psi.location("spell_bullet_grenade_upgrade"));
		ShapedRecipeBuilder.shaped(ModItems.chargeSpellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', Tags.Items.DUSTS_REDSTONE)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_charge"));
		ShapelessRecipeBuilder.shapeless(ModItems.chargeSpellBullet)
				.requires(ModItems.spellBullet)
				.requires(Tags.Items.DUSTS_REDSTONE)
				.unlockedBy("has_psidust", has(ModItems.psidust))
				.save(WrapperResult.ofType(BulletUpgradeRecipe.SERIALIZER, consumer), Psi.location("spell_bullet_charge_upgrade"));
		ShapedRecipeBuilder.shaped(ModItems.mineSpellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', ItemTags.BUTTONS)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_mine"));
		ShapelessRecipeBuilder.shapeless(ModItems.mineSpellBullet)
				.requires(ModItems.spellBullet)
				.requires(ItemTags.BUTTONS)
				.unlockedBy("has_psidust", has(ModItems.psidust))
				.save(WrapperResult.ofType(BulletUpgradeRecipe.SERIALIZER, consumer), Psi.location("spell_bullet_mine_upgrade"));

		ShapedRecipeBuilder.shaped(ModItems.spellDrive)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.pattern("I")
				.pattern("R")
				.pattern("I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("spell_drive"));
		ShapedRecipeBuilder.shaped(ModItems.psimetalShovel)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern("GP")
				.pattern(" I")
				.pattern(" I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_shovel"));
		ShapedRecipeBuilder.shaped(ModItems.psimetalPickaxe)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern("PGP")
				.pattern(" I ")
				.pattern(" I ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_pickaxe"));
		ShapedRecipeBuilder.shaped(ModItems.psimetalAxe)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern("GP")
				.pattern("PI")
				.pattern(" I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_axe"));
		ShapedRecipeBuilder.shaped(ModItems.psimetalSword)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern("P")
				.pattern("G")
				.pattern("I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_sword"));
		ShapedRecipeBuilder.shaped(ModItems.psimetalExosuitHelmet)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("GPG")
				.pattern("P P")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_exosuit_helmet"));
		ShapedRecipeBuilder.shaped(ModItems.psimetalExosuitChestplate)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("P P")
				.pattern("GPG")
				.pattern("PPP")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_exosuit_chestplate"));
		ShapedRecipeBuilder.shaped(ModItems.psimetalExosuitLeggings)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("GPG")
				.pattern("P P")
				.pattern("P P")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_exosuit_leggings"));
		ShapedRecipeBuilder.shaped(ModItems.psimetalExosuitBoots)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("G G")
				.pattern("P P")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_exosuit_boots"));
		ShapedRecipeBuilder.shaped(ModItems.detonator)
				.define('P', ModTags.PSIDUST)
				.define('B', ItemTags.BUTTONS)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern(" B ")
				.pattern("IPI")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("detonator"));
		ShapedRecipeBuilder.shaped(ModItems.exosuitController)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('G', Tags.Items.GLASS)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern("R")
				.pattern("G")
				.pattern("I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("exosuit_controller"));
		ShapedRecipeBuilder.shaped(ModItems.vectorRuler)
				.define('D', ModTags.PSIDUST)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern("D")
				.pattern("I")
				.pattern("I")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("vector_ruler"));
		ShapedRecipeBuilder.shaped(ModItems.exosuitSensorLight)
				.define('M', Tags.Items.DUSTS_GLOWSTONE)
				.define('R', Tags.Items.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("exosuit_sensor_light"));
		ShapedRecipeBuilder.shaped(ModItems.exosuitSensorWater)
				.define('M', Tags.Items.GEMS_PRISMARINE)
				.define('R', Tags.Items.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("exosuit_sensor_water"));
		ShapedRecipeBuilder.shaped(ModItems.exosuitSensorHeat)
				.define('M', Items.FIRE_CHARGE)
				.define('R', Tags.Items.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("exosuit_sensor_heat"));
		ShapedRecipeBuilder.shaped(ModItems.exosuitSensorStress)
				.define('M', Items.GLISTERING_MELON_SLICE)
				.define('R', Tags.Items.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("exosuit_sensor_stress"));
		ShapedRecipeBuilder.shaped(ModItems.exosuitSensorTrigger)
				.define('M', Items.GUNPOWDER)
				.define('R', Tags.Items.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("exosuit_sensor_trigger"));
		ShapedRecipeBuilder.shaped(ModItems.cadColorizerEmpty)
				.define('D', ModTags.PSIDUST)
				.define('G', Tags.Items.GLASS)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern(" D ")
				.pattern("G G")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("cad_colorizer_empty"));
		ShapedRecipeBuilder.shaped(ModBlocks.psidustBlock.asItem())
				.define('I', ModItems.psidust)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("psidust_block"));
		ShapedRecipeBuilder.shaped(ModBlocks.psimetalBlock.asItem())
				.define('I', ModItems.psimetal)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_block"));
		ShapedRecipeBuilder.shaped(ModBlocks.psigemBlock.asItem())
				.define('I', ModItems.psigem)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_psigem", has(ModItems.psigem))
				.save(consumer, Psi.location("psigem_block"));
		ShapedRecipeBuilder.shaped(ModBlocks.psimetalEbony.asItem())
				.define('I', ModItems.ebonyPsimetal)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_ebony_psimetal", hasEbonyPsimetal)
				.save(consumer, Psi.location("ebony_block"));
		ShapedRecipeBuilder.shaped(ModBlocks.psimetalIvory.asItem())
				.define('I', ModItems.ivoryPsimetal)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_ivory_psimetal", hasIvoryPsimetal)
				.save(consumer, Psi.location("ivory_block"));
		ShapelessRecipeBuilder.shapeless(ModItems.psidust, 9)
				.requires(ModBlocks.psidustBlock.asItem())
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("psidust_shapeless"));
		ShapelessRecipeBuilder.shapeless(ModItems.psimetal, 9)
				.requires(ModBlocks.psimetalBlock.asItem())
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_shapeless"));
		ShapelessRecipeBuilder.shapeless(ModItems.psigem, 9)
				.requires(ModBlocks.psigemBlock.asItem())
				.unlockedBy("has_psigem", has(ModItems.psigem))
				.save(consumer, Psi.location("psigem_shapeless"));
		ShapelessRecipeBuilder.shapeless(ModItems.ebonyPsimetal, 9)
				.requires(ModBlocks.psimetalEbony.asItem())
				.unlockedBy("has_ebony_psimetal", hasEbonyPsimetal)
				.save(consumer, Psi.location("ebony_ingot_shapeless"));
		ShapelessRecipeBuilder.shapeless(ModItems.ivoryPsimetal, 9)
				.requires(ModBlocks.psimetalIvory.asItem())
				.unlockedBy("has_ivory_psimetal", hasIvoryPsimetal)
				.save(consumer, Psi.location("ivory_ingot_shapeless"));
		ShapedRecipeBuilder.shaped(ModBlocks.psimetalPlateBlack.asItem())
				.define('C', ItemTags.COALS)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" C ")
				.pattern("CIC")
				.pattern(" C ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_plate_black"));
		ShapedRecipeBuilder.shaped(ModBlocks.psimetalPlateWhite.asItem())
				.define('C', Tags.Items.GEMS_QUARTZ)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" C ")
				.pattern("CIC")
				.pattern(" C ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_plate_white"));
		ShapelessRecipeBuilder.shapeless(ModBlocks.psimetalPlateBlackLight.asItem())
				.requires(Tags.Items.DUSTS_GLOWSTONE)
				.requires(ModBlocks.psimetalPlateBlack.asItem())
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_plate_black_light"));
		ShapelessRecipeBuilder.shapeless(ModBlocks.psimetalPlateWhiteLight.asItem())
				.requires(Tags.Items.DUSTS_GLOWSTONE)
				.requires(ModBlocks.psimetalPlateWhite.asItem())
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_plate_white_light"));
	}

	@Override
	public String getName() {
		return "Psi crafting recipes";
	}

	private static void specialRecipe(SimpleRecipeSerializer<?> serializer, Consumer<FinishedRecipe> consumer) {
		SpecialRecipeBuilder.special(serializer).save(consumer, Psi.location("dynamic/" + ForgeRegistries.RECIPE_SERIALIZERS.getKey(serializer).getPath()).toString());
	}

}

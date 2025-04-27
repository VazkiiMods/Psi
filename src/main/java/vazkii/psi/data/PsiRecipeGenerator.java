/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

import vazkii.patchouli.api.PatchouliAPI;
import vazkii.psi.api.recipe.TrickRecipeBuilder;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.crafting.recipe.*;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.lib.LibPieceNames;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.lib.ModTags;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

public class PsiRecipeGenerator extends RecipeProvider {

	public PsiRecipeGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
		super(pOutput, pRegistries);
	}

	protected void specialRecipe(RecipeOutput recipeOutput, Function<CraftingBookCategory, Recipe<?>> factory, CraftingBookCategory category) {
		Recipe<?> recipe = factory.apply(category);
		ResourceLocation serializerKey = BuiltInRegistries.RECIPE_SERIALIZER.getKey(recipe.getSerializer());
		recipeOutput.accept(ResourceLocation.fromNamespaceAndPath(serializerKey.getNamespace(), "dynamic/" + serializerKey.getPath()), recipe, null);
	}

	@Override
	protected void buildRecipes(RecipeOutput consumer) {
		specialRecipe(consumer, AssemblyScavengeRecipe::new, CraftingBookCategory.MISC);
		specialRecipe(consumer, BulletToDriveRecipe::new, CraftingBookCategory.MISC);
		specialRecipe(consumer, ColorizerChangeRecipe::new, CraftingBookCategory.MISC);
		specialRecipe(consumer, DriveDuplicateRecipe::new, CraftingBookCategory.MISC);
		specialRecipe(consumer, SensorAttachRecipe::new, CraftingBookCategory.MISC);
		specialRecipe(consumer, SensorRemoveRecipe::new, CraftingBookCategory.MISC);

		Criterion hasIron = has(Tags.Items.INGOTS_IRON);
		Criterion hasPsimetal = has(ModTags.INGOT_PSIMETAL);
		Criterion hasEbonyPsimetal = has(ModTags.INGOT_EBONY_PSIMETAL);
		Criterion hasIvoryPsimetal = has(ModTags.INGOT_IVORY_PSIMETAL);
		Criterion hasPsidust = has(ModTags.PSIDUST);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.cadAssembler)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('P', Items.PISTON)
				.pattern("IPI")
				.pattern("I I")
				.pattern(" I ")
				.unlockedBy("has_iron", hasIron)
				.save(consumer, Psi.location("assembler"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.programmer)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.pattern("IDI")
				.pattern("I I")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("programmer"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ebonyPsimetal)
				.define('S', ModTags.EBONY_SUBSTANCE)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern("SSS")
				.pattern("SIS")
				.pattern("SSS")
				.unlockedBy("has_ebony_substance", has(ModItems.ebonySubstance))
				.save(consumer, Psi.location("ebony_psimetal"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ivoryPsimetal)
				.define('S', ModTags.IVORY_SUBSTANCE)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern("SSS")
				.pattern("SIS")
				.pattern("SSS")
				.unlockedBy("has_ivory_substance", has(ModItems.ivorySubstance))
				.save(consumer, Psi.location("ivory_psimetal"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadAssemblyIron)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_iron", hasIron)
				.save(consumer, Psi.location("cad_assembly_iron"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadAssemblyGold)
				.define('I', Tags.Items.INGOTS_GOLD)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
				.save(consumer, Psi.location("cad_assembly_gold"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadAssemblyPsimetal)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_assembly_psimetal"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadAssemblyEbony)
				.define('I', ModTags.INGOT_EBONY_PSIMETAL)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_ebony_psimetal", hasEbonyPsimetal)
				.save(consumer, Psi.location("cad_assembly_ebony"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadAssemblyIvory)
				.define('I', ModTags.INGOT_IVORY_PSIMETAL)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_ivory_psimetal", hasIvoryPsimetal)
				.save(consumer, Psi.location("cad_assembly_ivory"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadCoreBasic)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.pattern(" I ")
				.pattern("IDI")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("cad_core_basic"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadCoreOverclocked)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_REDSTONE)
				.pattern(" I ")
				.pattern("IDI")
				.pattern(" I ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_core_overclocked"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadCoreConductive)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_GLOWSTONE)
				.pattern(" I ")
				.pattern("IDI")
				.pattern(" I ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_core_conductive"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadCoreHyperClocked)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_REDSTONE)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern(" G ")
				.pattern("IDI")
				.pattern(" G ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_core_hyperclocked"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadCoreRadiative)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_GLOWSTONE)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern(" G ")
				.pattern("IDI")
				.pattern(" G ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_core_radiative"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadSocketBasic)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.pattern("DI ")
				.pattern("I  ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("cad_socket_basic"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadSocketSignaling)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_REDSTONE)
				.pattern("DI ")
				.pattern("I  ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_socket_signaling"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadSocketLarge)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_GLOWSTONE)
				.pattern("DI ")
				.pattern("I  ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_socket_large"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadSocketTransmissive)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_REDSTONE)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("DI ")
				.pattern("IG ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_socket_transmissive"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadSocketHuge)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', Tags.Items.DUSTS_GLOWSTONE)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("DI ")
				.pattern("IG ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_socket_huge"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadBatteryBasic)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('G', Tags.Items.INGOTS_GOLD)
				.pattern("I")
				.pattern("D")
				.pattern("G")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("cad_battery_basic"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadBatteryExtended)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.INGOT_PSIMETAL)
				.define('G', Tags.Items.INGOTS_GOLD)
				.pattern("I")
				.pattern("D")
				.pattern("G")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_battery_extended"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadBatteryUltradense)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.GEM_PSIGEM)
				.define('G', Tags.Items.INGOTS_GOLD)
				.pattern("I")
				.pattern("D")
				.pattern("G")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("cad_battery_ultradense"));

		for(DyeColor color : DyeColor.values()) {
			ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BuiltInRegistries.ITEM.get(Psi.location(LibItemNames.CAD_COLORIZER + color.getSerializedName())))
					.group("psi:colorizer")
					.define('D', ModTags.PSIDUST)
					.define('I', Tags.Items.INGOTS_IRON)
					.define('G', Tags.Items.GLASS_BLOCKS)
					.define('C', color.getTag())
					.pattern(" D ")
					.pattern("GCG")
					.pattern(" I ")
					.unlockedBy("has_psidust", hasPsidust)
					.save(consumer, Psi.location(LibItemNames.CAD_COLORIZER + color.getSerializedName()));
		}

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadColorizerRainbow)
				.group("psi:colorizer")
				.define('D', ModTags.PSIDUST)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('G', Tags.Items.GLASS_BLOCKS)
				.define('C', Tags.Items.GEMS_PRISMARINE)
				.pattern(" D ")
				.pattern("GCG")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("cad_colorizer_rainbow"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadColorizerPsi)
				.group("psi:colorizer")
				.define('D', ModTags.PSIDUST)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('G', Tags.Items.GLASS_BLOCKS)
				.define('C', ModTags.PSIDUST)
				.pattern(" D ")
				.pattern("GCG")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("cad_colorizer_psi"));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.spellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.pattern("ID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_basic"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.projectileSpellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', ItemTags.ARROWS)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_projectile"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.projectileSpellBullet)
				.requires(ModItems.spellBullet)
				.requires(ItemTags.ARROWS)
				.unlockedBy("has_psidust", has(ModItems.psidust))
				.save(consumer, Psi.location("spell_bullet_projectile_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.loopSpellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', Tags.Items.STRINGS)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_loopcast"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.loopSpellBullet)
				.requires(ModItems.spellBullet)
				.requires(Tags.Items.STRINGS)
				.unlockedBy("has_psidust", has(ModItems.psidust))
				.save(consumer, Psi.location("spell_bullet_loopcast_upgrade"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.circleSpellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', Ingredient.fromValues(Stream.of(
						new Ingredient.TagValue(Tags.Items.SLIMEBALLS),
						new Ingredient.ItemValue(new ItemStack(Items.SNOWBALL)))))
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_circle"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.circleSpellBullet)
				.requires(ModItems.spellBullet)
				.requires(Ingredient.fromValues(Stream.of(
						new Ingredient.TagValue(Tags.Items.SLIMEBALLS),
						new Ingredient.ItemValue(new ItemStack(Items.SNOWBALL)))))
				.unlockedBy("has_psidust", has(ModItems.psidust))
				.save(consumer, Psi.location("spell_bullet_circle_upgrade"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.grenadeSpellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', Tags.Items.GUNPOWDERS)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_grenade"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.grenadeSpellBullet)
				.requires(ModItems.spellBullet)
				.requires(Tags.Items.GUNPOWDERS)
				.unlockedBy("has_psidust", has(ModItems.psidust))
				.save(consumer, Psi.location("spell_bullet_grenade_upgrade"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.chargeSpellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', Tags.Items.DUSTS_REDSTONE)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_charge"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.chargeSpellBullet)
				.requires(ModItems.spellBullet)
				.requires(Tags.Items.DUSTS_REDSTONE)
				.unlockedBy("has_psidust", has(ModItems.psidust))
				.save(consumer, Psi.location("spell_bullet_charge_upgrade"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.mineSpellBullet)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', ItemTags.BUTTONS)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("spell_bullet_mine"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.mineSpellBullet)
				.requires(ModItems.spellBullet)
				.requires(ItemTags.BUTTONS)
				.unlockedBy("has_psidust", has(ModItems.psidust))
				.save(consumer, Psi.location("spell_bullet_mine_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.spellDrive)
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.pattern("I")
				.pattern("R")
				.pattern("I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("spell_drive"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalShovel)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern("GP")
				.pattern(" I")
				.pattern(" I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_shovel"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalPickaxe)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern("PGP")
				.pattern(" I ")
				.pattern(" I ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_pickaxe"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalAxe)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern("GP")
				.pattern("PI")
				.pattern(" I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_axe"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalSword)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern("P")
				.pattern("G")
				.pattern("I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_sword"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalExosuitHelmet)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("GPG")
				.pattern("P P")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_exosuit_helmet"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalExosuitChestplate)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("P P")
				.pattern("GPG")
				.pattern("PPP")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_exosuit_chestplate"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalExosuitLeggings)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("GPG")
				.pattern("P P")
				.pattern("P P")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_exosuit_leggings"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalExosuitBoots)
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("G G")
				.pattern("P P")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_exosuit_boots"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.detonator)
				.define('P', ModTags.PSIDUST)
				.define('B', ItemTags.BUTTONS)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern(" B ")
				.pattern("IPI")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("detonator"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.exosuitController)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('G', Tags.Items.GLASS_BLOCKS)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern("R")
				.pattern("G")
				.pattern("I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("exosuit_controller"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.vectorRuler)
				.define('D', ModTags.PSIDUST)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern("D")
				.pattern("I")
				.pattern("I")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("vector_ruler"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.exosuitSensorLight)
				.define('M', Tags.Items.DUSTS_GLOWSTONE)
				.define('R', Tags.Items.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("exosuit_sensor_light"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.exosuitSensorWater)
				.define('M', Tags.Items.GEMS_PRISMARINE)
				.define('R', Tags.Items.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("exosuit_sensor_water"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.exosuitSensorHeat)
				.define('M', Items.FIRE_CHARGE)
				.define('R', Tags.Items.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("exosuit_sensor_heat"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.exosuitSensorStress)
				.define('M', Items.GLISTERING_MELON_SLICE)
				.define('R', Tags.Items.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("exosuit_sensor_stress"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.exosuitSensorTrigger)
				.define('M', Items.GUNPOWDER)
				.define('R', Tags.Items.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("exosuit_sensor_trigger"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadColorizerEmpty)
				.define('D', ModTags.PSIDUST)
				.define('G', Tags.Items.GLASS_BLOCKS)
				.define('I', Tags.Items.INGOTS_IRON)
				.pattern(" D ")
				.pattern("G G")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("cad_colorizer_empty"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.psidustBlock.asItem())
				.define('I', ModItems.psidust)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("psidust_block"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.psimetalBlock.asItem())
				.define('I', ModItems.psimetal)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_block"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.psigemBlock.asItem())
				.define('I', ModItems.psigem)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_psigem", has(ModItems.psigem))
				.save(consumer, Psi.location("psigem_block"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.psimetalEbony.asItem())
				.define('I', ModItems.ebonyPsimetal)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_ebony_psimetal", hasEbonyPsimetal)
				.save(consumer, Psi.location("ebony_block"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.psimetalIvory.asItem())
				.define('I', ModItems.ivoryPsimetal)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_ivory_psimetal", hasIvoryPsimetal)
				.save(consumer, Psi.location("ivory_block"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.psidust, 9)
				.requires(ModBlocks.psidustBlock.asItem())
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, Psi.location("psidust_shapeless"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.psimetal, 9)
				.requires(ModBlocks.psimetalBlock.asItem())
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_shapeless"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.psigem, 9)
				.requires(ModBlocks.psigemBlock.asItem())
				.unlockedBy("has_psigem", has(ModItems.psigem))
				.save(consumer, Psi.location("psigem_shapeless"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.ebonyPsimetal, 9)
				.requires(ModBlocks.psimetalEbony.asItem())
				.unlockedBy("has_ebony_psimetal", hasEbonyPsimetal)
				.save(consumer, Psi.location("ebony_ingot_shapeless"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.ivoryPsimetal, 9)
				.requires(ModBlocks.psimetalIvory.asItem())
				.unlockedBy("has_ivory_psimetal", hasIvoryPsimetal)
				.save(consumer, Psi.location("ivory_ingot_shapeless"));
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.psimetalPlateBlack.asItem())
				.define('C', ItemTags.COALS)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" C ")
				.pattern("CIC")
				.pattern(" C ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_plate_black"));
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.psimetalPlateWhite.asItem())
				.define('C', Tags.Items.GEMS_QUARTZ)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" C ")
				.pattern("CIC")
				.pattern(" C ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_plate_white"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.psimetalPlateBlackLight.asItem())
				.requires(Tags.Items.DUSTS_GLOWSTONE)
				.requires(ModBlocks.psimetalPlateBlack.asItem())
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_plate_black_light"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.psimetalPlateWhiteLight.asItem())
				.requires(Tags.Items.DUSTS_GLOWSTONE)
				.requires(ModBlocks.psimetalPlateWhite.asItem())
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, Psi.location("psimetal_plate_white_light"));
		this.buildTrickRecipes(consumer);
	}

	protected void buildTrickRecipes(RecipeOutput consumer) {
		TrickRecipeBuilder.of(ModItems.psidust).input(Tags.Items.DUSTS_REDSTONE).cad(ModItems.cadAssemblyIron).build(consumer);
		TrickRecipeBuilder.of(PatchouliAPI.get().getBookStack(LibResources.PATCHOULI_BOOK)).input(Items.BOOK).cad(ModItems.cadAssemblyIron).build(consumer);

		TrickRecipeBuilder.of(ModItems.cadAssemblyPsimetal)
				.input(ModItems.cadAssemblyGold)
				.trick(Psi.location(LibPieceNames.TRICK_INFUSION))
				.cad(ModItems.cadAssemblyIron)
				.unlockedBy(getHasName(ModItems.cadAssemblyGold), has(ModItems.cadAssemblyGold))
				.build(consumer, Psi.location("gold_to_psimetal_assembly_upgrade"));

		TrickRecipeBuilder.of(ModItems.psimetal)
				.input(Tags.Items.INGOTS_GOLD)
				.trick(Psi.location(LibPieceNames.TRICK_INFUSION))
				.cad(ModItems.cadAssemblyIron)
				.unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
				.build(consumer);

		TrickRecipeBuilder.of(ModItems.psigem)
				.input(Tags.Items.GEMS_DIAMOND)
				.trick(Psi.location(LibPieceNames.TRICK_GREATER_INFUSION))
				.cad(ModItems.cadAssemblyPsimetal)
				.unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
				.build(consumer);

		TrickRecipeBuilder.of(ModItems.ebonySubstance)
				.input(ItemTags.COALS)
				.trick(Psi.location(LibPieceNames.TRICK_EBONY_IVORY))
				.cad(ModItems.cadAssemblyPsimetal)
				.dimension(Level.END)
				.unlockedBy("has_coal", has(ItemTags.COALS))
				.build(consumer);

		TrickRecipeBuilder.of(ModItems.ivorySubstance)
				.input(Tags.Items.GEMS_QUARTZ)
				.trick(Psi.location(LibPieceNames.TRICK_EBONY_IVORY))
				.cad(ModItems.cadAssemblyPsimetal)
				.dimension(Level.END)
				.unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
				.build(consumer);
	}

}

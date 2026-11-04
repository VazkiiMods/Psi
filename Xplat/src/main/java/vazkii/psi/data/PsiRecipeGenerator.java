/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import vazkii.patchouli.api.PatchouliAPI;
import vazkii.psi.api.recipe.TrickRecipeBuilder;
import vazkii.psi.common.PsiMod;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.crafting.recipe.*;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.lib.LibPieceNames;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.lib.ModTags;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class PsiRecipeGenerator extends RecipeProvider {

	public PsiRecipeGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
		super(pOutput, pRegistries);
	}

	protected void specialRecipe(RecipeOutput recipeOutput, RegistryEntry<? extends RecipeType<?>> type, Function<CraftingBookCategory, Recipe<?>> factory) {
		Recipe<?> recipe = factory.apply(CraftingBookCategory.MISC);
		ResourceLocation id = type.id();
		recipeOutput.accept(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "dynamic/" + id.getPath()), recipe, null);
	}

	@Override
	public void buildRecipes(RecipeOutput consumer) {
		specialRecipe(consumer, ModCraftingRecipes.SCAVENGE_TYPE, AssemblyScavengeRecipe::new);
		specialRecipe(consumer, ModCraftingRecipes.BULLET_TO_DRIVE_TYPE, BulletToDriveRecipe::new);
		specialRecipe(consumer, ModCraftingRecipes.COLORIZER_CHANGE_TYPE, ColorizerChangeRecipe::new);
		specialRecipe(consumer, ModCraftingRecipes.DRIVE_DUPLICATE_TYPE, DriveDuplicateRecipe::new);
		specialRecipe(consumer, ModCraftingRecipes.SENSOR_ATTACH_TYPE, SensorAttachRecipe::new);
		specialRecipe(consumer, ModCraftingRecipes.SENSOR_REMOVE_TYPE, SensorRemoveRecipe::new);

		Criterion<InventoryChangeTrigger.TriggerInstance> hasIron = has(ModTags.INGOTS_IRON);
		Criterion<InventoryChangeTrigger.TriggerInstance> hasPsimetal = has(ModTags.INGOT_PSIMETAL);
		Criterion<InventoryChangeTrigger.TriggerInstance> hasEbonyPsimetal = has(ModTags.INGOT_EBONY_PSIMETAL);
		Criterion<InventoryChangeTrigger.TriggerInstance> hasIvoryPsimetal = has(ModTags.INGOT_IVORY_PSIMETAL);
		Criterion<InventoryChangeTrigger.TriggerInstance> hasPsidust = has(ModTags.PSIDUST);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.cadAssembler.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('P', Items.PISTON)
				.pattern("IPI")
				.pattern("I I")
				.pattern(" I ")
				.unlockedBy("has_iron", hasIron)
				.save(consumer, PsiMod.location("assembler"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.programmer.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.pattern("IDI")
				.pattern("I I")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("programmer"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ebonyPsimetal.get())
				.define('S', ModTags.EBONY_SUBSTANCE)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern("SSS")
				.pattern("SIS")
				.pattern("SSS")
				.unlockedBy("has_ebony_substance", has(ModItems.ebonySubstance.get()))
				.save(consumer, PsiMod.location("ebony_psimetal"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ivoryPsimetal.get())
				.define('S', ModTags.IVORY_SUBSTANCE)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern("SSS")
				.pattern("SIS")
				.pattern("SSS")
				.unlockedBy("has_ivory_substance", has(ModItems.ivorySubstance.get()))
				.save(consumer, PsiMod.location("ivory_psimetal"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadAssemblyIron.get())
				.define('I', ModTags.INGOTS_IRON)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_iron", hasIron)
				.save(consumer, PsiMod.location("cad_assembly_iron"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadAssemblyGold.get())
				.define('I', ModTags.INGOTS_GOLD)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_gold", has(ModTags.INGOTS_GOLD))
				.save(consumer, PsiMod.location("cad_assembly_gold"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadAssemblyPsimetal.get())
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("cad_assembly_psimetal"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadAssemblyEbony.get())
				.define('I', ModTags.INGOT_EBONY_PSIMETAL)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_ebony_psimetal", hasEbonyPsimetal)
				.save(consumer, PsiMod.location("cad_assembly_ebony"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadAssemblyIvory.get())
				.define('I', ModTags.INGOT_IVORY_PSIMETAL)
				.pattern("III")
				.pattern("I  ")
				.unlockedBy("has_ivory_psimetal", hasIvoryPsimetal)
				.save(consumer, PsiMod.location("cad_assembly_ivory"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadCoreBasic.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.pattern(" I ")
				.pattern("IDI")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("cad_core_basic"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadCoreOverclocked.get())
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', ModTags.DUSTS_REDSTONE)
				.pattern(" I ")
				.pattern("IDI")
				.pattern(" I ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("cad_core_overclocked"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadCoreConductive.get())
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', ModTags.DUSTS_GLOWSTONE)
				.pattern(" I ")
				.pattern("IDI")
				.pattern(" I ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("cad_core_conductive"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadCoreHyperClocked.get())
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', ModTags.DUSTS_REDSTONE)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern(" G ")
				.pattern("IDI")
				.pattern(" G ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("cad_core_hyperclocked"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadCoreRadiative.get())
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', ModTags.DUSTS_GLOWSTONE)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern(" G ")
				.pattern("IDI")
				.pattern(" G ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("cad_core_radiative"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadSocketBasic.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.pattern("DI ")
				.pattern("I  ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("cad_socket_basic"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadSocketSignaling.get())
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', ModTags.DUSTS_REDSTONE)
				.pattern("DI ")
				.pattern("I  ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("cad_socket_signaling"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadSocketLarge.get())
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', ModTags.DUSTS_GLOWSTONE)
				.pattern("DI ")
				.pattern("I  ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("cad_socket_large"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadSocketTransmissive.get())
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', ModTags.DUSTS_REDSTONE)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("DI ")
				.pattern("IG ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("cad_socket_transmissive"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadSocketHuge.get())
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('D', ModTags.DUSTS_GLOWSTONE)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("DI ")
				.pattern("IG ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("cad_socket_huge"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadBatteryBasic.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('G', ModTags.INGOTS_GOLD)
				.pattern("I")
				.pattern("D")
				.pattern("G")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("cad_battery_basic"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadBatteryExtended.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('D', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.INGOTS_GOLD)
				.pattern("I")
				.pattern("D")
				.pattern("G")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("cad_battery_extended"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadBatteryUltradense.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('D', ModTags.GEM_PSIGEM)
				.define('G', ModTags.INGOTS_GOLD)
				.pattern("I")
				.pattern("D")
				.pattern("G")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("cad_battery_ultradense"));

		for(DyeColor color : DyeColor.values()) {
			ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BuiltInRegistries.ITEM.get(PsiMod.location(LibItemNames.CAD_COLORIZER + color.getSerializedName())))
					.group("psi:colorizer")
					.define('D', ModTags.PSIDUST)
					.define('I', ModTags.INGOTS_IRON)
					.define('G', ModTags.GLASS_BLOCKS)
					.define('C', ModTags.dye(color))
					.pattern(" D ")
					.pattern("GCG")
					.pattern(" I ")
					.unlockedBy("has_psidust", hasPsidust)
					.save(consumer, PsiMod.location(LibItemNames.CAD_COLORIZER + color.getSerializedName()));
		}

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadColorizerRainbow.get())
				.group("psi:colorizer")
				.define('D', ModTags.PSIDUST)
				.define('I', ModTags.INGOTS_IRON)
				.define('G', ModTags.GLASS_BLOCKS)
				.define('C', ModTags.GEMS_PRISMARINE)
				.pattern(" D ")
				.pattern("GCG")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("cad_colorizer_rainbow"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadColorizerPsi.get())
				.group("psi:colorizer")
				.define('D', ModTags.PSIDUST)
				.define('I', ModTags.INGOTS_IRON)
				.define('G', ModTags.GLASS_BLOCKS)
				.define('C', ModTags.PSIDUST)
				.pattern(" D ")
				.pattern("GCG")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("cad_colorizer_psi"));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.spellBullet.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.pattern("ID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("spell_bullet_basic"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.projectileSpellBullet.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', ItemTags.ARROWS)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("spell_bullet_projectile"));
		new BulletUpgradeRecipe.Builder(ModItems.projectileSpellBullet.get())
				.requires(ModItems.spellBullet.get())
				.requires(Ingredient.of(ItemTags.ARROWS))
				.unlockedBy("has_psidust", has(ModItems.psidust.get()))
				.save(consumer, PsiMod.location("spell_bullet_projectile_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.loopSpellBullet.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', ModTags.STRINGS)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("spell_bullet_loopcast"));
		new BulletUpgradeRecipe.Builder(ModItems.loopSpellBullet.get())
				.requires(ModItems.spellBullet.get())
				.requires(Ingredient.of(ModTags.STRINGS))
				.unlockedBy("has_psidust", has(ModItems.psidust.get()))
				.save(consumer, PsiMod.location("spell_bullet_loopcast_upgrade"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.circleSpellBullet.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', tagOrItem(ModTags.SLIME_BALLS, Items.SNOWBALL))
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("spell_bullet_circle"));
		new BulletUpgradeRecipe.Builder(ModItems.circleSpellBullet.get())
				.requires(ModItems.spellBullet.get())
				.requires(tagOrItem(ModTags.SLIME_BALLS, Items.SNOWBALL))
				.unlockedBy("has_psidust", has(ModItems.psidust.get()))
				.save(consumer, PsiMod.location("spell_bullet_circle_upgrade"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.grenadeSpellBullet.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', ModTags.GUNPOWDERS)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("spell_bullet_grenade"));
		new BulletUpgradeRecipe.Builder(ModItems.grenadeSpellBullet.get())
				.requires(ModItems.spellBullet.get())
				.requires(Ingredient.of(ModTags.GUNPOWDERS))
				.unlockedBy("has_psidust", has(ModItems.psidust.get()))
				.save(consumer, PsiMod.location("spell_bullet_grenade_upgrade"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.chargeSpellBullet.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', ModTags.DUSTS_REDSTONE)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("spell_bullet_charge"));
		new BulletUpgradeRecipe.Builder(ModItems.chargeSpellBullet.get())
				.requires(ModItems.spellBullet.get())
				.requires(Ingredient.of(ModTags.DUSTS_REDSTONE))
				.unlockedBy("has_psidust", has(ModItems.psidust.get()))
				.save(consumer, PsiMod.location("spell_bullet_charge_upgrade"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.mineSpellBullet.get())
				.define('I', ModTags.INGOTS_IRON)
				.define('D', ModTags.PSIDUST)
				.define('A', ItemTags.BUTTONS)
				.pattern("AID")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("spell_bullet_mine"));
		new BulletUpgradeRecipe.Builder(ModItems.mineSpellBullet.get())
				.requires(ModItems.spellBullet.get())
				.requires(Ingredient.of(ItemTags.BUTTONS))
				.unlockedBy("has_psidust", has(ModItems.psidust.get()))
				.save(consumer, PsiMod.location("spell_bullet_mine_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.spellDrive.get())
				.define('I', ModTags.INGOT_PSIMETAL)
				.define('R', ModTags.DUSTS_REDSTONE)
				.pattern("I")
				.pattern("R")
				.pattern("I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("spell_drive"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalShovel.get())
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.define('I', ModTags.INGOTS_IRON)
				.pattern("GP")
				.pattern(" I")
				.pattern(" I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_shovel"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalPickaxe.get())
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.define('I', ModTags.INGOTS_IRON)
				.pattern("PGP")
				.pattern(" I ")
				.pattern(" I ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_pickaxe"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalAxe.get())
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.define('I', ModTags.INGOTS_IRON)
				.pattern("GP")
				.pattern("PI")
				.pattern(" I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_axe"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalSword.get())
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.define('I', ModTags.INGOTS_IRON)
				.pattern("P")
				.pattern("G")
				.pattern("I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_sword"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalExosuitHelmet.get())
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("GPG")
				.pattern("P P")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_exosuit_helmet"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalExosuitChestplate.get())
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("P P")
				.pattern("GPG")
				.pattern("PPP")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_exosuit_chestplate"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalExosuitLeggings.get())
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("GPG")
				.pattern("P P")
				.pattern("P P")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_exosuit_leggings"));
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.psimetalExosuitBoots.get())
				.define('P', ModTags.INGOT_PSIMETAL)
				.define('G', ModTags.GEM_PSIGEM)
				.pattern("G G")
				.pattern("P P")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_exosuit_boots"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.flashRing.get())
				.define('E', ModTags.INGOT_EBONY_PSIMETAL)
				.define('G', ModTags.DUSTS_GLOWSTONE)
				.define('P', ModTags.GEM_PSIGEM)
				.pattern(" E ")
				.pattern("EGE")
				.pattern(" P ")
				.unlockedBy("has_ebony_psimetal", hasEbonyPsimetal)
				.save(consumer, PsiMod.location("flash_ring"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.detonator.get())
				.define('P', ModTags.PSIDUST)
				.define('B', ItemTags.BUTTONS)
				.define('I', ModTags.INGOTS_IRON)
				.pattern(" B ")
				.pattern("IPI")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("detonator"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.exosuitController.get())
				.define('R', ModTags.DUSTS_REDSTONE)
				.define('G', ModTags.GLASS_BLOCKS)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern("R")
				.pattern("G")
				.pattern("I")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("exosuit_controller"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.vectorRuler.get())
				.define('D', ModTags.PSIDUST)
				.define('I', ModTags.INGOTS_IRON)
				.pattern("D")
				.pattern("I")
				.pattern("I")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("vector_ruler"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.exosuitSensorLight.get())
				.define('M', ModTags.DUSTS_GLOWSTONE)
				.define('R', ModTags.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("exosuit_sensor_light"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.exosuitSensorWater.get())
				.define('M', ModTags.GEMS_PRISMARINE)
				.define('R', ModTags.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("exosuit_sensor_water"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.exosuitSensorHeat.get())
				.define('M', Items.FIRE_CHARGE)
				.define('R', ModTags.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("exosuit_sensor_heat"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.exosuitSensorStress.get())
				.define('M', Items.GLISTERING_MELON_SLICE)
				.define('R', ModTags.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("exosuit_sensor_stress"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.exosuitSensorTrigger.get())
				.define('M', Items.GUNPOWDER)
				.define('R', ModTags.INGOTS_IRON)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" I ")
				.pattern("IMR")
				.pattern(" R ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("exosuit_sensor_trigger"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cadColorizerEmpty.get())
				.define('D', ModTags.PSIDUST)
				.define('G', ModTags.GLASS_BLOCKS)
				.define('I', ModTags.INGOTS_IRON)
				.pattern(" D ")
				.pattern("G G")
				.pattern(" I ")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("cad_colorizer_empty"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.psidustBlock.get().asItem())
				.define('I', ModItems.psidust.get())
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("psidust_block"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.psimetalBlock.get().asItem())
				.define('I', ModItems.psimetal.get())
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_block"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.psigemBlock.get().asItem())
				.define('I', ModItems.psigem.get())
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_psigem", has(ModItems.psigem.get()))
				.save(consumer, PsiMod.location("psigem_block"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.psimetalEbony.get().asItem())
				.define('I', ModItems.ebonyPsimetal.get())
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_ebony_psimetal", hasEbonyPsimetal)
				.save(consumer, PsiMod.location("ebony_block"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.psimetalIvory.get().asItem())
				.define('I', ModItems.ivoryPsimetal.get())
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_ivory_psimetal", hasIvoryPsimetal)
				.save(consumer, PsiMod.location("ivory_block"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.psidust.get(), 9)
				.requires(ModBlocks.psidustBlock.get().asItem())
				.unlockedBy("has_psidust", hasPsidust)
				.save(consumer, PsiMod.location("psidust_shapeless"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.psimetal.get(), 9)
				.requires(ModBlocks.psimetalBlock.get().asItem())
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_shapeless"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.psigem.get(), 9)
				.requires(ModBlocks.psigemBlock.get().asItem())
				.unlockedBy("has_psigem", has(ModItems.psigem.get()))
				.save(consumer, PsiMod.location("psigem_shapeless"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.ebonyPsimetal.get(), 9)
				.requires(ModBlocks.psimetalEbony.get().asItem())
				.unlockedBy("has_ebony_psimetal", hasEbonyPsimetal)
				.save(consumer, PsiMod.location("ebony_ingot_shapeless"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.ivoryPsimetal.get(), 9)
				.requires(ModBlocks.psimetalIvory.get().asItem())
				.unlockedBy("has_ivory_psimetal", hasIvoryPsimetal)
				.save(consumer, PsiMod.location("ivory_ingot_shapeless"));
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.psimetalPlateBlack.get().asItem())
				.define('C', ItemTags.COALS)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" C ")
				.pattern("CIC")
				.pattern(" C ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_plate_black"));
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.psimetalPlateWhite.get().asItem())
				.define('C', ModTags.GEMS_QUARTZ)
				.define('I', ModTags.INGOT_PSIMETAL)
				.pattern(" C ")
				.pattern("CIC")
				.pattern(" C ")
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_plate_white"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.psimetalPlateBlackLight.get().asItem())
				.requires(ModTags.DUSTS_GLOWSTONE)
				.requires(ModBlocks.psimetalPlateBlack.get().asItem())
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_plate_black_light"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.psimetalPlateWhiteLight.get().asItem())
				.requires(ModTags.DUSTS_GLOWSTONE)
				.requires(ModBlocks.psimetalPlateWhite.get().asItem())
				.unlockedBy("has_psimetal", hasPsimetal)
				.save(consumer, PsiMod.location("psimetal_plate_white_light"));
		this.buildTrickRecipes(consumer);
	}

	protected void buildTrickRecipes(RecipeOutput consumer) {
		TrickRecipeBuilder.of(ModItems.psidust.get()).input(ModTags.DUSTS_REDSTONE).cad(ModItems.cadAssemblyIron.get()).build(consumer);
		TrickRecipeBuilder.of(PatchouliAPI.get().getBookStack(LibResources.PATCHOULI_BOOK)).input(Items.BOOK).cad(ModItems.cadAssemblyIron.get()).build(consumer);

		TrickRecipeBuilder.of(ModItems.cadAssemblyPsimetal.get())
				.input(ModItems.cadAssemblyGold.get())
				.trick(PsiMod.location(LibPieceNames.TRICK_INFUSION))
				.cad(ModItems.cadAssemblyIron.get())
				.unlockedBy(getHasName(ModItems.cadAssemblyGold.get()), has(ModItems.cadAssemblyGold.get()))
				.build(consumer, PsiMod.location("gold_to_psimetal_assembly_upgrade"));

		TrickRecipeBuilder.of(ModItems.psimetal.get())
				.input(ModTags.INGOTS_GOLD)
				.trick(PsiMod.location(LibPieceNames.TRICK_INFUSION))
				.cad(ModItems.cadAssemblyIron.get())
				.unlockedBy("has_gold_ingot", has(ModTags.INGOTS_GOLD))
				.build(consumer);

		TrickRecipeBuilder.of(ModItems.psigem.get())
				.input(ModTags.GEMS_DIAMOND)
				.trick(PsiMod.location(LibPieceNames.TRICK_GREATER_INFUSION))
				.cad(ModItems.cadAssemblyPsimetal.get())
				.unlockedBy("has_diamond", has(ModTags.GEMS_DIAMOND))
				.build(consumer);

		TrickRecipeBuilder.of(ModItems.ebonySubstance.get())
				.input(ItemTags.COALS)
				.trick(PsiMod.location(LibPieceNames.TRICK_EBONY_IVORY))
				.cad(ModItems.cadAssemblyPsimetal.get())
				.dimension(Level.END)
				.unlockedBy("has_coal", has(ItemTags.COALS))
				.build(consumer);

		TrickRecipeBuilder.of(ModItems.ivorySubstance.get())
				.input(ModTags.GEMS_QUARTZ)
				.trick(PsiMod.location(LibPieceNames.TRICK_EBONY_IVORY))
				.cad(ModItems.cadAssemblyPsimetal.get())
				.dimension(Level.END)
				.unlockedBy("has_quartz", has(ModTags.GEMS_QUARTZ))
				.build(consumer);
	}

	private static Ingredient tagOrItem(TagKey<Item> tag, ItemLike item) {
		JsonObject tagValue = new JsonObject();
		tagValue.addProperty("tag", tag.location().toString());
		JsonObject itemValue = new JsonObject();
		itemValue.addProperty("item", BuiltInRegistries.ITEM.getKey(item.asItem()).toString());
		JsonArray values = new JsonArray();
		values.add(tagValue);
		values.add(itemValue);
		return Ingredient.CODEC.parse(JsonOps.INSTANCE, values).getOrThrow();
	}

}

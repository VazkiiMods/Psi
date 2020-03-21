package vazkii.psi.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalAdvancement;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.crafting.recipe.*;
import vazkii.psi.common.item.base.ModItems;
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

		// TODO: Pull Magical Psi recipes into here? Conditional recipes can hold more than one recipe, and 
		//  only the first one matching its condition will be enabled, so it makes sense to have both in one json.

		buildMagicalWrapper(Psi.location("assembler"), consumer,
				hasIron, "has_iron", ShapedRecipeBuilder.shapedRecipe(ModBlocks.cadAssembler)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('P', Items.PISTON)
						.patternLine("IPI")
						.patternLine("I I")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("programmer"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModBlocks.programmer)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('D', ModTags.PSIDUST)
						.patternLine("IDI")
						.patternLine("I I")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("ebony_psimetal"), consumer,
				hasItem(ModItems.ebonySubstance), "has_ebony_substance", ShapedRecipeBuilder.shapedRecipe(ModItems.ebonyPsimetal)
						.key('S', ModTags.EBONY_SUBSTANCE)
						.key('I', ModTags.INGOT_PSIMETAL)
						.patternLine("SSS")
						.patternLine("SIS")
						.patternLine("SSS")
		);
		buildMagicalWrapper(Psi.location("ivory_psimetal"), consumer,
				hasItem(ModItems.ivorySubstance), "has_ivory_substance", ShapedRecipeBuilder.shapedRecipe(ModItems.ivoryPsimetal)
						.key('S', ModTags.IVORY_SUBSTANCE)
						.key('I', ModTags.INGOT_PSIMETAL)
						.patternLine("SSS")
						.patternLine("SIS")
						.patternLine("SSS")
		);
		buildMagicalWrapper(Psi.location("cad_assembly_iron"), consumer,
				hasIron, "has_iron", ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyIron)
						.key('I', Tags.Items.INGOTS_IRON)
						.patternLine("III")
						.patternLine("I  ")
		);
		buildMagicalWrapper(Psi.location("cad_assembly_gold"), consumer,
				hasItem(Tags.Items.INGOTS_GOLD), "has_gold", ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyGold)
						.key('I', Tags.Items.INGOTS_GOLD)
						.patternLine("III")
						.patternLine("I  ")
		);
		buildMagicalWrapper(Psi.location("cad_assembly_psimetal"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyPsimetal)
						.key('I', ModTags.INGOT_PSIMETAL)
						.patternLine("III")
						.patternLine("I  ")
		);
		buildMagicalWrapper(Psi.location("cad_assembly_ebony"), consumer,
				hasEbonyPsimetal, "has_ebony_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyEbony)
						.key('I', ModTags.INGOT_EBONY_PSIMETAL)
						.patternLine("III")
						.patternLine("I  ")
		);
		buildMagicalWrapper(Psi.location("cad_assembly_ivory"), consumer,
				hasIvoryPsimetal, "has_ivory_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyIvory)
						.key('I', ModTags.INGOT_IVORY_PSIMETAL)
						.patternLine("III")
						.patternLine("I  ")
		);
		buildMagicalWrapper(Psi.location("cad_core_basic"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreBasic)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('D', ModTags.PSIDUST)
						.patternLine(" I ")
						.patternLine("IDI")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_core_overclocked"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreOverclocked)
						.key('I', ModTags.INGOT_PSIMETAL)
						.key('D', Tags.Items.DUSTS_REDSTONE)
						.patternLine(" I ")
						.patternLine("IDI")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_core_conductive"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreConductive)
						.key('I', ModTags.INGOT_PSIMETAL)
						.key('D', Tags.Items.DUSTS_GLOWSTONE)
						.patternLine(" I ")
						.patternLine("IDI")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_core_hyperclocked"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreHyperClocked)
						.key('I', ModTags.INGOT_PSIMETAL)
						.key('D', Tags.Items.DUSTS_REDSTONE)
						.key('G', ModTags.GEM_PSIGEM)
						.patternLine(" G ")
						.patternLine("IDI")
						.patternLine(" G ")
		);
		buildMagicalWrapper(Psi.location("cad_core_radiative"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreRadiative)
						.key('I', ModTags.INGOT_PSIMETAL)
						.key('D', Tags.Items.DUSTS_GLOWSTONE)
						.key('G', ModTags.GEM_PSIGEM)
						.patternLine(" G ")
						.patternLine("IDI")
						.patternLine(" G ")
		);
		buildMagicalWrapper(Psi.location("cad_socket_basic"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketBasic)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('D', ModTags.PSIDUST)
						.patternLine("DI ")
						.patternLine("I  ")
		);
		buildMagicalWrapper(Psi.location("cad_socket_signaling"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketSignaling)
						.key('I', ModTags.INGOT_PSIMETAL)
						.key('D', Tags.Items.DUSTS_REDSTONE)
						.patternLine("DI ")
						.patternLine("I  ")
		);
		buildMagicalWrapper(Psi.location("cad_socket_large"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketLarge)
						.key('I', ModTags.INGOT_PSIMETAL)
						.key('D', Tags.Items.DUSTS_GLOWSTONE)
						.patternLine("DI ")
						.patternLine("I  ")
		);
		buildMagicalWrapper(Psi.location("cad_socket_transmissive"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketTransmissive)
						.key('I', ModTags.INGOT_PSIMETAL)
						.key('D', Tags.Items.DUSTS_REDSTONE)
						.key('G', ModTags.GEM_PSIGEM)
						.patternLine("DI ")
						.patternLine("IG ")
		);
		buildMagicalWrapper(Psi.location("cad_socket_huge"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketHuge)
						.key('I', ModTags.INGOT_PSIMETAL)
						.key('D', Tags.Items.DUSTS_GLOWSTONE)
						.key('G', ModTags.GEM_PSIGEM)
						.patternLine("DI ")
						.patternLine("IG ")
		);
		buildMagicalWrapper(Psi.location("cad_battery_basic"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadBatteryBasic)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('D', ModTags.PSIDUST)
						.key('G', Tags.Items.INGOTS_GOLD)
						.patternLine("I")
						.patternLine("D")
						.patternLine("G")
		);
		buildMagicalWrapper(Psi.location("cad_battery_extended"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.cadBatteryExtended)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('D', ModTags.INGOT_PSIMETAL)
						.key('G', Tags.Items.INGOTS_GOLD)
						.patternLine("I")
						.patternLine("D")
						.patternLine("G")
		);
		buildMagicalWrapper(Psi.location("cad_battery_ultradense"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.cadBatteryUltradense)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('D', ModTags.GEM_PSIGEM)
						.key('G', Tags.Items.INGOTS_GOLD)
						.patternLine("I")
						.patternLine("D")
						.patternLine("G")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_white"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerWhite)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_WHITE)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_orange"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerOrange)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_ORANGE)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_magenta"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerMagenta)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_MAGENTA)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_light_blue"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerLightBlue)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_LIGHT_BLUE)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_yellow"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerYellow)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_YELLOW)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_lime"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerLime)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_LIME)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_pink"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerPink)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_PINK)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_gray"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerGray)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_GRAY)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_light_gray"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerLightGray)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_LIGHT_GRAY)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_cyan"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerCyan)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_CYAN)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_purple"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerPurple)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_PURPLE)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_blue"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerBlue)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_BLUE)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_brown"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerBrown)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_BROWN)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_green"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerGreen)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_GREEN)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_red"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerRed)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_RED)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_black"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerBlack)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.DYES_BLACK)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_rainbow"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerRainbow)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', Tags.Items.GEMS_PRISMARINE)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("cad_colorizer_psi"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerPsi)
						.setGroup("psi:colorizer")
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('G', Tags.Items.GLASS)
						.key('C', ModTags.PSIDUST)
						.patternLine(" D ")
						.patternLine("GCG")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("spell_bullet_basic"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.spellBullet)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('D', ModTags.PSIDUST)
						.patternLine("ID")
		);
		buildMagicalWrapper(Psi.location("spell_bullet_projectile"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.projectileSpellBullet)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('D', ModTags.PSIDUST)
						.key('A', Tags.Items.ARROWS)
						.patternLine("AID")
		);

		buildMagicalWrapper(Psi.location("spell_bullet_loopcast"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.loopSpellBullet)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('D', ModTags.PSIDUST)
						.key('A', Tags.Items.STRING)
						.patternLine("AID")
		);
		buildMagicalWrapper(Psi.location("spell_bullet_circle"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.circleSpellBullet)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('D', ModTags.PSIDUST)
						.key('A', Ingredient.fromItemListStream(Stream.of(
								new Ingredient.TagList(Tags.Items.SLIMEBALLS),
								new Ingredient.SingleItemList(new ItemStack(Items.SNOWBALL)))))
						.patternLine("AID")
		);
		buildMagicalWrapper(Psi.location("spell_bullet_grenade"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.grenadeSpellBullet)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('D', ModTags.PSIDUST)
						.key('A', Tags.Items.GUNPOWDER)
						.patternLine("AID")
		);
		buildMagicalWrapper(Psi.location("spell_bullet_charge"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.chargeSpellBullet)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('D', ModTags.PSIDUST)
						.key('A', Tags.Items.DUSTS_REDSTONE)
						.patternLine("AID")
		);
		buildMagicalWrapper(Psi.location("spell_bullet_mine"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.mineSpellBullet)
						.key('I', Tags.Items.INGOTS_IRON)
						.key('D', ModTags.PSIDUST)
						.key('A', ItemTags.BUTTONS)
						.patternLine("AID")
		);

		buildMagicalWrapper(Psi.location("spell_drive"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.spellDrive)
						.key('I', ModTags.INGOT_PSIMETAL)
						.key('R', Tags.Items.DUSTS_REDSTONE)
						.patternLine("I")
						.patternLine("R")
						.patternLine("I")
		);
		buildMagicalWrapper(Psi.location("psimetal_shovel"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalShovel)
						.key('P', ModTags.INGOT_PSIMETAL)
						.key('G', ModTags.GEM_PSIGEM)
						.key('I', Tags.Items.INGOTS_IRON)
						.patternLine("GP")
						.patternLine(" I")
						.patternLine(" I")
		);
		buildMagicalWrapper(Psi.location("psimetal_pickaxe"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalPickaxe)
						.key('P', ModTags.INGOT_PSIMETAL)
						.key('G', ModTags.GEM_PSIGEM)
						.key('I', Tags.Items.INGOTS_IRON)
						.patternLine("PGP")
						.patternLine(" I ")
						.patternLine(" I ")
		);
		buildMagicalWrapper(Psi.location("psimetal_axe"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalAxe)
						.key('P', ModTags.INGOT_PSIMETAL)
						.key('G', ModTags.GEM_PSIGEM)
						.key('I', Tags.Items.INGOTS_IRON)
						.patternLine("GP")
						.patternLine("PI")
						.patternLine(" I")
		);
		buildMagicalWrapper(Psi.location("psimetal_sword"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalSword)
						.key('P', ModTags.INGOT_PSIMETAL)
						.key('G', ModTags.GEM_PSIGEM)
						.key('I', Tags.Items.INGOTS_IRON)
						.patternLine("P")
						.patternLine("G")
						.patternLine("I")
		);
		buildMagicalWrapper(Psi.location("psimetal_exosuit_helmet"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalExosuitHelmet)
						.key('P', ModTags.INGOT_PSIMETAL)
						.key('G', ModTags.GEM_PSIGEM)
						.patternLine("GPG")
						.patternLine("P P")
		);
		buildMagicalWrapper(Psi.location("psimetal_exosuit_chestplate"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalExosuitChestplate)
						.key('P', ModTags.INGOT_PSIMETAL)
						.key('G', ModTags.GEM_PSIGEM)
						.patternLine("P P")
						.patternLine("GPG")
						.patternLine("PPP")
		);
		buildMagicalWrapper(Psi.location("psimetal_exosuit_leggings"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalExosuitLeggings)
						.key('P', ModTags.INGOT_PSIMETAL)
						.key('G', ModTags.GEM_PSIGEM)
						.patternLine("GPG")
						.patternLine("P P")
						.patternLine("P P")
		);
		buildMagicalWrapper(Psi.location("psimetal_exosuit_boots"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalExosuitBoots)
						.key('P', ModTags.INGOT_PSIMETAL)
						.key('G', ModTags.GEM_PSIGEM)
						.patternLine("G G")
						.patternLine("P P")
		);
		buildMagicalWrapper(Psi.location("detonator"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.detonator)
						.key('P', ModTags.PSIDUST)
						.key('B', ItemTags.BUTTONS)
						.key('I', Tags.Items.INGOTS_IRON)
						.patternLine(" B ")
						.patternLine("IPI")
		);
		buildMagicalWrapper(Psi.location("exosuit_controller"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitController)
						.key('R', Tags.Items.DUSTS_REDSTONE)
						.key('G', Tags.Items.GLASS)
						.key('I', ModTags.INGOT_PSIMETAL)
						.patternLine("R")
						.patternLine("G")
						.patternLine("I")
		);
		buildMagicalWrapper(Psi.location("vector_ruler"), consumer,
				hasPsidust, "has_psidust", ShapedRecipeBuilder.shapedRecipe(ModItems.vectorRuler)
						.key('D', ModTags.PSIDUST)
						.key('I', Tags.Items.INGOTS_IRON)
						.patternLine("D")
						.patternLine("I")
						.patternLine("I")
		);
		buildMagicalWrapper(Psi.location("exosuit_sensor_light"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitSensorLight)
						.key('M', Tags.Items.DUSTS_GLOWSTONE)
						.key('R', Tags.Items.INGOTS_IRON)
						.key('I', ModTags.INGOT_PSIMETAL)
						.patternLine(" I ")
						.patternLine("IMR")
						.patternLine(" R ")
		);
		buildMagicalWrapper(Psi.location("exosuit_sensor_water"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitSensorWater)
						.key('M', Tags.Items.GEMS_PRISMARINE)
						.key('R', Tags.Items.INGOTS_IRON)
						.key('I', ModTags.INGOT_PSIMETAL)
						.patternLine(" I ")
						.patternLine("IMR")
						.patternLine(" R ")
		);
		buildMagicalWrapper(Psi.location("exosuit_sensor_heat"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitSensorHeat)
						.key('M', Items.FIRE_CHARGE)
						.key('R', Tags.Items.INGOTS_IRON)
						.key('I', ModTags.INGOT_PSIMETAL)
						.patternLine(" I ")
						.patternLine("IMR")
						.patternLine(" R ")
		);
		buildMagicalWrapper(Psi.location("exosuit_sensor_stress"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitSensorStress)
						.key('M', Items.GLISTERING_MELON_SLICE)
						.key('R', Tags.Items.INGOTS_IRON)
						.key('I', ModTags.INGOT_PSIMETAL)
						.patternLine(" I ")
						.patternLine("IMR")
						.patternLine(" R ")
		);
		buildMagicalWrapper(Psi.location("psidust_block_shapeless"), consumer,
				hasPsidust, "has_psidust",
				ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psidustBlock.asItem())
						.addIngredient(ModItems.psidust, 9)
		);
		buildMagicalWrapper(Psi.location("psimetal_block_shapeless"), consumer,
				hasPsimetal, "has_psimetal",
				ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psimetalBlock.asItem())
						.addIngredient(ModItems.psimetal, 9)
		);
		buildMagicalWrapper(Psi.location("psigem_block_shapeless"), consumer,
				hasItem(ModItems.psigem), "has_psigem",
				ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psigemBlock.asItem())
						.addIngredient(ModItems.psigem, 9)
		);
		buildMagicalWrapper(Psi.location("ebony_block_shapeless"), consumer,
				hasEbonyPsimetal, "has_ebony_psimetal",
				ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psimetalEbony.asItem())
						.addIngredient(ModItems.ebonyPsimetal, 9)
		);
		buildMagicalWrapper(Psi.location("ivory_block_shapeless"), consumer,
				hasIvoryPsimetal, "has_ivory_psimetal",
				ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psimetalIvory.asItem())
						.addIngredient(ModItems.ivoryPsimetal, 9)
		);
		buildMagicalWrapper(Psi.location("psimetal_plate_black"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModBlocks.psimetalPlateBlack.asItem())
						.key('C', ItemTags.COALS)
						.key('I', ModTags.INGOT_PSIMETAL)
						.patternLine(" C ")
						.patternLine("CIC")
						.patternLine(" C ")
		);
		buildMagicalWrapper(Psi.location("psimetal_plate_white"), consumer,
				hasPsimetal, "has_psimetal", ShapedRecipeBuilder.shapedRecipe(ModBlocks.psimetalPlateWhite.asItem())
						.key('C', Tags.Items.GEMS_QUARTZ)
						.key('I', ModTags.INGOT_PSIMETAL)
						.patternLine(" C ")
						.patternLine("CIC")
						.patternLine(" C ")
		);
		buildMagicalWrapper(Psi.location("psimetal_plate_black_light"), consumer,
				hasPsimetal, "has_psimetal",
				ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psimetalPlateBlackLight.asItem())
						.addIngredient(Tags.Items.DUSTS_GLOWSTONE)
						.addIngredient(ModBlocks.psimetalPlateBlack.asItem())
		);
		buildMagicalWrapper(Psi.location("psimetal_plate_white_light"), consumer,
				hasPsimetal, "has_psimetal",
				ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psimetalPlateWhiteLight.asItem())
						.addIngredient(Tags.Items.DUSTS_GLOWSTONE)
						.addIngredient(ModBlocks.psimetalPlateWhite.asItem())
		);
	}

	private static void buildMagicalWrapper(ResourceLocation id, Consumer<IFinishedRecipe> consumer,
											ICriterionInstance recipeUnlockCriterion, String criterionName, ShapelessRecipeBuilder builder) {
		builder.addCriterion(criterionName, recipeUnlockCriterion);
		buildMagicalWrapper(id, consumer, recipeUnlockCriterion, criterionName, builder::build);
	}

	private static void buildMagicalWrapper(ResourceLocation id, Consumer<IFinishedRecipe> consumer,
											ICriterionInstance recipeUnlockCriterion, String criterionName, ShapedRecipeBuilder builder) {
		builder.addCriterion(criterionName, recipeUnlockCriterion);
		buildMagicalWrapper(id, consumer, recipeUnlockCriterion, criterionName, builder::build);
	}

	private static void buildMagicalWrapper(ResourceLocation id, Consumer<IFinishedRecipe> consumer, ICriterionInstance criterion,
											String criterionName, Consumer<Consumer<IFinishedRecipe>> recipe) {
		ConditionalRecipe.builder()
				.addCondition(new NotCondition(MagicalPsiCondition.INSTANCE))
				.addRecipe(recipe)
				.setAdvancement(Psi.location("recipe/" + id.getPath()), new ConditionalAdvancement.Builder()
						.addCondition(new NotCondition(MagicalPsiCondition.INSTANCE))
						.addAdvancement(Advancement.Builder.builder()
								.withParentId(new ResourceLocation("recipes/root"))
								.withRewards(AdvancementRewards.Builder.recipe(id))
								.withCriterion(criterionName, criterion)
								.withCriterion("has_the_recipe", new RecipeUnlockedTrigger.Instance(id))
								.withRequirementsStrategy(IRequirementsStrategy.OR)))
				.build(consumer, id);
	}

	private static void specialRecipe(SpecialRecipeSerializer<?> serializer, Consumer<IFinishedRecipe> consumer) {
		CustomRecipeBuilder.func_218656_a(serializer).build(consumer, Psi.location("dynamic/" + serializer.getRegistryName().getPath()).toString());
	}

}

package vazkii.psi.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ForgeRecipeProvider;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.crafting.recipe.*;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.ModTags;

import java.util.function.Consumer;

public class RecipeGenerator extends ForgeRecipeProvider implements IConditionBuilder {

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

        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModBlocks.cadAssembler)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('P', Items.PISTON)
                        .patternLine("IPI")
                        .patternLine("I I")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "assembler"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModBlocks.programmer)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', ModTags.DUST_PSI)
                        .patternLine("IDI")
                        .patternLine("I I")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "programmer"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.ebonyPsimetal)
                        .key('S', ModTags.SUBSTANCE_EBONY)
                        .key('I', ModTags.INGOT_PSI)
                        .patternLine("SSS")
                        .patternLine("SIS")
                        .patternLine("SSS")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "ebony_psimetal"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.ivoryPsimetal)
                        .key('S', ModTags.SUBSTANCE_IVORY)
                        .key('I', ModTags.INGOT_PSI)
                        .patternLine("SSS")
                        .patternLine("SIS")
                        .patternLine("SSS")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "ivory_psimetal"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyIron)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .patternLine("III")
                        .patternLine("I  ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_assembly_iron"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyGold)
                        .key('I', Tags.Items.INGOTS_GOLD)
                        .patternLine("III")
                        .patternLine("I  ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_assembly_gold"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyPsimetal)
                        .key('I', ModTags.INGOT_PSI)
                        .patternLine("III")
                        .patternLine("I  ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_assembly_psimetal"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyEbony)
                        .key('I', ModTags.INGOT_EBONY_PSI)
                        .patternLine("III")
                        .patternLine("I  ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_assembly_ebony"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadAssemblyIvory)
                        .key('I', ModTags.INGOT_IVORY_PSI)
                        .patternLine("III")
                        .patternLine("I  ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_assembly_ivory"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreBasic)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', ModTags.DUST_PSI)
                        .patternLine(" I ")
                        .patternLine("IDI")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_core_basic"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreOverclocked)
                        .key('I', ModTags.INGOT_PSI)
                        .key('D', Tags.Items.DUSTS_REDSTONE)
                        .patternLine(" I ")
                        .patternLine("IDI")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_core_overclocked"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreConductive)
                        .key('I', ModTags.INGOT_PSI)
                        .key('D', Tags.Items.DUSTS_GLOWSTONE)
                        .patternLine(" I ")
                        .patternLine("IDI")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_core_conductive"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreHyperClocked)
                        .key('I', ModTags.INGOT_PSI)
                        .key('D', Tags.Items.DUSTS_REDSTONE)
                        .key('G', ModTags.GEM_PSI)
                        .patternLine(" G ")
                        .patternLine("IDI")
                        .patternLine(" G ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_core_hyperclocked"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadCoreRadiative)
                        .key('I', ModTags.INGOT_PSI)
                        .key('D', Tags.Items.DUSTS_GLOWSTONE)
                        .key('G', ModTags.GEM_PSI)
                        .patternLine(" G ")
                        .patternLine("IDI")
                        .patternLine(" G ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_core_radiative"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketBasic)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', ModTags.DUST_PSI)
                        .patternLine("DI ")
                        .patternLine("I  ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_socket_basic"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketSignaling)
                        .key('I', ModTags.INGOT_PSI)
                        .key('D', Tags.Items.DUSTS_REDSTONE)
                        .patternLine("DI ")
                        .patternLine("I  ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_socket_signaling"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketLarge)
                        .key('I', ModTags.INGOT_PSI)
                        .key('D', Tags.Items.DUSTS_GLOWSTONE)
                        .patternLine("DI ")
                        .patternLine("I  ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_socket_large"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketTransmissive)
                        .key('I', ModTags.INGOT_PSI)
                        .key('D', Tags.Items.DUSTS_REDSTONE)
                        .key('G', ModTags.GEM_PSI)
                        .patternLine("DI ")
                        .patternLine("IG ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_socket_transmissive"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadSocketHuge)
                        .key('I', ModTags.INGOT_PSI)
                        .key('D', Tags.Items.DUSTS_GLOWSTONE)
                        .key('G', ModTags.GEM_PSI)
                        .patternLine("DI ")
                        .patternLine("IG ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_socket_huge"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadBatteryBasic)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', ModTags.DUST_PSI)
                        .key('G', Tags.Items.INGOTS_GOLD)
                        .patternLine("I")
                        .patternLine("D")
                        .patternLine("G")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_battery_basic"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadBatteryExtended)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', ModTags.INGOT_PSI)
                        .key('G', Tags.Items.INGOTS_GOLD)
                        .patternLine("I")
                        .patternLine("D")
                        .patternLine("G")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_battery_extended"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadBatteryUltradense)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', ModTags.GEM_PSI)
                        .key('G', Tags.Items.INGOTS_GOLD)
                        .patternLine("I")
                        .patternLine("D")
                        .patternLine("G")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_battery_ultradense"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerWhite)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_WHITE)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_white"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerOrange)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_ORANGE)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_orange"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerMagenta)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_MAGENTA)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_magenta"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerLightBlue)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_LIGHT_BLUE)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_light_blue"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerYellow)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_YELLOW)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_yellow"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerLime)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_LIME)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_lime"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerPink)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_PINK)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_pink"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerGray)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_GRAY)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_gray"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerLightGray)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_LIGHT_GRAY)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_light_gray"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerCyan)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_CYAN)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_cyan"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerPurple)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_PURPLE)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_purple"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerBlue)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_BLUE)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_blue"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerBrown)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_BROWN)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_brown"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerGreen)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_GREEN)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_green"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerRed)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_RED)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_red"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerBlack)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.DYES_BLACK)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_black"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerRainbow)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', Tags.Items.GEMS_PRISMARINE)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_rainbow"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.cadColorizerPsi)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('G', Tags.Items.GLASS)
                        .key('C', ModTags.DUST_PSI)
                        .patternLine(" D ")
                        .patternLine("GCG")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "cad_colorizer_psi"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.spellBullet)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', Tags.Items.GLASS)
                        .patternLine("ID")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "spell_bullet_basic"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.projectileSpellBullet)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', Tags.Items.GLASS)
                        .key('A', Tags.Items.ARROWS)
                        .patternLine("AID")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "spell_bullet_projectile"));

        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.loopSpellBullet)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', Tags.Items.GLASS)
                        .key('A', Tags.Items.STRING)
                        .patternLine("AID")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "spell_bullet_loopcast"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.circleSpellBullet)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', Tags.Items.GLASS)
                        .key('A', Tags.Items.SLIMEBALLS)
                        .patternLine("AID")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "spell_bullet_circle"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.projectileSpellBullet)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', Tags.Items.GLASS)
                        .key('A', Tags.Items.ARROWS)
                        .patternLine("AID")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "spell_bullet_projectile"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.circleSpellBullet)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', Tags.Items.GLASS)
                        .key('A', Items.SNOWBALL)
                        .patternLine("AID")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "spell_bullet_circle"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.grenadeSpellBullet)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', Tags.Items.GLASS)
                        .key('A', Tags.Items.GUNPOWDER)
                        .patternLine("AID")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "spell_bullet_grenade"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.chargeSpellBullet)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', Tags.Items.GLASS)
                        .key('A', Tags.Items.DUSTS_REDSTONE)
                        .patternLine("AID")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "spell_bullet_charge"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.mineSpellBullet)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', Tags.Items.GLASS)
                        .key('A', ItemTags.BUTTONS)
                        .patternLine("AID")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "spell_bullet_mine"));

        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.chargeSpellBullet)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .key('D', Tags.Items.GLASS)
                        .key('A', Tags.Items.DUSTS_REDSTONE)
                        .patternLine("AID")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "spell_bullet_charge"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.spellDrive)
                        .key('I', ModTags.INGOT_PSI)
                        .key('R', Tags.Items.DUSTS_REDSTONE)
                        .patternLine("I")
                        .patternLine("R")
                        .patternLine("I")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "spell_drive"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalShovel)
                        .key('P', ModTags.INGOT_PSI)
                        .key('G', ModTags.GEM_PSI)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .patternLine("GP")
                        .patternLine(" I")
                        .patternLine(" I")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_shovel"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalPickaxe)
                        .key('P', ModTags.INGOT_PSI)
                        .key('G', ModTags.GEM_PSI)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .patternLine("PGP")
                        .patternLine(" I ")
                        .patternLine(" I ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_pickaxe"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalAxe)
                        .key('P', ModTags.INGOT_PSI)
                        .key('G', ModTags.GEM_PSI)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .patternLine("GP")
                        .patternLine("PI")
                        .patternLine(" I")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_axe"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalSword)
                        .key('P', ModTags.INGOT_PSI)
                        .key('G', ModTags.GEM_PSI)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .patternLine("P")
                        .patternLine("G")
                        .patternLine("I")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_sword"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalExosuitHelmet)
                        .key('P', ModTags.INGOT_PSI)
                        .key('G', ModTags.GEM_PSI)
                        .patternLine("GPG")
                        .patternLine("P P")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_exosuit_helmet"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalExosuitChestplate)
                        .key('P', ModTags.INGOT_PSI)
                        .key('G', ModTags.GEM_PSI)
                        .patternLine("P P")
                        .patternLine("GPG")
                        .patternLine("PPP")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_exosuit_chestplate"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalExosuitLeggings)
                        .key('P', ModTags.INGOT_PSI)
                        .key('G', ModTags.GEM_PSI)
                        .patternLine("GPG")
                        .patternLine("P P")
                        .patternLine("P P")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_exosuit_leggings"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.psimetalExosuitBoots)
                        .key('P', ModTags.INGOT_PSI)
                        .key('G', ModTags.GEM_PSI)
                        .patternLine("G G")
                        .patternLine("P P")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_exosuit_boots"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.detonator)
                        .key('P', ModTags.DUST_PSI)
                        .key('B', ItemTags.BUTTONS)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .patternLine(" B ")
                        .patternLine("IPI")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "detonator"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitController)
                        .key('R', Tags.Items.DUSTS_REDSTONE)
                        .key('G', Tags.Items.GLASS)
                        .key('I', ModTags.INGOT_PSI)
                        .patternLine("R")
                        .patternLine("G")
                        .patternLine("I")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "exosuit_controller"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.vectorRuler)
                        .key('D', ModTags.DUST_PSI)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .patternLine("D")
                        .patternLine("I")
                        .patternLine("I")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "vector_ruler"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitSensorLight)
                        .key('M', Tags.Items.DUSTS_GLOWSTONE)
                        .key('R', Tags.Items.INGOTS_IRON)
                        .key('I', ModTags.INGOT_PSI)
                        .patternLine(" I ")
                        .patternLine("IMR")
                        .patternLine(" R ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "exosuit_sensor_light"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitSensorWater)
                        .key('M', Tags.Items.GEMS_PRISMARINE)
                        .key('R', Tags.Items.INGOTS_IRON)
                        .key('I', ModTags.INGOT_PSI)
                        .patternLine(" I ")
                        .patternLine("IMR")
                        .patternLine(" R ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "exosuit_sensor_water"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitSensorHeat)
                        .key('M', Items.FIRE_CHARGE)
                        .key('R', Tags.Items.INGOTS_IRON)
                        .key('I', ModTags.INGOT_PSI)
                        .patternLine(" I ")
                        .patternLine("IMR")
                        .patternLine(" R ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "exosuit_sensor_heat"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModItems.exosuitSensorStress)
                        .key('M', Items.GLISTERING_MELON_SLICE)
                        .key('R', Tags.Items.INGOTS_IRON)
                        .key('I', ModTags.INGOT_PSI)
                        .patternLine(" I ")
                        .patternLine("IMR")
                        .patternLine(" R ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "exosuit_sensor_stress"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModBlocks.psidustBlock.asItem())
                        .key('O', ModItems.psidust)
                        .patternLine("OOO")
                        .patternLine("OOO")
                        .patternLine("OOO")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psidust_block"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModBlocks.psimetalBlock.asItem())
                        .key('O', ModItems.psimetal)
                        .patternLine("OOO")
                        .patternLine("OOO")
                        .patternLine("OOO")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_block"));

        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModBlocks.psigemBlock.asItem())
                        .key('O', ModItems.psigem)
                        .patternLine("OOO")
                        .patternLine("OOO")
                        .patternLine("OOO")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psigem_block"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModBlocks.psimetalEbony.asItem())
                        .key('O', ModItems.ebonyPsimetal)
                        .patternLine("OOO")
                        .patternLine("OOO")
                        .patternLine("OOO")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "ebony_block"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModBlocks.psimetalIvory.asItem())
                        .key('O', ModItems.ivoryPsimetal)
                        .patternLine("OOO")
                        .patternLine("OOO")
                        .patternLine("OOO")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "ivory_block"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psidustBlock.asItem())
                        .addIngredient(ModItems.psidust, 9)
                        ::build
                ).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psidust_block_shapeless"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psimetalBlock.asItem())
                        .addIngredient(ModItems.psimetal, 9)
                        ::build
                ).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_block_shapeless"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psigemBlock.asItem())
                        .addIngredient(ModItems.psigem, 9)
                        ::build
                ).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psigem_block_shapeless"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psimetalEbony.asItem())
                        .addIngredient(ModItems.ebonyPsimetal, 9)
                        ::build
                ).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "ebony_block_shapeless"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psimetalIvory.asItem())
                        .addIngredient(ModItems.ivoryPsimetal, 9)
                        ::build
                ).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "ivory_block_shapeless"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModBlocks.psimetalPlateBlack.asItem())
                        .key('O', Tags.Items.ORES_COAL)
                        .key('I', ModTags.INGOT_PSI)
                        .patternLine(" C ")
                        .patternLine("CIC")
                        .patternLine(" C ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_plate_black"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapedRecipeBuilder.shapedRecipe(ModBlocks.psimetalPlateWhite.asItem())
                        .key('O', Tags.Items.GEMS_QUARTZ)
                        .key('I', ModTags.INGOT_PSI)
                        .patternLine(" C ")
                        .patternLine("CIC")
                        .patternLine(" C ")
                        ::build).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_plate_white"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psimetalPlateBlackLight.asItem())
                        .addIngredient(Tags.Items.DUSTS_GLOWSTONE)
                        .addIngredient(ModBlocks.psimetalPlateBlack.asItem())
                        ::build
                ).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_plate_black_light"));
        ConditionalRecipe.builder()
                .addCondition(
                        MagicalPsiCondition.INSTANCE)
                .addRecipe(ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.psimetalPlateWhiteLight.asItem())
                        .addIngredient(Tags.Items.DUSTS_GLOWSTONE)
                        .addIngredient(ModBlocks.psimetalPlateWhite.asItem())
                        ::build
                ).build(consumer, new ResourceLocation(LibMisc.MOD_ID, "psimetal_plate_white_light"));

    }

    private static void specialRecipe(SpecialRecipeSerializer<?> serializer, Consumer<IFinishedRecipe> consumer) {
        CustomRecipeBuilder.func_218656_a(serializer).build(consumer, serializer.getRegistryName().toString());
    }

}

package vazkii.psi.data;

import net.minecraft.block.Blocks;
import net.minecraft.data.CustomRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ForgeRecipeProvider;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.crafting.recipe.AssemblyScavengeRecipe;
import vazkii.psi.common.crafting.recipe.BulletToDriveRecipe;
import vazkii.psi.common.crafting.recipe.ColorizerChangeRecipe;
import vazkii.psi.common.crafting.recipe.DriveDuplicateRecipe;
import vazkii.psi.common.crafting.recipe.SensorAttachRecipe;
import vazkii.psi.common.crafting.recipe.SensorRemoveRecipe;
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


        addOreDictRecipe(ProxyRegistry.newStack(ModItems.cadSocket, 1, 0),
                "DI", "I ",
                'I', "ingotIron",
                'D', "dustPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.cadSocket, 1, 1),
                "DI", "I ",
                'I', "ingotPsi",
                'D', "dustRedstone");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.cadSocket, 1, 2),
                "DI", "I ",
                'I', "ingotPsi",
                'D', "dustGlowstone");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.cadSocket, 1, 3),
                "DI", "IG",
                'I', "ingotPsi",
                'G', "gemPsi",
                'D', "dustRedstone");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.cadSocket, 1, 4),
                "DI", "IG",
                'I', "ingotPsi",
                'G', "gemPsi",
                'D', "dustGlowstone");

        addOreDictRecipe(ProxyRegistry.newStack(ModItems.cadBattery, 1, 0),
                "I", "D", "G",
                'I', "ingotIron",
                'G', "ingotGold",
                'D', "dustPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.cadBattery, 1, 1),
                "I", "D", "G",
                'I', "ingotIron",
                'G', "ingotGold",
                'D', "ingotPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.cadBattery, 1, 2),
                "I", "D", "G",
                'I', "ingotIron",
                'G', "ingotGold",
                'D', "gemPsi");

        String[] dyes = {
                "White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"
        };
        for (int i = 0; i < 16; i++)
            addOreDictRecipe(ProxyRegistry.newStack(ModItems.cadColorizer, 1, i),
                    " D ", "GCG", " I ",
                    'I', "ingotIron",
                    'G', "blockGlass",
                    'C', "dye" + dyes[i],
                    'D', "dustPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.cadColorizer, 1, 16),
                " D ", "GCG", " I ",
                'I', "ingotIron",
                'G', "blockGlass",
                'C', "crystalsPrismarine",
                'D', "dustPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.cadColorizer, 1, 17),
                " D ", "GDG", " I ",
                'I', "ingotIron",
                'G', "blockGlass",
                'D', "dustPsi");

        addOreDictRecipe(ProxyRegistry.newStack(ModItems.spellBullet, 1, 0),
                "ID",
                'I', "ingotIron",
                'D', "dustPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.spellBullet, 1, 2),
                "AID",
                'A', ProxyRegistry.newStack(Items.ARROW),
                'I', "ingotIron",
                'D', "dustPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.spellBullet, 1, 4),
                "AID",
                'A', ProxyRegistry.newStack(Items.STRING),
                'I', "ingotIron",
                'D', "dustPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.spellBullet, 1, 6),
                "AID",
                'A', "slimeball",
                'I', "ingotIron",
                'D', "dustPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.spellBullet, 1, 6),
                "AID",
                'A', ProxyRegistry.newStack(Items.SNOWBALL),
                'I', "ingotIron",
                'D', "dustPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.spellBullet, 1, 8),
                "AID",
                'A', ProxyRegistry.newStack(Items.GUNPOWDER),
                'I', "ingotIron",
                'D', "dustPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.spellBullet, 1, 10),
                "AID",
                'A', "dustRedstone",
                'I', "ingotIron",
                'D', "dustPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.spellBullet, 1, 12),
                "AID",
                'A', ProxyRegistry.newStack(Blocks.STONE_BUTTON),
                'I', "ingotIron",
                'D', "dustPsi");

        addOreDictRecipe(ProxyRegistry.newStack(ModItems.spellDrive),
                "I", "R", "I",
                'I', "ingotPsi",
                'R', "dustRedstone");

        addOreDictRecipe(ProxyRegistry.newStack(ModItems.psimetalShovel),
                "GP", " I", " I",
                'P', "ingotPsi",
                'G', "gemPsi",
                'I', "ingotIron");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.psimetalPickaxe),
                "PGP", " I ", " I ",
                'P', "ingotPsi",
                'G', "gemPsi",
                'I', "ingotIron");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.psimetalAxe),
                "GP", "PI", " I",
                'P', "ingotPsi",
                'G', "gemPsi",
                'I', "ingotIron");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.psimetalSword),
                "P", "G", "I",
                'P', "ingotPsi",
                'G', "gemPsi",
                'I', "ingotIron");

        addOreDictRecipe(ProxyRegistry.newStack(ModItems.psimetalExosuitHelmet),
                "GPG", "P P",
                'P', "ingotPsi",
                'G', "gemPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.psimetalExosuitChestplate),
                "P P", "GPG", "PPP",
                'P', "ingotPsi",
                'G', "gemPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.psimetalExosuitLeggings),
                "GPG", "P P", "P P",
                'P', "ingotPsi",
                'G', "gemPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.psimetalExosuitBoots),
                "G G", "P P",
                'P', "ingotPsi",
                'G', "gemPsi");

        addOreDictRecipe(ProxyRegistry.newStack(ModItems.detonator),
                " B ", "IPI",
                'P', "dustPsi",
                'B', ProxyRegistry.newStack(Blocks.STONE_BUTTON),
                'I', "ingotIron");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.exosuitController),
                "R", "G", "I",
                'R', "dustRedstone",
                'G', "blockGlassColorless",
                'I', "ingotPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.vectorRuler),
                "D", "I", "I",
                'D', "dustPsi",
                'I', "ingotIron");

        addOreDictRecipe(ProxyRegistry.newStack(ModItems.exosuitSensor, 1, 0),
                " I ", "IMR", " R ",
                'M', "dustGlowstone",
                'I', "ingotPsi",
                'R', "ingotIron");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.exosuitSensor, 1, 1),
                " I ", "IMR", " R ",
                'M', "shardPrismarine",
                'I', "ingotPsi",
                'R', "ingotIron");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.exosuitSensor, 1, 2),
                " I ", "IMR", " R ",
                'M', ProxyRegistry.newStack(Items.FIRE_CHARGE),
                'I', "ingotPsi",
                'R', "ingotIron");
        addOreDictRecipe(ProxyRegistry.newStack(ModItems.exosuitSensor, 1, 3),
                " I ", "IMR", " R ",
                'M', ProxyRegistry.newStack(Items.SPECKLED_MELON),
                'I', "ingotPsi",
                'R', "ingotIron");

        addOreDictRecipe(ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 0),
                "OOO", "OOO", "OOO",
                'O', "dustPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 1),
                "OOO", "OOO", "OOO",
                'O', "ingotPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 2),
                "OOO", "OOO", "OOO",
                'O', "gemPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 7),
                "OOO", "OOO", "OOO",
                'O', "ingotEbonyPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 8),
                "OOO", "OOO", "OOO",
                'O', "ingotIvoryPsi");
        addShapelessOreDictRecipe(ProxyRegistry.newStack(ModItems.material, 9, 0), ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 0));
        addShapelessOreDictRecipe(ProxyRegistry.newStack(ModItems.material, 9, 1), ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 1));
        addShapelessOreDictRecipe(ProxyRegistry.newStack(ModItems.material, 9, 2), ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 2));
        addShapelessOreDictRecipe(ProxyRegistry.newStack(ModItems.material, 9, 3), ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 7));
        addShapelessOreDictRecipe(ProxyRegistry.newStack(ModItems.material, 9, 4), ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 8));

        addOreDictRecipe(ProxyRegistry.newStack(ModBlocks.psiDecorative, 6, 3),
                " C ", "CIC", " C ",
                'C', "coal",
                'I', "ingotPsi");
        addOreDictRecipe(ProxyRegistry.newStack(ModBlocks.psiDecorative, 6, 5),
                " C ", "CIC", " C ",
                'C', "gemQuartz",
                'I', "ingotPsi");
        addShapelessOreDictRecipe(ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 4), ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 3), "dustGlowstone");
        addShapelessOreDictRecipe(ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 6), ProxyRegistry.newStack(ModBlocks.psiDecorative, 1, 5), "dustGlowstone");

    }

    private static void specialRecipe(SpecialRecipeSerializer<?> serializer, Consumer<IFinishedRecipe> consumer) {
        CustomRecipeBuilder.func_218656_a(serializer).build(consumer, serializer.getRegistryName().toString());
    }

    private static void addOreDictRecipe(ItemStack output, Object... recipe) {
        RecipeHandler.addOreDictRecipe(output, recipe);
    }

    private static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
        RecipeHandler.addShapelessOreDictRecipe(output, recipe);
    }
}

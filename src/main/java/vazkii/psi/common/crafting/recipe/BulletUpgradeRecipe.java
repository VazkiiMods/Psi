/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.item.ItemSpellBullet;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class BulletUpgradeRecipe extends ShapelessRecipe {
    public final String group;
    public final CraftingBookCategory category;
    public final ItemStack result;
    public final NonNullList<Ingredient> ingredients;

    public BulletUpgradeRecipe(String pGroup, CraftingBookCategory pCategory, ItemStack pResult, NonNullList<Ingredient> pIngredients) {
        super(pGroup, pCategory, pResult, pIngredients);
        this.group = pGroup;
        this.category = pCategory;
        this.result = pResult;
        this.ingredients = pIngredients;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput inv, HolderLookup.Provider access) {
        ItemStack output = super.assemble(inv, access);
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.getItem() instanceof ItemSpellBullet) {
                output = stack.transmuteCopy(output.getItem(), 1);
                break;
            }
        }
        return output;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return !DatagenModLoader.isRunningDataGen() ? RecipeType.CRAFTING : ModCraftingRecipes.BULLET_UPGRADE_TYPE.get();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return !DatagenModLoader.isRunningDataGen() ? RecipeSerializer.SHAPELESS_RECIPE : ModCraftingRecipes.BULLET_UPGRADE_SERIALIZER.get();
    }

    public static class Builder implements RecipeBuilder {
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
        @Nullable
        private String group;
        private final Item result;
        private final NonNullList<Ingredient> ingredients = NonNullList.create();

        public Builder(Item result) {
            this.result = result;
        }

        @Override
        public @NotNull RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
            this.criteria.put(name, criterion);
            return this;
        }

        @Override
        public @NotNull RecipeBuilder group(@Nullable String groupName) {
            this.group = groupName;
            return this;
        }

        @Override
        public @NotNull Item getResult() {
            return this.result;
        }

        public Builder requires(Ingredient ingredient) {
            this.ingredients.add(ingredient);
            return this;
        }

        public Builder requires(ItemLike item) {
            return this.requires(Ingredient.of(item));
        }

        private void ensureValid(ResourceLocation id) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }

            if (!(this.result instanceof ItemSpellBullet)) {
                throw new IllegalStateException("Recipe " + id + " should produce an item that extends ItemSpellBullet");
            }
        }

        @Override
        public void save(RecipeOutput recipeOutput, ResourceLocation id) {
            this.ensureValid(id);
            Advancement.Builder advancement$builder = recipeOutput.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                    .rewards(AdvancementRewards.Builder.recipe(id))
                    .requirements(AdvancementRequirements.Strategy.OR);
            this.criteria.forEach(advancement$builder::addCriterion);
            BulletUpgradeRecipe shapelessrecipe = new BulletUpgradeRecipe(
                    Objects.requireNonNullElse(this.group, ""),
                    CraftingBookCategory.EQUIPMENT,
                    this.result.getDefaultInstance(),
                    this.ingredients
            );
            recipeOutput.accept(id, shapelessrecipe, advancement$builder.build(id.withPrefix("recipes/bullet_upgrade/")));
        }
    }

    public static class Serializer implements RecipeSerializer<BulletUpgradeRecipe> {
        private static final MapCodec<BulletUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(r -> r.group),
                                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(r -> r.category),
                                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
                                Ingredient.CODEC_NONEMPTY
                                        .listOf()
                                        .fieldOf("ingredients")
                                        .flatXmap(
                                                p_301021_ -> {
                                                    Ingredient[] aingredient = p_301021_.toArray(Ingredient[]::new); // Neo skip the empty check and immediately create the array.
                                                    if (aingredient.length == 0) {
                                                        return DataResult.error(() -> "No ingredients for shapeless recipe");
                                                    } else {
                                                        return aingredient.length > ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth()
                                                                ? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxHeight()))
                                                                : DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
                                                    }
                                                },
                                                DataResult::success
                                        )
                                        .forGetter(r -> r.ingredients)
                        )
                        .apply(instance, BulletUpgradeRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, BulletUpgradeRecipe> STREAM_CODEC = StreamCodec.of(
                BulletUpgradeRecipe.Serializer::toNetwork, BulletUpgradeRecipe.Serializer::fromNetwork
        );

        @Override
        public @NotNull MapCodec<BulletUpgradeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BulletUpgradeRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static BulletUpgradeRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String s = buffer.readUtf();
            CraftingBookCategory craftingbookcategory = buffer.readEnum(CraftingBookCategory.class);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            nonnulllist.replaceAll(p_319735_ -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            return new BulletUpgradeRecipe(s, craftingbookcategory, itemstack, nonnulllist);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, BulletUpgradeRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeEnum(recipe.category);
            buffer.writeVarInt(recipe.ingredients.size());

            for (Ingredient ingredient : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }
    }
}

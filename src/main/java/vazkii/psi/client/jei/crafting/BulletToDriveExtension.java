/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.jei.crafting;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.common.crafting.recipe.BulletToDriveRecipe;
import vazkii.psi.common.item.ItemSpellBullet;
import vazkii.psi.common.item.ItemSpellDrive;
import vazkii.psi.common.item.base.ModItems;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BulletToDriveExtension implements ICraftingCategoryExtension<BulletToDriveRecipe> {
	private final List<List<ItemStack>> inputs;

	public BulletToDriveExtension() {
		inputs = ImmutableList.of(
				ImmutableList.of(new ItemStack(ModItems.spellDrive.get())),
				BuiltInRegistries.ITEM.stream()
						.filter(item -> item instanceof ItemSpellBullet)
						.map(ItemStack::new)
						.collect(Collectors.toList()));
	}

	@Override
	public void setRecipe(RecipeHolder<BulletToDriveRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper helper, IFocusGroup focuses) {
		ItemStack drive = new ItemStack(ModItems.spellDrive.get());

		focuses.getFocuses(VanillaTypes.ITEM_STACK)
				.filter(focus -> focus.getTypedValue().getIngredient().getItem() instanceof ItemSpellBullet)
				.findFirst()
				.map(focus -> focus.getTypedValue().getIngredient())
				.flatMap(stack -> ISpellAcceptor.hasSpell(stack) ? Optional.ofNullable(ISpellAcceptor.acceptor(stack).getSpell()) : Optional.empty())
				.ifPresent(spell -> ItemSpellDrive.setSpell(drive, spell));

		helper.createAndSetInputs(builder, inputs, 0, 0);
		helper.createAndSetOutputs(builder, ImmutableList.of(drive));
	}

	@Override
	public void drawInfo(RecipeHolder<BulletToDriveRecipe> recipeHolder, int recipeWidth, int recipeHeight, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("jei.psi.spell_copy").withStyle(ChatFormatting.GRAY), 57, 46); // no color sadge 0x808080
		//RenderSystem.enableAlphaTest(); // Prevents state leak affecting the shapeless icon
	}
}

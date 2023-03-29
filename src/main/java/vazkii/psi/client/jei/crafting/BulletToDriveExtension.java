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
import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.common.crafting.recipe.BulletToDriveRecipe;
import vazkii.psi.common.item.ItemSpellBullet;
import vazkii.psi.common.item.ItemSpellDrive;
import vazkii.psi.common.item.base.ModItems;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BulletToDriveExtension implements ICraftingCategoryExtension {
	private final List<List<ItemStack>> inputs;
	private final BulletToDriveRecipe recipe;

	public BulletToDriveExtension(BulletToDriveRecipe recipe) {
		this.recipe = recipe;

		inputs = ImmutableList.of(
				ImmutableList.of(new ItemStack(ModItems.spellDrive)),
				Registry.ITEM.stream()
						.filter(item -> item instanceof ItemSpellBullet)
						.map(ItemStack::new)
						.collect(Collectors.toList()));
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper helper, IFocusGroup focuses) {
		ItemStack drive = new ItemStack(ModItems.spellDrive);

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
	public void drawInfo(int recipeWidth, int recipeHeight, PoseStack matrixStack, double mouseX, double mouseY) {
		Minecraft.getInstance().font.draw(matrixStack, I18n.get("jei.psi.spell_copy"), 57, 46, 0x808080);
		//RenderSystem.enableAlphaTest(); // Prevents state leak affecting the shapeless icon
	}

	@Override
	public ResourceLocation getRegistryName() {
		return recipe.getId();
	}
}

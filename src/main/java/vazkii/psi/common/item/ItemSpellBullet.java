/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

public class ItemSpellBullet extends Item implements ISpellContainer {

	private static final String TAG_SPELL = "spell";

	public ItemSpellBullet(Item.Properties properties) {
		super(properties.maxStackSize(1));
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return getSpell(stack) != null;
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		return itemStack.copy();
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
		if (containsSpell(stack)) {
			CompoundNBT cmp = stack.getOrCreateTag().getCompound(TAG_SPELL);
			String name = cmp.getString(Spell.TAG_SPELL_NAME); // We don't need to load the whole spell just for the name
			if (name.isEmpty()) {
				return super.getDisplayName(stack);
			}
			return new StringTextComponent(name);
		}
		return super.getDisplayName(stack);
	}

	@Override
	public void setSpell(PlayerEntity player, ItemStack stack, Spell spell) {
		ItemSpellDrive.setSpell(stack, spell);
	}

	@Override
	public Spell getSpell(ItemStack stack) {
		return ItemSpellDrive.getSpell(stack);
	}

	@Override
	public boolean containsSpell(ItemStack stack) {
		return stack.getOrCreateTag().getBoolean(ItemSpellDrive.HAS_SPELL);
	}

	@Nonnull
	@Override
	@OnlyIn(Dist.CLIENT)
	public Rarity getRarity(ItemStack stack) {
		return containsSpell(stack) ? Rarity.RARE : Rarity.COMMON;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<ITextComponent> tooltip, ITooltipFlag advanced) {
		TooltipHelper.tooltipIfShift(tooltip, () -> {
			tooltip.add(new TranslationTextComponent("psimisc.bullet_type", new TranslationTextComponent("psi.bullet_type_" + getBulletType())));
			tooltip.add(new TranslationTextComponent("psimisc.bullet_cost", (int) (getCostModifier(stack) * 100)));
		});
	}

	public String getBulletType() {
		return "basic";
	}

	@Override
	public void castSpell(ItemStack stack, SpellContext context) {
		context.cspell.safeExecute(context);
	}

	@Override
	public double getCostModifier(ItemStack stack) {
		return 1.0;
	}

	@Override
	public boolean isCADOnlyContainer(ItemStack stack) {
		return false;
	}

	@Override
	public boolean requiresSneakForSpellSet(ItemStack stack) {
		return false;
	}

}

/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PsiSoundHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemSpellBullet extends Item {

	public static final String TAG_SPELL = "spell";

	public ItemSpellBullet(Item.Properties properties) {
		super(properties.stacksTo(16));
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new SpellAcceptor(stack);
	}

	@Nonnull
	@Override
	public Component getName(@Nonnull ItemStack stack) {
		if (ISpellAcceptor.hasSpell(stack)) {
			CompoundTag cmp = stack.getOrCreateTag().getCompound(TAG_SPELL);
			String name = cmp.getString(Spell.TAG_SPELL_NAME); // We don't need to load the whole spell just for the name
			if (name.isEmpty()) {
				return super.getName(stack);
			}
			return new TextComponent(name);
		}
		return super.getName(stack);
	}

	@Nonnull
	@Override
	@OnlyIn(Dist.CLIENT)
	public Rarity getRarity(ItemStack stack) {
		return ISpellAcceptor.hasSpell(stack) ? Rarity.RARE : Rarity.COMMON;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level playerIn, List<Component> tooltip, TooltipFlag advanced) {
		TooltipHelper.tooltipIfShift(tooltip, () -> {
			tooltip.add(new TranslatableComponent("psimisc.bullet_type", new TranslatableComponent("psi.bullet_type_" + getBulletType())));
			tooltip.add(new TranslatableComponent("psimisc.bullet_cost", (int) (ISpellAcceptor.acceptor(stack).getCostModifier() * 100)));
		});
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand hand) {
		ItemStack itemStackIn = playerIn.getItemInHand(hand);
		if (ItemSpellDrive.getSpell(itemStackIn) != null && playerIn.isShiftKeyDown()) {
			if (!worldIn.isClientSide) {
				worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), PsiSoundHandler.compileError, SoundSource.PLAYERS, 0.5F, 1F);
			} else {
				playerIn.swing(hand);
			}
			ItemSpellDrive.setSpell(itemStackIn, null);

			return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStackIn);
		}

		return new InteractionResultHolder<>(InteractionResult.PASS, itemStackIn);
	}

	public String getBulletType() {
		return "basic";
	}

	public ArrayList<Entity> castSpell(ItemStack stack, SpellContext context) {
		context.cspell.safeExecute(context);
		return new ArrayList<>();
	}

	public boolean loopcastSpell(ItemStack stack, SpellContext context) {
		castSpell(stack, context);
		return false;
	}

	public double getCostModifier(ItemStack stack) {
		return 1.0;
	}

	public boolean isCADOnlyContainer(ItemStack stack) {
		return false;
	}

	protected static class SpellAcceptor implements ICapabilityProvider, ISpellAcceptor {
		protected final ItemStack stack;
		private final LazyOptional<ISpellAcceptor> capOptional;

		protected SpellAcceptor(ItemStack stack) {
			this.stack = stack;
			this.capOptional = LazyOptional.of(() -> this);
		}

		private ItemSpellBullet bulletItem() {
			return ((ItemSpellBullet) stack.getItem());
		}

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
			return PsiAPI.SPELL_ACCEPTOR_CAPABILITY.orEmpty(cap, capOptional);
		}

		@Override
		public void setSpell(Player player, Spell spell) {
			if (stack.getCount() == 1) {
				ItemSpellDrive.setSpell(stack, spell);
				return;
			}
			stack.shrink(1);
			ItemStack newStack = stack.copy();
			newStack.setCount(1);
			ItemSpellDrive.setSpell(newStack, spell);
			if (!player.addItem(newStack)) {
				player.drop(newStack, false);
			}
		}

		@Override
		public Spell getSpell() {
			return ItemSpellDrive.getSpell(stack);
		}

		@Override
		public boolean containsSpell() {
			return stack.getOrCreateTag().getBoolean(ItemSpellDrive.HAS_SPELL);
		}

		@Override
		public ArrayList<Entity> castSpell(SpellContext context) {
			return bulletItem().castSpell(stack, context);
		}

		@Override
		public boolean loopcastSpell(SpellContext context) {
			return bulletItem().loopcastSpell(stack, context);
		}

		@Override
		public double getCostModifier() {
			return bulletItem().getCostModifier(stack);
		}

		@Override
		public boolean castableFromSocket() {
			return true;
		}

		@Override
		public boolean isCADOnlyContainer() {
			return bulletItem().isCADOnlyContainer(stack);
		}

		@Override
		public boolean requiresSneakForSpellSet() {
			return false;
		}
	}

}

/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.base.ModItems;

import java.util.ArrayList;
import java.util.List;

public class ItemSpellBullet extends Item {

	public ItemSpellBullet(Item.Properties properties) {
		super(properties.stacksTo(16).rarity(Rarity.COMMON));
	}

	public void verifyComponentsAfterLoad(ItemStack pStack) {
		if(pStack.has(DataComponents.CUSTOM_DATA)) {
			CustomData patch = pStack.get(DataComponents.CUSTOM_DATA);
			CompoundTag compound = patch.copyTag();

			if(compound.contains("has_spell")) {
				pStack.set(ModItems.HAS_SPELL, compound.getBoolean("has_spell"));
				pStack.set(DataComponents.RARITY, compound.getBoolean("has_spell") ? Rarity.RARE : Rarity.COMMON);
				compound.remove("has_spell");
			}
			if(compound.contains("spell")) {
				pStack.set(ModItems.TAG_SPELL, compound.getCompound("spell"));
				compound.remove("spell");
			}
			CustomData.set(DataComponents.CUSTOM_DATA, pStack, compound);
		}
	}

	@NotNull
	@Override
	public Component getName(@NotNull ItemStack stack) {
		if(ISpellAcceptor.hasSpell(stack)) {
			CompoundTag cmp = stack.getOrDefault(ModItems.TAG_SPELL, new CompoundTag());
			String name = cmp.getString(Spell.TAG_SPELL_NAME); // We don't need to load the whole spell just for the name
			if(name.isEmpty()) {
				return super.getName(stack);
			}
			return Component.literal(name);
		}
		return super.getName(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, TooltipFlag advanced) {
		TooltipHelper.tooltipIfShift(tooltip, () -> {
			tooltip.add(Component.translatable("psimisc.bullet_type", Component.translatable("psi.bullet_type_" + getBulletType())));
			tooltip.add(Component.translatable("psimisc.bullet_cost", (int) (ISpellAcceptor.acceptor(stack).getCostModifier() * 100)));
		});
	}

	@NotNull
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @NotNull InteractionHand hand) {
		ItemStack itemStackIn = playerIn.getItemInHand(hand);
		if(ItemSpellDrive.getSpell(itemStackIn) != null && playerIn.isShiftKeyDown()) {
			if(!worldIn.isClientSide) {
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

	public static class SpellAcceptor implements ICapabilityProvider<ItemCapability<?, Void>, Void, SpellAcceptor>, ISpellAcceptor {
		protected final ItemStack stack;

		public SpellAcceptor(ItemStack stack) {
			this.stack = stack;
		}

		private ItemSpellBullet bulletItem() {
			return ((ItemSpellBullet) stack.getItem());
		}

		@Override
		public SpellAcceptor getCapability(ItemCapability<?, Void> capability, Void facing) {
			return capability == PsiAPI.SPELL_ACCEPTOR_CAPABILITY ? this : null;
		}

		@Override
		public void setSpell(Player player, Spell spell) {
			if(stack.getCount() == 1) {
				ItemSpellDrive.setSpell(stack, spell);
				return;
			}
			stack.shrink(1);
			ItemStack newStack = stack.copy();
			newStack.setCount(1);
			ItemSpellDrive.setSpell(newStack, spell);
			if(!player.addItem(newStack)) {
				player.drop(newStack, false);
			}
		}

		@Override
		public Spell getSpell() {
			return ItemSpellDrive.getSpell(stack);
		}

		@Override
		public boolean containsSpell() {
			return stack.getOrDefault(ModItems.HAS_SPELL, false);
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

    }

}

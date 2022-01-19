/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PsiSoundHandler;

import javax.annotation.Nonnull;

public class ItemSpellDrive extends Item {

	private static final String TAG_SPELL = "spell";
	public static final String HAS_SPELL = "has_spell";

	public ItemSpellDrive(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Nonnull
	@Override
	public Component getName(ItemStack stack) {
		String name = super.getName(stack).getString();
		CompoundTag cmp = stack.getOrCreateTag().getCompound(TAG_SPELL);
		String spellName = cmp.getString(Spell.TAG_SPELL_NAME); // We don't need to load the whole spell just for the name
		if (spellName.isEmpty()) {
			return new TextComponent(name);
		}

		return new TextComponent(name + " (" + ChatFormatting.GREEN + spellName + ChatFormatting.RESET + ")");
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Player playerIn = ctx.getPlayer();
		InteractionHand hand = ctx.getHand();
		Level worldIn = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		ItemStack stack = playerIn.getItemInHand(hand);
		BlockEntity tile = worldIn.getBlockEntity(pos);
		if (tile instanceof TileProgrammer) {
			TileProgrammer programmer = (TileProgrammer) tile;
			Spell spell = getSpell(stack);
			if (spell == null && programmer.canCompile()) {
				setSpell(stack, programmer.spell);
				if (!worldIn.isClientSide) {
					worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, PsiSoundHandler.bulletCreate, SoundSource.PLAYERS, 0.5F, 1F);
				}
				return InteractionResult.SUCCESS;
			} else if (spell != null) {
				boolean enabled = programmer.isEnabled();
				if (enabled && !programmer.playerLock.isEmpty()) {
					if (!programmer.playerLock.equals(playerIn.getName().getString())) {
						if (!worldIn.isClientSide) {
							playerIn.sendMessage(new TranslatableComponent("psimisc.not_your_programmer").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), Util.NIL_UUID);
						}
						return InteractionResult.SUCCESS;
					}
				} else {
					programmer.playerLock = playerIn.getName().getString();
				}

				programmer.spell = spell;
				programmer.onSpellChanged();
				if (!worldIn.isClientSide) {
					worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, PsiSoundHandler.bulletCreate, SoundSource.PLAYERS, 0.5F, 1F);
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(programmer);
				}
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand hand) {
		ItemStack itemStackIn = playerIn.getItemInHand(hand);
		if (getSpell(itemStackIn) != null && playerIn.isShiftKeyDown()) {
			if (!worldIn.isClientSide) {
				worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), PsiSoundHandler.compileError, SoundSource.PLAYERS, 0.5F, 1F);
			} else {
				playerIn.swing(hand);
			}
			setSpell(itemStackIn, null);

			return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStackIn);
		}

		return new InteractionResultHolder<>(InteractionResult.PASS, itemStackIn);
	}

	public static void setSpell(ItemStack stack, Spell spell) {
		CompoundTag cmp = new CompoundTag();
		if (spell != null) {
			spell.writeToNBT(cmp);
			stack.getOrCreateTag().put(TAG_SPELL, cmp);
			stack.getOrCreateTag().putBoolean(HAS_SPELL, true);
		} else {
			stack.getOrCreateTag().remove(TAG_SPELL);
			stack.getOrCreateTag().remove(HAS_SPELL);
		}

	}

	public static Spell getSpell(ItemStack stack) {
		CompoundTag cmp = stack.getOrCreateTag().getCompound(TAG_SPELL);
		return Spell.createFromNBT(cmp);
	}

}

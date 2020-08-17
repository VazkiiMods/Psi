/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PsiSoundHandler;

import javax.annotation.Nonnull;

public class ItemSpellDrive extends Item {

	private static final String TAG_SPELL = "spell";
	public static final String HAS_SPELL = "has_spell";

	public ItemSpellDrive(Item.Properties properties) {
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
	public ITextComponent getDisplayName(ItemStack stack) {
		String name = super.getDisplayName(stack).getString();
		CompoundNBT cmp = stack.getOrCreateTag().getCompound(TAG_SPELL);
		String spellName = cmp.getString(Spell.TAG_SPELL_NAME); // We don't need to load the whole spell just for the name
		if (spellName.isEmpty()) {
			return new StringTextComponent(name);
		}

		return new StringTextComponent(name + " (" + TextFormatting.GREEN + spellName + TextFormatting.RESET + ")");
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		PlayerEntity playerIn = ctx.getPlayer();
		Hand hand = ctx.getHand();
		World worldIn = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		ItemStack stack = playerIn.getHeldItem(hand);
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileProgrammer) {
			TileProgrammer programmer = (TileProgrammer) tile;
			Spell spell = getSpell(stack);
			if (spell == null && programmer.canCompile()) {
				setSpell(stack, programmer.spell);
				if (!worldIn.isRemote) {
					worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, PsiSoundHandler.bulletCreate, SoundCategory.PLAYERS, 0.5F, 1F);
				}
				return ActionResultType.SUCCESS;
			} else if (spell != null) {
				boolean enabled = programmer.isEnabled();
				if (enabled && !programmer.playerLock.isEmpty()) {
					if (!programmer.playerLock.equals(playerIn.getName().getString())) {
						if (!worldIn.isRemote) {
							playerIn.sendMessage(new TranslationTextComponent("psimisc.not_your_programmer").setStyle(Style.EMPTY.withColor(TextFormatting.RED)), Util.NIL_UUID);
						}
						return ActionResultType.SUCCESS;
					}
				} else {
					programmer.playerLock = playerIn.getName().getString();
				}

				programmer.spell = spell;
				programmer.onSpellChanged();
				if (!worldIn.isRemote) {
					worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, PsiSoundHandler.bulletCreate, SoundCategory.PLAYERS, 0.5F, 1F);
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(programmer);
				}
				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		if (getSpell(itemStackIn) != null && playerIn.isSneaking()) {
			if (!worldIn.isRemote) {
				worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), PsiSoundHandler.compileError, SoundCategory.PLAYERS, 0.5F, 1F);
			} else {
				playerIn.swingArm(hand);
			}
			setSpell(itemStackIn, null);

			return new ActionResult<>(ActionResultType.SUCCESS, itemStackIn);
		}

		return new ActionResult<>(ActionResultType.PASS, itemStackIn);
	}

	public static void setSpell(ItemStack stack, Spell spell) {
		CompoundNBT cmp = new CompoundNBT();
		if (spell != null) {
			spell.writeToNBT(cmp);
		}

		stack.getOrCreateTag().put(TAG_SPELL, cmp);
		stack.getOrCreateTag().putBoolean(HAS_SPELL, true);
	}

	public static Spell getSpell(ItemStack stack) {
		CompoundNBT cmp = stack.getOrCreateTag().getCompound(TAG_SPELL);
		return Spell.createFromNBT(cmp);
	}

}

/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [31/01/2016, 18:06:16 (GMT)]
 */
package vazkii.psi.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import vazkii.arl.item.ItemMod;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.crafting.recipe.BulletToDriveRecipe;
import vazkii.psi.common.crafting.recipe.DriveDuplicateRecipe;
import vazkii.psi.common.item.base.IPsiItem;
import vazkii.psi.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemSpellDrive extends ItemMod implements IPsiItem {

	private static final String TAG_SPELL = "spell";

	public ItemSpellDrive() {
		super(LibItemNames.SPELL_DRIVE);
		setMaxStackSize(1);

		new DriveDuplicateRecipe();
		new BulletToDriveRecipe();
		setCreativeTab(PsiCreativeTab.INSTANCE);
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
	public String getItemStackDisplayName(@Nonnull ItemStack stack) {
		String name = super.getItemStackDisplayName(stack);
		CompoundNBT cmp = ItemNBTHelper.getCompound(stack, TAG_SPELL, false);
		String spellName = cmp.getString(Spell.TAG_SPELL_NAME); // We don't need to load the whole spell just for the name
		if(spellName.isEmpty())
			return name;

		return name + " (" + TextFormatting.GREEN + spellName + TextFormatting.RESET + ")";
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(PlayerEntity playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TileProgrammer) {
			TileProgrammer programmer = (TileProgrammer) tile;
			Spell spell = getSpell(stack);
			if(spell == null && programmer.canCompile()) {
				setSpell(stack, programmer.spell);
				if(!worldIn.isRemote)
					worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, PsiSoundHandler.bulletCreate, SoundCategory.PLAYERS, 0.5F, 1F);
				return EnumActionResult.SUCCESS;
			} else if(spell != null) {
				boolean enabled = programmer.isEnabled();
				if(enabled && !programmer.playerLock.isEmpty()) {
					if(!programmer.playerLock.equals(playerIn.getName())) {
						if(!worldIn.isRemote)
							playerIn.sendMessage(new TranslationTextComponent("psimisc.notYourProgrammer").setStyle(new Style().setColor(TextFormatting.RED)));
						return EnumActionResult.SUCCESS;
					}
				} else programmer.playerLock = playerIn.getName();

				programmer.spell = spell;
				programmer.onSpellChanged();
				if(!worldIn.isRemote) {
					worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, PsiSoundHandler.bulletCreate, SoundCategory.PLAYERS, 0.5F, 1F);
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(programmer);
				}
				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull EnumHand hand){
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		if(getSpell(itemStackIn) != null && playerIn.isSneaking()) {
			if(!worldIn.isRemote)
				worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, PsiSoundHandler.compileError, SoundCategory.PLAYERS, 0.5F, 1F);
			else playerIn.swingArm(hand);
			setSpell(itemStackIn, null);
			
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		}

		return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
	}

	public static void setSpell(ItemStack stack, Spell spell) {
		CompoundNBT cmp = new CompoundNBT();
		if(spell != null)
			spell.writeToNBT(cmp);
		ItemNBTHelper.setCompound(stack, TAG_SPELL, cmp);
	}

	public static Spell getSpell(ItemStack stack) {
		CompoundNBT cmp = ItemNBTHelper.getCompound(stack, TAG_SPELL, false);
		return Spell.createFromNBT(cmp);
	}

}

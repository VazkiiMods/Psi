/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [31/01/2016, 18:06:16 (GMT)]
 */
package vazkii.psi.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.helper.ItemNBTHelper;
import vazkii.psi.common.crafting.recipe.DriveDuplicateRecipe;
import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.lib.LibItemNames;

public class ItemSpellDrive extends ItemMod {

	private static final String TAG_SPELL = "spell";

	public ItemSpellDrive() {
		super(LibItemNames.SPELL_DRIVE);
		setMaxStackSize(1);
		
		GameRegistry.addRecipe(new DriveDuplicateRecipe());
		RecipeSorter.register("psi:driveDuplicate", DriveDuplicateRecipe.class, Category.SHAPELESS, "");
	}
	
	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return getSpell(stack) != null;
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		return itemStack.copy();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String name = super.getItemStackDisplayName(stack);
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_SPELL, false);
		String spellName = cmp.getString(Spell.TAG_SPELL_NAME); // We don't need to load the whole spell just for the name
		if(spellName == null || spellName.isEmpty())
			return name;
		
		return name + " (" + EnumChatFormatting.GREEN + spellName + EnumChatFormatting.RESET + ")";
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TileProgrammer) {
			TileProgrammer programmer = (TileProgrammer) tile;
			Spell spell = getSpell(stack);
			if(spell == null && programmer.canCompile()) {
				setSpell(stack, programmer.spell);
				if(!worldIn.isRemote)
					worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "psi:bulletCreate", 0.5F, 1F);
				return true;
			} else if(spell != null) {
				boolean enabled = programmer.isEnabled();
				if(enabled && !programmer.playerLock.isEmpty()) {
					if(!programmer.playerLock.equals(playerIn.getName())) {
						if(!worldIn.isRemote)
							playerIn.addChatComponentMessage(new ChatComponentTranslation("psimisc.notYourProgrammer").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
						return true;
					}
				} else programmer.playerLock = playerIn.getName();
				
				programmer.spell = spell;
				programmer.onSpellChanged();
				if(!worldIn.isRemote) {
					worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "psi:bulletCreate", 0.5F, 1F);
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(programmer);
				}
				return true;
			}
		}

		return false;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)  {
		if(getSpell(itemStackIn) != null && playerIn.isSneaking()) {
			if(!worldIn.isRemote)
				worldIn.playSoundAtEntity(playerIn, "psi:compileError", 0.5F, 1F);
			else playerIn.swingItem();
			setSpell(itemStackIn, null);
		}
		
		return itemStackIn;
	}

	public static void setSpell(ItemStack stack, Spell spell) {
		NBTTagCompound cmp = new NBTTagCompound();
		if(spell != null)
			spell.writeToNBT(cmp);
		ItemNBTHelper.setCompound(stack, TAG_SPELL, cmp);
	}

	public static Spell getSpell(ItemStack stack) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_SPELL, false);
		return Spell.createFromNBT(cmp);
	}

}

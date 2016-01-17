/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [13/01/2016, 16:48:52 (GMT)]
 */
package vazkii.psi.common.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.core.helper.ItemNBTHelper;
import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.lib.LibItemNames;

public class ItemSpellBullet extends ItemMod implements ISpellContainer {

	private static final String TAG_SPELL = "spell";
	
	public static final String[] VARIANTS = {
			"spellBullet",
			"spellBulletActive",
			"spellBulletProjectile",
			"spellBulletProjectileActive"
	};
	
	public ItemSpellBullet() {
		super(LibItemNames.SPELL_BULLET, VARIANTS);
		setMaxStackSize(1);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if(!containsSpell(stack))
			return super.getItemStackDisplayName(stack);
		
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_SPELL, false);
		String name = cmp.getString(Spell.TAG_SPELL_NAME); // We don't need to load the whole spell just for the name
		if(name == null || name.isEmpty())
			return super.getItemStackDisplayName(stack);
		
		return name;
	}
	
	@Override
	public void setSpell(ItemStack stack, Spell spell) {
		NBTTagCompound cmp = new NBTTagCompound();
		spell.writeToNBT(cmp);
		ItemNBTHelper.setCompound(stack, TAG_SPELL, cmp);
		
		if(!containsSpell(stack))
			stack.setItemDamage(stack.getItemDamage() + 1);
	}

	@Override
	public Spell getSpell(ItemStack stack) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_SPELL, false);
		return Spell.createFromNBT(cmp);
	}

	@Override
	public boolean containsSpell(ItemStack stack) {
		return stack.getItemDamage() % 2 == 1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}

}

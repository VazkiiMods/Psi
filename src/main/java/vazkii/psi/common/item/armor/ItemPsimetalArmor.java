/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [20/02/2016, 22:20:00 (GMT)]
 */
package vazkii.psi.common.item.armor;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.exosuit.IPsiEventArmor;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.client.model.ModelPsimetalExosuit;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.item.base.ItemModArmor;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.IPsimetalTool;
import vazkii.psi.common.item.tool.ItemPsimetalTool;
import vazkii.psi.common.lib.LibResources;

public abstract class ItemPsimetalArmor extends ItemModArmor implements IPsimetalTool, IPsiEventArmor {

	protected ModelBiped[] models = null;
	
	public ItemPsimetalArmor(String name, int type) {
		super(name, PsiAPI.PSIMETAL_ARMOR_MATERIAL, type);
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		ItemPsimetalTool.regen(itemStack, player, false);
	}
	
	public void cast(ItemStack stack, PsiArmorEvent event) {
		PlayerData data = PlayerDataHandler.get(event.entityPlayer);
		ItemStack playerCad = PsiAPI.getPlayerCAD(event.entityPlayer);

		if(playerCad != null) {
			ItemStack bullet = getBulletInSocket(stack, getSelectedSlot(stack));
			ItemCAD.cast(event.entityPlayer.worldObj, event.entityPlayer, data, bullet, playerCad, 5, 10, getCastVolume(), (SpellContext context) -> {
				context.tool = stack;
				context.attackingEntity = event.attacker;
				context.damageDealt = event.damage;
			});
		}
	}
	
	public float getCastVolume() {
		return 0.025F;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		String componentName = ItemMod.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
		ItemMod.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.material && par2ItemStack.getItemDamage() == 1 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}
	
	@Override
	public final String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		boolean overlay = type != null && type.equals("overlay");
		return overlay ? LibResources.MODEL_PSIMETAL_EXOSUIT : LibResources.MODEL_PSIMETAL_EXOSUIT_SENSOR;
	}
	
	@Override
	public boolean hasColor(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getColor(ItemStack stack) { // TODO
		return ICADColorizer.DEFAULT_SPELL_COLOR;
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		return super.getColorFromItemStack(stack, ~renderPass & 1); // do the switcheroo
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
		ModelBiped model = getArmorModelForSlot(entityLiving, itemStack, armorSlot - 1);
		if(model == null)
			model = provideArmorModelForSlot(itemStack, armorSlot - 1);

		if(model != null)
			return model;

		return super.getArmorModel(entityLiving, itemStack, armorSlot);
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModelForSlot(EntityLivingBase entity, ItemStack stack, int slot) {
		if(models == null)
			models = new ModelBiped[4];

		return models[slot];
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped provideArmorModelForSlot(ItemStack stack, int slot) {
		models[slot] = new ModelPsimetalExosuit(slot);
		return models[slot];
	}

}

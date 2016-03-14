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
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
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
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.IColorProvider;
import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.item.base.ItemModArmor;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.IPsimetalTool;
import vazkii.psi.common.item.tool.ItemPsimetalTool;
import vazkii.psi.common.lib.LibResources;

public class ItemPsimetalArmor extends ItemModArmor implements IPsimetalTool, IPsiEventArmor, IColorProvider {

	protected ModelBiped[] models = null;

	public ItemPsimetalArmor(String name, int type, EntityEquipmentSlot slot) {
		super(name, PsiAPI.PSIMETAL_ARMOR_MATERIAL, type, slot);
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
			ItemCAD.cast(event.entityPlayer.worldObj, event.entityPlayer, data, bullet, playerCad, getCastCooldown(stack), 0, getCastVolume(), (SpellContext context) -> {
				context.tool = stack;
				context.attackingEntity = event.attacker;
				context.damageTaken = event.damage;
			});
		}
	}

	@Override
	public void onEvent(ItemStack stack, PsiArmorEvent event) {
		if(event.type.equals(getEvent(stack)))
			cast(stack, event);
	}

	public String getEvent(ItemStack stack) {
		return PsiArmorEvent.NONE;
	}

	public int getCastCooldown(ItemStack stack) {
		return 5;
	}

	public float getCastVolume() {
		return 0.025F;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		ItemMod.tooltipIfShift(tooltip, () -> {
			String componentName = ItemMod.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
			ItemMod.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
			ItemMod.addToTooltip(tooltip, getEvent(stack));
		});
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.material && par2ItemStack.getItemDamage() == 1 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		boolean overlay = type != null && type.equals("overlay");
		return overlay ? LibResources.MODEL_PSIMETAL_EXOSUIT : LibResources.MODEL_PSIMETAL_EXOSUIT_SENSOR;
	}

	@Override
	public boolean hasColor(ItemStack stack) {
		return true;
	}

	@Override
	public int getColor(ItemStack stack) {
		return ICADColorizer.DEFAULT_SPELL_COLOR;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IItemColor getColor() {
		return (stack, renderIndex) -> renderIndex == 1 ? getColor(stack) : 0xFFFFFF;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		int slotIndex = armorSlot.ordinal();
		ModelBiped model = getArmorModelForSlot(entityLiving, itemStack, slotIndex);
		if(model == null)
			model = provideArmorModelForSlot(itemStack, slotIndex);

		return model;
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

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
import java.util.function.Function;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.item.ItemMod;
import vazkii.arl.item.ItemModArmor;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.exosuit.IPsiEventArmor;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.client.model.ModelPsimetalExosuit;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.IPsimetalTool;
import vazkii.psi.common.item.tool.ItemPsimetalTool;
import vazkii.psi.common.lib.LibResources;

public class ItemPsimetalArmor extends ItemModArmor implements IPsimetalTool, IPsiEventArmor, IItemColorProvider {

	@SideOnly(Side.CLIENT)
	public static Function<Integer, ModelBiped> modelSupplier;
	
	@SideOnly(Side.CLIENT)
	protected ModelBiped[] models;

	public ItemPsimetalArmor(String name, int type, EntityEquipmentSlot slot) {
		super(name, PsiAPI.PSIMETAL_ARMOR_MATERIAL, type, slot);
		setCreativeTab(PsiCreativeTab.INSTANCE);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		ItemPsimetalTool.regen(itemStack, player, false);
	}

	public void cast(ItemStack stack, PsiArmorEvent event) {
		PlayerData data = PlayerDataHandler.get(event.getEntityPlayer());
		ItemStack playerCad = PsiAPI.getPlayerCAD(event.getEntityPlayer());

		if(!playerCad.isEmpty()) {
			ItemStack bullet = getBulletInSocket(stack, getSelectedSlot(stack));
			ItemCAD.cast(event.getEntityPlayer().getEntityWorld(), event.getEntityPlayer(), data, bullet, playerCad, getCastCooldown(stack), 0, getCastVolume(), (SpellContext context) -> {
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
	public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
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
	public IItemColor getItemColor() {
		return new IItemColor() {
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				return tintIndex == 1 ? getColor(stack) : 0xFFFFFF;
			}
		};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		int slotIndex = armorSlot.ordinal();
		ModelBiped model = getArmorModelForSlot(entityLiving, itemStack, slotIndex);
	
		if(model == null || entityLiving.isSwingInProgress)
			model = provideArmorModelForSlot(itemStack, slotIndex);

		return model;
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModelForSlot(EntityLivingBase entity, ItemStack stack, int slot) {
		if(models == null)
			models = new ModelBiped[4];

		slot -= 2; // WHY YOU DO THIS MOJANG
		return models[slot];
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped provideArmorModelForSlot(ItemStack stack, int slot) {
		if(modelSupplier == null)
			modelSupplier = ModelPsimetalExosuit::new;
			
		slot -= 2; // JUST PLEASE STOP
		models[slot] = modelSupplier.apply(slot);
		return models[slot];
	}

	@Override
	public boolean requiresSneakForSpellSet(ItemStack stack) {
		return false;
	}
	
}

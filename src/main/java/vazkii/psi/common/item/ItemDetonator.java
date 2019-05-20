/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [19/02/2016, 18:08:41 (GMT)]
 */
package vazkii.psi.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import vazkii.arl.item.ItemMod;
import vazkii.psi.api.spell.detonator.DetonationEvent;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.item.base.IPsiItem;
import vazkii.psi.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static vazkii.psi.api.spell.SpellContext.MAX_DISTANCE;

public class ItemDetonator extends ItemMod implements IPsiItem {

	public ItemDetonator() {
		super(LibItemNames.DETONATOR);
		setMaxStackSize(1);
		setCreativeTab(PsiCreativeTab.INSTANCE);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		List<Entity> charges = worldIn.getEntitiesWithinAABB(Entity.class,
				playerIn.getEntityBoundingBox().grow(MAX_DISTANCE),
				entity -> {
					if (entity == null || !IDetonationHandler.canBeDetonated(entity))
						return false;
					else
						return entity.getDistanceSq(playerIn) <= MAX_DISTANCE * MAX_DISTANCE;
				});

		List<IDetonationHandler> handlers = charges.stream()
				.map(IDetonationHandler::detonator)
				.collect(Collectors.toList());

		if(!MinecraftForge.EVENT_BUS.post(new DetonationEvent(playerIn, MAX_DISTANCE, handlers))) {
			if (!handlers.isEmpty()) {
				for (IDetonationHandler handler : handlers)
					handler.detonate();
			}
		}

		if(!worldIn.isRemote)
			worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1F, 1F);
		else playerIn.swingArm(hand);

		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}

}

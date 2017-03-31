/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 20:16:28 (GMT)]
 */
package vazkii.psi.common.item.tool;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibItemNames;

public class ItemPsimetalAxe extends ItemPsimetalTool {

	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] {Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER});

	public ItemPsimetalAxe() {
		super(LibItemNames.PSIMETAL_AXE, 6F, -3.1F, EFFECTIVE_ON);
	}
	
	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase target, EntityLivingBase attacker) {
		super.hitEntity(itemstack, target, attacker);

		if(attacker instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) attacker;

			PlayerData data = PlayerDataHandler.get(player);
			ItemStack playerCad = PsiAPI.getPlayerCAD(player);

			if(playerCad != null) {
				ItemStack bullet = getBulletInSocket(itemstack, getSelectedSlot(itemstack));
				ItemCAD.cast(player.worldObj, player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
					context.attackedEntity = target;
				});
			}
		}

		return true;
	}

	// ItemAxe copypasta:

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		return state.getMaterial() != Material.WOOD && state.getMaterial() != Material.PLANTS && state.getMaterial() != Material.VINE ? super.getStrVsBlock(stack, state) : efficiencyOnProperMaterial;
	}

}

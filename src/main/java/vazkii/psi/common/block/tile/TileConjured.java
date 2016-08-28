/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [17/02/2016, 18:30:46 (GMT)]
 */
package vazkii.psi.common.block.tile;

import java.awt.Color;
import java.util.Arrays;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import vazkii.arl.block.tile.TileMod;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.base.ModBlocks;

public class TileConjured extends TileMod implements ITickable {

	private static final String TAG_TIME = "time";
	private static final String TAG_COLORIZER = "colorizer";

	public int time = -1;
	public ItemStack colorizer = null;

	@Override
	public void update() {
		if(worldObj.isRemote) {
			Color color = new Color(ICADColorizer.DEFAULT_SPELL_COLOR);
			if(colorizer != null)
				color = Psi.proxy.getColorizerColor(colorizer);

			float r = color.getRed() / 255F;
			float g = color.getGreen() / 255F;
			float b = color.getBlue() / 255F;

			IBlockState state = worldObj.getBlockState(getPos());
			state = state.getBlock().getActualState(state, worldObj, getPos());

			if(state.getBlock() == ModBlocks.conjured && state.getValue(BlockConjured.SOLID)) {
				// http://cns-alumni.bu.edu/~lavanya/Graphics/cs580/p5/web-page/cube_edges.gif
				boolean[] edges = new boolean[12];
				Arrays.fill(edges, true);

				if(state.getValue(BlockConjured.BLOCK_DOWN))
					removeEdges(edges, 0, 1, 2, 3);
				if(state.getValue(BlockConjured.BLOCK_UP))
					removeEdges(edges, 4, 5, 6, 7);
				if(state.getValue(BlockConjured.BLOCK_NORTH))
					removeEdges(edges, 3, 7, 8, 11);
				if(state.getValue(BlockConjured.BLOCK_SOUTH))
					removeEdges(edges, 1, 5, 9, 10);
				if(state.getValue(BlockConjured.BLOCK_EAST))
					removeEdges(edges, 2, 6, 10, 11);
				if(state.getValue(BlockConjured.BLOCK_WEST))
					removeEdges(edges, 0, 4, 8, 9);

				double x = getPos().getX();
				double y = getPos().getY();
				double z = getPos().getZ();

				// Bottom
				makeParticle(edges[0],  r, g, b, x + 0, y + 0, z + 0, 0, 0, 1);
				makeParticle(edges[1],  r, g, b, x + 0, y + 0, z + 1, 1, 0, 0);
				makeParticle(edges[2],  r, g, b, x + 1, y + 0, z + 0, 0, 0, 1);
				makeParticle(edges[3],  r, g, b, x + 0, y + 0, z + 0, 1, 0, 0);

				// Top
				makeParticle(edges[4],  r, g, b, x + 0, y + 1, z + 0, 0, 0, 1);
				makeParticle(edges[5],  r, g, b, x + 0, y + 1, z + 1, 1, 0, 0);
				makeParticle(edges[6],  r, g, b, x + 1, y + 1, z + 0, 0, 0, 1);
				makeParticle(edges[7],  r, g, b, x + 0, y + 1, z + 0, 1, 0, 0);

				// Sides
				makeParticle(edges[8],  r, g, b, x + 0, y + 0, z + 0, 0, 1, 0);
				makeParticle(edges[9],  r, g, b, x + 0, y + 0, z + 1, 0, 1, 0);
				makeParticle(edges[10], r, g, b, x + 1, y + 0, z + 1, 0, 1, 0);
				makeParticle(edges[11], r, g, b, x + 1, y + 0, z + 0, 0, 1, 0);

			} else if(Math.random() < 0.5) {
				float w = 0.15F;
				float h = 0.05F;
				double x = getPos().getX() + 0.5 + (Math.random() - 0.5) * w;
				double y = getPos().getY() + 0.25 + (Math.random() - 0.5) * h;
				double z = getPos().getZ() + 0.5 + (Math.random() - 0.5) * w;

				float s = 0.2F + (float) Math.random() * 0.1F;
				float m = 0.01F + (float) Math.random() * 0.015F;

				Psi.proxy.wispFX(worldObj, x, y, z, r, g, b, s, -m);
			}
		}

		if(time < 0)
			return;

		if(time == 0)
			worldObj.setBlockToAir(getPos());
		else time--;
	}

	public void makeParticle(boolean doit, float r, float g, float b, double xp, double yp, double zp, double xv, double yv, double zv) {
		if(doit && Math.random() < 0.3) {
			float m = 0.1F;
			xv *= m;
			yv *= m;
			zv *= m;

			double x = xp; //+ Math.random() * xv;
			double y = yp; //+ Math.random() * yv;
			double z = zp; //+ Math.random() * zv;
			Psi.proxy.sparkleFX(worldObj, x, y, z, r, g, b, (float) xv, (float) yv, (float) zv, 1.25F, 20);
		}
	}

	public void removeEdges(boolean[] edges, int... posArray) {
		for(int i : posArray)
			edges[i] = false;
	}

	@Override
	public void writeSharedNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_TIME, time);

		NBTTagCompound stackCmp = new NBTTagCompound();
		if(colorizer != null)
			colorizer.writeToNBT(stackCmp);
		cmp.setTag(TAG_COLORIZER, stackCmp);
	}

	@Override
	public void readSharedNBT(NBTTagCompound cmp) {
		time = cmp.getInteger(TAG_TIME);

		NBTTagCompound stackCmp = cmp.getCompoundTag(TAG_COLORIZER);
		colorizer = ItemStack.loadItemStackFromNBT(stackCmp);
	}

}

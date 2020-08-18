/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibMisc;

import java.util.Arrays;

public class TileConjured extends TileEntity {
	@ObjectHolder(LibMisc.PREFIX_MOD + LibBlockNames.CONJURED)
	public static TileEntityType<TileConjured> TYPE;

	private static final String TAG_COLORIZER = "colorizer";

	public ItemStack colorizer = ItemStack.EMPTY;

	public TileConjured() {
		super(TYPE);
	}

	public void doParticles() {
		int color = Psi.proxy.getColorForColorizer(colorizer);

		float r = PsiRenderHelper.r(color) / 255F;
		float g = PsiRenderHelper.g(color) / 255F;
		float b = PsiRenderHelper.b(color) / 255F;

		BlockState state = getWorld().getBlockState(getPos());

		if (state.getBlock() == ModBlocks.conjured && state.get(BlockConjured.SOLID)) {
			// http://cns-alumni.bu.edu/~lavanya/Graphics/cs580/p5/web-page/cube_edges.gif
			boolean[] edges = new boolean[12];
			Arrays.fill(edges, true);

			if (state.get(BlockConjured.BLOCK_DOWN)) {
				removeEdges(edges, 0, 1, 2, 3);
			}
			if (state.get(BlockConjured.BLOCK_UP)) {
				removeEdges(edges, 4, 5, 6, 7);
			}
			if (state.get(BlockConjured.BLOCK_NORTH)) {
				removeEdges(edges, 3, 7, 8, 11);
			}
			if (state.get(BlockConjured.BLOCK_SOUTH)) {
				removeEdges(edges, 1, 5, 9, 10);
			}
			if (state.get(BlockConjured.BLOCK_EAST)) {
				removeEdges(edges, 2, 6, 10, 11);
			}
			if (state.get(BlockConjured.BLOCK_WEST)) {
				removeEdges(edges, 0, 4, 8, 9);
			}

			double x = getPos().getX();
			double y = getPos().getY();
			double z = getPos().getZ();

			makeParticle(edges[0], r, g, b, x + 0, y + 0, z + 0, 0, 0, 1);
			makeParticle(edges[1], r, g, b, x + 0, y + 0, z + 1, 1, 0, 0);
			makeParticle(edges[2], r, g, b, x + 1, y + 0, z + 0, 0, 0, 1);
			makeParticle(edges[3], r, g, b, x + 0, y + 0, z + 0, 1, 0, 0);

			// Top
			makeParticle(edges[4], r, g, b, x + 0, y + 1, z + 0, 0, 0, 1);
			makeParticle(edges[5], r, g, b, x + 0, y + 1, z + 1, 1, 0, 0);
			makeParticle(edges[6], r, g, b, x + 1, y + 1, z + 0, 0, 0, 1);
			makeParticle(edges[7], r, g, b, x + 0, y + 1, z + 0, 1, 0, 0);

			// Sides
			makeParticle(edges[8], r, g, b, x + 0, y + 0, z + 0, 0, 1, 0);
			makeParticle(edges[9], r, g, b, x + 0, y + 0, z + 1, 0, 1, 0);
			makeParticle(edges[10], r, g, b, x + 1, y + 0, z + 1, 0, 1, 0);
			makeParticle(edges[11], r, g, b, x + 1, y + 0, z + 0, 0, 1, 0);

		} else if (Math.random() < 0.5) {
			float w = 0.15F;
			float h = 0.05F;
			double x = getPos().getX() + 0.5 + (Math.random() - 0.5) * w;
			double y = getPos().getY() + 0.25 + (Math.random() - 0.5) * h;
			double z = getPos().getZ() + 0.5 + (Math.random() - 0.5) * w;

			float s = 0.2F + (float) Math.random() * 0.1F;
			float m = 0.01F + (float) Math.random() * 0.015F;

			Psi.proxy.wispFX(x, y, z, r, g, b, s, -m);
		}
	}

	public void makeParticle(boolean doit, float r, float g, float b, double xp, double yp, double zp, double xv, double yv, double zv) {
		if (doit) {
			float m = 0.1F;
			xv *= m;
			yv *= m;
			zv *= m;

			Psi.proxy.sparkleFX(xp, yp, zp, r, g, b, (float) xv, (float) yv, (float) zv, 2.75f, 15);
		}
	}

	public void removeEdges(boolean[] edges, int... posArray) {
		for (int i : posArray) {
			edges[i] = false;
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT cmp) {
		cmp = super.write(cmp);
		if (!colorizer.isEmpty()) {
			cmp.put(TAG_COLORIZER, colorizer.write(new CompoundNBT()));
		}
		return cmp;
	}

	@Override
	public void fromTag(BlockState state, CompoundNBT cmp) {
		super.fromTag(state, cmp);
		this.readPacketNBT(cmp);
	}

	public void readPacketNBT(CompoundNBT cmp) {
		if (cmp.contains(TAG_COLORIZER)) {
			colorizer = ItemStack.read(cmp.getCompound(TAG_COLORIZER));
		} else {
			colorizer = ItemStack.EMPTY;
		}
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(getPos(), 0, write(new CompoundNBT()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.readPacketNBT(pkt.getNbtCompound());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

}

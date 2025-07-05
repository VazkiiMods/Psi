/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.base.ModBlocks;

import java.util.Arrays;

public class TileConjured extends BlockEntity {
	private static final String TAG_COLORIZER = "colorizer";
	public ItemStack colorizer = ItemStack.EMPTY;

	public TileConjured(BlockPos pos, BlockState state) {
		super(ModBlocks.conjuredType, pos, state);
	}

	public void doParticles() {
		int color = Psi.proxy.getColorForColorizer(colorizer);

		float r = PsiRenderHelper.r(color) / 255F;
		float g = PsiRenderHelper.g(color) / 255F;
		float b = PsiRenderHelper.b(color) / 255F;

		BlockState state = getLevel().getBlockState(getBlockPos());

		if(state.getBlock() == ModBlocks.conjured && state.getValue(BlockConjured.SOLID)) {
			// http://cns-alumni.bu.edu/~lavanya/Graphics/cs580/p5/web-page/cube_edges.gif
			boolean[] edges = new boolean[12];
			Arrays.fill(edges, true);

			if(state.getValue(BlockConjured.BLOCK_DOWN)) {
				removeEdges(edges, 0, 1, 2, 3);
			}
			if(state.getValue(BlockConjured.BLOCK_UP)) {
				removeEdges(edges, 4, 5, 6, 7);
			}
			if(state.getValue(BlockConjured.BLOCK_NORTH)) {
				removeEdges(edges, 3, 7, 8, 11);
			}
			if(state.getValue(BlockConjured.BLOCK_SOUTH)) {
				removeEdges(edges, 1, 5, 9, 10);
			}
			if(state.getValue(BlockConjured.BLOCK_EAST)) {
				removeEdges(edges, 2, 6, 10, 11);
			}
			if(state.getValue(BlockConjured.BLOCK_WEST)) {
				removeEdges(edges, 0, 4, 8, 9);
			}

			double x = getBlockPos().getX();
			double y = getBlockPos().getY();
			double z = getBlockPos().getZ();

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

		} else if(Math.random() < 0.5) {
			float w = 0.15F;
			float h = 0.05F;
			double x = getBlockPos().getX() + 0.5 + (Math.random() - 0.5) * w;
			double y = getBlockPos().getY() + 0.25 + (Math.random() - 0.5) * h;
			double z = getBlockPos().getZ() + 0.5 + (Math.random() - 0.5) * w;

			float s = 0.2F + (float) Math.random() * 0.1F;
			float m = 0.01F + (float) Math.random() * 0.015F;

			Psi.proxy.wispFX(x, y, z, r, g, b, s, -m);
		}
	}

	public void makeParticle(boolean doit, float r, float g, float b, double xp, double yp, double zp, double xv, double yv, double zv) {
		if(doit) {
			float m = 0.1F;
			xv *= m;
			yv *= m;
			zv *= m;

			Psi.proxy.sparkleFX(xp, yp, zp, r, g, b, (float) xv, (float) yv, (float) zv, 2.75f, 15);
		}
	}

	public void removeEdges(boolean[] edges, int... posArray) {
		for(int i : posArray) {
			edges[i] = false;
		}
	}

	@NotNull
	@Override
	public void saveAdditional(CompoundTag cmp, HolderLookup.Provider pRegistries) {
		super.saveAdditional(cmp, pRegistries);
		if(!colorizer.isEmpty()) {
			cmp.put(TAG_COLORIZER, colorizer.save(pRegistries, new CompoundTag()));
		}
	}

	@Override
	public void loadAdditional(CompoundTag cmp, HolderLookup.Provider pRegistries) {
		super.loadAdditional(cmp, pRegistries);
		this.readPacketNBT(cmp, pRegistries);
	}

	public void readPacketNBT(CompoundTag cmp, HolderLookup.Provider pRegistries) {
		if(cmp.contains(TAG_COLORIZER)) {
			colorizer = ItemStack.parseOptional(pRegistries, cmp.getCompound(TAG_COLORIZER));
		} else {
			colorizer = ItemStack.EMPTY;
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
		CompoundTag cmp = new CompoundTag();
		saveAdditional(cmp, pRegistries);
		return cmp;
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider pRegistries) {
		if(pkt != null && pkt.getTag() != null) {
			this.readPacketNBT(pkt.getTag(), pRegistries);
		}
	}

}

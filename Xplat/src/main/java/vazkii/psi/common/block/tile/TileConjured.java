/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.cad.CADComponentLookup;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.base.ModConjuredBlock;
import vazkii.psi.common.client.PsiClientRuntime;

public class TileConjured extends BlockEntity {
	private static final String TAG_COLORIZER = "colorizer";
	public ItemStack colorizer = ItemStack.EMPTY;

	public TileConjured(BlockPos pos, BlockState state) {
		super(ModConjuredBlock.TYPE.get(), pos, state);
	}

	public void doParticles() {
		if(getLevel() == null) {
			return;
		}

		int color = CADComponentLookup.color(getLevel().registryAccess(), colorizer);

		float r = PsiRenderHelper.r(color) / 255F;
		float g = PsiRenderHelper.g(color) / 255F;
		float b = PsiRenderHelper.b(color) / 255F;

		BlockState state = getLevel().getBlockState(getBlockPos());

		if(state.getBlock() == ModConjuredBlock.BLOCK.get() && state.getValue(BlockConjured.SOLID)) {
			return;
		} else if(Math.random() < 0.5) {
			float w = 0.15F;
			float h = 0.05F;
			double x = getBlockPos().getX() + 0.5 + (Math.random() - 0.5) * w;
			double y = getBlockPos().getY() + 0.25 + (Math.random() - 0.5) * h;
			double z = getBlockPos().getZ() + 0.5 + (Math.random() - 0.5) * w;

			float s = 0.2F + (float) Math.random() * 0.1F;
			float m = 0.01F + (float) Math.random() * 0.015F;

			PsiClientRuntime.wisp(getLevel(), x, y, z, r, g, b, s, 0, m, 0, 1);
		}
	}

	@Override
	public void saveAdditional(@NotNull CompoundTag cmp, HolderLookup.@NotNull Provider pRegistries) {
		super.saveAdditional(cmp, pRegistries);
		if(!colorizer.isEmpty()) {
			cmp.put(TAG_COLORIZER, colorizer.save(pRegistries, new CompoundTag()));
		}
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag cmp, HolderLookup.@NotNull Provider pRegistries) {
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
	public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
		CompoundTag cmp = new CompoundTag();
		saveAdditional(cmp, pRegistries);
		return cmp;
	}

}

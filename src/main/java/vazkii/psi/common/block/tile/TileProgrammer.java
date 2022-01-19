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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.spell.SpellCompiler;

import javax.annotation.Nonnull;

public class TileProgrammer extends TileEntity {
	@ObjectHolder(LibMisc.PREFIX_MOD + LibBlockNames.PROGRAMMER)
	public static TileEntityType<TileProgrammer> TYPE;

	private static final String TAG_SPELL = "spell";
	private static final String TAG_PLAYER_LOCK = "playerLock";

	public Spell spell;
	public boolean enabled;

	public String playerLock = "";

	public TileProgrammer() {
		super(TYPE);
	}

	public boolean isEnabled() {
		return spell != null && !spell.grid.isEmpty();
	}

	public boolean canCompile() {
		return isEnabled() && new SpellCompiler().compile(spell).left().isPresent();
	}

	public void onSpellChanged() {
		boolean wasEnabled = enabled;
		enabled = isEnabled();
		if (wasEnabled != enabled) {
			getLevel().setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockProgrammer.ENABLED, enabled));
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT cmp) {
		super.load(state, cmp);
		readPacketNBT(cmp);
	}

	@Nonnull
	@Override
	public CompoundNBT save(CompoundNBT cmp) {
		cmp = super.save(cmp);

		CompoundNBT spellCmp = new CompoundNBT();
		if (spell != null) {
			spell.writeToNBT(spellCmp);
		}
		cmp.put(TAG_SPELL, spellCmp);
		cmp.putString(TAG_PLAYER_LOCK, playerLock);
		return cmp;
	}

	public void readPacketNBT(CompoundNBT cmp) {
		CompoundNBT spellCmp = cmp.getCompound(TAG_SPELL);
		if (spell == null) {
			spell = Spell.createFromNBT(spellCmp);
		} else {
			spell.readFromNBT(spellCmp);
		}
		playerLock = cmp.getString(TAG_PLAYER_LOCK);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(getBlockPos(), 0, save(new CompoundNBT()));
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return save(new CompoundNBT());
	}

	public boolean canPlayerInteract(PlayerEntity player) {
		return player.isAlive() && player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.readPacketNBT(pkt.getTag());
	}
}

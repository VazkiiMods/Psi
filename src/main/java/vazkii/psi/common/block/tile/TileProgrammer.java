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
		return isEnabled() && !new SpellCompiler(spell).isErrored();
	}

	public void onSpellChanged() {
		boolean wasEnabled = enabled;
		enabled = isEnabled();
		if (wasEnabled != enabled) {
			getWorld().setBlockState(pos, getBlockState().with(BlockProgrammer.ENABLED, enabled));
		}
	}


	@Override
	public void fromTag(BlockState state, CompoundNBT cmp) {
		super.fromTag(state, cmp);
		readPacketNBT(cmp);
	}

	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT cmp) {
		cmp = super.write(cmp);

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
		return new SUpdateTileEntityPacket(getPos(), 0, write(new CompoundNBT()));
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	public boolean canPlayerInteract(PlayerEntity player) {
		return player.isAlive() && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.readPacketNBT(pkt.getNbtCompound());
	}
}

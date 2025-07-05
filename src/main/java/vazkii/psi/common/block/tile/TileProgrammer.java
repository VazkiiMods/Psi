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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.spell.SpellCompiler;

public class TileProgrammer extends BlockEntity {
	private static final String TAG_SPELL = "spell";
	private static final String TAG_PLAYER_LOCK = "playerLock";
	public Spell spell;
	public boolean enabled;

	public String playerLock = "";

	public TileProgrammer(BlockPos pos, BlockState state) {
		super(ModBlocks.programmerType, pos, state);
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
		if(wasEnabled != enabled) {
			getLevel().setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockProgrammer.ENABLED, enabled));
		}
		setChanged();
	}

	@Override
	public void loadAdditional(CompoundTag cmp, HolderLookup.Provider pRegistries) {
		super.loadAdditional(cmp, pRegistries);
		readPacketNBT(cmp);
	}

	@NotNull
	@Override
	public void saveAdditional(CompoundTag cmp, HolderLookup.Provider pRegistries) {
		super.saveAdditional(cmp, pRegistries);

		CompoundTag spellCmp = new CompoundTag();
		if(spell != null) {
			spell.writeToNBT(spellCmp);
		}
		cmp.put(TAG_SPELL, spellCmp);
		cmp.putString(TAG_PLAYER_LOCK, playerLock);
	}

	public void readPacketNBT(CompoundTag cmp) {
		CompoundTag spellCmp = cmp.getCompound(TAG_SPELL);
		if(spell == null) {
			spell = Spell.createFromNBT(spellCmp);
		} else {
			spell.readFromNBT(spellCmp);
		}
		playerLock = cmp.getString(TAG_PLAYER_LOCK);
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

	public boolean canPlayerInteract(Player player) {
		return player.isAlive() && player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider pRegistries) {
		this.readPacketNBT(pkt.getTag());
	}
}

/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.SpellProgrammer;
import vazkii.psi.common.block.base.ModProgrammerBlock;
import vazkii.psi.common.spell.SpellCompiler;

public class TileProgrammer extends BlockEntity implements SpellProgrammer {
	private static final String TAG_SPELL = "spell";
	private static final String TAG_PLAYER_LOCK = "playerLock";
	public Spell spell;
	public boolean enabled;

	public String playerLock = "";

	public TileProgrammer(BlockPos pos, BlockState state) {
		super(ModProgrammerBlock.TYPE.get(), pos, state);
	}

	public boolean isEnabled() {
		return spell != null && !spell.grid.isEmpty();
	}

	@Override
	public boolean canCompile(@Nullable Player player) {
		return isEnabled() && new SpellCompiler().compile(spell, getLevel().registryAccess(), player).left().isPresent();
	}

	@Override
	public Spell getSpellForDrive() {
		return spell;
	}

	@Override
	public boolean setSpellFromDrive(Player player, Spell newSpell) {
		if(isEnabled() && !playerLock.isEmpty() && !playerLock.equals(player.getName().getString())) {
			return false;
		}
		if(!isEnabled() || playerLock.isEmpty()) {
			playerLock = player.getName().getString();
		}
		spell = newSpell;
		onSpellChanged();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		return true;
	}

	public void onSpellChanged() {
		boolean wasEnabled = enabled;
		enabled = isEnabled();
		if(getLevel() == null) {
			return;
		}

		if(wasEnabled != enabled) {
			getLevel().setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockProgrammer.ENABLED, enabled));
		}
		setChanged();
	}

	@Override
	public void setSpellFromEditor(Player player, Spell newSpell) {
		if(playerLock == null || playerLock.isEmpty() || playerLock.equals(player.getName().getString())) {
			spell = newSpell;
			onSpellChanged();
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag cmp, HolderLookup.@NotNull Provider pRegistries) {
		super.loadAdditional(cmp, pRegistries);
		readPacketNBT(cmp);
	}

	@Override
	public void saveAdditional(@NotNull CompoundTag cmp, HolderLookup.@NotNull Provider pRegistries) {
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
	public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
		CompoundTag cmp = new CompoundTag();
		saveAdditional(cmp, pRegistries);
		return cmp;
	}

	public boolean canPlayerInteract(Player player) {
		return player.isAlive() && player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
	}

}

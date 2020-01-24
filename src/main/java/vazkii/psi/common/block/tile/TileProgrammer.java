/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 16:27:55 (GMT)]
 */
package vazkii.psi.common.block.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.arl.block.tile.TileMod;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.spell.SpellCompiler;

import javax.annotation.Nullable;

public class TileProgrammer extends TileMod implements INamedContainerProvider {
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
		if(wasEnabled != enabled) {
			getWorld().setBlockState(pos, getBlockState().with(BlockProgrammer.ENABLED, enabled));
		}
	}

	@Override
	public void writeSharedNBT(CompoundNBT cmp) {
		super.writeSharedNBT(cmp);

		CompoundNBT spellCmp = new CompoundNBT();
		if(spell != null)
			spell.writeToNBT(spellCmp);
		cmp.put(TAG_SPELL, spellCmp);
		cmp.putString(TAG_PLAYER_LOCK, playerLock);
	}

	@Override
	public void readSharedNBT(CompoundNBT cmp) {
		super.readSharedNBT(cmp);

		CompoundNBT spellCmp = cmp.getCompound(TAG_SPELL);
		if (spell == null)
			spell = Spell.createFromNBT(spellCmp);
		else spell.readFromNBT(spellCmp);
		playerLock = cmp.getString(TAG_PLAYER_LOCK);
	}

	public boolean canPlayerInteract(PlayerEntity player) {
		return player.isAlive() && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
	}


	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(ModBlocks.programmer.getTranslationKey());
	}

	@Nullable
	@Override
	public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		//TODO Take a look at this
		return null;
	}
}

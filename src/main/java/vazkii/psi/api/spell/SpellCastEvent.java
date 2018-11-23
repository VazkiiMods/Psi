package vazkii.psi.api.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;
import vazkii.psi.api.internal.IPlayerData;

/**
 * Posted after a spell successfully executes.
 *
 */
public class SpellCastEvent extends Event {

	public final Spell spell;
	public final SpellContext context;
	public final EntityPlayer player;
	public final IPlayerData playerData;
	public final ItemStack cad;
	public final ItemStack bullet;
	
	public SpellCastEvent(Spell spell, SpellContext context, EntityPlayer player, IPlayerData playerData, ItemStack cad, ItemStack bullet) {
		this.spell = spell;
		this.context = context;
		this.player = player;
		this.playerData = playerData;
		this.cad = cad;
		this.bullet = bullet;
	}
	
}

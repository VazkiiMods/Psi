/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 19:56:25 (GMT)]
 */
package vazkii.psi.api.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import vazkii.psi.common.spell.SpellCompiler;

public final class SpellContext {

	public EntityPlayer caster;
	public Entity focalPoint; 
	public CompiledSpell cspell;
	
	public SpellContext setPlayer(EntityPlayer player) {
		caster = player;
		return setFocalPoint(player);
	}
	
	public SpellContext setFocalPoint(Entity e) {
		focalPoint = e;
		return this;
	}
	
	public SpellContext setCompiledSpell(CompiledSpell spell) {
		cspell = spell;
		return this;
	}
	
	public SpellContext setSpell(Spell spell) {
		SpellCompiler compiler = new SpellCompiler(spell);
		
		if(!compiler.isErrored())
			setCompiledSpell(compiler.getCompiledSpell());
		
		return this;
	}

	public boolean isValid() {
		return cspell != null;
	}
	
}

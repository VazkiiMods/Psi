/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 15:17:40 (GMT)]
 */
package vazkii.psi.api.spell;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * A spell that has been compiled by a compiler and is ready to be executed.
 */
public class CompiledSpell {

	public Spell sourceSpell;
	public SpellMetadata metadata = new SpellMetadata();

	public Stack<Action> actions = new Stack();
	public Map<SpellPiece, Action> actionMap = new HashMap();
	
	public boolean[][] spotsEvaluated;
	public Object[][] evaluatedObjects;
	
	public CompiledSpell(Spell source) {
		sourceSpell = source;
		metadata.setStat(EnumSpellStat.BANDWIDTH, source.grid.getSize());
		
		spotsEvaluated = new boolean[SpellGrid.GRID_SIZE][SpellGrid.GRID_SIZE];
		evaluatedObjects = new Object[SpellGrid.GRID_SIZE][SpellGrid.GRID_SIZE];
	}

	/**
	 * Executes the spell, making a copy of the {@link #actions} stack so it
	 * can be reused if cached.
	 */
	public void execute(SpellContext context) throws SpellRuntimeException {
		Stack<Action> actions = (Stack<Action>) this.actions.clone();
		
		while(!actions.isEmpty())
			actions.pop().execute(context);
		
		evaluatedObjects = new Object[SpellGrid.GRID_SIZE][SpellGrid.GRID_SIZE];
	}
	
	public boolean hasEvaluated(int x, int y) {
		if(!SpellGrid.exists(x, y))
			return false;
		
		return spotsEvaluated[x][y];
	}
	
	
	public class Action {
		
		final SpellPiece piece;
		
		public Action(SpellPiece piece) {
			this.piece = piece;
		}
		
		public void execute(SpellContext context) throws SpellRuntimeException {
			Object o = piece.execute(context);
			
			if(piece.getEvaluationType() != null)
				evaluatedObjects[piece.x][piece.y] = o;
		}
		
	}
	
}

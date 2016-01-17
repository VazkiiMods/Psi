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

import java.util.Stack;

public class CompiledSpell {

	public Spell sourceSpell;
	public SpellMetadata metadata = new SpellMetadata();

	public Stack<Action> actions = new Stack();
	
	public boolean[][] spotsEvaluated;
	public Object[][] evaluatedObjects;
	
	public CompiledSpell(Spell source) {
		sourceSpell = source;
		
		spotsEvaluated = new boolean[SpellGrid.GRID_SIZE][SpellGrid.GRID_SIZE];
		evaluatedObjects = new Object[SpellGrid.GRID_SIZE][SpellGrid.GRID_SIZE];
	}

	public boolean hasEvaluated(int x, int y) {
		if(!SpellGrid.exists(x, y))
			return false;
		
		return spotsEvaluated[x][y];
	}
	
	public void execute(SpellContext context) {
		while(!actions.isEmpty())
			actions.pop().execute(context);
	}
	
	public class Action {
		
		final SpellPiece piece;
		
		public Action(SpellPiece piece) {
			this.piece = piece;
		}
		
		public void execute(SpellContext context) {
			Object o = piece.execute(context); 
			
			if(piece.getEvaluationType() != null)
				evaluatedObjects[piece.x][piece.y] = o;
		}
		
	}
	
}

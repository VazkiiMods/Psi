/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.util.Util;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.param.ParamError;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageSpellError;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * A spell that has been compiled by a compiler and is ready to be executed.
 */
public class CompiledSpell {

	public final Spell sourceSpell;
	public final SpellMetadata metadata = new SpellMetadata();

	public final Stack<Action> actions = new Stack<>();
	public final Map<SpellPiece, Action> actionMap = new HashMap<>();

	public final boolean[][] spotsEvaluated;

	public CompiledSpell(Spell source) {
		sourceSpell = source;
		metadata.setStat(EnumSpellStat.BANDWIDTH, source.grid.getSize());

		spotsEvaluated = new boolean[SpellGrid.GRID_SIZE][SpellGrid.GRID_SIZE];
	}

	/**
	 * Executes the spell, making a copy of the {@link #actions} stack so it can
	 * be reused if cached.
	 */
	public boolean execute(SpellContext context) throws SpellRuntimeException {
		IPlayerData data = PsiAPI.internalHandler.getDataForPlayer(context.caster);
		while (!context.actions.isEmpty()) {
			Action a = context.actions.pop();

			PsiAPI.internalHandler.setCrashData(this, a.piece);
			a.execute(data, context);
			PsiAPI.internalHandler.setCrashData(null, null);

			if (context.stopped) {
				return false;
			}

			if (context.delay > 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @see #execute
	 */
	@SuppressWarnings("unchecked")
	public void safeExecute(SpellContext context) {
		if (context.caster.getEntityWorld().isRemote) {
			return;
		}

		try {
			if (context.actions == null) {
				context.actions = (Stack<Action>) actions.clone();
			}

			if (context.cspell.execute(context)) {
				PsiAPI.internalHandler.delayContext(context);
			}
		} catch (SpellRuntimeException e) {
			if (!context.shouldSuppressErrors()) {
				context.caster.sendMessage(new TranslationTextComponent(e.getMessage()).setStyle(Style.EMPTY.setFormatting(TextFormatting.RED)), Util.DUMMY_UUID);

				MessageSpellError message = new MessageSpellError("psi.spellerror.position", e.x + 1, e.y + 1);
				MessageRegister.sendToPlayer(message, context.caster);
			}
		}
	}

	public boolean hasEvaluated(int x, int y) {
		if (!SpellGrid.exists(x, y)) {
			return false;
		}

		return spotsEvaluated[x][y];
	}

	public class Action {

		public final SpellPiece piece;

		public Action(SpellPiece piece) {
			this.piece = piece;
		}

		public void execute(IPlayerData data, SpellContext context) throws SpellRuntimeException {
			for (SpellParam<?> param : piece.paramSides.keySet()) {
				if (!(param instanceof ParamError)) {
					Object v = piece.getRawParamValue(context, param);
					if (v instanceof SpellRuntimeException) {
						throw (SpellRuntimeException) v;
					}
				}
			}
			try {
				data.markPieceExecuted(piece);
				Object o = piece.execute(context);

				Class<?> eval = piece.getEvaluationType();
				if (eval != null && eval != Void.class) {
					context.evaluatedObjects[piece.x][piece.y] = o;
				}
			} catch (SpellRuntimeException exception) {
				exception.x = piece.x;
				exception.y = piece.y;
				if (metadata.errorSuppressed[piece.x][piece.y]) {
					context.evaluatedObjects[piece.x][piece.y] = exception;
				} else {
					throw exception;
				}
			}
		}

	}

}

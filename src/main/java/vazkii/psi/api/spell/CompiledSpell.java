/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.IPlayerData;
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
	public final Map<SpellPiece, CatchHandler> errorHandlers = new HashMap<>();
	public final Map<SpellPiece, Action> actionMap = new HashMap<>();
	public final boolean[][] spotsEvaluated;
	public Action currentAction;

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
		while(!context.actions.isEmpty()) {
			Action a = context.actions.pop();
			currentAction = a;

			PsiAPI.internalHandler.setCrashData(this, a.piece);
			a.execute(data, context);
			PsiAPI.internalHandler.setCrashData(null, null);

			currentAction = null;

			if(context.stopped) {
				return false;
			}

			if(context.delay > 0) {
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
		if(context.caster.getCommandSenderWorld().isClientSide) {
			return;
		}

		try {
			if(context.actions == null) {
				context.actions = (Stack<Action>) actions.clone();
			}

			if(context.cspell.execute(context)) {
				PsiAPI.internalHandler.delayContext(context);
			}
		} catch (SpellRuntimeException e) {
			if(!context.shouldSuppressErrors()) {
				context.caster.sendSystemMessage(e.toComponent().setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));

				int x = context.cspell.currentAction.piece.x + 1;
				int y = context.cspell.currentAction.piece.y + 1;
				MessageSpellError message = new MessageSpellError("psi.spellerror.position", x, y);
				MessageRegister.sendToPlayer((ServerPlayer) context.caster, message);
			}
		}
	}

	public static class CatchHandler {

		public final SpellPiece handlerPiece;
		public final IErrorCatcher handler;

		public CatchHandler(SpellPiece handlerPiece) {
			this.handlerPiece = handlerPiece;
			this.handler = (IErrorCatcher) handlerPiece;
		}

		public boolean suppress(SpellPiece piece, SpellContext context, SpellRuntimeException exception) {
			boolean handled = handler.catchException(piece, context, exception);
			if(handled) {
				Class<?> eval = piece.getEvaluationType();
				if(eval != null && eval != Void.class) {
					context.evaluatedObjects[piece.x][piece.y] =
							handler.supplyReplacementValue(piece, context, exception);
				}
			}

			return handled;
		}
	}

	public class Action {

		public final SpellPiece piece;

		public Action(SpellPiece piece) {
			this.piece = piece;
		}

		public void execute(IPlayerData data, SpellContext context) throws SpellRuntimeException {
			try {
				data.markPieceExecuted(piece);
				Object o = piece.execute(context);

				Class<?> eval = piece.getEvaluationType();
				if(eval != null && eval != Void.class) {
					context.evaluatedObjects[piece.x][piece.y] = o;
				}
			} catch (SpellRuntimeException exception) {
				if(errorHandlers.containsKey(piece)) {
					if(!errorHandlers.get(piece).suppress(piece, context, exception)) {
						throw exception;
					}
					return;
				}
				throw exception;
			}
		}

	}

}

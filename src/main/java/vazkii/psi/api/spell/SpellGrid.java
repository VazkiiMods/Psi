/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

/**
 * Holder class for a spell's piece grid. Pretty much all internal, nothing to see here.
 */
public final class SpellGrid {

	private static final String TAG_SPELL_LIST = "spellList";

	private static final String TAG_SPELL_POS_X_LEGACY = "spellPosX";
	private static final String TAG_SPELL_POS_Y_LEGACY = "spellPosY";
	private static final String TAG_SPELL_DATA_LEGACY = "spellData";

	private static final String TAG_SPELL_POS_X = "x";
	private static final String TAG_SPELL_POS_Y = "y";
	private static final String TAG_SPELL_DATA = "data";

	public static final int GRID_SIZE = 9;
	public static final int GRID_CENTER = (GRID_SIZE - 1) / 2;

	public final Spell spell;
	public SpellPiece[][] gridData;

	private boolean empty;
	private int leftmost, rightmost, topmost, bottommost;

	@OnlyIn(Dist.CLIENT)
	public void draw(MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				SpellPiece p = gridData[i][j];
				if (p != null) {
					ms.push();
					ms.translate(i * 18, j * 18, 0);
					p.draw(ms, buffers, light);
					ms.pop();
				}
			}
		}
	}

	private void recalculateBoundaries() {
		empty = true;
		leftmost = GRID_SIZE;
		rightmost = -1;
		topmost = GRID_SIZE;
		bottommost = -1;

		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				SpellPiece p = gridData[i][j];
				if (p != null) {
					empty = false;
					if (i < leftmost) {
						leftmost = i;
					}
					if (i > rightmost) {
						rightmost = i;
					}
					if (j < topmost) {
						topmost = j;
					}
					if (j > bottommost) {
						bottommost = j;
					}
				}
			}
		}
	}

	public int getSize() {
		recalculateBoundaries();

		if (empty) {
			return 0;
		}

		return Math.max(rightmost - leftmost + 1, bottommost - topmost + 1);
	}

	public void mirrorVertical() {
		recalculateBoundaries();
		if (empty) {
			return;
		}

		SpellPiece[][] newGrid = new SpellPiece[GRID_SIZE][GRID_SIZE];

		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				SpellPiece p = gridData[i][j];

				if (p != null) {
					int newY = GRID_SIZE - j - 1;

					newGrid[i][newY] = p;
					p.y = newY;

					p.paramSides.replaceAll((k, v) -> p.paramSides.get(k).mirrorVertical());
				}
			}
		}

		gridData = newGrid;
	}

	public void rotate(boolean ccw) {
		recalculateBoundaries();
		if (empty) {
			return;
		}

		int xMod = ccw ? -1 : 1;
		int yMod = ccw ? 1 : -1;

		SpellPiece[][] newGrid = new SpellPiece[GRID_SIZE][GRID_SIZE];

		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				SpellPiece p = gridData[i][j];

				if (p != null) {
					int newX = xMod * (j - GRID_CENTER) + GRID_CENTER;
					int newY = yMod * (i - GRID_CENTER) + GRID_CENTER;

					newGrid[newX][newY] = p;
					p.x = newX;
					p.y = newY;

					for (SpellParam<?> param : p.paramSides.keySet()) {
						SpellParam.Side side = p.paramSides.get(param);
						p.paramSides.put(param, ccw ? side.rotateCCW() : side.rotateCW());
					}
				}
			}
		}

		gridData = newGrid;
	}

	public boolean shift(SpellParam.Side side, boolean doit) {
		recalculateBoundaries();

		if (empty) {
			return false;
		}

		if (exists(leftmost + side.offx, topmost + side.offy) && exists(rightmost + side.offx, bottommost + side.offy)) {
			if (!doit) {
				return true;
			}

			SpellPiece[][] newGrid = new SpellPiece[GRID_SIZE][GRID_SIZE];

			for (int i = 0; i < GRID_SIZE; i++) {
				for (int j = 0; j < GRID_SIZE; j++) {
					SpellPiece p = gridData[i][j];

					if (p != null) {
						int newX = i + side.offx;
						int newY = j + side.offy;
						newGrid[newX][newY] = p;
						p.x = newX;
						p.y = newY;
					}
				}
			}

			gridData = newGrid;
			return true;
		}
		return false;
	}

	public static boolean exists(int x, int y) {
		return x >= 0 && y >= 0 && x < GRID_SIZE && y < GRID_SIZE;
	}

	private SpellPiece getPieceAtSide(Multimap<SpellPiece, SpellParam.Side> traversed, int x, int y, SpellParam.Side side) throws SpellCompilationException {
		SpellPiece atSide = getPieceAtSideSafely(x, y, side);
		if (traversed.containsEntry(atSide, side)) {
			throw new SpellCompilationException(SpellCompilationException.INFINITE_LOOP);
		}

		traversed.put(atSide, side);
		return atSide;
	}

	@Deprecated
	@SuppressWarnings("unused")
	public SpellPiece getPieceAtSideWithRedirections(List<SpellPiece> unused, int x, int y, SpellParam.Side side) throws SpellCompilationException {
		return getPieceAtSideWithRedirections(x, y, side);
	}

	public SpellPiece getPieceAtSideWithRedirections(int x, int y, SpellParam.Side side) throws SpellCompilationException {
		return getPieceAtSideWithRedirections(HashMultimap.create(), x, y, side);
	}

	public SpellPiece getPieceAtSideWithRedirections(Multimap<SpellPiece, SpellParam.Side> traversed, int x, int y, SpellParam.Side side) throws SpellCompilationException {
		SpellPiece atSide = getPieceAtSide(traversed, x, y, side);
		if (!(atSide instanceof IGenericRedirector)) {
			return atSide;
		}

		IGenericRedirector redirector = (IGenericRedirector) atSide;
		SpellParam.Side rside = redirector.remapSide(side);
		if (!rside.isEnabled()) {
			return null;
		}

		return getPieceAtSideWithRedirections(traversed, atSide.x, atSide.y, rside);
	}

	public SpellPiece getPieceAtSideWithRedirections(int x, int y, SpellParam.Side side, ISpellCompiler compiler) throws SpellCompilationException {
		return getPieceAtSideWithRedirections(HashMultimap.create(), x, y, side, compiler);
	}

	public SpellPiece getPieceAtSideWithRedirections(Multimap<SpellPiece, SpellParam.Side> traversed, int x, int y, SpellParam.Side side, ISpellCompiler compiler) throws SpellCompilationException {
		SpellPiece atSide = getPieceAtSide(traversed, x, y, side);
		if (!(atSide instanceof IGenericRedirector)) {
			return atSide;
		}

		IGenericRedirector redirector = (IGenericRedirector) atSide;
		compiler.buildRedirect(atSide);
		SpellParam.Side rside = redirector.remapSide(side);
		if (!rside.isEnabled()) {
			return null;
		}

		return getPieceAtSideWithRedirections(traversed, atSide.x, atSide.y, rside, compiler);
	}

	public SpellPiece getPieceAtSideSafely(int x, int y, SpellParam.Side side) {
		int xp = x + side.offx;
		int yp = y + side.offy;
		if (!exists(xp, yp)) {
			return null;
		}

		return gridData[xp][yp];
	}

	public SpellGrid(Spell spell) {
		this.spell = spell;
		gridData = new SpellPiece[GRID_SIZE][GRID_SIZE];
	}

	public boolean isEmpty() {
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				SpellPiece piece = gridData[i][j];
				if (piece != null) {
					return false;
				}
			}
		}

		return true;
	}

	public void readFromNBT(CompoundNBT cmp) {
		gridData = new SpellPiece[GRID_SIZE][GRID_SIZE];

		ListNBT list = cmp.getList(TAG_SPELL_LIST, 10);
		int len = list.size();
		for (int i = 0; i < len; i++) {
			CompoundNBT lcmp = list.getCompound(i);
			int posX, posY;

			if (lcmp.contains(TAG_SPELL_POS_X_LEGACY)) {
				posX = lcmp.getInt(TAG_SPELL_POS_X_LEGACY);
				posY = lcmp.getInt(TAG_SPELL_POS_Y_LEGACY);
			} else {
				posX = lcmp.getInt(TAG_SPELL_POS_X);
				posY = lcmp.getInt(TAG_SPELL_POS_Y);
			}

			CompoundNBT data;
			if (lcmp.contains(TAG_SPELL_DATA_LEGACY)) {
				data = lcmp.getCompound(TAG_SPELL_DATA_LEGACY);
			} else {
				data = lcmp.getCompound(TAG_SPELL_DATA);
			}

			SpellPiece piece = SpellPiece.createFromNBT(spell, data);
			if (piece != null) {
				gridData[posX][posY] = piece;
				piece.isInGrid = true;
				piece.x = posX;
				piece.y = posY;
			}
		}
	}

	public void writeToNBT(CompoundNBT cmp) {
		ListNBT list = new ListNBT();
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				SpellPiece piece = gridData[i][j];
				if (piece != null) {
					CompoundNBT lcmp = new CompoundNBT();
					lcmp.putInt(TAG_SPELL_POS_X, i);
					lcmp.putInt(TAG_SPELL_POS_Y, j);

					CompoundNBT data = new CompoundNBT();
					piece.writeToNBT(data);
					lcmp.put(TAG_SPELL_DATA, data);

					list.add(lcmp);
				}
			}
		}

		cmp.put(TAG_SPELL_LIST, list);
	}

}

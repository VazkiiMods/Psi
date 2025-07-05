/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.ArrayList;
import java.util.List;

/**
 * Holder class for a spell's piece grid. Pretty much all internal, nothing to see here.
 */
public final class SpellGrid {

	public static final int GRID_SIZE = 9;
	public static final int GRID_CENTER = (GRID_SIZE - 1) / 2;
	private static final String TAG_SPELL_LIST = "spellList";
	private static final String TAG_SPELL_POS_X_LEGACY = "spellPosX";
	private static final String TAG_SPELL_POS_Y_LEGACY = "spellPosY";
	private static final String TAG_SPELL_DATA_LEGACY = "spellData";
	private static final String TAG_SPELL_POS_X = "x";
	private static final String TAG_SPELL_POS_Y = "y";
	private static final String TAG_SPELL_DATA = "data";
	public final Spell spell;
	public SpellPiece[][] gridData;

	private boolean empty;
	private int leftmost, rightmost, topmost, bottommost;

	public SpellGrid(Spell spell) {
		this.spell = spell;
		gridData = new SpellPiece[GRID_SIZE][GRID_SIZE];
	}

	public static boolean exists(int x, int y) {
		return x >= 0 && y >= 0 && x < GRID_SIZE && y < GRID_SIZE;
	}

	@OnlyIn(Dist.CLIENT)
	public void draw(PoseStack pPoseStack, MultiBufferSource buffers, int light) {
		for(int i = 0; i < GRID_SIZE; i++) {
			for(int j = 0; j < GRID_SIZE; j++) {
				SpellPiece p = gridData[i][j];
				if(p != null) {
					pPoseStack.pushPose();
					pPoseStack.translate(i * 18, j * 18, 0);
					p.draw(pPoseStack, buffers, light);
					pPoseStack.popPose();
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

		for(int i = 0; i < GRID_SIZE; i++) {
			for(int j = 0; j < GRID_SIZE; j++) {
				SpellPiece p = gridData[i][j];
				if(p != null) {
					empty = false;
					if(i < leftmost) {
						leftmost = i;
					}
					if(i > rightmost) {
						rightmost = i;
					}
					if(j < topmost) {
						topmost = j;
					}
					if(j > bottommost) {
						bottommost = j;
					}
				}
			}
		}
	}

	public int getSize() {
		recalculateBoundaries();

		if(empty) {
			return 0;
		}

		return Math.max(rightmost - leftmost + 1, bottommost - topmost + 1);
	}

	public void mirrorVertical() {
		recalculateBoundaries();
		if(empty) {
			return;
		}

		SpellPiece[][] newGrid = new SpellPiece[GRID_SIZE][GRID_SIZE];

		for(int i = 0; i < GRID_SIZE; i++) {
			for(int j = 0; j < GRID_SIZE; j++) {
				SpellPiece p = gridData[i][j];

				if(p != null) {
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
		if(empty) {
			return;
		}

		int xMod = ccw ? -1 : 1;
		int yMod = ccw ? 1 : -1;

		SpellPiece[][] newGrid = new SpellPiece[GRID_SIZE][GRID_SIZE];

		for(int i = 0; i < GRID_SIZE; i++) {
			for(int j = 0; j < GRID_SIZE; j++) {
				SpellPiece p = gridData[i][j];

				if(p != null) {
					int newX = xMod * (j - GRID_CENTER) + GRID_CENTER;
					int newY = yMod * (i - GRID_CENTER) + GRID_CENTER;

					newGrid[newX][newY] = p;
					p.x = newX;
					p.y = newY;

					for(SpellParam<?> param : p.paramSides.keySet()) {
						p.paramSides.compute(param, (k, side) -> ccw ? side.rotateCCW() : side.rotateCW());
					}
				}
			}
		}

		gridData = newGrid;
	}

	public boolean shift(SpellParam.Side side, boolean doit) {
		recalculateBoundaries();

		if(empty) {
			return false;
		}

		if(exists(leftmost + side.offx, topmost + side.offy) && exists(rightmost + side.offx, bottommost + side.offy)) {
			if(!doit) {
				return true;
			}

			SpellPiece[][] newGrid = new SpellPiece[GRID_SIZE][GRID_SIZE];

			for(int i = 0; i < GRID_SIZE; i++) {
				for(int j = 0; j < GRID_SIZE; j++) {
					SpellPiece p = gridData[i][j];

					if(p != null) {
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

	private SpellPiece getPieceAtSide(Multimap<SpellPiece, SpellParam.Side> traversed, int x, int y, SpellParam.Side side) throws SpellCompilationException {
		SpellPiece atSide = getPieceAtSideSafely(x, y, side);
		if(!traversed.put(atSide, side)) {
			throw new SpellCompilationException(SpellCompilationException.INFINITE_LOOP);
		}

		return atSide;
	}

	@Deprecated
	@SuppressWarnings("unused")
	public SpellPiece getPieceAtSideWithRedirections(List<SpellPiece> unused, int x, int y, SpellParam.Side side) throws SpellCompilationException {
		return getPieceAtSideWithRedirections(x, y, side);
	}

	public SpellPiece getPieceAtSideWithRedirections(int x, int y, SpellParam.Side side) throws SpellCompilationException {
		return getPieceAtSideWithRedirections(x, y, side, piece -> {});
	}

	/**
	 * @param walker a callback that incrementally gets called on each redirector reached
	 */
	public SpellPiece getPieceAtSideWithRedirections(int x, int y, SpellParam.Side side, SpellPieceConsumer walker) throws SpellCompilationException {
		SpellPiece atSide;
		Multimap<SpellPiece, SpellParam.Side> traversed = HashMultimap.create();
		while((atSide = getPieceAtSide(traversed, x, y, side)) instanceof IGenericRedirector) {
			IGenericRedirector redirector = (IGenericRedirector) atSide;
			walker.accept(atSide);
			SpellParam.Side rside = redirector.remapSide(side);
			if(!rside.isEnabled()) {
				return null;
			}
			side = rside;
			x = atSide.x;
			y = atSide.y;
		}

		return atSide;
	}

	public SpellPiece getPieceAtSideSafely(int x, int y, SpellParam.Side side) {
		int xp = x + side.offx;
		int yp = y + side.offy;
		if(!exists(xp, yp)) {
			return null;
		}

		return gridData[xp][yp];
	}

	public boolean isEmpty() {
		for(int i = 0; i < GRID_SIZE; i++) {
			for(int j = 0; j < GRID_SIZE; j++) {
				SpellPiece piece = gridData[i][j];
				if(piece != null) {
					return false;
				}
			}
		}

		return true;
	}

	public void readFromNBT(CompoundTag cmp) {
		gridData = new SpellPiece[GRID_SIZE][GRID_SIZE];

		ListTag list = cmp.getList(TAG_SPELL_LIST, 10);
		int len = list.size();
		for(int i = 0; i < len; i++) {
			CompoundTag lcmp = list.getCompound(i);
			int posX, posY;

			if(lcmp.contains(TAG_SPELL_POS_X_LEGACY)) {
				posX = lcmp.getInt(TAG_SPELL_POS_X_LEGACY);
				posY = lcmp.getInt(TAG_SPELL_POS_Y_LEGACY);
			} else {
				posX = lcmp.getInt(TAG_SPELL_POS_X);
				posY = lcmp.getInt(TAG_SPELL_POS_Y);
			}

			CompoundTag data;
			if(lcmp.contains(TAG_SPELL_DATA_LEGACY)) {
				data = lcmp.getCompound(TAG_SPELL_DATA_LEGACY);
			} else {
				data = lcmp.getCompound(TAG_SPELL_DATA);
			}

			SpellPiece piece = SpellPiece.createFromNBT(spell, data);
			if(piece != null) {
				gridData[posX][posY] = piece;
				piece.isInGrid = true;
				piece.x = posX;
				piece.y = posY;
			}
		}
	}

	private List<PieceWithPosition> getPiecesAsFlattenedList() {
		List<PieceWithPosition> pieces = new ArrayList<>();
		for(int i = 0; i < GRID_SIZE; i++) {
			for(int j = 0; j < GRID_SIZE; j++) {
				SpellPiece piece = this.gridData[i][j];
				if(piece != null) {
					pieces.add(new PieceWithPosition(piece, i, j));
				}
			}
		}
		return pieces;
	}

	private static SpellGrid fromCodecData(List<PieceWithPosition> spellList) {
		var grid = new SpellGrid(new Spell());
		for(var piece : spellList) {
			piece.piece.x = piece.x;
			piece.piece.y = piece.y;
			grid.gridData[piece.x][piece.y] = piece.piece;
		}
		grid.empty = spellList.isEmpty();
		return grid;
	}

	public static final MapCodec<SpellGrid> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.lazyInitialized(() -> Codec.list(PieceWithPosition.CODEC.codec())).fieldOf(TAG_SPELL_LIST).forGetter(SpellGrid::getPiecesAsFlattenedList)
	).apply(instance, SpellGrid::fromCodecData));

	public static final StreamCodec<RegistryFriendlyByteBuf, SpellGrid> STREAM_CODEC = StreamCodec.composite(
			NeoForgeStreamCodecs.lazy(() -> PieceWithPosition.STREAM_CODEC.apply(ByteBufCodecs.list())), SpellGrid::getPiecesAsFlattenedList,
			SpellGrid::fromCodecData
	);

	record PieceWithPosition(SpellPiece piece, int x, int y) {
		public static final MapCodec<PieceWithPosition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.lazyInitialized(() -> SpellPiece.CODEC).fieldOf(TAG_SPELL_DATA).forGetter(PieceWithPosition::piece),
				Codec.INT.fieldOf(TAG_SPELL_POS_X).forGetter(PieceWithPosition::x),
				Codec.INT.fieldOf(TAG_SPELL_POS_Y).forGetter(PieceWithPosition::y)
		).apply(instance, PieceWithPosition::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, PieceWithPosition> STREAM_CODEC = StreamCodec.composite(
				NeoForgeStreamCodecs.lazy(() -> SpellPiece.STREAM_CODEC), PieceWithPosition::piece,
				ByteBufCodecs.VAR_INT, PieceWithPosition::x,
				ByteBufCodecs.VAR_INT, PieceWithPosition::y,
				PieceWithPosition::new
		);
	}

	public void writeToNBT(CompoundTag cmp) {
		ListTag list = new ListTag();
		for(int i = 0; i < GRID_SIZE; i++) {
			for(int j = 0; j < GRID_SIZE; j++) {
				SpellPiece piece = gridData[i][j];
				if(piece != null) {
					CompoundTag lcmp = new CompoundTag();
					lcmp.putInt(TAG_SPELL_POS_X, i);
					lcmp.putInt(TAG_SPELL_POS_Y, j);

					CompoundTag data = new CompoundTag();
					piece.writeToNBT(data);
					lcmp.put(TAG_SPELL_DATA, data);

					list.add(lcmp);
				}
			}
		}

		cmp.put(TAG_SPELL_LIST, list);
	}

	// TODO: Put this somewhere nicer, or track down a library? Not sure where
	@FunctionalInterface
	public interface SpellPieceConsumer {
		void accept(SpellPiece piece) throws SpellCompilationException;
	}
}

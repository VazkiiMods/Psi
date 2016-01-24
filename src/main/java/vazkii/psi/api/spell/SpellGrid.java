/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 15:19:16 (GMT)]
 */
package vazkii.psi.api.spell;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Holder class for a spell's piece grid. Pretty much all internal, nothing to see here.
 */
public final class SpellGrid {

	private static final String TAG_SPELL_LIST = "spellList";
	private static final String TAG_SPELL_POS_X = "spellPosX";
	private static final String TAG_SPELL_POS_Y = "spellPosY";
	private static final String TAG_SPELL_DATA = "spellData";
	
	public static final int GRID_SIZE = 9;
	
	public final Spell spell;
	public SpellPiece[][] gridData; 
	
	@SideOnly(Side.CLIENT)
	public void draw() {
	for(int i = 0; i < GRID_SIZE; i++)
		for(int j = 0; j < GRID_SIZE; j++) {
			SpellPiece p = gridData[i][j];
			if(p != null) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(i * 18, j * 18, 0);
				p.draw();
				GlStateManager.popMatrix();
			}
		}
	}
	
	public int getSize() {
		boolean empty = false;
		int leftmost = GRID_SIZE;
		int rightmost = -1;
		int topmost = GRID_SIZE;
		int bottommost = -1;
		
		for(int i = 0; i < GRID_SIZE; i++)
			for(int j = 0; j < GRID_SIZE; j++) {
				SpellPiece p = gridData[i][j];
				if(p != null) {
					empty = false;
					if(i < leftmost)
						leftmost = i;
					if(i > rightmost)
						rightmost = i;
					if(j < topmost)
						topmost = j;
					if(j > bottommost)
						bottommost = j;
				}
			}
		
		if(empty)
			return 0;
		
		return Math.max(rightmost - leftmost + 1, bottommost - topmost + 1);
	}
	
	public static boolean exists(int x, int y) {
		return x >= 0 && y >= 0 && x < GRID_SIZE && y < GRID_SIZE;
	}
	
	public SpellPiece getPieceAtSideWithRedirections(int x, int y, SpellParam.Side side) throws SpellCompilationException {
		return getPieceAtSideWithRedirections(new ArrayList(), x, y, side);
	}
	
	public SpellPiece getPieceAtSideWithRedirections(List<SpellPiece> traversed, int x, int y, SpellParam.Side side) throws SpellCompilationException {
		SpellPiece atSide = getPieceAtSideSafely(x, y, side);
		if(traversed.contains(atSide))
			throw new SpellCompilationException(SpellCompilationException.INFINITE_LOOP);
			
		traversed.add(atSide);
		if(atSide == null || !(atSide instanceof IRedirector))
			return atSide;
		
		IRedirector redirector = (IRedirector) atSide;
		SpellParam.Side rside = redirector.getRedirectionSide();
		if(!rside.isEnabled())
			return null;
		
		return getPieceAtSideWithRedirections(traversed, atSide.x, atSide.y, rside);
	}
	
	public SpellPiece getPieceAtSideSafely(int x, int y, SpellParam.Side side) {
		int xp = x + side.offx;
		int yp = y + side.offy;
		if(!exists(xp, yp))
			return null;
		
		return gridData[xp][yp];
	}
	
	public SpellGrid(Spell spell) {
		this.spell = spell;
		gridData = new SpellPiece[GRID_SIZE][GRID_SIZE];
	}
	
	public boolean isEmpty() {
		for(int i = 0; i < GRID_SIZE; i++)
			for(int j = 0; j < GRID_SIZE; j++) {
				SpellPiece piece = gridData[i][j];
				if(piece != null)
					return false;
			}
		
		return true;
	}
	
	public void readFromNBT(NBTTagCompound cmp) {
		gridData = new SpellPiece[GRID_SIZE][GRID_SIZE];
		
		NBTTagList list = cmp.getTagList(TAG_SPELL_LIST, 10);
		int len = list.tagCount();
		for(int i = 0; i < len; i++) {
			NBTTagCompound lcmp = list.getCompoundTagAt(i);
			int posX = lcmp.getInteger(TAG_SPELL_POS_X);
			int posY = lcmp.getInteger(TAG_SPELL_POS_Y);
			
			NBTTagCompound data = lcmp.getCompoundTag(TAG_SPELL_DATA);
			SpellPiece piece = SpellPiece.createFromNBT(spell, data);
			if(piece != null) {
				gridData[posX][posY] = piece;
				piece.isInGrid = true;
				piece.x = posX;
				piece.y = posY;
			}
		}
	}
	
	public void writeToNBT(NBTTagCompound cmp) {
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < GRID_SIZE; i++)
			for(int j = 0; j < GRID_SIZE; j++) {
				SpellPiece piece = gridData[i][j];
				if(piece != null) {
					NBTTagCompound lcmp = new NBTTagCompound();
					lcmp.setInteger(TAG_SPELL_POS_X, i);
					lcmp.setInteger(TAG_SPELL_POS_Y, j);
					
					NBTTagCompound data = new NBTTagCompound();
					piece.writeToNBT(data);
					lcmp.setTag(TAG_SPELL_DATA, data);
					
					list.appendTag(lcmp);
				}
			}
		
		cmp.setTag(TAG_SPELL_LIST, list);
	}
	
}


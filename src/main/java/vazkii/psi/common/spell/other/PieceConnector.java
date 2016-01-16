/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 22:57:27 (GMT)]
 */
package vazkii.psi.common.spell.other;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.common.lib.LibResources;

public class PieceConnector extends SpellPiece {

	private static final ResourceLocation lines = new ResourceLocation(LibResources.SPELL_CONNECTOR_LINES);
	
	public SpellParam target;
	
	public PieceConnector(Spell spell) {
		super(spell);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void draw() {
		drawBackground();
		
		drawSide(paramSides.get(target));
		for(SpellParam.Side side : SpellParam.Side.class.getEnumConstants())
			if(side.isEnabled()) {
				SpellPiece piece = spell.grid.getPieceAtSideSafely(x, y, side);
				if(piece != null)
					for(SpellParam param : piece.paramSides.keySet()) {
						SpellParam.Side paramSide = piece.paramSides.get(param);
						if(paramSide.getOpposite() == side) {
							drawSide(side);
							break;
						}
				}
			}
		
		drawParams();
		GlStateManager.color(1F, 1F, 1F);
	}
	
	@SideOnly(Side.CLIENT)
	public void drawSide(SpellParam.Side side) {
		if(side.isEnabled()) {
			Minecraft mc = Minecraft.getMinecraft();
			mc.renderEngine.bindTexture(lines);
			
			double minU = 0;
			double minV = 0;
			switch(side) {
			case LEFT:
				minU = 0.5;
				break;
			case RIGHT: break;
			case TOP:
				minV = 0.5;
				break;
			case BOTTOM:
				minU = 0.5;
				minV = 0.5;
				break;
			default: break;
			}
			
			double maxU = minU + 0.5;
			double maxV = minV + 0.5;
			
			GlStateManager.color(1F, 1F, 1F);
			WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);
			wr.pos(0, 16, 0).tex(minU, maxV).endVertex();
			wr.pos(16, 16, 0).tex(maxU, maxV).endVertex();
			wr.pos(16, 0, 0).tex(maxU, minV).endVertex();;
			wr.pos(0, 0, 0).tex(minU, minV).endVertex();
			Tessellator.getInstance().draw();
		}
	}
	
	@Override
	public void getShownPieces(List<SpellPiece> pieces) {
		for(SpellParam.Side side : SpellParam.Side.class.getEnumConstants())
			if(side.isEnabled()) {
				PieceConnector piece = (PieceConnector) copy();
				piece.paramSides.put(piece.target, side);
				pieces.add(piece);
			}
	}
	
	@Override
	public void initParams() {
		addParam(target = new ParamAny(SpellParam.GENERIC_NAME_TARGET, SpellParam.GRAY, false));
	}

	@Override
	public EnumPieceType getPieceType() {
		return EnumPieceType.CONNECTOR;
	}

}

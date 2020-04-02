/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 22:57:27 (GMT)]
 */
package vazkii.psi.common.spell.other;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

import java.util.List;

public class PieceConnector extends SpellPiece implements IRedirector {

	public static final ResourceLocation LINES_TEXTURE = new ResourceLocation(LibResources.SPELL_CONNECTOR_LINES);

	public SpellParam<SpellParam.Any> target;

	public PieceConnector(Spell spell) {
		super(spell);
	}

	@Override
	public String getSortingName() {
		return "00000000000";
	}

	@Override
	public ITextComponent getEvaluationTypeString() {
        return new TranslationTextComponent("psi.datatype.any");
    }

	@Override
	@OnlyIn(Dist.CLIENT)
	public void drawAdditional(MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		drawSide(ms, buffers, light, paramSides.get(target));

		if(isInGrid)
			for(SpellParam.Side side : SpellParam.Side.class.getEnumConstants())
				if(side.isEnabled()) {
					SpellPiece piece = spell.grid.getPieceAtSideSafely(x, y, side);
					if(piece != null)
						for(SpellParam param : piece.paramSides.keySet()) {
							SpellParam.Side paramSide = piece.paramSides.get(param);
							if(paramSide.getOpposite() == side) {
								drawSide(ms, buffers, light, side);
								break;
							}
						}
				}
	}

	@OnlyIn(Dist.CLIENT)
	private void drawSide(MatrixStack ms, IRenderTypeBuffer buffers, int light, SpellParam.Side side) {
		if(side.isEnabled()) {
			Material material = new Material(PsiAPI.PSI_PIECE_TEXTURE_ATLAS, LINES_TEXTURE);
			IVertexBuilder buffer = material.getVertexConsumer(buffers, SpellPiece::getRenderLayer);

			float minU = 0;
			float minV = 0;
			switch (side) {
				case LEFT:
					minU = 0.5f;
					break;
				case RIGHT:
					break;
				case TOP:
					minV = 0.5f;
					break;
				case BOTTOM:
					minU = 0.5f;
					minV = 0.5f;
					break;
				default:
					break;
			}

			float maxU = minU + 0.5f;
			float maxV = minV + 0.5f;

			/*
			  See note in SpellPiece#drawBackground for why this chain needs to be split
 			 */
			Matrix4f mat = ms.peek().getModel();
			buffer.vertex(mat, 0, 16, 0).color(1F, 1F, 1F, 1F);
			buffer.texture(minU, maxV).light(light).endVertex();
			buffer.vertex(mat, 16, 16, 0).color(1F, 1F, 1F, 1F);
			buffer.texture(maxU, maxV).light(light).endVertex();
			buffer.vertex(mat, 16, 0, 0).color(1F, 1F, 1F, 1F);
			buffer.texture(maxU, minV).light(light).endVertex();
			buffer.vertex(mat, 0, 0, 0).color(1F, 1F, 1F, 1F);
			buffer.texture(minU, minV).light(light).endVertex();
		}
	}

	@Override
	public void getShownPieces(List<SpellPiece> pieces) {
		for(SpellParam.Side side : SpellParam.Side.class.getEnumConstants())
			if(side.isEnabled()) {
				PieceConnector piece = (PieceConnector) SpellPiece.create(PieceConnector.class, new Spell());
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

	@Override
	public SpellParam.Side getRedirectionSide() {
		return paramSides.get(target);
	}

	// Dist this class implements IRedirector we don't need this
	@Override
	public Class<?> getEvaluationType() {
		return null;
	}

	@Override
	public Object evaluate() {
		return null;
	}

	@Override
	public Object execute(SpellContext context) {
		return null;
	}


}

/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.other;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.joml.Matrix4f;

import vazkii.psi.api.ClientPsiAPI;
import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.IRedirector;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.param.ParamAny;
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
	public Component getEvaluationTypeString() {
		return Component.translatable("psi.datatype.any");
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void drawAdditional(PoseStack ms, MultiBufferSource buffers, int light) {
		drawSide(ms, buffers, light, paramSides.get(target));

		if(isInGrid) {
			for(SpellParam.Side side : SpellParam.Side.class.getEnumConstants()) {
				if(side.isEnabled()) {
					SpellPiece piece = spell.grid.getPieceAtSideSafely(x, y, side);
					if(piece != null && piece.isInputSide(side.getOpposite())) {
						drawSide(ms, buffers, light, side);
					}
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	private void drawSide(PoseStack ms, MultiBufferSource buffers, int light, SpellParam.Side side) {
		if(side.isEnabled()) {
			Material material = new Material(ClientPsiAPI.PSI_PIECE_TEXTURE_ATLAS, LINES_TEXTURE);
			VertexConsumer buffer = material.buffer(buffers, ignored -> SpellPiece.getLayer());

			float minU = 0;
			float minV = 0;
			switch(side) {
			case LEFT:
				minU = 0.5f;
				break;
			default:
			case RIGHT:
				break;
			case TOP:
				minV = 0.5f;
				break;
			case BOTTOM:
				minU = 0.5f;
				minV = 0.5f;
				break;
			}

			float maxU = minU + 0.5f;
			float maxV = minV + 0.5f;

			/*
			See note in SpellPiece#drawBackground for why this chain needs to be split
			*/
			Matrix4f mat = ms.last().pose();
			buffer.vertex(mat, 0, 16, 0).color(1F, 1F, 1F, 1F);
			buffer.uv(minU, maxV).uv2(light).endVertex();
			buffer.vertex(mat, 16, 16, 0).color(1F, 1F, 1F, 1F);
			buffer.uv(maxU, maxV).uv2(light).endVertex();
			buffer.vertex(mat, 16, 0, 0).color(1F, 1F, 1F, 1F);
			buffer.uv(maxU, minV).uv2(light).endVertex();
			buffer.vertex(mat, 0, 0, 0).color(1F, 1F, 1F, 1F);
			buffer.uv(minU, minV).uv2(light).endVertex();
		}
	}

	@Override
	public void getShownPieces(List<SpellPiece> pieces) {
		for(SpellParam.Side side : SpellParam.Side.class.getEnumConstants()) {
			if(side.isEnabled()) {
				PieceConnector piece = (PieceConnector) SpellPiece.create(PieceConnector.class, new Spell());
				piece.paramSides.put(piece.target, side);
				pieces.add(piece);
			}
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

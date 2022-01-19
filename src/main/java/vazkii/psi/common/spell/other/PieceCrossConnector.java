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
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Matrix4f;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.psi.api.ClientPsiAPI;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.IGenericRedirector;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellParam.ArrowType;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.common.lib.LibResources;

public class PieceCrossConnector extends SpellPiece implements IGenericRedirector {

	SpellParam<SpellParam.Any> in1;
	SpellParam<SpellParam.Any> out1;
	SpellParam<SpellParam.Any> in2;
	SpellParam<SpellParam.Any> out2;

	public PieceCrossConnector(Spell spell) {
		super(spell);
	}

	private static final int LINE_ONE = 0xA0A0A0;
	private static final int LINE_TWO = 0xA040FF;

	@Override
	public void initParams() {
		addParam(in1 = new ParamAny(SpellParam.CONNECTOR_NAME_FROM1, LINE_ONE, false));
		addParam(out1 = new ParamAny(SpellParam.CONNECTOR_NAME_TO1, LINE_ONE, false, ArrowType.NONE));
		addParam(in2 = new ParamAny(SpellParam.CONNECTOR_NAME_FROM2, LINE_TWO, false));
		addParam(out2 = new ParamAny(SpellParam.CONNECTOR_NAME_TO2, LINE_TWO, false, ArrowType.NONE));
	}

	@Override
	public boolean isInputSide(SpellParam.Side side) {
		return paramSides.get(in1) == side || paramSides.get(in2) == side;
	}

	@Override
	public String getSortingName() {
		return "00000000000";
	}

	@Override
	public Component getEvaluationTypeString() {
		return new TranslatableComponent("psi.datatype.any");
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);
	}

	@Override
	public void drawAdditional(PoseStack ms, MultiBufferSource buffers, int light) {
		drawSide(ms, buffers, paramSides.get(in1), light, LINE_ONE);
		drawSide(ms, buffers, paramSides.get(out1), light, LINE_ONE);

		drawSide(ms, buffers, paramSides.get(in2), light, LINE_TWO);
		drawSide(ms, buffers, paramSides.get(out2), light, LINE_TWO);
	}

	@OnlyIn(Dist.CLIENT)
	private void drawSide(PoseStack ms, MultiBufferSource buffers, SpellParam.Side side, int light, int color) {
		if (side.isEnabled()) {
			Material material = new Material(ClientPsiAPI.PSI_PIECE_TEXTURE_ATLAS, new ResourceLocation(LibResources.SPELL_CONNECTOR_LINES));
			VertexConsumer buffer = material.buffer(buffers, ignored -> SpellPiece.getLayer());

			float minU = 0;
			float minV = 0;
			switch (side) {
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
			float r = PsiRenderHelper.r(color) / 255f;
			float g = PsiRenderHelper.g(color) / 255f;
			float b = PsiRenderHelper.b(color) / 255f;

			/*
			See note in SpellPiece#drawBackground for why this chain needs to be split
			*/
			Matrix4f mat = ms.last().pose();
			buffer.vertex(mat, 0, 16, 0).color(r, g, b, 1F);
			buffer.uv(minU, maxV).uv2(light).endVertex();
			buffer.vertex(mat, 16, 16, 0).color(r, g, b, 1F);
			buffer.uv(maxU, maxV).uv2(light).endVertex();
			buffer.vertex(mat, 16, 0, 0).color(r, g, b, 1F);
			buffer.uv(maxU, minV).uv2(light).endVertex();
			buffer.vertex(mat, 0, 0, 0).color(r, g, b, 1F);
			buffer.uv(minU, minV).uv2(light).endVertex();
		}
	}

	@Override
	public EnumPieceType getPieceType() {
		return EnumPieceType.CONNECTOR;
	}

	@Override
	public SpellParam.Side remapSide(SpellParam.Side inputSide) {
		if (paramSides.get(out1).getOpposite() == inputSide) {
			return paramSides.get(in1);
		} else if (paramSides.get(out2).getOpposite() == inputSide) {
			return paramSides.get(in2);
		} else {
			return SpellParam.Side.OFF;
		}
	}

	// Since this class implements IGenericRedirector we don't need this
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

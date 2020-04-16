package vazkii.psi.common.spell.other;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.spell.*;
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
		addParam(out1 = new ParamAny(SpellParam.CONNECTOR_NAME_TO1, LINE_ONE, false));
		addParam(in2 = new ParamAny(SpellParam.CONNECTOR_NAME_FROM2, LINE_TWO, false));
		addParam(out2 = new ParamAny(SpellParam.CONNECTOR_NAME_TO2, LINE_TWO, false));
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
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);
	}


	@Override
	public void drawAdditional(MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		drawSide(ms, buffers,paramSides.get(in1), light, LINE_ONE);
		drawSide(ms, buffers,paramSides.get(out1), light,  LINE_ONE);

		drawSide(ms, buffers,paramSides.get(in2), light,  LINE_TWO);
		drawSide(ms, buffers,paramSides.get(out2), light, LINE_TWO);
	}

	@OnlyIn(Dist.CLIENT)
	private void drawSide(MatrixStack ms, IRenderTypeBuffer buffers, SpellParam.Side side, int light, int color) {
		if(side.isEnabled()) {
			Material material = new Material(PsiAPI.PSI_PIECE_TEXTURE_ATLAS, new ResourceLocation(LibResources.SPELL_CONNECTOR_LINES));
			IVertexBuilder buffer = material.getVertexConsumer(buffers, ignored -> SpellPiece.getLayer());

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
			Matrix4f mat = ms.peek().getModel();
			buffer.vertex(mat, 0, 16, 0).color(r, g, b, 1F);
			buffer.texture(minU, maxV).light(light).endVertex();
			buffer.vertex(mat, 16, 16, 0).color(r, g, b, 1F);
			buffer.texture(maxU, maxV).light(light).endVertex();
			buffer.vertex(mat, 16, 0, 0).color(r, g, b, 1F);
			buffer.texture(maxU, minV).light(light).endVertex();
			buffer.vertex(mat, 0, 0, 0).color(r, g, b, 1F);
			buffer.texture(minU, minV).light(light).endVertex();
		}
	}
	@OnlyIn(Dist.CLIENT)
	public void drawParams(MatrixStack ms, IRenderTypeBuffer buffers, int light) {

		drawParam(ms, buffers, light, in1);
		drawParam(ms, buffers, light, in2);
	}

	public void drawParam(MatrixStack ms, IRenderTypeBuffer buffers, int light, SpellParam param) {
		IVertexBuilder buffer = buffers.getBuffer(PsiAPI.internalHandler.getProgrammerLayer());
		SpellParam.Side side = paramSides.get(param);
		if (side.isEnabled()) {
			int minX = 4;
			int minY = 4;
			minX += side.offx * 9;
			minY += side.offy * 9;

			int maxX = minX + 8;
			int maxY = minY + 8;

			float wh = 8F;
			float minU = side.u / 256F;
			float minV = side.v / 256F;
			float maxU = (side.u + wh) / 256F;
			float maxV = (side.v + wh) / 256F;
			int r = PsiRenderHelper.r(param.color);
			int g = PsiRenderHelper.g(param.color);
			int b = PsiRenderHelper.b(param.color);
			int a = 255;
			Matrix4f mat = ms.peek().getModel();

			buffer.vertex(mat, minX, maxY, 0).color(r, g, b, a).texture(minU, maxV).light(light).endVertex();
			buffer.vertex(mat, maxX, maxY, 0).color(r, g, b, a).texture(maxU, maxV).light(light).endVertex();
			buffer.vertex(mat, maxX, minY, 0).color(r, g, b, a).texture(maxU, minV).light(light).endVertex();
			buffer.vertex(mat, minX, minY, 0).color(r, g, b, a).texture(minU, minV).light(light).endVertex();
		}
	}


	@Override
	public EnumPieceType getPieceType() {
		return EnumPieceType.CONNECTOR;
	}

	@Override
	public SpellParam.Side remapSide(SpellParam.Side inputSide) {
		if (paramSides.get(out1).getOpposite() == inputSide)
			return paramSides.get(in1);
		else if (paramSides.get(out2).getOpposite() == inputSide)
			return paramSides.get(in2);
		else
			return SpellParam.Side.OFF;
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

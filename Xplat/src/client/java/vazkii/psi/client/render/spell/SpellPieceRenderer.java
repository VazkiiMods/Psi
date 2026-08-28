/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.render.spell;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import org.joml.Matrix4f;

import vazkii.psi.api.ClientPsiAPI;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.SpellParam.ArrowType;
import vazkii.psi.common.platform.PsiPlatform;
import vazkii.psi.common.platform.PsiServices;
import vazkii.psi.common.spell.constant.PieceConstantNumber;
import vazkii.psi.common.spell.other.PieceConnector;
import vazkii.psi.common.spell.other.PieceCrossConnector;

import java.util.List;

public final class SpellPieceRenderer {

	private static final ResourceLocation CONNECTOR_LINES =
			ResourceLocation.fromNamespaceAndPath("psi", "spell/connector_lines");

	private SpellPieceRenderer() {}

	public static void draw(Spell spell, PoseStack poseStack, MultiBufferSource buffers, int light) {
		draw(spell.grid, poseStack, buffers, light);
	}

	public static void draw(SpellGrid grid, PoseStack poseStack, MultiBufferSource buffers, int light) {
		for(int x = 0; x < SpellGrid.GRID_SIZE; x++) {
			for(int y = 0; y < SpellGrid.GRID_SIZE; y++) {
				SpellPiece piece = grid.gridData[x][y];
				if(piece != null) {
					poseStack.pushPose();
					poseStack.translate(x * 18, y * 18, 0);
					draw(piece, poseStack, buffers, light);
					poseStack.popPose();
				}
			}
		}
	}

	public static void draw(SpellPiece piece, PoseStack poseStack, MultiBufferSource buffers, int light) {
		poseStack.pushPose();
		drawBackground(piece, poseStack, buffers, light);
		poseStack.translate(0F, 0F, 0.1F);
		drawAdditional(piece, poseStack, buffers, light);
		if(piece.isInGrid) {
			poseStack.translate(0F, 0F, 0.1F);
			drawParams(piece, poseStack, buffers, light);
			poseStack.translate(0F, 0F, 0.1F);
			drawComment(piece, poseStack, buffers, light);
		}
		poseStack.popPose();
	}

	public static void drawBackground(
			SpellPiece piece, PoseStack poseStack, MultiBufferSource buffers, int light) {
		Material material = ClientPsiAPI.SPELL_PIECE_MATERIAL_REGISTRY.get(piece.registryKey);
		if(material == null) {
			return;
		}

		VertexConsumer buffer = material.buffer(buffers, ignored -> SpellPieceRenderLayer.get());
		Matrix4f matrix = poseStack.last().pose();
		vertex(buffer, matrix, 0, 16, 0, 1, 1F, 1F, 1F, light);
		vertex(buffer, matrix, 16, 16, 1, 1, 1F, 1F, 1F, light);
		vertex(buffer, matrix, 16, 0, 1, 0, 1F, 1F, 1F, light);
		vertex(buffer, matrix, 0, 0, 0, 0, 1F, 1F, 1F, light);
	}

	private static void drawAdditional(
			SpellPiece piece, PoseStack poseStack, MultiBufferSource buffers, int light) {
		if(piece instanceof PieceConnector connector) {
			drawConnector(connector, poseStack, buffers, light);
		} else if(piece instanceof PieceCrossConnector connector) {
			drawCrossConnector(connector, poseStack, buffers, light);
		} else if(piece instanceof PieceConstantNumber number) {
			drawNumber(number, poseStack, buffers);
		}
	}

	private static void drawComment(
			SpellPiece piece, PoseStack poseStack, MultiBufferSource buffers, int light) {
		if(piece.comment == null || piece.comment.isEmpty()) {
			return;
		}

		VertexConsumer buffer = buffers.getBuffer(SpellPieceRenderLayer.programmer());
		float size = 6F;
		float minU = 150 / 256F;
		float minV = 184 / 256F;
		float maxU = (150 + size) / 256F;
		float maxV = (184 + size) / 256F;
		Matrix4f matrix = poseStack.last().pose();
		vertex(buffer, matrix, -2, 4, minU, maxV, 1F, 1F, 1F, light);
		vertex(buffer, matrix, 4, 4, maxU, maxV, 1F, 1F, 1F, light);
		vertex(buffer, matrix, 4, -2, maxU, minV, 1F, 1F, 1F, light);
		vertex(buffer, matrix, -2, -2, minU, minV, 1F, 1F, 1F, light);
	}

	private static void drawParams(
			SpellPiece piece, PoseStack poseStack, MultiBufferSource buffers, int light) {
		VertexConsumer buffer = buffers.getBuffer(SpellPieceRenderLayer.programmer());
		for(SpellParam<?> param : piece.paramSides.keySet()) {
			drawParam(piece, poseStack, buffer, light, param);
		}
	}

	private static void drawParam(
			SpellPiece piece, PoseStack poseStack, VertexConsumer buffer, int light, SpellParam<?> param) {
		SpellParam.Side side = piece.paramSides.get(param);
		if(!side.isEnabled() || param.getArrowType() == ArrowType.NONE) {
			return;
		}

		int index = piece.getParamArrowIndex(param);
		int count = piece.getParamArrowCount(side);
		SpellPiece neighbour = piece.spell.grid.getPieceAtSideSafely(piece.x, piece.y, side);
		if(neighbour != null) {
			int neighbourCount = neighbour.getParamArrowCount(side.getOpposite());
			if(side.asInt() > side.getOpposite().asInt()) {
				index += neighbourCount;
			}
			count += neighbourCount;
		}

		float percent = count > 1 ? (float) index / (count - 1) : 0.5f;
		drawParam(poseStack, buffer, light, side, param.color, param.getArrowType(), percent);
	}

	private static void drawParam(PoseStack poseStack, VertexConsumer buffer, int light,
			SpellParam.Side side, int color, ArrowType arrowType, float percent) {
		if(arrowType == ArrowType.NONE) {
			return;
		}

		float minX = 4 + side.minx * percent + side.maxx * (1 - percent);
		float minY = 4 + side.miny * percent + side.maxy * (1 - percent);
		float maxX = minX + 8;
		float maxY = minY + 8;
		if(arrowType == ArrowType.OUT) {
			side = side.getOpposite();
		}

		float minU = side.u / 256F;
		float minV = side.v / 256F;
		float maxU = (side.u + 8F) / 256F;
		float maxV = (side.v + 8F) / 256F;
		float red = PsiRenderHelper.r(color) / 255F;
		float green = PsiRenderHelper.g(color) / 255F;
		float blue = PsiRenderHelper.b(color) / 255F;
		Matrix4f matrix = poseStack.last().pose();
		vertex(buffer, matrix, minX, maxY, minU, maxV, red, green, blue, light);
		vertex(buffer, matrix, maxX, maxY, maxU, maxV, red, green, blue, light);
		vertex(buffer, matrix, maxX, minY, maxU, minV, red, green, blue, light);
		vertex(buffer, matrix, minX, minY, minU, minV, red, green, blue, light);
	}

	private static void drawConnector(
			PieceConnector connector, PoseStack poseStack, MultiBufferSource buffers, int light) {
		drawConnectorSide(poseStack, buffers, light, connector.paramSides.get(connector.target), 0xFFFFFF);
		if(connector.isInGrid) {
			for(SpellParam.Side side : SpellParam.Side.class.getEnumConstants()) {
				if(side.isEnabled()) {
					SpellPiece neighbour = connector.spell.grid.getPieceAtSideSafely(connector.x, connector.y, side);
					if(neighbour != null && neighbour.isInputSide(side.getOpposite())) {
						drawConnectorSide(poseStack, buffers, light, side, 0xFFFFFF);
					}
				}
			}
		}
	}

	private static void drawCrossConnector(
			PieceCrossConnector connector, PoseStack poseStack, MultiBufferSource buffers, int light) {
		drawConnectorSide(poseStack, buffers, light, connector.paramSides.get(connector.in1), 0xA0A0A0);
		drawConnectorSide(poseStack, buffers, light, connector.paramSides.get(connector.out1), 0xA0A0A0);
		drawConnectorSide(poseStack, buffers, light, connector.paramSides.get(connector.in2), 0xA040FF);
		drawConnectorSide(poseStack, buffers, light, connector.paramSides.get(connector.out2), 0xA040FF);
	}

	private static void drawConnectorSide(PoseStack poseStack, MultiBufferSource buffers, int light,
			SpellParam.Side side, int color) {
		if(!side.isEnabled()) {
			return;
		}
		Material material = new Material(InventoryMenu.BLOCK_ATLAS, CONNECTOR_LINES);
		VertexConsumer buffer = material.buffer(buffers, ignored -> SpellPieceRenderLayer.get());
		float minU = side == SpellParam.Side.LEFT || side == SpellParam.Side.BOTTOM ? 0.5F : 0F;
		float minV = side == SpellParam.Side.TOP || side == SpellParam.Side.BOTTOM ? 0.5F : 0F;
		float red = PsiRenderHelper.r(color) / 255F;
		float green = PsiRenderHelper.g(color) / 255F;
		float blue = PsiRenderHelper.b(color) / 255F;
		Matrix4f matrix = poseStack.last().pose();
		vertex(buffer, matrix, 0, 16, minU, minV + 0.5F, red, green, blue, light);
		vertex(buffer, matrix, 16, 16, minU + 0.5F, minV + 0.5F, red, green, blue, light);
		vertex(buffer, matrix, 16, 0, minU + 0.5F, minV, red, green, blue, light);
		vertex(buffer, matrix, 0, 0, minU, minV, red, green, blue, light);
	}

	private static void drawNumber(
			PieceConstantNumber number, PoseStack poseStack, MultiBufferSource buffers) {
		if(number.valueStr == null || number.valueStr.isEmpty() || number.valueStr.length() > 5) {
			number.valueStr = "0";
		}
		Minecraft minecraft = Minecraft.getInstance();
		int color = PsiServices.load(PsiPlatform.class).findMod("magipsi").isPresent() ? 0 : 0xFFFFFF;
		float effectiveLength = minecraft.font.width(number.valueStr);
		float scale = 1;
		while(effectiveLength > 16) {
			scale++;
			effectiveLength = minecraft.font.width(number.valueStr) / scale;
		}
		poseStack.pushPose();
		poseStack.scale(1F / scale, 1F / scale, 1F);
		poseStack.translate((9 - effectiveLength / 2) * scale, 4 * scale, 0);
		minecraft.font.drawInBatch(number.valueStr, 0, 0, color, false, poseStack.last().pose(), buffers,
				Font.DisplayMode.NORMAL, 0, 15728880);
		poseStack.popPose();
	}

	public static void getTooltip(SpellPiece piece, List<Component> tooltip) {
		tooltip.add(Component.translatable(piece.getUnlocalizedName()));
		tooltip.add(Component.translatable(piece.getUnlocalizedDesc()).withStyle(ChatFormatting.GRAY));
		if(Screen.hasShiftDown()) {
			addToTooltipAfterShift(piece, tooltip);
		} else {
			tooltip.add(Component.translatable("psimisc.shift_for_info"));
		}
		if(!piece.getStatLabels().isEmpty()) {
			if(Screen.hasControlDown()) {
				addToTooltipAfterControl(piece, tooltip);
			} else {
				tooltip.add(Component.translatable("psimisc.ctrl_for_stats"));
			}
		}

		String addon = piece.registryKey.getNamespace();
		if(!addon.equals("psi")) {
			PsiServices.load(PsiPlatform.class).findMod(addon)
					.ifPresent(mod -> tooltip.add(Component.translatable("psimisc.provider_mod", mod.name())));
		}
	}

	private static void addToTooltipAfterShift(SpellPiece piece, List<Component> tooltip) {
		tooltip.add(Component.empty());
		MutableComponent evaluation = piece.getEvaluationTypeString().plainCopy().withStyle(ChatFormatting.GOLD);
		tooltip.add(Component.literal("Output ").append(evaluation));
		for(SpellParam<?> param : piece.paramSides.keySet()) {
			Component name = Component.translatable(param.name).withStyle(ChatFormatting.YELLOW);
			Component type = Component.literal(" [").append(param.getRequiredTypeString()).append("]")
					.withStyle(ChatFormatting.YELLOW);
			tooltip.add(Component.literal(param.canDisable ? "[Input] " : " Input  ").append(name).append(type));
		}
	}

	private static void addToTooltipAfterControl(SpellPiece piece, List<Component> tooltip) {
		tooltip.add(Component.empty());
		piece.getStatLabels().forEach((type, stat) -> {
			tooltip.add(Component.translatable(type.getName()).append(":"));
			tooltip.add(Component.literal(" " + stat).withStyle(ChatFormatting.YELLOW));
		});
	}

	public static void drawTooltip(
			GuiGraphics graphics, int x, int y, List<Component> tooltip, Screen screen) {
		if(!tooltip.isEmpty()) {
			graphics.renderTooltip(Minecraft.getInstance().font, tooltip, java.util.Optional.empty(), x, y);
		}
	}

	public static void drawCommentText(
			GuiGraphics graphics, int x, int y, List<Component> comment, Screen screen) {
		if(!comment.isEmpty()) {
			graphics.renderTooltip(Minecraft.getInstance().font, comment, java.util.Optional.empty(),
					x, y - 9 - comment.size() * 10);
		}
	}

	private static void vertex(VertexConsumer buffer, Matrix4f matrix, float x, float y, float u, float v,
			float red, float green, float blue, int light) {
		buffer.addVertex(matrix, x, y, 0).setColor(red, green, blue, 1F);
		buffer.setUv(u, v).setLight(light);
	}

}

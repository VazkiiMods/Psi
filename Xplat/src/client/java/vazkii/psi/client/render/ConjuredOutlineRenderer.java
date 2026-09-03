/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.slf4j.Logger;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.CADComponentLookup;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.tile.TileConjured;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws a depth-tested silhouette outline around solid conjured blocks, replacing the old
 * particle-edge effect.
 */
public final class ConjuredOutlineRenderer {

	private static final ResourceLocation POST_CHAIN_LOCATION = PsiAPI.location("shaders/post/conjured_outline.json");
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final int SEARCH_RADIUS_CHUNKS = 6;

	private static PostChain postChain;
	private static RenderTarget fillTarget;
	private static int chainWidth = -1;
	private static int chainHeight = -1;
	private static boolean disabled = false;
	private static boolean filledThisFrame = false;

	private ConjuredOutlineRenderer() {}

	public static void fillPass(Matrix4f modelViewMatrix, Matrix4f projectionMatrix, Camera camera) {
		if(disabled) {
			return;
		}

		try {
			filledThisFrame = fillPassUnsafe(modelViewMatrix, projectionMatrix, camera);
		} catch (Exception e) {
			disable(e);
		}
	}

	public static void compositePass(float partialTick) {
		if(disabled || !filledThisFrame) {
			return;
		}

		filledThisFrame = false;

		try {
			compositePassUnsafe(partialTick);
		} catch (Exception e) {
			disable(e);
		}
	}

	private static void disable(Exception e) {
		// PostChain allocates GL framebuffers before it can fail; retrying every frame would leak them.
		disabled = true;
		if(postChain != null) {
			postChain.close();
		}
		postChain = null;
		fillTarget = null;
		LOGGER.error("Conjured outline rendering failed; disabling it for the rest of this session", e);
	}

	private static boolean fillPassUnsafe(Matrix4f modelViewMatrix, Matrix4f projectionMatrix, Camera camera) throws IOException {
		Minecraft mc = Minecraft.getInstance();
		ClientLevel level = mc.level;
		if(level == null) {
			return false;
		}

		List<TileConjured> tiles = collectNearbyConjured(level, camera);
		if(tiles.isEmpty()) {
			return false;
		}

		ensurePostChain(mc);

		RenderTarget mainTarget = mc.getMainRenderTarget();

		fillTarget.clear(Minecraft.ON_OSX);
		fillTarget.copyDepthFrom(mainTarget);
		fillTarget.bindWrite(false);
		drawTiles(tiles, modelViewMatrix, projectionMatrix, camera);
		fillTarget.unbindWrite();
		mainTarget.bindWrite(false);
		return true;
	}

	private static void compositePassUnsafe(float partialTick) {
		Minecraft mc = Minecraft.getInstance();
		RenderTarget mainTarget = mc.getMainRenderTarget();

		postChain.process(partialTick);
		mainTarget.bindWrite(false);

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
				GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
		fillTarget.blitToScreen(mc.getWindow().getWidth(), mc.getWindow().getHeight(), false);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	}

	private static void ensurePostChain(Minecraft mc) throws IOException {
		int width = mc.getWindow().getWidth();
		int height = mc.getWindow().getHeight();
		if(postChain != null && width == chainWidth && height == chainHeight) {
			return;
		}

		if(postChain != null) {
			postChain.close();
			postChain = null;
			fillTarget = null;
		}

		postChain = new PostChain(mc.getTextureManager(), mc.getResourceManager(), mc.getMainRenderTarget(), POST_CHAIN_LOCATION);
		postChain.resize(width, height);
		fillTarget = postChain.getTempTarget("final");
		chainWidth = width;
		chainHeight = height;
	}

	private static List<TileConjured> collectNearbyConjured(ClientLevel level, Camera camera) {
		List<TileConjured> result = new ArrayList<>();
		BlockPos camPos = camera.getBlockPosition();
		int camChunkX = camPos.getX() >> 4;
		int camChunkZ = camPos.getZ() >> 4;

		for(int dx = -SEARCH_RADIUS_CHUNKS; dx <= SEARCH_RADIUS_CHUNKS; dx++) {
			for(int dz = -SEARCH_RADIUS_CHUNKS; dz <= SEARCH_RADIUS_CHUNKS; dz++) {
				LevelChunk chunk = level.getChunkSource().getChunk(camChunkX + dx, camChunkZ + dz, false);
				if(chunk == null) {
					continue;
				}

				for(BlockEntity be : chunk.getBlockEntities().values()) {
					if(be instanceof TileConjured tile && tile.getBlockState().getValue(BlockConjured.SOLID)) {
						result.add(tile);
					}
				}
			}
		}

		return result;
	}

	private static void drawTiles(List<TileConjured> tiles, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, Camera camera) {
		Vec3 camPos = camera.getPosition();

		RenderSystem.setProjectionMatrix(projectionMatrix, VertexSorting.DISTANCE_TO_ORIGIN);
		Matrix4fStack modelView = RenderSystem.getModelViewStack();
		modelView.pushMatrix();
		modelView.set(modelViewMatrix);
		RenderSystem.applyModelViewMatrix();

		RenderSystem.disableCull();
		RenderSystem.enableDepthTest();
		RenderSystem.depthFunc(515);
		RenderSystem.depthMask(true);
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		for(TileConjured tile : tiles) {
			if(tile.getLevel() == null) {
				continue;
			}

			int color = CADComponentLookup.color(tile.getLevel().registryAccess(), tile.colorizer);
			float r = PsiRenderHelper.r(color) / 255F;
			float g = PsiRenderHelper.g(color) / 255F;
			float b = PsiRenderHelper.b(color) / 255F;

			BlockPos pos = tile.getBlockPos();
			double x = pos.getX() - camPos.x;
			double y = pos.getY() - camPos.y;
			double z = pos.getZ() - camPos.z;
			addCube(buffer, x, y, z, r, g, b);
		}

		BufferUploader.drawWithShader(buffer.buildOrThrow());
		RenderSystem.enableCull();

		modelView.popMatrix();
		RenderSystem.applyModelViewMatrix();
	}

	private static void addCube(BufferBuilder buffer, double x, double y, double z, float r, float g, float b) {
		float x0 = (float) x;
		float y0 = (float) y;
		float z0 = (float) z;
		float x1 = x0 + 1F;
		float y1 = y0 + 1F;
		float z1 = z0 + 1F;

		quad(buffer, r, g, b, x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0); // west
		quad(buffer, r, g, b, x1, y0, z0, x1, y1, z0, x1, y1, z1, x1, y0, z1); // east
		quad(buffer, r, g, b, x0, y0, z0, x1, y0, z0, x1, y0, z1, x0, y0, z1); // down
		quad(buffer, r, g, b, x0, y1, z0, x0, y1, z1, x1, y1, z1, x1, y1, z0); // up
		quad(buffer, r, g, b, x0, y0, z0, x0, y1, z0, x1, y1, z0, x1, y0, z0); // north
		quad(buffer, r, g, b, x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1); // south
	}

	private static void quad(BufferBuilder buffer, float r, float g, float b,
			float x0, float y0, float z0, float x1, float y1, float z1,
			float x2, float y2, float z2, float x3, float y3, float z3) {
		buffer.addVertex(x0, y0, z0).setColor(r, g, b, 1F);
		buffer.addVertex(x1, y1, z1).setColor(r, g, b, 1F);
		buffer.addVertex(x2, y2, z2).setColor(r, g, b, 1F);
		buffer.addVertex(x3, y3, z3).setColor(r, g, b, 1F);
	}

}

/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * File Created @ [Jul 2, 2014, 12:12:45 AM (GMT)]
 */
package vazkii.psi.client.fx;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import vazkii.psi.common.lib.LibMisc;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public final class ParticleRenderDispatcher {

	public static int wispFxCount = 0;
	public static int depthIgnoringWispFxCount = 0;
	public static int sparkleFxCount = 0;

	@SubscribeEvent
	public static void onRenderWorldLast(RenderWorldLastEvent event) {
		Tessellator tessellator = Tessellator.getInstance();

		Profiler profiler = Minecraft.getMinecraft().profiler;

		boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		GlStateManager.disableLighting();

		profiler.startSection("psi-particles");
		profiler.startSection("sparkle");
		FXSparkle.dispatchQueuedRenders(tessellator);
		profiler.endStartSection("wisp");
		FXWisp.dispatchQueuedRenders(tessellator);
		profiler.endSection();
		profiler.endSection();

		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
		if (lighting)
			GlStateManager.enableLighting();
	}

}

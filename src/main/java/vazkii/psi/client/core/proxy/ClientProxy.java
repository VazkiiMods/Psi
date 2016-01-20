/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [08/01/2016, 21:23:11 (GMT)]
 */
package vazkii.psi.client.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.client.core.handler.HUDHandler;
import vazkii.psi.client.core.handler.KeybindHandler;
import vazkii.psi.client.core.handler.ModelHandler;
import vazkii.psi.client.core.handler.ShaderHandler;
import vazkii.psi.client.fx.FXSparkle;
import vazkii.psi.client.fx.FXWisp;
import vazkii.psi.client.fx.ParticleRenderDispatcher;
import vazkii.psi.client.render.tile.RenderTileProgrammer;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.proxy.CommonProxy;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		ModelHandler.init();
		ShaderHandler.init();
		KeybindHandler.init();
		
		MinecraftForge.EVENT_BUS.register(new HUDHandler());
		MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
		MinecraftForge.EVENT_BUS.register(new ParticleRenderDispatcher());

		ClientRegistry.bindTileEntitySpecialRenderer(TileProgrammer.class, new RenderTileProgrammer());
	}
	
	private static boolean noclipEnabled = false;
	private static boolean corruptSparkle = false;

	@Override
	public void setSparkleFXNoClip(boolean noclip) {
		noclipEnabled = noclip;
	}
	
	@Override
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
		if(!doParticle(world))
			return;

		FXSparkle sparkle = new FXSparkle(world, x, y, z, size, r, g, b, m);
		if(noclipEnabled)
			sparkle.noClip = true;
		sparkle.motionX = motionx;
		sparkle.motionY = motiony;
		sparkle.motionZ = motionz;
		Minecraft.getMinecraft().effectRenderer.addEffect(sparkle);
	}

	private static boolean distanceLimit = true;
	private static boolean depthTest = true;

	@Override
	public void setWispFXDistanceLimit(boolean limit) {
		distanceLimit = limit;
	}

	@Override
	public void setWispFXDepthTest(boolean test) {
		depthTest = test;
	}

	@Override
	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
		if(!doParticle(world))
			return;

		FXWisp wisp = new FXWisp(world, x, y, z, size, r, g, b, distanceLimit, depthTest, maxAgeMul);
		wisp.motionX = motionx;
		wisp.motionY = motiony;
		wisp.motionZ = motionz;

		Minecraft.getMinecraft().effectRenderer.addEffect(wisp);
	}
	
	private boolean doParticle(World world) {
		if(!world.isRemote)
			return false;

		if(!ConfigHandler.useVanillaParticleLimiter)
			return true;

		float chance = 1F;
		if(Minecraft.getMinecraft().gameSettings.particleSetting == 1)
			chance = 0.6F;
		else if(Minecraft.getMinecraft().gameSettings.particleSetting == 2)
			chance = 0.2F;

		return chance == 1F || Math.random() < chance;
	}
	
}

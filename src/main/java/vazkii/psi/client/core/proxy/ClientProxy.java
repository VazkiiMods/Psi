/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/01/2016, 21:23:11 (GMT)]
 */
package vazkii.psi.client.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import vazkii.arl.util.ClientTicker;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.client.core.handler.HUDHandler;
import vazkii.psi.client.core.handler.KeybindHandler;
import vazkii.psi.client.core.handler.ShaderHandler;
import vazkii.psi.client.fx.FXSparkle;
import vazkii.psi.client.fx.FXWisp;
import vazkii.psi.client.render.entity.RenderSpellCircle;
import vazkii.psi.client.render.tile.RenderTileProgrammer;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.PersistencyHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.proxy.IProxy;
import vazkii.psi.common.entity.EntitySpellCircle;

@OnlyIn(Dist.CLIENT)
public class ClientProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        ShaderHandler.init();
        KeybindHandler.init();

//		if(ConfigHandler.versionCheckEnabled)
//			new VersionChecker().init();

        ClientRegistry.bindTileEntitySpecialRenderer(TileProgrammer.class, new RenderTileProgrammer());

        RenderingRegistry.registerEntityRenderingHandler(EntitySpellCircle.class, RenderSpellCircle::new);
    }

    @Override
    public boolean isTheClientPlayer(LivingEntity entity) {
        return entity == Minecraft.getInstance().player;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public long getWorldElapsedTicks() {
        return ClientTicker.ticksInGame;
    }

    @Override
    public int getClientRenderDistance() {
        return Minecraft.getInstance().gameSettings.renderDistanceChunks;
    }

    @Override
    public void onLevelUp(int level) {
        HUDHandler.levelUp(level);
    }

    @Override
    public void savePersistency() {
        PersistencyHandler.save(PlayerDataHandler.get(getClientPlayer()).level);
    }

    @Override
    public int getColorForCAD(ItemStack cadStack) {
        ICAD icad = (ICAD) cadStack.getItem();
        return icad.getSpellColor(cadStack);
    }

    @Override
    public int getColorForColorizer(ItemStack colorizer) {
        ICADColorizer icc = (ICADColorizer) colorizer.getItem();
		return icc.getColor(colorizer);
	}

	@Override
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
		if(noParticles(world) || m == 0)
			return;

		FXSparkle sparkle = new FXSparkle(world, x, y, z, size, r, g, b, m);
		sparkle.setSpeed(motionx, motiony, motionz);
		if (sparkle.getMultiplier() > 0)
			Minecraft.getInstance().effectRenderer.addEffect(sparkle);
	}

	@Override
	public void sparkleFX(double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
		sparkleFX(Minecraft.getInstance().world, x, y, z, r, g, b, motionx, motiony, motionz, size, m);
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
        if (noParticles(world) || maxAgeMul == 0)
            return;

        FXWisp wisp = new FXWisp(world, x, y, z, size, r, g, b, distanceLimit, depthTest, maxAgeMul);
        wisp.setSpeed(motionx, motiony, motionz);

        if (wisp.getMoteHalfLife() > 0)
            Minecraft.getInstance().effectRenderer.addEffect(wisp);
        Minecraft.getInstance().worldRenderer.addParticle();
    }

	@Override
	public void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
		wispFX(Minecraft.getInstance().world, x, y, z, r, g, b, size, motionx, motiony, motionz, maxAgeMul);
	}

	private boolean noParticles(World world) {
        if (world == null)
            return true;

        if (!world.isRemote)
            return true;

        if (!ConfigHandler.useVanillaParticleLimiter)
            return false;

        float chance = 1F;
        if (Minecraft.getInstance().gameSettings.particles == ParticleStatus.DECREASED)
            chance = 0.6F;
        else if (Minecraft.getInstance().gameSettings.particles == ParticleStatus.MINIMAL)
            chance = 0.2F;

        return !(chance == 1F) && !(Math.random() < chance);
    }

	@Override
	public String localize(String key, Object... arguments) {
		return I18n.format(key, arguments);
	}
}

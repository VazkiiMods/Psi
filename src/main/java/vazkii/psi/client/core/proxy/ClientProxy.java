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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.client.core.handler.*;
import vazkii.psi.client.fx.SparkleParticleData;
import vazkii.psi.client.fx.WispParticleData;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.client.model.ModelCAD;
import vazkii.psi.client.render.entity.RenderSpellCircle;
import vazkii.psi.client.render.entity.RenderSpellProjectile;
import vazkii.psi.client.render.tile.RenderTileProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.proxy.IProxy;
import vazkii.psi.common.entity.*;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

@OnlyIn(Dist.CLIENT)
public class ClientProxy implements IProxy {


    @Override
    public void registerHandlers() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::modelBake);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::addCADModels);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
	}

	private void clientSetup(FMLClientSetupEvent event) {

		KeybindHandler.init();

		ClientRegistry.bindTileEntityRenderer(TileProgrammer.TYPE, RenderTileProgrammer::new);

		RenderingRegistry.registerEntityRenderingHandler(EntitySpellCircle.TYPE, RenderSpellCircle::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellCharge.TYPE, RenderSpellProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellGrenade.TYPE, RenderSpellProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.TYPE, RenderSpellProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellMine.TYPE, RenderSpellProjectile::new);
		RenderTypeLookup.setRenderLayer(ModBlocks.conjured, RenderType.getTranslucent());
		ContributorSpellCircleHandler.firstStart();
		addTexturesToAtlas();

	}

	private void loadComplete(FMLLoadCompleteEvent event) {
		DeferredWorkQueue.runLater(ColorHandler::init);
		DeferredWorkQueue.runLater(ShaderHandler::init);
	}

	private void modelBake(ModelBakeEvent event) {
		ModelResourceLocation key = new ModelResourceLocation(ModItems.cad.getRegistryName(), "inventory");
		event.getModelRegistry().put(key, new ModelCAD(event.getModelRegistry().get(key)));

	}

	private void addTexturesToAtlas() {
		ModelBakery.LOCATIONS_BUILTIN_TEXTURES.addAll(PsiAPI.getAllSpellPieceMaterial());
		PsiAPI.registerAtlasTexture(LibResources.SPELL_CONNECTOR_LINES, PsiAPI.PSI_PIECE_TEXTURE_ATLAS);
		PsiAPI.registerAtlasTexture(LibResources.GUI_PROGRAMMER, PsiAPI.PSI_PROGRAMMER_TEXTURE_ATLAS);
		ModelBakery.LOCATIONS_BUILTIN_TEXTURES.addAll(PsiAPI.getMiscAtlasMaterial());
	}


	private void addCADModels(ModelRegistryEvent event) {
		ModelLoader.addSpecialModel(new ResourceLocation(LibMisc.MOD_ID, "item/" + LibItemNames.CAD_IRON));
		ModelLoader.addSpecialModel(new ResourceLocation(LibMisc.MOD_ID, "item/" + LibItemNames.CAD_GOLD));
		ModelLoader.addSpecialModel(new ResourceLocation(LibMisc.MOD_ID, "item/" + LibItemNames.CAD_PSIMETAL));
		ModelLoader.addSpecialModel(new ResourceLocation(LibMisc.MOD_ID, "item/" + LibItemNames.CAD_EBONY_PSIMETAL));
		ModelLoader.addSpecialModel(new ResourceLocation(LibMisc.MOD_ID, "item/" + LibItemNames.CAD_IVORY_PSIMETAL));
		ModelLoader.addSpecialModel(new ResourceLocation(LibMisc.MOD_ID, "item/" + LibItemNames.CAD_CREATIVE));

	}



	@Override
	public void addParticleForce(World world, IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		world.addParticle(particleData, true, x, y, z, xSpeed, ySpeed, zSpeed);
	}

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public long getWorldElapsedTicks() {
		return ClientTickHandler.ticksInGame;
    }

    @Override
    public int getClientRenderDistance() {
        return Minecraft.getInstance().gameSettings.renderDistanceChunks;
    }

    @Override
	public void onLevelUp(ResourceLocation level) {
		HUDHandler.levelUp(level);
	}


    @Override
    public int getColorForCAD(ItemStack cadStack) {
        ICAD icad = (ICAD) cadStack.getItem();
        return icad.getSpellColor(cadStack);
    }

    @Override
    public int getColorForColorizer(ItemStack colorizer) {
		if (colorizer.isEmpty())
			return ICADColorizer.DEFAULT_SPELL_COLOR;
		ICADColorizer icc = (ICADColorizer) colorizer.getItem();
		return icc.getColor(colorizer);
	}

	@Override
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
        if (m == 0)
            return;
		SparkleParticleData data = new SparkleParticleData(size, r, g, b, m, motionx, motiony, motionz);
        addParticleForce(world, data, x, y, z, motionx, motiony, motionz);

    }

	@Override
	public void sparkleFX(double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
		sparkleFX(Minecraft.getInstance().world, x, y, z, r, g, b, motionx, motiony, motionz, size, m);
	}

	@Override
	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
		if (maxAgeMul == 0)
			return;
		WispParticleData data = new WispParticleData(size, r, g, b, maxAgeMul);
		addParticleForce(world, data, x, y, z, motionx, motiony, motionz);
	}

	@Override
	public void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
		wispFX(Minecraft.getInstance().world, x, y, z, r, g, b, size, motionx, motiony, motionz, maxAgeMul);
	}

	@Override
	public void openProgrammerGUI(TileProgrammer programmer) {
		Minecraft.getInstance().displayGuiScreen(new GuiProgrammer(programmer));
	}

}

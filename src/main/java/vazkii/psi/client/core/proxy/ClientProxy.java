/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.proxy;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.client.core.handler.ColorHandler;
import vazkii.psi.client.fx.SparkleParticleData;
import vazkii.psi.client.fx.WispParticleData;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.client.model.ModModelLayers;
import vazkii.psi.client.model.ModelCAD;
import vazkii.psi.client.model.ModelPsimetalExosuit;
import vazkii.psi.client.render.entity.RenderSpellCircle;
import vazkii.psi.client.render.entity.RenderSpellProjectile;
import vazkii.psi.client.render.tile.RenderTileProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.proxy.IProxy;
import vazkii.psi.common.entity.ModEntities;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.mixin.client.AccessorRenderBuffers;

import java.util.SequencedMap;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientProxy implements IProxy {

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
		evt.registerBlockEntityRenderer(ModBlocks.programmerType, RenderTileProgrammer::new);
		evt.registerEntityRenderer(ModEntities.spellCircle, RenderSpellCircle::new);
		evt.registerEntityRenderer(ModEntities.spellCharge, RenderSpellProjectile::new);
		evt.registerEntityRenderer(ModEntities.spellGrenade, RenderSpellProjectile::new);
		evt.registerEntityRenderer(ModEntities.spellProjectile, RenderSpellProjectile::new);
		evt.registerEntityRenderer(ModEntities.spellMine, RenderSpellProjectile::new);
	}

	@SubscribeEvent
	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
		evt.registerLayerDefinition(ModModelLayers.PSIMETAL_EXOSUIT_INNER_ARMOR, () -> LayerDefinition.create(ModelPsimetalExosuit.createInsideMesh(), 64, 128));
		evt.registerLayerDefinition(ModModelLayers.PSIMETAL_EXOSUIT_OUTER_ARMOR, () -> LayerDefinition.create(ModelPsimetalExosuit.createOutsideMesh(), 64, 128));
	}

	@Override
	public void registerHandlers(IEventBus bus) {
		bus.addListener(this::modelBake);
		bus.addListener(this::addCADModels);
		bus.addListener(this::loadComplete);
	}

	private void loadComplete(FMLLoadCompleteEvent event) {
		event.enqueueWork(() -> {
			SequencedMap<RenderType, ByteBufferBuilder> map = ((AccessorRenderBuffers) Minecraft.getInstance().renderBuffers().bufferSource()).getFixedBuffers();
			RenderType layer = SpellPiece.getLayer();
			map.put(layer, new ByteBufferBuilder(layer.bufferSize()));
			map.put(GuiProgrammer.LAYER, new ByteBufferBuilder(GuiProgrammer.LAYER.bufferSize()));
			ColorHandler.init();
		});
	}

	private void modelBake(ModelEvent.ModifyBakingResult event) {
		event.getModels().computeIfPresent(ModelResourceLocation.inventory(BuiltInRegistries.ITEM.getKey(ModItems.cad)), (k, oldModel) -> new ModelCAD());
	}

	private void addCADModels(ModelEvent.RegisterAdditional event) {
		event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "item/" + LibItemNames.CAD_IRON)));
		event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "item/" + LibItemNames.CAD_GOLD)));
		event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "item/" + LibItemNames.CAD_PSIMETAL)));
		event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "item/" + LibItemNames.CAD_EBONY_PSIMETAL)));
		event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "item/" + LibItemNames.CAD_IVORY_PSIMETAL)));
		event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "item/" + LibItemNames.CAD_CREATIVE)));
	}

	@Override
	public boolean hasAdvancement(ResourceLocation advancement, Player playerEntity) {
		if(playerEntity instanceof LocalPlayer clientPlayerEntity) {
			return clientPlayerEntity.connection.getAdvancements().get(advancement) != null;
		} else if(playerEntity instanceof ServerPlayer serverPlayerEntity) {
			return serverPlayerEntity.getServer().getAdvancements().get(advancement) != null && serverPlayerEntity.getAdvancements().getOrStartProgress(serverPlayerEntity.getServer().getAdvancements().get(advancement)).isDone();
		}
		return false;
	}

	@Override
	public void addParticleForce(Level world, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		world.addParticle(particleData, true, x, y, z, xSpeed, ySpeed, zSpeed);
	}

	@Override
	public Player getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	@Override
	public Level getClientWorld() {
		return Minecraft.getInstance().level;
	}

	@Override
	public long getWorldElapsedTicks() {
		return ClientTickHandler.ticksInGame;
	}

	@Override
	public int getClientRenderDistance() {
		return Minecraft.getInstance().options.renderDistance().get();
	}

	@Override
	public int getColorForCAD(ItemStack cadStack) {
		ICAD icad = (ICAD) cadStack.getItem();
		return icad.getSpellColor(cadStack);
	}

	@Override
	public int getColorForColorizer(ItemStack colorizer) {
		if(colorizer.isEmpty() || !(colorizer.getItem() instanceof ICADColorizer icc)) {
			return ICADColorizer.DEFAULT_SPELL_COLOR;
		}
		return icc.getColor(colorizer);
	}

	@Override
	public void sparkleFX(Level world, double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
		if(m == 0) {
			return;
		}
		SparkleParticleData data = new SparkleParticleData(size, r, g, b, m, motionx, motiony, motionz);
		addParticleForce(world, data, x, y, z, motionx, motiony, motionz);

	}

	@Override
	public void sparkleFX(double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
		sparkleFX(Minecraft.getInstance().level, x, y, z, r, g, b, motionx, motiony, motionz, size, m);
	}

	@Override
	public void wispFX(Level world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
		if(maxAgeMul == 0) {
			return;
		}
		WispParticleData data = new WispParticleData(size, r, g, b, maxAgeMul);
		addParticleForce(world, data, x, y, z, motionx, motiony, motionz);
	}

	@Override
	public void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
		wispFX(Minecraft.getInstance().level, x, y, z, r, g, b, size, motionx, motiony, motionz, maxAgeMul);
	}

	@Override
	public void openProgrammerGUI(TileProgrammer programmer) {
		Minecraft.getInstance().setScreen(new GuiProgrammer(programmer));
	}

}

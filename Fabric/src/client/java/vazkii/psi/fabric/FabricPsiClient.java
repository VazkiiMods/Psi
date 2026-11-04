/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import vazkii.psi.client.core.PsiClientHooks;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.client.core.handler.ColorHandler;
import vazkii.psi.client.core.handler.HUDHandler;
import vazkii.psi.client.core.handler.KeybindHandler;
import vazkii.psi.client.core.handler.PlayerDataRenderHandler;
import vazkii.psi.client.core.handler.ShaderHandler;
import vazkii.psi.client.fx.FXSparkle;
import vazkii.psi.client.fx.FXWisp;
import vazkii.psi.client.fx.ModParticles;
import vazkii.psi.client.gui.GuiCADAssembler;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.client.model.ArmorModels;
import vazkii.psi.client.model.ClientModelHandler;
import vazkii.psi.client.model.ModModelLayers;
import vazkii.psi.client.model.ModelArmor;
import vazkii.psi.client.model.ModelPsimetalExosuit;
import vazkii.psi.client.render.entity.RenderSpellCircle;
import vazkii.psi.client.render.entity.RenderSpellProjectile;
import vazkii.psi.client.render.spell.SpellPieceMaterial;
import vazkii.psi.client.render.spell.SpellPieceRenderLayer;
import vazkii.psi.client.render.tile.RenderTileProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.entity.ModEntities;
import vazkii.psi.common.item.armor.ItemPsimetalArmor;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.component.ItemCADComponent;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.fabric.platform.FabricPsiNetworkService;
import vazkii.psi.fabric.platform.FabricPsiNetworkService.ClientboundRegistration;
import vazkii.psi.mixin.client.AccessorRenderBuffers;

import java.util.SequencedMap;

import static vazkii.psi.common.block.base.ModBlocks.containerCADAssembler;

public final class FabricPsiClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		if(System.getProperty("fabric-api.datagen") != null) {
			return;
		}

		FabricPsiNetworkService network = FabricPsiNetworkService.instance();
		SpellPieceMaterial.init();
		PsiClientHooks.install();
		ClientModelHandler.registerItemProperties(ItemProperties::register);
		ClientModelHandler.installCadModelLookup(id -> ((FabricBakedModelManager) Minecraft.getInstance().getModelManager()).getModel(id));
		registerModels();
		KeyBindingHelper.registerKeyBinding(KeybindHandler.keybind);
		ColorHandler.register((color, items) -> ColorProviderRegistry.ITEM.register(color, items));
		CoreShaderRegistrationCallback.EVENT.register(context -> ShaderHandler.register(context::register));
		HudRenderCallback.EVENT.register(HUDHandler::renderAll);
		net.minecraft.client.gui.screens.MenuScreens.register(containerCADAssembler.get(), GuiCADAssembler::new);
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> installRenderBuffers(client.renderBuffers()));
		ParticleFactoryRegistry.getInstance().register(ModParticles.WISP.get(), FXWisp.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.SPARKLE.get(), FXSparkle.Factory::new);
		registerRenderers();
		ClientTickEvents.START_CLIENT_TICK.register(client -> ClientTickHandler.clientTickPre());
		ClientTickEvents.END_CLIENT_TICK.register(client -> ClientTickHandler.clientTickPost());
		WorldRenderEvents.START.register(context -> ClientTickHandler.renderTick(context.tickCounter().getGameTimeDeltaPartialTick(false)));
		WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> PlayerDataRenderHandler.renderAll(
				context.tickCounter().getGameTimeDeltaPartialTick(false), context.matrixStack()));
		ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> ItemCADComponent.appendForeignHoverText(context.registries(), stack, lines));
		network.clientboundRegistrations().forEach(FabricPsiClient::registerClientbound);
		network.installClientSender(ClientPlayNetworking::send);
	}

	private static void registerRenderers() {
		BlockEntityRendererRegistry.register(ModBlocks.programmerType.get(), RenderTileProgrammer::new);
		EntityRendererRegistry.register(ModEntities.spellCircle.get(), RenderSpellCircle::new);
		EntityRendererRegistry.register(ModEntities.spellCharge.get(), RenderSpellProjectile::new);
		EntityRendererRegistry.register(ModEntities.spellGrenade.get(), RenderSpellProjectile::new);
		EntityRendererRegistry.register(ModEntities.spellProjectile.get(), RenderSpellProjectile::new);
		EntityRendererRegistry.register(ModEntities.spellMine.get(), RenderSpellProjectile::new);
		EntityModelLayerRegistry.registerModelLayer(ModModelLayers.PSIMETAL_EXOSUIT_INNER_ARMOR,
				() -> LayerDefinition.create(ModelPsimetalExosuit.createInsideMesh(), 64, 128));
		EntityModelLayerRegistry.registerModelLayer(ModModelLayers.PSIMETAL_EXOSUIT_OUTER_ARMOR,
				() -> LayerDefinition.create(ModelPsimetalExosuit.createOutsideMesh(), 64, 128));
		ArmorRenderer.register((matrices, vertexConsumers, stack, entity, slot, light, contextModel) -> {
			ModelArmor model = ArmorModels.prepare(stack, slot, contextModel);
			if(model != null && stack.getItem() instanceof ItemPsimetalArmor armor) {
				ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, LibResources.MODEL_PSIMETAL_EXOSUIT);
				model.renderToBuffer(matrices,
						vertexConsumers.getBuffer(RenderType.armorCutoutNoCull(LibResources.MODEL_PSIMETAL_EXOSUIT_SENSOR)),
						light, OverlayTexture.NO_OVERLAY, armor.getColor(stack));
			}
		}, ModItems.psimetalExosuitHelmet.get(), ModItems.psimetalExosuitChestplate.get(),
				ModItems.psimetalExosuitLeggings.get(), ModItems.psimetalExosuitBoots.get());
	}

	private static void registerModels() {
		ModelLoadingPlugin.register(context -> context.addModels(ClientModelHandler.additionalCadModels()));
	}

	private static void installRenderBuffers(RenderBuffers renderBuffers) {
		SequencedMap<RenderType, ByteBufferBuilder> buffers = ((AccessorRenderBuffers) renderBuffers.bufferSource()).getFixedBuffers();
		RenderType spellLayer = SpellPieceRenderLayer.get();
		buffers.put(spellLayer, new ByteBufferBuilder(spellLayer.bufferSize()));
		buffers.put(GuiProgrammer.LAYER, new ByteBufferBuilder(GuiProgrammer.LAYER.bufferSize()));
	}

	private static <T extends CustomPacketPayload> void registerClientbound(ClientboundRegistration<T> registration) {
		ClientPlayNetworking.registerGlobalReceiver(registration.type(),
				(payload, context) -> registration.handler().accept(payload, context.player()));
	}

}

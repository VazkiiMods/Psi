/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.client;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.client.core.PsiClientHooks;
import vazkii.psi.client.core.handler.ColorHandler;
import vazkii.psi.client.core.handler.HUDHandler;
import vazkii.psi.client.core.handler.KeybindHandler;
import vazkii.psi.client.core.handler.ShaderHandler;
import vazkii.psi.client.fx.FXSparkle;
import vazkii.psi.client.fx.FXWisp;
import vazkii.psi.client.fx.ModParticles;
import vazkii.psi.client.gui.GuiCADAssembler;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.client.model.ArmorModels;
import vazkii.psi.client.model.ClientModelHandler;
import vazkii.psi.client.model.ModModelLayers;
import vazkii.psi.client.model.ModelPsimetalExosuit;
import vazkii.psi.client.render.entity.RenderSpellCircle;
import vazkii.psi.client.render.entity.RenderSpellProjectile;
import vazkii.psi.client.render.spell.SpellPieceMaterial;
import vazkii.psi.client.render.spell.SpellPieceRenderLayer;
import vazkii.psi.client.render.tile.RenderTileProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.entity.ModEntities;
import vazkii.psi.common.item.component.ItemCADComponent;
import vazkii.psi.mixin.client.AccessorRenderBuffers;

import java.io.IOException;
import java.util.Objects;
import java.util.SequencedMap;

import static vazkii.psi.common.block.base.ModBlocks.containerCADAssembler;
import static vazkii.psi.common.item.base.ModItems.psimetalExosuitBoots;
import static vazkii.psi.common.item.base.ModItems.psimetalExosuitChestplate;
import static vazkii.psi.common.item.base.ModItems.psimetalExosuitHelmet;
import static vazkii.psi.common.item.base.ModItems.psimetalExosuitLeggings;

@EventBusSubscriber(value = Dist.CLIENT, modid = PsiAPI.MOD_ID)
public final class NeoForgePsiClient {

	private NeoForgePsiClient() {}

	public static void initialize(IEventBus bus) {
		SpellPieceMaterial.init();
		PsiClientHooks.install();
		ClientModelHandler.installCadModelLookup(id -> Minecraft.getInstance().getModelManager()
				.getModel(new ModelResourceLocation(id, "standalone")));
		bus.addListener((RegisterKeyMappingsEvent event) -> event.register(KeybindHandler.keybind));
		bus.addListener(NeoForgePsiClient::addCadModels);
		bus.addListener(NeoForgePsiClient::loadComplete);
	}

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		ItemCADComponent.appendForeignHoverText(event.getContext().registries(), event.getItemStack(), event.getToolTip());
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlocks.programmerType.get(), RenderTileProgrammer::new);
		event.registerEntityRenderer(ModEntities.spellCircle.get(), RenderSpellCircle::new);
		event.registerEntityRenderer(ModEntities.spellCharge.get(), RenderSpellProjectile::new);
		event.registerEntityRenderer(ModEntities.spellGrenade.get(), RenderSpellProjectile::new);
		event.registerEntityRenderer(ModEntities.spellProjectile.get(), RenderSpellProjectile::new);
		event.registerEntityRenderer(ModEntities.spellMine.get(), RenderSpellProjectile::new);
	}

	@SubscribeEvent
	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModModelLayers.PSIMETAL_EXOSUIT_INNER_ARMOR,
				() -> LayerDefinition.create(ModelPsimetalExosuit.createInsideMesh(), 64, 128));
		event.registerLayerDefinition(ModModelLayers.PSIMETAL_EXOSUIT_OUTER_ARMOR,
				() -> LayerDefinition.create(ModelPsimetalExosuit.createOutsideMesh(), 64, 128));
	}

	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(ModParticles.WISP.get(), FXWisp.Factory::new);
		event.registerSpriteSet(ModParticles.SPARKLE.get(), FXSparkle.Factory::new);
	}

	@SubscribeEvent
	public static void registerMenuScreens(RegisterMenuScreensEvent event) {
		event.register(containerCADAssembler.get(), GuiCADAssembler::new);
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		ColorHandler.register((color, items) -> event.register(color, items));
	}

	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent event) {
		HUDHandler.register(event::registerAboveAll);
	}

	@SubscribeEvent
	public static void registerShaders(RegisterShadersEvent event) throws IOException {
		ShaderHandler.register((id, vertexFormat, onLoaded) -> event.registerShader(
				new ShaderInstance(event.getResourceProvider(), id, vertexFormat), onLoaded));
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new IClientItemExtensions() {
			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(@NotNull LivingEntity entity,
					@NotNull ItemStack stack, @NotNull EquipmentSlot slot, @NotNull HumanoidModel<?> original) {
				return Objects.requireNonNull(ArmorModels.get(stack));
			}
		}, psimetalExosuitHelmet.get(), psimetalExosuitChestplate.get(), psimetalExosuitLeggings.get(),
				psimetalExosuitBoots.get());
		ClientModelHandler.registerItemProperties(ItemProperties::register);
	}

	private static void loadComplete(FMLLoadCompleteEvent event) {
		event.enqueueWork(() -> {
			SequencedMap<RenderType, ByteBufferBuilder> buffers = ((AccessorRenderBuffers) Minecraft.getInstance()
					.renderBuffers().bufferSource()).getFixedBuffers();
			RenderType spellLayer = SpellPieceRenderLayer.get();
			buffers.put(spellLayer, new ByteBufferBuilder(spellLayer.bufferSize()));
			buffers.put(GuiProgrammer.LAYER, new ByteBufferBuilder(GuiProgrammer.LAYER.bufferSize()));
		});
	}

	private static void addCadModels(ModelEvent.RegisterAdditional event) {
		ClientModelHandler.additionalCadModels()
				.forEach(model -> event.register(ModelResourceLocation.standalone(model)));
	}

}

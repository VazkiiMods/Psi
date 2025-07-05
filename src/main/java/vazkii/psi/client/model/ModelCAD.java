/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADAssembly;

import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ModelCAD implements BakedModel {

	private final ItemOverrides itemHandler = new ItemOverrides() {
		@Nullable
		@Override
		public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int mode) {
			ICAD cad = (ICAD) stack.getItem();
			ItemStack assemblyStack = cad.getComponentInSlot(stack, EnumCADComponent.ASSEMBLY);
			if(assemblyStack.isEmpty()) {
				return Minecraft.getInstance().getModelManager().getMissingModel();
			}
			ResourceLocation cadModel = ((ICADAssembly) assemblyStack.getItem()).getCADModel(assemblyStack, stack);
			return Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(cadModel));
		}
	};

	/**
	 * @deprecated Forge: Use {@link #getQuads(BlockState, Direction, RandomSource,
	 *             net.neoforged.neoforge.client.model.data.ModelData,
	 *             net.minecraft.client.renderer.RenderType)}
	 */
	@Deprecated
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
		return Collections.emptyList();
	}

	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @org.jetbrains.annotations.Nullable Direction side, RandomSource rand, ModelData data, @org.jetbrains.annotations.Nullable RenderType renderType) {
		return Collections.emptyList();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean usesBlockLight() {
		return true;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	/** @deprecated Forge: Use {@link #getParticleIcon(net.neoforged.neoforge.client.model.data.ModelData)} */
	@NotNull
	@Override
	@Deprecated
	public TextureAtlasSprite getParticleIcon() {
		return this.getParticleIcon(ModelData.EMPTY);
	}

	@NotNull
	@Override
	public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
		return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MissingTextureAtlasSprite.getLocation());
	}

	@NotNull
	@Override
	public ItemOverrides getOverrides() {
		return itemHandler;
	}

}

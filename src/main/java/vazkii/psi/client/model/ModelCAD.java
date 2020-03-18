package vazkii.psi.client.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADAssembly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ModelCAD implements IBakedModel {
    private final IBakedModel original;

    public ModelCAD(IBakedModel original) {
        this.original = original;
    }

    private final ItemOverrideList itemHandler = new ItemOverrideList() {

        @Override
        public IBakedModel getModelWithOverrides(@Nonnull IBakedModel model, ItemStack stack, @Nullable World world, @Nullable LivingEntity entity) {
            ICAD cad = (ICAD) stack.getItem();
            ItemStack assemblyStack = cad.getComponentInSlot(stack, EnumCADComponent.ASSEMBLY);
            if (assemblyStack.isEmpty()) {
                return Minecraft.getInstance().getModelManager().getMissingModel();
            }
			ResourceLocation cadModel = ((ICADAssembly) assemblyStack.getItem()).getCADModel(assemblyStack, stack);
			return Minecraft.getInstance().getModelManager().getModel(cadModel);
		}
	};

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random random) {
		return Collections.emptyList();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isSideLit() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Nonnull
	@Override
	public TextureAtlasSprite getParticleTexture() {
		return Minecraft.getInstance().getSpriteAtlas(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(MissingTextureSprite.getLocation());
	}

	@Nonnull
	@Override
	public ItemOverrideList getOverrides() {
		return itemHandler;
	}

}

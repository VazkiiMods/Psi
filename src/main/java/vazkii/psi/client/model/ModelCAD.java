package vazkii.psi.client.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADAssembly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * How psi actually loads the CAD models.
 * Beware that they need to be already baked or else it wont work.
 * See  {@link vazkii.psi.client.core.proxy.ClientProxy} for an example on how to do it
 */
@OnlyIn(Dist.CLIENT)
public class ModelCAD implements IBakedModel {


    private final IBakedModel original;

    public ModelCAD(ModelBakery bakery, IBakedModel original) {
        this.original = original;
    }

    private final ItemOverrideList itemHandler = new ItemOverrideList() {

        @Nullable
        @Override
        public IBakedModel getModelWithOverrides(@Nonnull IBakedModel model, ItemStack stack, @Nullable World world, @Nullable LivingEntity entity) {
            ICAD cad = (ICAD) stack.getItem();
            ItemStack assemblyStack = cad.getComponentInSlot(stack, EnumCADComponent.ASSEMBLY);
            if (assemblyStack.isEmpty()) {
                return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation("missingno"));
            }
            ICADAssembly assembly = (ICADAssembly) assemblyStack.getItem();
            IBakedModel newModel = ModelLoader.instance().func_217845_a(assembly.getCADModel(assemblyStack, stack), ModelRotation.X0_Y0);
            return newModel;
        }
    };

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random random) {
        return original.getQuads(state, side, random);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return original.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return original.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return original.isBuiltInRenderer();
    }

    @Nonnull
    @Override
    public TextureAtlasSprite getParticleTexture() {
        return original.getParticleTexture();
    }

    @Nonnull
    @Override
    public ItemOverrideList getOverrides() {
        return itemHandler;
    }

}

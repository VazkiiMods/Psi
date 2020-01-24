package vazkii.psi.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADAssembly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ModelCAD implements IBakedModel {

    private final HashMap<Item, CADBakedModel> cache = new HashMap<Item, CADBakedModel>();

    private final ModelBakery bakery;
    private final IBakedModel original;

    public ModelCAD(ModelBakery bakery, IBakedModel original) {
        this.bakery = bakery;
        this.original = original;
    }

    //TODO Add Original model as the basic iron cad model and register this in client proxy


    private final ItemOverrideList itemHandler = new ItemOverrideList() {

        @Nullable
        @Override
        public IBakedModel getModelWithOverrides(IBakedModel model, ItemStack stack, @Nullable World world, @Nullable LivingEntity entity) {
            ICAD cad = (ICAD) stack.getItem();
            ItemStack assemblyStack = cad.getComponentInSlot(stack, EnumCADComponent.ASSEMBLY);
            if (assemblyStack.isEmpty()) {
                return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation("missingno"));
            }
            ICADAssembly assembly = (ICADAssembly) assemblyStack.getItem();
            return ModelCAD.this.getModel(stack, assembly.getCADModel(assemblyStack, stack), assembly.getCadTexture(assemblyStack, stack));
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

    private CADBakedModel getModel(ItemStack stack, ModelResourceLocation loc, ResourceLocation rl) {
        return cache.computeIfAbsent(stack.getItem(), p -> new CADBakedModel(bakery, stack, original, loc, rl));
    }

    private static class CADBakedModel extends BakedModelWrapper<IBakedModel> {

        private static final BlockModel MODEL_GENERATED = ObfuscationReflectionHelper.getPrivateValue(ModelBakery.class, null, "field_177606_o");

        private final List<BakedQuad> genQuads = new ArrayList<>();
        private final Map<Direction, List<BakedQuad>> faceQuads = new EnumMap<>(Direction.class);

        CADBakedModel(ModelBakery bakery, ItemStack cad, IBakedModel original, ModelResourceLocation cadmodel, ResourceLocation name) {
            super(original);

            IUnbakedModel cadUnbaked = bakery.getUnbakedModel(cadmodel);
            IBakedModel cadBaked;
            if (cadUnbaked instanceof BlockModel && ((BlockModel) cadUnbaked).getRootModel() == MODEL_GENERATED) {
                BlockModel bm = (BlockModel) cadUnbaked;
                cadBaked = new ItemModelGenerator()
                        .makeItemModel(ModelLoader.defaultTextureGetter(), bm)
                        .bake(bakery, bm, ModelLoader.defaultTextureGetter(), ModelRotation.X0_Y0, name);
            } else {
                cadBaked = cadUnbaked.bake(bakery, ModelLoader.defaultTextureGetter(), ModelRotation.X0_Y0, name);
            }

            for (Direction e : Direction.values())
                faceQuads.put(e, new ArrayList<>());

            Random rand = new Random(0);
            genQuads.addAll(cadBaked.getQuads(null, null, rand));

            for (Direction e : Direction.values()) {
                rand.setSeed(0);
                faceQuads.get(e).addAll(cadBaked.getQuads(null, e, rand));
            }

        }

        @Nonnull
        @Override
        public List<BakedQuad> getQuads(BlockState state, Direction face, @Nonnull Random rand) {
            return face == null ? genQuads : faceQuads.get(face);
        }

        @Nonnull
        @Override
        public IBakedModel handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType, MatrixStack stack) {
            super.handlePerspective(cameraTransformType, stack);
            return this;
        }

    }
}

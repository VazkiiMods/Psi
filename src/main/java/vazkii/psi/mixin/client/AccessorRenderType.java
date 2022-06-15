package vazkii.psi.mixin.client;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.class)
public interface AccessorRenderType {
    @Invoker("create")
    static RenderType.CompositeRenderType create(String string, VertexFormat vertexFormat,
                                                 VertexFormat.Mode mode, int bufSize, boolean hasCrumbling, boolean sortOnUpload,
                                                 RenderType.CompositeState compositeState) {
        throw new IllegalStateException();
    }
}
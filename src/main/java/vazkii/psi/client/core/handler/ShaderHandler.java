/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.handler;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

import java.io.IOException;

@EventBusSubscriber(modid = LibMisc.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ShaderHandler {

    private static ShaderInstance psiBarShader;

    @SubscribeEvent
    static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibResources.SHADER_PSI_BAR), DefaultVertexFormat.POSITION_TEX_COLOR),
                shader -> psiBarShader = shader
        );
    }

    public static ShaderInstance getPsiBarShader() {
        return psiBarShader;
    }

}

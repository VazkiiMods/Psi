/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.render.entity;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.entity.EntitySpellProjectile;

public class RenderSpellProjectile extends EntityRenderer<EntitySpellProjectile> {

	public RenderSpellProjectile(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public boolean shouldRender(@NotNull EntitySpellProjectile livingEntityIn, @NotNull Frustum camera, double camX, double camY, double camZ) {
		return false;
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull EntitySpellProjectile entity) {
		return PsiAPI.location("spell_projectile");
	}
}

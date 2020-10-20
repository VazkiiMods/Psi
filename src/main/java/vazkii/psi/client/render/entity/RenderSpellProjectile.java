/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.render.entity;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import vazkii.psi.common.entity.EntitySpellProjectile;

public class RenderSpellProjectile extends EntityRenderer<EntitySpellProjectile> {

	public RenderSpellProjectile(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public boolean shouldRender(EntitySpellProjectile livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
		return false;
	}

	@Override
	public ResourceLocation getEntityTexture(EntitySpellProjectile entity) {
		return null;
	}
}

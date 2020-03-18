package vazkii.psi.client.render.entity;

import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.common.entity.EntitySpellProjectile;

public class RenderSpellProjectile extends EntityRenderer<EntitySpellProjectile> {

	public RenderSpellProjectile(EntityRendererManager p_i46179_1_) {
		super(p_i46179_1_);
	}

	@Override
	public boolean shouldRender(EntitySpellProjectile p_225626_1_, ClippingHelperImpl p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
		return false;
	}

	@Override
	public ResourceLocation getEntityTexture(EntitySpellProjectile p_110775_1_) {
		return null;
	}
}

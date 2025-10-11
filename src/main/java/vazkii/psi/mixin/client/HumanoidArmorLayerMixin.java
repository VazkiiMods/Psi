package vazkii.psi.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.psi.common.item.armor.ItemPsimetalArmor;
import vazkii.psi.common.lib.LibResources;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

	public HumanoidArmorLayerMixin(RenderLayerParent<T, M> pRenderer) {
		super(pRenderer);
	}

	@Shadow
	protected abstract net.minecraft.client.model.Model getArmorModelHook(T entity, ItemStack itemStack, EquipmentSlot slot, A model);

	@Shadow
	protected abstract void renderModel(PoseStack p_289664_, MultiBufferSource p_289689_, int p_289681_, Model p_289658_, int p_350798_, ResourceLocation p_324344_);

	@Inject(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"))
	private void render(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot slot, int packedLight, A p_model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci, @Local ItemStack itemStack) {
		if(itemStack.getItem() instanceof ItemPsimetalArmor psiArmorItem) {
			net.minecraft.client.model.Model model = this.getArmorModelHook(livingEntity, itemStack, slot, p_model);
			this.renderModel(poseStack, bufferSource, packedLight, model, psiArmorItem.getColor(itemStack), LibResources.MODEL_PSIMETAL_EXOSUIT_SENSOR);
		}
	}
}

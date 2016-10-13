/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [20/02/2016, 22:41:34 (GMT)]
 */
package vazkii.psi.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import vazkii.arl.item.ModelModArmor;
import vazkii.psi.client.core.handler.ClientTickHandler;

public class ModelPsimetalExosuit extends ModelModArmor {

	public ModelRenderer helm;
	public ModelRenderer armR;
	public ModelRenderer armL;
	public ModelRenderer legR;
	public ModelRenderer legL;
	public ModelRenderer bootR;
	public ModelRenderer bootL;
	public ModelRenderer body;
	public ModelRenderer helm_1;
	public ModelRenderer helm_2;
	public ModelRenderer sensor;
	public ModelRenderer sensorcolor;
	public ModelRenderer armRpauldron;
	public ModelRenderer armLpauldron;
	public ModelRenderer body2;
	public ModelRenderer body3;

	int slot;

	public ModelPsimetalExosuit(int slot) {
		this.slot = slot;

		textureWidth = 64;
		textureHeight = 128;
		float s = 0.255F;

		sensorcolor = new ModelRenderer(this, 38, 40);
		sensorcolor.mirror = true;
		sensorcolor.setRotationPoint(0.0F, 0.0F, 0.0F);
		sensorcolor.addBox(4.0F, -6.5F, -1.0F, 1, 2, 3, s);
		setRotateAngle(sensorcolor, 0.0F, 0.0F, 0.08726646259971647F);
		body = new ModelRenderer(this, 0, 51);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addBox(-4.5F, 0.0F, -3.0F, 9, 7, 6, s);
		helm = new ModelRenderer(this, 0, 32);
		helm.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm.addBox(-4.5F, -8.5F, -4.5F, 9, 9, 10, s);
		setRotateAngle(helm, 0.08726646259971647F, 0.0F, 0.0F);
		armL = new ModelRenderer(this, 0, 85);
		armL.mirror = true;
		armL.setRotationPoint(5.0F, 2.0F, -0.0F);
		armL.addBox(0.0F, 6.5F, -2.0F, 3, 4, 4, s);
		body2 = new ModelRenderer(this, 30, 51);
		body2.setRotationPoint(0.0F, 0.0F, 0.0F);
		body2.addBox(-2.5F, 6.0F, -2.0F, 5, 5, 4, s);
		armR = new ModelRenderer(this, 0, 85);
		armR.setRotationPoint(-5.0F, 2.0F, 0.0F);
		armR.addBox(-3.0F, 6.5F, -2.0F, 3, 4, 4, s);
		legR = new ModelRenderer(this, 16, 73);
		legR.setRotationPoint(-1.9F, 12.0F, 0.0F);
		legR.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, s);
		sensor = new ModelRenderer(this, 38, 32);
		sensor.mirror = true;
		sensor.setRotationPoint(0.0F, 0.0F, 0.0F);
		sensor.addBox(4.0F, -7.0F, -2.0F, 1, 3, 5, s);
		armRpauldron = new ModelRenderer(this, 0, 73);
		armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
		armRpauldron.addBox(-3.5F, -2.0F, -2.0F, 3, 7, 4, s);
		setRotateAngle(armRpauldron, 0.0F, 0.0F, 0.17453292519943295F);
		armLpauldron = new ModelRenderer(this, 0, 73);
		armLpauldron.mirror = true;
		armLpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
		armLpauldron.addBox(0.5F, -2.0F, -2.0F, 3, 7, 4, s);
		setRotateAngle(armLpauldron, 0.0F, 0.0F, -0.17453292519943295F);
		helm_1 = new ModelRenderer(this, 0, 32);
		helm_1.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm_1.addBox(-5.0F, -4.0F, 0.5F, 1, 3, 3, s);
		setRotateAngle(helm_1, 0.0F, 0.0F, -0.08726646259971647F);
		helm_2 = new ModelRenderer(this, 0, 32);
		helm_2.mirror = true;
		helm_2.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm_2.addBox(4.0F, -4.0F, 0.5F, 1, 3, 3, s);
		setRotateAngle(helm_2, 0.0F, 0.0F, 0.08726646259971647F);
		body3 = new ModelRenderer(this, 0, 64);
		body3.setRotationPoint(0.0F, 0.0F, 0.0F);
		body3.addBox(-4.0F, 10.0F, -2.5F, 8, 4, 5, s);
		bootR = new ModelRenderer(this, 16, 85);
		bootR.setRotationPoint(-1.9F, 12.0F, 0.0F);
		bootR.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, s);
		legL = new ModelRenderer(this, 16, 73);
		legL.mirror = true;
		legL.setRotationPoint(1.9F, 12.0F, 0.0F);
		legL.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, s);
		bootL = new ModelRenderer(this, 16, 85);
		bootL.mirror = true;
		bootL.setRotationPoint(1.9F, 12.0F, 0.0F);
		bootL.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, s);

		sensor.addChild(sensorcolor);
		body.addChild(body2);
		helm.addChild(sensor);
		armR.addChild(armRpauldron);
		armL.addChild(armLpauldron);
		helm.addChild(helm_1);
		helm.addChild(helm_2);
		body.addChild(body3);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		if(entity instanceof EntityArmorStand) {
			f = 0;
			f1 = 0;
			f2 = 0;
			f3 = 0;
			f4 = 0;
		}
		
		helm.showModel = slot == 3;
		body.showModel = slot == 2;
		armR.showModel = slot == 2;
		armL.showModel = slot == 2;
		legR.showModel = slot == 1;
		legL.showModel = slot == 1;
		bootL.showModel = slot == 0;
		bootR.showModel = slot == 0;
		bipedHeadwear.showModel = false;

		setModelParts();
		prepareForRender(entity, ClientTickHandler.partialTicks);
		super.render(entity, f, f1, f2, f3, f4, f5);
	}
	
	@Override
	public void setModelParts() {
		bipedHead = helm;
		bipedBody = body;
		bipedRightArm = armR;
		bipedLeftArm = armL;
		if(slot == 1) {
			bipedRightLeg = legR;
			bipedLeftLeg = legL;
		} else {
			bipedRightLeg = bootR;
			bipedLeftLeg = bootL;
		}
	}

}

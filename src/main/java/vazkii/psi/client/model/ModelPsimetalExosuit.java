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
import vazkii.arl.util.ClientTicker;

public class ModelPsimetalExosuit extends ModelModArmor {

	private final ModelRenderer helmAnchor;
	private final ModelRenderer helm;
	private final ModelRenderer helmDetailR;
	private final ModelRenderer helmDetailL;

	private final ModelRenderer bodyAnchor;
	private final ModelRenderer body;

	private final ModelRenderer armLAnchor;
	private final ModelRenderer armL;
	private final ModelRenderer armLpauldron;
	
	private final ModelRenderer armRAnchor;
	private final ModelRenderer armR;
	private final ModelRenderer armRpauldron;

	private final ModelRenderer beltAnchor;
	private final ModelRenderer pantsAnchor;
	private final ModelRenderer belt;
	private final ModelRenderer legL;
	private final ModelRenderer legR;

	private final ModelRenderer bootL;
	private final ModelRenderer bootR;
	
	private final ModelRenderer sensor;
	private final ModelRenderer sensorColor;
	
	int slot = 0;

	public ModelPsimetalExosuit(int slot) {
		this.slot = slot;

		textureWidth = 64;
		textureHeight = 128;
		float s = 0.01F;

		//helm
		helmAnchor = new ModelRenderer(this, 0, 0);
		helmAnchor.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm = new ModelRenderer(this, 0, 0);
		helm.setRotationPoint(0.0F, 0.0F, 0.0F);
		helm.addBox(-4.5F, -9.0F, -5.0F, 9, 9, 10, 0.0F);
		helmDetailL = new ModelRenderer(this, 0, 0);
		helmDetailL.mirror = true;
		helmDetailL.setRotationPoint(0.0F, 0.0F, 0.0F);
		helmDetailL.addBox(4.5F, -5.0F, 0.0F, 1, 3, 3, 0.0F);
		helmDetailR = new ModelRenderer(this, 0, 0);
		helmDetailR.setRotationPoint(0.0F, 0.0F, 0.0F);
		helmDetailR.addBox(-5.5F, -5.0F, 0.0F, 1, 3, 3, 0.0F);

		//sensor
		sensor = new ModelRenderer(this, 38, 0);
		sensor.mirror = true;
		sensor.setRotationPoint(0.0F, 0.0F, 0.0F);
		sensor.addBox(4.5F, -8.0F, -2.0F, 1, 3, 5, 0.0F);
		sensorColor = new ModelRenderer(this, 38, 8);
		sensorColor.mirror = true;
		sensorColor.setRotationPoint(0.0F, 0.0F, 0.0F);
		sensorColor.addBox(4.51F, -7.0F, -1.0F, 1, 2, 3, 0.0F);

		//body
		bodyAnchor = new ModelRenderer(this, 0, 0);
		bodyAnchor.setRotationPoint(0.0F, 0.0F, 0.0F);
		body = new ModelRenderer(this, 0, 19);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addBox(-4.5F, -0.5F, -3.0F, 9, 7, 6, s);

	 	//armL
		armLAnchor = new ModelRenderer(this, 0, 0);
		armLAnchor.mirror = true;
		armLAnchor.setRotationPoint(4.0F, 2.0F, 0.0F);
		armL = new ModelRenderer(this, 0, 44);
		armL.mirror = true;
		armL.setRotationPoint(0.0F, 0.0F, 0.0F);
		armL.addBox(0.5F, 6.0F, -2.5F, 3, 4, 5, s);
		armLpauldron = new ModelRenderer(this, 0, 32);
		armLpauldron.mirror = true;
		armLpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
		armLpauldron.addBox(1.0F, -2.5F, -2.5F, 3, 7, 5, s);
		setRotateAngle(armLpauldron, 0.0F, 0.0F, -0.17453292519943295F);

		//armR
		armRAnchor = new ModelRenderer(this, 0, 0);
		armRAnchor.mirror = true;
		armRAnchor.setRotationPoint(-4.0F, 2.0F, 0.0F);
		armR = new ModelRenderer(this, 0, 44);
		armR.setRotationPoint(0.0F, 0.0F, 0.0F);
		armR.addBox(-3.5F, 6.0F, -2.51F, 3, 4, 5, s);
		armRpauldron = new ModelRenderer(this, 0, 32);
		armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
		armRpauldron.addBox(-4.0F, -2.5F, -2.5F, 3, 7, 5, s);
		setRotateAngle(armRpauldron, 0.0F, 0.0F, 0.17453292519943295F);

		//pants
		beltAnchor = new ModelRenderer(this, 0, 0);
		beltAnchor.setRotationPoint(0.0F, 0.0F, 0.0F);
		pantsAnchor = new ModelRenderer(this, 0, 0);
		pantsAnchor.setRotationPoint(0.0F, 0.0F, 0.0F);
		belt = new ModelRenderer(this, 0, 53);
		belt.setRotationPoint(0.0F, 0.0F, 0.0F);
		belt.addBox(-4.5F, 8.0F, -3.0F, 9, 5, 6, 0.0F);
		legL = new ModelRenderer(this, 0, 64);
		legL.mirror = true;
		legL.setRotationPoint(1.9F, 12.0F, 0.0F);
		legL.addBox(-1.39F, 1.0F, -2.49F, 4, 5, 5, 0.0F);
		legR = new ModelRenderer(this, 0, 64);
		legR.setRotationPoint(-1.9F, 12.0F, 0.0F);
		legR.addBox(-2.61F, 1.0F, -2.51F, 4, 5, 5, 0.0F);

		//boots
		bootL = new ModelRenderer(this, 0, 74);
		bootL.mirror = true;
		bootL.setRotationPoint(1.9F, 12.0F, 0.0F);
		bootL.addBox(-2.39F, 8.0F, -2.49F, 5, 4, 5, 0.0F);
		bootR = new ModelRenderer(this, 0, 74);
		bootR.setRotationPoint(-1.9F, 12.0F, 0.0F);
		bootR.addBox(-2.61F, 8.0F, -2.51F, 5, 4, 5, 0.0F);

		//hierarchy
		helmAnchor.addChild(helm);
		helm.addChild(sensor);
		helm.addChild(helmDetailL);
		helm.addChild(helmDetailR);
		sensor.addChild(sensorColor);

		bodyAnchor.addChild(body);

		armLAnchor.addChild(armL);
		armL.addChild(armLpauldron);
		armRAnchor.addChild(armR);
		armR.addChild(armRpauldron);

		beltAnchor.addChild(belt);
		pantsAnchor.addChild(legL);
		pantsAnchor.addChild(legR);

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
		belt.showModel = slot == 1;
		bootL.showModel = slot == 0;
		bootR.showModel = slot == 0;
		bipedHeadwear.showModel = false;

		setModelParts();
		prepareForRender(entity, ClientTicker.partialTicks);
		super.render(entity, f, f1, f2, f3, f4, f5);
	}
	
	@Override
	public void setModelParts() {
		bipedHead = helmAnchor;
		bipedBody = bodyAnchor;
		bipedRightArm = armRAnchor;
		bipedLeftArm = armLAnchor;
		if(slot == 1) {
			bipedBody = beltAnchor;
			bipedRightLeg = legR;
			bipedLeftLeg = legL;
		} else {
			bipedRightLeg = bootR;
			bipedLeftLeg = bootL;
		}
	}	
}

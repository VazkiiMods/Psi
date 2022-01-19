/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;

public class ModelPsimetalExosuit extends ModelArmor {

	private final ModelPart helm;
	private final ModelPart helmDetailR;
	private final ModelPart helmDetailL;

	private final ModelPart bodyAnchor;
	private final ModelPart body;

	private final ModelPart armL;
	private final ModelPart armLpauldron;

	private final ModelPart armR;
	private final ModelPart armRpauldron;

	private final ModelPart beltAnchor;
	private final ModelPart pantsAnchor;
	private final ModelPart belt;
	private final ModelPart legL;
	private final ModelPart legR;

	private final ModelPart bootL;
	private final ModelPart bootR;

	private final ModelPart sensor;
	private final ModelPart sensorColor;

	public ModelPsimetalExosuit(EquipmentSlot slot) {
		super(slot);

		texWidth = 64;
		texHeight = 128;
		float s = 0.01F;

		//helm
		head = new ModelPart(this, 0, 0);
		head.setPos(0.0F, 0.0F, 0.0F);
		helm = new ModelPart(this, 0, 0);
		helm.setPos(0.0F, 0.0F, 0.0F);
		helm.addBox(-4.5F, -9.0F, -5.0F, 9, 9, 10, 0.0F);
		helmDetailL = new ModelPart(this, 0, 0);
		helmDetailL.mirror = true;
		helmDetailL.setPos(0.0F, 0.0F, 0.0F);
		helmDetailL.addBox(4.5F, -5.0F, 0.0F, 1, 3, 3, 0.0F);
		helmDetailR = new ModelPart(this, 0, 0);
		helmDetailR.setPos(0.0F, 0.0F, 0.0F);
		helmDetailR.addBox(-5.5F, -5.0F, 0.0F, 1, 3, 3, 0.0F);

		//sensor
		sensor = new ModelPart(this, 38, 0);
		sensor.mirror = true;
		sensor.setPos(0.0F, 0.0F, 0.0F);
		sensor.addBox(4.5F, -8.0F, -2.0F, 1, 3, 5, 0.0F);
		sensorColor = new ModelPart(this, 38, 8);
		sensorColor.mirror = true;
		sensorColor.setPos(0.0F, 0.0F, 0.0F);
		sensorColor.addBox(4.51F, -7.0F, -1.0F, 1, 2, 3, 0.0F);

		//body
		bodyAnchor = new ModelPart(this, 0, 0);
		bodyAnchor.setPos(0.0F, 0.0F, 0.0F);
		body = new ModelPart(this, 0, 19);
		body.setPos(0.0F, 0.0F, 0.0F);
		body.addBox(-4.5F, -0.5F, -3.0F, 9, 7, 6, s);

		//armL
		leftArm = new ModelPart(this, 0, 0);
		leftArm.mirror = true;
		leftArm.setPos(4.0F, 2.0F, 0.0F);
		armL = new ModelPart(this, 0, 44);
		armL.mirror = true;
		armL.setPos(0.0F, 0.0F, 0.0F);
		armL.addBox(0.5F, 6.0F, -2.5F, 3, 4, 5, s);
		armLpauldron = new ModelPart(this, 0, 32);
		armLpauldron.mirror = true;
		armLpauldron.setPos(0.0F, 0.0F, 0.0F);
		armLpauldron.addBox(1.0F, -2.5F, -2.5F, 3, 7, 5, s);
		setRotateAngle(armLpauldron, 0.0F, 0.0F, -0.17453292519943295F);

		//armR
		rightArm = new ModelPart(this, 0, 0);
		rightArm.mirror = true;
		rightArm.setPos(-4.0F, 2.0F, 0.0F);
		armR = new ModelPart(this, 0, 44);
		armR.setPos(0.0F, 0.0F, 0.0F);
		armR.addBox(-3.5F, 6.0F, -2.51F, 3, 4, 5, s);
		armRpauldron = new ModelPart(this, 0, 32);
		armRpauldron.setPos(0.0F, 0.0F, 0.0F);
		armRpauldron.addBox(-4.0F, -2.5F, -2.5F, 3, 7, 5, s);
		setRotateAngle(armRpauldron, 0.0F, 0.0F, 0.17453292519943295F);

		//pants
		beltAnchor = new ModelPart(this, 0, 0);
		beltAnchor.setPos(0.0F, 0.0F, 0.0F);
		pantsAnchor = new ModelPart(this, 0, 0);
		pantsAnchor.setPos(0.0F, 0.0F, 0.0F);
		belt = new ModelPart(this, 0, 53);
		belt.setPos(0.0F, 0.0F, 0.0F);
		belt.addBox(-4.5F, 8.0F, -3.0F, 9, 5, 6, 0.0F);
		legL = new ModelPart(this, 0, 64);
		legL.mirror = true;
		legL.setPos(1.9F, 12.0F, 0.0F);
		legL.addBox(-1.39F, 1.0F, -2.49F, 4, 5, 5, 0.0F);
		legR = new ModelPart(this, 0, 64);
		legR.setPos(-1.9F, 12.0F, 0.0F);
		legR.addBox(-2.61F, 1.0F, -2.51F, 4, 5, 5, 0.0F);

		//boots
		bootL = new ModelPart(this, 0, 74);
		bootL.mirror = true;
		bootL.setPos(1.9F, 12.0F, 0.0F);
		bootL.addBox(-2.39F, 8.0F, -2.49F, 5, 4, 5, 0.0F);
		bootR = new ModelPart(this, 0, 74);
		bootR.setPos(-1.9F, 12.0F, 0.0F);
		bootR.addBox(-2.61F, 8.0F, -2.51F, 5, 4, 5, 0.0F);

		//hierarchy
		head.addChild(helm);
		helm.addChild(sensor);
		helm.addChild(helmDetailL);
		helm.addChild(helmDetailR);
		sensor.addChild(sensorColor);

		bodyAnchor.addChild(body);

		leftArm.addChild(armL);
		armL.addChild(armLpauldron);
		rightArm.addChild(armR);
		armR.addChild(armRpauldron);

		beltAnchor.addChild(belt);
		pantsAnchor.addChild(legL);
		pantsAnchor.addChild(legR);

	}

	@Override
	public void renderToBuffer(PoseStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
		helm.visible = slot == EquipmentSlot.HEAD;
		body.visible = slot == EquipmentSlot.CHEST;
		armR.visible = slot == EquipmentSlot.CHEST;
		armL.visible = slot == EquipmentSlot.CHEST;
		belt.visible = slot == EquipmentSlot.LEGS;
		bootL.visible = slot == EquipmentSlot.FEET;
		bootR.visible = slot == EquipmentSlot.FEET;
		hat.visible = false;
		body = bodyAnchor;
		if (slot == EquipmentSlot.LEGS) {
			body = beltAnchor;
			rightLeg = legR;
			leftLeg = legL;
		} else {
			rightLeg = bootR;
			leftLeg = bootL;
		}
		super.renderToBuffer(ms, buffer, light, overlay, r, g, b, a);
	}

}

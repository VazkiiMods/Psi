/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class ModelPsimetalExosuit  {

	public static MeshDefinition createInsideMesh() {
		var deformation = new CubeDeformation(0.01F);
		var mesh = new MeshDefinition();
		var root = mesh.getRoot();
		root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
		var body = root.addOrReplaceChild("body", CubeListBuilder.create()
				.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, deformation), PartPose.ZERO);
		var belt = body.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(0, 53)
				.addBox(-4.5F, 8.0F, -3.0F, 9, 5, 6), PartPose.ZERO);
		var legL = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 64)
				.addBox(-1.39F, 1.0F, -2.49F, 4, 5, 5), PartPose.ZERO);
		var legR = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 64)
				.addBox(-2.61F, 1.0F, -2.51F, 4, 5, 5), PartPose.ZERO);
		return mesh;
	}

	public static MeshDefinition createOutsideMesh() {

		var deformation = new CubeDeformation(0.01F);
		var mesh = new MeshDefinition();
		var root = mesh.getRoot();
		var head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
				.mirror(), PartPose.ZERO);
		var helm = head.addOrReplaceChild("helm", CubeListBuilder.create().texOffs(0, 0)
				.mirror()
				.addBox(-4.5F, -9.0F, -5.0F, 9, 9, 10), PartPose.ZERO);
		var helmDetailL = helm.addOrReplaceChild("helmDetailL", CubeListBuilder.create().texOffs(0, 0)
				.addBox(4.5F, -5.0F, 0.0F, 1, 3, 3), PartPose.ZERO);
		var helmDetailR = helm.addOrReplaceChild("helmDetailR", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-5.5F, -5.0F, 0.0F, 1, 3, 3), PartPose.ZERO);
		var sensor = helm.addOrReplaceChild("sensor", CubeListBuilder.create().texOffs(38, 0)
				.mirror()
				.addBox(4.5F, -8.0F, -2.0F, 1, 3, 5), PartPose.ZERO);
		var sensorColor = helm.addOrReplaceChild("sensorColor", CubeListBuilder.create().texOffs(38, 8)
				.mirror()
				.addBox(4.51F, -7.01F, -1.0F, 1, 2, 3), PartPose.ZERO);
		var body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19)
				.addBox(-4.5F, -0.5F, -3.0F, 9, 7, 6, deformation), PartPose.ZERO);
		var leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 0)
				.mirror(), PartPose.ZERO);
		var armL = leftArm.addOrReplaceChild("armL", CubeListBuilder.create().texOffs(0, 44)
				.mirror()
				.addBox(0.5F, 6.0F, -2.5F, 3, 4, 5, deformation), PartPose.ZERO);
		var armLpauldron = armL.addOrReplaceChild("armLpauldron", CubeListBuilder.create().texOffs(0, 32)
						.mirror()
						.addBox(1.0F, -2.5F, -2.5F, 3, 7, 5, deformation),
				PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0F, 0.0F, -0.17453292519943295F));
		var rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 0)
				.mirror(), PartPose.ZERO);
		var armR = rightArm.addOrReplaceChild("armR", CubeListBuilder.create().texOffs(0, 44)
				.addBox(-3.5F, 6.0F, -2.51F, 3, 4, 5, deformation), PartPose.ZERO);
		var armRpauldron = armR.addOrReplaceChild("armRpauldron", CubeListBuilder.create().texOffs(0, 32)
						.addBox(-4.0F, -2.5F, -2.5F, 3, 7, 5, deformation),
				PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0F, 0.0F, 0.17453292519943295F));

		var belt = root.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(0, 53)
				.addBox(-4.5F, 8.0F, -3.0F, 9, 5, 6), PartPose.ZERO);
		var legL = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 64)
				.mirror()
				.addBox(-1.39F, 1.0F, -2.49F, 4, 5, 5), PartPose.ZERO);
		var legR = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 64)
				.addBox(-2.61F, 1.0F, -2.51F, 4, 5, 5), PartPose.ZERO);
		var bootL = legL.addOrReplaceChild("bootL", CubeListBuilder.create().texOffs(0, 74)
				.mirror()
				.addBox(-2.39F, 8.0F, -2.49F, 5, 4, 5), PartPose.ZERO);
		var bootR = legR.addOrReplaceChild("bootR", CubeListBuilder.create().texOffs(0, 74)
				.addBox(-2.61F, 8.0F, -2.51F, 5, 4, 5), PartPose.ZERO);

		root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		return mesh;
	}

}

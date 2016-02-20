/*package model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * armor_exosuit - wiiv
 * Created using Tabula 4.1.1
 */
 
 /*
public class armorExosuit extends ModelBase {
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

    public armorExosuit() {
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.sensorcolor = new ModelRenderer(this, 38, 40);
        this.sensorcolor.mirror = true;
        this.sensorcolor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.sensorcolor.addBox(4.0F, -6.5F, -1.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(sensorcolor, 0.0F, 0.0F, 0.08726646259971647F);
        this.body = new ModelRenderer(this, 0, 51);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.5F, 0.0F, -3.0F, 9, 7, 6, 0.0F);
        this.helm = new ModelRenderer(this, 0, 32);
        this.helm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm.addBox(-4.5F, -8.0F, -4.5F, 9, 9, 10, 0.0F);
        this.setRotateAngle(helm, 0.08726646259971647F, 0.0F, 0.0F);
        this.armL = new ModelRenderer(this, 0, 85);
        this.armL.mirror = true;
        this.armL.setRotationPoint(5.0F, 2.0F, -0.0F);
        this.armL.addBox(0.0F, 5.5F, -2.0F, 3, 4, 4, 0.0F);
        this.body2 = new ModelRenderer(this, 30, 51);
        this.body2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body2.addBox(-2.5F, 6.0F, -2.0F, 5, 5, 4, 0.0F);
        this.armR = new ModelRenderer(this, 0, 85);
        this.armR.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.armR.addBox(-3.0F, 5.5F, -2.0F, 3, 4, 4, 0.0F);
        this.legR = new ModelRenderer(this, 16, 73);
        this.legR.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.legR.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        this.sensor = new ModelRenderer(this, 38, 32);
        this.sensor.mirror = true;
        this.sensor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.sensor.addBox(4.0F, -7.0F, -2.0F, 1, 3, 5, 0.0F);
        this.armRpauldron = new ModelRenderer(this, 0, 73);
        this.armRpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRpauldron.addBox(-3.5F, -1.5F, -2.0F, 3, 7, 4, 0.0F);
        this.setRotateAngle(armRpauldron, 0.0F, 0.0F, 0.17453292519943295F);
        this.armLpauldron = new ModelRenderer(this, 0, 73);
        this.armLpauldron.mirror = true;
        this.armLpauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armLpauldron.addBox(0.5F, -1.5F, -2.0F, 3, 7, 4, 0.0F);
        this.setRotateAngle(armLpauldron, 0.0F, 0.0F, -0.17453292519943295F);
        this.helm_1 = new ModelRenderer(this, 0, 32);
        this.helm_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm_1.addBox(-5.0F, -4.0F, 0.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(helm_1, 0.0F, 0.0F, -0.08726646259971647F);
        this.helm_2 = new ModelRenderer(this, 0, 32);
        this.helm_2.mirror = true;
        this.helm_2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helm_2.addBox(4.0F, -4.0F, 0.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(helm_2, 0.0F, 0.0F, 0.08726646259971647F);
        this.body3 = new ModelRenderer(this, 0, 64);
        this.body3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body3.addBox(-4.0F, 10.0F, -2.5F, 8, 4, 5, 0.0F);
        this.bootR = new ModelRenderer(this, 16, 85);
        this.bootR.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.bootR.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, 0.0F);
        this.legL = new ModelRenderer(this, 16, 73);
        this.legL.mirror = true;
        this.legL.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.legL.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        this.bootL = new ModelRenderer(this, 16, 85);
        this.bootL.mirror = true;
        this.bootL.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.bootL.addBox(-2.0F, 8.0F, -3.0F, 4, 4, 5, 0.0F);
        this.sensor.addChild(this.sensorcolor);
        this.body.addChild(this.body2);
        this.helm.addChild(this.sensor);
        this.armR.addChild(this.armRpauldron);
        this.armL.addChild(this.armLpauldron);
        this.helm.addChild(this.helm_1);
        this.helm.addChild(this.helm_2);
        this.body.addChild(this.body3);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.body.render(f5);
        this.helm.render(f5);
        this.armL.render(f5);
        this.armR.render(f5);
        this.legR.render(f5);
        this.bootR.render(f5);
        this.legL.render(f5);
        this.bootL.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     *//*
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
*/

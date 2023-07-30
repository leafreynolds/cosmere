/*
 * File updated ~ 19 - 7 - 2023 ~ Leaf
 */

// Made with Blockbench 4.6.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

package leaf.cosmere.hemalurgy.client.render.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public class KolossLargeModel<T extends LivingEntity> extends HumanoidModel<T>
{
	private final ModelPart root;

	public KolossLargeModel(ModelPart root)
	{
		super(root);
		this.root = root;
	}

	@Override
	protected Iterable<ModelPart> headParts()
	{
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> bodyParts()
	{
		return ImmutableList.of(this.root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(-10.0F, -6.0784F, 3.8832F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(56, 158).addBox(-5.9375F, -10.0F, -6.75F, 12.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(52, 0).addBox(-4.0625F, 2.0F, -3.75F, 8.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0552F, -53.7473F, -0.678F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(110, 24).addBox(-11.9927F, -47.1152F, -2.1903F, 24.0F, 10.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(94, 77).addBox(-34.5F, 10.9375F, -7.5625F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(72, 24).addBox(-34.5F, -13.9375F, -7.5625F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0073F, -36.1727F, 3.2478F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition body_r2 = body.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-35.9375F, -0.75F, -7.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -36.1727F, 2.3941F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition body_r3 = body.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(52, 11).addBox(-30.25F, -2.0F, -24.5625F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -36.1727F, 20.1794F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition body_r4 = body.addOrReplaceChild("body_r4", CubeListBuilder.create().texOffs(128, 48).addBox(2.5F, -14.0F, -8.0F, 8.0F, 28.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-13.5F, -17.0F, -10.0F, 16.0F, 34.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0073F, -62.4227F, 3.2478F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition body_r5 = body.addOrReplaceChild("body_r5", CubeListBuilder.create().texOffs(72, 0).addBox(-12.0F, 10.5F, -8.0F, 24.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0073F, -64.4227F, 3.2478F, 0.1309F, 0.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(17.0F, -45.0F, 2.0F));

		PartDefinition left_arm_r1 = left_arm.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(149, 119).addBox(-2.0F, -7.2903F, -6.8003F, 14.0F, 12.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0073F, 0.5773F, -0.7522F, 0.1309F, 0.0F, 0.0F));

		PartDefinition left_arm_r2 = left_arm.addOrReplaceChild("left_arm_r2", CubeListBuilder.create().texOffs(0, 100).addBox(-1.5299F, 0.4276F, -6.0625F, 12.0F, 23.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0073F, 0.5773F, -0.7522F, 0.0F, 0.0F, -0.2182F));

		PartDefinition left_arm_r3 = left_arm.addOrReplaceChild("left_arm_r3", CubeListBuilder.create().texOffs(0, 54).addBox(-2.5299F, 20.638F, -2.2242F, 14.0F, 23.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0073F, 0.5773F, -0.7522F, -0.2182F, 0.0F, -0.2182F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-17.0F, -45.0F, 2.0F));

		PartDefinition right_arm_r1 = right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(194, 6).addBox(-1.1716F, -64.9073F, 4.4307F, 4.0F, 4.75F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-1.1716F, -64.9073F, -8.0772F, 4.0F, 4.75F, 12.75F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-2.8284F, -64.9073F, -8.0772F, 4.0F, 4.75F, 12.75F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-2.8284F, -64.9073F, 4.4307F, 4.0F, 4.75F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-1.1716F, -60.5482F, -10.6011F, 4.0F, 5.75F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-2.8284F, -60.5482F, -10.6011F, 4.0F, 5.75F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-2.8284F, -60.5482F, 6.9547F, 4.0F, 1.75F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-1.1716F, -60.5482F, 6.9547F, 4.0F, 1.75F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-2.8284F, -60.5482F, -14.6011F, 4.0F, 1.75F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-1.1716F, -60.5482F, -14.6011F, 4.0F, 1.75F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(211, 117).addBox(-3.1419F, 61.7823F, -5.2961F, 6.0F, 1.5F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(211, 117).addBox(-3.1419F, 61.7823F, 0.7961F, 6.0F, 1.5F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(211, 117).addBox(-5.438F, 61.7823F, -3.0F, 10.5F, 1.5F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(211, 117).addBox(0.6542F, 61.7823F, -3.0F, 4.5F, 1.5F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(51, 221).addBox(-2.5F, 32.2391F, -3.0F, 5.0F, 24.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-1.1716F, -55.2609F, -10.0F, 4.0F, 80.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(188, 190).addBox(-6.0F, 27.4412F, -6.8296F, 12.0F, 1.0F, 19.0F, new CubeDeformation(0.0F))
				.texOffs(188, 190).addBox(-6.0F, 27.2445F, -4.7739F, 12.0F, 5.0F, 12.25F, new CubeDeformation(0.0F))
				.texOffs(188, 190).addBox(-6.0F, 27.2445F, -7.4761F, 12.0F, 5.0F, 3.25F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-2.8284F, -55.2609F, -10.0F, 4.0F, 80.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(80, 195).addBox(-6.0F, 23.8854F, -14.0F, 12.0F, 4.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 22.2609F, -45.0F, 1.2654F, 0.0F, 0.0F));

		PartDefinition right_arm_r2 = right_arm.addOrReplaceChild("right_arm_r2", CubeListBuilder.create().texOffs(194, 6).addBox(-2.8284F, -53.1386F, 32.4895F, 4.0F, 3.25F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-1.1716F, -53.1386F, 32.4895F, 4.0F, 3.25F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(188, 190).addBox(-6.0F, 27.0867F, -17.514F, 12.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 22.2609F, -45.0F, 2.0508F, 0.0F, 0.0F));

		PartDefinition right_arm_r3 = right_arm.addOrReplaceChild("right_arm_r3", CubeListBuilder.create().texOffs(194, 6).addBox(-2.8284F, -63.0576F, 13.3766F, 4.0F, 4.75F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-1.1716F, -63.0576F, 13.3766F, 4.0F, 4.75F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-1.1716F, -59.91F, 9.0114F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-2.8284F, -59.91F, 9.0114F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(211, 117).addBox(-3.1419F, 53.1062F, -21.7501F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(188, 190).addBox(-6.0F, 30.1203F, -1.737F, 12.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 22.2609F, -45.0F, 1.6581F, 0.0F, 0.0F));

		PartDefinition right_arm_r4 = right_arm.addOrReplaceChild("right_arm_r4", CubeListBuilder.create().texOffs(194, 6).addBox(-1.1716F, -63.1929F, -17.05F, 4.0F, 4.75F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-2.8284F, -63.1929F, -17.05F, 4.0F, 4.75F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-1.1716F, -60.0453F, -12.6848F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-2.8284F, -60.0453F, -12.6848F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(211, 117).addBox(-3.1419F, 53.1062F, 18.7501F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(188, 190).addBox(-6.0F, 30.1203F, -2.263F, 12.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 22.2609F, -45.0F, 0.8727F, 0.0F, 0.0F));

		PartDefinition right_arm_r5 = right_arm.addOrReplaceChild("right_arm_r5", CubeListBuilder.create().texOffs(194, 6).addBox(-1.1716F, -53.3886F, -36.2395F, 4.0F, 4.25F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(-2.8284F, -53.3886F, -36.2395F, 4.0F, 4.25F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(188, 190).addBox(-6.0F, 25.0867F, 9.514F, 12.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(188, 190).addBox(-6.0F, 9.514F, 25.0867F, 12.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(188, 190).addBox(-6.0F, 27.0867F, 9.514F, 12.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 22.2609F, -45.0F, 0.48F, 0.0F, 0.0F));

		PartDefinition right_arm_r6 = right_arm.addOrReplaceChild("right_arm_r6", CubeListBuilder.create().texOffs(211, 117).addBox(-21.8812F, 53.0519F, -3.0F, 3.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 22.2609F, -45.0F, 1.2419F, 0.3736F, -0.1239F));

		PartDefinition right_arm_r7 = right_arm.addOrReplaceChild("right_arm_r7", CubeListBuilder.create().texOffs(211, 117).addBox(18.619F, 53.1605F, -3.0F, 3.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 22.2609F, -45.0F, 1.2419F, -0.3736F, 0.1239F));

		PartDefinition right_arm_r8 = right_arm.addOrReplaceChild("right_arm_r8", CubeListBuilder.create().texOffs(194, 6).addBox(-9.0711F, -59.2609F, 5.0711F, 4.0F, 84.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(194, 6).addBox(5.0711F, -59.2609F, -9.0711F, 4.0F, 84.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 22.2609F, -45.0F, 1.3514F, 0.2143F, 0.7617F));

		PartDefinition right_arm_r9 = right_arm.addOrReplaceChild("right_arm_r9", CubeListBuilder.create().texOffs(64, 36).addBox(-7.75F, -16.5F, -11.9375F, 14.0F, 23.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.9925F, 37.1806F, 0.6853F, -0.2182F, 0.0F, 0.2182F));

		PartDefinition right_arm_r10 = right_arm.addOrReplaceChild("right_arm_r10", CubeListBuilder.create().texOffs(54, 120).addBox(-9.25F, -23.0F, -9.0625F, 12.0F, 23.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3052F, 23.0773F, 2.2478F, 0.0F, 0.0F, 0.2182F));

		PartDefinition right_arm_r11 = right_arm.addOrReplaceChild("right_arm_r11", CubeListBuilder.create().texOffs(148, 154).addBox(-34.0F, -13.5F, -8.0F, 14.0F, 12.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.0073F, 6.5773F, 1.2478F, 0.1309F, 0.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(94, 97).addBox(-7.2177F, 18.3554F, -9.2339F, 15.0F, 20.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(142, 92).addBox(-6.2177F, 17.2404F, -7.9641F, 13.0F, 5.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(10.5645F, -14.9826F, 3.1852F));

		PartDefinition left_leg_r1 = left_leg.addOrReplaceChild("left_leg_r1", CubeListBuilder.create().texOffs(108, 135).addBox(-6.5F, -10.0F, -7.5F, 13.0F, 20.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5645F, 8.9043F, 0.6981F, -0.1304F, -0.0114F, -0.0865F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(46, 77).addBox(-7.7823F, 18.3554F, -9.6506F, 15.0F, 20.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(2, 194).addBox(-6.7823F, 17.2404F, -7.7558F, 13.0F, 5.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.5645F, -14.9826F, 2.9768F));

		PartDefinition right_leg_r1 = right_leg.addOrReplaceChild("right_leg_r1", CubeListBuilder.create().texOffs(0, 143).addBox(-6.5F, -10.0F, -7.5F, 13.0F, 20.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5645F, 8.9043F, 0.9064F, -0.1304F, 0.0114F, 0.0865F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}



	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch)
	{
		this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
		this.rightLeg.xRot = -1.5F * Mth.triangleWave(pLimbSwing, 13.0F) * pLimbSwingAmount;
		this.leftLeg.xRot = 1.5F * Mth.triangleWave(pLimbSwing, 13.0F) * pLimbSwingAmount;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
	}

	@Override
	public void prepareMobModel(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick)
	{
		float i = pEntity.getAttackAnim(pPartialTick);
		if (i > 0)
		{
			this.rightArm.xRot = -2.0F + 1.5F * Mth.triangleWave(i - pPartialTick, 10.0F);
			this.leftArm.xRot = -2.0F + 1.5F * Mth.triangleWave(i - pPartialTick, 10.0F);
		}
		else
		{
			this.rightArm.xRot = (-0.2F + 1.5F * Mth.triangleWave(pLimbSwing, 13.0F)) * pLimbSwingAmount;
			this.leftArm.xRot = (-0.2F - 1.5F * Mth.triangleWave(pLimbSwing, 13.0F)) * pLimbSwingAmount;
		}
	}

	@Override
	public void translateToHand(HumanoidArm pSide, PoseStack pPoseStack)
	{
		float f = pSide == HumanoidArm.RIGHT ? 2.0F : -2.0F;
		ModelPart modelpart = this.getArm(pSide);
		modelpart.x -= 10;
		modelpart.y += 20;
		modelpart.translateAndRotate(pPoseStack);
		modelpart.x += 10;
		modelpart.y -= 20;

	}
}
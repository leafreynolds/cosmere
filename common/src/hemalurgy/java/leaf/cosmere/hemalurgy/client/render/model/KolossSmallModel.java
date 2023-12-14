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

public class KolossSmallModel<T extends LivingEntity> extends HumanoidModel<T>
{
	private final ModelPart root;

	public KolossSmallModel(ModelPart root)
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

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 25).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.5F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 11).addBox(-7.5F, -8.25F, -3.0F, 15.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 22).addBox(-6.5F, -3.25F, -3.5F, 13.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, 0.75F, -4.0F, 14.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 35).addBox(-6.0F, 3.75F, -3.0F, 12.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.75F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-7.3659F, 1.5282F, 0.0F));

		PartDefinition right_arm_r1 = right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(0, 67).addBox(-0.2929F, -16.2268F, 1.1077F, 1.0F, 1.1875F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.2929F, -16.2268F, -2.0193F, 1.0F, 1.1875F, 3.1875F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.7071F, -16.2268F, -2.0193F, 1.0F, 1.1875F, 3.1875F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.7071F, -16.2268F, 1.1077F, 1.0F, 1.1875F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.2929F, -15.137F, -2.6503F, 1.0F, 1.4375F, 5.5F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.7071F, -15.137F, -2.6503F, 1.0F, 1.4375F, 5.5F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.7071F, -15.137F, 1.7387F, 1.0F, 0.4375F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.2929F, -15.137F, 1.7387F, 1.0F, 0.4375F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.7071F, -15.137F, -3.6503F, 1.0F, 0.4375F, 3.5F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.2929F, -15.137F, -3.6503F, 1.0F, 0.4375F, 3.5F, new CubeDeformation(0.0F))
				.texOffs(2, 105).addBox(-0.7855F, 15.4456F, -1.324F, 1.5F, 0.375F, 1.125F, new CubeDeformation(0.0F))
				.texOffs(2, 105).addBox(-0.7855F, 15.4456F, 0.199F, 1.5F, 0.375F, 1.125F, new CubeDeformation(0.0F))
				.texOffs(2, 105).addBox(-1.3595F, 15.4456F, -0.75F, 2.625F, 0.375F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(2, 105).addBox(0.1635F, 15.4456F, -0.75F, 1.125F, 0.375F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(51, 100).addBox(-0.625F, 8.0598F, -0.75F, 1.25F, 6.0F, 1.5F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.2929F, -13.8152F, -2.5F, 1.0F, 20.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(41, 66).addBox(-1.5F, 6.8603F, -1.7074F, 3.0F, 0.25F, 4.75F, new CubeDeformation(0.0F))
				.texOffs(41, 66).addBox(-1.5F, 6.8111F, -1.1935F, 3.0F, 1.25F, 3.0625F, new CubeDeformation(0.0F))
				.texOffs(41, 66).addBox(-1.5F, 6.8111F, -1.869F, 3.0F, 1.25F, 0.8125F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.7071F, -13.8152F, -2.5F, 1.0F, 20.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(41, 66).addBox(-1.5F, 5.9713F, -3.5F, 3.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1966F, 9.287F, -11.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition right_arm_r2 = right_arm.addOrReplaceChild("right_arm_r2", CubeListBuilder.create().texOffs(0, 67).addBox(-0.7071F, -13.2846F, 8.1224F, 1.0F, 0.8125F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.2929F, -13.2846F, 8.1224F, 1.0F, 0.8125F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(41, 66).addBox(-1.5F, 6.7717F, -4.3785F, 3.0F, 0.25F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1966F, 9.287F, -11.0F, 2.3562F, 0.0F, 0.0F));

		PartDefinition right_arm_r3 = right_arm.addOrReplaceChild("right_arm_r3", CubeListBuilder.create().texOffs(0, 67).addBox(-0.7071F, -15.7644F, 3.3442F, 1.0F, 1.1875F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.2929F, -15.7644F, 3.3442F, 1.0F, 1.1875F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.2929F, -14.9775F, 2.2529F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.7071F, -14.9775F, 2.2529F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(2, 105).addBox(-0.7855F, 13.2765F, -5.4375F, 1.5F, 1.5F, 0.75F, new CubeDeformation(0.0F))
				.texOffs(41, 66).addBox(-1.5F, 7.5301F, -0.4342F, 3.0F, 0.25F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1966F, 9.287F, -11.0F, 1.9635F, 0.0F, 0.0F));

		PartDefinition right_arm_r4 = right_arm.addOrReplaceChild("right_arm_r4", CubeListBuilder.create().texOffs(0, 67).addBox(-0.2929F, -15.7982F, -4.2625F, 1.0F, 1.1875F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.7071F, -15.7982F, -4.2625F, 1.0F, 1.1875F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.2929F, -15.0113F, -3.1712F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.7071F, -15.0113F, -3.1712F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(2, 105).addBox(-0.7855F, 13.2765F, 4.6875F, 1.5F, 1.5F, 0.75F, new CubeDeformation(0.0F))
				.texOffs(41, 66).addBox(-1.5F, 7.5301F, -0.5658F, 3.0F, 0.25F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1966F, 9.287F, -11.0F, 1.1781F, 0.0F, 0.0F));

		PartDefinition right_arm_r5 = right_arm.addOrReplaceChild("right_arm_r5", CubeListBuilder.create().texOffs(0, 67).addBox(-0.2929F, -13.3471F, -9.0599F, 1.0F, 1.0625F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(-0.7071F, -13.3471F, -9.0599F, 1.0F, 1.0625F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(41, 66).addBox(-1.5F, 6.2717F, 2.3785F, 3.0F, 0.5F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(41, 66).addBox(-1.5F, 2.3785F, 6.2717F, 3.0F, 2.0F, 0.5F, new CubeDeformation(0.0F))
				.texOffs(41, 66).addBox(-1.5F, 6.7717F, 2.3785F, 3.0F, 0.25F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1966F, 9.287F, -11.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition right_arm_r6 = right_arm.addOrReplaceChild("right_arm_r6", CubeListBuilder.create().texOffs(2, 105).addBox(-5.4703F, 13.263F, -0.75F, 0.75F, 1.5F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1966F, 9.287F, -11.0F, 1.5708F, 0.3927F, 0.0F));

		PartDefinition right_arm_r7 = right_arm.addOrReplaceChild("right_arm_r7", CubeListBuilder.create().texOffs(2, 105).addBox(4.6547F, 13.2901F, -0.75F, 0.75F, 1.5F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1966F, 9.287F, -11.0F, 1.5708F, -0.3927F, 0.0F));

		PartDefinition right_arm_r8 = right_arm.addOrReplaceChild("right_arm_r8", CubeListBuilder.create().texOffs(0, 67).addBox(-2.2678F, -14.8152F, 1.2678F, 1.0F, 21.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 67).addBox(1.2678F, -14.8152F, -2.2678F, 1.0F, 21.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.1966F, 9.287F, -11.0F, 1.5708F, 0.0F, 0.7854F));

		PartDefinition right_arm_r9 = right_arm.addOrReplaceChild("right_arm_r9", CubeListBuilder.create().texOffs(0, 43).addBox(2.1875F, -6.0F, -2.5F, 4.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.7591F, 5.9718F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition right_arm_r10 = right_arm.addOrReplaceChild("right_arm_r10", CubeListBuilder.create().texOffs(12, 52).addBox(-2.0F, -2.5F, -3.0F, 4.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5456F, 0.0982F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(7.3354F, 1.535F, 0.0F));

		PartDefinition left_arm_r1 = left_arm.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(56, 16).addBox(-2.0F, -2.5F, -3.0F, 4.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5761F, 0.0914F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition left_arm_r2 = left_arm.addOrReplaceChild("left_arm_r2", CubeListBuilder.create().texOffs(51, 41).addBox(-6.25F, -6.0F, -2.5F, 4.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.7896F, 5.965F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(31, 41).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 13.5F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(42, 6).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.25F, 13.5F, 0.0F));

		PartDefinition right_arm2 = partdefinition.addOrReplaceChild("right_arm2", CubeListBuilder.create(), PartPose.offset(-9.1077F, -6.0991F, 8.2071F));

		return LayerDefinition.create(meshdefinition, 128, 128);
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
		modelpart.x -= 4;
		modelpart.y += 4;
		modelpart.translateAndRotate(pPoseStack);
		modelpart.x += 4;
		modelpart.y -= 4;

	}
}
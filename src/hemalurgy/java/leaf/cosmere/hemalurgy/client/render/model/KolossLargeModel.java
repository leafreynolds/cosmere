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

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(-10.0F, -6.0784F, 3.8832F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(56, 158).addBox(-5.9375F, -10.0F, -6.75F, 12.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(52, 0).addBox(-4.0625F, 2.0F, -3.75F, 8.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0552F, -53.7473F, -0.678F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(110, 24).addBox(-12.1232F, 13.7469F, -2.1389F, 24.0F, 10.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1305F, -36.8621F, -0.0514F));

		PartDefinition bottom_spike_r1 = body.addOrReplaceChild("bottom_spike_r1", CubeListBuilder.create().texOffs(52, 11).addBox(-30.25F, -2.0F, -24.5625F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1305F, 24.6894F, 20.2308F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition top_spike_r1 = body.addOrReplaceChild("top_spike_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-35.9375F, -0.75F, -7.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1195F, 24.6894F, 2.4456F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition left_spike_r1 = body.addOrReplaceChild("left_spike_r1", CubeListBuilder.create().texOffs(72, 24).addBox(-34.5F, -13.9375F, -7.5625F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(94, 77).addBox(-34.5F, 10.9375F, -7.5625F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1232F, 24.6894F, 3.2993F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition Body_r1 = body.addOrReplaceChild("Body_r1", CubeListBuilder.create().texOffs(72, 0).addBox(-12.0F, 10.5F, -8.0F, 24.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1232F, -3.5606F, 3.2993F, 0.1309F, 0.0F, 0.0F));

		PartDefinition Body_r2 = body.addOrReplaceChild("Body_r2", CubeListBuilder.create().texOffs(128, 48).addBox(2.5F, -14.0F, -8.0F, 8.0F, 28.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-13.5F, -17.0F, -10.0F, 16.0F, 34.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1232F, -1.5606F, 3.2993F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(19.0F, -43.0F, 2.0F));

		PartDefinition LeftArmHand_r1 = left_arm.addOrReplaceChild("LeftArmHand_r1", CubeListBuilder.create().texOffs(0, 54).addBox(-6.75F, -16.5F, -12.25F, 14.0F, 23.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.4447F, 35.1806F, 0.9978F, -0.2182F, 0.0F, -0.2182F));

		PartDefinition LeftArm_r1 = left_arm.addOrReplaceChild("LeftArm_r1", CubeListBuilder.create().texOffs(0, 100).addBox(-3.25F, -23.0F, -9.0625F, 12.0F, 23.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.7573F, 21.0773F, 2.2478F, 0.0F, 0.0F, -0.2182F));

		PartDefinition LeftArmShoulder_r1 = left_arm.addOrReplaceChild("LeftArmShoulder_r1", CubeListBuilder.create().texOffs(149, 119).addBox(20.0F, -13.5F, -8.0F, 14.0F, 12.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-21.9927F, 4.5773F, 1.2478F, 0.1309F, 0.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-19.0F, -43.0F, 2.0F));

		PartDefinition RightArmHand_r1 = right_arm.addOrReplaceChild("RightArmHand_r1", CubeListBuilder.create().texOffs(64, 36).addBox(-7.75F, -16.5F, -11.9375F, 14.0F, 23.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.9925F, 35.1806F, 0.6853F, -0.2182F, 0.0F, 0.2182F));

		PartDefinition RightArm_r1 = right_arm.addOrReplaceChild("RightArm_r1", CubeListBuilder.create().texOffs(54, 120).addBox(-9.25F, -23.0F, -9.0625F, 12.0F, 23.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.3052F, 21.0773F, 2.2478F, 0.0F, 0.0F, 0.2182F));

		PartDefinition RightArmShoulder_r1 = right_arm.addOrReplaceChild("RightArmShoulder_r1", CubeListBuilder.create().texOffs(148, 154).addBox(-34.0F, -13.5F, -8.0F, 14.0F, 12.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.0073F, 4.5773F, 1.2478F, 0.1309F, 0.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(142, 92).addBox(-6.6532F, 15.2577F, -4.779F, 13.0F, 5.0F, 15.0F, new CubeDeformation(0.0F))
				.texOffs(94, 97).addBox(-7.6532F, 16.3727F, -6.0488F, 15.0F, 20.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(11.0F, -13.0F, 0.0F));

		PartDefinition LeftLeg_r1 = left_leg.addOrReplaceChild("LeftLeg_r1", CubeListBuilder.create().texOffs(108, 135).addBox(-6.5F, -10.0F, -7.5F, 13.0F, 20.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 6.9216F, 3.8832F, -0.1304F, -0.0114F, -0.0865F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(152, 0).addBox(-6.3468F, 15.2577F, -4.779F, 13.0F, 5.0F, 15.0F, new CubeDeformation(0.0F))
				.texOffs(46, 77).addBox(-7.3468F, 16.3727F, -6.6738F, 15.0F, 20.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.0F, -13.0F, 0.0F));

		PartDefinition RightLeg_r1 = right_leg.addOrReplaceChild("RightLeg_r1", CubeListBuilder.create().texOffs(0, 143).addBox(-6.5F, -10.0F, -7.5F, 13.0F, 20.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 6.9216F, 3.8832F, -0.1304F, 0.0114F, 0.0865F));

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
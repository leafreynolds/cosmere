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

public class KolossMediumModel<T extends LivingEntity> extends HumanoidModel<T>
{
	private final ModelPart root;

	public KolossMediumModel(ModelPart root)
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

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(108, 101).addBox(-7.5015F, 16.1433F, -1.7583F, 15.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(114, 91).addBox(-7.5015F, 13.1433F, 6.8042F, 15.0F, 3.0F, 0.4375F, new CubeDeformation(0.0F))
				.texOffs(54, 18).addBox(-4.0953F, -9.9935F, -5.7417F, 8.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.375F, 0.0F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(82, 137).addBox(-6.9945F, 6.5625F, -0.1023F, 1.5F, 1.5F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(0, 100).addBox(-6.9945F, -8.3438F, 8.8977F, 1.5F, 1.5F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(118, 0).addBox(-6.7445F, -8.0938F, -5.3523F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F))
				.texOffs(137, 45).addBox(-7.4007F, -0.4375F, 0.1789F, 1.5F, 1.5F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(66, 135).addBox(-7.2445F, -0.6875F, -5.8211F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(137, 0).addBox(3.5055F, -0.4375F, -2.3523F, 1.5F, 1.5F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(127, 126).addBox(3.7555F, -0.6875F, -3.8523F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(118, 45).addBox(-6.7445F, 6.8125F, -5.3523F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2969F, 0.0F, -2.4451F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition body_r2 = body.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(106, 83).addBox(-6.6014F, 8.7943F, 23.4909F, 15.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9002F, 8.6908F, -26.5107F, 0.1309F, 0.0F, 0.0F));

		PartDefinition body_r3 = body.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(42, 27).addBox(-6.6014F, 2.8188F, 22.1768F, 15.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9002F, 8.6908F, -26.5107F, 0.1309F, 0.0F, 0.0F));

		PartDefinition body_r4 = body.addOrReplaceChild("body_r4", CubeListBuilder.create().texOffs(52, 68).addBox(-3.1812F, -9.8986F, 22.1768F, 6.0F, 18.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-15.1812F, -12.8986F, 20.6768F, 12.0F, 24.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9002F, 8.6908F, -26.5107F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-11.9764F, -19.5725F, -1.2969F));

		PartDefinition right_arm_r1 = right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(117, 95).addBox(-23.4764F, 18.3248F, 3.8869F, 6.75F, 0.5625F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(84, 6).addBox(-23.4764F, 8.4401F, 12.6465F, 6.75F, 4.5F, 1.125F, new CubeDeformation(0.0F))
				.texOffs(84, 0).addBox(-23.4764F, 17.1998F, 3.8869F, 6.75F, 1.125F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(32, 207).addBox(-21.6924F, -26.9426F, -21.8494F, 2.25F, 2.3906F, 2.25F, new CubeDeformation(0.0F))
				.texOffs(24, 207).addBox(-20.7604F, -26.9426F, -21.8494F, 2.25F, 2.3906F, 2.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0777F, 12.8882F, -25.9789F, 0.5236F, 0.0F, 0.0F));

		PartDefinition right_arm_r2 = right_arm.addOrReplaceChild("right_arm_r2", CubeListBuilder.create().texOffs(28, 104).addBox(-23.4764F, 19.2355F, -3.8081F, 6.75F, 0.5625F, 2.25F, new CubeDeformation(0.0F))
				.texOffs(22, 211).addBox(-21.6924F, -31.4826F, -9.6703F, 2.25F, 9.0F, 2.25F, new CubeDeformation(0.0F))
				.texOffs(14, 214).addBox(-20.7604F, -31.4826F, -9.6703F, 2.25F, 9.0F, 2.25F, new CubeDeformation(0.0F))
				.texOffs(21, 220).addBox(-21.6924F, -33.2531F, -12.1258F, 2.25F, 2.6719F, 2.25F, new CubeDeformation(0.0F))
				.texOffs(34, 207).addBox(-20.7604F, -33.2531F, -12.1258F, 2.25F, 2.6719F, 2.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0777F, 12.8882F, -25.9789F, 0.9163F, 0.0F, 0.0F));

		PartDefinition right_arm_r3 = right_arm.addOrReplaceChild("right_arm_r3", CubeListBuilder.create().texOffs(88, 83).addBox(-23.4764F, 16.7713F, -4.391F, 6.75F, 0.5625F, 2.25F, new CubeDeformation(0.0F))
				.texOffs(30, 205).addBox(-20.7604F, -33.8707F, 1.655F, 2.25F, 9.0F, 2.25F, new CubeDeformation(0.0F))
				.texOffs(18, 205).addBox(-21.6924F, -33.8707F, 1.655F, 2.25F, 9.0F, 2.25F, new CubeDeformation(0.0F))
				.texOffs(31, 209).addBox(-20.7604F, -35.6412F, 4.1104F, 2.25F, 2.6719F, 2.25F, new CubeDeformation(0.0F))
				.texOffs(26, 218).addBox(-21.6924F, -35.6412F, 4.1104F, 2.25F, 2.6719F, 2.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0777F, 12.8882F, -25.9789F, 1.7017F, 0.0F, 0.0F));

		PartDefinition right_arm_r4 = right_arm.addOrReplaceChild("right_arm_r4", CubeListBuilder.create().texOffs(103, 1).addBox(-23.4764F, 13.7715F, -12.9401F, 6.75F, 0.5625F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(32, 213).addBox(-20.7604F, -31.3552F, 15.1868F, 2.25F, 1.8281F, 2.25F, new CubeDeformation(0.0F))
				.texOffs(28, 206).addBox(-21.6924F, -31.3552F, 15.1868F, 2.25F, 1.8281F, 2.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0777F, 12.8882F, -25.9789F, 2.0944F, 0.0F, 0.0F));

		PartDefinition right_arm_r5 = right_arm.addOrReplaceChild("right_arm_r5", CubeListBuilder.create().texOffs(96, 113).addBox(-23.4764F, 16.4732F, -7.4249F, 6.75F, 2.8125F, 1.8281F, new CubeDeformation(0.0F))
				.texOffs(98, 138).addBox(-23.4764F, 16.4732F, -5.905F, 6.75F, 2.8125F, 6.8906F, new CubeDeformation(0.0F))
				.texOffs(128, 62).addBox(-23.4764F, 16.5839F, -7.0613F, 6.75F, 0.5625F, 10.6875F, new CubeDeformation(0.0F))
				.texOffs(26, 182).addBox(-20.7604F, -29.9361F, -8.8446F, 2.25F, 45.0F, 11.25F, new CubeDeformation(0.0F))
				.texOffs(47, 138).addBox(-21.5076F, 19.2827F, -4.9071F, 2.8125F, 13.5F, 3.375F, new CubeDeformation(0.0F))
				.texOffs(27, 212).addBox(-20.7604F, -32.9102F, -11.4328F, 2.25F, 0.9844F, 7.875F, new CubeDeformation(0.0F))
				.texOffs(16, 203).addBox(-21.6924F, -32.9102F, -11.4328F, 2.25F, 0.9844F, 7.875F, new CubeDeformation(0.0F))
				.texOffs(28, 203).addBox(-20.7604F, -32.9102F, 0.6924F, 2.25F, 0.9844F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(29, 215).addBox(-21.6924F, -32.9102F, 0.6924F, 2.25F, 0.9844F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(16, 201).addBox(-21.6924F, -32.9102F, -9.1828F, 2.25F, 3.2344F, 12.375F, new CubeDeformation(0.0F))
				.texOffs(10, 212).addBox(-20.7604F, -32.9102F, -9.1828F, 2.25F, 3.2344F, 12.375F, new CubeDeformation(0.0F))
				.texOffs(20, 215).addBox(-21.6924F, -35.3622F, -0.7273F, 2.25F, 2.6719F, 2.25F, new CubeDeformation(0.0F))
				.texOffs(11, 206).addBox(-21.6924F, -35.3622F, -7.763F, 2.25F, 2.6719F, 7.1719F, new CubeDeformation(0.0F))
				.texOffs(20, 208).addBox(-20.7604F, -35.3622F, -7.763F, 2.25F, 2.6719F, 7.1719F, new CubeDeformation(0.0F))
				.texOffs(26, 205).addBox(-20.7604F, -35.3622F, -0.7273F, 2.25F, 2.6719F, 2.25F, new CubeDeformation(0.0F))
				.texOffs(54, 0).addBox(-23.4764F, 14.5837F, -11.0946F, 6.75F, 2.25F, 15.75F, new CubeDeformation(0.0F))
				.texOffs(0, 182).addBox(-21.6924F, -29.9361F, -8.8446F, 2.25F, 45.0F, 11.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0777F, 12.8882F, -25.9789F, 1.309F, 0.0F, 0.0F));

		PartDefinition right_arm_r6 = right_arm.addOrReplaceChild("right_arm_r6", CubeListBuilder.create().texOffs(0, 188).addBox(-9.0847F, -32.1861F, -21.5929F, 2.25F, 47.25F, 2.25F, new CubeDeformation(0.0F))
				.texOffs(8, 188).addBox(-17.0397F, -32.1861F, -13.638F, 2.25F, 47.25F, 2.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0777F, 12.8882F, -25.9789F, 1.3835F, 0.1841F, 0.7681F));

		PartDefinition right_arm_r7 = right_arm.addOrReplaceChild("right_arm_r7", CubeListBuilder.create().texOffs(76, 74).addBox(-21.8687F, 29.7009F, -15.6484F, 3.375F, 3.375F, 1.6875F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0777F, 12.8882F, -25.9789F, 1.7017F, 0.0F, 0.0F));

		PartDefinition right_arm_r8 = right_arm.addOrReplaceChild("right_arm_r8", CubeListBuilder.create().texOffs(52, 72).addBox(-21.8687F, 32.1651F, 8.0118F, 3.375F, 3.375F, 1.6875F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0777F, 12.8882F, -25.9789F, 0.9163F, 0.0F, 0.0F));

		PartDefinition right_arm_r9 = right_arm.addOrReplaceChild("right_arm_r9", CubeListBuilder.create().texOffs(30, 58).addBox(-7.6587F, 38.656F, -4.9071F, 1.6875F, 3.375F, 3.375F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0777F, 12.8882F, -25.9789F, 1.2885F, -0.3786F, 0.1068F));

		PartDefinition right_arm_r10 = right_arm.addOrReplaceChild("right_arm_r10", CubeListBuilder.create().texOffs(5, 8).addBox(-31.3188F, 23.21F, -4.9071F, 1.6875F, 3.375F, 3.375F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0777F, 12.8882F, -25.9789F, 1.2885F, 0.3786F, -0.1068F));

		PartDefinition right_arm_r11 = right_arm.addOrReplaceChild("right_arm_r11", CubeListBuilder.create().texOffs(52, 49).addBox(-19.7334F, 35.9007F, -4.9071F, 2.5313F, 0.8438F, 3.375F, new CubeDeformation(0.0F))
				.texOffs(52, 68).addBox(-21.8687F, 35.9007F, -2.7718F, 3.375F, 0.8438F, 2.5313F, new CubeDeformation(0.0F))
				.texOffs(41, 45).addBox(-23.1603F, 35.9007F, -4.9071F, 5.9063F, 0.8438F, 3.375F, new CubeDeformation(0.0F))
				.texOffs(52, 53).addBox(-21.8687F, 35.9007F, -6.1987F, 3.375F, 0.8438F, 2.5313F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0777F, 12.8882F, -25.9789F, 1.309F, 0.0F, 0.0F));

		PartDefinition right_arm_r12 = right_arm.addOrReplaceChild("right_arm_r12", CubeListBuilder.create().texOffs(90, 58).addBox(-22.715F, -3.1661F, 21.3761F, 9.0F, 15.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0762F, 12.8882F, -25.2138F, -0.2182F, 0.0F, 0.2182F));

		PartDefinition right_arm_r13 = right_arm.addOrReplaceChild("right_arm_r13", CubeListBuilder.create().texOffs(40, 98).addBox(-19.1014F, -15.1812F, 22.1768F, 8.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0762F, 12.8882F, -25.2138F, 0.1309F, 0.0F, 0.0F));

		PartDefinition right_arm_r14 = right_arm.addOrReplaceChild("right_arm_r14", CubeListBuilder.create().texOffs(105, 113).addBox(-22.215F, -11.7218F, 21.8383F, 8.0F, 16.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0762F, 12.8882F, -25.2138F, 0.0F, 0.0F, 0.2182F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(11.9734F, -19.5857F, -1.2954F));

		PartDefinition left_arm_r1 = left_arm.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(70, 110).mirror().addBox(15.9578F, -11.2246F, 21.8383F, 8.0F, 16.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-12.8736F, 12.9015F, -25.2153F, 0.0F, 0.0F, -0.2182F));

		PartDefinition left_arm_r2 = left_arm.addOrReplaceChild("left_arm_r2", CubeListBuilder.create().texOffs(90, 8).mirror().addBox(15.4578F, -2.7212F, 21.4792F, 9.0F, 15.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-12.8736F, 12.9015F, -25.2153F, -0.2182F, 0.0F, -0.2182F));

		PartDefinition left_arm_r3 = left_arm.addOrReplaceChild("left_arm_r3", CubeListBuilder.create().texOffs(0, 95).mirror().addBox(12.8986F, -15.1812F, 22.1768F, 8.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-12.8736F, 12.9015F, -25.2153F, 0.1309F, 0.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(88, 34).addBox(-4.1265F, 8.4852F, -5.0834F, 9.0F, 12.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 3.5148F, 1.3485F));

		PartDefinition left_leg_r1 = left_leg.addOrReplaceChild("left_leg_r1", CubeListBuilder.create().texOffs(16, 119).addBox(3.1486F, 4.2399F, 26.0595F, 8.0F, 11.5F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9002F, -10.199F, -27.8592F, -0.1309F, 0.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(77, 87).addBox(-4.8765F, 8.4852F, -4.9584F, 9.0F, 12.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 3.5148F, 1.2235F));

		PartDefinition right_leg_r1 = right_leg.addOrReplaceChild("right_leg_r1", CubeListBuilder.create().texOffs(119, 24).addBox(-9.3514F, 4.2399F, 26.0595F, 8.0F, 11.5F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0998F, -10.199F, -27.7342F, -0.1309F, 0.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(170, 52).addBox(-5.9547F, -10.125F, -4.4849F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0469F, -27.2435F, -2.7568F));

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
		modelpart.x -= 4;
		modelpart.y += 4;
		modelpart.translateAndRotate(pPoseStack);
		modelpart.x += 4;
		modelpart.y -= 4;

	}
}
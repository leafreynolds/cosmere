/*
 * File updated ~ 26 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.client.render.model;

// Made with Blockbench 4.3.1
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

public class ShardplateModel extends HumanoidModel<LivingEntity>
{
	private final ModelPart Root;

	public ShardplateModel(ModelPart root)
	{
		super(root, RenderType::entityCutoutNoCull);

		this.Root = root;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.75F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.5F, 0.0F));

		PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(50, 33).addBox(7.0F, -9.0F, 2.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 52).addBox(-1.0F, -9.0F, 2.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -8.75F, -4.4009F, -0.3927F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(19, 23).addBox(-4.0F, 0.75F, -2.5F, 8.0F, 3.0F, 5.0F, new CubeDeformation(0.75F))
				.texOffs(0, 16).addBox(-4.0F, 3.5F, -2.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.75F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition right_arm_r1 = right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(48, 0).addBox(1.3325F, 2.4154F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(-2.681F, 0.7946F, 0.0F, 0.0F, 0.0F, -2.138F));

		PartDefinition right_arm_r2 = right_arm.addOrReplaceChild("right_arm_r2", CubeListBuilder.create().texOffs(16, 46).addBox(-2.6306F, -1.7709F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(-2.681F, 0.7946F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition right_arm_r3 = right_arm.addOrReplaceChild("right_arm_r3", CubeListBuilder.create().texOffs(24, 16).addBox(-1.6347F, -2.6993F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.7F))
				.texOffs(44, 27).addBox(-3.3418F, -3.1797F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(-2.681F, 0.7946F, 0.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition right_arm_r4 = right_arm.addOrReplaceChild("right_arm_r4", CubeListBuilder.create().texOffs(0, 28).addBox(-1.069F, -2.0446F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(-2.681F, 0.7946F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 31).addBox(-0.375F, -1.25F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition left_arm_r1 = left_arm.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(48, 5).addBox(-4.3325F, 2.4154F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(2.556F, 0.7946F, 0.0F, 0.0F, 0.0F, 2.138F));

		PartDefinition left_arm_r2 = left_arm.addOrReplaceChild("left_arm_r2", CubeListBuilder.create().texOffs(30, 46).addBox(-0.3694F, -1.7709F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(2.556F, 0.7946F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition left_arm_r3 = left_arm.addOrReplaceChild("left_arm_r3", CubeListBuilder.create().texOffs(44, 47).addBox(0.3418F, -3.1797F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.7F))
				.texOffs(42, 40).addBox(0.6347F, -2.6993F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(2.556F, 0.7946F, 0.0F, 0.0F, 0.0F, -0.6109F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 43).addBox(2.4F, 6.5F, -2.25F, 4.0F, 5.0F, 4.25F, new CubeDeformation(0.5F))
				.texOffs(24, 0).addBox(-1.85F, 9.5F, -4.75F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.5F))
				.texOffs(32, 0).addBox(-2.45F, 0.65F, -2.1F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.4F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 31).addBox(-1.55F, 0.65F, -2.1F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.4F))
				.texOffs(40, 15).addBox(-6.4F, 6.5F, -2.25F, 4.0F, 5.0F, 4.25F, new CubeDeformation(0.5F))
				.texOffs(20, 16).addBox(-1.15F, 9.5F, -4.75F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}





	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(LivingEntity entity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch)
	{
		if (entity instanceof ArmorStand)
		{
			super.setupAnim(entity, 0, 0, 0, 0, 0);
		}
		else
		{
			super.setupAnim(entity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		}
	}
}
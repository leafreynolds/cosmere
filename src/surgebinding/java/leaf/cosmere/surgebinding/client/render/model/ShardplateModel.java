/*
 * File updated ~ 5 - 8 - 2023 ~ Leaf
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
	private final ModelPart root;
	public final ModelPart Chestplate;
	public final ModelPart LeftLeg;
	public final ModelPart RightLeg;
	public final ModelPart LeftBoot;
	public final ModelPart RightBoot;

	public ShardplateModel(ModelPart root)
	{
		super(root, RenderType::entityCutoutNoCull);

		this.root = root;

		this.Chestplate = this.root.getChild("body").getChild("chestplate");

		ModelPart left_leg = this.root.getChild("left_leg");
		ModelPart right_leg = this.root.getChild("right_leg");

		this.LeftLeg = left_leg.getChild("leftleg");
		this.RightLeg = right_leg.getChild("rightleg");

		this.LeftBoot = left_leg.getChild("left_boot");
		this.RightBoot = right_leg.getChild("right_boot");
	}


	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.75F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F)).texOffs(0, 52).addBox(-5.0F, -15.5F, 1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(50, 33).addBox(3.0F, -15.5F, 1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition chestplate = body.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(19, 23).addBox(-4.0F, 0.75F, -2.5F, 8.0F, 3.0F, 5.0F, new CubeDeformation(0.75F)).texOffs(0, 16).addBox(-4.0F, 3.5F, -2.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.75F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition right_arm_r1 = right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(48, 0).addBox(1.1217F, 2.2811F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(-2.5625F, 0.5446F, 0.0F, 0.0F, 0.0F, -2.138F));

		PartDefinition right_arm_r2 = right_arm.addOrReplaceChild("right_arm_r2", CubeListBuilder.create().texOffs(44, 47).addBox(-2.5765F, -1.5268F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(-2.5625F, 0.5446F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition right_arm_r3 = right_arm.addOrReplaceChild("right_arm_r3", CubeListBuilder.create().texOffs(24, 16).addBox(-1.4913F, -2.4945F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.7F)).texOffs(44, 27).addBox(-3.1984F, -2.9749F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(-2.5625F, 0.5446F, 0.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition right_arm_r4 = right_arm.addOrReplaceChild("right_arm_r4", CubeListBuilder.create().texOffs(32, 0).addBox(-1.069F, -1.7946F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(-2.5625F, 0.5446F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 31).addBox(-0.3685F, -1.25F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition left_arm_r1 = left_arm.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(48, 5).addBox(-4.3325F, 2.4154F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(2.5625F, 0.7946F, 0.0F, 0.0F, 0.0F, 2.138F));

		PartDefinition left_arm_r2 = left_arm.addOrReplaceChild("left_arm_r2", CubeListBuilder.create().texOffs(16, 46).addBox(-0.3694F, -1.7709F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(2.5625F, 0.7946F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition left_arm_r3 = left_arm.addOrReplaceChild("left_arm_r3", CubeListBuilder.create().texOffs(30, 46).addBox(0.3418F, -3.1797F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.7F)).texOffs(42, 40).addBox(0.6347F, -2.6993F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(2.5625F, 0.7946F, 0.0F, 0.0F, 0.0F, -0.6109F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition rightleg = right_leg.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(16, 31).addBox(-4.35F, -11.6F, -2.1F, 4.0F, 9.25F, 4.0F, new CubeDeformation(0.4F)), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition right_boot = right_leg.addOrReplaceChild("right_boot", CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition leftboot_outside = right_boot.addOrReplaceChild("leftboot_outside", CubeListBuilder.create().texOffs(40, 15).addBox(-4.5F, -5.5F, -2.25F, 4.0F, 5.0F, 4.25F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightboot_tip = right_boot.addOrReplaceChild("rightboot_tip", CubeListBuilder.create().texOffs(24, 0).addBox(-4.05F, -2.5F, -4.75F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition leftleg = left_leg.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(0, 28).addBox(0.35F, -11.6F, -2.1F, 4.0F, 9.25F, 4.0F, new CubeDeformation(0.4F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition left_boot = left_leg.addOrReplaceChild("left_boot", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition rightboot_outside = left_boot.addOrReplaceChild("rightboot_outside", CubeListBuilder.create().texOffs(0, 43).addBox(0.5F, -5.5F, -2.25F, 4.0F, 5.0F, 4.25F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftboot_tip = left_boot.addOrReplaceChild("leftboot_tip", CubeListBuilder.create().texOffs(20, 16).addBox(0.75F, -2.5F, -4.75F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

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
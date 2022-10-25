/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.client.render.armor;

// Made with Blockbench 4.3.1
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

public class ShardplateModel extends HumanoidModel<LivingEntity>
{
	private static final ResourceLocation TEXTURE = Surgebinding.rl("textures/item/models/shardplate.png");

	private final ModelPart Root;

	public ShardplateModel(ModelPart root)
	{
		super(root, RenderType::entityCutoutNoCull);

		this.Root = root;
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0.5F), 0);
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Helmet = head.addOrReplaceChild("Helmet", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = Helmet.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 47).addBox(3.0F, -16.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(24, 47).addBox(-5.0F, -16.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition chestplate = body.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.75F))
				.texOffs(20, 22).addBox(-4.0F, 0.5F, -2.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition chestplateRightArm = rightArm.addOrReplaceChild("chestplateRightArm", CubeListBuilder.create().texOffs(32, 31).addBox(-4.3125F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.6F))
				.texOffs(0, 42).addBox(-4.3125F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.7F))
				.texOffs(0, 0).addBox(-2.2071F, -1.1679F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F))
				.texOffs(0, 0).addBox(-0.7929F, -1.1679F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F))
				.texOffs(20, 12).addBox(-1.5F, -1.4608F, -2.5F, 0.0F, 1.0F, 5.0F, new CubeDeformation(0.5F))
				.texOffs(0, 0).addBox(-1.5F, -1.875F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightArmLayer_r1 = chestplateRightArm.addOrReplaceChild("RightArmLayer_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-2.0F, -0.6679F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition RightArmLayer_r2 = chestplateRightArm.addOrReplaceChild("RightArmLayer_r2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-2.0F, -1.6679F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition RightArmLayer_r3 = chestplateRightArm.addOrReplaceChild("RightArmLayer_r3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-1.0F, -0.6679F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition RightArmLayer_r4 = chestplateRightArm.addOrReplaceChild("RightArmLayer_r4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-1.0F, -1.6679F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition chestplateLeftArm = leftArm.addOrReplaceChild("chestplateLeftArm", CubeListBuilder.create().texOffs(32, 0).addBox(0.3125F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.6F))
				.texOffs(40, 16).addBox(0.375F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Shouldermount = chestplateLeftArm.addOrReplaceChild("Shouldermount", CubeListBuilder.create().texOffs(20, 13).addBox(0.0F, 0.2892F, -2.5F, 0.0F, 1.0F, 5.0F, new CubeDeformation(0.5F))
				.texOffs(0, 0).addBox(0.0F, -0.125F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offset(1.5F, -1.75F, 0.0F));

		PartDefinition LeftArmLayer_r1 = Shouldermount.addOrReplaceChild("LeftArmLayer_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.5F, 0.0821F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition LeftArmLayer_r2 = Shouldermount.addOrReplaceChild("LeftArmLayer_r2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.5F, 1.0821F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition LeftArmLayer_r3 = Shouldermount.addOrReplaceChild("LeftArmLayer_r3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-0.5F, 1.0821F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition LeftArmLayer_r4 = Shouldermount.addOrReplaceChild("LeftArmLayer_r4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-0.5F, 0.0821F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition LeftArmLayer_r5 = Shouldermount.addOrReplaceChild("LeftArmLayer_r5", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -0.125F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-0.7071F, 0.7071F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition LeftArmLayer_r6 = Shouldermount.addOrReplaceChild("LeftArmLayer_r6", CubeListBuilder.create().texOffs(0, 0).addBox(2.125F, -0.125F, -2.5F, 0.0F, 0.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-1.4179F, 0.7071F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition Shouldermount2 = chestplateLeftArm.addOrReplaceChild("Shouldermount2", CubeListBuilder.create(), PartPose.offset(8.5F, -1.75F, 0.0F));

		PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition greavesRightLeg = rightLeg.addOrReplaceChild("greavesRightLeg", CubeListBuilder.create().texOffs(16, 31).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.4F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bootsRightLeg = rightLeg.addOrReplaceChild("bootsRightLeg", CubeListBuilder.create().texOffs(44, 43).addBox(-2.0F, 9.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.5F))
				.texOffs(28, 17).addBox(-1.6F, 10.0F, -4.4F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition greavesLeftLeg = leftLeg.addOrReplaceChild("greavesLeftLeg", CubeListBuilder.create().texOffs(0, 26).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.4F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bootsLeftLeg = leftLeg.addOrReplaceChild("bootsLeftLeg", CubeListBuilder.create().texOffs(44, 25).addBox(-2.0F, 9.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.5F))
				.texOffs(24, 0).addBox(-1.4F, 10.0F, -4.4F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

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
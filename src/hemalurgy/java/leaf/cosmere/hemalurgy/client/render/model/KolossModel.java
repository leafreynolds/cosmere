/*
 * File updated ~ 7 - 6 - 2023 ~ Leaf
 */

// Made with Blockbench 4.6.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

package leaf.cosmere.hemalurgy.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class KolossModel<T extends Entity> extends EntityModel<T>
{
	private final ModelPart bb_main;

	public KolossModel(ModelPart root)
	{
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(56, 158).addBox(-5.9927F, -87.7473F, -7.428F, 12.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(94, 97).addBox(3.3468F, -20.6273F, -6.0488F, 15.0F, 20.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(46, 77).addBox(-18.3468F, -20.6273F, -6.6738F, 15.0F, 20.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(52, 0).addBox(-4.1177F, -75.7473F, -4.428F, 8.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(152, 0).addBox(-17.3468F, -21.7423F, -4.779F, 13.0F, 5.0F, 15.0F, new CubeDeformation(0.0F))
				.texOffs(142, 92).addBox(4.3468F, -21.7423F, -4.779F, 13.0F, 5.0F, 15.0F, new CubeDeformation(0.0F))
				.texOffs(110, 24).addBox(-11.9927F, -47.1152F, -2.1903F, 24.0F, 10.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition LeftLeg_r1 = bb_main.addOrReplaceChild("LeftLeg_r1", CubeListBuilder.create().texOffs(108, 135).addBox(-6.5F, -10.0F, -7.5F, 13.0F, 20.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.0F, -30.0784F, 3.8832F, -0.1304F, -0.0114F, -0.0865F));

		PartDefinition RightLeg_r1 = bb_main.addOrReplaceChild("RightLeg_r1", CubeListBuilder.create().texOffs(0, 143).addBox(-6.5F, -10.0F, -7.5F, 13.0F, 20.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, -30.0784F, 3.8832F, -0.1304F, 0.0114F, 0.0865F));

		PartDefinition Body_r1 = bb_main.addOrReplaceChild("Body_r1", CubeListBuilder.create().texOffs(0, 10).addBox(-36.75F, 12.25F, -4.0625F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-36.75F, -12.5625F, 1.9375F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(72, 24).addBox(-34.5F, -13.9375F, -7.5625F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(94, 77).addBox(-34.5F, 10.9375F, -7.5625F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0073F, -36.1727F, 3.2478F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition Body_r2 = bb_main.addOrReplaceChild("Body_r2", CubeListBuilder.create().texOffs(52, 11).addBox(-30.25F, -2.0F, -24.5625F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -36.1727F, 20.1794F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition Body_r3 = bb_main.addOrReplaceChild("Body_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-35.9375F, -0.75F, -7.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -36.1727F, 2.3941F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition LeftArm_r1 = bb_main.addOrReplaceChild("LeftArm_r1", CubeListBuilder.create().texOffs(0, 54).addBox(-6.75F, -16.5F, -12.25F, 14.0F, 23.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(31.4447F, -31.8194F, 2.9978F, -0.2182F, 0.0F, -0.2182F));

		PartDefinition RightArm_r1 = bb_main.addOrReplaceChild("RightArm_r1", CubeListBuilder.create().texOffs(64, 36).addBox(-7.75F, -16.5F, -11.9375F, 14.0F, 23.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.9925F, -31.8194F, 2.6853F, -0.2182F, 0.0F, 0.2182F));

		PartDefinition LeftArm_r2 = bb_main.addOrReplaceChild("LeftArm_r2", CubeListBuilder.create().texOffs(0, 100).addBox(-3.25F, -23.0F, -9.0625F, 12.0F, 23.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(25.7573F, -45.9227F, 4.2478F, 0.0F, 0.0F, -0.2182F));

		PartDefinition RightArm_r2 = bb_main.addOrReplaceChild("RightArm_r2", CubeListBuilder.create().texOffs(54, 120).addBox(-9.25F, -23.0F, -9.0625F, 12.0F, 23.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-25.3052F, -45.9227F, 4.2478F, 0.0F, 0.0F, 0.2182F));

		PartDefinition Body_r4 = bb_main.addOrReplaceChild("Body_r4", CubeListBuilder.create().texOffs(128, 48).addBox(2.5F, -14.0F, -8.0F, 8.0F, 28.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-13.5F, -17.0F, -10.0F, 16.0F, 34.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0073F, -62.4227F, 3.2478F, 0.0F, -0.1309F, 1.5708F));

		PartDefinition LeftArm_r3 = bb_main.addOrReplaceChild("LeftArm_r3", CubeListBuilder.create().texOffs(149, 119).addBox(20.0F, -13.5F, -8.0F, 14.0F, 12.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(148, 154).addBox(-28.0F, -13.5F, -8.0F, 14.0F, 12.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.9927F, -62.4227F, 3.2478F, 0.1309F, 0.0F, 0.0F));

		PartDefinition Body_r5 = bb_main.addOrReplaceChild("Body_r5", CubeListBuilder.create().texOffs(72, 0).addBox(-12.0F, 10.5F, -8.0F, 24.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0073F, -64.4227F, 3.2478F, 0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
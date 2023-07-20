/*
 * File updated ~ 7 - 6 - 2023 ~ Leaf
 */

// Made with Blockbench 4.6.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

package leaf.cosmere.surgebinding.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class BabyChullModel<T extends Entity> extends EntityModel<T>
{
	private final ModelPart bb_main;

	public BabyChullModel(ModelPart root)
	{
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer()
	{

		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(50, 53).addBox(-11.5592F, -17.69F, -4.2F, 11.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 50).addBox(-13.1592F, -14.89F, -5.6F, 14.0F, 3.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(0, 28).addBox(-14.7592F, -12.365F, -7.0F, 17.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -9.335F, -0.8217F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(1.236F, -0.3676F, -10.3699F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.6475F, -0.8217F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 3).addBox(-1.964F, -0.3676F, -10.3699F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -10.6475F, -0.8217F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(28, 64).addBox(-3.264F, 1.3199F, -10.3699F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(52, 64).addBox(-1.664F, 4.1583F, -5.9686F, 3.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(81, 0).addBox(7.8733F, 5.6523F, -18.8367F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 14).addBox(4.8733F, 5.6523F, -13.2367F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(86, 9).addBox(9.5242F, 5.6523F, -14.7396F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, 0.3927F, 0.0F));

		PartDefinition cube_r6 = bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(48, 28).addBox(-11.4483F, 5.6523F, -13.2367F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(92, 27).addBox(-11.2483F, 5.6523F, -18.8367F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r7 = bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(90, 18).addBox(-12.8859F, 5.6523F, -14.6726F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, -0.3927F, 0.0F));

		PartDefinition cube_r8 = bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 64).addBox(3.0022F, 2.584F, -13.983F, 3.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.2182F, -0.3054F, 0.0F));

		PartDefinition cube_r9 = bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(64, 0).addBox(-6.2022F, 2.584F, -13.983F, 3.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.2182F, 0.3054F, 0.0F));

		PartDefinition cube_r10 = bb_main.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 28).addBox(-20.7488F, -1.5937F, 5.5528F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -9.335F, -0.8217F, 0.0F, 0.3927F, 0.0F));

		PartDefinition cube_r11 = bb_main.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-20.7488F, 1.8063F, 4.1528F, 3.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 34).addBox(-22.7249F, 6.965F, 3.8827F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(48, 34).addBox(-22.7249F, 6.965F, 7.2077F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, 0.3927F, 0.0F));

		PartDefinition cube_r12 = bb_main.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(39, 50).addBox(-16.893F, -1.5937F, 4.5718F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -9.335F, -0.8217F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r13 = bb_main.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(80, 78).addBox(-16.893F, 1.8063F, 3.1718F, 3.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(64, 14).addBox(-18.8691F, 6.965F, 2.9017F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(17, 64).addBox(-18.8691F, 6.965F, 6.2267F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r14 = bb_main.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(46, 64).addBox(-18.0547F, 6.965F, 4.8737F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(66, 28).addBox(-18.0547F, 6.965F, 1.5487F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(82, 58).addBox(-16.0785F, 1.8063F, 1.8188F, 3.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, -0.3927F, 0.0F));

		PartDefinition cube_r15 = bb_main.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(34, 76).addBox(-16.0785F, -1.5937F, 3.2188F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -9.335F, -0.8217F, 0.0F, -0.3927F, 0.0F));

		PartDefinition cube_r16 = bb_main.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(14, 78).addBox(19.5249F, 6.965F, 3.8827F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(36, 86).addBox(17.5488F, 1.8063F, 4.1528F, 3.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(48, 86).addBox(19.5249F, 6.965F, 7.2077F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, -0.3927F, 0.0F));

		PartDefinition cube_r17 = bb_main.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(72, 78).addBox(17.5488F, -1.5937F, 5.5528F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -8.335F, -0.8217F, 0.0F, -0.3927F, 0.0F));

		PartDefinition cube_r18 = bb_main.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(88, 72).addBox(15.6691F, 6.965F, 2.9017F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 87).addBox(13.693F, 1.8063F, 3.1718F, 3.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 90).addBox(15.6691F, 6.965F, 6.2267F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r19 = bb_main.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(66, 89).addBox(13.693F, -1.5937F, 4.5718F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -8.335F, -0.8217F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r20 = bb_main.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(75, 92).addBox(14.8547F, 6.965F, 1.5487F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(92, 78).addBox(14.8547F, 6.965F, 4.8737F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(54, 89).addBox(12.8785F, 1.8063F, 1.8188F, 3.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, 0.3927F, 0.0F));

		PartDefinition cube_r21 = bb_main.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(87, 92).addBox(12.8785F, -1.5937F, 3.2188F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -8.335F, -0.8217F, 0.0F, 0.3927F, 0.0F));

		PartDefinition cube_r22 = bb_main.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(66, 67).addBox(-7.7592F, 5.6525F, -12.7F, 3.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, 1.5708F, 0.3491F));

		PartDefinition cube_r23 = bb_main.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(44, 75).addBox(-9.7429F, 5.6525F, -16.2092F, 3.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 3.1416F, 1.2217F, -2.7925F));

		PartDefinition cube_r24 = bb_main.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(20, 76).addBox(-5.0326F, 5.6525F, -11.9961F, 3.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, 1.2217F, 0.3491F));

		PartDefinition cube_r25 = bb_main.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(76, 20).addBox(-7.7592F, 5.6525F, 4.3F, 3.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, 1.5708F, -0.3491F));

		PartDefinition cube_r26 = bb_main.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(58, 78).addBox(-9.7429F, 5.6525F, 7.8092F, 3.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, 1.2217F, -0.3491F));

		PartDefinition cube_r27 = bb_main.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(0, 79).addBox(-5.0326F, 5.6525F, 3.5961F, 3.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, -3.1416F, 1.2217F, 2.7925F));

		PartDefinition cube_r28 = bb_main.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(48, 36).addBox(-14.7592F, 3.66F, -7.0F, 17.0F, 3.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-16.9592F, -3.965F, -9.8F, 22.0F, 8.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -10.335F, -0.8217F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
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
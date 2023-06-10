/*
 * File updated ~ 10 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ChullModel<T extends Entity> extends EntityModel<T>
{
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart backRightLeg;
	private final ModelPart backLeftLeg;
	private final ModelPart middleRightLeg;
	private final ModelPart middleLeftLeg;
	private final ModelPart frontRightLeg;
	private final ModelPart frontLeftLeg;
	private final ModelPart rightForeleg;
	private final ModelPart leftForeleg;

	public ChullModel(ModelPart pRoot)
	{
		this.root = pRoot.getChild("Everything");
		this.head = root.getChild("head");

		this.backRightLeg = root.getChild("back_right_leg");
		this.backLeftLeg = root.getChild("back_left_leg");

		this.middleRightLeg = root.getChild("middle_right_leg");
		this.middleLeftLeg = root.getChild("middle_left_leg");

		this.frontRightLeg = root.getChild("front_right_leg");
		this.frontLeftLeg = root.getChild("front_left_leg");

		this.rightForeleg = root.getChild("right_foreleg");
		this.leftForeleg = root.getChild("left_foreleg");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Everything = partdefinition.addOrReplaceChild("Everything", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition body = Everything.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.4142F, -24.0F, -47.0F, 48.0F, 10.0F, 68.0F, new CubeDeformation(0.0F))
				.texOffs(146, 95).addBox(3.5858F, -35.0F, -43.0F, 40.0F, 11.0F, 60.0F, new CubeDeformation(0.0F))
				.texOffs(164, 0).addBox(7.5858F, -46.0F, -39.0F, 32.0F, 11.0F, 52.0F, new CubeDeformation(0.0F))
				.texOffs(0, 221).addBox(11.5858F, -52.0F, -35.0F, 24.0F, 6.0F, 44.0F, new CubeDeformation(0.0F))
				.texOffs(0, 78).addBox(2.5858F, -14.0F, -46.0F, 40.0F, 14.0F, 63.0F, new CubeDeformation(0.0F))
				.texOffs(164, 22).addBox(-1.4142F, -14.0F, 13.0F, 17.0F, 14.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(92, 221).addBox(2.5858F, -1.75F, -47.0F, 40.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(280, 12).addBox(29.3431F, -1.7574F, 17.0F, 13.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 56).addBox(2.8284F, -1.7574F, 17.0F, 27.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(164, 0).addBox(29.5858F, -14.0F, 13.0F, 17.0F, 14.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(204, 246).addBox(-1.4142F, -14.0F, -27.0F, 48.0F, 14.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(224, 70).addBox(-1.4142F, -14.0F, -47.0F, 48.0F, 14.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(92, 240).addBox(-1.4142F, -14.0F, -7.0F, 48.0F, 14.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 155).addBox(2.5858F, 0.0F, -44.0F, 40.0F, 5.0F, 61.0F, new CubeDeformation(0.0F)), PartPose.offset(-22.5858F, -8.75F, 13.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(208, 172).addBox(-1.0F, -1.0F, -47.0F, 3.0F, 6.0F, 68.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(134, 166).addBox(-2.0F, -1.0F, -47.0F, 3.0F, 6.0F, 68.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(45.1716F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition head = Everything.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-7.3F, -2.9F, -8.5F, 15.0F, 15.0F, 15.0F, new CubeDeformation(0.0F))
				.texOffs(141, 192).addBox(-5.5F, -2.2F, 6.9F, 11.0F, 11.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(60, 45).addBox(2.4F, -5.2F, 15.7F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(60, 40).addBox(-4.2F, -5.2F, 15.7F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -19.8F, 34.9F));

		PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(32, 30).addBox(-5.5062F, 0.0F, -23.1F, 2.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.4F, 0.0F, 41.0F, 0.0F, -0.2182F, 0.0F));

		PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, 0).addBox(-0.9F, 0.0F, -9.9F, 2.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.0384F, 4.9499F, 45.1511F, -0.5236F, 0.2182F, 0.0F));

		PartDefinition cube_r5 = head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(40, 20).addBox(3.7063F, 0.0F, -23.1F, 2.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.4F, 0.0F, 41.0F, 0.0F, 0.2182F, 0.0F));

		PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(264, 268).addBox(-7.5F, -1.1875F, -2.6875F, 15.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2F, 12.5703F, -1.0191F, 0.3054F, 0.0F, 0.0F));

		PartDefinition cube_r7 = head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(36, 30).addBox(-0.9F, 0.0F, -9.9F, 2.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0384F, 4.9499F, 45.1511F, -0.5236F, -0.2182F, 0.0F));

		PartDefinition right_foreleg = Everything.addOrReplaceChild("right_foreleg", CubeListBuilder.create(), PartPose.offset(15.0F, -10.8F, 32.0F));

		PartDefinition cube_r8 = right_foreleg.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(185, 200).addBox(-1.7899F, 4.5261F, -0.5167F, 5.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(38, 78).addBox(-2.7899F, -2.4739F, -1.5167F, 7.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2244F, -0.0286F, 23.8964F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r9 = right_foreleg.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(181, 104).addBox(-5.0F, 10.0F, 2.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(38, 130).addBox(-6.0F, 5.0F, 2.0F, 7.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(318, 144).addBox(-7.0F, 0.0F, 2.0F, 9.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(326, 301).addBox(-7.0F, -19.0F, 2.0F, 9.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(38, 271).addBox(-7.0F, -15.0F, -3.0F, 9.0F, 18.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0173F, -0.8875F, 23.2888F, 0.4363F, 0.0F, 0.7854F));

		PartDefinition cube_r10 = right_foreleg.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(236, 166).addBox(-6.5F, -9.0F, -3.0F, 12.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5173F, -0.8875F, 19.2888F, 1.2787F, -0.7203F, -0.9387F));

		PartDefinition cube_r11 = right_foreleg.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(181, 78).addBox(-14.0F, -3.5F, -4.5F, 13.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3506F, -0.8875F, 14.1357F, 0.0F, -1.3526F, 0.4363F));

		PartDefinition front_right_leg = Everything.addOrReplaceChild("front_right_leg", CubeListBuilder.create(), PartPose.offset(17.0F, -15.0F, 16.5F));

		PartDefinition cube_r12 = front_right_leg.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(324, 55).addBox(4.1359F, 5.2993F, -6.2532F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(152, 301).addBox(11.5734F, 8.2993F, -5.2532F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(188, 262).addBox(13.5734F, 8.2993F, -4.2532F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(64, 322).addBox(4.1301F, 4.9867F, -6.2532F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, 0.0F, 0.3927F, 0.0F));

		PartDefinition cube_r13 = front_right_leg.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 181).addBox(12.1033F, -10.6069F, -4.2532F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, 0.3591F, 0.1624F, 1.164F));

		PartDefinition cube_r14 = front_right_leg.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(254, 333).addBox(12.9983F, -1.7464F, -5.2532F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(32, 322).addBox(4.9983F, -1.7464F, -6.2532F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, 0.2042F, 0.3378F, 0.5585F));

		PartDefinition cube_r15 = front_right_leg.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(160, 325).addBox(1.4557F, 5.2993F, -3.4542F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(208, 307).addBox(8.8932F, 8.2993F, -2.4542F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(266, 166).addBox(10.8932F, 8.2993F, -1.4542F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(128, 325).addBox(1.4499F, 4.9867F, -3.4542F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, -3.1416F, -1.5272F, 3.1416F));

		PartDefinition cube_r16 = front_right_leg.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 187).addBox(10.9706F, -8.1778F, -1.4542F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, -1.6189F, -0.4359F, 1.5911F));

		PartDefinition cube_r17 = front_right_leg.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(272, 333).addBox(10.6772F, -0.4064F, -2.4542F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(96, 324).addBox(2.6772F, -0.4064F, -3.4542F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, -1.6579F, -1.0456F, 1.6462F));

		PartDefinition cube_r18 = front_right_leg.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 322).addBox(-7.8494F, 5.2993F, -5.5889F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(120, 300).addBox(-9.2869F, 8.2993F, -4.5889F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(258, 63).addBox(-11.2869F, 8.2993F, -3.5889F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(296, 321).addBox(-7.8436F, 4.9867F, -5.5889F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, 0.0F, -0.6109F, 0.0F));

		PartDefinition cube_r19 = front_right_leg.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(141, 172).addBox(-12.2918F, -6.7219F, -3.5889F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, 0.5655F, -0.2448F, -1.2059F));

		PartDefinition cube_r20 = front_right_leg.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(222, 333).addBox(-12.2861F, 0.3968F, -4.5889F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(264, 321).addBox(-9.2861F, 0.3968F, -5.5889F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, 0.3368F, -0.5198F, -0.6139F));

		PartDefinition cube_r21 = front_right_leg.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(326, 289).addBox(3.0846F, 4.9867F, -5.5889F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(320, 325).addBox(3.0905F, 5.2993F, -5.5889F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(308, 246).addBox(10.528F, 8.2993F, -4.5889F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(150, 270).addBox(12.528F, 8.2993F, -3.5889F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 207).addBox(-24.3795F, -7.6835F, -3.5889F, 15.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(122, 262).addBox(-6.3795F, -14.6835F, -8.5889F, 7.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(0, 155).addBox(-6.3795F, -2.6835F, -8.5889F, 12.0F, 12.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, 0.0F, -0.6109F, 0.0F));

		PartDefinition cube_r22 = front_right_leg.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(202, 44).addBox(11.6615F, -9.6594F, -3.5889F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, -0.5655F, -0.2448F, 1.2059F));

		PartDefinition cube_r23 = front_right_leg.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(290, 333).addBox(12.0929F, -1.2237F, -4.5889F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(328, 30).addBox(4.0929F, -1.2237F, -5.5889F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, -0.3368F, -0.5198F, 0.6139F));

		PartDefinition cube_r24 = front_right_leg.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(0, 271).addBox(1.2196F, -13.3283F, -8.5889F, 5.0F, 13.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, 0.2618F, -0.5585F, -0.4682F));

		PartDefinition cube_r25 = front_right_leg.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(314, 230).addBox(-3.2073F, -13.5528F, -3.5889F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.9537F, 5.996F, 16.145F, 0.4923F, -0.3776F, -0.9686F));

		PartDefinition middle_right_leg = Everything.addOrReplaceChild("middle_right_leg", CubeListBuilder.create().texOffs(143, 104).addBox(13.0F, 2.8125F, -7.0F, 12.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(310, 120).addBox(22.4641F, 10.4827F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 221).addBox(31.9074F, 13.7953F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(296, 63).addBox(29.9074F, 13.7953F, -3.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(310, 132).addBox(22.4699F, 10.7953F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 221).addBox(13.0F, -9.1875F, -7.0F, 7.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(188, 268).addBox(0.0F, -2.1875F, -2.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(20.0F, -14.5F, 0.0F));

		PartDefinition cube_r26 = middle_right_leg.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(240, 317).addBox(-1.0625F, -4.0F, -3.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(88, 298).addBox(-2.5F, -1.0F, -2.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(242, 63).addBox(-4.5F, -1.0F, -1.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.5926F, 14.7953F, -1.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r27 = middle_right_leg.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(184, 319).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.5359F, 12.4827F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r28 = middle_right_leg.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(216, 321).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(332, 258).addBox(-7.0F, -2.0F, -3.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.0F, 10.2147F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition cube_r29 = middle_right_leg.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(141, 166).addBox(-0.5F, -2.0F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.1165F, 14.1874F, -1.0F, 0.0F, 0.0F, -1.1345F));

		PartDefinition cube_r30 = middle_right_leg.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(240, 268).addBox(-3.5F, -7.0F, -7.0F, 5.0F, 13.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(21.3181F, -2.1568F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition cube_r31 = middle_right_leg.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(316, 246).addBox(-8.0111F, -2.0401F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(332, 212).addBox(-0.0111F, -2.0401F, -3.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.4537F, 12.255F, -9.0F, 0.6654F, 0.8189F, 0.8213F));

		PartDefinition cube_r32 = middle_right_leg.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(316, 0).addBox(-6.9896F, -1.7723F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 226).addBox(2.4537F, 1.5403F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(56, 298).addBox(0.4537F, 1.5403F, -3.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(160, 313).addBox(-6.9838F, -1.4597F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.4537F, 12.255F, -9.0F, 0.0F, 1.0036F, 0.0F));

		PartDefinition cube_r33 = middle_right_leg.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(0, 161).addBox(1.2782F, -3.3855F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.4537F, 12.255F, -9.0F, 0.9581F, 0.3644F, 1.3253F));

		PartDefinition cube_r34 = middle_right_leg.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(128, 313).addBox(-8.0111F, -2.0401F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(332, 200).addBox(-0.0111F, -2.0401F, -3.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.4537F, 12.255F, 9.0F, -0.6654F, -0.8189F, 0.8213F));

		PartDefinition cube_r35 = middle_right_leg.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(96, 312).addBox(-6.9896F, -1.7723F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(224, 63).addBox(2.4537F, 1.5403F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(24, 298).addBox(0.4537F, 1.5403F, -3.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(310, 309).addBox(-6.9838F, -1.4597F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.4537F, 12.255F, 9.0F, 0.0F, -1.0036F, 0.0F));

		PartDefinition cube_r36 = middle_right_leg.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(0, 155).addBox(1.2782F, -3.3855F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.4537F, 12.255F, 9.0F, -0.9581F, -0.3644F, 1.3253F));

		PartDefinition cube_r37 = middle_right_leg.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(143, 110).addBox(-1.5F, -2.0F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(31.8835F, 14.1874F, -1.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition cube_r38 = middle_right_leg.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(332, 188).addBox(4.0F, -2.0F, -3.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(310, 108).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(26.0F, 10.2147F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r39 = middle_right_leg.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(226, 268).addBox(-5.0F, -2.0F, -2.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.6818F, -3.3033F, 0.0F, 0.0F, 0.0F, -0.8727F));

		PartDefinition back_right_leg = Everything.addOrReplaceChild("back_right_leg", CubeListBuilder.create(), PartPose.offset(20.0F, -14.0F, -16.0F));

		PartDefinition cube_r40 = back_right_leg.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(310, 96).addBox(-9.2861F, 0.3968F, -2.4111F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(332, 176).addBox(-12.2861F, 0.3968F, -1.4111F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, -0.3368F, 0.5198F, -0.6139F));

		PartDefinition cube_r41 = back_right_leg.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(64, 310).addBox(-7.8436F, 4.9867F, -2.4111F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(212, 63).addBox(-11.2869F, 8.2993F, -0.4111F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(254, 295).addBox(-9.2869F, 8.2993F, -1.4111F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(32, 310).addBox(-7.8494F, 5.2993F, -2.4111F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, 0.0F, 0.6109F, 0.0F));

		PartDefinition cube_r42 = back_right_leg.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(143, 104).addBox(-12.2918F, -6.7219F, -0.4111F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, -0.5655F, 0.2448F, -1.2059F));

		PartDefinition cube_r43 = back_right_leg.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(150, 262).addBox(-3.2073F, -13.5528F, -0.4111F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, -0.4923F, 0.3776F, -0.9686F));

		PartDefinition cube_r44 = back_right_leg.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(164, 44).addBox(-24.3795F, -7.6835F, -0.4111F, 15.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(208, 192).addBox(-6.3795F, -14.6835F, -5.4111F, 7.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(306, 218).addBox(3.0905F, 5.2993F, -2.4111F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(134, 288).addBox(10.528F, 8.2993F, -1.4111F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(208, 197).addBox(12.528F, 8.2993F, -0.4111F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(306, 206).addBox(3.0846F, 4.9867F, -2.4111F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(143, 78).addBox(-6.3795F, -2.6835F, -5.4111F, 12.0F, 12.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, 0.0F, 0.6109F, 0.0F));

		PartDefinition cube_r45 = back_right_leg.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(202, 268).addBox(1.2196F, -13.3283F, -5.4111F, 5.0F, 13.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, -0.2618F, 0.5585F, -0.4682F));

		PartDefinition cube_r46 = back_right_leg.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(0, 310).addBox(4.9983F, -1.7464F, -1.7468F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(332, 44).addBox(12.9983F, -1.7464F, -0.7468F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, -0.2042F, -0.3378F, 0.5585F));

		PartDefinition cube_r47 = back_right_leg.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(278, 309).addBox(4.1301F, 4.9867F, -1.7468F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(46, 211).addBox(13.5734F, 8.2993F, 0.2532F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(222, 295).addBox(11.5734F, 8.2993F, -0.7468F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(308, 260).addBox(4.1359F, 5.2993F, -1.7468F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, 0.0F, -0.3927F, 0.0F));

		PartDefinition cube_r48 = back_right_leg.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(143, 84).addBox(12.1033F, -10.6069F, 0.2532F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, -0.3591F, -0.1624F, 1.164F));

		PartDefinition cube_r49 = back_right_leg.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(216, 307).addBox(2.6772F, -0.4064F, -4.5458F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(332, 20).addBox(10.6772F, -0.4064F, -3.5458F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, 1.6579F, 1.0456F, 1.6462F));

		PartDefinition cube_r50 = back_right_leg.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(184, 307).addBox(1.4499F, 4.9867F, -4.5458F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(34, 211).addBox(10.8932F, 8.2993F, -2.5458F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(166, 289).addBox(8.8932F, 8.2993F, -3.5458F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(306, 273).addBox(1.4557F, 5.2993F, -4.5458F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, 3.1416F, 1.5272F, 3.1416F));

		PartDefinition cube_r51 = back_right_leg.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(143, 78).addBox(10.9706F, -8.1778F, -2.5458F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, 1.6189F, 0.4359F, 1.5911F));

		PartDefinition cube_r52 = back_right_leg.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(0, 110).addBox(11.6615F, -9.6594F, -0.4111F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, 0.5655F, 0.2448F, 1.2059F));

		PartDefinition cube_r53 = back_right_leg.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(204, 331).addBox(12.0929F, -1.2237F, -1.4111F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(306, 194).addBox(4.0929F, -1.2237F, -2.4111F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.9537F, 4.996F, -16.645F, 0.3368F, 0.5198F, 0.6139F));

		PartDefinition left_foreleg = Everything.addOrReplaceChild("left_foreleg", CubeListBuilder.create(), PartPose.offset(-16.687F, -12.5489F, 32.0F));

		PartDefinition cube_r54 = left_foreleg.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(330, 12).addBox(-2.0F, -19.0F, 2.0F, 9.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(181, 130).addBox(0.0F, 10.0F, 2.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(38, 155).addBox(-1.0F, 5.0F, 2.0F, 7.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(328, 67).addBox(-2.0F, 0.0F, 2.0F, 9.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(74, 271).addBox(-2.0F, -15.0F, -3.0F, 9.0F, 18.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3303F, 0.8614F, 23.2888F, 0.4363F, 0.0F, -0.6981F));

		PartDefinition cube_r55 = left_foreleg.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(185, 208).addBox(-3.2101F, 5.5261F, -0.5167F, 5.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(38, 104).addBox(-4.2101F, -1.4739F, -1.5167F, 7.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.4626F, 1.7203F, 23.8964F, 0.0F, 0.0F, -0.6981F));

		PartDefinition cube_r56 = left_foreleg.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(0, 247).addBox(1.0F, -3.5F, -4.5F, 13.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6636F, 0.8614F, 14.1357F, 0.0F, 1.3526F, -0.3491F));

		PartDefinition cube_r57 = left_foreleg.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(280, 0).addBox(-5.5F, -9.0F, -3.0F, 12.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.8303F, 0.8614F, 19.2888F, 1.2787F, 0.7203F, 1.0259F));

		PartDefinition front_left_leg = Everything.addOrReplaceChild("front_left_leg", CubeListBuilder.create(), PartPose.offset(-17.0F, -15.0F, 16.5F));

		PartDefinition cube_r58 = front_left_leg.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(0, 0).addBox(-13.6615F, -9.6594F, -3.5889F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, -0.5655F, 0.2448F, -1.2059F));

		PartDefinition cube_r59 = front_left_leg.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(183, 178).addBox(-15.0929F, -1.2237F, -4.5889F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(169, 166).addBox(-12.0929F, -1.2237F, -5.5889F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, -0.3368F, 0.5198F, -0.6139F));

		PartDefinition cube_r60 = front_left_leg.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(28, 181).addBox(-11.0846F, 4.9867F, -5.5889F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 30).addBox(-5.6205F, -2.6835F, -8.5889F, 12.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(0, 130).addBox(9.3795F, -7.6835F, -3.5889F, 15.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(141, 166).addBox(-0.6205F, -14.6835F, -8.5889F, 7.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(280, 22).addBox(-11.0905F, 5.2993F, -5.5889F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(28, 221).addBox(-12.528F, 8.2993F, -4.5889F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(38, 164).addBox(-14.528F, 8.2993F, -3.5889F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, 0.0F, 0.6109F, 0.0F));

		PartDefinition cube_r61 = front_left_leg.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(282, 226).addBox(-0.1564F, 4.9867F, -5.5889F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(181, 113).addBox(9.2869F, 8.2993F, -3.5889F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(114, 233).addBox(7.2869F, 8.2993F, -4.5889F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(282, 214).addBox(-0.1506F, 5.2993F, -5.5889F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, 0.0F, 0.6109F, 0.0F));

		PartDefinition cube_r62 = front_left_leg.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(180, 221).addBox(9.2861F, 0.3968F, -4.5889F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(286, 92).addBox(1.2861F, 0.3968F, -5.5889F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, 0.3368F, 0.5198F, 0.6139F));

		PartDefinition cube_r63 = front_left_leg.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(0, 36).addBox(10.2918F, -6.7219F, -3.5889F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, 0.5655F, 0.2448F, 1.2059F));

		PartDefinition cube_r64 = front_left_leg.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(174, 192).addBox(-6.7927F, -13.5528F, -3.5889F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, 0.4923F, 0.3776F, 0.9686F));

		PartDefinition cube_r65 = front_left_leg.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(236, 178).addBox(-6.2196F, -13.3283F, -8.5889F, 5.0F, 13.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, 0.2618F, 0.5585F, 0.4682F));

		PartDefinition cube_r66 = front_left_leg.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(282, 202).addBox(-12.9983F, -1.7464F, -6.2532F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(36, 202).addBox(-15.9983F, -1.7464F, -5.2532F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, 0.2042F, -0.3378F, -0.5585F));

		PartDefinition cube_r67 = front_left_leg.addOrReplaceChild("cube_r67", CubeListBuilder.create().texOffs(282, 190).addBox(-12.1301F, 4.9867F, -6.2532F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(176, 63).addBox(-15.5734F, 8.2993F, -4.2532F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(232, 63).addBox(-13.5734F, 8.2993F, -5.2532F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(282, 178).addBox(-12.1359F, 5.2993F, -6.2532F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, 0.0F, -0.3927F, 0.0F));

		PartDefinition cube_r68 = front_left_leg.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(0, 30).addBox(-14.1033F, -10.6069F, -4.2532F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, 0.3591F, -0.1624F, -1.164F));

		PartDefinition cube_r69 = front_left_leg.addOrReplaceChild("cube_r69", CubeListBuilder.create().texOffs(282, 166).addBox(-10.6772F, -0.4064F, -3.4542F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(42, 193).addBox(-13.6772F, -0.4064F, -2.4542F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, -1.6579F, 1.0456F, -1.6462F));

		PartDefinition cube_r70 = front_left_leg.addOrReplaceChild("cube_r70", CubeListBuilder.create().texOffs(278, 281).addBox(-9.4499F, 4.9867F, -3.4542F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(164, 63).addBox(-12.8932F, 8.2993F, -1.4542F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(28, 228).addBox(-10.8932F, 8.2993F, -2.4542F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(280, 34).addBox(-9.4557F, 5.2993F, -3.4542F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, -3.1416F, 1.5272F, -3.1416F));

		PartDefinition cube_r71 = front_left_leg.addOrReplaceChild("cube_r71", CubeListBuilder.create().texOffs(0, 6).addBox(-12.9706F, -8.1778F, -1.4542F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.9537F, 5.996F, 16.145F, -1.6189F, 0.4359F, -1.5911F));

		PartDefinition middle_left_leg = Everything.addOrReplaceChild("middle_left_leg", CubeListBuilder.create().texOffs(0, 78).addBox(-25.0F, 2.8125F, -7.0F, 12.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(230, 295).addBox(-30.4699F, 10.7953F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(260, 185).addBox(-31.9074F, 13.7953F, -3.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(200, 63).addBox(-33.9074F, 13.7953F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(294, 297).addBox(-30.4641F, 10.4827F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(208, 226).addBox(-10.0F, -2.1875F, -2.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 181).addBox(-20.0F, -9.1875F, -7.0F, 7.0F, 12.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-20.0F, -14.5F, 0.0F));

		PartDefinition cube_r72 = middle_left_leg.addOrReplaceChild("cube_r72", CubeListBuilder.create().texOffs(38, 30).addBox(-1.5F, -2.0F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.1165F, 14.1874F, -1.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition cube_r73 = middle_left_leg.addOrReplaceChild("cube_r73", CubeListBuilder.create().texOffs(24, 271).addBox(4.0F, -2.0F, -3.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(286, 104).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.0F, 10.2147F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r74 = middle_left_leg.addOrReplaceChild("cube_r74", CubeListBuilder.create().texOffs(286, 116).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.5359F, 12.4827F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r75 = middle_left_leg.addOrReplaceChild("cube_r75", CubeListBuilder.create().texOffs(183, 187).addBox(2.5F, -1.0F, -1.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(196, 240).addBox(0.5F, -1.0F, -2.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(286, 128).addBox(-6.9375F, -4.0F, -3.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.5926F, 14.7953F, -1.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r76 = middle_left_leg.addOrReplaceChild("cube_r76", CubeListBuilder.create().texOffs(208, 218).addBox(-5.0F, -2.0F, -2.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.6818F, -3.3033F, 0.0F, 0.0F, 0.0F, 0.8727F));

		PartDefinition cube_r77 = middle_left_leg.addOrReplaceChild("cube_r77", CubeListBuilder.create().texOffs(188, 63).addBox(-4.4537F, 1.5403F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(110, 288).addBox(-1.0104F, -1.7723F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(142, 289).addBox(-1.0162F, -1.4597F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(248, 63).addBox(-2.4537F, 1.5403F, -3.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-23.4537F, 12.255F, -9.0F, 0.0F, -1.0036F, 0.0F));

		PartDefinition cube_r78 = middle_left_leg.addOrReplaceChild("cube_r78", CubeListBuilder.create().texOffs(38, 36).addBox(-3.2782F, -3.3855F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-23.4537F, 12.255F, -9.0F, 0.9581F, -0.3644F, -1.3253F));

		PartDefinition cube_r79 = middle_left_leg.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(65, 271).addBox(-2.9889F, -2.0401F, -3.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(286, 140).addBox(0.0111F, -2.0401F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-23.4537F, 12.255F, -9.0F, 0.6654F, -0.8189F, -0.8213F));

		PartDefinition cube_r80 = middle_left_leg.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(236, 205).addBox(-1.5F, -7.0F, -7.0F, 5.0F, 13.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-21.3181F, -2.1568F, 0.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition cube_r81 = middle_left_leg.addOrReplaceChild("cube_r81", CubeListBuilder.create().texOffs(0, 298).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(242, 329).addBox(-7.0F, -2.0F, -3.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-26.0F, 10.2147F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition cube_r82 = middle_left_leg.addOrReplaceChild("cube_r82", CubeListBuilder.create().texOffs(45, 6).addBox(-0.5F, -2.0F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-31.8835F, 14.1874F, -1.0F, 0.0F, 0.0F, -1.1345F));

		PartDefinition cube_r83 = middle_left_leg.addOrReplaceChild("cube_r83", CubeListBuilder.create().texOffs(198, 295).addBox(-1.0162F, -1.4597F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(260, 178).addBox(-2.4537F, 1.5403F, -3.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(193, 113).addBox(-4.4537F, 1.5403F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(270, 293).addBox(-1.0104F, -1.7723F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-23.4537F, 12.255F, 9.0F, 0.0F, 1.0036F, 0.0F));

		PartDefinition cube_r84 = middle_left_leg.addOrReplaceChild("cube_r84", CubeListBuilder.create().texOffs(45, 0).addBox(-3.2782F, -3.3855F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-23.4537F, 12.255F, 9.0F, -0.9581F, 0.3644F, -1.3253F));

		PartDefinition cube_r85 = middle_left_leg.addOrReplaceChild("cube_r85", CubeListBuilder.create().texOffs(101, 271).addBox(-2.9889F, -2.0401F, -3.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(174, 289).addBox(0.0111F, -2.0401F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-23.4537F, 12.255F, 9.0F, -0.6654F, 0.8189F, -0.8213F));

		PartDefinition back_left_leg = Everything.addOrReplaceChild("back_left_leg", CubeListBuilder.create(), PartPose.offset(-20.0F, -14.0F, -16.0F));

		PartDefinition cube_r86 = back_left_leg.addOrReplaceChild("cube_r86", CubeListBuilder.create().texOffs(48, 202).addBox(9.2869F, 8.2993F, -0.4111F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(64, 298).addBox(-0.1564F, 4.9867F, -2.4111F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(96, 300).addBox(-0.1506F, 5.2993F, -2.4111F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(260, 205).addBox(7.2869F, 8.2993F, -1.4111F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, 0.0F, -0.6109F, 0.0F));

		PartDefinition cube_r87 = back_left_leg.addOrReplaceChild("cube_r87", CubeListBuilder.create().texOffs(52, 50).addBox(10.2918F, -6.7219F, -0.4111F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, -0.5655F, -0.2448F, 1.2059F));

		PartDefinition cube_r88 = back_left_leg.addOrReplaceChild("cube_r88", CubeListBuilder.create().texOffs(330, 86).addBox(9.2861F, 0.3968F, -1.4111F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(32, 298).addBox(1.2861F, 0.3968F, -2.4111F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, -0.3368F, -0.5198F, 0.6139F));

		PartDefinition cube_r89 = back_left_leg.addOrReplaceChild("cube_r89", CubeListBuilder.create().texOffs(208, 166).addBox(-0.6205F, -14.6835F, -5.4111F, 7.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(143, 130).addBox(9.3795F, -7.6835F, -0.4111F, 15.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(306, 170).addBox(-11.0846F, 4.9867F, -2.4111F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(208, 192).addBox(-14.528F, 8.2993F, -0.4111F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(280, 63).addBox(-12.528F, 8.2993F, -1.4111F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(306, 14).addBox(-11.0905F, 5.2993F, -2.4111F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 104).addBox(-5.6205F, -2.6835F, -5.4111F, 12.0F, 12.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, 0.0F, -0.6109F, 0.0F));

		PartDefinition cube_r90 = back_left_leg.addOrReplaceChild("cube_r90", CubeListBuilder.create().texOffs(92, 231).addBox(-6.7927F, -13.5528F, -0.4111F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, -0.4923F, -0.3776F, 0.9686F));

		PartDefinition cube_r91 = back_left_leg.addOrReplaceChild("cube_r91", CubeListBuilder.create().texOffs(0, 78).addBox(-14.1033F, -10.6069F, 0.2532F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, -0.3591F, 0.1624F, -1.164F));

		PartDefinition cube_r92 = back_left_leg.addOrReplaceChild("cube_r92", CubeListBuilder.create().texOffs(330, 166).addBox(-15.9983F, -1.7464F, -0.7468F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(128, 301).addBox(-12.9983F, -1.7464F, -1.7468F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, -0.2042F, 0.3378F, -0.5585F));

		PartDefinition cube_r93 = back_left_leg.addOrReplaceChild("cube_r93", CubeListBuilder.create().texOffs(160, 301).addBox(-12.1301F, 4.9867F, -1.7468F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(302, 285).addBox(-12.1359F, 5.2993F, -1.7468F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(260, 212).addBox(-13.5734F, 8.2993F, -0.7468F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(208, 166).addBox(-15.5734F, 8.2993F, 0.2532F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, 0.0F, 0.3927F, 0.0F));

		PartDefinition cube_r94 = back_left_leg.addOrReplaceChild("cube_r94", CubeListBuilder.create().texOffs(164, 262).addBox(-6.2196F, -13.3283F, -5.4111F, 5.0F, 13.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, -0.2618F, -0.5585F, 0.4682F));

		PartDefinition cube_r95 = back_left_leg.addOrReplaceChild("cube_r95", CubeListBuilder.create().texOffs(0, 84).addBox(-12.9706F, -8.1778F, -2.5458F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, 1.6189F, -0.4359F, -1.5911F));

		PartDefinition cube_r96 = back_left_leg.addOrReplaceChild("cube_r96", CubeListBuilder.create().texOffs(330, 272).addBox(-13.6772F, -0.4064F, -3.5458F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(304, 26).addBox(-10.6772F, -0.4064F, -4.5458F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, 1.6579F, -1.0456F, -1.6462F));

		PartDefinition cube_r97 = back_left_leg.addOrReplaceChild("cube_r97", CubeListBuilder.create().texOffs(304, 38).addBox(-9.4499F, 4.9867F, -4.5458F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(254, 305).addBox(-9.4557F, 5.2993F, -4.5458F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(264, 63).addBox(-10.8932F, 8.2993F, -3.5458F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(208, 171).addBox(-12.8932F, 8.2993F, -2.5458F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, 3.1416F, -1.5272F, -3.1416F));

		PartDefinition cube_r98 = back_left_leg.addOrReplaceChild("cube_r98", CubeListBuilder.create().texOffs(306, 182).addBox(-12.0929F, -1.2237F, -2.4111F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(186, 331).addBox(-15.0929F, -1.2237F, -1.4111F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, 0.3368F, -0.5198F, -0.6139F));

		PartDefinition cube_r99 = back_left_leg.addOrReplaceChild("cube_r99", CubeListBuilder.create().texOffs(0, 104).addBox(-13.6615F, -9.6594F, -0.4111F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9537F, 4.996F, -16.645F, 0.5655F, -0.2448F, -1.2059F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch)
	{
		this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);

		//reset to zero, the models have default position baked in
		this.backRightLeg.zRot = 0;
		this.backLeftLeg.zRot = 0;
		this.middleRightLeg.zRot = 0;
		this.middleLeftLeg.zRot = 0;
		this.frontRightLeg.zRot = 0;
		this.frontLeftLeg.zRot = 0;
		this.rightForeleg.zRot = 0;
		this.leftForeleg.zRot = 0;

		//reset to zero, the models have default position baked in
		this.backRightLeg.yRot = 0;
		this.backLeftLeg.yRot = 0;
		this.middleRightLeg.yRot = 0;
		this.middleLeftLeg.yRot = 0;
		this.frontRightLeg.yRot = 0;
		this.frontLeftLeg.yRot = 0;
		this.rightForeleg.yRot = 0;
		this.leftForeleg.yRot = 0;

		float backLegRotationY = (-(Mth.cos(pLimbSwing * 0.6662F) * 0.4F) * pLimbSwingAmount);
		float middleLegRotationY = (-(Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 0.4F) * pLimbSwingAmount);
		float frontLegRotationY = (-(Mth.cos(pLimbSwing * 0.6662F + ((float) Math.PI / 2F)) * 0.4F) * pLimbSwingAmount);
		float forelegRotationY = (-(Mth.cos(pLimbSwing * 0.6662F + ((float) Math.PI * 1.5F)) * 0.4F) * pLimbSwingAmount);

		float backLegRotationZ = (Mth.sin(pLimbSwing * 0.6662F + 0.0F) * 0.4F) * pLimbSwingAmount;
		float middleLegRotationZ = (Mth.sin(pLimbSwing * 0.6662F + (float) Math.PI) * 0.4F) * pLimbSwingAmount;
		float frontLegRotationZ = (Mth.sin(pLimbSwing * 0.6662F + ((float) Math.PI / 2F)) * 0.4F) * pLimbSwingAmount;
		float forelegRotationZ = (Mth.sin(pLimbSwing * 0.6662F + ((float) Math.PI * 1.5F)) * 0.4F) * pLimbSwingAmount;

		// the right side needs to have a negative value to go forwards
		this.frontRightLeg.yRot = -frontLegRotationY;
		this.middleRightLeg.yRot = -middleLegRotationY;
		this.backRightLeg.yRot = -backLegRotationY;

		// the left side needs to have a positive value to go forwards
		this.frontLeftLeg.yRot = frontLegRotationY;
		this.middleLeftLeg.yRot = middleLegRotationY;
		this.backLeftLeg.yRot = backLegRotationY;

		//We clamp these values so that it looks like the foot is planted on the ground as it moves forward
		{
			//the left side needs to be positive to have the leg go up
			this.frontLeftLeg.zRot = Mth.clamp(frontLegRotationZ, 0, 25);
			this.middleLeftLeg.zRot = Mth.clamp(middleLegRotationZ, 0, 25);
			this.backLeftLeg.zRot = Mth.clamp(backLegRotationZ, 0, 25);

			//the right side needs to be negative to have the leg go up.
			this.frontRightLeg.zRot = Mth.clamp(-frontLegRotationZ, -25, 0);
			this.middleRightLeg.zRot = Mth.clamp(-middleLegRotationZ, -25, 0);
			this.backRightLeg.zRot = Mth.clamp(-backLegRotationZ, -25, 0);
		}


		//todo decide what to do about the forelegs
		this.rightForeleg.zRot += -forelegRotationZ;
		this.leftForeleg.zRot += forelegRotationZ;

		this.rightForeleg.yRot += forelegRotationY;
		this.leftForeleg.yRot += -forelegRotationY;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
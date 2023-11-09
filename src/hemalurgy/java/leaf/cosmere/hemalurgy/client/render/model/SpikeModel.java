/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy.client.render.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nonnull;
import java.awt.*;

public class SpikeModel extends HumanoidModel<LivingEntity>
{
	private static final ResourceLocation TEXTURE = Cosmere.rl("textures/block/metal_block.png");

	// Body Parts
	private static final String headID = "head";
	private static final String bodyID = "body";
	private static final String leftArmID = "left_arm";
	private static final String rightArmID = "right_arm";
	private static final String leftLegID = "left_leg";
	private static final String rightLegID = "right_leg";

	// Categories
	private static final String eyeRootID = "eyeRoot";
	private static final String eyesID = "eyes";

	private static final String linchpinID = "linchpin";

	private static final String backID = "back";

	private static final String chestID = "chest";

	private static final String shouldersTopID = "shoulders_top";
	private static final String shouldersSideID = "shoulders_side";
	private static final String shouldersBackID = "shoulders_back";

	private static final String upperArmsID = "upper_arms";
	private static final String upperArmsBackID = "upper_arms_back";
	private static final String middleArmsID = "middle_arms";
	private static final String lowerArmsID = "lower_arms";
	private static final String handsID = "hands";

	private static final String ribsTopID = "ribs_top";
	private static final String ribsTopMiddleID = "ribs_top_middle";
	private static final String ribsBottomMiddleID = "ribs_bottom_middle";
	private static final String ribsBottomID = "ribs_bottom";

	private static final String upperLegsID = "upper_legs";
	private static final String upperMiddleLegsID = "upper_middle_legs";
	private static final String lowerMiddleLegsID = "lower_middle_legs";
	private static final String lowerLegsID = "lower_legs";

	// Model Parts
	private final ModelPart root;
	private final ModelPart eyeRoot;
	private final ModelPart eyeLeftFront;
	private final ModelPart eyeRightFront;
	private final ModelPart eyeLeftBack;
	private final ModelPart eyeRightBack;

	private final ModelPart linchpin;

	private final ModelPart upperBack;
	private final ModelPart upperMiddleBack;
	private final ModelPart lowerMiddleBack;
	private final ModelPart lowerBack;

	private final ModelPart upperChest;
	private final ModelPart lowerChest;

	private final ModelPart rightShoulderTop;
	private final ModelPart leftShoulderTop;
	private final ModelPart rightShoulderSide;
	private final ModelPart leftShoulderSide;
	private final ModelPart rightShoulderBack;
	private final ModelPart leftShoulderBack;

	private final ModelPart ribLeftTop;
	private final ModelPart ribLeftTopMiddle;
	private final ModelPart ribLeftBottomMiddle;
	private final ModelPart ribLeftBottom;
	private final ModelPart ribRightTop;
	private final ModelPart ribRightTopMiddle;
	private final ModelPart ribRightBottomMiddle;
	private final ModelPart ribRightBottom;

	private final ModelPart rightArmUpper;
	private final ModelPart rightArmUpperBack;
	private final ModelPart rightArmMiddle;
	private final ModelPart rightArmLower;

	private final ModelPart leftArmUpper;
	private final ModelPart leftArmUpperBack;
	private final ModelPart leftArmMiddle;
	private final ModelPart leftArmLower;

	private final ModelPart rightHand;
	private final ModelPart leftHand;

	private final ModelPart rightLegUpper;
	private final ModelPart rightLegUpperMiddle;
	private final ModelPart rightLegLowerMiddle;
	private final ModelPart rightLegLower;

	private final ModelPart leftLegUpper;
	private final ModelPart leftLegUpperMiddle;
	private final ModelPart leftLegLowerMiddle;
	private final ModelPart leftLegLower;

	public SpikeModel(ModelPart part)
	{
		super(part, RenderType::entityCutoutNoCull);
		this.root = part;

		ModelPart find = this.root.getChild(headID);
		eyeRoot = find.getChild(eyeRootID);
		eyeLeftFront = eyeRoot.getChild("left_eye_front");
		eyeRightFront = eyeRoot.getChild("right_eye_front");
		eyeLeftBack = eyeRoot.getChild("left_eye_back");
		eyeRightBack = eyeRoot.getChild("right_eye_back");

		find = part.getChild(bodyID);
		linchpin = find.getChild("linchpin");
		upperBack = find.getChild("upper_back");
		upperMiddleBack = find.getChild("upper_middle_back");
		lowerMiddleBack = find.getChild("lower_middle_back");
		lowerBack = find.getChild("lower_back");

		upperChest = find.getChild("upper_chest");
		lowerChest = find.getChild("lower_chest");

		ribLeftTop = find.getChild("rib_left_top");
		ribLeftTopMiddle = find.getChild("rib_left_top_middle");
		ribLeftBottomMiddle = find.getChild("rib_left_bottom_middle");
		ribLeftBottom = find.getChild("rib_left_bottom");
		ribRightTop = find.getChild("rib_right_top");
		ribRightTopMiddle = find.getChild("rib_right_top_middle");
		ribRightBottomMiddle = find.getChild("rib_right_bottom_middle");
		ribRightBottom = find.getChild("rib_right_bottom");

		find = part.getChild(rightArmID);
		rightShoulderTop = find.getChild("shoulder_top");
		rightShoulderSide = find.getChild("shoulder_side");
		rightShoulderBack = find.getChild("shoulder_back");

		rightArmUpper = find.getChild("upper");
		rightArmUpperBack = find.getChild("back");
		rightArmMiddle = find.getChild("middle");
		rightArmLower = find.getChild("lower");

		rightHand = find.getChild("hand");

		find = part.getChild(leftArmID);
		leftShoulderTop = find.getChild("shoulder_top");
		leftShoulderSide = find.getChild("shoulder_side");
		leftShoulderBack = find.getChild("shoulder_back");

		leftArmUpper = find.getChild("upper");
		leftArmUpperBack = find.getChild("back");
		leftArmMiddle = find.getChild("middle");
		leftArmLower = find.getChild("lower");

		leftHand = find.getChild("hand");

		find = part.getChild(rightLegID);
		rightLegUpper = find.getChild("upper");
		rightLegUpperMiddle = find.getChild("upper_middle");
		rightLegLowerMiddle = find.getChild("lower_middle");
		rightLegLower = find.getChild("lower");

		leftLegUpper = find.getChild("upper");
		leftLegUpperMiddle = find.getChild("upper_middle");
		leftLegLowerMiddle = find.getChild("lower_middle");
		leftLegLower = find.getChild("lower");
	}

	public static LayerDefinition createLayer()
	{
		CubeDeformation cube = new CubeDeformation(0.4F);
		MeshDefinition mesh = HumanoidModel.createMesh(cube, 0.0F);
		PartDefinition part = mesh.getRoot();


		final PartDefinition head = part.addOrReplaceChild(
				headID,
				CubeListBuilder.create(),
				PartPose.ZERO
		);
		final PartDefinition eyeRoot = head.addOrReplaceChild(
				eyeRootID,
				CubeListBuilder.create(),
				PartPose.ZERO
		);

		eyeRoot.addOrReplaceChild(
				"right_eye_front",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.0F, -3.0F, -4.25F, 2.0F, 2.0F, 5, cube),
				PartPose.ZERO);

		eyeRoot.addOrReplaceChild(
				"right_eye_back",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-2.25F, -2.25F, 3.0F, 0.5F, 0.5F, 1, cube),
				PartPose.ZERO);

		eyeRoot.addOrReplaceChild(
				"left_eye_front",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.0F, -3.0F, -4.25F, 2.0F, 2.0F, 5, cube),
				PartPose.ZERO);

		eyeRoot.addOrReplaceChild(
				"left_eye_back",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.75F, -2.25F, 3.0F, 0.5F, 0.5F, 1, cube),
				PartPose.ZERO);


		final PartDefinition body = part.addOrReplaceChild(
				bodyID,
				CubeListBuilder.create(),
				PartPose.ZERO
		);

		body.addOrReplaceChild(
				"linchpin",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 1.0F, 1.5F, 0.5F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"upper_back",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 3.5F, 1.5F, 0.5F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"upper_middle_back",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 5.5F, 1.5F, 0.5F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"lower_middle_back",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 7.5F, 1.5F, 0.5F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"lower_back",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 9.5F, 1.5F, 0.5F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"upper_chest",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 5.5F, -2.03125F, 0.5F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"lower_chest",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 9.5F, -2.03125F, 0.5F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"rib_right_top",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-2.5F, 4.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"rib_right_top_middle",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-2.5F, 6.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"rib_right_bottom_middle",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-2.5F, 8.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"rib_right_bottom",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-2.5F, 10.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"rib_left_top",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(2F, 4.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"rib_left_top_middle",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(2F, 6.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"rib_left_bottom_middle",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(2F, 8.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"rib_left_bottom",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(2F, 10.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		final PartDefinition rightArm = part.addOrReplaceChild(
				rightArmID,
				CubeListBuilder.create(),
				PartPose.ZERO
		);

		rightArm.addOrReplaceChild("shoulder_top",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-1.25F, -2.0F, -0.25F, 0.5F, 1.0F, 0.5F, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("shoulder_side",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.0F, 0.0F, -0.25F, 2.0F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("shoulder_back",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-1.25F, 0.0F, 0.0625F, 0.5F, 0.5F, 2.0F, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("upper",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.0F, 2.0F, -0.25F, 2.0F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("back",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-1.25F, 2.0F, 0.0625F, 0.5F, 0.5F, 2.0F, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("middle",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.0F, 4.0F, -0.25F, 2.0F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("lower",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.0F, 6.0F, -0.25F, 2.0F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("hand",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.0F, 8.0F, -0.25F, 2.0F, 0.5F, 0.5F, cube),
				PartPose.ZERO);


		final PartDefinition leftArm = part.addOrReplaceChild(
				leftArmID,
				CubeListBuilder.create().mirror(),
				PartPose.ZERO
		);

		leftArm.addOrReplaceChild("shoulder_top",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(0.75F, -2.0F, -0.25F, 0.5F, 1.0F, 0.5F, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("shoulder_side",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.0F, 0.0F, -0.25F, 2.0F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("shoulder_back",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(0.75F, 0.0F, 0.0625F, 0.5F, 0.5F, 2.0F, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("upper",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.0F, 2.0F, -0.25F, 2.0F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("back",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(0.75F, 2.0F, 0.0625F, 0.5F, 0.5F, 2.0F, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("middle",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.0F, 4.0F, -0.25F, 2.0F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("lower",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.0F, 6.0F, -0.25F, 2.0F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("hand",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.0F, 8.0F, -0.25F, 2.0F, 0.5F, 0.5F, cube),
				PartPose.ZERO);

		final PartDefinition rightLeg = part.addOrReplaceChild(
				rightLegID,
				CubeListBuilder.create(),
				PartPose.ZERO
		);

		rightLeg.addOrReplaceChild("upper",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 2.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		rightLeg.addOrReplaceChild("upper_middle",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 4.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		rightLeg.addOrReplaceChild("lower_middle",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 6.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		rightLeg.addOrReplaceChild("lower",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 8.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);


		final PartDefinition leftLeg = part.addOrReplaceChild(
				leftLegID,
				CubeListBuilder.create().mirror(),
				PartPose.ZERO
		);

		leftLeg.addOrReplaceChild("upper",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 2.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		leftLeg.addOrReplaceChild("upper_middle",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 4.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		leftLeg.addOrReplaceChild("lower_middle",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 6.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		leftLeg.addOrReplaceChild("lower",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.25F, 8.0F, -2.03125F, 0.5F, 0.5F, 4.0625F, cube),
				PartPose.ZERO);

		return LayerDefinition.create(mesh, 16, 16);
	}

	@Override
	@Nonnull
	protected Iterable<ModelPart> headParts()
	{
		return ImmutableList.of(this.head);
	}

	@Override
	@Nonnull
	protected Iterable<ModelPart> bodyParts()
	{
		return ImmutableList.of(
				this.body,
				this.rightArm,
				this.leftArm,
				this.rightLeg,
				this.leftLeg,
				this.hat);
	}

	public void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, MultiBufferSource buffer, int light)
	{
		final boolean leftHandSide = slotContext.index() % 2 == 0;


		ModelPart modelPartToRender = null;
		switch (slotContext.identifier())
		{
			case eyesID:
				eyeLeftFront.visible = leftHandSide;
				eyeRightFront.visible = !leftHandSide;
				eyeLeftBack.visible = leftHandSide;
				eyeRightBack.visible = !leftHandSide;

				SpiritwebCapability.get(slotContext.entity()).ifPresent((data) ->
				{
					//todo better


					eyeRoot.y = CompoundNBTHelper.getInt(data.getCompoundTag(), "eye_height", 0);
				});

				//we can call render on the head directly since we have made other spike not visible.
				//we do this so that we can use the eye height and have it follow proper head rotations
				modelPartToRender = head;

				break;
			case linchpinID:
				modelPartToRender = linchpin;
				modelPartToRender.copyFrom(this.root.getChild(bodyID));
				break;
			case backID:
				switch (slotContext.index())
				{
					case 0:
						modelPartToRender = upperBack;
						break;
					case 1:
						modelPartToRender = upperMiddleBack;
						break;
					case 2:
						modelPartToRender = lowerMiddleBack;
						break;
					case 3:
						modelPartToRender = lowerBack;
						break;
				}
				modelPartToRender.copyFrom(this.root.getChild(bodyID));
				break;
			case chestID:
				switch (slotContext.index())
				{
					case 0:
						modelPartToRender = upperChest;
						break;
					case 1:
						modelPartToRender = lowerChest;
						break;
				}
				modelPartToRender.copyFrom(this.root.getChild(bodyID));
				break;
			case shouldersTopID:
				modelPartToRender = leftHandSide ? leftShoulderTop : rightShoulderTop;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftArmID : rightArmID));
				break;
			case shouldersSideID:
				modelPartToRender = leftHandSide ? leftShoulderSide : rightShoulderSide;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftArmID : rightArmID));
				break;
			case shouldersBackID:
				modelPartToRender = leftHandSide ? leftShoulderBack : rightShoulderBack;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftArmID : rightArmID));
				break;
			case upperArmsID:
				modelPartToRender = leftHandSide ? leftArmUpper : rightArmUpper;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftArmID : rightArmID));
				break;
			case upperArmsBackID:
				modelPartToRender = leftHandSide ? leftArmUpperBack : rightArmUpperBack;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftArmID : rightArmID));
				break;
			case middleArmsID:
				modelPartToRender = leftHandSide ? leftArmMiddle : rightArmMiddle;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftArmID : rightArmID));
				break;
			case lowerArmsID:
				modelPartToRender = leftHandSide ? leftArmLower : rightArmLower;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftArmID : rightArmID));
				break;
			case handsID:
				modelPartToRender = leftHandSide ? leftHand : rightHand;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftArmID : rightArmID));
				break;
			case ribsTopID:
				switch (slotContext.index())
				{
					case 0:
						modelPartToRender = ribLeftTop;
						break;
					case 1:
						modelPartToRender = ribRightTop;
						break;
				}
				modelPartToRender.copyFrom(this.root.getChild(bodyID));
				break;
			case ribsTopMiddleID:
				switch (slotContext.index()) {
					case 0:
						modelPartToRender = ribLeftTopMiddle;
						break;
					case 1:
						modelPartToRender = ribRightTopMiddle;
						break;
				}
				modelPartToRender.copyFrom(this.root.getChild(bodyID));
				break;
			case ribsBottomMiddleID:
				switch (slotContext.index()) {
					case 0:
						modelPartToRender = ribLeftBottomMiddle;
						break;
					case 1:
						modelPartToRender = ribRightBottomMiddle;
						break;
				}
				modelPartToRender.copyFrom(this.root.getChild(bodyID));
				break;
			case ribsBottomID:
				switch (slotContext.index()) {
					case 0:
						modelPartToRender = ribLeftBottom;
						break;
					case 1:
						modelPartToRender = ribRightBottom;
						break;
				}
				modelPartToRender.copyFrom(this.root.getChild(bodyID));
				break;
			case upperLegsID:
				modelPartToRender = leftHandSide ? leftLegUpper : rightLegUpper;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftLegID : rightLegID));
				break;
			case upperMiddleLegsID:
				modelPartToRender = leftHandSide ? leftLegUpperMiddle : rightLegUpperMiddle;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftLegID : rightLegID));
				break;
			case lowerMiddleLegsID:
				modelPartToRender = leftHandSide ? leftLegLowerMiddle : rightLegLowerMiddle;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftLegID : rightLegID));
				break;
			case lowerLegsID:
				modelPartToRender = leftHandSide ? leftLegLower : rightLegLower;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftLegID : rightLegID));
				break;
		}

		if (modelPartToRender == null)
		{
			return;
		}

		IHasMetalType item = (IHasMetalType) stack.getItem();

		Color color = item.getMetalType().getColor();
		VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(buffer, renderType(TEXTURE), false, false);


		modelPartToRender.render(
				matrixStack,
				vertexBuilder,
				light,
				OverlayTexture.NO_OVERLAY,
				color.getRed() / 255f,
				color.getGreen() / 255f,
				color.getBlue() / 255f,
				1);

		/*
		//debug only, so a spike will render in all locations
		renderToBuffer(
				matrixStack,
				vertexBuilder,
				light,
				OverlayTexture.NO_OVERLAY,
				color.getRed(),
				color.getGreen(),
				color.getBlue(),
				1);*/
	}
}

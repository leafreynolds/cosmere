/*
 * File created ~ 30 - 5 - 2022 ~ Leaf
 */

package leaf.cosmere.client.render.curio.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.items.IHasMetalType;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
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
	private static final ResourceLocation TEXTURE = ResourceLocationHelper.prefix("textures/block/metal_block.png");

// region string IDs
	private static final String leftArmID = "left_arm";
	private static final String rightArmID = "right_arm";
	private static final String leftLegID = "left_leg";
	private static final String rightLegID = "right_leg";
	private static final String headID = "head";
	private static final String eyeRootID = "eyeRoot";
	private static final String backID = "back";
	private static final String bodyID = "body";
	private static final String linchpinID = "linchpin";
	private static final String braceletID = "bracelet";
	private static final String handsID = "hands";
	private static final String legsID = "legs";
	private static final String feetID = "feet";
//endregion

//region model parts
	private final ModelPart root;
	private final ModelPart eyeRoot;
	private final ModelPart eyeLeft;
	private final ModelPart eyeRight;
	private final ModelPart linchpin;
	private final ModelPart back;
	private final ModelPart ribLeft;
	private final ModelPart ribRight;
	private final ModelPart rightArmUpper;
	private final ModelPart rightArmLower;
	private final ModelPart rightHand;
	private final ModelPart leftArmUpper;
	private final ModelPart leftArmLower;
	private final ModelPart leftHand;
	private final ModelPart rightLegUpper;
	private final ModelPart rightLegLower;
	private final ModelPart leftLegUpper;
	private final ModelPart leftLegLower;
//endregion

	public SpikeModel(ModelPart part)
	{
		super(part, RenderType::entityCutoutNoCull);
		this.root = part;
		ModelPart find = this.root.getChild(headID);

		eyeRoot = find.getChild(eyeRootID);
		eyeLeft = eyeRoot.getChild("left_eye");
		eyeRight = eyeRoot.getChild("right_eye");

		find = part.getChild(bodyID);
		linchpin = find.getChild(linchpinID);
		back = find.getChild("back");
		ribLeft = find.getChild("rib_left");
		ribRight = find.getChild("rib_right");

		find = part.getChild(rightArmID);
		rightArmUpper = find.getChild("upper_spike");
		rightArmLower = find.getChild("lower_spike");
		rightHand = find.getChild("hand");

		find = part.getChild(leftArmID);
		leftArmUpper = find.getChild("upper_spike");
		leftArmLower = find.getChild("lower_spike");
		leftHand = find.getChild("hand");

		find = part.getChild(rightLegID);
		rightLegUpper = find.getChild("upper_spike");
		rightLegLower = find.getChild("lower_spike");

		find = part.getChild(leftLegID);
		leftLegUpper = find.getChild("upper_spike");
		leftLegLower = find.getChild("lower_spike");
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
				"right_eye",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.0F, -3.0F, -5.0F, 2.0F, 2.0F, 10, cube),
				PartPose.ZERO);

		final String left_eye = "left_eye";
		eyeRoot.addOrReplaceChild(
				left_eye,
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.0F, -3.0F, -5.0F, 2.0F, 2.0F, 10, cube),
				PartPose.ZERO);


		final PartDefinition body = part.addOrReplaceChild(
				bodyID,
				CubeListBuilder.create(),
				PartPose.ZERO
		);

		body.addOrReplaceChild(
				linchpinID,
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.5F, 1.5F, 2.0F, 1.0F, 1.0F, 2.0F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"back",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-1F, 3.5f, 0.0F, 2.0F, 2.0F, 3.0F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"rib_right",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-2.5F, 7.0F, -2.5f, 0.5F, 0.5F, 5.0F, cube),
				PartPose.ZERO);

		body.addOrReplaceChild(
				"rib_left",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(2F, 7.0F, -2.5f, 0.5F, 0.5F, 5.0F, cube),
				PartPose.ZERO);


		final PartDefinition rightArm = part.addOrReplaceChild(
				rightArmID,
				CubeListBuilder.create(),
				PartPose.ZERO
		);

		rightArm.addOrReplaceChild("upper_spike",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.5f, 0, -0.5F, 2, 1, 1, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("lower_spike",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.5f, 3, -0.5F, 2, 1, 1, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("hand",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.5f, 6, -0.5F, 2, 1, 1, cube),
				PartPose.ZERO);


		final PartDefinition leftArm = part.addOrReplaceChild(
				leftArmID,
				CubeListBuilder.create().mirror(),
				PartPose.ZERO
		);

		leftArm.addOrReplaceChild("upper_spike",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.5f, 0, -0.5F, 2, 1, 1, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("lower_spike",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.5f, 3, -0.5F, 2, 1, 1, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("hand",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.5f, 6, -0.5F, 2, 1, 1, cube),
				PartPose.ZERO);

		final PartDefinition rightLeg = part.addOrReplaceChild(
				rightLegID,
				CubeListBuilder.create(),
				PartPose.ZERO
		);

		rightLeg.addOrReplaceChild("upper_spike",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.5F, 2.0F, -2.5F, 1, 1, 5, cube),
				PartPose.ZERO);

		rightLeg.addOrReplaceChild("lower_spike",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.5F, 7.0F, -2.5F, 1, 1, 5, cube),
				PartPose.ZERO);


		final PartDefinition leftLeg = part.addOrReplaceChild(
				leftLegID,
				CubeListBuilder.create().mirror(),
				PartPose.ZERO
		);

		leftLeg.addOrReplaceChild("upper_spike",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.5F, 2.0F, -2.5F, 1, 1, 5, cube),
				PartPose.ZERO);

		leftLeg.addOrReplaceChild("lower_spike",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.5F, 7.0F, -2.5F, 1, 1, 5, cube),
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
			case headID:
				eyeLeft.visible  = leftHandSide;
				eyeRight.visible  = !leftHandSide;

				SpiritwebCapability.get(slotContext.entity()).ifPresent((data)->{
					eyeRoot.y = data.getEyeHeight();
				});

				//we can call render on the head directly, since we have made other spike not visible.
				//we do this so that we can use the eye height and have it follow proper head rotations
				modelPartToRender = head;

				break;
			case linchpinID:
				modelPartToRender = linchpin;
				modelPartToRender.copyFrom(this.root.getChild(bodyID));

				modelPartToRender.y = -1;
				break;
			case backID:
				modelPartToRender = back;
				modelPartToRender.copyFrom(this.root.getChild(bodyID));
				break;
			case bodyID:
				modelPartToRender = leftHandSide ? ribLeft : ribRight;
				modelPartToRender.copyFrom(this.root.getChild(bodyID));
				break;
			case braceletID:
				switch (slotContext.index())
				{
					case 0:
						modelPartToRender = leftArmUpper;
						break;
					case 1:
						modelPartToRender = rightArmUpper;
						break;
					case 2:
						modelPartToRender = leftArmLower;
						break;
					case 3:
						modelPartToRender = rightArmLower;
						break;
				}

				if (modelPartToRender != null)
				{
					modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftArmID : rightArmID));
				}
				break;
			case handsID:
				modelPartToRender = leftHandSide ? leftHand : rightHand;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftArmID : rightArmID));
				break;
			case legsID:
				modelPartToRender = slotContext.index() == 0 ? leftLegUpper : rightLegUpper;
				modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftLegID : rightLegID));
				break;
			case feetID:
				modelPartToRender = slotContext.index() == 0 ? leftLegLower : rightLegLower;
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

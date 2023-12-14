/*
 * File updated ~ 11 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.client.render.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
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

public class BraceletModel extends HumanoidModel<LivingEntity>
{
	private static final ResourceLocation TEXTURE = Cosmere.rl("textures/block/metal_block.png");

	// Body Parts
	private static final String leftArmID = "left_arm";
	private static final String rightArmID = "right_arm";

	// Categories
	private static final String braceletID = "bracelet";

	// Model Parts
	private final ModelPart root;
	private final ModelPart rightShoulderSide;
	private final ModelPart rightArmUpper;
	private final ModelPart rightArmMiddle;
	private final ModelPart rightArmLower;

	private final ModelPart leftShoulderSide;
	private final ModelPart leftArmUpper;
	private final ModelPart leftArmMiddle;
	private final ModelPart leftArmLower;

	public BraceletModel(ModelPart part)
	{
		super(part, RenderType::entityCutoutNoCull);
		this.root = part;
		ModelPart find = part.getChild(rightArmID);
		rightShoulderSide = find.getChild("shoulder_side");
		rightArmUpper = find.getChild("upper");
		rightArmMiddle = find.getChild("middle");
		rightArmLower = find.getChild("lower");

		find = part.getChild(leftArmID);
		leftShoulderSide = find.getChild("shoulder_side");
		leftArmUpper = find.getChild("upper");
		leftArmMiddle = find.getChild("middle");
		leftArmLower = find.getChild("lower");
	}

	public static LayerDefinition createLayer()
	{
		CubeDeformation cube = new CubeDeformation(0.4F);
		MeshDefinition mesh = HumanoidModel.createMesh(cube, 0.0F);
		PartDefinition part = mesh.getRoot();

		final PartDefinition rightArm = part.addOrReplaceChild(
				rightArmID,
				CubeListBuilder.create(),
				PartPose.ZERO
		);

		rightArm.addOrReplaceChild("shoulder_side",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-2.9F, 1.0F, -2.0F, 3.0F, 0.5F, 4.0F, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("upper",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-2.9F, 3.0F, -2.0F, 3.0F, 0.5F, 4.0F, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("middle",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-2.9F, 5.0F, -2.0F, 3.0F, 0.5F, 4.0F, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("lower",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-2.9F, 7.0F, -2.0F, 3.0F, 0.5F, 4.0F, cube),
				PartPose.ZERO);

		final PartDefinition leftArm = part.addOrReplaceChild(
				leftArmID,
				CubeListBuilder.create().mirror(),
				PartPose.ZERO
		);

		leftArm.addOrReplaceChild("shoulder_side",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.1F, 1.0F, -2.0F, 3.0F, 0.5F, 4.0F, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("upper",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.1F, 3.0F, -2.0F, 3.0F, 0.5F, 4.0F, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("middle",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.1F, 5.0F, -2.0F, 3.0F, 0.5F, 4.0F, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("lower",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-0.1F, 7.0F, -2.0F, 3.0F, 0.5F, 4.0F, cube),
				PartPose.ZERO);

		return LayerDefinition.create(mesh, 16, 16);
	}

	@Override
	@Nonnull
	protected Iterable<ModelPart> headParts()
	{
		return ImmutableList.of();
	}

	@Override
	@Nonnull
	protected Iterable<ModelPart> bodyParts()
	{
		return ImmutableList.of(
				this.rightArm,
				this.leftArm);
	}

	public void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, MultiBufferSource buffer, int light)
	{
		final boolean leftHandSide = slotContext.index() % 2 == 0;

		ModelPart modelPartToRender = null;
		if (slotContext.identifier().equals(braceletID))
		{
			modelPartToRender = switch (slotContext.index())
			{
				case 0 -> leftShoulderSide;
				case 1 -> rightShoulderSide;
				case 2 -> leftArmUpper;
				case 3 -> rightArmUpper;
				case 4 -> leftArmMiddle;
				case 5 -> rightArmMiddle;
				case 6 -> leftArmLower;
				case 7 -> rightArmLower;
				default -> null;
			};
		}

		if (modelPartToRender == null)
		{
			return;
		}

		modelPartToRender.copyFrom(this.root.getChild(leftHandSide ? leftArmID : rightArmID));

		IHasMetalType item = (IHasMetalType) stack.getItem();

		Color color = item.getMetalType().getColor();
		VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(
				buffer,
				renderType(TEXTURE),
				false,
				stack.getItem() == FeruchemyItems.BANDS_OF_MOURNING.get());


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

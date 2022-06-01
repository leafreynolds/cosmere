/*
 * Copyright (c) 2018-2020 C4
 *
 * This file is part of Curios, a mod made for Minecraft.
 *
 * Curios is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Curios is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Curios.  If not, see <https://www.gnu.org/licenses/>.
 */

package leaf.cosmere.client.render.curio.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import leaf.cosmere.items.IHasMetalType;
import leaf.cosmere.registry.ItemsRegistry;
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

public class BraceletModel extends HumanoidModel<LivingEntity>
{
	private static final ResourceLocation TEXTURE = ResourceLocationHelper.prefix("textures/block/metal_block.png");

	// region string IDs
	private static final String leftArmID = "left_arm";
	private static final String rightArmID = "right_arm";
	private static final String braceletID = "bracelet";
	private static final String handsID = "hands";
//endregion

	//region model parts
	private final ModelPart root;
	private final ModelPart rightArmUpper;
	private final ModelPart rightArmLower;
	private final ModelPart rightHand;
	private final ModelPart leftArmUpper;
	private final ModelPart leftArmLower;
	private final ModelPart leftHand;
//endregion

	public BraceletModel(ModelPart part)
	{
		super(part, RenderType::entityCutoutNoCull);
		this.root = part;
		ModelPart find = part.getChild(rightArmID);
		rightArmUpper = find.getChild("upper_spike");
		rightArmLower = find.getChild("lower_spike");
		rightHand = find.getChild("hand");

		find = part.getChild(leftArmID);
		leftArmUpper = find.getChild("upper_spike");
		leftArmLower = find.getChild("lower_spike");
		leftHand = find.getChild("hand");
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

		rightArm.addOrReplaceChild("upper_spike",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.5f, 0, -2.5F, 2, 1, 5, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("lower_spike",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.5f, 3, -2.5F, 2, 1, 5, cube),
				PartPose.ZERO);

		rightArm.addOrReplaceChild("hand",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.5f, 6, -2.5F, 2, 1, 5, cube),
				PartPose.ZERO);


		final PartDefinition leftArm = part.addOrReplaceChild(
				leftArmID,
				CubeListBuilder.create().mirror(),
				PartPose.ZERO
		);

		leftArm.addOrReplaceChild("upper_spike",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.5f, 0, -2.5F, 2, 1, 5, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("lower_spike",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.5f, 3, -2.5F, 2, 1, 5, cube),
				PartPose.ZERO);

		leftArm.addOrReplaceChild("hand",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(1.5f, 6, -2.5F, 2, 1, 5, cube),
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
		switch (slotContext.identifier())
		{
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
		}

		if (modelPartToRender == null)
		{
			return;
		}

		IHasMetalType item = (IHasMetalType) stack.getItem();

		Color color = item.getMetalType().getColor();
		VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(
				buffer,
				renderType(TEXTURE),
				false,
				stack.getItem() == ItemsRegistry.BANDS_OF_MOURNING.get());


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

/*
 * File created ~ 12 - 9 - 2022 ~ Leaf
 */

package leaf.cosmere.client.render.armor;

// Made with Blockbench 4.3.1
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import leaf.cosmere.items.IHasColour;
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

import java.awt.*;

public class ShardplateModel extends HumanoidModel<LivingEntity>
{
	private static final ResourceLocation TEXTURE = ResourceLocationHelper.prefix("textures/item/models/shardplate.png");

	private final ModelPart Root;

	public ShardplateModel(ModelPart root)
	{
		super(root, RenderType::entityCutoutNoCull);

		this.Root = root;
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = mesh.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(4, 24).addBox(7.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(4, 24).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -8.1481F, 0.3266F, -0.2182F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.6F))
				.texOffs(18, 28).addBox(-2.1F, 10.6F, -4.7F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.6F))
				.texOffs(18, 28).addBox(-1.9F, 10.6F, -4.7F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));


		return LayerDefinition.create(mesh, 64, 64);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		//we handle rendering our own way
	}


	public void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, MultiBufferSource buffer, int light)
	{
		Color color = stack.getItem() instanceof IHasColour hasColour ? hasColour.getColour() : Color.white;
		VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(
				buffer,
				renderType(TEXTURE),
				false,
				stack.getItem() == ItemsRegistry.BANDS_OF_MOURNING.get());

		switch (LivingEntity.getEquipmentSlotForItem(stack))
		{
			case FEET:
				//ImmutableList.of(this.leftLeg,this.rightLeg)
				//		.forEach(x -> renderPart(matrixStack, vertexBuilder, light, color, x));
				break;
			case LEGS:
				ImmutableList.of(this.leftLeg,this.rightLeg)
						.forEach(x -> renderPart(matrixStack, vertexBuilder, light, color, x));
				break;
			case CHEST:
				ImmutableList.of(this.leftArm,this.rightArm, this.body)
						.forEach(x -> renderPart(matrixStack, vertexBuilder, light, color, x));
				break;
			case HEAD:
				ImmutableList.of(this.head)
						.forEach(x -> renderPart(matrixStack, vertexBuilder, light, color, x));
				break;
		}

	}

	private void renderPart(PoseStack matrixStack, VertexConsumer vertexBuilder, int light, Color color, ModelPart root)
	{
		root.render(
				matrixStack,
				vertexBuilder,
				light,
				OverlayTexture.NO_OVERLAY,
				color.getRed() / 255f,
				color.getGreen() / 255f,
				color.getBlue() / 255f,
				1);
	}
}
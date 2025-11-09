/*
 * File updated ~ 14 - 8 - 2023 ~ Leaf
 * File updated ~ 12 - 7 - 2025 ~ Soar
 */

package leaf.cosmere.allomancy.client.render.model;

// Made with Blockbench 4.3.1
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import leaf.cosmere.allomancy.common.Allomancy;
import net.minecraft.client.model.HumanoidArmorModel;
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
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.NotNull;

public class MistcloakModel extends HumanoidModel<LivingEntity>
{
	private static final ResourceLocation TEXTURE = Allomancy.rl("textures/models/armor/mistcloak.png");


	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart right_arm;
	private final ModelPart body;
	private final ModelPart left_arm;

	public MistcloakModel(ModelPart root) {
		super(root.getChild("root"));
		this.root = root.getChild("root");
		this.head = this.root.getChild("head");
		this.right_arm = this.root.getChild("right_arm");
		this.body = this.root.getChild("body");
		this.left_arm = this.root.getChild("left_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, -0.25F, 0.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -9.75F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.75F, 0.0F));

		PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(28, 33).addBox(-3.5F, -3.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(16, 37).addBox(-1.25F, 5.0F, 2.5F, 2.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(40, 0).addBox(-3.5F, 5.0F, 2.5F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.25F, 0.0F));


		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 20).addBox(-4.5F, 0.0F, -2.5F, 9.0F, 12.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 37).addBox(0.25F, 12.0F, 2.5F, 2.0F, 11.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(4, 37).addBox(-2.25F, 12.0F, 2.5F, 2.0F, 12.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(20, 37).addBox(2.5F, 12.0F, 2.5F, 2.0F, 9.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(8, 37).addBox(-4.5F, 12.0F, 2.5F, 2.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.75F, 0.0F));

		PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(12, 37).addBox(-0.75F, 5.0F, 2.5F, 2.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(24, 37).addBox(1.5F, 5.0F, 2.5F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(28, 20).addBox(-1.5F, -3.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.25F, 0.0F));

		PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(-5.0F, 24.25F, 3.0F));
		PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 24.25F, 0.0F));
		PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.25F, 0.0F));


		return LayerDefinition.create(meshdefinition, 64, 64);
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

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void render(PoseStack matrixStack, MultiBufferSource buffer, int light)
	{
		VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(buffer, renderType(TEXTURE), false, false);

		this.hat.visible = false;
		this.head.visible = true;
		this.body.visible = true;
		this.rightArm.visible = true;
		this.leftArm.visible = true;

		this.rightLeg.visible = false;
		this.leftLeg.visible = false;


		renderToBuffer(
				matrixStack,
				vertexBuilder,
				light,
				OverlayTexture.NO_OVERLAY,
				1,
				1,
				1,
				1
		);
	}
}
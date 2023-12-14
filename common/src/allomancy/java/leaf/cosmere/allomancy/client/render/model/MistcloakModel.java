/*
 * File updated ~ 14 - 8 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.client.render.model;

// Made with Blockbench 4.3.1
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import leaf.cosmere.allomancy.common.Allomancy;
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

public class MistcloakModel extends HumanoidModel<LivingEntity>
{
	private static final ResourceLocation TEXTURE = Allomancy.rl("textures/models/armor/mistcloak.png");

	private final ModelPart Root;
	private final ModelPart hat;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart left_arm;
	private final ModelPart right_arm;

	public MistcloakModel(ModelPart root)
	{
		super(root, RenderType::entityCutoutNoCull);

		this.Root = root;
		this.hat = root.getChild("hat");
		this.left_leg = root.getChild("left_leg");
		this.right_leg = root.getChild("right_leg");
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.left_arm = root.getChild("left_arm");
		this.right_arm = root.getChild("right_arm");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(24, 0).addBox(4.0F, -8.0F, -4.0F, 0.125F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 24).addBox(-4.125F, -8.0F, -4.0F, 0.125F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(16, 37).addBox(-4.125F, -8.0F, 4.0F, 8.25F, 8.0F, 0.0625F, new CubeDeformation(0.0F))
				.texOffs(2, 8).addBox(-4.125F, -8.0F, -4.0625F, 1.0F, 8.0F, 0.0625F, new CubeDeformation(0.0F))
				.texOffs(16, 0).addBox(-4.125F, -8.0625F, -4.0625F, 8.25F, 0.0625F, 8.125F, new CubeDeformation(0.0F))
				.texOffs(0, 8).addBox(3.125F, -8.0F, -4.0625F, 1.0F, 8.0F, 0.0625F, new CubeDeformation(0.0F))
				.texOffs(36, 48).addBox(-3.125F, -8.0F, -4.0625F, 6.25F, 1.0F, 0.0625F, new CubeDeformation(0.0F))
				.texOffs(36, 47).addBox(-3.125F, -8.0F, -4.0625F, 6.25F, 1.0F, 0.0625F, new CubeDeformation(0.0F))
				.texOffs(2, 0).addBox(-4.125F, -8.0F, -4.0625F, 1.0F, 8.0F, 0.0625F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-4.125F, -8.0625F, -4.0625F, 8.25F, 0.0625F, 8.125F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.125F, -8.0F, -4.0625F, 1.0F, 8.0F, 0.0625F, new CubeDeformation(0.0F))
				.texOffs(16, 21).addBox(4.0F, -8.0F, -4.0F, 0.125F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 16).addBox(-4.125F, -8.0F, -4.0F, 0.125F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 16).addBox(-4.125F, 0.0F, -4.0625F, 8.25F, 0.0625F, 8.125F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(20, 20).addBox(-4.0F, -8.4745F, -2.5423F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 8).addBox(-4.125F, -8.4745F, -4.6048F, 8.25F, 0.0625F, 8.125F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 32.4745F, 0.5423F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(6.0F, -1.1684F, 1.7913F, 1.0F, 0.25F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(11, 24).addBox(5.0F, -1.1684F, 2.7913F, 1.0F, 0.25F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 12).addBox(8.0F, -1.1059F, -1.2087F, 1.0F, 0.1875F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(30, 16).addBox(8.0F, -1.0434F, -10.2087F, 1.0F, 0.125F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(29, 44).addBox(8.0F, -1.1059F, -6.2087F, 1.0F, 0.1875F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(13, 24).addBox(8.0F, -1.1684F, 2.7913F, 1.0F, 0.25F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(34, 41).addBox(7.0F, -1.1059F, -4.2087F, 1.0F, 0.1875F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 6).addBox(7.0F, -1.1684F, 1.7913F, 1.0F, 0.25F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(18, 45).addBox(7.0F, -1.0434F, -9.2087F, 1.0F, 0.125F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 12).addBox(9.0F, -1.1684F, 1.7913F, 1.0F, 0.25F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(36, 41).addBox(9.0F, -1.1059F, -4.2087F, 1.0F, 0.1875F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(20, 45).addBox(9.0F, -1.0434F, -9.2087F, 1.0F, 0.125F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(22, 45).addBox(2.0F, -1.0434F, -9.2087F, 1.0F, 0.125F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(38, 41).addBox(2.0F, -1.1059F, -4.2087F, 1.0F, 0.1875F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 18).addBox(2.0F, -1.1684F, 1.7913F, 1.0F, 0.25F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(24, 45).addBox(4.0F, -1.0434F, -9.2087F, 1.0F, 0.125F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(38, 27).addBox(4.0F, -1.1684F, 1.7913F, 1.0F, 0.25F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(40, 41).addBox(4.0F, -1.1059F, -4.2087F, 1.0F, 0.1875F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(39, 7).addBox(3.0F, -1.1684F, 2.7913F, 1.0F, 0.25F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 45).addBox(3.0F, -1.1059F, -6.2087F, 1.0F, 0.1875F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(36, 20).addBox(3.0F, -1.0434F, -10.2087F, 1.0F, 0.125F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 16).addBox(3.0F, -1.1059F, -1.2087F, 1.0F, 0.1875F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -0.611F, -2.0574F, 1.4835F, 0.0F, 0.0F));

		PartDefinition body_r2 = body.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(23, 29).addBox(0.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(25, 38).addBox(0.0F, -0.0938F, -1.25F, 1.0F, 0.1875F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(6, 40).addBox(0.0F, -0.0313F, 5.75F, 1.0F, 0.125F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(42, 27).addBox(1.0F, -0.0938F, -2.25F, 1.0F, 0.1875F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(30, 29).addBox(1.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(44, 20).addBox(1.0F, -0.0313F, 3.75F, 1.0F, 0.125F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(26, 45).addBox(3.0F, -0.0313F, 1.75F, 1.0F, 0.125F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(40, 27).addBox(3.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(42, 41).addBox(3.0F, -0.0938F, -4.25F, 1.0F, 0.1875F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(41, 0).addBox(2.0F, -0.0313F, 0.5F, 1.0F, 0.125F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(39, 0).addBox(2.0F, -0.0938F, -6.5F, 1.0F, 0.1875F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(44, 47).addBox(2.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 3.75F, new CubeDeformation(0.0F))
				.texOffs(0, 22).addBox(-3.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(39, 20).addBox(-3.0F, -0.0938F, -8.25F, 1.0F, 0.1875F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(41, 7).addBox(-3.0F, -0.0313F, -1.25F, 1.0F, 0.125F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(44, 0).addBox(-4.0F, -0.0938F, -4.25F, 1.0F, 0.1875F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(30, 41).addBox(-4.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(45, 26).addBox(-4.0F, -0.0313F, 1.75F, 1.0F, 0.125F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(45, 31).addBox(-2.0F, -0.0313F, 1.75F, 1.0F, 0.125F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(32, 41).addBox(-2.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(44, 6).addBox(-2.0F, -0.0938F, -4.25F, 1.0F, 0.1875F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(41, 20).addBox(-1.0F, -0.0313F, 3.75F, 1.0F, 0.125F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-1.0F, -0.0938F, -3.25F, 1.0F, 0.1875F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(37, 0).addBox(-1.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.6469F, 3.0837F, -1.3963F, 0.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(36, 33).addBox(-2.836F, -6.3344F, -2.2414F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(6.836F, 30.3344F, 0.2414F));

		PartDefinition left_arm_r1 = left_arm.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -1.1684F, 5.7913F, 1.0F, 0.25F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(37, 20).addBox(-2.0F, -1.1059F, -1.2087F, 1.0F, 0.1875F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(2, 40).addBox(-2.0F, -1.0434F, -8.2087F, 1.0F, 0.125F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.836F, 1.5291F, -1.7565F, 1.4835F, 0.0F, 0.0F));

		PartDefinition left_arm_r2 = left_arm.addOrReplaceChild("left_arm_r2", CubeListBuilder.create().texOffs(35, 7).addBox(0.5F, -0.1563F, 2.75F, 1.0F, 0.25F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(3, 47).addBox(0.5F, -0.0313F, -4.25F, 1.0F, 0.125F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(49, 0).addBox(0.5F, -0.0938F, -0.25F, 1.0F, 0.1875F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(33, 0).addBox(-0.5F, -0.1563F, 0.75F, 1.0F, 0.25F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(26, 16).addBox(-0.5F, -0.0938F, -3.25F, 1.0F, 0.1875F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(49, 15).addBox(-0.5F, -0.0313F, -6.25F, 1.0F, 0.125F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(26, 29).addBox(-1.5F, -0.1563F, -0.25F, 1.0F, 0.25F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(49, 3).addBox(-1.5F, -0.0938F, -3.25F, 1.0F, 0.1875F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(5, 47).addBox(-1.5F, -0.0313F, -7.25F, 1.0F, 0.125F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.336F, 1.3997F, -2.7612F, 1.4835F, 0.0F, 0.0F));

		PartDefinition left_arm_r3 = left_arm.addOrReplaceChild("left_arm_r3", CubeListBuilder.create().texOffs(51, 26).addBox(-3.0F, -2.0313F, 8.5F, 2.0F, 0.125F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(42, 49).addBox(-5.0F, -2.0938F, 8.5F, 2.0F, 0.1875F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6034F, 2.9033F, -7.7414F, 0.0F, 0.0F, 1.3963F));

		PartDefinition left_arm_r4 = left_arm.addOrReplaceChild("left_arm_r4", CubeListBuilder.create().texOffs(49, 50).addBox(-8.5F, -2.1563F, 8.5F, 4.0F, 0.25F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(51, 27).addBox(-0.5F, -2.0313F, 7.5F, 2.0F, 0.125F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(14, 50).addBox(-5.5F, -2.0938F, 7.5F, 5.0F, 0.1875F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(40, 50).addBox(-8.5F, -2.1563F, 7.5F, 3.0F, 0.25F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(49, 36).addBox(-8.5F, -2.1563F, 6.5F, 5.0F, 0.25F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(20, 51).addBox(-3.5F, -2.0938F, 6.5F, 3.0F, 0.1875F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(51, 23).addBox(-0.5F, -2.0313F, 6.5F, 3.0F, 0.125F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(51, 28).addBox(-1.5F, -2.0313F, 5.5F, 2.0F, 0.125F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(51, 21).addBox(-4.5F, -2.0938F, 5.5F, 3.0F, 0.1875F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 51).addBox(-8.5F, -2.1563F, 5.5F, 4.0F, 0.25F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5165F, 2.4109F, -7.7414F, 0.0F, 0.0F, 1.3963F));

		PartDefinition left_arm_r5 = left_arm.addOrReplaceChild("left_arm_r5", CubeListBuilder.create().texOffs(36, 29).addBox(6.0F, -0.0313F, -3.25F, 1.0F, 0.125F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(38, 20).addBox(7.0F, -0.0313F, -4.25F, 1.0F, 0.125F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(46, 14).addBox(7.0F, -0.0938F, -6.25F, 1.0F, 0.1875F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 20).addBox(6.0F, -0.0938F, -7.25F, 1.0F, 0.1875F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(7.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(6.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(4.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 4).addBox(5.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(10, 45).addBox(5.0F, -0.0938F, -6.25F, 1.0F, 0.1875F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(20, 16).addBox(4.0F, -0.0938F, -5.25F, 1.0F, 0.1875F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(12, 45).addBox(4.0F, -0.0938F, -1.25F, 1.0F, 0.1875F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(38, 29).addBox(5.0F, -0.0313F, -1.25F, 1.0F, 0.125F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(44, 33).addBox(4.0F, -0.0313F, 3.75F, 1.0F, 0.125F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.836F, 3.787F, 3.3846F, -1.3963F, 0.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(36, 12).addBox(-1.164F, -6.2307F, -2.2232F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.836F, 30.2307F, 0.2232F));

		PartDefinition right_arm_r1 = right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(7, 47).addBox(0.5F, -0.0313F, -7.25F, 1.0F, 0.125F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(49, 6).addBox(0.5F, -0.0938F, -3.25F, 1.0F, 0.1875F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(28, 29).addBox(0.5F, -0.1563F, -0.25F, 1.0F, 0.25F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(49, 18).addBox(-0.5F, -0.0313F, -6.25F, 1.0F, 0.125F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(28, 16).addBox(-0.5F, -0.0938F, -3.25F, 1.0F, 0.1875F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(35, 0).addBox(-0.5F, -0.1563F, 0.75F, 1.0F, 0.25F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(49, 9).addBox(-1.5F, -0.0938F, -0.25F, 1.0F, 0.1875F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(9, 47).addBox(-1.5F, -0.0313F, -4.25F, 1.0F, 0.125F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(37, 7).addBox(-1.5F, -0.1563F, 2.75F, 1.0F, 0.25F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.336F, 1.5034F, -2.743F, 1.4835F, 0.0F, 0.0F));

		PartDefinition right_arm_r2 = right_arm.addOrReplaceChild("right_arm_r2", CubeListBuilder.create().texOffs(4, 40).addBox(1.0F, -1.0434F, -8.2087F, 1.0F, 0.125F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(27, 37).addBox(1.0F, -1.1059F, -1.2087F, 1.0F, 0.1875F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 18).addBox(1.0F, -1.1684F, 5.7913F, 1.0F, 0.25F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.836F, 1.6327F, -1.7383F, 1.4835F, 0.0F, 0.0F));

		PartDefinition right_arm_r3 = right_arm.addOrReplaceChild("right_arm_r3", CubeListBuilder.create().texOffs(24, 50).addBox(4.5F, -2.1563F, -6.5F, 4.0F, 0.25F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(8, 51).addBox(1.5F, -2.0938F, -6.5F, 3.0F, 0.1875F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(51, 24).addBox(-0.5F, -2.0313F, -6.5F, 2.0F, 0.125F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(51, 22).addBox(-2.5F, -2.0313F, -7.5F, 3.0F, 0.125F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(14, 51).addBox(0.5F, -2.0938F, -7.5F, 3.0F, 0.1875F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(32, 49).addBox(3.5F, -2.1563F, -7.5F, 5.0F, 0.25F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 50).addBox(5.5F, -2.1563F, -8.5F, 3.0F, 0.25F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(49, 49).addBox(0.5F, -2.0938F, -8.5F, 5.0F, 0.1875F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(51, 25).addBox(-1.5F, -2.0313F, -8.5F, 2.0F, 0.125F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(32, 50).addBox(4.5F, -2.1563F, -9.5F, 4.0F, 0.25F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 49).addBox(2.5F, -2.0938F, -9.5F, 2.0F, 0.1875F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(26, 51).addBox(0.5F, -2.0313F, -9.5F, 2.0F, 0.125F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5165F, 2.5146F, 7.2768F, 0.0F, 0.0F, -1.3963F));

		PartDefinition right_arm_r4 = right_arm.addOrReplaceChild("right_arm_r4", CubeListBuilder.create().texOffs(49, 12).addBox(-8.0F, -0.0313F, -5.25F, 1.0F, 0.125F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 46).addBox(-8.0F, -0.0938F, -8.25F, 1.0F, 0.1875F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 20).addBox(-8.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(22, 16).addBox(-5.0F, -0.0938F, -5.25F, 1.0F, 0.1875F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(46, 12).addBox(-5.0F, -0.0313F, 3.75F, 1.0F, 0.125F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(14, 45).addBox(-5.0F, -0.0938F, -1.25F, 1.0F, 0.1875F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(41, 7).addBox(-5.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(46, 41).addBox(-6.0F, -0.0313F, -1.25F, 1.0F, 0.125F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(16, 45).addBox(-6.0F, -0.0938F, -6.25F, 1.0F, 0.1875F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 8).addBox(-6.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(46, 45).addBox(-7.0F, -0.0313F, -3.25F, 1.0F, 0.125F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(24, 16).addBox(-7.0F, -0.0938F, -7.25F, 1.0F, 0.1875F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 43).addBox(-7.0F, -0.1563F, -10.25F, 1.0F, 0.25F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.836F, 3.8907F, 3.4029F, -1.3963F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
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


		Root.render(
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
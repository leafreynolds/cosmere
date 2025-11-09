/*
* File created ~ 12 - 7 - 2025 ~ Soar
 */
package leaf.cosmere.surgebinding.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardplateData;
import leaf.cosmere.surgebinding.common.capabilities.ShardData;
import leaf.cosmere.surgebinding.common.eventHandlers.SurgebindingCapabilitiesHandler;
import leaf.cosmere.surgebinding.common.items.ShardplateCurioItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import java.awt.*;

public class DynamicShardplateModel extends HumanoidModel<LivingEntity>
{
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ResourceLocation TEXTURE = Surgebinding.rl("textures/models/armor/shardplate_base.png");
	public static final ResourceLocation VISOR = Surgebinding.rl("textures/models/armor/shardplate_visors.png");
	public static final ResourceLocation CRACKS1 = Surgebinding.rl("textures/models/armor/shardplate_cracks_1.png");
	public static final ResourceLocation TRIM = Surgebinding.rl("textures/models/armor/shardplate_trims.png");

	public static final int TOTAL_HELMET_IDS = 1;
	public static final int TOTAL_FACEPLATE_IDS = 4;
	public static final int TOTAL_ARM_IDS = 1;
	public static final int TOTAL_PALDRON_IDS = 2;
	public static final int TOTAL_TORSO_IDS = 1;
	public static final int TOTAL_LEG_IDS = 1;
	public static final int TOTAL_KAMA_IDS = 1;
	public static final int TOTAL_BOOT_IDS = 1;

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "shardplate"), "main");
	public final ModelPart root;
	public final ModelPart head;
	public final ModelPart head1;
	public final ModelPart faceplate;
	public final ModelPart faceplate1;
	public final ModelPart faceplate2;
	public final ModelPart faceplate3;
	public final ModelPart faceplate4;
	public final ModelPart body;
	public final ModelPart body1;
	public final ModelPart chestplate1;
	public final ModelPart left_leg;
	public final ModelPart left_leg_top;
	public final ModelPart leftleg_top1;
	public final ModelPart left_boot;
	public final ModelPart left_boot_outside;
	public final ModelPart leftboot_outside1;
	public final ModelPart left_boot_tip;
	public final ModelPart leftboot_tip1;
	public final ModelPart right_leg;
	public final ModelPart right_leg_top;
	public final ModelPart rightleg_top1;
	public final ModelPart right_boot;
	public final ModelPart right_boot_outside;
	public final ModelPart rightboot_outside1;
	public final ModelPart right_boot_tip;
	public final ModelPart rightboot_tip1;
	public final ModelPart kama;
	public final ModelPart kama1;
	public final ModelPart left_kama;
	public final ModelPart left_front_kama;
	public final ModelPart right_kama;
	public final ModelPart right_front_kama;
	public final ModelPart right_arm;
	public final ModelPart right_arm_main;
	public final ModelPart right_armmain1;
	public final ModelPart right_paldron;
	public final ModelPart right_paldron1;
	public final ModelPart right_paldron2;
	public final ModelPart left_arm;
	public final ModelPart left_arm_main;
	public final ModelPart left_armmain1;
	public final ModelPart left_paldron;
	public final ModelPart left_paldron1;
	public final ModelPart left_paldron2;
	public final ModelPart hat;

	public Color color;

	public DynamicShardplateModel(ModelPart root) {

		super(root.getChild("root"));

		this.root = root.getChild("root");
		this.head = this.root.getChild("head");
		this.head1 = this.head.getChild("head1");

		this.faceplate = this.head.getChild("faceplate");
		this.faceplate1 = this.faceplate.getChild("faceplate1");
		this.faceplate2 = this.faceplate.getChild("faceplate2");
		this.faceplate3 = this.faceplate.getChild("faceplate3");
		this.faceplate4 = this.faceplate.getChild("faceplate4");

		this.body = this.root.getChild("body");
		this.body1 = this.body.getChild("body1");
		this.chestplate1 = this.body1.getChild("chestplate1");

		this.left_leg = this.root.getChild("left_leg");
		this.left_leg_top = this.left_leg.getChild("left_leg_top");
		this.leftleg_top1 = this.left_leg_top.getChild("leftleg_top1");
		this.left_boot = this.left_leg.getChild("left_boot");
		this.left_boot_outside = this.left_boot.getChild("left_boot_outside");
		this.leftboot_outside1 = this.left_boot_outside.getChild("leftboot_outside1");
		this.left_boot_tip = this.left_boot.getChild("left_boot_tip");
		this.leftboot_tip1 = this.left_boot_tip.getChild("leftboot_tip1");

		this.right_leg = this.root.getChild("right_leg");
		this.right_leg_top = this.right_leg.getChild("right_leg_top");
		this.rightleg_top1 = this.right_leg_top.getChild("rightleg_top1");
		this.right_boot = this.right_leg.getChild("right_boot");
		this.right_boot_outside = this.right_boot.getChild("right_boot_outside");
		this.rightboot_outside1 = this.right_boot_outside.getChild("rightboot_outside1");
		this.right_boot_tip = this.right_boot.getChild("right_boot_tip");
		this.rightboot_tip1 = this.right_boot_tip.getChild("rightboot_tip1");

		this.kama = this.root.getChild("kama");
		this.kama1 = this.kama.getChild("kama1");
		this.left_kama = this.kama1.getChild("left_kama");
		this.left_front_kama = this.left_kama.getChild("left_front_kama");
		this.right_kama = this.kama1.getChild("right_kama");
		this.right_front_kama = this.right_kama.getChild("right_front_kama");

		this.right_arm = this.root.getChild("right_arm");
		this.right_arm_main = this.right_arm.getChild("right_arm_main");
		this.right_armmain1 = this.right_arm_main.getChild("right_armmain1");
		this.right_paldron = this.right_arm.getChild("right_paldron");
		this.right_paldron1 = this.right_paldron.getChild("right_paldron1");
		this.right_paldron2 = this.right_paldron.getChild("right_paldron2");

		this.left_arm = this.root.getChild("left_arm");
		this.left_arm_main = this.left_arm.getChild("left_arm_main");
		this.left_armmain1 = this.left_arm_main.getChild("left_armmain1");
		this.left_paldron = this.left_arm.getChild("left_paldron");
		this.left_paldron1 = this.left_paldron.getChild("left_paldron1");
		this.left_paldron2 = this.left_paldron.getChild("left_paldron2");

		this.hat = this.root.getChild("hat");

		this.color = color;
	}



	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, -0.25F, 0.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 0.0F));

		PartDefinition head1 = head.addOrReplaceChild("head1", CubeListBuilder.create().texOffs(99, 49).addBox(-4.0F, -8.4F, -2.0F, 8.0F, 8.0F, 6.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition faceplate = head.addOrReplaceChild("faceplate", CubeListBuilder.create(), PartPose.offset(0.0F, -9.0F, -2.7F));

		PartDefinition faceplate1 = faceplate.addOrReplaceChild("faceplate1", CubeListBuilder.create().texOffs(87, 87).addBox(-4.0F, 1.35F, -0.7F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.6F))
				.texOffs(96, 77).addBox(-2.0F, 1.35F, -1.7F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, -0.75F, 0.2F));

		PartDefinition cube_r1 = faceplate1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(96, 67).mirror().addBox(1.5F, -7.65F, 0.3F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(-5.0F, 9.0F, -0.5F, 0.0F, 0.5672F, 0.0F));

		PartDefinition cube_r2 = faceplate1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(86, 77).addBox(-5.5F, -7.65F, 0.3F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(5.0F, 9.0F, -0.5F, 0.0F, -0.5672F, 0.0F));

		PartDefinition faceplate2 = faceplate.addOrReplaceChild("faceplate2", CubeListBuilder.create().texOffs(108, 85).addBox(-4.0F, 1.35F, -0.7F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.6F))
				.texOffs(116, 75).addBox(-2.0F, 1.35F, -1.7F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, -0.75F, 0.2F));

		PartDefinition cube_r3 = faceplate2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(106, 75).mirror().addBox(1.5F, -7.65F, 0.3F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(-5.0F, 9.0F, -0.5F, 0.0F, 0.5672F, 0.0F));

		PartDefinition cube_r4 = faceplate2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(116, 65).addBox(-5.5F, -7.65F, 0.3F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(5.0F, 9.0F, -0.5F, 0.0F, -0.5672F, 0.0F));

		PartDefinition faceplate3 = faceplate.addOrReplaceChild("faceplate3", CubeListBuilder.create().texOffs(87, 117).addBox(-4.0F, 1.35F, -0.7F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.6F))
				.texOffs(95, 107).addBox(-2.0F, 1.35F, -1.7F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, -0.75F, 0.2F));

		PartDefinition cube_r5 = faceplate3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(85, 107).mirror().addBox(1.5F, -7.65F, 0.3F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(-5.0F, 9.0F, -0.5F, 0.0F, 0.5672F, 0.0F));

		PartDefinition cube_r6 = faceplate3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(95, 97).addBox(-5.5F, -7.65F, 0.3F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(5.0F, 9.0F, -0.5F, 0.0F, -0.5672F, 0.0F));

		PartDefinition faceplate4 = faceplate.addOrReplaceChild("faceplate4", CubeListBuilder.create().texOffs(108, 117).addBox(-4.0F, 1.35F, -0.7F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.6F))
				.texOffs(116, 107).addBox(-2.0F, 1.35F, -1.7F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, -0.75F, 0.2F));

		PartDefinition cube_r7 = faceplate4.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(116, 97).mirror().addBox(1.5F, -7.65F, 0.3F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offsetAndRotation(-5.0F, 9.0F, -0.5F, 0.0F, 0.5672F, 0.0F));

		PartDefinition cube_r8 = faceplate4.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(106, 107).addBox(-5.5F, -7.65F, 0.3F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(5.0F, 9.0F, -0.5F, 0.0F, -0.5672F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.25F, 0.0F));

		PartDefinition body1 = body.addOrReplaceChild("body1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition chestplate1 = body1.addOrReplaceChild("chestplate1", CubeListBuilder.create().texOffs(99, 3).addBox(-4.0F, 0.75F, -2.5F, 8.0F, 3.0F, 5.0F, new CubeDeformation(0.75F))
				.texOffs(75, 3).addBox(-4.0F, 3.5F, -2.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.75F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9F, 12.25F, 0.0F));

		PartDefinition left_leg_top = left_leg.addOrReplaceChild("left_leg_top", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftleg_top1 = left_leg_top.addOrReplaceChild("leftleg_top1", CubeListBuilder.create().texOffs(50, 112).addBox(0.35F, -11.6F, -2.1F, 4.0F, 9.25F, 4.0F, new CubeDeformation(0.4F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition left_boot = left_leg.addOrReplaceChild("left_boot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_boot_outside = left_boot.addOrReplaceChild("left_boot_outside", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition leftboot_outside1 = left_boot_outside.addOrReplaceChild("leftboot_outside1", CubeListBuilder.create().texOffs(64, 105).addBox(0.5F, -5.0F, -2.25F, 4.0F, 5.0F, 4.25F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_boot_tip = left_boot.addOrReplaceChild("left_boot_tip", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftboot_tip1 = left_boot_tip.addOrReplaceChild("leftboot_tip1", CubeListBuilder.create().texOffs(68, 121).addBox(0.75F, -2.0F, -4.75F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.25F, 0.0F));

		PartDefinition right_leg_top = right_leg.addOrReplaceChild("right_leg_top", CubeListBuilder.create(), PartPose.offset(3.8F, 0.0F, 0.0F));

		PartDefinition rightleg_top1 = right_leg_top.addOrReplaceChild("rightleg_top1", CubeListBuilder.create().texOffs(15, 113).addBox(-4.35F, -11.6F, -2.1F, 4.0F, 9.25F, 4.0F, new CubeDeformation(0.4F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition right_boot = right_leg.addOrReplaceChild("right_boot", CubeListBuilder.create(), PartPose.offset(3.8F, 0.0F, 0.0F));

		PartDefinition right_boot_outside = right_boot.addOrReplaceChild("right_boot_outside", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition rightboot_outside1 = right_boot_outside.addOrReplaceChild("rightboot_outside1", CubeListBuilder.create().texOffs(31, 105).addBox(-4.5F, -5.0F, -2.25F, 4.0F, 5.0F, 4.25F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_boot_tip = right_boot.addOrReplaceChild("right_boot_tip", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightboot_tip1 = right_boot_tip.addOrReplaceChild("rightboot_tip1", CubeListBuilder.create().texOffs(35, 120).addBox(-4.05F, -2.0F, -4.75F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition kama = root.addOrReplaceChild("kama", CubeListBuilder.create(), PartPose.offset(0.0F, 14.25F, -2.0F));

		PartDefinition kama1 = kama.addOrReplaceChild("kama1", CubeListBuilder.create().texOffs(93, 36).mirror().addBox(-4.0F, -2.0F, 3.75F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_kama = kama1.addOrReplaceChild("left_kama", CubeListBuilder.create().texOffs(107, 36).mirror().addBox(0.0F, -2.0F, -5.25F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 0.0F, 4.0F));

		PartDefinition left_front_kama = left_kama.addOrReplaceChild("left_front_kama", CubeListBuilder.create().texOffs(117, 36).addBox(-4.0F, -2.0F, -0.75F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.5F));

		PartDefinition right_kama = kama1.addOrReplaceChild("right_kama", CubeListBuilder.create().texOffs(107, 25).addBox(-1.0F, -2.0F, -5.25F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 4.0F));

		PartDefinition right_front_kama = right_kama.addOrReplaceChild("right_front_kama", CubeListBuilder.create().texOffs(117, 25).mirror().addBox(0.0F, -2.0F, -0.75F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -4.5F));

		PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.25F, 0.0F));

		PartDefinition right_arm_main = right_arm.addOrReplaceChild("right_arm_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_armmain1 = right_arm_main.addOrReplaceChild("right_armmain1", CubeListBuilder.create().texOffs(2, 84).addBox(-0.9375F, -1.7947F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.8F)), PartPose.offset(-2.5625F, 0.5447F, 0.0F));

		PartDefinition right_paldron = right_arm.addOrReplaceChild("right_paldron", CubeListBuilder.create(), PartPose.offset(10.0F, 0.0F, 0.0F));

		PartDefinition right_paldron1 = right_paldron.addOrReplaceChild("right_paldron1", CubeListBuilder.create(), PartPose.offset(-12.5625F, 0.5447F, 0.0F));

		PartDefinition right_arm_r1 = right_paldron1.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(1, 56).addBox(1.1217F, 2.2811F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.138F));

		PartDefinition right_arm_r2 = right_paldron1.addOrReplaceChild("right_arm_r2", CubeListBuilder.create().texOffs(13, 58).addBox(-2.5765F, -1.5268F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition right_arm_r3 = right_paldron1.addOrReplaceChild("right_arm_r3", CubeListBuilder.create().texOffs(10, 65).addBox(-1.4913F, -2.4945F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.7F))
				.texOffs(1, 64).addBox(-3.1984F, -2.9749F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition right_paldron2 = right_paldron.addOrReplaceChild("right_paldron2", CubeListBuilder.create().texOffs(33, 93).mirror().addBox(-0.9375F, -3.0447F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false), PartPose.offset(-12.5625F, 0.7947F, 0.0F));

		PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0F, 2.25F, 0.0F));

		PartDefinition left_arm_main = left_arm.addOrReplaceChild("left_arm_main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_armmain1 = left_arm_main.addOrReplaceChild("left_armmain1", CubeListBuilder.create().texOffs(64, 85).addBox(-3.0625F, -1.7947F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.8F)), PartPose.offset(2.5625F, 0.5447F, 0.0F));

		PartDefinition left_paldron = left_arm.addOrReplaceChild("left_paldron", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_paldron1 = left_paldron.addOrReplaceChild("left_paldron1", CubeListBuilder.create(), PartPose.offset(2.5625F, 0.7947F, 0.0F));

		PartDefinition left_arm_r1 = left_paldron1.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(54, 54).addBox(-4.3325F, 2.4154F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.138F));

		PartDefinition left_arm_r2 = left_paldron1.addOrReplaceChild("left_arm_r2", CubeListBuilder.create().texOffs(54, 63).addBox(-0.3694F, -1.7709F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition left_arm_r3 = left_paldron1.addOrReplaceChild("left_arm_r3", CubeListBuilder.create().texOffs(66, 57).addBox(0.3418F, -3.1797F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.7F))
				.texOffs(64, 65).addBox(0.6347F, -2.6993F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6109F));

		PartDefinition left_paldron2 = left_paldron.addOrReplaceChild("left_paldron2", CubeListBuilder.create().texOffs(33, 85).addBox(-3.0625F, -3.0447F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(2.5625F, 0.7947F, 0.0F));

		PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 24.25F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void render(ItemStack pStack, SlotContext slot, PoseStack matrixStack, MultiBufferSource buffer, int light)
	{
		this.root.visible = true;

		this.head.getAllParts().forEach(part -> part.visible = false);
		this.faceplate.getAllParts().forEach(part -> part.visible = false);
		this.hat.getAllParts().forEach(part -> part.visible = false);

		this.right_arm_main.getAllParts().forEach(part -> part.visible = false);
		this.right_paldron.getAllParts().forEach(part -> part.visible = false);
		this.left_arm_main.getAllParts().forEach(part -> part.visible = false);
		this.left_paldron.getAllParts().forEach(part -> part.visible = false);

		this.body.getAllParts().forEach(part -> part.visible = false);

		this.right_leg_top.getAllParts().forEach(part -> part.visible = false);
		this.right_boot_tip.getAllParts().forEach(part -> part.visible = false);
		this.right_boot_outside.getAllParts().forEach(part -> part.visible = false);
		this.left_leg_top.getAllParts().forEach(part -> part.visible = false);
		this.left_boot_tip.getAllParts().forEach(part -> part.visible = false);
		this.left_boot_outside.getAllParts().forEach(part -> part.visible = false);

		this.kama.getAllParts().forEach(part -> part.visible = false);

		//now we need to get the actual data from the itemstack
		//and set the correct pieces to be visible

		if (!pStack.getCapability(ShardData.SHARD_DATA).isPresent())
		{
			return;
		}


		final ShardplateCurioItem item = (ShardplateCurioItem) pStack.getItem();

		final DynamicShardplateData data = item.getShardData(pStack);


		this.right_leg.visible = true;
		this.left_leg.visible = true;
		this.right_boot.visible = true;
		this.right_boot_outside.visible = true;
		this.right_boot_tip.visible = true;
		this.right_boot_outside.getChild(data.getRightBootOutsideID()).visible = true;
		this.right_boot_tip.getChild(data.getRightBootTipID()).visible = true;
		this.left_boot.visible = true;
		this.left_boot_tip.visible = true;
		this.left_boot_outside.visible = true;
		this.left_boot_outside.getChild(data.getLeftBootOutsideID()).visible = true;
		this.left_boot_tip.getChild(data.getLeftBootTipID()).visible = true;

		this.left_leg_top.visible = true;
		this.right_leg_top.visible = true;

		if(!data.getKamaID().equals("kama0"))
		{
			this.kama.visible = true;
			this.kama.getChild(data.getKamaID()).getAllParts().forEach(part -> part.visible = true);
		}
		this.right_leg_top.getChild(data.getRightLegID()).visible = true;
		this.left_leg_top.getChild(data.getLeftLegID()).visible = true;


		this.body.visible = true;
		this.right_arm.visible = true;
		this.left_arm.visible = true;
		this.right_arm_main.visible = true;
		this.right_paldron.visible = true;
		this.left_arm_main.visible = true;
		this.left_paldron.visible = true;

		this.body.getChild(data.getBodyID()).getAllParts().forEach(part -> part.visible = true);

		this.right_arm_main.getChild(data.getRightArmID()).visible = true;
		if (!data.getRightPaldronsID().equals("right_paldron0"))
		{
			this.right_paldron.getChild(data.getRightPaldronsID()).getAllParts().forEach(part -> part.visible = true);
		}

		this.left_arm_main.getChild(data.getLeftArmID()).visible = true;
		if (!data.getLeftPaldronsID().equals("left_paldron0"))
		{
			this.left_paldron.getChild(data.getLeftPaldronsID()).getAllParts().forEach(part -> part.visible = true);
		}

		this.head.visible = true;
		this.head.getChild(data.getHeadID()).getAllParts().forEach(part -> part.visible = true);
		this.faceplate.visible = true;
		this.faceplate.getChild(data.getFaceplateID()).getAllParts().forEach(part -> part.visible = true);


		Color color = item.getColour(pStack);
		// First texture layer
		VertexConsumer baseLayer = buffer.getBuffer(RenderType.armorCutoutNoCull(TEXTURE));
		renderToBuffer(matrixStack,
				baseLayer,
				light,
				OverlayTexture.NO_OVERLAY,
				color.getRed()/255f,
				color.getGreen()/255f,
				color.getBlue()/255f,
				1f);

		// Second overlay texture (e.g., glowing runes, color tint)


		// Third layer
		if(data.isLiving())
		{
			String location = "textures/models/armor/glyphs/" + data.getOrder().getName().toLowerCase() + "_glyph.png";
			ResourceLocation glyphRL = Surgebinding.rl(location);
			Color glyphColor = item.getOrder(pStack).getColor();
			glyphColor = glyphColor.brighter();
/*
			VertexConsumer lightLayer = buffer.getBuffer(RenderType.armorCutoutNoCull(TRIM));
			renderToBuffer(matrixStack,
					lightLayer,
					LightTexture.FULL_BRIGHT,
					OverlayTexture.NO_OVERLAY,
					glyphColor.getRed()/255F,
					glyphColor.getGreen()/255F,
					glyphColor.getBlue()/255F,
					1F);
*/

			VertexConsumer glyphLayer = buffer.getBuffer(RenderType.armorCutoutNoCull(glyphRL));
			renderToBuffer(matrixStack,
					glyphLayer,
					LightTexture.FULL_BRIGHT,
					OverlayTexture.NO_OVERLAY,
					glyphColor.getRed()/255f,
					glyphColor.getGreen()/255f,
					glyphColor.getBlue()/255f,
					1f);

			VertexConsumer overlayLayer = buffer.getBuffer(RenderType.armorCutoutNoCull(VISOR));
			renderToBuffer(matrixStack,
					overlayLayer,
					LightTexture.FULL_BRIGHT,
					OverlayTexture.NO_OVERLAY,
					glyphColor.getRed()/255f,
					glyphColor.getGreen()/255f,
					glyphColor.getBlue()/255f,
					1f);


		}
		else
		{
			VertexConsumer overlayLayer = buffer.getBuffer(RenderType.armorCutoutNoCull(VISOR));
			renderToBuffer(matrixStack,
					overlayLayer,
					LightTexture.FULL_BRIGHT,
					OverlayTexture.NO_OVERLAY,
					0.6F,
					0.8F,
					1,
					1f);
		}

		//Cracked Layer
		if(item.getMaxCharge(pStack) - item.getCharge(pStack) > item.getMaxCharge(pStack)/4)
		{
			VertexConsumer cracksLayer = buffer.getBuffer(RenderType.armorCutoutNoCull(CRACKS1));
			renderToBuffer(matrixStack,
					cracksLayer,
					15728700,
					OverlayTexture.NO_OVERLAY,
					1f,
					1f,
					1f,
					1f);
		}



	}

	public void setupAnim(@NotNull LivingEntity entity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch)
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

}

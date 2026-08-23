/*
 * File updated ~ 14 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import leaf.cosmere.surgebinding.common.entity.spren.Honorspren;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HonorsprenModel extends HierarchicalModel<Honorspren> implements ArmedModel
{
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	//private final ModelPart right_wing;
	//private final ModelPart left_wing;
	private static final float FLYING_ANIMATION_X_ROT = ((float) Math.PI / 4F);
	private static final float MAX_HAND_HOLDING_ITEM_X_ROT_RAD = -1.134464F;
	private static final float MIN_HAND_HOLDING_ITEM_X_ROT_RAD = (-(float) Math.PI / 3F);

	public HonorsprenModel(ModelPart pRoot)
	{
		super(RenderType::entityTranslucent);
		this.root = pRoot.getChild("root");
		this.head = this.root.getChild("head");
		this.body = this.root.getChild("body");
		this.right_arm = this.body.getChild("right_arm");
		this.left_arm = this.body.getChild("left_arm");
		//this.right_wing = this.body.getChild("right_wing");
		//this.left_wing = this.body.getChild("left_wing");
	}

	public ModelPart root()
	{
		return this.root;
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 23.5F, 0.0F));
		partdefinition1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.99F, 0.0F));
		PartDefinition partdefinition2 = partdefinition1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, -4.0F, 0.0F));
		partdefinition2.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(23, 0).addBox(-0.75F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offset(-1.75F, 0.5F, 0.0F));
		partdefinition2.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(23, 6).addBox(-0.25F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offset(1.75F, 0.5F, 0.0F));
		//partdefinition2.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 1.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, 0.6F));
		//partdefinition2.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 1.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.6F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	/**
	 * Sets this entity's model rotation angles
	 */
	public void setupAnim(Honorspren pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch)
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		float f = pAgeInTicks * 20.0F * ((float) Math.PI / 180F) + pLimbSwing;
		float f1 = Mth.cos(f) * (float) Math.PI * 0.15F + pLimbSwingAmount;
		float f2 = pAgeInTicks - (float) pEntity.tickCount;
		float f3 = pAgeInTicks * 9.0F * ((float) Math.PI / 180F);
		float f4 = Math.min(pLimbSwingAmount / 0.3F, 1.0F);
		float f5 = 1.0F - f4;
		float f6 = pEntity.getHoldingItemAnimationProgress(f2);
		if (pEntity.isDancing())
		{
			float f7 = pAgeInTicks * 8.0F * ((float) Math.PI / 180F) + pLimbSwingAmount;
			float f8 = Mth.cos(f7) * 16.0F * ((float) Math.PI / 180F);
			float f9 = pEntity.getSpinningProgress(f2);
			float f10 = Mth.cos(f7) * 14.0F * ((float) Math.PI / 180F);
			float f11 = Mth.cos(f7) * 30.0F * ((float) Math.PI / 180F);
			this.root.yRot = pEntity.isSpinning() ? 12.566371F * f9 : this.root.yRot;
			this.root.zRot = f8 * (1.0F - f9);
			this.head.yRot = f11 * (1.0F - f9);
			this.head.zRot = f10 * (1.0F - f9);
		}
		else
		{
			this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
			this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
		}

		//this.right_wing.xRot = 0.43633232F * (1.0F - f4);
		//this.right_wing.yRot = (-(float) Math.PI / 4F) + f1;
		//this.left_wing.xRot = 0.43633232F * (1.0F - f4);
		//this.left_wing.yRot = FLYING_ANIMATION_X_ROT - f1;
		this.body.xRot = f4 * FLYING_ANIMATION_X_ROT;
		float f12 = f6 * Mth.lerp(f4, MIN_HAND_HOLDING_ITEM_X_ROT_RAD, MAX_HAND_HOLDING_ITEM_X_ROT_RAD);
		this.root.y += (float) Math.cos((double) f3) * 0.25F * f5;
		this.right_arm.xRot = f12;
		this.left_arm.xRot = f12;
		float f13 = f5 * (1.0F - f6);
		float f14 = 0.43633232F - Mth.cos(f3 + ((float) Math.PI * 1.5F)) * (float) Math.PI * 0.075F * f13;
		this.left_arm.zRot = -f14;
		this.right_arm.zRot = f14;
		this.right_arm.yRot = 0.27925268F * f6;
		this.left_arm.yRot = -0.27925268F * f6;
	}

	public void translateToHand(HumanoidArm pSide, PoseStack pPoseStack)
	{
		this.root.translateAndRotate(pPoseStack);
		this.body.translateAndRotate(pPoseStack);
		pPoseStack.translate(0.0F, 0.0625F, 0.1875F);
		pPoseStack.mulPose(Axis.XP.rotation(this.right_arm.xRot));
		pPoseStack.scale(0.7F, 0.7F, 0.7F);
		pPoseStack.translate(0.0625F, 0.0F, 0.0F);
	}
}
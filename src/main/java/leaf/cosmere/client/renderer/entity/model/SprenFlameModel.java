package leaf.cosmere.client.renderer.entity.model;// Made with Blockbench 3.8.3
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.google.common.collect.*;
import com.mojang.blaze3d.matrix.*;
import com.mojang.blaze3d.vertex.*;
import leaf.cosmere.entities.spren.*;
import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.client.renderer.model.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.math.*;

public class SprenFlameModel<T extends SprenFlameEntity> extends AgeableModel<T>
{
	private final ModelRenderer body;
	private final ModelRenderer wingLeft;
	private final ModelRenderer wingLeftUpper_r1;
	private final ModelRenderer wingLeftLower_r1;
	private final ModelRenderer wingRight;
	private final ModelRenderer wingRightLower_r1;
	private final ModelRenderer wingRightUpper_r1;

	private float bodyPitch;

	public SprenFlameModel()
	{
		texWidth = 16;
		texHeight = 16;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 24.0F, 0.0F);
		body.texOffs(0, 0).addBox(-2.0F, -4.0F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		body.texOffs(0, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		wingLeft = new ModelRenderer(this);
		wingLeft.setPos(0.0F, 0.0F, 0.0F);
		body.addChild(wingLeft);


		wingLeftUpper_r1 = new ModelRenderer(this);
		wingLeftUpper_r1.setPos(0.0F, 0.0F, 0.0F);
		wingLeft.addChild(wingLeftUpper_r1);
		setRotationAngle(wingLeftUpper_r1, 0.0F, 0.7854F, -0.1745F);
		wingLeftUpper_r1.texOffs(0, 0).addBox(1.0F, -3.75F, 1.0F, 4.0F, 3.0F, 0.0F, 0.0F, false);

		wingLeftLower_r1 = new ModelRenderer(this);
		wingLeftLower_r1.setPos(0.0F, 0.0F, 0.0F);
		wingLeft.addChild(wingLeftLower_r1);
		setRotationAngle(wingLeftLower_r1, 0.0F, 0.3491F, 0.0F);
		wingLeftLower_r1.texOffs(0, 0).addBox(0.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, 0.0F, false);

		wingRight = new ModelRenderer(this);
		wingRight.setPos(0.0F, 0.0F, 0.0F);
		body.addChild(wingRight);


		wingRightLower_r1 = new ModelRenderer(this);
		wingRightLower_r1.setPos(0.0F, 0.0F, 0.0F);
		wingRight.addChild(wingRightLower_r1);
		setRotationAngle(wingRightLower_r1, 0.0F, -0.3491F, 0.0F);
		wingRightLower_r1.texOffs(0, 0).addBox(-3.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, 0.0F, false);

		wingRightUpper_r1 = new ModelRenderer(this);
		wingRightUpper_r1.setPos(0.0F, 0.0F, 0.0F);
		wingRight.addChild(wingRightUpper_r1);
		setRotationAngle(wingRightUpper_r1, 0.0F, -0.7854F, 0.1745F);
		wingRightUpper_r1.texOffs(0, 0).addBox(-5.0F, -3.75F, 1.0F, 4.0F, 3.0F, 0.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.wingRight.xRot = 0.0F;

		this.body.xRot = 0.0F;
		this.body.y = 19.0F;

		boolean flag = entityIn.isOnGround() && entityIn.getDeltaMovement().lengthSqr() < 1.0E-7D;
		if (flag)
		{
			this.wingRight.yRot = -0.2618F;
			this.wingRight.zRot = 0.0F;
			this.wingLeft.xRot = 0.0F;
			this.wingLeft.yRot = 0.2618F;
			this.wingLeft.zRot = 0.0F;
		}
		else
		{
			float f = ageInTicks * 2.1F;
			this.wingRight.yRot = 0.0F;
			this.wingRight.zRot = MathHelper.cos(f) * (float) Math.PI * 0.15F;
			this.wingLeft.xRot = this.wingRight.xRot;
			this.wingLeft.yRot = this.wingRight.yRot;
			this.wingLeft.zRot = -this.wingRight.zRot;
			this.body.xRot = 0.0F;
			this.body.yRot = 180.0F;
			this.body.zRot = 0.0F;
		}


		if (this.bodyPitch > 0.0F)
		{
			this.body.xRot = ModelUtils.rotlerpRad(this.body.xRot, 3.0915928F, this.bodyPitch);
		}
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	@Override
	protected Iterable<ModelRenderer> headParts()
	{
		return ImmutableList.of();
	}


	@Override
	protected Iterable<ModelRenderer> bodyParts()
	{
		return ImmutableList.of(this.body);
	}

	public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick)
	{
		super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
		this.bodyPitch = entityIn.getBodyPitch(partialTick);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
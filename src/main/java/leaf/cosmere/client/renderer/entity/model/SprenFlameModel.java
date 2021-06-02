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
		textureWidth = 16;
		textureHeight = 16;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		body.setTextureOffset(0, 0).addBox(-2.0F, -4.0F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		wingLeft = new ModelRenderer(this);
		wingLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(wingLeft);


		wingLeftUpper_r1 = new ModelRenderer(this);
		wingLeftUpper_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		wingLeft.addChild(wingLeftUpper_r1);
		setRotationAngle(wingLeftUpper_r1, 0.0F, 0.7854F, -0.1745F);
		wingLeftUpper_r1.setTextureOffset(0, 0).addBox(1.0F, -3.75F, 1.0F, 4.0F, 3.0F, 0.0F, 0.0F, false);

		wingLeftLower_r1 = new ModelRenderer(this);
		wingLeftLower_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		wingLeft.addChild(wingLeftLower_r1);
		setRotationAngle(wingLeftLower_r1, 0.0F, 0.3491F, 0.0F);
		wingLeftLower_r1.setTextureOffset(0, 0).addBox(0.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, 0.0F, false);

		wingRight = new ModelRenderer(this);
		wingRight.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(wingRight);


		wingRightLower_r1 = new ModelRenderer(this);
		wingRightLower_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		wingRight.addChild(wingRightLower_r1);
		setRotationAngle(wingRightLower_r1, 0.0F, -0.3491F, 0.0F);
		wingRightLower_r1.setTextureOffset(0, 0).addBox(-3.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, 0.0F, false);

		wingRightUpper_r1 = new ModelRenderer(this);
		wingRightUpper_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		wingRight.addChild(wingRightUpper_r1);
		setRotationAngle(wingRightUpper_r1, 0.0F, -0.7854F, 0.1745F);
		wingRightUpper_r1.setTextureOffset(0, 0).addBox(-5.0F, -3.75F, 1.0F, 4.0F, 3.0F, 0.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.wingRight.rotateAngleX = 0.0F;

		this.body.rotateAngleX = 0.0F;
		this.body.rotationPointY = 19.0F;

		boolean flag = entityIn.isOnGround() && entityIn.getMotion().lengthSquared() < 1.0E-7D;
		if (flag)
		{
			this.wingRight.rotateAngleY = -0.2618F;
			this.wingRight.rotateAngleZ = 0.0F;
			this.wingLeft.rotateAngleX = 0.0F;
			this.wingLeft.rotateAngleY = 0.2618F;
			this.wingLeft.rotateAngleZ = 0.0F;
		}
		else
		{
			float f = ageInTicks * 2.1F;
			this.wingRight.rotateAngleY = 0.0F;
			this.wingRight.rotateAngleZ = MathHelper.cos(f) * (float) Math.PI * 0.15F;
			this.wingLeft.rotateAngleX = this.wingRight.rotateAngleX;
			this.wingLeft.rotateAngleY = this.wingRight.rotateAngleY;
			this.wingLeft.rotateAngleZ = -this.wingRight.rotateAngleZ;
			this.body.rotateAngleX = 0.0F;
			this.body.rotateAngleY = 180.0F;
			this.body.rotateAngleZ = 0.0F;
		}


		if (this.bodyPitch > 0.0F)
		{
			this.body.rotateAngleX = ModelUtils.func_228283_a_(this.body.rotateAngleX, 3.0915928F, this.bodyPitch);
		}
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	@Override
	protected Iterable<ModelRenderer> getHeadParts()
	{
		return ImmutableList.of();
	}


	@Override
	protected Iterable<ModelRenderer> getBodyParts()
	{
		return ImmutableList.of(this.body);
	}

	public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick)
	{
		super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
		this.bodyPitch = entityIn.getBodyPitch(partialTick);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
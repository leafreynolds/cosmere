// Made with Blockbench 3.8.3
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


public class SprenFlameModel extends EntityModel<Entity> {
	private final ModelRenderer body;
	private final ModelRenderer wingLeft;
	private final ModelRenderer wingLeftUpper_r1;
	private final ModelRenderer wingLeftLower_r1;
	private final ModelRenderer wingRight;
	private final ModelRenderer wingRightLower_r1;
	private final ModelRenderer wingRightUpper_r1;

	public SprenFlameModel() {
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
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.IShardbladeDynamicData;
import leaf.cosmere.surgebinding.common.items.ShardbladeDynamicItem;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ShardbladeModel extends Model
{
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ResourceLocation TEXTURE = Surgebinding.rl("textures/item/models/shardblade/dynamic.png");
	public static final int TOTAL_BLADE_IDS = 2;
	public static final int TOTAL_HANDLE_IDS = 1;
	public static final int TOTAL_POMMEL_IDS = 1;
	public static final int TOTAL_CROSS_GUARD_IDS = 1;

	private final ModelPart root;
	private final ModelPart blade;
	private final ModelPart handle;
	private final ModelPart pommel;
	private final ModelPart cross_guard;

	public ShardbladeModel(ModelPart root)
	{
		super(RenderType::entitySolid);
		this.root = root.getChild("root");
		this.blade = this.root.getChild("blade");
		this.handle = this.root.getChild("handle");
		this.pommel = this.root.getChild("pommel");
		this.cross_guard = this.root.getChild("cross_guard");
	}


	public static LayerDefinition createLayerDefinition()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition blade = root.addOrReplaceChild("blade", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition blade_guide = blade.addOrReplaceChild("blade_guide", CubeListBuilder.create().texOffs(19, 3).addBox(-2.5F, -45.0F, -0.5F, 5.0F, 28.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition blade_1 = blade.addOrReplaceChild("blade_1", CubeListBuilder.create().texOffs(0, 3).addBox(-0.5F, -45.0F, -0.5F, 1.0F, 28.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition blade_2 = blade.addOrReplaceChild("blade_2", CubeListBuilder.create().texOffs(19, 3).addBox(-2.5F, -45.0F, -0.5F, 5.0F, 28.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition handle = root.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition handle_guide = handle.addOrReplaceChild("handle_guide", CubeListBuilder.create().texOffs(4, 8).addBox(-0.5F, -16.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition handle_1 = handle.addOrReplaceChild("handle_1", CubeListBuilder.create().texOffs(4, 8).addBox(-0.5F, -16.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition pommel = root.addOrReplaceChild("pommel", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition pommel_guide = pommel.addOrReplaceChild("pommel_guide", CubeListBuilder.create().texOffs(4, 3).addBox(-1.5F, -9.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition pommel_1 = pommel.addOrReplaceChild("pommel_1", CubeListBuilder.create().texOffs(4, 3).addBox(-1.5F, -9.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cross_guard = root.addOrReplaceChild("cross_guard", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition crossguard_guide = cross_guard.addOrReplaceChild("crossguard_guide", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -17.0F, -1.0F, 9.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition crossguard_1 = cross_guard.addOrReplaceChild("crossguard_1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -17.0F, -1.0F, 9.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}


	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setup(ItemStack pStack)
	{
		this.root.visible = true;

		//kinda yuck that we can't just have block bench not include these folders in the first place
		final String bladeGuide = "blade_guide";
		final String handleGuide = "handle_guide";
		final String pommelGuide = "pommel_guide";
		final String crossGuardGuide = "crossguard_guide";
		this.blade.getChild(bladeGuide).visible = false;
		this.handle.getChild(handleGuide).visible = false;
		this.pommel.getChild(pommelGuide).visible = false;
		this.cross_guard.getChild(crossGuardGuide).visible = false;

		this.blade.getAllParts().forEach(part -> part.visible = false);
		this.handle.getAllParts().forEach(part -> part.visible = false);
		this.pommel.getAllParts().forEach(part -> part.visible = false);
		this.cross_guard.getAllParts().forEach(part -> part.visible = false);

		this.blade.visible = true;
		this.handle.visible = true;
		this.pommel.visible = true;
		this.cross_guard.visible = true;

		//now we need to get the actual data from the itemstack
		//and set the correct pieces to be visible

		if (!pStack.getCapability(ShardbladeDynamicItem.CAPABILITY).isPresent())
		{
			return;
		}

		final IShardbladeDynamicData data = pStack.getCapability(ShardbladeDynamicItem.CAPABILITY).resolve().get();

		this.blade.getChild(data.getBladeID()).visible = true;
		this.handle.getChild(data.getHandleID()).visible = true;
		this.pommel.getChild(data.getPommelID()).visible = true;
		this.cross_guard.getChild(data.getCrossGuardID()).visible = true;
	}
}
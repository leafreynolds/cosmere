// Made with Blockbench 4.8.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package leaf.cosmere.surgebinding.client.render.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class CrypticModel<T extends Entity> extends EntityModel<T> {
	private final ModelPart root;
	private final ModelPart limbSet1;
	private final ModelPart limbSet2;
	private final ModelPart limbSet3;
	private final ModelPart limbSet4;

	public CrypticModel(ModelPart root) {
		this.root = root.getChild("root");
		this.limbSet1 = this.root.getChild("limbSet1");
		this.limbSet2 = this.root.getChild("limbSet2");
		this.limbSet3 = this.root.getChild("limbSet3");
		this.limbSet4 = this.root.getChild("limbSet4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 19.0F, -8.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition limbSet1 = root.addOrReplaceChild("limbSet1", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition fgLimb1 = limbSet1.addOrReplaceChild("fgLimb1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -10.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -12.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -14.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -15.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -16.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition fgLimb2 = limbSet1.addOrReplaceChild("fgLimb2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 8.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(0.0F, 8.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(1.0F, 10.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(2.0F, 12.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(3.0F, 13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(4.0F, 14.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(6.0F, 15.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition fgLimb3 = limbSet1.addOrReplaceChild("fgLimb3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 8.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(0.0F, 8.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(1.0F, 10.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(2.0F, 12.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(3.0F, 13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(4.0F, 14.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(6.0F, 15.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition fgLimb4 = limbSet1.addOrReplaceChild("fgLimb4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -10.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -12.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -14.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -15.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -16.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition limbSet2 = root.addOrReplaceChild("limbSet2", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition fgLimb5 = limbSet2.addOrReplaceChild("fgLimb5", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -9.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(0.0F, -10.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(1.0F, -12.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(2.0F, -13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(3.0F, -14.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(4.0F, -15.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(6.0F, -16.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition fgLimb6 = limbSet2.addOrReplaceChild("fgLimb6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 8.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, 8.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, 10.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, 12.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, 13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, 14.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, 15.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition fgLimb7 = limbSet2.addOrReplaceChild("fgLimb7", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 8.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, 8.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, 10.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, 12.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, 13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, 14.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, 15.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition fgLimb8 = limbSet2.addOrReplaceChild("fgLimb8", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -9.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(0.0F, -10.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(1.0F, -12.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(2.0F, -13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(3.0F, -14.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(4.0F, -15.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(6.0F, -16.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition limbSet3 = root.addOrReplaceChild("limbSet3", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition bgLimb1 = limbSet3.addOrReplaceChild("bgLimb1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -4.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-2.0F, -6.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-6.0F, -2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-1.0F, -7.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-7.0F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-5.0F, -8.0F, 4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-8.0F, -5.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-3.0F, -5.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-5.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bgLimb2 = limbSet3.addOrReplaceChild("bgLimb2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(1.0F, -2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(2.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(2.0F, -4.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(3.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(1.0F, -6.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(5.0F, -2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(0.0F, -7.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(6.0F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(1.0F, -8.0F, 4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(7.0F, -5.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(2.0F, -5.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bgLimb3 = limbSet3.addOrReplaceChild("bgLimb3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(1.0F, 1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(2.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(2.0F, 3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(3.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(1.0F, 5.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(5.0F, 1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(0.0F, 6.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(6.0F, 0.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(1.0F, 7.0F, 4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(7.0F, 1.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(2.0F, 4.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(4.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bgLimb4 = limbSet3.addOrReplaceChild("bgLimb4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, 1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, 3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-4.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-2.0F, 5.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-6.0F, 1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-1.0F, 6.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-7.0F, 0.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-5.0F, 7.0F, 4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-8.0F, 1.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-3.0F, 4.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-5.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition limbSet4 = root.addOrReplaceChild("limbSet4", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition bgLimb5 = limbSet4.addOrReplaceChild("bgLimb5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -4.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-2.0F, -6.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-6.0F, -2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-1.0F, -7.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-7.0F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-5.0F, -8.0F, 4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-8.0F, -5.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-3.0F, -5.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-5.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bgLimb6 = limbSet4.addOrReplaceChild("bgLimb6", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(1.0F, -2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(2.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(2.0F, -4.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(3.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(1.0F, -6.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(5.0F, -2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(0.0F, -7.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(6.0F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(1.0F, -8.0F, 4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(7.0F, -5.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(2.0F, -5.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bgLimb7 = limbSet4.addOrReplaceChild("bgLimb7", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(1.0F, 1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(2.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(2.0F, 3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(3.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(1.0F, 5.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(5.0F, 1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(0.0F, 6.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(6.0F, 0.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(1.0F, 7.0F, 4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(7.0F, 1.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(2.0F, 4.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(4.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bgLimb8 = limbSet4.addOrReplaceChild("bgLimb8", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, 1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, 3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-4.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-2.0F, 5.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-6.0F, 1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-1.0F, 6.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-7.0F, 0.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-5.0F, 7.0F, 4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-8.0F, 1.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).addBox(-3.0F, 4.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-5.0F, 2.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		limbSet1.zRot = ageInTicks / 20;
		limbSet2.zRot = -(ageInTicks / 16);
		limbSet3.zRot = ageInTicks / 40;
		limbSet4.zRot = -(ageInTicks / 35);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
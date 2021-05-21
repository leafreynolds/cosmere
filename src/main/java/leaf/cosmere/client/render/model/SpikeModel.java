/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to TheIllusiveC4 and their mod Curios for their example of rendering an extra model on the player.
 * https://github.com/TheIllusiveC4/Curios
 *
 * Eventually this script will change to take in where exactly the user is spiked.
 * https://coppermind.net/w/images/Hemalurgy_table.jpg
 *
 */

package leaf.cosmere.client.render.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import top.theillusivec4.curios.api.SlotTypePreset;

import javax.annotation.Nonnull;
import java.util.Optional;

public class SpikeModel<T extends LivingEntity> extends BipedModel<T>
{
    public ModelRenderer leftEyeSpike;
    public ModelRenderer rightEyeSpike;
    public ModelRenderer neckSpike;
    public String renderMode;
    public int renderIndex;

    public SpikeModel()
    {
        super(1f);
        this.textureWidth = 16;
        this.textureHeight = 16;

        //left eye
        this.leftEyeSpike = new ModelRenderer(this, 0, 0);
        this.leftEyeSpike.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftEyeSpike.addBox(1.0F, -3.0F, -7.0F, 2.0F, 2.0F, 14.0F, 0.0F);

        //right eye
        this.rightEyeSpike = new ModelRenderer(this, 0, 0);
        this.rightEyeSpike.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightEyeSpike.addBox(-3.0F, -3.0F, -7.0F, 2.0F, 2.0F, 14.0F, 0.0F);

        //neck
        this.neckSpike = new ModelRenderer(this, 0, 0);
        this.neckSpike.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.neckSpike.addBox(-0.5F, -1.0F, 4.0F, 1.0F, 1.0F, 2.0F, 0.0F);

        //body
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody.addBox(-1F, 2.0F, 0.0F, 2.0F, 2.0F, 3.0F, 0.0F);

        //right arm
        this.bipedRightArm = new ModelRenderer(this, 0, 0);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightArm.addBox(-3.0F, 4.0F, -0.5F, 5, 1, 1, 0.4F);

        //left arm
        this.bipedLeftArm = new ModelRenderer(this, 0, 0);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedLeftArm.addBox(-2.0F, 4.0F, -0.5F, 5, 1, 1, 0.4F);

        //right leg
        this.bipedRightLeg = new ModelRenderer(this, 0, 0);
        this.bipedRightLeg.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightLeg.addBox(-3.0F, 4.0F, -0.5F, 1, 1, 5, 0.4F);

        //left leg
        this.bipedLeftLeg = new ModelRenderer(this, 0, 0);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedLeftLeg.addBox(-2.0F, 4.0F, -0.5F, 1, 1, 5, 0.4F);


    }

    public void setRotationAngles(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float netHeadPitch)
    {
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder vertexBuilder, int light, int overlay, float red, float green, float blue, float alpha)
    {
        Optional<SlotTypePreset> slotTypePreset = SlotTypePreset.findPreset(renderMode);
        if (!slotTypePreset.isPresent())
        {
            return;
        }

        switch (slotTypePreset.get())
        {
            case HEAD:
                if (renderIndex == 0)
                {
                    this.leftEyeSpike.render(matrixStack, vertexBuilder, light, overlay, red, green, blue, alpha);
                }
                else
                {
                    this.rightEyeSpike.render(matrixStack, vertexBuilder, light, overlay, red, green, blue, alpha);
                }
                break;
            case NECKLACE:
                this.neckSpike.render(matrixStack, vertexBuilder, light, overlay, red, green, blue, alpha);
                break;
            case BACK:
            case BODY:
                this.bipedBody.render(matrixStack, vertexBuilder, light, overlay, red, green, blue, alpha);
                break;
            case BRACELET:
            case HANDS:
            case RING:
                if (renderIndex == 0)
                {
                    this.bipedLeftArm.render(matrixStack, vertexBuilder, light, overlay, red, green, blue, alpha);
                }
                else
                {
                    this.bipedRightArm.render(matrixStack, vertexBuilder, light, overlay, red, green, blue, alpha);
                }
                break;
            case BELT:
                break;
            case CHARM:
                if (renderIndex == 0)
                {
                    this.bipedLeftLeg.render(matrixStack, vertexBuilder, light, overlay, red, green, blue, alpha);
                }
                else
                {
                    this.bipedRightLeg.render(matrixStack, vertexBuilder, light, overlay, red, green, blue, alpha);
                }
                break;
        }
    }
}

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

package leaf.cosmere.client.renderer.wearables;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import top.theillusivec4.curios.api.SlotTypePreset;

import javax.annotation.Nonnull;
import java.util.Optional;

public class SpikeModel<T extends LivingEntity> extends CurioModel
{
    public ModelRenderer leftEyeSpike;
    public ModelRenderer rightEyeSpike;
    public ModelRenderer neckSpike;

    public SpikeModel()
    {
        super(1f);

        //left eye
        this.leftEyeSpike = new ModelRenderer(this, 0, 0);
        this.leftEyeSpike.setPos(0.0F, 0.0F, 0.0F);
        this.leftEyeSpike.addBox(1.0F, -3.0F, -7.0F, 2.0F, 2.0F, 14.0F, 0.0F);

        //right eye
        this.rightEyeSpike = new ModelRenderer(this, 0, 0);
        this.rightEyeSpike.setPos(0.0F, 0.0F, 0.0F);
        this.rightEyeSpike.addBox(-3.0F, -3.0F, -7.0F, 2.0F, 2.0F, 14.0F, 0.0F);

        //neck
        this.neckSpike = new ModelRenderer(this, 0, 0);
        this.neckSpike.setPos(0.0F, 0.0F, 0.0F);
        this.neckSpike.addBox(-0.5F, -1.0F, 4.0F, 1.0F, 1.0F, 2.0F, 0.0F);

        //body
        this.body = new ModelRenderer(this, 16, 16);
        this.body.setPos(0.0F, 0.0F, 0.0F);
        this.body.addBox(-1F, 2.0F, 0.0F, 2.0F, 2.0F, 3.0F, 0.0F);

        //left arm
        this.leftArm = new ModelRenderer(this, 0, 0);
        this.leftArm.mirror = true;
        this.leftArm.setPos(-5.0F, 2.0F, 0.0F);
        this.leftArm.addBox(-2.0F, 4.0F, -0.5F, 5, 1, 1, 0.4F);

        //right arm
        this.rightArm = new ModelRenderer(this, 0, 0);
        this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.rightArm.addBox(-3.0F, 4.0F, -0.5F, 5, 1, 1, 0.4F);

        //left leg
        this.leftLeg = new ModelRenderer(this, 0, 0);
        this.leftLeg.mirror = true;
        this.leftLeg.setPos(-5.0F, 2.0F, 0.0F);
        this.leftLeg.addBox(0.0F, 4.0F, -0.5F, 1, 1, 5, 0.4F);

        //right leg
        this.rightLeg = new ModelRenderer(this, 0, 0);
        this.rightLeg.setPos(-5.0F, 2.0F, 0.0F);
        this.rightLeg.addBox(-1.0F, 4.0F, -0.5F, 1, 1, 5, 0.4F);

    }

    @Override
    public void renderToBuffer(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder vertexBuilder, int light, int overlay, float red, float green, float blue, float alpha)
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
        }

        super.renderToBuffer(matrixStack, vertexBuilder, light, overlay, red, green, blue, alpha);
    }
}

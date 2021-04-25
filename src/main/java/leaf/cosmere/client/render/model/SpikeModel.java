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
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

import javax.annotation.Nonnull;

public class SpikeModel<T extends LivingEntity> extends EntityModel<T>
{
    public ModelRenderer spike;

    public SpikeModel()
    {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.spike = new ModelRenderer(this, 0, 0);
        this.spike.setRotationPoint(0.0F, 0.0F, 0.0F);

        //todo change this based on spike location
        this.spike.addBox(-3.0F, -3.0F, -7.0F, 2.0F, 2.0F, 14.0F, 0.0F);
    }

    public void setRotationAngles(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float netHeadPitch)
    {
    }

    public void render(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder vertexBuilder, int light, int overlay, float red, float green, float blue, float alpha)
    {
        this.spike.render(matrixStack, vertexBuilder, light, overlay);
    }
}

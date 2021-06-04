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

import com.mojang.blaze3d.matrix.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.client.renderer.model.*;
import net.minecraft.entity.*;
import top.theillusivec4.curios.api.*;

import javax.annotation.*;
import java.util.*;

public abstract class CurioModel<T extends LivingEntity> extends BipedModel<T>
{
    public String renderMode;
    public int renderIndex;

    public CurioModel(float modelSize)
    {
        super(modelSize);
        this.textureWidth = 16;
        this.textureHeight = 16;
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

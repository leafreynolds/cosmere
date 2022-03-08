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

public class BraceletModel<T extends LivingEntity> extends CurioModel
{

	public BraceletModel()
	{
		super(1f);

		//left arm
		this.leftArm = new ModelRenderer(this, 0, 0);
		//this.leftArm.mirror = false;
		this.leftArm.setPos(1.0F, 2.0F, 0.0F);
		this.leftArm.addBox(2.2F, 6.0F, -2.5F, 1, 1, 5, 0.4F);

		//right arm
		this.rightArm = new ModelRenderer(this, 0, 0);
		this.rightArm.setPos(1.0F, 2.0F, 0.0F);
		this.rightArm.addBox(-3.2F, 6.0F, -2.5F, 1, 1, 5, 0.4F);


	}

	@Override
	public void renderToBuffer(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder vertexBuilder, int light, int overlay, float red, float green, float blue, float alpha)
	{
		Optional<SlotTypePreset> slotTypePreset = SlotTypePreset.findPreset(renderMode);
		if (!slotTypePreset.isPresent())
		{
			return;
		}

		super.renderToBuffer(matrixStack, vertexBuilder, light, overlay, red, green, blue, alpha);
	}
}

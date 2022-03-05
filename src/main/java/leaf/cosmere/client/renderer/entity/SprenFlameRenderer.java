package leaf.cosmere.client.renderer.entity;

import com.mojang.blaze3d.matrix.*;
import leaf.cosmere.client.renderer.entity.model.*;
import leaf.cosmere.entities.spren.*;
import leaf.cosmere.utils.helpers.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class SprenFlameRenderer extends MobRenderer<SprenFlameEntity, SprenFlameModel<SprenFlameEntity>>
{
	private static final ResourceLocation FIRE_SPREN_TEXTURES = ResourceLocationHelper.prefix("textures/entity/spren_flame.png");

	public SprenFlameRenderer(EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, new SprenFlameModel(), 0.25F);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(SprenFlameEntity entity)
	{
		return FIRE_SPREN_TEXTURES;
	}

	protected void scale(SprenFlameEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime)
	{
		matrixStackIn.scale(0.8F, 0.8F, 0.8F);
	}

	protected void setupRotations(SprenFlameEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks)
	{
		matrixStackIn.translate(0.0D, (double) (MathHelper.cos(ageInTicks * 0.3F) * 0.1F), 0.0D);
		super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}
}
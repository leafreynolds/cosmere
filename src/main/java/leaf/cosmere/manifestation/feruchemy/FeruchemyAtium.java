/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.AttributesRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.registries.RegistryObject;

public class FeruchemyAtium extends FeruchemyBase
{
	public FeruchemyAtium(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public void onModeChange(ISpiritweb data)
	{
		super.onModeChange(data);
		data.getLiving().refreshDimensions();
	}

	public static float getScale(LivingEntity living)
	{
		/*final int mode = getMode(data);
		float v;// = mode * 0.1f;

		if (isStoring(data))
		{
			v = mode * 0.1f;
		}
		else// if (isTapping(data))
		{
			final float inverseLerp = MathHelper.InverseLerp(0, 21, Math.abs(mode));
			final float v1 = Easing.easeOutQuad(inverseLerp);

			v = Mth.lerp(v1,0, -0.8f);
		}
		float scale = 1 + v;*/

		try
		{

			final RegistryObject<Attribute> metalRelatedAttribute = AttributesRegistry.SIZE_ATTRIBUTE;
			if (metalRelatedAttribute != null && metalRelatedAttribute.isPresent())
			{
				AttributeInstance attribute = living.getAttribute(metalRelatedAttribute.get());
				//return modded val
				final float v = attribute != null ? (float) attribute.getValue() : 1;
				return v;
			}
		}
		catch (Exception e)
		{
			//player is in a weird uninitialized state when logging in,
			//so if it errors, I don't care, just return 1 in those cases.
		}

		return 1;
	}
}

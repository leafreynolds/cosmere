/*
 * File updated ~ 31 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.registries.FeruchemyAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class FeruchemyAtium extends FeruchemyManifestation
{
	public FeruchemyAtium(Metals.MetalType metalType)
	{
		super(metalType);
	}


	@Override
	public Attribute getAttribute()
	{
		return FeruchemyAttributes.FERUCHEMY_ATTRIBUTES.get(Metals.MetalType.ELECTRUM).getAttribute();
	}

	@Override
	public void onModeChange(ISpiritweb data, int lastMode)
	{
		super.onModeChange(data, lastMode);
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

			final AttributeRegistryObject<Attribute> metalRelatedAttribute = AttributesRegistry.SIZE_ATTRIBUTE;
			if (metalRelatedAttribute != null)
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

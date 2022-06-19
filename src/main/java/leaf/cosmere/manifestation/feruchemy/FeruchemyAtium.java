/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.MathHelper;
import leaf.cosmere.utils.math.Easing;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.common.util.LazyOptional;
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

	public float getScale(ISpiritweb data)
	{
		return getScale(data.getLiving());
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



		final RegistryObject<Attribute> attributeRegistryObject = AttributesRegistry.COSMERE_ATTRIBUTES.get(Metals.MetalType.ATIUM.getName());
		AttributeInstance attribute = living.getAttribute(attributeRegistryObject.get());
		//return modded val
		final float v = attribute != null ? (float) attribute.getValue() : 1;
		return v;
	}
}

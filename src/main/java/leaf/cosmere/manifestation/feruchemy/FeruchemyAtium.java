/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.MathHelper;
import leaf.cosmere.utils.math.Easing;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;

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
		if (isActive(data) && canAfford(data, true))
		{
			final int mode = getMode(data);
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



			float scale = 1 + v;



			return scale;
		}
		return 1;
	}



	public static float getScale(LivingEntity living)
	{
		final LazyOptional<ISpiritweb> iSpiritwebLazyOptional = SpiritwebCapability.get(living);

		if (iSpiritwebLazyOptional.isPresent())
		{
			var spiritwebOptional = iSpiritwebLazyOptional.resolve();
			if (spiritwebOptional.isPresent())
			{
				final ISpiritweb iSpiritweb = spiritwebOptional.get();
				FeruchemyAtium atiumF = (FeruchemyAtium) ManifestationRegistry.FERUCHEMY_POWERS.get(Metals.MetalType.ATIUM).get();
				return atiumF.getScale(iSpiritweb);
			}
		}

		return 1;
	}
}

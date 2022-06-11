/*
 * File created ~ 11 - 6 - 2022 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.manifestation.feruchemy.FeruchemyAtium;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.extensions.IForgeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(IForgeEntity.class)
public interface IForgeEntityMixin
{
	@Overwrite(remap = false)
	default float getStepHeight()
	{
		final Entity self = (Entity) this;
		float vanillaStep = self.maxUpStep;
		if (self instanceof LivingEntity living)
		{
			AttributeInstance stepHeightAttribute = living.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());
			if (stepHeightAttribute != null)
			{
				final float max = (float) Math.max(0, vanillaStep + stepHeightAttribute.getValue());



				return max * FeruchemyAtium.getScale(living);
			}
		}
		return vanillaStep;
	}
}

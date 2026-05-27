/*
 * File updated ~ 20 - 12 - 2024 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.InterpolatedValue;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.Optional;

@Mixin(LightTexture.class)
public class LightTextureMixin
{
	@Unique
	private static final ResourceLocation TIN_RL = ResourceLocation.fromNamespaceAndPath("allomancy", Metals.MetalType.TIN.getName());

	@Unique
	private final InterpolatedValue _cosmere$nightVision = new InterpolatedValue(0);

	@ModifyConstant(method = "updateLightTexture", constant = @Constant(floatValue = 0f, ordinal = 1))
	private float updateLightTextureConstant(float prev)
	{
		//do stuff
		final Minecraft mc = Minecraft.getInstance();

		if (!(Minecraft.getInstance().getCameraEntity() instanceof Player clientPlayer))
		{
			return prev;
		}

		final Optional<ISpiritweb> spiritwebOpt = SpiritwebCapability.get(clientPlayer);
		float tinAlloVal = 0;

		if (spiritwebOpt.isPresent())
		{
			ISpiritweb spiritweb = spiritwebOpt.get();
			final Manifestation tinAllomancy = CosmereAPI.manifestationRegistry().getHolder(TIN_RL).get().value();
			if (spiritweb instanceof SpiritwebCapability data && tinAllomancy != null && tinAllomancy.isActive(spiritweb))
			{
				//burning or flaring strength
				float currentBurnStrength = (float) (tinAllomancy.getStrength(data, false) * data.getMode(tinAllomancy));
				final RangedAttribute attribute = (RangedAttribute) tinAllomancy.getAttribute();
				float maxTinFlareStrengthPossible = (float) (attribute.getMaxValue() * 2);

				//tin takes actual strength into account, as compared to total possible strength including flaring.
				//todo - move the min/max night vision to config, ideally server side that gets synced to client
				tinAlloVal = Mth.lerp(MathHelper.clamp01(currentBurnStrength / maxTinFlareStrengthPossible), 0.0f, 0.95f);
			}
		}

		final AttributeRegistryObject<Attribute> attributeRegistryObject = AttributesRegistry.NIGHT_VISION_ATTRIBUTE;
		AttributeInstance attribute = clientPlayer.getAttribute(attributeRegistryObject.getHolder());
		//return modded val
		final float v = attribute != null ? (float) attribute.getValue() : prev;
		final float total = v + tinAlloVal;

		//I know I shouldn't be changing the default value, but it's just so gosh darned convenient
		_cosmere$nightVision.setDefaultValue(MathHelper.clamp01(total));
		_cosmere$nightVision.interpolate();

		return _cosmere$nightVision.get(mc.getTimer().getGameTimeDeltaPartialTick(true));
	}
}

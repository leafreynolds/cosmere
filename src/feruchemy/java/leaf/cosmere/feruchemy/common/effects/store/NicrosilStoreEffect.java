/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import leaf.cosmere.feruchemy.common.registries.FeruchemyManifestations;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;


public class NicrosilStoreEffect extends FeruchemyEffectBase
{
	public NicrosilStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);

		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			final Attribute attributeRegistryObject = manifestation.getAttribute();

			final boolean invalidMetalToDisable =
					manifestation == FeruchemyManifestations.FERUCHEMY_POWERS.get(Metals.MetalType.NICROSIL).get()
							|| manifestation == FeruchemyManifestations.FERUCHEMY_POWERS.get(Metals.MetalType.ALUMINUM).get();

			if (attributeRegistryObject == null || invalidMetalToDisable)
			{
				continue;
			}

			addAttributeModifier(
					attributeRegistryObject,
					"00b3f9bc-9a43-46e7-98de-56dfa6e80618",
					-1000.0D,
					AttributeModifier.Operation.ADDITION);
		}

	}
}

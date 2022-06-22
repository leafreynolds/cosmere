/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.RegistryObject;


public class NicrosilStoreEffect extends FeruchemyEffectBase
{
	public NicrosilStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);

		for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			final RegistryObject<Attribute> attributeRegistryObject = manifestation.getAttribute();
			if (attributeRegistryObject == null
					|| !attributeRegistryObject.isPresent()
					|| manifestation == ManifestationRegistry.FERUCHEMY_POWERS.get(Metals.MetalType.NICROSIL).get())
			{
				continue;
			}

			addAttributeModifier(
					attributeRegistryObject.get(),
					"00b3f9bc-9a43-46e7-98de-56dfa6e80618",
					-1000.0D,
					AttributeModifier.Operation.ADDITION);
		}

	}
}

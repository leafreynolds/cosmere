/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.store;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.feruchemy.FeruchemyNicrosil;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.CompoundNBTHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.RegistryObject;


public class NicrosilStoreEffect extends FeruchemyEffectBase
{
	public NicrosilStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);

		for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			String manifestationName = manifestation.getName();
			if (!AttributesRegistry.COSMERE_ATTRIBUTES.containsKey(manifestationName))
			{
				continue;
			}

			addAttributeModifier(
					AttributesRegistry.COSMERE_ATTRIBUTES.get(manifestationName).get(),
					"00b3f9bc-9a43-46e7-98de-56dfa6e80618",
					-1000.0D,
					AttributeModifier.Operation.ADDITION);
		}

	}
}

package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.providers.IAttributeProvider;
import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class AttributeDeferredRegister extends WrappedDeferredRegister<Attribute>
{
	private final List<IAttributeProvider> allAttributes = new ArrayList<>();

	public AttributeDeferredRegister(String modid)
	{
		super(modid, ForgeRegistries.ATTRIBUTES);
	}

	public <ATTRIBUTE extends Attribute> AttributeRegistryObject<ATTRIBUTE> register(String name, Supplier<? extends ATTRIBUTE> sup)
	{
		AttributeRegistryObject<ATTRIBUTE> registeredItem = register(name, sup, AttributeRegistryObject::new);
		allAttributes.add(registeredItem);
		return registeredItem;
	}

	public AttributeRegistryObject<Attribute> register(String registryName, String modid, float defaultVal, float min, float max)
	{
		return register(
				registryName,
				() -> new RangedAttribute(
						"manifestation." + modid + "." + registryName,//todo update manifestation to attribute on porting
						defaultVal,
						min,
						max)
						.setSyncable(true)
		);
	}

	public List<IAttributeProvider> getAllAttributes()
	{
		return Collections.unmodifiableList(allAttributes);
	}
}

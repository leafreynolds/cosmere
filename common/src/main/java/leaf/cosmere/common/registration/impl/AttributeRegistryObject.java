package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.providers.IAttributeProvider;
import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class AttributeRegistryObject<ATTRIBUTE extends Attribute> extends WrappedRegistryObject<ATTRIBUTE> implements IAttributeProvider
{
	public AttributeRegistryObject(RegistryObject<ATTRIBUTE> registryObject)
	{
		super(registryObject);
	}

	@NotNull
	@Override
	public ATTRIBUTE getAttribute()
	{
		return get();
	}

}
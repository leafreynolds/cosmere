/*
 * File updated ~ 17 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class StatDeferredRegister extends WrappedDeferredRegister<ResourceLocation>
{

	private final List<StatRegistryObject> allItems = new ArrayList<>();

	public StatDeferredRegister(String modid)
	{
		super(modid, Registries.CUSTOM_STAT);
	}


	public StatRegistryObject register(String name)
	{
		return register(name, () -> new ResourceLocation(name));
	}

	public StatRegistryObject register(String name, Supplier<ResourceLocation> supplier)
	{
		final StatRegistryObject statRegistryObject = register(name, supplier, StatRegistryObject::new);
		allItems.add(statRegistryObject);
		return statRegistryObject;
	}

	public List<StatRegistryObject> getAllItems()
	{
		return Collections.unmodifiableList(allItems);
	}
}
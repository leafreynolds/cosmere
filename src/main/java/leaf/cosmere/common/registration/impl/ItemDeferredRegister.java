/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemDeferredRegister extends WrappedDeferredRegister<Item>
{

	private final List<IItemProvider> allItems = new ArrayList<>();

	public ItemDeferredRegister(String modid)
	{
		super(modid, ForgeRegistries.ITEMS);
	}

	public static Item.Properties getCosmereDefaultProperties()
	{
		return new Item.Properties();
	}

	public ItemRegistryObject<Item> register(String name)
	{
		return register(name, Item::new);
	}

	public ItemRegistryObject<Item> registerUnburnable(String name)
	{
		return registerUnburnable(name, Item::new);
	}

	public ItemRegistryObject<Item> register(String name, Rarity rarity)
	{
		return register(name, properties -> new Item(properties.rarity(rarity)));
	}


	public <ITEM extends Item> ItemRegistryObject<ITEM> register(String name, Function<Item.Properties, ITEM> sup)
	{
		return register(name, () -> sup.apply(getCosmereDefaultProperties()));
	}

	public <ITEM extends Item> ItemRegistryObject<ITEM> registerUnburnable(String name, Function<Item.Properties, ITEM> sup)
	{
		return register(name, () -> sup.apply(getCosmereDefaultProperties().fireResistant()));
	}

	public <ITEM extends Item> ItemRegistryObject<ITEM> register(String name, Supplier<? extends ITEM> sup)
	{
		ItemRegistryObject<ITEM> registeredItem = register(name, sup, ItemRegistryObject::new);
		allItems.add(registeredItem);
		return registeredItem;
	}

	public <ENTITY extends Mob> ItemRegistryObject<ForgeSpawnEggItem> registerSpawnEgg(EntityTypeRegistryObject<ENTITY> entityTypeProvider,
	                                                                                   int primaryColor, int secondaryColor)
	{
		return register(entityTypeProvider.getInternalRegistryName() + "_spawn_egg", props -> new ForgeSpawnEggItem(entityTypeProvider, primaryColor,
				secondaryColor, props));
	}

	public List<IItemProvider> getAllItems()
	{
		return Collections.unmodifiableList(allItems);
	}
}
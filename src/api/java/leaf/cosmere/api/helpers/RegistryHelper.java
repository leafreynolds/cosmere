/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.helpers;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public class RegistryHelper
{
	private RegistryHelper()
	{

	}

	public static ResourceLocation get(Item item)
	{
		return ForgeRegistries.ITEMS.getKey(item);
	}

	public static ResourceLocation get(Entity entity)
	{
		return ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
	}

	public static ResourceLocation get(Block block)
	{
		return ForgeRegistries.BLOCKS.getKey(block);
	}

	public static ResourceLocation get(EntityType<?> test)
	{
		return ForgeRegistries.ENTITY_TYPES.getKey(test);
	}

	public static ResourceLocation getName(MenuType<?> element)
	{
		return getName(ForgeRegistries.MENU_TYPES, element);
	}

	public static ResourceLocation getName(ParticleType<?> element)
	{
		return getName(ForgeRegistries.PARTICLE_TYPES, element);
	}

	public static ResourceLocation getName(Item element)
	{
		return getName(ForgeRegistries.ITEMS, element);
	}

	public static String getPath(Item element)
	{
		return getName(element).getPath();
	}

	public static ResourceLocation getName(Block element)
	{
		return getName(ForgeRegistries.BLOCKS, element);
	}

	public static String getNamespace(Block element)
	{
		return getName(element).getNamespace();
	}

	public static String getPath(Block element)
	{
		return getName(element).getPath();
	}

	public static ResourceLocation getName(Fluid element)
	{
		return getName(ForgeRegistries.FLUIDS, element);
	}

	public static ResourceLocation getName(BlockEntityType<?> element)
	{
		return getName(ForgeRegistries.BLOCK_ENTITY_TYPES, element);
	}

	public static ResourceLocation getName(EntityType<?> element)
	{
		return getName(ForgeRegistries.ENTITY_TYPES, element);
	}

	public static ResourceLocation getName(RecipeSerializer<?> element)
	{
		return getName(ForgeRegistries.RECIPE_SERIALIZERS, element);
	}

	private static <T> ResourceLocation getName(IForgeRegistry<T> registry, T element)
	{
		return registry.getKey(element);
	}

	@Nullable
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static ResourceLocation getNameGeneric(Object element)
	{
		for (Registry<?> registry : BuiltInRegistries.REGISTRY)
		{
			//Note: We have to use getResourceKey as getKey for defaulted registries returns the default key
			Optional<ResourceKey<?>> resourceKey = ((Registry) registry).getResourceKey(element);
			if (resourceKey.isPresent())
			{
				return resourceKey.get().location();
			}
		}
		return null;
	}
}

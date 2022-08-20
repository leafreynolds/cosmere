/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import leaf.cosmere.Cosmere;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

public class ResourceLocationHelper
{
	public static ResourceLocation prefix(String path)
	{
		return new ResourceLocation(Cosmere.MODID, path.toLowerCase(Locale.ROOT));
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
}

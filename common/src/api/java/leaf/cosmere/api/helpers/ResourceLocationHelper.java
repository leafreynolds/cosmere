/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.helpers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;


public class ResourceLocationHelper
{
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

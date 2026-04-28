package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;

public class SurgebindingTags
{
	public static class Blocks
	{

	}

	public static class Items
	{

	}

	public static class BannerPatterns
	{
		public static final TagKey<BannerPattern> PATTERN_ITEM_SURGE = tag("pattern_item/surge");
		public static final TagKey<BannerPattern> PATTERN_ITEM_RADIANT_ORDER = tag("pattern_item/radiant_order");

		private static TagKey<BannerPattern> tag(String name)
		{
			return TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation(Surgebinding.MODID, name));
		}
	}
}

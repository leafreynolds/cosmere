/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.itemgroups;

import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class FeruchemyItemGroups
{

	public static CreativeModeTab METALMINDS = new CreativeModeTab(Feruchemy.MODID + ".metalminds")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(FeruchemyItems.METAL_BRACELETS.entrySet().stream().findAny().get().getValue().get());
		}
	};
}

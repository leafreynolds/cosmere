/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.itemgroups;

import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class HemalurgyItemGroups
{

	public static CreativeModeTab HEMALURGIC_SPIKES = new CreativeModeTab(Hemalurgy.MODID + ".spikes")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(HemalurgyItems.METAL_SPIKE.entrySet().stream().findAny().get().getValue().get());
		}
	};

}

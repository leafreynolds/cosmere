/*
 * File created ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.capabilities;

import leaf.cosmere.common.datamaps.CosmereDataMaps;
import leaf.cosmere.common.datamaps.SpikeProperties;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.items.IHemalurgicInfo;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ItemCapability;

import javax.annotation.Nullable;

//look spikes up through this, never instanceof - that is what lets datapacks declare spikes
public final class HemalurgyItemCapabilities
{
	public static final ItemCapability<IHemalurgicInfo, Void> SPIKE =
			ItemCapability.createVoid(Hemalurgy.rl("spike"), IHemalurgicInfo.class);

	private HemalurgyItemCapabilities()
	{
	}

	@Nullable
	public static IHemalurgicInfo getSpike(ItemStack stack)
	{
		return stack.isEmpty() ? null : stack.getCapability(SPIKE);
	}

	@Nullable
	public static SpikeProperties getSpikeProperties(Item item)
	{
		return CosmereDataMaps.getSpikeProperties(item);
	}
}

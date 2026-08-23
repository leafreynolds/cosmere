/*
 * File created ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.common.cap.item;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.charge.IChargeable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ItemCapability;

import javax.annotation.Nullable;

public final class CosmereItemCapabilities
{
	public static final ItemCapability<IChargeable, Void> CHARGEABLE =
			ItemCapability.createVoid(Cosmere.rl("chargeable"), IChargeable.class);

	private CosmereItemCapabilities()
	{
	}

	@Nullable
	public static IChargeable getChargeable(ItemStack stack)
	{
		return stack.isEmpty() ? null : stack.getCapability(CHARGEABLE);
	}
}

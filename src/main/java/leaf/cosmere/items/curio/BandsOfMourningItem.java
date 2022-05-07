/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items.curio;

import leaf.cosmere.constants.Metals;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Optional;

public class BandsOfMourningItem extends BraceletMetalmindItem
{
	public BandsOfMourningItem()
	{
		super(Metals.MetalType.HARMONIUM);
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> stacks)
	{
		if (allowdedIn(tab))
		{
			ItemStack fullPower = new ItemStack(this);
			setCharge(fullPower, getMaxCharge(fullPower));

			CompoundTag nbt = fullPower.getOrCreateTagElement("StoredInvestiture");
			//for each power the user has access to
			for (int i = 0; i < 16; i++)
			{
				//even if it's granted from hemalurgy/temporary
				//update the nbt.
				//this will add/remove powers based on what the user currently has.
				//todo, come back to this later when more sleep. bugs me about losing potential stored powers
				final Optional<Metals.MetalType> metalType = Metals.MetalType.valueOf(i);
				if (metalType.isPresent())
				{
					nbt.putDouble(metalType.get().getAllomancyRegistryName(), 20);
					nbt.putDouble(metalType.get().getFeruchemyRegistryName(), 20);
				}
			}

			stacks.add(fullPower);
		}
	}

	@Override
	public float getMaxChargeModifier()
	{
		return 1;
	}

	@Override
	public int getMaxCharge(ItemStack stack)
	{
		return Integer.MAX_VALUE;
	}
}

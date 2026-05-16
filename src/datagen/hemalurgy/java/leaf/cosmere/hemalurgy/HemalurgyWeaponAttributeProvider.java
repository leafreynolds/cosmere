package leaf.cosmere.hemalurgy;

import leaf.cosmere.CosmereWeaponAttributeProvider;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.items.HemalurgicSpikeItem;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;

import java.util.function.BiConsumer;

public class HemalurgyWeaponAttributeProvider extends CosmereWeaponAttributeProvider
{
	public HemalurgyWeaponAttributeProvider(PackOutput output)
	{
		super(output, Hemalurgy.MODID);
	}

	@Override
	protected void registerWeaponAttributes(BiConsumer<String, String> consumer)
	{

		for (IItemProvider itemRegistryObject : HemalurgyItems.ITEMS.getAllItems())
		{
			Item item = itemRegistryObject.asItem();

			if (item instanceof HemalurgicSpikeItem)
			{
				consumer.accept(itemRegistryObject.getName(), "hemalurgy:metal_spike");
			}
			else if (item instanceof SwordItem)
			{
				consumer.accept(itemRegistryObject.getName(), "bettercombat:claymore");
			}
		}
	}
}

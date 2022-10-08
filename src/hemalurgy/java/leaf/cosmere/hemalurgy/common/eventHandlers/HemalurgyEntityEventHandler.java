/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.eventHandlers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.items.HemalurgicSpikeItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Hemalurgy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HemalurgyEntityEventHandler
{
	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (!(event.getTarget() instanceof LivingEntity target))
		{
			return;
		}

		ItemStack stack = event.getEntity().getMainHandItem();
		if (!stack.isEmpty())
		{
			if (stack.getItem() instanceof HemalurgicSpikeItem spike)
			{
				//https://www.theoryland.com/intvmain.php?i=977#43
				if (!(event.getTarget() instanceof Cat cat))
				{
					return;
				}

				//only apply spike if it has a power
				//no accidentally losing spikes
				if (!spike.hemalurgicIdentityExists(stack))
				{
					return;
				}

				//todo random list of catquisitor names
				target.setCustomName(TextHelper.createTranslatedText("Catquisitor"));

				boolean spikeApplied = false;

				try
				{
					for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
					{
						final double hemalurgicStrength = spike.getHemalurgicStrength(stack, manifestation);
						if (hemalurgicStrength > 0)
						{
							final Attribute regAttribute = manifestation.getAttribute();
							if (regAttribute == null)
							{
								continue;
							}
							spikeApplied = true;

							final AttributeMap catAttributes = cat.getAttributes();
							final AttributeInstance instance = catAttributes.getInstance(regAttribute);

							if (instance != null)
							{
								instance.setBaseValue(hemalurgicStrength);
							}
						}
					}
				}
				catch (Exception ignored)
				{
				}

				if (spikeApplied && !event.getEntity().isCreative())
				{
					stack.shrink(1);
				}
			}

		}

	}

}

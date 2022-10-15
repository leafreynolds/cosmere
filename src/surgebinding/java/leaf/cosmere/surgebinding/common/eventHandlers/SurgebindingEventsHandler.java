/*
 * File updated ~ 15 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.eventHandlers;

import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.manifestation.SurgeProgression;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Surgebinding.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SurgebindingEventsHandler
{
/*
	//todo fix roshar not respecting sleep ??
	@SubscribeEvent
	public static void onSleepFinished(final SleepFinishedTimeEvent evt)
	{
		LevelAccessor levelAccessor = evt.getLevel();

		if (levelAccessor instanceof ServerLevel serverLevel)
		{
			long newTime = evt.getNewTime();

			ServerLevel level = serverLevel.getServer().getLevel(SurgebindingDimensions.ROSHAR_DIM_KEY);
			if (level != null)
			{
				evt.setTimeAddition(600);
			}
		}
	}*/

	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (!(event.getTarget() instanceof LivingEntity target) || event.isCanceled())
		{
			return;
		}

		ItemStack stack = event.getEntity().getMainHandItem();
		if (stack.isEmpty())
		{
			//hand empty, check surge?
			SurgeProgression.onEntityInteract(event);
		}
	}

}

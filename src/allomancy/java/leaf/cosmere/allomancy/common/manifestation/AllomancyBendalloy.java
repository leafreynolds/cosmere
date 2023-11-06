/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.List;

public class AllomancyBendalloy extends AllomancyManifestation
{
	private static final HashMap<String, BendalloyThread> playerThreadMap = new HashMap<>();
	public AllomancyBendalloy(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		int mode = getMode(data);

		String uuid = data.getLiving().getStringUUID();
		if (mode > 0 && !playerThreadMap.containsKey(uuid))
		{
			playerThreadMap.put(uuid, new BendalloyThread(data));
		}

        playerThreadMap.entrySet().removeIf(entry -> !entry.getValue().isRunning);
	}

	class BendalloyThread implements Runnable
	{
		private final ISpiritweb data;
		public boolean isRunning = true;

		public BendalloyThread(ISpiritweb data)
		{
			this.data = data;

			Thread t = new Thread(this, "bendalloy_thread_" + data.getLiving().getDisplayName());
			t.start();
		}

		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					int mode = getMode(data);

					// check if bendalloy is off or compounding
					if (mode <= 0)
					{
						break;
					}

					// this is the only way to check if the player is still online, thanks forge devs
					if (data.getLiving().level.getServer().getPlayerList().getPlayer(data.getLiving().getUUID()) == null)
					{
						break;
					}

					LivingEntity livingEntity = data.getLiving();
					boolean isActiveTick = livingEntity.tickCount % 20 == 0;

					//Slows Down Time for the entities around the user
					if (isActiveTick)
					{
						int range = getRange(data);

						List<LivingEntity> entitiesToAffect = EntityHelper.getLivingEntitiesInRange(data.getLiving(), range, false);

						for (LivingEntity e : entitiesToAffect)
						{
							e.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SLOWDOWN, mode));
						}

						//todo slow tile entities? not sure how to do that. cadmium just calls tick more often.
					}

					// sleep thread for 1 tick (50ms)
					Thread.sleep(50);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					break;
				}
			}
			isRunning = false;
		}
	}
}

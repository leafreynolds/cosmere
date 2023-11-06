/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.HashMap;
import java.util.List;

public class AllomancyBrass extends AllomancyManifestation
{
	private static final HashMap<String, BrassThread> playerThreadMap = new HashMap<>();
	public AllomancyBrass(Metals.MetalType metalType)
	{
		super(metalType);
	}

	//Dampens Emotions
	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		int mode = getMode(data);

		String uuid = data.getLiving().getStringUUID();
		if (mode > 0 && !playerThreadMap.containsKey(uuid))
		{
			playerThreadMap.put(uuid, new BrassThread(data));
		}

		playerThreadMap.entrySet().removeIf(entry -> !entry.getValue().isRunning);
	}

	class BrassThread implements Runnable
	{
		private final ISpiritweb data;
		public boolean isRunning = true;

		public BrassThread(ISpiritweb data)
		{
			this.data = data;

			Thread t = new Thread(this, "brass_thread_" + data.getLiving().getDisplayName().getString());
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
					int range = getRange(data);

					// check if brass is off or compounding
					if (mode <= 0)
					{
						break;
					}

					// this is the only way to check if the player is still online, thanks forge devs
					if (data.getLiving().level.getServer().getPlayerList().getPlayer(data.getLiving().getUUID()) == null)
					{
						break;
					}

					//todo, replace x * mode with config based value
					double allomanticStrength = getStrength(data, false);

					List<LivingEntity> entitiesToAffect = EntityHelper.getLivingEntitiesInRange(data.getLiving(), range, true);

					for (LivingEntity e : entitiesToAffect)
					{
						if (e instanceof Mob mob)
						{
							mob.setNoAi(mode == 3 && allomanticStrength > 15);

							switch (mode)
							{
								case 2:
									mob.setTarget(null);
								case 1:
									mob.setAggressive(false);
								default:
									//stop angry targets from attacking things
									e.setLastHurtByMob(null);

							}
						}
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

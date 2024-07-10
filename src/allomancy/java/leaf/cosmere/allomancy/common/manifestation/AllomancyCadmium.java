/*
 * File updated ~ 15 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

public class AllomancyCadmium extends AllomancyManifestation
{
	private static final HashMap<String, CadmiumThread> playerThreadMap = new HashMap<>();

	public AllomancyCadmium(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		int mode = getMode(data);
		String uuid = data.getLiving().getStringUUID();

		// data processing
		{
			// this is the only way to check if the player is still online, thanks forge devs
			if (data.getLiving().level().getServer().getPlayerList().getPlayer(data.getLiving().getUUID()) == null)
			{
				return;
			}

			if (playerThreadMap.get(uuid) == null)
			{
				playerThreadMap.put(uuid, new CadmiumThread(data));
			}
			//tick entities around user
			int range = getRange(data);
			int x = (int) (data.getLiving().getX() + (data.getLiving().getRandomX(range * 2 + 1) - range));
			int z = (int) (data.getLiving().getZ() + (data.getLiving().getRandomZ(range * 2 + 1) - range));

			for (int i = 4; i > -2; i--)
			{
				int y = data.getLiving().blockPosition().getY() + i;
				BlockPos pos = new BlockPos(x, y, z);
				Level world = data.getLiving().level();

				if (world.isEmptyBlock(pos))
				{
					continue;
				}

				BlockState state = world.getBlockState(pos);
				state.randomTick((ServerLevel) world, pos, world.random);

				break;
			}

			//todo tick living entities?

			List<LivingEntity> entitiesToCheck = playerThreadMap.get(uuid).requestEntityList();

			for (LivingEntity e : entitiesToCheck)
			{
				try
				{
					e.aiStep();
				}
				catch (Exception err)
				{
					if (!(err instanceof NullPointerException))
					{
						err.printStackTrace();
					}
				}
			}
			playerThreadMap.get(uuid).releaseEntityList();
		}
	}

	@Override
	public void onModeChange(ISpiritweb data, int lastMode)
	{
		String uuid = data.getLiving().getStringUUID();
		if (getMode(data) <= 0)
		{
			playerThreadMap.remove(uuid);
		}

		super.onModeChange(data, lastMode);
	}

	@Override
	public boolean tick(ISpiritweb data)
	{
		// data thread management
		{
			String uuid = data.getLiving().getStringUUID();
			if (!playerThreadMap.containsKey(uuid))
			{
				playerThreadMap.put(uuid, new CadmiumThread(data));
			}

			playerThreadMap.entrySet().removeIf(entry -> !entry.getValue().isRunning || AllomancyEntityThread.serverShutdown || entry.getValue() == null);
		}

		return super.tick(data);
	}

	class CadmiumThread extends AllomancyEntityThread
	{

		public CadmiumThread(ISpiritweb data)
		{
			super(data);

			Thread t = new Thread(this, "cadmium_thread_" + data.getLiving().getDisplayName());
			t.start();
		}

		@Override
		public void run()
		{
			List<LivingEntity> newEntityList;
			//Speeds Up Time for everything around the user, implying the user is slower
			while (true)
			{
				if (serverShutdown)
				{
					break;
				}
				try
				{
					int mode = getMode(data);

					// check if cadmium is off or compounding
					if (mode <= 0)
					{
						break;
					}

					// this is the only way to check if the player is still online, thanks forge devs
					if (data.getLiving().level().getServer().getPlayerList().getPlayer(data.getLiving().getUUID()) == null)
					{
						break;
					}

					if (lock.tryLock())
					{
						int range = getRange(data);

						newEntityList = EntityHelper.getLivingEntitiesInRange(data.getLiving(), range, true);
						setEntityList(newEntityList);
						lock.unlock();
					}
					// sleep thread for 1 tick (50ms)
					Thread.sleep(50);
				}
				catch (Exception e)
				{
					CosmereAPI.logger.debug(Arrays.toString(e.getStackTrace()));

					if (e instanceof ConcurrentModificationException)
					{
						lock.unlock();
					}

					break;
				}
			}
			isRunning = false;
		}
	}
}

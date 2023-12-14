/*
 * File updated ~ 9 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AllomancyEntityThread implements Runnable
{
	private final List<LivingEntity> entityList = new ArrayList<>();
	public final Lock localLock = new ReentrantLock();
	public static boolean serverShutdown = false;
	public static final Lock lock = new ReentrantLock();
	public final ISpiritweb data;
	public boolean isRunning = true;

	public AllomancyEntityThread(ISpiritweb data)
	{
		this.data = data;
	}

	public List<LivingEntity> requestEntityList()
	{
		localLock.lock();
		try
		{
			return new ArrayList<>(entityList);
		}
		catch (Exception e)
		{
			CosmereAPI.logger.info(Arrays.toString(e.getStackTrace()));
		}

		// empty return in case of exception
		return new ArrayList<>();
	}

	public void releaseEntityList()
	{
		localLock.unlock();
	}

	public void setEntityList(List<LivingEntity> newList)
	{
		entityList.clear();
		entityList.addAll(newList);
	}

	@Override
	public void run() {}
}

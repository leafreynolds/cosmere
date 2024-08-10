/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.client.utils;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FeruchemyChargeThread implements Runnable
{
	private static final Lock lock = new ReentrantLock();
	private static FeruchemyChargeThread INSTANCE;
	private static final HashMap<Metals.MetalType, Double> feruchemyChargeMap = new HashMap<>();
	static Thread t;
	static boolean isStopping = false;

	public static FeruchemyChargeThread getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new FeruchemyChargeThread();
		}

		return INSTANCE;
	}

	public HashMap<Metals.MetalType, Double> getCharges()
	{
		try
		{
			HashMap<Metals.MetalType, Double> retVal = new HashMap<>();
			if (lock.tryLock())
			{
				retVal.putAll(feruchemyChargeMap);
				lock.unlock();
			}
			return retVal;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			lock.unlock();
		}
		return new HashMap<>();
	}

	public void start()
	{
		if (t == null || isStopping)
		{
			CosmereAPI.logger.info("Feruchemy thread started");
			t = new Thread(this, "feruchemy_enumerator_thread");
			isStopping = false;
			t.start();
		}
	}

	public void stop()
	{
		if (t != null && !isStopping)
		{
			isStopping = true;
		}
	}

	@Override
	public void run()
	{
		Minecraft mc = Minecraft.getInstance();

		// hashmap to keep track of each metal's f-charge in the inventory
		final HashMap<Metals.MetalType, Double> metalmindCharges = new HashMap<>();

		while (!isStopping)
		{
			try
			{
				// no serverside action, unloaded levels, or non-existent players allowed >:(
				if (mc.level == null || mc.player == null || !mc.level.isClientSide)
				{
					break;
				}

				if (mc.player.tickCount % 2 != 0)   // only run on even ticks
				{
					try
					{
						Thread.sleep(50);       // 20 ticks per 1000ms, 1000/20 = 50, rest for 1 tick
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					continue;
				}

				metalmindCharges.clear();

				// all inventory metalminds are counted
				for (ItemStack stack : mc.player.getInventory().items)
				{
					if (stack.getItem() instanceof ChargeableMetalCurioItem item)
					{
						// is either f-item or h-item
						//if (item.getItemCategory() == FeruchemyItemGroups.METALMINDS)
						{
							Double chargeToAdd = (double) item.getCharge(stack);
							if (metalmindCharges.containsKey(item.getMetalType()))
							{
								metalmindCharges.put(item.getMetalType(), metalmindCharges.get(item.getMetalType()) + chargeToAdd);
							}
							else
							{
								metalmindCharges.put(item.getMetalType(), chargeToAdd);
							}
						}
					}
				}

				// all curio metalminds are counted
				CuriosApi.getCuriosInventory(mc.player).ifPresent(handler ->
				{
					Map<String, ICurioStacksHandler> curios = handler.getCurios();
					for (ICurioStacksHandler stacksHandler : curios.values())
					{
						final IDynamicStackHandler stacks = stacksHandler.getStacks();

						for (int i = 0; i < stacks.getSlots(); i++)
						{
							final ItemStack stackInSlot = stacks.getStackInSlot(i);
							if (stackInSlot.getItem() instanceof ChargeableMetalCurioItem item)
							{
								Double chargeToAdd = (double) item.getCharge(stackInSlot);
								if (metalmindCharges.containsKey(item.getMetalType()))
								{
									metalmindCharges.put(item.getMetalType(), metalmindCharges.get(item.getMetalType()) + chargeToAdd);
								}
								else
								{
									metalmindCharges.put(item.getMetalType(), chargeToAdd);
								}
							}
						}
					}
				});

				CuriosApi.getCuriosInventory(mc.player)
						.map(mapper ->
						{

							return true;
						});

				try
				{
					if (lock.tryLock())
					{
						feruchemyChargeMap.clear();
						feruchemyChargeMap.putAll(metalmindCharges);
						lock.unlock();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					lock.unlock();
				}
			}
			catch (Exception e)
			{
				CosmereAPI.logger.warn(Arrays.toString(e.getStackTrace()));
			}
		}
		stop();
	}
}

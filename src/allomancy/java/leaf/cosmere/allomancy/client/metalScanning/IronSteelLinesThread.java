/*
 * File updated ~ 16 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.client.metalScanning;

import leaf.cosmere.allomancy.common.manifestation.AllomancyIronSteel;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static leaf.cosmere.allomancy.common.manifestation.AllomancyIronSteel.containsMetal;
import static leaf.cosmere.allomancy.common.manifestation.AllomancyIronSteel.entityContainsMetal;

public class IronSteelLinesThread implements Runnable
{
	private static IronSteelLinesThread INSTANCE;
	private static Thread t;
	private static final Lock lock = new ReentrantLock();
	private static ScanResult scanResult = new ScanResult();
	private static Vec3 closestMetalObjectInLookVector;
	private final double tolerance = 1.8D;
	private static int scanRange = 0;
	private static boolean isStopping = false;


	TagKey<Block> aluminumOre;
	TagKey<Block> aluminumStorage;
	// these two below may or may not work, better to keep them than remove
	TagKey<Block> aluminumSheet;
	TagKey<Block> aluminumWire;

	public static IronSteelLinesThread getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new IronSteelLinesThread();
		}

		return INSTANCE;
	}

	private IronSteelLinesThread()
	{
		aluminumOre = CosmereTags.Blocks.METAL_ORE_BLOCK_TAGS.get(Metals.MetalType.ALUMINUM);
		aluminumStorage = CosmereTags.Blocks.METAL_BLOCK_TAGS.get(Metals.MetalType.ALUMINUM);
// these two below may or may not work, better to keep them than remove
		aluminumSheet = BlockTags.create(new ResourceLocation("sheetmetals/aluminum"));
		aluminumWire = BlockTags.create(new ResourceLocation("wires/aluminum"));
	}


	//creates new thread if needed
	public static void startThread()
	{
		getInstance().start();
	}

	//stops and kills thread
	public static void stopThread()
	{
		if (INSTANCE != null)
		{
			INSTANCE.stop();
			INSTANCE = null;
		}
	}

	public ScanResult requestScanResult()
	{
		lock.lock();
		try
		{
			return scanResult;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			lock.unlock();
			return new ScanResult();    // empty ScanResult so it doesn't crash
		}
	}

	public void releaseScanResult()
	{
		lock.unlock();
	}

	public void setScanRange(int range)
	{
		scanRange = range;
	}

	public Vec3 getClosestMetalObject()
	{
		if (closestMetalObjectInLookVector == null)
		{
			return null;
		}

		// return copy adjusted for center
		return new Vec3(closestMetalObjectInLookVector.x(), closestMetalObjectInLookVector.y(), closestMetalObjectInLookVector.z());
	}

	public void start()
	{
		if (t == null || isStopping)
		{
			CosmereAPI.logger.info("Starting lines thread");
			t = new Thread(this, "lines_thread");
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

	private void setScanResult(ScanResult result)
	{
		scanResult = result;
	}

	private void setClosestMetalObject(Vec3 vector)
	{
		closestMetalObjectInLookVector = vector;
	}

	private Vec3i toVec3i(Vec3 vector)
	{
		if (vector == null)
		{
			return null;
		}

		return new Vec3i((int) vector.x(), (int) vector.y(), (int) vector.z());
	}

	// this should be threaded to avoid lag spikes on the render thread when flaring metals
	@Override
	@OnlyIn(Dist.CLIENT)
	public void run()
	{
		final Minecraft mc = Minecraft.getInstance();
		while (!isStopping)
		{
			try
			{
				AtomicReference<Vec3> closestMetalThingLookedAt = new AtomicReference<>(null);
				ScanResult nextScan;
				LocalPlayer playerEntity = mc.player;
				nextScan = new ScanResult();
				// todo: add configurable tick rate for this thread

				//find all the things that we want to draw a line to from the player
				//metal blocks
				{
					BlockPos.withinManhattanStream(playerEntity.blockPosition(), scanRange, scanRange, scanRange)
							.filter(blockPos ->
							{
								Block block = playerEntity.level().getBlockState(blockPos).getBlock();
								final boolean validMetalBlock = block instanceof IHasMetalType iHasMetalType && iHasMetalType.getMetalType() != Metals.MetalType.ALUMINUM;
								boolean isGood = validMetalBlock || containsMetal(block);

								if (isGood)
								{
									Player player = Minecraft.getInstance().player;
									Level level = Minecraft.getInstance().level;
									// if level is null, the player has no world loaded, so stop
									if (player == null || mc.level == null)
									{
										stopThread();
										return false;
									}
									isGood = !isBlockObscured(blockPos, player, level);

									if (isGood)
									{
										// compare player look vector to directional vector
										closestMetalThingLookedAt.set(compareVectors(blockPos, player, closestMetalThingLookedAt.get()));
									}
								}

								return isGood;
							})
							.forEach(blockPos -> nextScan.addBlock(blockPos.immutable(), closestMetalThingLookedAt.get()));

					Vec3 possibleClosestMetalObject = nextScan.finalizeClusters();
					if (possibleClosestMetalObject != null)
					{
						closestMetalThingLookedAt.set(possibleClosestMetalObject);
					}
				}

				//entities with metal armor/tools
				{
					EntityHelper.getEntitiesInRange(playerEntity, scanRange, false).forEach(entity ->
					{
						Player player = Minecraft.getInstance().player;
						Level level = Minecraft.getInstance().level;
						// if level is null, the player has no world loaded, so stop
						if (player == null || mc.level == null)
						{
							stopThread();
							return;
						}
						if (entityContainsMetal(entity)
								&& !isEntityObscured(entity, player, level))
						{
							nextScan.foundEntities.add(
									entity.position().add(
											0,
											entity.getBoundingBox().getYsize() / 2,
											0));

							closestMetalThingLookedAt.set(compareVectors(entity.position().add(0, entity.getBoundingBox().getYsize() / 2, 0), player, closestMetalThingLookedAt.get()));
						}
					});
				}

				if (lock.tryLock())
				{
					setScanResult(nextScan);
					setClosestMetalObject(closestMetalThingLookedAt.get());
					lock.unlock();
				}
			}
			catch (Exception e)
			{
				CosmereAPI.logger.info("Unexpected exception in lines thread: \n" + Arrays.toString(e.getStackTrace()));

				break;
			}
		}
		stopThread();
	}

	private boolean isBlockObscured(BlockPos blockPos, Player player, Level level)
	{
		boolean isObscured;
		Vec3 currVec = player.getEyePosition();
		currVec = currVec.add(-0.75D, 0D, 0D);
		Vec3 endPos = blockPos.getCenter();
		endPos = endPos.add(-0.75D, 0D, 0D);
		Vec3 endFloorVec = new Vec3(Math.floor(endPos.x), Math.floor(endPos.y), Math.floor(endPos.z));
		double resistance = 0.0F;

		// linear interpolation to see if the block is obscured by blocks
		int loopTimes = (int) Math.ceil(currVec.distanceTo(endPos));
		for (int i = 0; i < loopTimes; i++)
		{
			BlockState bState = Objects.requireNonNull(level.getBlockState(new BlockPos(toVec3i(currVec))));
			Vec3 currFloorVec = new Vec3(Math.floor(currVec.x), Math.floor(currVec.y), Math.floor(currVec.z));

			if (currFloorVec.equals(endFloorVec) || resistance >= 1.0F)
			{
				break;
			}

			Block currBlock = bState.getBlock();

			if (bState.is(aluminumOre)
					|| bState.is(aluminumStorage)
					|| bState.is(aluminumSheet)
					|| bState.is(aluminumWire)
					|| (currBlock instanceof IHasMetalType iHasMetalType && iHasMetalType.getMetalType() == Metals.MetalType.DURALUMIN))
			{
				// aluminum completely blocks steelsight
				resistance += 1.0F;
			}
			else
			{
				resistance += AllomancyIronSteel.getResistance(bState);
			}

			double distance = currVec.distanceTo(endPos);
			currVec = currVec.lerp(endPos, 1.0F / distance);
		}

		isObscured = resistance >= 1.0F;
		return isObscured;
	}

	private boolean isEntityObscured(Entity entity, Player player, Level level)
	{
		double resistance = 0.0F;
		try
		{
			Vec3 currVec = player.getEyePosition();
			currVec = currVec.add(-0.75D, 0D, 0D);
			Vec3 endPos = entity.position();
			endPos = endPos.add(-0.75D, 0D, 0D);

			// linear interpolation to see if the entity is obscured by blocks
			int loopTimes = (int) Math.ceil(currVec.distanceTo(endPos));
			for (int i = 0; i < loopTimes; i++)
			{
				BlockState bState = Objects.requireNonNull(level.getBlockState(new BlockPos(toVec3i(currVec))));

				final boolean pastEntity = (player.getEyePosition().distanceTo(currVec) >= player.getEyePosition().distanceTo(endPos));

				if (pastEntity || resistance >= 1.0F)
				{
					break;
				}

				Block currBlock = level.getBlockState(new BlockPos(toVec3i(currVec))).getBlock();

				if (bState.is(aluminumOre) || bState.is(aluminumStorage) || bState.is(aluminumSheet) || bState.is(aluminumWire) || (currBlock instanceof IHasMetalType iHasMetalType && iHasMetalType.getMetalType() == Metals.MetalType.DURALUMIN))
				{
					// aluminum completely blocks steelsight
					resistance += 1.0F;
				}
				else
				{
					resistance += AllomancyIronSteel.getResistance(bState);
				}

				double distance = currVec.distanceTo(endPos);
				currVec = currVec.lerp(endPos, 1.0F / distance);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resistance = 100.0F;    // just to be sure :)
		}

		if (resistance < 1.0F)
		{
			//entity is not obscured, can be added to the list
			return false;
		}

		//entity obscured
		return true;
	}

	private Vec3 compareVectors(BlockPos blockPos, Player player, Vec3 currentClosestMetalObject)
	{
		Vec3 lookVector = player.getLookAngle();
		Vec3 vectorToPos = blockPos.getCenter().subtract(player.getEyePosition());
		Vec3 playerPos = player.getEyePosition();
		vectorToPos = vectorToPos.normalize();

		double dynamicTolerance = tolerance / playerPos.distanceTo(blockPos.getCenter());

		if (vectorToPos.distanceTo(lookVector) < dynamicTolerance)
		{
			if (currentClosestMetalObject == null)
			{
				return blockPos.getCenter();
			}

			Vec3 currentVector = currentClosestMetalObject.subtract(player.getEyePosition());
			currentVector = currentVector.normalize();

			if (vectorToPos.distanceTo(lookVector) > currentVector.distanceTo(lookVector))
			{
				return currentClosestMetalObject;
			}
			else
			{
				return blockPos.getCenter();
			}
		}

		return currentClosestMetalObject;
	}

	private Vec3 compareVectors(Vec3 pos, Player player, Vec3 currentClosestMetalObject)
	{
		Vec3 lookVector = player.getLookAngle();
		Vec3 vectorToPos = pos.subtract(player.getEyePosition());
		Vec3 playerPos = player.getEyePosition();
		vectorToPos = vectorToPos.normalize();

		double dynamicTolerance = tolerance / playerPos.distanceTo(pos);

		if (vectorToPos.distanceTo(lookVector) < dynamicTolerance)
		{
			if (currentClosestMetalObject == null)
			{
				return pos;
			}

			Vec3 currentVector = currentClosestMetalObject.subtract(player.getEyePosition());
			currentVector = currentVector.normalize();

			if (vectorToPos.distanceTo(lookVector) > currentVector.distanceTo(lookVector))
			{
				return currentClosestMetalObject;
			}
			else
			{
				return pos;
			}
		}

		return currentClosestMetalObject;
	}
}

/*
 * File updated ~ 28 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.client.metalScanning.ScanResult;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.entities.CoinProjectile;
import leaf.cosmere.api.*;
import leaf.cosmere.api.helpers.CodecHelper;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.helpers.ResourceLocationHelper;
import leaf.cosmere.api.math.VectorHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.packets.SyncPushPullMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AllomancyIronSteel extends AllomancyManifestation
{
	private static final IronSteelLinesThread linesThread = new IronSteelLinesThread();
	private static final HashMap<Material, Double> materialResistanceMap = initHashMap();

	private final boolean isPush;
	private static Set<String> s_whiteList = null;

	public AllomancyIronSteel(Metals.MetalType metalType)
	{
		super(metalType);
		this.isPush = metalType == Metals.MetalType.STEEL;
	}

	public static ScanResult requestScanResult()
	{
		return linesThread.requestScanResult();
	}

	public static void releaseScanResult()
	{
		linesThread.releaseScanResult();
	}

	public static void startLinesThread()
	{
		CosmereAPI.logger.info("Thread started");
		linesThread.start();
	}

	public static void stopLinesThread()
	{
		CosmereAPI.logger.info("Thread stopped");
		linesThread.stop();
	}

	private static HashMap<Material, Double> initHashMap()
	{
		HashMap<Material, Double> output = new HashMap<>();

		// map definitely isn't done, but this is a good start
		output.put(Material.STONE, 0.5);
		output.put(Material.DIRT, 0.2);
		output.put(Material.WOOD, 0.25);
		output.put(Material.METAL, 0.5);
		output.put(Material.PISTON, 0.5);
		output.put(Material.SAND, 0.2);
		output.put(Material.CLAY, 0.2);

		return output;
	}

	@Override
	public void applyEffectTick(ISpiritweb data)
	{
		if (data.getLiving().level.isClientSide)
		{
			performEffectClient(data);
		}
		else
		{
			performEffectServer(data);
		}
	}

	@Override
	public void onModeChange(ISpiritweb cap, int lastMode)
	{
		super.onModeChange(cap, lastMode);

		if (getMode(cap) != 0)
		{
			if (getMode(cap) > 0)
				startLinesThread();
			else if (getMode(cap) <= 0)
				stopLinesThread();
			return;
		}

		SpiritwebCapability data = (SpiritwebCapability) cap;

		List<BlockPos> blocks = isPush ? data.pushBlocks : data.pullBlocks;
		List<Integer> entities = isPush ? data.pushEntities : data.pullEntities;

		blocks.clear();
		entities.clear();
	}

	@OnlyIn(Dist.CLIENT)
	private void performEffectClient(ISpiritweb cap)
	{
		boolean hasChanged = false;
		SpiritwebCapability data = (SpiritwebCapability) cap;
		List<BlockPos> blocks = isPush ? data.pushBlocks : data.pullBlocks;
		List<Integer> entities = isPush ? data.pushEntities : data.pullEntities;

		if (s_whiteList == null)
		{
			createWhitelist(cap.getLiving());
		}

		//Pushes/Pulls on Nearby Metals
		if (getKeyBinding().isDown())
		{
			Player player = Minecraft.getInstance().player;
			Level level = Minecraft.getInstance().level;
			Minecraft mc = Minecraft.getInstance();

			Vec3 lerpAngle = player.getLookAngle();
			Vec3 currPos = player.getEyePosition();
			float resistance = 0.0F;
			boolean hitEntity = false;
			Entity entityHitResult = null;

			// lerp forward within range
			for (int i = 0; i < getRange(cap); i++)
			{
				BlockState blockAtPos = level.getBlockState(new BlockPos(currPos));

				// if block is air, might be entity. check.
				if (blockAtPos.isAir())
				{
					try
					{
						AABB aabb = new AABB(new BlockPos(currPos));
						Entity firstMetalEntity = null;
						for (Entity ent : level.getEntities(player, aabb, potentialEntityHit -> !potentialEntityHit.isSpectator())) {
							if (entityContainsMetal(ent)) {
								firstMetalEntity = ent;
								break;
							}
						}
						hitEntity = firstMetalEntity != null && entityContainsMetal(firstMetalEntity);
						if (hitEntity)
						{
							entityHitResult = firstMetalEntity;
							break;
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
						hitEntity = false;
					}
				}

				// if resistance is 100+% or the currently targeted block has metal; exit
				if (resistance >= 1.0F || containsMetal(blockAtPos.getBlock()))
					break;

				// if block isn't air, add material resistance value
				if (!blockAtPos.isAir())
					resistance += materialResistanceMap.getOrDefault(blockAtPos.getMaterial(), 0.0D);

				// lerp to next position (1 block forward)
				currPos = currPos.add(lerpAngle);
			}

			if (resistance < 1.0F && !hitEntity)
			{
				BlockPos pos = new BlockPos(currPos);
				//todo check block is of ihasmetal type
				BlockState state = mc.level.getBlockState(pos);
				if (state.getBlock() instanceof IHasMetalType || containsMetal(state.getBlock()))
				{
					blocks.add(pos.immutable());

					if (blocks.size() > 5)
					{
						blocks.remove(0);
					}
					hasChanged = true;
				}
			}

			if (hitEntity)
			{
				//tracks entity if it meets requirements
				//eg must contain metal
				hasChanged = trackValidEntity(data, entityHitResult);
			}
		}
		else
		{
			//clear list
			if (blocks.size() > 0)
			{
				blocks.clear();
				hasChanged = true;
			}
			if (entities.size() > 0)
			{
				entities.clear();
				hasChanged = true;
			}

		}

		//sync the move things.
		//we don't let the spirit web sync from client back to server, so this is needed.
		if (hasChanged)
		{
			CompoundTag nbt = new CompoundTag();
			CodecHelper.BlockPosListCodec.encodeStart(NbtOps.INSTANCE, blocks)
					.resultOrPartial(CosmereAPI.logger::error)
					.ifPresent(inbt1 -> nbt.put(isPush ? "pushBlocks" : "pullBlocks", inbt1));
			nbt.putIntArray(isPush ? "pushEntities" : "pullEntities", entities);
			Allomancy.packetHandler().sendToServer(new SyncPushPullMessage(nbt));
		}
	}


	public boolean trackValidEntity(ISpiritweb cap, Entity entity)
	{
		//perform the entity move thing.
		SpiritwebCapability data = (SpiritwebCapability) cap;
		List<Integer> entities = isPush ? data.pushEntities : data.pullEntities;

		if (!entities.contains(entity.getId()) && entityContainsMetal(entity))
		{
			entities.add(entity.getId());
			if (entities.size() > 5)
			{
				entities.remove(0);
			}
			return true;
		}

		return false;
	}

	private void performEffectServer(ISpiritweb cap)
	{
		if (cap.getLiving().tickCount % 3 == 0)
		{
			return;
		}

		//perform the entity move thing.
		SpiritwebCapability data = (SpiritwebCapability) cap;
		//todo change this. We shouldn't be setting data on the manifestation base
		List<BlockPos> blocks = isPush ? data.pushBlocks : data.pullBlocks;
		List<Integer> entities = isPush ? data.pushEntities : data.pullEntities;

		if (!blocks.isEmpty())
		{
			pushpullBlocks(data);
		}
		if (!entities.isEmpty())
		{
			pushpullEntities(data);
		}
	}

	private void pushpullEntities(SpiritwebCapability data)
	{
		List<Integer> entities = isPush ? data.pushEntities : data.pullEntities;
		for (int i = entities.size() - 1; i >= 0; i--)
		{
			int entityID = entities.get(i);
			final LivingEntity dataLiving = data.getLiving();
			Entity targetEntity = dataLiving.level.getEntity(entityID);
			if (targetEntity != null)
			{
				if (targetEntity.blockPosition().closerThan(dataLiving.blockPosition(), getRange(data)))
				{
					//move small things
					if (targetEntity instanceof ItemEntity itemEntity)
					{
						moveEntityTowards(itemEntity, dataLiving.blockPosition());
					}
					//affect both entities
					else if (targetEntity instanceof LivingEntity livingEntity)
					{
						moveEntityTowards(livingEntity, dataLiving.blockPosition());
						moveEntityTowards(dataLiving, livingEntity.blockPosition());
						dataLiving.hurtMarked = true;
					}
					//affect entity who is doing the push/pull
					else
					{
						if (isPush)
						{
							moveEntityTowards(dataLiving, targetEntity.blockPosition());
						}
						//if not push, then check if we should pull coin projectiles back to player
						else if (dataLiving instanceof Player player && targetEntity instanceof CoinProjectile coinProjectile)
						{
							//technically we could do this with item entities, but I like how those currently work
							//Doing this with projectiles meansa I don't have to mess with the physics of un-sticking
							//the coin projectiles from whatever surface they may be attached to
							coinProjectile.playerTouch(player);
						}
					}
				}
			}
			else
			{
				//remove entities the level couldn't find
				entities.remove(i);
			}
		}
	}

	private void moveEntityTowards(Entity entity, BlockPos toMoveTo)
	{
		Vec3 blockCenter = Vec3.atCenterOf(toMoveTo);

		Vec3 direction = VectorHelper.getDirection(
				blockCenter,
				Vec3.atCenterOf(entity.blockPosition()),//use entity block position, so we can do things like hover directly over a block more easily
				(isPush ? -1f : 2f));

		//todo, clean up all the unnecessary calculations once we find what feels good at run time
		Vec3 normalize = direction.normalize();

		double shortenFactor = isPush ? 0.2 : 0.4;
		Vec3 add = entity.getDeltaMovement().add(normalize.multiply(shortenFactor, shortenFactor, shortenFactor));

		//get flung off rides
		entity.stopRiding();
		//don't let the motion go crazy large
		entity.setDeltaMovement(VectorHelper.ClampMagnitude(add, 1));
		//hurt marked true means it will tell clients that they are moving.
		entity.hurtMarked = true;

		//let people get damaged but not too much?
		//todo check what a good max fall distance would be
		//todo add to config
		if (entity instanceof Player player)
		{
			//doesn't really work, because entity may not be pushing anymore and so this wont get hit.
			player.fallDistance = Math.min(player.fallDistance, 1);
		}
	}

	private void pushpullBlocks(SpiritwebCapability data)
	{
		List<BlockPos> blocks = isPush ? data.pushBlocks : data.pullBlocks;
		int blockListCount = blocks.size();

		if (blockListCount == 0)
		{
			return;
		}

		LivingEntity living = data.getLiving();

		for (int i = blockListCount - 1; i >= 0; i--)
		{
			BlockPos blockPos = blocks.get(i);
			if (!isPush && blockPos.distManhattan(living.blockPosition()) < 2)
			{
				//stop shoving the user into the block
				continue;
			}
			//if the entity is in range of being able to push or pull from
			double maxDistance = getRange(data);
			if (blockPos.closerThan(living.blockPosition(), maxDistance))
			{
				moveEntityTowards(living, blockPos);
			}
			else
			{
				//we don't want to remove blocks that are out of distance
				//in case we are still holding the button down and get back into range
				//blocks.remove(i);
			}
		}
		living.hurtMarked = true;
	}

	private static boolean entityContainsMetal(Entity entity)
	{
		if (entity instanceof LivingEntity livingEntity)
		{
			if (containsMetal(entity))
			{
				return true;
			}

			if (containsMetal(livingEntity.getMainHandItem()) || containsMetal(livingEntity.getOffhandItem()))
			{
				return true;
			}

			for (ItemStack itemStack : livingEntity.getArmorSlots())
			{
				if (containsMetal(itemStack.getItem()))
				{
					return true;
				}
			}

			/* //probably overkill, todo decide if we want this.
			if (livingEntity instanceof Player)
			{
				Player player = ((Player) livingEntity);
				for (ItemStack itemStack : player.getInventory().items)
				{
					if (containsMetal(itemStack.getItem().getRegistryName().getPath()))
					{
						return true;
					}
				}
			}*/
			return false;
		}
		else if (entity instanceof ItemEntity itemEntity)
		{
			ItemStack stack = (itemEntity).getItem();
			Item item = stack.getItem();

			if (item instanceof BlockItem blockItem && containsMetal(blockItem.getBlock()))
			{
				return true;
			}

			final boolean validMetalItem = containsMetal(item);
			return validMetalItem;
		}
		else if (entity instanceof CoinProjectile coinProjectile)
		{
			return true;
		}

		return false;
	}

	private static boolean containsMetal(ItemStack itemStack)
	{
		return containsMetal(itemStack.getItem());
	}

	private static boolean containsMetal(Item item)
	{
		if (item.builtInRegistryHolder().is(CosmereTags.Items.CONTAINS_METAL))
		{
			return true;
		}
		if (s_whiteList == null)
		{
			return false;
		}
		return s_whiteList.contains(ResourceLocationHelper.get(item).getPath());
	}


	private static boolean containsMetal(Block block)
	{
		if (block.builtInRegistryHolder().is(CosmereTags.Blocks.CONTAINS_METAL))
		{
			return true;
		}
		if (s_whiteList == null)
		{
			return false;
		}
		return s_whiteList.contains(ResourceLocationHelper.get(block).getPath());
	}

	private static boolean containsMetal(Entity entity)
	{
		if (entity.getType().is(CosmereTags.EntityTypes.CONTAINS_METAL))
		{
			return true;
		}
		if (s_whiteList == null)
		{
			return false;
		}
		return s_whiteList.contains(ResourceLocationHelper.get(entity).getPath());
	}

	public static void invalidateWhitelist()
	{
		s_whiteList = null;
	}

	//client side is the only time this gets initialized.
	// this does not work for items with no recipe, such as vanilla ores
	private static void createWhitelist(Entity entity)
	{
		if (s_whiteList != null)
		{
			return;
		}

		s_whiteList = new HashSet<>();

		final TagKey<Item> containsMetal = CosmereTags.Items.CONTAINS_METAL;
		final RecipeManager recipeManager = entity.level.getRecipeManager();
		final Collection<Recipe<?>> recipes = recipeManager.getRecipes();

		for (var recipe : recipes)
		{
			final ItemStack resultItem = recipe.getResultItem();

			if (resultItem.is(containsMetal))
			{
				continue;
			}

			CheckRecipeForMetal(containsMetal, recipe, resultItem);
		}
	}


	public static void CheckRecipeForMetal(TagKey<Item> containsMetal, Recipe<?> recipe, ItemStack resultItem)
	{
		for (Ingredient ingredient : recipe.getIngredients())
		{
			for (ItemStack itemStack : ingredient.getItems())
			{
				if (itemStack.is(containsMetal))
				{
					//found one
					final Holder.Reference<Item> itemReference = resultItem.getItem().builtInRegistryHolder();
					List<TagKey<Item>> allTags = itemReference.tags().collect(Collectors.toList());
					allTags.add(CosmereTags.Items.CONTAINS_METAL);
					itemReference.bindTags(allTags);

					CosmereAPI.logger.info(itemReference + " has been identified as containing metal.");

					s_whiteList.add(ResourceLocationHelper.get(resultItem.getItem()).getPath());
					return;
				}
			}
		}
	}

	// set scan range of Lines Thread
	public static void setScanRange(int range)
	{
		linesThread.setScanRange(range);
	}

	// this should be threaded to avoid lag spikes on the render thread when flaring metals
	static class IronSteelLinesThread implements Runnable {
		private static Thread t;
		private static final Lock lock = new ReentrantLock();
		private static ScanResult scanResult = new ScanResult();
		private static int scanRange = 0;
		private static boolean isStopping = false;
		final static ProfilerFiller profiler = Minecraft.getInstance().getProfiler();

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
				return new ScanResult();	// empty ScanResult so it doesn't crash
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

		public void start()
		{
			if (t == null)
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

		@Override
		@OnlyIn(Dist.CLIENT)
		public void run() {
			while (!isStopping) {
				final Minecraft mc = Minecraft.getInstance();
				ScanResult nextScan;
				LocalPlayer playerEntity = mc.player;
				profiler.push("cosmere-linesThread");
				//only update box list every so often
				if ((playerEntity.tickCount + 5) % 5 != 0)
				{
					try
					{
						Thread.sleep(50);
					}
					catch (InterruptedException e)
					{
						throw new RuntimeException(e);
					}
					continue;
				}
				nextScan = new ScanResult();

				//find all the things that we want to draw a line to from the player

				//metal blocks
				{
					BlockPos.withinManhattanStream(playerEntity.blockPosition(), scanRange, scanRange, scanRange)
							.filter(blockPos ->
							{
								Block block = playerEntity.level.getBlockState(blockPos).getBlock();
								final boolean validMetalBlock = block instanceof IHasMetalType iHasMetalType && iHasMetalType.getMetalType() != Metals.MetalType.ALUMINUM;
								boolean isGood = validMetalBlock || containsMetal(block);

								if (isGood)
								{
									try
									{
										Player player = Minecraft.getInstance().player;
										Level level = Minecraft.getInstance().level;
										BlockState endBlock = Objects.requireNonNull(level.getBlockState(blockPos));
										Vec3 currVec = player.getEyePosition();
										Vec3 endPos = new Vec3(blockPos.getX() + 0.5F, blockPos.getY() + 0.5F, blockPos.getZ() + 0.5F);
										double resistance = 0.0F;

										// linear interpolation to see if the block is obscured by blocks
										// this method is, insanely enough, *more performant* than not filtering at all
										int loopTimes = (int) Math.ceil(currVec.distanceTo(endPos));
										for (int i = 0; i < loopTimes; i++)
										{
											BlockState bState = Objects.requireNonNull(level.getBlockState(new BlockPos(currVec)));

											if (bState == endBlock || resistance >= 1.0F)
											{
												break;
											}

											resistance += (materialResistanceMap.containsKey(bState.getMaterial())) ? materialResistanceMap.get(bState.getMaterial()) : 0.0F;

											double distance = currVec.distanceTo(endPos);
											currVec = currVec.lerp(endPos, 1.0F / distance);
										}

										isGood = resistance < 1.0F;
									}
									catch (Exception e)
									{
										e.printStackTrace();
										isGood = false;
									}
								}

								return isGood;
							})
							.forEach(blockPos -> nextScan.addBlock(blockPos.immutable()));

					nextScan.finalizeClusters();
				}

				//entities with metal armor/tools
				{
					EntityHelper.getEntitiesInRange(playerEntity, scanRange, false).forEach(entity ->
					{
						if (entityContainsMetal(entity)) {
							double resistance = 0.0F;
							try
							{
								Player player = Minecraft.getInstance().player;
								Level level = Minecraft.getInstance().level;
								Vec3 currVec = player.getEyePosition();
								Vec3 endPos = new Vec3(entity.getX(), entity.getY(), entity.getZ());

								// linear interpolation to see if the entity is obscured by blocks
								// this method is, insanely enough, *more performant* than not filtering at all
								int loopTimes = (int) Math.ceil(currVec.distanceTo(endPos));
								for (int i = 0; i < loopTimes; i++)
								{
									BlockState bState = Objects.requireNonNull(level.getBlockState(new BlockPos(currVec)));

									final boolean pastEntity = (player.getEyePosition().distanceTo(currVec) >= player.getEyePosition().distanceTo(endPos));

									if (pastEntity || resistance >= 1.0F)
									{
										break;
									}

									resistance += (materialResistanceMap.containsKey(bState.getMaterial())) ? materialResistanceMap.get(bState.getMaterial()) : 0.0F;

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
								nextScan.foundEntities.add(
										entity.position().add(
												0,
												entity.getBoundingBox().getYsize() / 2,
												0));
							}
						}
					});
				}

				lock.lock();
				try
				{
					setScanResult(nextScan);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					lock.unlock();
				}
				profiler.pop();
			}
		}
	}
}


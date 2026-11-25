/*
 * File updated ~ 30 - 4 - 2025 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.client.metalScanning.IronSteelLinesThread;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.config.AllomancyConfigs;
import leaf.cosmere.allomancy.common.entities.CoinProjectile;
import leaf.cosmere.allomancy.common.items.CoinPouchItem;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.CodecHelper;
import leaf.cosmere.api.math.VectorHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.packets.SyncPushPullMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

public class AllomancyIronSteel extends AllomancyManifestation
{
	public static final HashMap<TagKey<Block>, Double> materialResistanceMap = initHashMap();

	private final boolean isPush;

	public AllomancyIronSteel(Metals.MetalType metalType)
	{
		super(metalType);
		this.isPush = metalType == Metals.MetalType.STEEL;
	}

	private static HashMap<TagKey<Block>, Double> initHashMap()
	{
		HashMap<TagKey<Block>, Double> output = new HashMap<>();

		output.put(BlockTags.MINEABLE_WITH_PICKAXE, 0.5);
		output.put(BlockTags.MINEABLE_WITH_SHOVEL, 0.2);
		output.put(BlockTags.MINEABLE_WITH_AXE, 0.25);

		return output;
	}

	public static Double getResistance(BlockState blockState)
	{
		Double retValue = 0D;

		for (TagKey<Block> tagKey : materialResistanceMap.keySet())
		{
			if (blockState.is(tagKey))
			{
				retValue = materialResistanceMap.get(tagKey);
				break;
			}
		}

		return retValue;
	}

	@Override
	public void applyEffectTick(ISpiritweb data)
	{
		if (data.getLiving().level().isClientSide)
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
		if (cap.getLiving().level().isClientSide)
		{

			super.onModeChange(cap, lastMode);

			if (getMode(cap) != 0)
			{
				if (getMode(cap) > 0)
				{
					IronSteelLinesThread.startThread();
				}
				else if (getMode(cap) <= 0)
				{
					IronSteelLinesThread.stopThread(false);
				}
				return;
			}

			SpiritwebCapability data = (SpiritwebCapability) cap;

			List<BlockPos> blocks = isPush ? data.pushBlocks : data.pullBlocks;
			List<Integer> entities = isPush ? data.pushEntities : data.pullEntities;

			blocks.clear();
			entities.clear();
		}
	}

	@OnlyIn(Dist.CLIENT)
	private void performEffectClient(ISpiritweb cap)
	{
		boolean hasChanged = false;
		SpiritwebCapability data = (SpiritwebCapability) cap;
		List<BlockPos> blocks = isPush ? data.pushBlocks : data.pullBlocks;
		List<Integer> entities = isPush ? data.pushEntities : data.pullEntities;

		//Pushes/Pulls on Nearby Metals
		if (getKeyBinding().isDown())
		{
			final Minecraft mc = Minecraft.getInstance();
			Player player = mc.player;
			Level level = mc.level;

			boolean hitEntity = false;
			Entity entityHitResult = null;

			Vec3 closestMetalObjectVec3 = IronSteelLinesThread.getInstance().getClosestMetalObject();
			BlockPos closestMetalObject = null;
			if (closestMetalObjectVec3 != null)
			{
				closestMetalObject = BlockPos.containing(closestMetalObjectVec3);
			}

			if (closestMetalObject != null)
			{
				BlockState blockAtPos = level.getBlockState(closestMetalObject);

				if (blockAtPos.isAir())
				{
					try
					{
						AABB aabb = new AABB(closestMetalObject);
						Entity firstMetalEntity = null;
						for (Entity ent : level.getEntities(player, aabb, potentialEntityHit -> !potentialEntityHit.isSpectator()))
						{
							if (entityContainsMetal(ent))
							{
								firstMetalEntity = ent;
								break;
							}
						}
						hitEntity = firstMetalEntity != null && entityContainsMetal(firstMetalEntity);
						if (hitEntity)
						{
							entityHitResult = firstMetalEntity;
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
						hitEntity = false;
					}
				}

				if (hitEntity)
				{
					//tracks entity if it meets requirements
					//eg must contain metal
					hasChanged = trackValidEntity(data, entityHitResult);
				}
				else
				{
					blocks.add(closestMetalObject);

					if (blocks.size() > 5)
					{
						blocks.remove(0);
					}
					hasChanged = true;
				}
			}
		}
		else
		{
			//clear list
			if (!blocks.isEmpty())
			{
				blocks.clear();
				hasChanged = true;
			}
			if (!entities.isEmpty())
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
			nbt.putInt("weight", IronSteelLinesThread.getInstance().getWeight());
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
		int weight = data.pushPullWeight;
		for (int i = entities.size() - 1; i >= 0; i--)
		{
			int entityID = entities.get(i);
			final LivingEntity dataLiving = data.getLiving();
			Entity targetEntity = dataLiving.level().getEntity(entityID);
			if (targetEntity != null)
			{
				if (targetEntity.blockPosition().closerThan(dataLiving.blockPosition(), getRange(data)))
				{
					//move small things
					if (targetEntity instanceof ItemEntity itemEntity)
					{
						// pick up item if pulling
						if (dataLiving instanceof Player player && !isPush)
						{
							itemEntity.playerTouch(player);
						}
						else
						{
							moveEntityTowards(itemEntity, dataLiving.blockPosition(), weight);
						}
					}
					//affect both entities
					else if (targetEntity instanceof LivingEntity livingEntity)
					{
						moveEntityTowards(livingEntity, dataLiving.blockPosition(), weight);
						moveEntityTowards(dataLiving, livingEntity.blockPosition(), weight);
						dataLiving.hurtMarked = true;
					}
					//affect entity who is doing the push/pull
					else
					{
						if (isPush)
						{
							moveEntityTowards(dataLiving, targetEntity.blockPosition(), weight);
						}
						//if not push, then check if we should pull coin projectiles back to player
						else if (dataLiving instanceof Player player && targetEntity instanceof CoinProjectile coinProjectile)
						{
							CoinPouchItem.onPickupItem(coinProjectile, player);
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

	private void moveEntityTowards(Entity entity, BlockPos toMoveTo, int weight)
	{
		Vec3 blockCenter = toMoveTo.getCenter();

		Vec3 direction = VectorHelper.getDirection(
				blockCenter,
				entity.blockPosition().getCenter(),//use entity block position, so we can do things like hover directly over a block more easily
				(isPush ? -1f : 2f));

		//todo, clean up all the unnecessary calculations once we find what feels good at run time
		Vec3 normalize = direction.normalize();

		double shortenFactor = isPush ? 0.2 : 0.4;
		Vec3 add = entity.getDeltaMovement().add(normalize.multiply(shortenFactor, shortenFactor, shortenFactor));

		// cannot have flat multiplier; can get ridiculous
		double adjustedWeight = 1d + Math.min(AllomancyConfigs.SERVER.MAX_PUSH_PULL_WEIGHT.get(), (weight - 1) * AllomancyConfigs.SERVER.PUSH_PULL_WEIGHT.get());

		//get flung off rides
		entity.stopRiding();
		//don't let the motion go crazy large
		entity.setDeltaMovement(VectorHelper.ClampMagnitude(add, 1).multiply(adjustedWeight, adjustedWeight, adjustedWeight));
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
		int weight = data.pushPullWeight;
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
				moveEntityTowards(living, blockPos, weight);
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

	public static boolean entityContainsMetal(Entity entity)
	{
		if (containsMetal(entity))
		{
			return true;
		}

		if (entity instanceof LivingEntity livingEntity)
		{
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

	public static boolean containsMetal(ItemStack itemStack)
	{
		return containsMetal(itemStack.getItem());
	}

	public static boolean containsMetal(Item item)
	{
		return BuiltInRegistries.ITEM.wrapAsHolder(item).is(CosmereTags.Items.CONTAINS_METAL);
	}

	public static boolean containsMetal(Block block)
	{
		return block.defaultBlockState().is(CosmereTags.Blocks.CONTAINS_METAL);
	}

	public static boolean containsMetal(Entity entity)
	{
		if (entity.getType().is(CosmereTags.EntityTypes.CONTAINS_METAL))
		{
			return true;
		}
		// minecarts, boats etc. resolve to their item
		final ItemStack pickResult = entity.getPickResult();
		return pickResult != null && containsMetal(pickResult);
	}

	@Override
	public int getRange(ISpiritweb data)
	{
		if (!isActive(data))
		{
			return 0;
		}

		//get allomantic strength
		double allomanticStrength = getStrength(data, false);

		//no range if compounding.
		final double potentialRange = Math.max(getMode(data), 0) * allomanticStrength;
		return Mth.floor(potentialRange * AllomancyConfigs.SERVER.IRON_STEEL_RANGE.get());
	}
}


/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.entities.CoinProjectile;
import leaf.cosmere.items.IHasMetalType;
import leaf.cosmere.network.Network;
import leaf.cosmere.network.packets.SyncPushPullMessage;
import leaf.cosmere.utils.helpers.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

import static leaf.cosmere.utils.helpers.EntityHelper.getEntitiesInRange;

public class AllomancyIronSteel extends AllomancyBase
{
	private final boolean isPush;
	private static Set<String> s_whiteList = null;

	public AllomancyIronSteel(Metals.MetalType metalType)
	{
		super(metalType);
		this.isPush = metalType == Metals.MetalType.STEEL;
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
	public void onModeChange(ISpiritweb cap)
	{
		super.onModeChange(cap);

		if (getMode(cap) != 0)
		{
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

		//Pushes/Pulls on Nearby Metals
		if (getKeyBinding().isDown())
		{
			Minecraft mc = Minecraft.getInstance();
			HitResult ray = PlayerHelper.pickWithRange(cap.getLiving(), getRange(cap));

			if (ray.getType() == HitResult.Type.BLOCK && !blocks.contains(((BlockHitResult) ray).getBlockPos()))
			{
				BlockPos pos = ((BlockHitResult) ray).getBlockPos();
				//todo check block is of ihasmetal type
				BlockState state = mc.level.getBlockState(pos);
				if (state.getBlock() instanceof IHasMetalType || containsMetal(ResourceLocationHelper.get(state.getBlock()).getPath()))
				{
					blocks.add(pos.immutable());

					if (blocks.size() > 5)
					{
						blocks.remove(0);
					}
					hasChanged = true;
				}
			}

			if (ray instanceof EntityHitResult entityHitResult)
			{
				final Entity hitResultEntity = entityHitResult.getEntity();
				//tracks entity if it meets requirements
				//eg must contain metal
				hasChanged = trackValidEntity(data, hitResultEntity);
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
					.resultOrPartial(LogHelper.LOGGER::error)
					.ifPresent(inbt1 -> nbt.put(isPush ? "pushBlocks" : "pullBlocks", inbt1));
			nbt.putIntArray(isPush ? "pushEntities" : "pullEntities", entities);
			Network.sendToServer(new SyncPushPullMessage(nbt));
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
			Entity entity = data.getLiving().level.getEntity(entityID);
			if (entity != null)
			{
				if (entity.blockPosition().closerThan(data.getLiving().blockPosition(), getRange(data)))
				{
					//move small things
					if (entity instanceof ItemEntity itemEntity)
					{
						moveEntityTowards(itemEntity, data.getLiving().blockPosition());
					}
					//affect both entities
					else if (entity instanceof LivingEntity livingEntity)
					{
						moveEntityTowards(livingEntity, data.getLiving().blockPosition());
						moveEntityTowards(data.getLiving(), livingEntity.blockPosition());
						data.getLiving().hurtMarked = true;
					}
					//affect entity who is doing the push/pull
					else
					{
						if (isPush)
						{
							moveEntityTowards(data.getLiving(), entity.blockPosition());
						}
						//if not push, then check if we should pull coin projectiles back to player
						else if (data.getLiving() instanceof Player player && entity instanceof CoinProjectile coinProjectile)
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

	private static final List<Vec3> found = new ArrayList<>();


	@OnlyIn(Dist.CLIENT)
	public static List<Vec3> getDrawLines(int range)
	{
		final Minecraft mc = Minecraft.getInstance();
		final ProfilerFiller profiler = mc.getProfiler();
		LocalPlayer playerEntity = mc.player;
		//only update box list every so often
		if (playerEntity.tickCount % 15 != 0)
		{
			return found;
		}

		found.clear();

		//find all the things that we want to draw a line to from the player

		//todo stop aluminum showing up, check IHasMetalType.getMetalType != aluminum

		//metal blocks
		profiler.push("cosmere-getBlocksInRange");
		BlockPos.withinManhattanStream(playerEntity.blockPosition(), range, range, range)
				.filter(blockPos ->
				{
					Block block = playerEntity.level.getBlockState(blockPos).getBlock();
					return block instanceof IHasMetalType || AllomancyIronSteel.containsMetal(ResourceLocationHelper.get(block).getPath());
				})
				.forEach(blockPos -> found.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5)));

		profiler.pop();

		//entities with metal armor/tools
		profiler.push("cosmere-getEntitiesInRange");
		getEntitiesInRange(playerEntity, range, false).forEach(entity ->
		{
			if (entityContainsMetal(entity))
			{
				found.add(entity.position());
			}
		});
		profiler.pop();

		return found;
	}


	private static boolean entityContainsMetal(Entity entity)
	{
		if (entity instanceof LivingEntity livingEntity)
		{
			//metal entities like iron golems
			if (containsMetal(ResourceLocationHelper.get(entity).getPath()))
			{
				return true;
			}

			if (containsMetal(ResourceLocationHelper.get(livingEntity.getMainHandItem().getItem()).getPath()))
			{
				return true;
			}
			if (containsMetal(ResourceLocationHelper.get(livingEntity.getOffhandItem().getItem()).getPath()))
			{
				return true;
			}

			for (ItemStack itemStack : livingEntity.getArmorSlots())
			{
				if (containsMetal(ResourceLocationHelper.get(itemStack.getItem()).getPath()))
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

			if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof IHasMetalType || containsMetal(ResourceLocationHelper.get(item).getPath()))
			{
				return true;
			}
			if (item instanceof IHasMetalType || containsMetal(ResourceLocationHelper.get(item).getPath()))
			{
				return true;
			}
		}
		else if (entity instanceof CoinProjectile coinProjectile)
		{
			return true;
		}

		return false;
	}

	public static boolean containsMetal(String path)
	{
		if (s_whiteList == null)
		{
			createWhitelist();
		}

		final boolean foundMatch = s_whiteList.contains(path);

		//look for any block or item that contains one of our metals
		//eg iron fence

		return foundMatch;
	}

	//client side is the only time this gets initialized.
	private static void createWhitelist()
	{
		s_whiteList = new HashSet<>();

		//add the obvious stuff
		s_whiteList.add("lightning_rod");
		s_whiteList.add("netherite");
		s_whiteList.add("ancient_debris");
		s_whiteList.add("hopper");

		String[] metalNames = Arrays.stream(Metals.MetalType.values()).map(Metals.MetalType::getName).toArray(String[]::new);

		//add the potential stuff.
		//This may result in false positives.
		//requires testing.
		ForgeRegistries.ITEMS.getValues()
				.stream()
				.filter(test -> testPath(ResourceLocationHelper.get(test), metalNames))
				.forEach(match -> s_whiteList.add(ResourceLocationHelper.get(match).getPath()));
		ForgeRegistries.BLOCKS.getValues()
				.stream()
				.filter(test -> testPath(ResourceLocationHelper.get(test), metalNames))
				.forEach(match -> s_whiteList.add(ResourceLocationHelper.get(match).getPath()));
		ForgeRegistries.ENTITY_TYPES.getValues()
				.stream()
				.filter(test -> testPath(ResourceLocationHelper.get(test), metalNames))
				.forEach(match -> s_whiteList.add(ResourceLocationHelper.get(match).getPath()));

		for (String s : s_whiteList)
		{
			LogHelper.LOGGER.info("Cosmere: %s added to Push/Pull whitelist".formatted(s));
		}
	}

	private static boolean testPath(ResourceLocation test, String[] metalNames)
	{
		final String path = test.getPath();
		//No twisting vines, paintings, crafting tables or silverfish. Lead by itself is also incorrect.
		//also no pushing on aluminum, ya dang fool
		boolean misMatch =
				path.contains("ting")
						|| path.contains("tint")
						|| path.contains("silverfish")
						|| path.contains("aluminum")
						|| path.equals("lead");

		return !misMatch && Arrays.stream(metalNames).anyMatch(path::contains);
	}
}

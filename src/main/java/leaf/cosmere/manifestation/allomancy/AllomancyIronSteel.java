/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.IHasMetalType;
import leaf.cosmere.network.Network;
import leaf.cosmere.network.packets.SyncPushPullMessage;
import leaf.cosmere.utils.helpers.CodecHelper;
import leaf.cosmere.utils.helpers.LogHelper;
import leaf.cosmere.utils.helpers.VectorHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
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
import java.util.stream.Collectors;

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
	public void performEffect(ISpiritweb data)
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

	private List<BlockPos> blocks;
	private List<Integer> entities;

	@OnlyIn(Dist.CLIENT)
	private void performEffectClient(ISpiritweb cap)
	{
		boolean hasChanged = false;
		SpiritwebCapability data = (SpiritwebCapability) cap;
		blocks = isPush ? data.pushBlocks : data.pullBlocks;
		entities = isPush ? data.pushEntities : data.pullEntities;

		//Pushes on Nearby Metals
		if (getKeyBinding().isDown())
		{
			Minecraft mc = Minecraft.getInstance();
			HitResult ray = cap.getLiving().pick(getRange(cap), 0, false);

			if (ray.getType() == HitResult.Type.BLOCK && !blocks.contains(((BlockHitResult) ray).getBlockPos()))
			{
				BlockPos pos = ((BlockHitResult) ray).getBlockPos();
				//todo check block is of ihasmetal type
				BlockState state = mc.level.getBlockState(pos);
				if (state.getBlock() instanceof IHasMetalType)
				{
					blocks.add(pos.immutable());

					if (blocks.size() > 5)
					{
						blocks.remove(0);
					}
					hasChanged = true;
				}
			}
			else if (ray.getType() == HitResult.Type.ENTITY && !entities.contains(((EntityHitResult) ray).getEntity().getId()))
			{
				//todo check for metal
				entities.add(((EntityHitResult) ray).getEntity().getId());

				if (entities.size() > 5)
				{
					entities.remove(0);
				}
				hasChanged = true;
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

			final String pushBlocks = "pushBlocks";
			final String pullBlocks = "pullBlocks";
			String target = isPush ? pushBlocks : pullBlocks;

			CodecHelper.BlockPosListCodec.encodeStart(NbtOps.INSTANCE, blocks)
					.resultOrPartial(LogHelper.LOGGER::error)
					.ifPresent(inbt1 -> nbt.put(target, inbt1));

			Network.sendToServer(new SyncPushPullMessage(nbt));
		}
	}

	private void performEffectServer(ISpiritweb cap)
	{
		if (cap.getLiving().tickCount % 3 == 0)
		{
			return;
		}

		//perform the entity move thing.
		SpiritwebCapability data = (SpiritwebCapability) cap;
		blocks = isPush ? data.pushBlocks : data.pullBlocks;
		entities = isPush ? data.pushEntities : data.pullEntities;

		int blockListCount = blocks.size();

		if (blockListCount == 0)
		{
			return;
		}

		LivingEntity living = data.getLiving();
		Vec3 direction;
		float renderPartialTicks = Minecraft.getInstance().getFrameTime();

		double strength = getStrength(cap);

		for (int i = blockListCount - 1; i >= 0; i--)
		{
			BlockPos blockPos = blocks.get(i);
			if (!isPush && blockPos.distManhattan(living.blockPosition()) < 2)
			{
				//stop shoving the user into the block
				continue;
			}


			//if the entity is in range of being able to push or pull from
			double maxDistance = (strength * data.getMode(Manifestations.ManifestationTypes.ALLOMANCY, getMetalType().getID()));// * 0.1f;
			if (blockPos.closerThan(living.blockPosition(), maxDistance))
			{
				Vec3 blockCenter = Vec3.atCenterOf(blockPos);

				direction = VectorHelper.getDirection(
						blockCenter,
						living.position(),
						(isPush ? -1f : 2f) * renderPartialTicks);

				//todo, clean up all the unnecessary calculations once we find what feels good at run time
				Vec3 normalize = direction.normalize();

				double shortenFactor = isPush ? 0.2 : 0.4;
				Vec3 add = living.getDeltaMovement().add(normalize.multiply(shortenFactor, shortenFactor, shortenFactor));

				//don't let the motion go crazy large
				living.setDeltaMovement(VectorHelper.ClampMagnitude(add, 1));

				//let people get damaged but not too much?
				//todo check what a good max fall distance would be
				//todo add to config
				if (living.fallDistance > 3)
				{
					living.fallDistance = Math.min(living.fallDistance, 3);
				}
			}
			else
			{
				//remove blocks that are out of distance
				blocks.remove(i);
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
					return block instanceof IHasMetalType || AllomancyIronSteel.containsMetal(block.getRegistryName().getPath());
				})
				.forEach(blockPos -> found.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5)));

		profiler.pop();

		//entities with metal armor/tools
		profiler.push("cosmere-getEntitiesInRange");
		getEntitiesInRange(playerEntity, range, false).forEach(entity ->
		{
			if (entity instanceof LivingEntity)
			{
				//check for metal items on the entity

			}
			else if (entity instanceof ItemEntity)
			{
				ItemStack stack = ((ItemEntity) entity).getItem();
				Item item = stack.getItem();

				/*if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof IHasMetalType || )
				{
					found.add(entity.position());
				}*/
				if (item instanceof IHasMetalType || AllomancyIronSteel.containsMetal(item.getRegistryName().getPath()))
				{
					found.add(entity.position());
				}
			}
		});
		profiler.pop();

		return found;
	}

	private static boolean containsMetal(String path)
	{
		Minecraft.getInstance().getProfiler().push("cosmere-containsMetal");
		if (s_whiteList == null)
		{
			createWhitelist();
		}

		final boolean foundMatch = s_whiteList.contains(path);

		//look for any block or item that contains one of our metals
		//eg iron fence
		Minecraft.getInstance().getProfiler().pop();

		return foundMatch;
	}

	//client side is the only time this gets initialized.
	private static void createWhitelist()
	{
		s_whiteList = new HashSet<>();

		//add the obvious stuff
		s_whiteList.add("lightning_rod");

		String[] metalNames = Arrays.stream(Metals.MetalType.values()).map(Metals.MetalType::getName).toArray(String[]::new);

		//add the potential stuff.
		//This may result in false positives.
		//requires testing.
		ForgeRegistries.ITEMS.getValues()
				.stream()
				.filter(test -> testPath(test.getRegistryName(), metalNames))
				.forEach(match -> s_whiteList.add(match.getRegistryName().getPath()));
		ForgeRegistries.BLOCKS.getValues()
				.stream()
				.filter(test -> testPath(test.getRegistryName(), metalNames))
				.forEach(match -> s_whiteList.add(match.getRegistryName().getPath()));
		ForgeRegistries.ENTITIES.getValues()
				.stream()
				.filter(test -> testPath(test.getRegistryName(), metalNames))
				.forEach(match -> s_whiteList.add(match.getRegistryName().getPath()));

		for (String s : s_whiteList)
		{
			LogHelper.LOGGER.info("Cosmere: %s added to Push/Pull whitelist".formatted(s));
		}
	}

	private static boolean testPath(ResourceLocation test, String[] metalNames)
	{
		final String path = test.getPath();
		//No twisting vines, paintings, crafting tables or silverfish. Lead by itself is also incorrect.
		boolean misMatch = path.contains("ting") || path.contains("silverfish") || path.equals("lead");
		return !misMatch && Arrays.stream(metalNames).anyMatch(path::contains);
	}

}

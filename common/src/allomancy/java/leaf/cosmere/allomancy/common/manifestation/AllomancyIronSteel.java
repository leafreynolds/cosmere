/*
 * File updated ~ 15 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.client.metalScanning.IronSteelLinesThread;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.entities.CoinProjectile;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.CodecHelper;
import leaf.cosmere.api.helpers.ResourceLocationHelper;
import leaf.cosmere.api.math.VectorHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.packets.SyncPushPullMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

public class AllomancyIronSteel extends AllomancyManifestation
{
	public static final HashMap<Material, Double> materialResistanceMap = initHashMap();

	private final boolean isPush;
	private static Set<String> s_whiteList = null;
	private static Set<String> s_blackList = null;

	public AllomancyIronSteel(Metals.MetalType metalType)
	{
		super(metalType);
		this.isPush = metalType == Metals.MetalType.STEEL;
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
		if (cap.getLiving().level.isClientSide)
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
					IronSteelLinesThread.stopThread();
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

		TagKey<Block> aluminumOre = CosmereTags.Blocks.METAL_ORE_BLOCK_TAGS.get(Metals.MetalType.ALUMINUM);
		TagKey<Block> aluminumStorage = CosmereTags.Blocks.METAL_BLOCK_TAGS.get(Metals.MetalType.ALUMINUM);
		TagKey<Block> aluminumSheet = BlockTags.create(new ResourceLocation("sheetmetals/aluminum"));
		TagKey<Block> aluminumWire = BlockTags.create(new ResourceLocation("wires/aluminum"));

		if (s_whiteList == null)
		{
			createWhitelist(cap.getLiving());
		}

		//Pushes/Pulls on Nearby Metals
		if (getKeyBinding().isDown())
		{
			final Minecraft mc = Minecraft.getInstance();
			Player player = mc.player;
			Level level = mc.level;

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
				{
					break;
				}

				// if block isn't air, add material resistance value
				if (!blockAtPos.isAir())
				{
					Block currBlock = level.getBlockState(new BlockPos(currPos)).getBlock();

					if (blockAtPos.is(aluminumOre) || blockAtPos.is(aluminumStorage) || blockAtPos.is(aluminumSheet) || blockAtPos.is(aluminumWire) || (currBlock instanceof IHasMetalType iHasMetalType && iHasMetalType.getMetalType() == Metals.MetalType.DURALUMIN))
					{
						// aluminum completely blocks steelsight
						resistance += 1.0F;
					}
					else
					{
						resistance += materialResistanceMap.getOrDefault(blockAtPos.getMaterial(), 0.0D);
					}
				}

				// lerp to next position (1 block forward)
				currPos = currPos.add(lerpAngle);
			}

			if (resistance < 1.0F && !hitEntity)
			{
				BlockPos pos = new BlockPos(currPos);
				//todo check block is of ihasmetal type
				BlockState state = mc.level.getBlockState(pos);
				Block block = state.getBlock();
				final boolean validMetalBlock = block instanceof IHasMetalType iHasMetalType && iHasMetalType.getMetalType() != Metals.MetalType.ALUMINUM;
				if (validMetalBlock || containsMetal(state.getBlock()))
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

	public static boolean entityContainsMetal(Entity entity)
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

	public static boolean containsMetal(ItemStack itemStack)
	{
		return containsMetal(itemStack.getItem());
	}

	public static boolean containsMetal(Item item)
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


	public static boolean containsMetal(Block block)
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

	public static boolean containsMetal(Entity entity)
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
	private static void createWhitelist(Entity entity)
	{
		if (s_whiteList != null)
		{
			return;
		}

		//generate blacklist
		//reset each time, because it's not working properly for some reason
		{
			// would have used Items here, but it's ridiculously hard to get item IDs for blocks for no reason
			s_blackList = new HashSet<>();
			s_blackList.add(ResourceLocationHelper.get(Blocks.AIR).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.WATER).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.LAVA).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.GRASS_BLOCK).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.FARMLAND).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.GLASS).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.GLASS_PANE).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.BLACK_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.BROWN_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.BLUE_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.RED_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.CYAN_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.GRAY_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.GREEN_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.LIGHT_BLUE_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.LIGHT_GRAY_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.LIME_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.MAGENTA_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.ORANGE_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.PINK_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.PURPLE_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.WHITE_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.YELLOW_BED).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.TORCH).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.SHULKER_BOX).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.ICE).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.BLUE_ICE).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.FROSTED_ICE).getPath());
			s_blackList.add(ResourceLocationHelper.get(Blocks.PACKED_ICE).getPath());
			s_blackList.add(ResourceLocationHelper.get(Items.DIAMOND).getPath());
			s_blackList.add(ResourceLocationHelper.get(Items.EMERALD).getPath());
			s_blackList.add(ResourceLocationHelper.get(Items.AIR).getPath());
			s_blackList.add("philosophers_stone");//problem child
		}
		for (var itemInList : s_blackList)
		{
			CosmereAPI.logger.info("[MetalWhitelist] %s has been blacklisted from containing metal".formatted(itemInList));
		}

		s_whiteList = new HashSet<>();

		final TagKey<Item> containsMetal = CosmereTags.Items.CONTAINS_METAL;
		final RecipeManager recipeManager = entity.level.getRecipeManager();
		final Collection<Recipe<?>> recipes = recipeManager.getRecipes();

		for (var recipe : recipes)
		{
			final ItemStack resultItem = recipe.getResultItem();

			// check if is blacklisted, and if is, skip
			//if it says result item is never null, ignore it,
			//we have one confirmed bug report that it _can_ be null
			// https://github.com/leafreynolds/cosmere/issues/58
			if (resultItem == null || resultItem.isEmpty() || s_blackList.contains(ResourceLocationHelper.get(resultItem.getItem()).getPath()))
			{
				continue;
			}

			if (resultItem.is(containsMetal))
			{
				continue;
			}

			CheckRecipeForMetal(containsMetal, recipe, resultItem);
		}
	}


	public static void CheckRecipeForMetal(TagKey<Item> containsMetal, Recipe<?> recipe, ItemStack resultItemStack)
	{
		for (Ingredient ingredient : recipe.getIngredients())
		{
			for (ItemStack ingredientStack : ingredient.getItems())
			{
				final Item ingredientItem = ingredientStack.getItem();
				final ResourceLocation ingredientItemRL = ResourceLocationHelper.get(ingredientItem);
				if (s_blackList.contains(ingredientItemRL.getPath()))
				{
					continue;
				}

				if (ingredientStack.is(containsMetal))
				{
					//found one
					final Item resultItem = resultItemStack.getItem();
					final ResourceLocation resultItemRL = ResourceLocationHelper.get(resultItem);
					// final Holder.Reference<Item> itemReference = resultItem.builtInRegistryHolder();
					// List<TagKey<Item>> allTags = itemReference.tags().collect(Collectors.toList());
					// allTags.add(CosmereTags.Items.CONTAINS_METAL);
					// itemReference.bindTags(allTags);

					CosmereAPI.logger.info("[MetalWhitelist] %s has been identified as containing metal, thanks to %s in recipe"
							.formatted(resultItemRL, ingredientItemRL));

					s_whiteList.add(resultItemRL.getPath());
					return;
				}
			}
		}
	}
}


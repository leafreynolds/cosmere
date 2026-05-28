/*
 * File updated ~ 11 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.allomancy.common.items;

import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.allomancy.common.utils.MiscHelper;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.helpers.PlayerHelper;
import leaf.cosmere.api.text.TextHelper;
import leaf.cosmere.common.items.BaseItem;
import leaf.cosmere.common.items.GodMetalNuggetItem;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static leaf.cosmere.api.Constants.Strings.CONTAINED_METALS;

public class MetalVialItem extends BaseItem implements IHasMetalType
{
	private static final String metal_ids = "metalIDs";
	private static final String metal_amounts = "metalAmounts";
	private static final int MAX_METALS_COUNT = 16;


	public void addFilled(CreativeModeTab.Output output)
	{
		final ItemStack filled = new ItemStack(this);
		for (int i = 0; i < 16; i++)
		{
			addMetals(filled, i, 1);
		}
		output.accept(filled);
	}

	private static CompoundTag getContainedMetalsTag(ItemStack stack)
	{
		CompoundTag customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		if (!customData.contains("metals_contained"))
		{
			customData.put("metals_contained", new CompoundTag());
		}
		return customData.getCompound("metals_contained");
	}

	public static boolean isFull(ItemStack stack)
	{
		return containedMetalCount(stack) >= getMaxFillCount(stack);
	}

	public static int getMaxFillCount(ItemStack stack)
	{
		final CompoundTag stackTags = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		final String max_count = "max_count";
		return stackTags.contains(max_count) ? stackTags.getInt(max_count) : MAX_METALS_COUNT;
	}

	@Nonnull
	@Override
	public UseAnim getUseAnimation(ItemStack stack)
	{
		return UseAnim.DRINK;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity)
	{
		//same drink time as normal potions
		return 16;
	}


	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (player.canEat(true) && containedMetalCount(player.getItemInHand(hand)) > 0)
		{
			player.startUsingItem(hand);
			return InteractionResultHolder.consume(stack);
		}
		return InteractionResultHolder.pass(stack);
	}


	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving)
	{
		if (worldIn.isClientSide)
		{
			return stack;
		}

		Player playerentity = entityLiving instanceof Player ? (Player) entityLiving : null;

		//for each metal in the vial
		Map<Integer, Integer> metalsInVial = getStoredMetalsMap(getContainedMetalsTag(stack));

		metalsInVial.forEach((metalId, amount) ->
		{
			ItemStack newStack;
			if(Metals.MetalType.valueOf(metalId).isPresent())
			{
				Metals.MetalType metalType = Metals.MetalType.valueOf(metalId).get();
				if(metalType.isGodMetal())
				{
					newStack = new ItemStack(ItemsRegistry.GOD_METAL_NUGGETS.get(metalType).asItem(), amount);
					GodMetalNuggetItem item = (GodMetalNuggetItem) newStack.getItem();
					item.writeMetalAlloySizeNbtData(newStack, item.getMaxSize());
				}
				else
				{
					switch(metalType)
					{
						case IRON:
							newStack = new ItemStack(Items.IRON_NUGGET, amount);
							break;
						case GOLD:
							newStack = new ItemStack(Items.GOLD_NUGGET, amount);
							break;
						default:
							newStack = new ItemStack(ItemsRegistry.METAL_NUGGETS.get(metalType).asItem(), amount);
							break;
					}

				}
				MiscHelper.consumeNugget(entityLiving, newStack, amount);
			}
		});


		if (playerentity == null || !playerentity.getAbilities().instabuild)
		{
			if (stack.isEmpty())
			{
				return new ItemStack(AllomancyItems.METAL_VIAL.get());
			}

			if (playerentity != null)
			{
				if (stack.getCount() > 1)
				{
					//split 1 off the stack, if more than one
					//drain that new stack
					ItemStack splitStack = stack.split(1);
					emptyMetals(splitStack);

					PlayerHelper.addItem(playerentity, splitStack);
				}
				else
				{
					emptyMetals(stack);
				}
			}
		}

		return stack;
	}

	public static int containedMetalCount(ItemStack stack)
	{
		int count = 0;
		int[] metalAmounts = CompoundNBTHelper.getIntArray(getContainedMetalsTag(stack), metal_amounts);

		for (int metalCount : metalAmounts)
			count += metalCount;

		return count;
	}

	public static void addMetals(ItemStack stack, int metalID, int count)
	{
		if (!(stack.getItem() instanceof MetalVialItem))
		{
			return;
		}

		//todo refactor this? seems so convoluted compared to what I'm used to

		//get and add
		CompoundTag customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		if (!customData.contains("metals_contained"))
		{
			customData.put("metals_contained", new CompoundTag());
		}
		CompoundTag nbt = getContainedMetalsTag(stack);

		Map<Integer, Integer> sorted = getStoredMetalsMap(nbt);

		if (sorted.containsKey(metalID))
		{
			count += sorted.get(metalID);
		}

		sorted.put(metalID, count);
		List<Integer> keys = new ArrayList<>(sorted.keySet());
		List<Integer> values = new ArrayList<>(sorted.values());

		CompoundNBTHelper.setIntArray(nbt, metal_ids, keys);
		CompoundNBTHelper.setIntArray(nbt, metal_amounts, values);
		customData.put("metals_contained", nbt);
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(customData));
	}

	private static Map<Integer, Integer> getStoredMetalsMap(CompoundTag nbt)
	{
		int[] metalIDs = CompoundNBTHelper.getIntArray(nbt, metal_ids);
		int[] metalAmounts = CompoundNBTHelper.getIntArray(nbt, metal_amounts);

		Map<Integer, Integer> sorted = IntStream.range(0, metalIDs.length).boxed()
				.collect(Collectors.toMap(
						i -> metalIDs[i], i -> metalAmounts[i],
						(i, j) -> i, LinkedHashMap::new));
		return sorted;
	}

	public void emptyMetals(ItemStack stack)
	{
		CompoundTag customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		if (customData.contains("metals_contained"))
		{
			CompoundTag nbt = customData.getCompound("metals_contained");
			nbt.remove(metal_ids);
			nbt.remove(metal_amounts);
			customData.put("metals_contained", nbt);
			stack.set(DataComponents.CUSTOM_DATA, CustomData.of(customData));
		}
	}

	@Override
	public boolean isBarVisible(ItemStack stack)
	{
		return true;
	}

	@Override
	public int getBarWidth(ItemStack stack)
	{
		return getBarWidth(containedMetalCount(stack), MAX_METALS_COUNT);
	}

	@Override
	public int getBarColor(ItemStack stack)
	{
		return getBarColour(containedMetalCount(stack), MAX_METALS_COUNT);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn)
	{
		Map<Integer, Integer> sorted = getStoredMetalsMap(getContainedMetalsTag(stack));
		tooltip.add(TextHelper.createTranslatedText(CONTAINED_METALS));
		sorted.entrySet().stream().forEach(metalInfo ->
		{
			// MetalName x Value
			String metalName = Metals.MetalType.valueOf(metalInfo.getKey()).get().getName();

			String metalTranslation = String.format("item.cosmere.%s_nugget", metalName);
			tooltip.add(TextHelper.createTranslatedText(metalTranslation).append(TextHelper.createText(String.format(": x%s", metalInfo.getValue()))));

		});

		tooltip.add(TextHelper.createText(String.format("%s / %s", containedMetalCount(stack), MAX_METALS_COUNT)));

	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return Metals.MetalType.IRON;
	}

	public static void addMetals(ItemStack newMetalVialStack, ItemStack oldMetalVialStack)
	{
		Map<Integer, Integer> sorted = getStoredMetalsMap(getContainedMetalsTag(oldMetalVialStack));
		for (Integer metalID : sorted.keySet())
		{
			addMetals(newMetalVialStack, metalID, sorted.get(metalID));
		}
	}
}

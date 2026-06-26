package leaf.cosmere.surgebinding.common.recipes;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardplateData;
import leaf.cosmere.surgebinding.common.capabilities.IRadiantShardData;
import leaf.cosmere.surgebinding.common.capabilities.RadiantShardData;
import leaf.cosmere.surgebinding.common.items.GemstoneItem;
import leaf.cosmere.surgebinding.common.items.ShardplateCurioItem;
import leaf.cosmere.surgebinding.common.registries.SurgebindingRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ShardplateChargingRecipe extends CustomRecipe
{
	private static final Ingredient SHARDPLATE = Ingredient.of(CosmereTags.Items.CURIO_SHARDPLATE);

	public ShardplateChargingRecipe(ResourceLocation loc, CraftingBookCategory pCategory)
	{
		super(loc, pCategory);
	}

	@Override
	public boolean matches(CraftingContainer inv, @Nonnull Level world)
	{
		boolean hasGem = false;
		ItemStack shardplate = null;
		int plateCharge = 0;
		int totalStormlight = 0;
		int gems = 0;
		ShardplateCurioItem shardplateCurioItem = null;

		for (int i = 0; i < inv.getContainerSize(); i++)
		{
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty())
			{
				continue;
			}

			if (SHARDPLATE.test(stack))
			{
				//only one allowed
				if (shardplate != null)
				{
					return false;
				}

				shardplate = stack;
				shardplateCurioItem = (ShardplateCurioItem) shardplate.getItem();
				//if is vial and not bottle, check it for contained metals.
				plateCharge = shardplateCurioItem.getCharge(stack);
			}
			else if (stack.getItem() instanceof GemstoneItem gemstoneItem)
			{
				if (stack.getCount() == 1)
				{
					//but multiple nuggets allowed
					hasGem = true;
					gems++;
					totalStormlight += gemstoneItem.getCharge(stack);
				}
				else
				{
					return false;
				}
			}
			else if (stack.isEmpty())
			{
				//ignore empty slots
			}
			else
			{
				//if its not a vial, bottle, or nugget, then its not a valid recipe
				return false;
			}
		}

		if (shardplate == null)
		{
			//no shardplate, no service
			return false;
		}
		//Returns true if there is a gem AND if there is space to charge
		return hasGem && shardplateCurioItem.getMaxCharge(shardplate) > plateCharge;
	}


	@Override
	public ItemStack assemble(CraftingContainer inv, RegistryAccess pRegistryAccess)
	{
		//Determine what kind of plate it is
		ShardplateCurioItem shardplateItem = null;
		CompoundTag dataTag = null;
		for (ItemStack item : inv.getItems())
		{
			if (item.is(CosmereTags.Items.CURIO_SHARDPLATE))
			{
				shardplateItem = (ShardplateCurioItem) item.getItem();
				IRadiantShardData cap = item.getCapability(RadiantShardData.RADIANT_SHARD_DATA).orElseGet(() -> new DynamicShardplateData(item));
				dataTag = cap.serializeNBT();
			}
		}

		ItemStack itemstack = new ItemStack(shardplateItem);
		itemstack.getCapability(RadiantShardData.RADIANT_SHARD_DATA).orElseGet(() -> new DynamicShardplateData(itemstack)).deserializeNBT(dataTag);

		for (int i = 0; i < inv.getContainerSize(); ++i)
		{
			ItemStack stackInSlot = inv.getItem(i);
			if (stackInSlot.isEmpty())
			{
				continue;
			}

			if (stackInSlot.getItem() instanceof GemstoneItem gemstoneItem)
			{
				shardplateItem.adjustCharge(itemstack,
						Math.min(gemstoneItem.getCharge(stackInSlot),
								shardplateItem.getMaxCharge(itemstack) - shardplateItem.getCharge(itemstack)));
				break;
			}
			else if (stackInSlot.is(shardplateItem))
			{
				shardplateItem.adjustCharge(itemstack, shardplateItem.getCharge(stackInSlot));
			}
		}

		return itemstack;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv)
	{
		NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

		List<ItemStack> gemstones = new ArrayList<>();
		List<Integer> gemstoneIndices = new ArrayList<>();
		List<Integer> gemstoneCharges = new ArrayList<>();

		ItemStack armor = ItemStack.EMPTY;
		int missing = 0;

		// Step 1: Identify armor and gemstones
		for (int i = 0; i < inv.getContainerSize(); i++)
		{
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty())
			{
				continue;
			}

			if (stack.is(CosmereTags.Items.CURIO_SHARDPLATE))
			{
				armor = stack;
				ShardplateCurioItem sp = (ShardplateCurioItem) stack.getItem();
				missing = sp.getMaxCharge(stack) - sp.getCharge(stack);
			}
			if (stack.getItem() instanceof GemstoneItem gemstoneItem)
			{
				gemstones.add(stack.copy());
				gemstoneIndices.add(i);
				gemstoneCharges.add(gemstoneItem.getCharge(stack));
			}
		}

		if (armor.isEmpty() || gemstones.isEmpty() || missing <= 0)
		{
			return remaining;
		}

		int totalAvailable = gemstoneCharges.stream().mapToInt(Integer::intValue).sum();
		int toTransfer = Math.min(missing, totalAvailable);

		// Step 2: Weighted drain
		int transferred = 0;
		for (int j = 0; j < gemstones.size(); j++)
		{
			int available = gemstoneCharges.get(j);
			double ratio = (double) available / totalAvailable;
			int amountToDrain = (int) Math.floor(toTransfer * ratio);

			// If last donor, absorb remainder
			if (j == gemstones.size() - 1)
			{
				amountToDrain = toTransfer - transferred;
			}

			int newCharge = Math.max(0, available - amountToDrain);
			GemstoneItem gi = (GemstoneItem) gemstones.get(j).getItem();
			gi.setCharge(gemstones.get(j), newCharge);
			remaining.set(gemstoneIndices.get(j), gemstones.get(j));

			transferred += amountToDrain;
		}

		return remaining;
	}


	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		//if you can fit 2 items, the bottle and a metal, you can combine
		return width * height > 1;
	}

	@Override
	public @Nonnull ResourceLocation getId()
	{
		return Surgebinding.rl("plate_charging");
	}

	@Override
	public @Nonnull RecipeSerializer<?> getSerializer()
	{
		return SurgebindingRecipes.PLATE_CHARGE.get();
	}
}

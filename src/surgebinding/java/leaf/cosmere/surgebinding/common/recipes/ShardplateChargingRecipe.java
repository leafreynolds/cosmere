/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * 1.21.1 changes:
 *  - CraftingContainer → CraftingInput (1.21.1 recipe input wrapper)
 *  - CustomRecipe ctor drops the ResourceLocation id (recipes are keyed by their map entry)
 *  - assemble(CraftingContainer, RegistryAccess) → assemble(CraftingInput, HolderLookup.Provider)
 *  - getRemainingItems(CraftingContainer) → getRemainingItems(CraftingInput)
 *  - getId() override removed
 *  - DynamicShardplateData binds directly to a stack now (no more `stack.getCapability(...)`),
 *    serialized state is implicit in DataComponents.CUSTOM_DATA. The dance of read-then-write
 *    becomes: read source data, copy CUSTOM_DATA across to the output stack.
 *  - Tags.Items.GEMS removed; iterate the per-gemstone tags from CosmereTags.
 */

package leaf.cosmere.surgebinding.common.recipes;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.items.GemstoneItem;
import leaf.cosmere.surgebinding.common.items.ShardplateCurioItem;
import leaf.cosmere.surgebinding.common.registries.SurgebindingRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShardplateChargingRecipe extends CustomRecipe
{
	private static final Ingredient SHARDPLATE = Ingredient.of(CosmereTags.Items.CURIO_SHARDPLATE);

	public ShardplateChargingRecipe(CraftingBookCategory pCategory)
	{
		super(pCategory);
	}

	@Override
	public boolean matches(CraftingInput inv, @Nonnull Level world)
	{
		boolean hasGem = false;
		ItemStack shardplate = null;
		int plateCharge = 0;
		ShardplateCurioItem shardplateCurioItem = null;

		for (int i = 0; i < inv.size(); i++)
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
				plateCharge = shardplateCurioItem.getCharge(stack);
			}
			else if (testForGem(stack).isPresent())
			{
				if (stack.getCount() == 1)
				{
					//but multiple gems allowed
					hasGem = true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				//if it's not a shardplate or gemstone, then it's not a valid recipe
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

	private Optional<TagKey<Item>> testForGem(ItemStack stack)
	{
		for (Roshar.Gemstone value : EnumUtils.GEMSTONE_TYPES)
		{
			TagKey<Item> gemstoneTag = CosmereTags.Items.GEM_TAGS.get(value);
			if (stack.is(gemstoneTag))
			{
				return Optional.of(gemstoneTag);
			}
		}

		return Optional.empty();
	}

	private boolean isAnyGemstone(ItemStack stack)
	{
		return testForGem(stack).isPresent();
	}


	@Override
	public ItemStack assemble(CraftingInput inv, HolderLookup.Provider pRegistries)
	{
		//Determine what kind of plate it is
		ShardplateCurioItem shardplateItem = null;
		ItemStack source = ItemStack.EMPTY;
		for (int i = 0; i < inv.size(); i++)
		{
			ItemStack item = inv.getItem(i);
			if (item.is(CosmereTags.Items.CURIO_SHARDPLATE))
			{
				shardplateItem = (ShardplateCurioItem) item.getItem();
				source = item;
				break;
			}
		}

		if (shardplateItem == null)
		{
			return ItemStack.EMPTY;
		}

		ItemStack itemstack = new ItemStack(shardplateItem);
		// Copy DataComponents.CUSTOM_DATA (where DynamicShardplateData persists) from source.
		CustomData sourceData = source.get(DataComponents.CUSTOM_DATA);
		if (sourceData != null)
		{
			itemstack.set(DataComponents.CUSTOM_DATA, sourceData);
		}

		for (int i = 0; i < inv.size(); ++i)
		{
			ItemStack stackInSlot = inv.getItem(i);
			if (stackInSlot.isEmpty())
			{
				continue;
			}

			if (isAnyGemstone(stackInSlot))
			{
				if (stackInSlot.getItem() instanceof GemstoneItem gemstoneItem)
				{
					shardplateItem.adjustCharge(itemstack,
							Math.min(gemstoneItem.getCharge(stackInSlot),
									shardplateItem.getMaxCharge(itemstack) - shardplateItem.getCharge(itemstack)));
				}
			}
			else if (stackInSlot.is(shardplateItem))
			{
				shardplateItem.adjustCharge(itemstack, shardplateItem.getCharge(stackInSlot));
			}
		}

		return itemstack;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput inv)
	{
		NonNullList<ItemStack> remaining = NonNullList.withSize(inv.size(), ItemStack.EMPTY);

		List<ItemStack> gemstones = new ArrayList<>();
		List<Integer> gemstoneIndices = new ArrayList<>();
		List<Integer> gemstoneCharges = new ArrayList<>();

		ItemStack armor = ItemStack.EMPTY;
		int missing = 0;

		// Step 1: Identify armor and gemstones
		for (int i = 0; i < inv.size(); i++)
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
			else if (isAnyGemstone(stack))
			{
				gemstones.add(stack.copy());
				gemstoneIndices.add(i);
				if (stack.getItem() instanceof GemstoneItem gi)
				{
					gemstoneCharges.add(gi.getCharge(stack));
				}
				else
				{
					gemstoneCharges.add(0);
				}
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
			double ratio = totalAvailable == 0 ? 0 : (double) available / totalAvailable;
			int amountToDrain = (int) Math.floor(toTransfer * ratio);

			// If last donor, absorb remainder
			if (j == gemstones.size() - 1)
			{
				amountToDrain = toTransfer - transferred;
			}

			int newCharge = Math.max(0, available - amountToDrain);
			if (gemstones.get(j).getItem() instanceof GemstoneItem gi)
			{
				gi.setCharge(gemstones.get(j), newCharge);
			}
			remaining.set(gemstoneIndices.get(j), gemstones.get(j));

			transferred += amountToDrain;
		}

		return remaining;
	}


	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		//if you can fit 2 items, the plate and a gem, you can combine
		return width * height > 1;
	}

	@Override
	public @Nonnull RecipeSerializer<?> getSerializer()
	{
		return SurgebindingRecipes.PLATE_CHARGE.get();
	}
}

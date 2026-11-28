package leaf.cosmere.common.recipes;

import leaf.cosmere.api.IHasSize;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.common.items.GodMetalAlloyNuggetItem;
import leaf.cosmere.common.items.GodMetalNuggetItem;
import leaf.cosmere.common.registry.CosmereRecipesRegistry;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class GodMetalNuggetsDecompress extends CustomRecipe
{

	Ingredient INGREDIENT_GOD_METAL_ALLOY_NUG = Ingredient.of(ItemsRegistry.GOD_METAL_ALLOY_NUGGETS.values().stream()
			.flatMap(inner -> inner.values().stream())
			.map(IItemProvider::getItemStack));

	Ingredient INGREDIENT_GOD_METAL_NUG = Ingredient.of(ItemsRegistry.GOD_METAL_NUGGETS.values().stream()
			.map(IItemProvider::getItemStack));

	public GodMetalNuggetsDecompress(CraftingBookCategory pCategory)
	{
		super(pCategory);
	}

	@Override
	public boolean matches(CraftingInput inv, @Nonnull Level world)
	{
		int itemCount = 0;
		IHasSize item = null;
		for (int i = 0; i < inv.size(); i++)
		{
			ItemStack itemStack = inv.getItem(i);
			if (itemStack.isEmpty())
			{
				continue;
			}

			if (INGREDIENT_GOD_METAL_ALLOY_NUG.test(inv.getItem(i)) || INGREDIENT_GOD_METAL_NUG.test(inv.getItem(i)))
			{
				itemCount++;
				if (itemCount > 1)
				{
					return false;
				}

				item = (IHasSize) itemStack.getItem();
				int currentSize = item.readMetalAlloySizeNbtData(itemStack);
				if (currentSize == item.getMinSize())
				{
					return false; // No splitting smallest size
				}
				if (currentSize % 2 == 1)
				{
					return false; // No odd splitting
				}
			}
			else
			{
				return false;
			}
		}
		if (itemCount != 1)
		{
			return false;
		}

		return true;
	}

	@Override
	public ItemStack assemble(CraftingInput inv, HolderLookup.Provider pRegistries)
	{
		Item item = null;
		int index = 0;
		for (int i = 0; i < inv.size(); i++)
		{
			if (!inv.getItem(i).isEmpty())
			{
				item = inv.getItem(i).getItem();
				index = i;
				break;
			}
		}
		ItemStack itemStack = ItemStack.EMPTY;

		if (INGREDIENT_GOD_METAL_ALLOY_NUG.test(inv.getItem(index)))
		{
			GodMetalAlloyNuggetItem gItem = (GodMetalAlloyNuggetItem) item;
			int currentSize = gItem.readMetalAlloySizeNbtData(inv.getItem(index));
			itemStack = new ItemStack(ItemsRegistry.GOD_METAL_ALLOY_NUGGETS.get(gItem.getMetalType()).get(gItem.getAlloyedMetalType()));
			gItem.writeMetalAlloySizeNbtData(itemStack, currentSize / 2);
			itemStack.setCount(2);
		}
		else if (INGREDIENT_GOD_METAL_NUG.test(inv.getItem(index)))
		{
			GodMetalNuggetItem gItem = (GodMetalNuggetItem) item;
			int currentSize = gItem.readMetalAlloySizeNbtData(inv.getItem(index));
			itemStack = new ItemStack(ItemsRegistry.GOD_METAL_NUGGETS.get(gItem.getMetalType()));
			gItem.writeMetalAlloySizeNbtData(itemStack, currentSize / 2);
			itemStack.setCount(2);
		}

		return itemStack;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		// We need a 3x3 grid
		return width * height == 9;
	}

	@Override
	public @Nonnull RecipeSerializer<?> getSerializer()
	{
		return CosmereRecipesRegistry.GOD_METAL_NUGGETS_DECOMPRESS.get();
	}

	@Override
	public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput pInput)
	{
		return NonNullList.withSize(pInput.size(), ItemStack.EMPTY);
	}

}

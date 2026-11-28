package leaf.cosmere.common.recipes;

import leaf.cosmere.api.IHasSize;
import leaf.cosmere.api.Metals.MetalType;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.common.items.GodMetalAlloyNuggetItem;
import leaf.cosmere.common.items.GodMetalNuggetItem;
import leaf.cosmere.common.registry.CosmereRecipesRegistry;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class GodMetalNuggetsCompress extends CustomRecipe
{

	Ingredient INGREDIENT_GOD_METAL_ALLOY_NUG = Ingredient.of(ItemsRegistry.GOD_METAL_ALLOY_NUGGETS.values().stream()
			.flatMap(inner -> inner.values().stream())
			.map(IItemProvider::getItemStack));

	Ingredient INGREDIENT_GOD_METAL_NUG = Ingredient.of(ItemsRegistry.GOD_METAL_NUGGETS.values().stream()
			.map(IItemProvider::getItemStack));

	public GodMetalNuggetsCompress(CraftingBookCategory pCategory)
	{
		super(pCategory);
	}

	@Override
	public boolean matches(CraftingInput inv, @Nonnull Level world)
	{
		MetalType godMetalType = null;
		MetalType alloyedMetalType = null;
		int totalSize = 0;
		int numItems = 0;
		boolean isAlloy = false;
		boolean isGod = false;
		for (int i = 0; i < inv.size(); i++)
		{
			ItemStack itemStack = inv.getItem(i);
			if (itemStack.isEmpty())
			{
				continue;
			}
			if (INGREDIENT_GOD_METAL_ALLOY_NUG.test(inv.getItem(i)))
			{
				isAlloy = true;
				numItems++;
				GodMetalAlloyNuggetItem item = (GodMetalAlloyNuggetItem) itemStack.getItem();

				if (godMetalType == null)
				{
					godMetalType = item.getMetalType();
				}
				if (godMetalType != item.getMetalType())
				{
					return false;
				}

				if (alloyedMetalType == null)
				{
					alloyedMetalType = item.getAlloyedMetalType();
				}
				if (alloyedMetalType != item.getAlloyedMetalType())
				{
					return false;
				}

				totalSize += item.readMetalAlloySizeNbtData(itemStack);
				if (totalSize > item.getMaxSize())
				{
					return false;
				}
			}
			else if (INGREDIENT_GOD_METAL_NUG.test(inv.getItem(i)))
			{
				isGod = true;
				numItems++;
				GodMetalNuggetItem item = (GodMetalNuggetItem) itemStack.getItem();

				if (godMetalType == null)
				{
					godMetalType = item.getMetalType();
				}
				if (godMetalType != item.getMetalType())
				{
					return false;
				}

				totalSize += item.readMetalAlloySizeNbtData(itemStack);
				if (totalSize > item.getMaxSize())
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}

		if (numItems < 2)
		{
			return false;
		}
		if (isGod && isAlloy)
		{
			return false;
		}

		return true;
	}

	@Override
	public ItemStack assemble(CraftingInput inv, HolderLookup.Provider pRegistries)
	{
		int totalSize = 0;
		MetalType godMetalType = null;
		MetalType alloyedMetalType = null;

		for (int i = 0; i < inv.size(); i++)
		{
			if (!inv.getItem(i).isEmpty())
			{
				ItemStack curItemStack = inv.getItem(i);
				if (INGREDIENT_GOD_METAL_ALLOY_NUG.test(inv.getItem(i)))
				{
					GodMetalAlloyNuggetItem curItem = (GodMetalAlloyNuggetItem) curItemStack.getItem();
					godMetalType = curItem.getMetalType();
					alloyedMetalType = curItem.getAlloyedMetalType();
					totalSize += curItem.readMetalAlloySizeNbtData(curItemStack);
				}
				else if (INGREDIENT_GOD_METAL_NUG.test(inv.getItem(i)))
				{
					GodMetalNuggetItem curItem = (GodMetalNuggetItem) curItemStack.getItem();
					godMetalType = curItem.getMetalType();
					totalSize += curItem.readMetalAlloySizeNbtData(curItemStack);
				}
			}
		}

		ItemStack itemStack;
		if (alloyedMetalType != null)
		{
			itemStack = new ItemStack(ItemsRegistry.GOD_METAL_ALLOY_NUGGETS.get(godMetalType).get(alloyedMetalType));
		}
		else
		{
			itemStack = new ItemStack(ItemsRegistry.GOD_METAL_NUGGETS.get(godMetalType));
		}

		IHasSize item = (IHasSize) itemStack.getItem();

		item.writeMetalAlloySizeNbtData(itemStack, totalSize);
		itemStack.setCount(1);

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
		return CosmereRecipesRegistry.GOD_METAL_NUGGETS_COMPRESS.get();
	}

	@Override
	public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput pInput)
	{
		return NonNullList.withSize(pInput.size(), ItemStack.EMPTY);
	}

}

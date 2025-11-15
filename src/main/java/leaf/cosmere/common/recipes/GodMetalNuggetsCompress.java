package leaf.cosmere.common.recipes;

import leaf.cosmere.api.IHasSize;
import leaf.cosmere.api.Metals.MetalType;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.items.GodMetalAlloyNuggetItem;
import leaf.cosmere.common.items.GodMetalNuggetItem;
import leaf.cosmere.common.registry.CosmereRecipesRegistry;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
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

	public GodMetalNuggetsCompress(ResourceLocation loc, CraftingBookCategory pCategory)
	{
		super(loc, pCategory);
	}

	@Override
	public boolean matches(CraftingContainer inv, @Nonnull Level world)
	{
		MetalType godMetalType = null;
		MetalType alloyedMetalType = null;
		int totalSize = 0;
		int numItems = 0;
		boolean isAlloy = false;
		boolean isGod = false;
		for(int i = 0; i < inv.getContainerSize(); i++)
		{
			ItemStack itemStack = inv.getItem(i);
			if(itemStack.isEmpty()) continue;
			if(INGREDIENT_GOD_METAL_ALLOY_NUG.test(inv.getItem(i)))
			{
				isAlloy = true;
				numItems++;
				GodMetalAlloyNuggetItem item = (GodMetalAlloyNuggetItem) itemStack.getItem();

				if(godMetalType == null) godMetalType = item.getMetalType();
				if(godMetalType != item.getMetalType()) return false;

				if(alloyedMetalType == null) alloyedMetalType = item.getAlloyedMetalType();
				if(alloyedMetalType != item.getAlloyedMetalType()) return false;

				totalSize += item.readMetalAlloySizeNbtData(itemStack);
				if(totalSize > item.getMaxSize()) return false;
			}
		    else if (INGREDIENT_GOD_METAL_NUG.test(inv.getItem(i)))
			{
				isGod = true;
				numItems++;
				GodMetalNuggetItem item = (GodMetalNuggetItem) itemStack.getItem();

				if(godMetalType == null) godMetalType = item.getMetalType();
				if(godMetalType != item.getMetalType()) return false;

				totalSize += item.readMetalAlloySizeNbtData(itemStack);
				if(totalSize > item.getMaxSize()) return false;
			}
			else
			{
				return false;
			}
		}

		if(numItems < 2) return false;
		if(isGod && isAlloy) return false;

		return true;
	}

	@Override
	public ItemStack assemble(CraftingContainer inv, RegistryAccess pRegistryAccess)
	{
		int totalSize = 0;
		MetalType godMetalType = null;
		MetalType alloyedMetalType = null;

		for(int i = 0; i < inv.getContainerSize(); i++)
		{
			if(!inv.getItem(i).isEmpty())
			{
				ItemStack curItemStack = inv.getItem(i);
				if(INGREDIENT_GOD_METAL_ALLOY_NUG.test(inv.getItem(i)))
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
		if(alloyedMetalType != null)
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
	public @Nonnull ResourceLocation getId()
	{
		return new ResourceLocation(Cosmere.MODID, "god_metal_alloy_nugget_compress");
	}

	@Override
	public @Nonnull RecipeSerializer<?> getSerializer()
	{
		return CosmereRecipesRegistry.GOD_METAL_ALLOY_NUGGET_COMPRESS.get();
	}

	public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer pContainer) {
		return NonNullList.withSize(pContainer.getContainerSize(), ItemStack.EMPTY);
	}

}

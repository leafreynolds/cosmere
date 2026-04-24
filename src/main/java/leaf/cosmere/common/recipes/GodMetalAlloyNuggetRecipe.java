package leaf.cosmere.common.recipes;

import leaf.cosmere.api.IHasSize;
import leaf.cosmere.api.Metals.MetalType;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.common.items.GodMetalAlloyNuggetItem;
import leaf.cosmere.common.items.GodMetalNuggetItem;
import leaf.cosmere.common.items.MetalNuggetItem;
import leaf.cosmere.common.registry.CosmereRecipesRegistry;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class GodMetalAlloyNuggetRecipe extends CustomRecipe
{

	public GodMetalAlloyNuggetRecipe(CraftingBookCategory pCategory)
	{
		super(pCategory);
	}

	@Override
	public boolean matches(CraftingInput inv, @Nonnull Level world)
	{
		Ingredient INGREDIENT_GOD_METAL_NUG = Ingredient.of(
				ItemsRegistry.GOD_METAL_NUGGETS.get(MetalType.LERASIUM).getItemStack(),
				ItemsRegistry.GOD_METAL_NUGGETS.get(MetalType.LERASATIUM).getItemStack()
		);

		Ingredient INGREDIENT_COSMERE_METAL_NUG = Ingredient.of(
				ItemsRegistry.METAL_NUGGETS.values().stream().map(IItemProvider::getItemStack)
		);

		Ingredient INGREDIENT_MC_METAL_NUG = Ingredient.of(
				Items.IRON_NUGGET,
				Items.GOLD_NUGGET
		);

		if(!INGREDIENT_GOD_METAL_NUG.test(inv.getItem(4))) return false;
		if(inv.getItem(4).getCount() != 1) return false;

		MetalType metalType = null;
		for(int i = 0; i < inv.size(); i++)
		{
			if(i == 4) continue;
			ItemStack itemStack = inv.getItem(i);

			if(INGREDIENT_COSMERE_METAL_NUG.test(itemStack))
			{
				MetalNuggetItem item = (MetalNuggetItem) itemStack.getItem();
				if(i == 0) metalType = item.getMetalType();
				if(metalType != item.getMetalType()) return false;
			}
			else if(INGREDIENT_MC_METAL_NUG.test(itemStack))
			{
				Item item = itemStack.getItem();
				MetalType newMetalType;
				if (item == Items.IRON_NUGGET)
				{
					newMetalType = MetalType.IRON;
				}
				else if(item == Items.GOLD_NUGGET)
				{
					newMetalType = MetalType.GOLD;
				}
				else {
					return false;
				}
				if(i == 0) metalType = newMetalType;
				if(metalType != newMetalType) return false;
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack assemble(CraftingInput inv, HolderLookup.Provider pRegistries)
	{
		GodMetalNuggetItem godMetalNuggetItem = (GodMetalNuggetItem) inv.getItem(4).getItem();
		MetalType godMetalType = godMetalNuggetItem.getMetalType();

		MetalType metalType = null;
		if(inv.getItem(0).getItem() == Items.IRON_NUGGET)
		{
			metalType = MetalType.IRON;
		}
		else if (inv.getItem(0).getItem() == Items.GOLD_NUGGET)
		{
			metalType = MetalType.GOLD;
		}
		else
		{
			MetalNuggetItem metalNuggetItem = (MetalNuggetItem) inv.getItem(0).getItem();
			metalType = metalNuggetItem.getMetalType();
		}

		ItemStack itemStack = new ItemStack(ItemsRegistry.GOD_METAL_ALLOY_NUGGETS.get(godMetalType).get(metalType));
		GodMetalAlloyNuggetItem item = (GodMetalAlloyNuggetItem) itemStack.getItem();

		item.writeMetalAlloySizeNbtData(itemStack, 2);
		itemStack.setCount(8);

		return itemStack;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		// Must have 9 items
		return width * height == 9;
	}

	@Override
	public @Nonnull RecipeSerializer<?> getSerializer()
	{
		return CosmereRecipesRegistry.GOD_METAL_ALLOY_NUGGET_RECIPE.get();
	}


	@Override
	public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput pInput) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(pInput.size(), ItemStack.EMPTY);

		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack item = pInput.getItem(i);
			if (item.hasCraftingRemainingItem()) {
				nonnulllist.set(i, getCraftingRemainingItem(item));
			}
		}

		return nonnulllist;
	}

	public ItemStack getCraftingRemainingItem(ItemStack stack) {
		ItemStack out = stack.copy();
		IHasSize item = (IHasSize) stack.getItem();
		int size = item.readMetalAlloySizeNbtData(stack);

		int newSize = size - 1;
		if (newSize < 1) return ItemStack.EMPTY;

		item.writeMetalAlloySizeNbtData(out, newSize);
		return out;
	}

}

/*
 * File created ~ 10 - 5 - 2021 ~ Leaf
 */

package leaf.cosmere.recipes;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.MetalNuggetItem;
import leaf.cosmere.items.MetalVialItem;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.RecipeRegistry;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;

//not to be confused with the vile mixing recipe,
// where you just make gross things
public class VialMixingRecipe extends CustomRecipe
{
	private static final Ingredient INGREDIENT_BOTTLE = Ingredient.of(Items.GLASS_BOTTLE, ItemsRegistry.METAL_VIAL.get());
/*    private static final Ingredient INGREDIENT_NUGGET = Ingredient.fromItems(
            ItemsRegistry.METAL_NUGGETS
                    .values()
                    .stream()
                    .map(RegistryObject::get)
                    .toArray(Item[]::new)
    );*/


	public VialMixingRecipe(ResourceLocation loc)
	{
		super(loc);
	}

	@Override
	public boolean matches(CraftingContainer inv, Level world)
	{
		boolean hasNugget = false;
		ItemStack bottle = null;
		final Ingredient INGREDIENT_NUGGETS = Ingredient.of(Tags.Items.NUGGETS);

		for (int i = 0; i < inv.getContainerSize(); i++)
		{
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty())
			{
				continue;
			}

			if (INGREDIENT_BOTTLE.test(stack))
			{
				//only one allowed
				if (bottle != null)
				{
					return false;
				}

				bottle = stack;
			}
			else if (INGREDIENT_NUGGETS.test(stack))
			{
				//but multiple nuggets allowed
				hasNugget = true;
			}
		}

		if (bottle == null)
		{
			//no bottle, no service
			return false;
		}

		//check how full the bottle is, but only if its the vial
		//minecraft bottles can be inherently empty
		if (bottle.getItem() == ItemsRegistry.METAL_VIAL.get())
		{
			MetalVialItem item = (MetalVialItem) bottle.getItem();
			return hasNugget && !item.isFull(bottle);
		}


		return hasNugget;
	}

	@Override
	@Nonnull
	public ItemStack assemble(CraftingContainer inv)
	{
		MetalVialItem metalVial = (MetalVialItem) ItemsRegistry.METAL_VIAL.get();
		final Ingredient INGREDIENT_NUGGETS = Ingredient.of(Tags.Items.NUGGETS);
		ItemStack itemstack = new ItemStack(metalVial);

		for (int i = 0; i < inv.getContainerSize(); ++i)
		{
			ItemStack stackInSlot = inv.getItem(i);
			if (stackInSlot.isEmpty())
			{
				continue;
			}

			if (INGREDIENT_NUGGETS.test(stackInSlot))
			{
				if (stackInSlot.getItem() instanceof MetalNuggetItem)
				{
					metalVial.addMetals(itemstack, ((MetalNuggetItem) stackInSlot.getItem()).getMetalType().getID(), 1);
				}
				//special vanilla logic since we don't create our own copies of iron/gold
				else if (stackInSlot.getItem() == Items.IRON_NUGGET)
				{
					metalVial.addMetals(itemstack, Metals.MetalType.IRON.getID(), 1);
				}
				else if (stackInSlot.getItem() == Items.GOLD_NUGGET)
				{
					metalVial.addMetals(itemstack, Metals.MetalType.GOLD.getID(), 1);
				}

			}
		}
		return itemstack;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		//if you can fit 2 items, the bottle and a metal, you can combine
		return width * height > 1;
	}

	@Override
	public ResourceLocation getId()
	{
		return ResourceLocationHelper.prefix("vial_mix");
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return RecipeRegistry.VIAL_RECIPE_SERIALIZER.get();
	}

}

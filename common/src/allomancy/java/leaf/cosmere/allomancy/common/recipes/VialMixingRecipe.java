/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.recipes;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.items.MetalVialItem;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.allomancy.common.registries.AllomancyRecipes;
import leaf.cosmere.api.Metals;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.Optional;

//not to be confused with the vile mixing recipe,
// where you just make gross things
public class VialMixingRecipe extends CustomRecipe
{
	private static final Ingredient INGREDIENT_BOTTLE = Ingredient.of(Items.GLASS_BOTTLE, AllomancyItems.METAL_VIAL.get());

	public VialMixingRecipe(ResourceLocation loc)
	{
		super(loc);
	}

	@Override
	public boolean matches(CraftingContainer inv, @Nonnull Level world)
	{
		boolean hasNugget = false;
		ItemStack vialStack = null;
		int bottleAmount = 0;
		int nuggetTotal = 0;
		MetalVialItem metalVialItem = AllomancyItems.METAL_VIAL.get();

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
				if (vialStack != null)
				{
					return false;
				}

				vialStack = stack;
				//if is vial and not bottle, check it for contained metals.
				if (vialStack.is(metalVialItem))
				{
					bottleAmount = MetalVialItem.containedMetalCount(vialStack);
				}
			}
			else if (testForViableNugget(stack).isPresent())
			{
				//but multiple nuggets allowed
				hasNugget = true;
				nuggetTotal++;
			}
		}

		if (vialStack == null)
		{
			//no vialStack, no service
			return false;
		}

		if (bottleAmount + nuggetTotal > MetalVialItem.getMaxFillCount(vialStack))
		{
			return false;
		}

		//check how full the vialStack is, but only if its the vial
		//minecraft bottles can be inherently empty
		if (vialStack.getItem() == AllomancyItems.METAL_VIAL.get())
		{
			return hasNugget && !MetalVialItem.isFull(vialStack);
		}


		return hasNugget;
	}

	private Optional<TagKey<Item>> testForViableNugget(ItemStack stack)
	{
		for (Metals.MetalType value : Metals.MetalType.values())
		{
			TagKey<Item> metalNuggetTag = value.getMetalNuggetTag();
			if (stack.is(metalNuggetTag))
			{
				return Optional.of(metalNuggetTag);
			}
		}

		return Optional.empty();
	}

	@Override
	@Nonnull
	public ItemStack assemble(CraftingContainer inv)
	{
		MetalVialItem metalVial = AllomancyItems.METAL_VIAL.get();
		ItemStack itemstack = new ItemStack(metalVial);

		for (int i = 0; i < inv.getContainerSize(); ++i)
		{
			ItemStack stackInSlot = inv.getItem(i);
			if (stackInSlot.isEmpty())
			{
				continue;
			}

			if (stackInSlot.is(Tags.Items.NUGGETS))
			{
				for (Metals.MetalType metalType : Metals.MetalType.values())
				{
					if (stackInSlot.is(metalType.getMetalNuggetTag()))
					{
						MetalVialItem.addMetals(itemstack, metalType.getID(), 1);
						break;
					}
				}
			}
			else if (stackInSlot.is(metalVial))
			{
				MetalVialItem.addMetals(itemstack, stackInSlot);
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
	public @Nonnull ResourceLocation getId()
	{
		return Allomancy.rl("vial_mix");
	}

	@Override
	public @Nonnull RecipeSerializer<?> getSerializer()
	{
		return AllomancyRecipes.VIAL_RECIPE_SERIALIZER.get();
	}


	public static class Serializer extends SimpleRecipeSerializer<VialMixingRecipe>
	{
		public Serializer()
		{
			super(VialMixingRecipe::new);
		}
	}
}

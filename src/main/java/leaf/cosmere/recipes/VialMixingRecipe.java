/*
 * File created ~ 10 - 5 - 2021 ~ Leaf
 */

package leaf.cosmere.recipes;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import leaf.cosmere.items.MetalNuggetItem;
import leaf.cosmere.items.MetalVialItem;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.RecipeRegistry;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;

//not to be confused with the vile mixing recipe,
// where you just make gross things
public class VialMixingRecipe extends SpecialRecipe
{
    private static final Ingredient INGREDIENT_BOTTLE = Ingredient.fromItems(Items.GLASS_BOTTLE, ItemsRegistry.METAL_VIAL.get());
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
    public boolean matches(CraftingInventory inv, World world)
    {
        boolean hasNugget = false;
        ItemStack bottle = null;
        final Ingredient INGREDIENT_NUGGETS = Ingredient.fromTag(Tags.Items.NUGGETS);

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
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
    public ItemStack getCraftingResult(CraftingInventory inv)
    {
        MetalVialItem metalVial = (MetalVialItem) ItemsRegistry.METAL_VIAL.get();
        final Ingredient INGREDIENT_NUGGETS = Ingredient.fromTag(Tags.Items.NUGGETS);
        ItemStack itemstack = new ItemStack(metalVial);

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack stackInSlot = inv.getStackInSlot(i);
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
    public boolean canFit(int width, int height)
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
    public IRecipeSerializer<?> getSerializer()
    {
        return RecipeRegistry.VIAL_RECIPE_SERIALIZER.get();
    }

}

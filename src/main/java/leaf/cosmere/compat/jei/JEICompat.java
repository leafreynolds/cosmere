/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.compat.jei;

import leaf.cosmere.registry.ItemsRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEICompat implements IModPlugin
{
    @Override
    public ResourceLocation getPluginUid()
    {
        return Constants.Resources.JEI;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg)
    {
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registry)
    {
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg)
    {
        //for (RegistryObject<Item> itemRO : ItemsRegistry.ITEMS.get())
        //reg.addRecipes(TestRecipe.getAllRecipes(world), TestRecipeCategory.NAME);

        addItemInfoPage(reg, ItemsRegistry.GUIDE.get());

        for (Metals.MetalType metalType : Metals.MetalType.values())
        {
            if (metalType.hasFeruchemicalEffect())
            {
                addItemInfoPage(reg, metalType.getNecklaceItem());
                addItemInfoPage(reg, metalType.getRingItem());
                addItemInfoPage(reg, metalType.getBraceletItem());
            }
            if (metalType.hasHemalurgicEffect())
            {
                addItemInfoPage(reg, metalType.getSpikeItem());
            }
        }
    }

    private void addItemInfoPage(IRecipeRegistration reg, Item item)
    {
        reg.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM, String.format("item.cosmere.%s.tooltip", item.getRegistryName().getPath()));
    }

}

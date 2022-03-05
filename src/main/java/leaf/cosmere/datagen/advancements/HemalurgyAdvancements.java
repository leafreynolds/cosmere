/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.advancements;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.TagsRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Consumer;

public class HemalurgyAdvancements implements Consumer<Consumer<Advancement>>
{
    public HemalurgyAdvancements()
    {
    }

    public void accept(Consumer<Advancement> advancementConsumer)
    {
        final String categoryName = "hemalurgy";
        Advancement root = Advancement.Builder.advancement()
                .display(ItemsRegistry.GUIDE.get(),
                        new TranslationTextComponent("advancements.cosmere." + categoryName + ".title"),
                        new TranslationTextComponent("advancements.cosmere." + categoryName + ".description"),
                        new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
                        FrameType.TASK,
                        false,
                        false,
                        false)
                .addCriterion("spike", InventoryChangeTrigger.Instance.hasItems(ItemPredicate.Builder.item().of(TagsRegistry.Items.METAL_SPIKE).build()))
                //.withRewards(new AdvancementRewards(0, new ResourceLocation[]{new ResourceLocation("cosmere:guide")}, new ResourceLocation[0], FunctionObject.CacheableFunction.EMPTY))
                .save(advancementConsumer, categoryName + "/root");
    }
}
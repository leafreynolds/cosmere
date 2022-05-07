/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.advancements;

import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.TagsRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

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
						new TranslatableComponent("advancements.cosmere." + categoryName + ".title"),
						new TranslatableComponent("advancements.cosmere." + categoryName + ".description"),
						new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
						FrameType.TASK,
						false,
						false,
						false)
				.addCriterion("spike", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TagsRegistry.Items.METAL_SPIKE).build()))
				//.withRewards(new AdvancementRewards(0, new ResourceLocation[]{new ResourceLocation("cosmere:guide")}, new ResourceLocation[0], FunctionObject.CacheableFunction.EMPTY))
				.save(advancementConsumer, categoryName + "/root");
	}
}
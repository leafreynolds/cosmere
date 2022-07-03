/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.advancements;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.TagsRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class HemalurgyAdvancements implements Consumer<Consumer<Advancement>>
{
	public HemalurgyAdvancements()
	{
	}

	public void accept(Consumer<Advancement> advancementConsumer)
	{
		String tabName = "hemalurgy";

		final String titleFormat = "advancements.cosmere.%s.title";
		final String descriptionFormat = "advancements.cosmere.%s.description";
		final String achievementPathFormat = "cosmere:%s/%s";

		Advancement root = Advancement.Builder.advancement()
				.display(ItemsRegistry.METAL_SPIKE.get(Metals.MetalType.IRON).get(),
						Component.translatable(String.format(titleFormat, tabName)),
						Component.translatable(String.format(descriptionFormat, tabName)),
						new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
						FrameType.TASK,
						false,//showToast
						false,//announceChat
						false)//hidden
				.addCriterion("spike", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(TagsRegistry.Items.METAL_SPIKE).build()))
				.save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));



	}
}
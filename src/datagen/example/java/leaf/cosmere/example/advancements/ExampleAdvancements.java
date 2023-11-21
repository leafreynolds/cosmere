/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.example.advancements;

import net.minecraft.advancements.Advancement;

import java.util.function.Consumer;

public class ExampleAdvancements implements Consumer<Consumer<Advancement>>
{
	public ExampleAdvancements()
	{
	}

	public void accept(Consumer<Advancement> advancementConsumer)
	{
		String tabName = "example";

		final String titleFormat = "advancements.example.%s.title";
		final String descriptionFormat = "advancements.example.%s.description";
		final String achievementPathFormat = "example:%s/%s";

		//Advancement root = Advancement.Builder.advancement()
		//		.display(ExampleItems.WORM.get(),
		//				Component.translatable(String.format(titleFormat, tabName)),
		//				Component.translatable(String.format(descriptionFormat, tabName)),
		//				new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
		//				FrameType.TASK,
		//				false,//showToast
		//				false,//announceChat
		//				false)//hidden
		//		.addCriterion("worm", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ExampleItems.WORM.get()).build()))
		//		.save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));
	}
}
/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aondor.advancements;

import net.minecraft.advancements.Advancement;

import java.util.function.Consumer;

public class AonDorAdvancements implements Consumer<Consumer<Advancement>>
{
	public AonDorAdvancements()
	{
	}

	public void accept(Consumer<Advancement> advancementConsumer)
	{
		String tabName = "aondor";

		final String titleFormat = "advancements.aondor.%s.title";
		final String descriptionFormat = "advancements.aondor.%s.description";
		final String achievementPathFormat = "aondor:%s/%s";

		//Advancement root = Advancement.Builder.advancement()
		//		.display(AonDorItems.WORM.get(),
		//				Component.translatable(String.format(titleFormat, tabName)),
		//				Component.translatable(String.format(descriptionFormat, tabName)),
		//				new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
		//				FrameType.TASK,
		//				false,//showToast
		//				false,//announceChat
		//				false)//hidden
		//		.addCriterion("worm", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(AonDorItems.WORM.get()).build()))
		//		.save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));
	}
}
/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.awakening.advancements;

import net.minecraft.advancements.Advancement;

import java.util.function.Consumer;

public class AwakeningAdvancements implements Consumer<Consumer<Advancement>>
{
	public AwakeningAdvancements()
	{
	}

	public void accept(Consumer<Advancement> advancementConsumer)
	{
		String tabName = "awakening";

		final String titleFormat = "advancements.awakening.%s.title";
		final String descriptionFormat = "advancements.awakening.%s.description";
		final String achievementPathFormat = "awakening:%s/%s";

		//Advancement root = Advancement.Builder.advancement()
		//		.display(AwakeningItems.WORM.get(),
		//				Component.translatable(String.format(titleFormat, tabName)),
		//				Component.translatable(String.format(descriptionFormat, tabName)),
		//				new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
		//				FrameType.TASK,
		//				false,//showToast
		//				false,//announceChat
		//				false)//hidden
		//		.addCriterion("worm", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(AwakeningItems.WORM.get()).build()))
		//		.save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));
	}
}
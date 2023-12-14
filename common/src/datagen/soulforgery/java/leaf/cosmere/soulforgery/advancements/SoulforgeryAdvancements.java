/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.soulforgery.advancements;

import net.minecraft.advancements.Advancement;

import java.util.function.Consumer;

public class SoulforgeryAdvancements implements Consumer<Consumer<Advancement>>
{
	public SoulforgeryAdvancements()
	{
	}

	public void accept(Consumer<Advancement> advancementConsumer)
	{
		String tabName = "soulforgery";

		final String titleFormat = "advancements.soulforgery.%s.title";
		final String descriptionFormat = "advancements.soulforgery.%s.description";
		final String achievementPathFormat = "soulforgery:%s/%s";

		//Advancement root = Advancement.Builder.advancement()
		//		.display(SoulforgeryItems.WORM.get(),
		//				Component.translatable(String.format(titleFormat, tabName)),
		//				Component.translatable(String.format(descriptionFormat, tabName)),
		//				new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
		//				FrameType.TASK,
		//				false,//showToast
		//				false,//announceChat
		//				false)//hidden
		//		.addCriterion("worm", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(SoulforgeryItems.WORM.get()).build()))
		//		.save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));
	}
}